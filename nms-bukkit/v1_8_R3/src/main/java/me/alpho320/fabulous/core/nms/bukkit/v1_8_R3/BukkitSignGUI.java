package me.alpho320.fabulous.core.nms.bukkit.v1_8_R3;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import me.alpho320.fabulous.core.api.IOpenable;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignGUI;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignManager;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayInUpdateSign;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockChange;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenSignEditor;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.TileEntitySign;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftSign;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BukkitSignGUI extends SignGUI {

    public BukkitSignGUI(@NotNull SignManager manager) {
        super(manager);
    }

    @Override
    public @NotNull SignGUI open(@NotNull Object p, @NotNull SignType signType, @Nullable Consumer<String[]> whenOpen, @Nullable Consumer<String[]> whenClose) {
        Player player = (Player) p;
        PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;

        BlockPosition blockPosition = new BlockPosition(player.getLocation().getBlockX(), 1, player.getLocation().getBlockZ());
        PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(((CraftWorld) player.getWorld()).getHandle(), blockPosition);
        packet.block = CraftMagicNumbers.getBlock((Material) manager().core().getSignMaterial(signType)).getBlockData();
        
        playerConnection.sendPacket(packet);

        IChatBaseComponent[] components = CraftSign.sanitizeLines(lines());
        TileEntitySign sign = new TileEntitySign();
        sign.a(new BlockPosition(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ()));

        System.arraycopy(components, 0, sign.lines, 0, sign.lines.length);
        playerConnection.sendPacket(sign.getUpdatePacket());

        PacketPlayOutOpenSignEditor outOpenSignEditor = new PacketPlayOutOpenSignEditor(blockPosition);
        playerConnection.sendPacket(outOpenSignEditor);

        setChannelID(blockPosition + player.getName());
        if (whenOpen != null) whenOpen.accept(lines());

        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
                if (packet instanceof PacketPlayInUpdateSign) {
                    PacketPlayInUpdateSign packetSign = (PacketPlayInUpdateSign) packet;

                    Bukkit.getScheduler().runTask((Plugin) manager().core().plugin(), () -> {
                        BlockPosition position = packetSign.a();
                        String id = position + player.getName();

                        if (channelID() != null && channelID().equals(id)) {
                            Block block = player.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ());
                            block.setType(block.getType());

                            int m = 0;
                            String[] lines = new String[packetSign.b().length];
                            for (IChatBaseComponent line : packetSign.b()) {
                                lines[m] = line.getText();
                                m++;
                            }

                            if (whenClose != null) whenClose.accept(lines);
                            manager().remove(id());

                            Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
                            channel.eventLoop().submit(() -> {
                                channel.pipeline().remove("fsign_api_pipeline_channel_" + player.getName());
                                return null;
                            });
                        }
                    });
                }
                super.channelRead(ctx, packet);
            }
        };

        Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
        channel.eventLoop().submit(() -> {
            try {
                ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
                pipeline.addBefore("packet_handler", "fsign_api_pipeline_channel_" + player.getName(), channelDuplexHandler);
            } catch (Exception ignored) {}
            return null;
        });

        return this;
    }

}
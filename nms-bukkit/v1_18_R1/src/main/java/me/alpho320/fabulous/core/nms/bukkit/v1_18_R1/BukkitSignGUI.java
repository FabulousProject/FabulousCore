package me.alpho320.fabulous.core.nms.bukkit.v1_18_R1;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignGUI;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignManager;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayInUpdateSign;
import net.minecraft.network.protocol.game.PacketPlayOutBlockChange;
import net.minecraft.network.protocol.game.PacketPlayOutOpenSignEditor;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TileEntitySign;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_18_R1.block.CraftSign;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class BukkitSignGUI extends SignGUI {

    public BukkitSignGUI(@NotNull SignManager manager) {
        super(manager);
    }

    @Override
    public @NotNull SignGUI open(@NotNull Object p, @NotNull SignType signType, @Nullable Consumer<String[]> whenOpen, @Nullable Consumer<String[]> whenClose) {
        Player player = (Player) p;

        PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().b;
        BlockPosition blockPosition = new BlockPosition(player.getLocation().getBlockX(), 1, player.getLocation().getBlockZ());

        PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(blockPosition, Blocks.cg.n());
        playerConnection.a(packet);

        IChatBaseComponent[] components = CraftSign.sanitizeLines(lines());
        TileEntitySign sign = new TileEntitySign(new BlockPosition(blockPosition.u(), blockPosition.v(), blockPosition.w()), Blocks.cg.n());
        System.arraycopy(components, 0, sign.d, 0, sign.d.length);
        playerConnection.a(sign.c());

        PacketPlayOutOpenSignEditor outOpenSignEditor = new PacketPlayOutOpenSignEditor(blockPosition);
        playerConnection.a(outOpenSignEditor);

        setChannelID(blockPosition + player.getName());
        if (whenOpen != null) whenOpen.accept(lines());

        ChannelDuplexHandler channelHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
                if (packet instanceof PacketPlayInUpdateSign) {
                    PacketPlayInUpdateSign packetSign = (PacketPlayInUpdateSign) packet;

                    Bukkit.getScheduler().runTask((Plugin) manager().core().plugin(), () -> {
                        BlockPosition position = packetSign.b();
                        String id = position + player.getName();

                        if (channelID() != null && channelID().equals(id)) {
                            Block block = player.getWorld().getBlockAt(position.u(), position.v(), position.w());
                            block.setType(block.getType());

                            if (whenClose != null) whenClose.accept(packetSign.c());
                            manager().remove(id());

                            Channel channel = ((CraftPlayer) player).getHandle().b.a.k;
                            channel.eventLoop().submit(() -> {
                                channel.pipeline().remove("fsign_api_pipeline_channel_" + player.getName() + blockPosition);
                                return null;
                            });
                        }
                    });

                    super.channelRead(ctx, packet);
                }
            }
        };

        Callable<Object> callable = () -> {
            try {
                ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().b.a.k.pipeline();
                pipeline.addBefore("packet_handler", "fsign_api_pipeline_channel_" + player.getName() + blockPosition, channelHandler);
            } catch (Exception ignored) {}
            return null;
        };
        Channel channel = ((CraftPlayer) player).getHandle().b.a.k;
        channel.eventLoop().submit(callable);

        return this;
    }

}
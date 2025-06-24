package me.alpho320.fabulous.core.nms.bukkit.v1_20_R2;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import me.alpho320.fabulous.core.api.FCore;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignGUI;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignManager;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayInUpdateSign;
import net.minecraft.network.protocol.game.PacketPlayOutBlockChange;
import net.minecraft.network.protocol.game.PacketPlayOutOpenSignEditor;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.util.profiling.jfr.event.PacketEvent;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.entity.TileEntitySign;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_20_R2.block.CraftSign;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Consumer;


public class BukkitSignGUI extends SignGUI {

    public BukkitSignGUI(@NotNull SignManager manager) {
        super(manager);
    }
    
    public Material getMaterial() {
        if (type().equals(SignType.OAK)) return Material.OAK_SIGN;
        else if (type().equals(SignType.ACACIA)) return Material.ACACIA_SIGN;
        else if (type().equals(SignType.BIRCH)) return Material.BIRCH_SIGN;
        else if (type().equals(SignType.DARK_OAK)) return Material.DARK_OAK_SIGN;
        else if (type().equals(SignType.JUNGLE)) return Material.JUNGLE_SIGN;
        else if (type().equals(SignType.SPRUCE)) return Material.SPRUCE_SIGN;
        else if (type().equals(SignType.CRIMSON)) return Material.CRIMSON_SIGN;
        else return Material.OAK_SIGN;
    }

    @Override
    public @NotNull SignGUI open(@NotNull Object p, @NotNull SignType signType, @Nullable Consumer<String[]> whenOpen, @Nullable Consumer<String[]> whenClose) {
        Player player = (Player) p;
        PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().c;

        BlockPosition blockPosition = new BlockPosition(player.getLocation().getBlockX(), -64 + 1, player.getLocation().getBlockZ());
        IBlockData data = CraftMagicNumbers.getBlock(getMaterial()).n();

        playerConnection.b(new PacketPlayOutBlockChange(blockPosition, data));

        TileEntitySign sign = new TileEntitySign(blockPosition, null);
        SignText signText = sign.a(true);

        for (int i = 0; i < lines().length; i++) {
            signText = signText.a(i, IChatBaseComponent.a(lines()[i]));
        }

        sign.a(signText, true);
        playerConnection.b(sign.j());

        playerConnection.b(new PacketPlayOutOpenSignEditor(blockPosition, true));

        setChannelID(blockPosition + player.getName());
        if (whenOpen != null) whenOpen.accept(lines());

        try {
            NetworkManager networkManager = FCore.getField(playerConnection, ServerCommonPacketListenerImpl.class, "c");
            networkManager.n.pipeline().addBefore("packet_handler", blockPosition + player.getName(), new ChannelDuplexHandler() {
                @Override
                public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                    if (packet instanceof PacketPlayInUpdateSign) {
                        PacketPlayInUpdateSign packetSign = (PacketPlayInUpdateSign) packet;

                        BlockPosition position = packetSign.a();
                        Block block = player.getWorld().getBlockAt(position.u(), position.v(), position.w());
                        IBlockData data = CraftMagicNumbers.getBlock(block.getType()).n();

                        String[] b = packetSign.e();
                        String[] lines = new String[b.length];
                        System.arraycopy(b, 0, lines, 0, b.length);

                        Bukkit.getScheduler().runTask((Plugin) manager().core().plugin(), () -> {
                            String id = position + player.getName();

                            if (channelID() != null && channelID().equals(id)) {
                                //Block block = player.getWorld().getBlockAt(position.u(), position.v(), position.w());
                                //block.setType(block.getType());
                                playerConnection.b(new PacketPlayOutBlockChange(position, data));

                                if (whenClose != null)
                                    whenClose.accept(Arrays.copyOf(packetSign.e(), packetSign.e().length));
                                manager().remove(id());
                            }
                        });
                    }
                    super.channelRead(channelHandlerContext, packet);
                }

                @Override
                public void write(ChannelHandlerContext channelHandlerContext, Object o, ChannelPromise channelPromise) throws Exception {
                    super.write(channelHandlerContext, o, channelPromise);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

}
package me.alpho320.fabulous.core.nms.bukkit.v1_21_R1;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageToMessageDecoder;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignGUI;
import me.alpho320.fabulous.core.api.manager.impl.sign.SignManager;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayInUpdateSign;
import net.minecraft.network.protocol.game.PacketPlayOutOpenSignEditor;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.world.item.EnumColor;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.entity.TileEntitySign;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;

public class BukkitSignGUI extends SignGUI {

    public BukkitSignGUI(@NotNull SignManager manager) {
        super(manager);
    }

    private final Field NETWORK_MANAGER_FIELD;

    {
        Field networkManagerField = null;
        for (Field field : ServerCommonPacketListenerImpl.class.getDeclaredFields()) {
            if (field.getType() == NetworkManager.class) {
                field.setAccessible(true);
                networkManagerField = field;
                break;
            }
        }

        NETWORK_MANAGER_FIELD = networkManagerField;
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
    public @NotNull SignGUI open(@NotNull Object player, @NotNull SignType signType, @Nullable Consumer<String[]> whenOpen, @Nullable Consumer<String[]> whenClose) {
        try {
            EntityPlayer p = ((CraftPlayer) player).getHandle();
            PlayerConnection conn = p.c;

            if (NETWORK_MANAGER_FIELD == null)
                throw new IllegalStateException("Unable to find NetworkManager field in PlayerConnection class.");
            if (!NETWORK_MANAGER_FIELD.canAccess(conn)) {
                NETWORK_MANAGER_FIELD.setAccessible(true);
                if (!NETWORK_MANAGER_FIELD.canAccess(conn))
                    throw new IllegalStateException("Unable to access NetworkManager field in PlayerConnection class.");
            }

            Location loc = getDefaultLocation((Player) player);
            BlockPosition pos = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

            TileEntitySign sign = new TileEntitySign(pos, null);
            SignText signText = sign.a(true) // flag = front/back of sign
                    .a(EnumColor.a);
            for (int i = 0; i < lines().length; i++)
                signText = signText.a(i, IChatBaseComponent.a(lines()[i]));
            sign.a(signText, true);

            boolean schedule = false;
            NetworkManager manager = (NetworkManager) NETWORK_MANAGER_FIELD.get(conn);
            ChannelPipeline pipeline = manager.n.pipeline();
            if (pipeline.names().contains("SignGUI")) {
                ChannelHandler handler = pipeline.get("SignGUI");
                if (handler instanceof SignGUIChannelHandler<?> signGUIHandler) {
                    signGUIHandler.close();
                    schedule = signGUIHandler.getBlockPosition().equals(pos);
                }

                if (pipeline.names().contains("SignGUI"))
                    pipeline.remove("SignGUI");
            }

            Runnable runnable = () -> {
                ((CraftPlayer) player).sendBlockChange(loc, getMaterial().createBlockData());
                sign.a(p.dO());
                conn.b(sign.l());
                sign.a((World) null);
                conn.b(new PacketPlayOutOpenSignEditor(pos, true)); // flag = front/back of sign

                SignEditor signEditor = new SignEditor(sign, loc, pos, pipeline);
                pipeline.addAfter("decoder", "SignGUI", new SignGUIChannelHandler<Packet<?>>() {

                    @Override
                    protected void decode(ChannelHandlerContext channelHandlerContext, Packet<?> packet, List<Object> out) {
                        try {
                            if (packet instanceof PacketPlayInUpdateSign updateSign) {
                                if (updateSign.b().equals(pos))
                                    if (whenClose != null) whenClose.accept(updateSign.f());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        out.add(packet);
                    }

                    @Override
                    public Object getBlockPosition() {
                        return pos;
                    }

                    @Override
                    public void close() {
                        closeSignEditor((Player) player, signEditor);
                    }


                });
            };

            runnable.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public void closeSignEditor(Player player, SignEditor signEditor) {
        Location loc = signEditor.getLocation();
        signEditor.getPipeline().remove("SignGUI");
        player.sendBlockChange(loc, loc.getBlock().getBlockData());
    }

    private Location getDefaultLocation(Player player) {
        Location loc = player.getEyeLocation();
        return loc.clone().add(loc.getDirection().multiply(-3));
    }


    public class SignEditor {

        private final Object sign;
        private final Location location;
        private final Object blockPosition;
        private final ChannelPipeline pipeline;

        public SignEditor(Object sign, Location location, Object blockPosition, ChannelPipeline pipeline) {
            this.sign = sign;
            this.location = location;
            this.blockPosition = blockPosition;
            this.pipeline = pipeline;
        }

        public Object getSign() {
            return sign;
        }

        public Location getLocation() {
            return location;
        }

        public Object getBlockPosition() {
            return blockPosition;
        }

        public ChannelPipeline getPipeline() {
            return pipeline;
        }
    }

    public abstract class SignGUIChannelHandler<I> extends MessageToMessageDecoder<I> {

        public abstract Object getBlockPosition();

        public abstract void close();
    }

}
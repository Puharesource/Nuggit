package io.puharesource.mc.nuggit.player;

import io.puharesource.mc.nuggit.util.BasicNMSUtils;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

public class PlayerInfo {
    private final Player player;

    private Field pingField;

    private Object craftPlayer;
    private Object entityPlayer;
    private Method handle;

    public PlayerInfo(@NonNull final Player player) {
        this.player = player;

        try {
            Class<?> craftPlayerClass = BasicNMSUtils.getCraftbukkitClass("entity.CraftPlayer");
            craftPlayer = craftPlayerClass.cast(player);
            handle = craftPlayer.getClass().getMethod("getHandle");
            entityPlayer = handle.invoke(craftPlayer);
            pingField = entityPlayer.getClass().getField("ping");
        } catch (NoSuchMethodException | ClassNotFoundException | NoSuchFieldException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Object getCraftPlayer() {
        return craftPlayer;
    }

    public Object getEntityPlayer() {
        return entityPlayer;
    }

    public Method getHandle() {
        return handle;
    }

    public String getLocale() {
        return player.spigot().getLocale();
    }

    public InetSocketAddress getRawAddress() {
        return player.spigot().getRawAddress();
    }

    public void setFirstPlayed(long timePlayed) {
        try {
            craftPlayer.getClass().getMethod("setFirstPlayed").invoke(timePlayed);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public int getPing() {
        try {
            return pingField.getInt(entityPlayer);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return -1;
        }
    }
}

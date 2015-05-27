package io.puharesource.mc.nuggit.util;

import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

public final class BasicNMSUtils {

    private final static Pattern BRAND = Pattern.compile("(v|)[0-9][_.][0-9][_.][R0-9]*");
    private final static String CRAFTBUKKIT_PATH = "org.bukkit.craftbukkit.";
    private final static String NMS_PATH = "net.minecraft.server.";

    public static Class<?> getCraftbukkitClass(String path) throws ClassNotFoundException {
        return Class.forName(CRAFTBUKKIT_PATH + getServerVersion() + path);
    }

    public static Class<?> getCraftbukkitClassArray(String path) throws ClassNotFoundException {
        return Class.forName("[L" + CRAFTBUKKIT_PATH + getServerVersion() + path + ";");
    }

    public static Class<?> getNMSClass(String path) throws ClassNotFoundException {
        return Class.forName(NMS_PATH + getServerVersion() + path);
    }

    public static Class<?> getNMSClassArray(String path) throws ClassNotFoundException {
        return Class.forName("[L" + NMS_PATH + getServerVersion() + path + ";");
    }

    public static Object getPrivateField(Object object, String field) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field objectField = clazz.getDeclaredField(field);
        objectField.setAccessible(true);
        Object result = objectField.get(object);
        objectField.setAccessible(false);
        return result;
    }

    public static String getServerVersion() {
        String version;
        String pkg = Bukkit.getServer().getClass().getPackage().getName();
        String version0 = pkg.substring(pkg.lastIndexOf('.') + 1);
        if (!BRAND.matcher(version0).matches()) {
            version0 = "";
        }
        version = version0;
        return !"".equals(version) ? version + "." : "";
    }
}

package io.puharesource.mc.nuggit;

import io.puharesource.mc.nuggit.backend.config.ConfigMain;
import io.puharesource.mc.nuggit.backend.listeners.ListenerInventory;
import io.puharesource.mc.nuggit.config.Config;
import io.puharesource.mc.nuggit.config.ConfigSerializer;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

public final class Nuggit extends JavaPlugin {

    private static @Getter Nuggit instance;
    private @Getter ConfigMain mainConfig;

    @Override
    public void onEnable() {
        instance = this;

        final Config config = new Config(this, getDataFolder(), "config", false);
        try {
            mainConfig = ConfigSerializer.deserialize(ConfigMain.class, config.getFile());
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }


        final PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new ListenerInventory(), this);
    }
}

package io.puharesource.mc.nuggit.command;

import io.puharesource.mc.nuggit.backend.Log;
import io.puharesource.mc.nuggit.command.v1.RootCommandExecutor;
import io.puharesource.mc.nuggit.command.v1.SubCommandExecutor;
import io.puharesource.mc.nuggit.command.v2.*;
import io.puharesource.mc.nuggit.util.BasicNMSUtils;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public final class CommandManager {
    public static void registerCommands(@NonNull final CommandContainer container) {
        final Class<? extends CommandContainer> clazz = container.getClass();

        for (final Method method : clazz.getMethods()) {
            if (method.getParameterCount() != 3) continue;
            final Class<?>[] params = method.getParameterTypes();

            if (params[0] != String.class || params[1] != String[].class || params[2] != CommandSender.class) continue;

            if (method.isAnnotationPresent(RootCommand.class)) {
                final RootCommand cmd = method.getAnnotation(RootCommand.class);
                final RootCommandExecutor executor = new AnnotationRootCommandExecutor(cmd, method, container);
                register(executor);
            } else if (method.isAnnotationPresent(SubCommand.class)) {
                final SubCommand cmd = method.getAnnotation(SubCommand.class);
                final SubCommandExecutor executor = new AnnotationSubCommandExecutor(cmd, method, container);

                final PluginCommand parentCommand = getCommand(cmd.name(), null);

                if (parentCommand == null) continue;
                final CommandExecutor parent = parentCommand.getExecutor();

                if (parent instanceof RootCommandExecutor) {
                    ((RootCommandExecutor) parent).addSubCommand(executor);
                }
            }
        }
    }

    public static void register(@NonNull final RootCommandExecutor executor) {
        unregisterSilently(executor.getName().toLowerCase());
        executor.getAliases().forEach((alias) -> unregisterSilently(alias.toLowerCase().trim()));
        if (registerAttempt(executor))
            Log.info("Registered command \"" + executor.getName().toLowerCase() + "\".");
        else Log.warning("Failed to register command \"" + executor.getName().toLowerCase() + "\".");
    }

    public static void unregister(@NonNull final String cmdName) {
        unregister(getCommand(cmdName, null));
    }

    public static void unregisterSilently(@NonNull final String name) {
        unregisterSilently(getCommand(name, null));
    }

    public static void unregister(@NonNull final Command command) {
        if (command != null) {
            try {
                final Map<String, Command> knownCommands = getKnownCommandMap();
                knownCommands.remove(command.getName());
                command.getAliases().forEach(knownCommands::remove);
                Log.info("Unregistered command \"" + command.getName() + "\".");
            } catch (SecurityException | IllegalArgumentException | NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                Log.warning("Failed to unregister command!");
            }
        } else Log.warning("Failed to unregister command!");
    }

    public static void unregisterSilently(@NonNull final Command command) {
        if (command != null) {
            try {
                final Map<String, Command> knownCommands = getKnownCommandMap();
                knownCommands.remove(command.getName());
                command.getAliases().forEach(knownCommands::remove);
            } catch (SecurityException | IllegalArgumentException | NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean registerAttempt(@NonNull final RootCommandExecutor cmd) {
        PluginCommand pluginCmd = cmd.getPlugin().getCommand(cmd.getName().toLowerCase());
        if (pluginCmd != null) return false;

        pluginCmd = getCommand(cmd.getName(), cmd.getPlugin());
        pluginCmd.setPermission(cmd.getPermission());
        pluginCmd.setAliases(cmd.getAliases());
        pluginCmd.setDescription(cmd.getDescription());
        pluginCmd.setPermissionMessage(ChatColor.RED + "You do not have permission to use that command!");
        try {
            getCommandMap().register(cmd.getPlugin().getDescription().getName(), pluginCmd);
            pluginCmd.setExecutor(cmd);
            pluginCmd.setTabCompleter(cmd);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static PluginCommand getCommand(@NonNull final String name, final Plugin plugin) {
        PluginCommand command = null;
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);

            command = constructor.newInstance(name, plugin);
        } catch (SecurityException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        return command;
    }

    private static CommandMap getCommandMap() {
        CommandMap commandMap = null;
        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                final Field f = SimplePluginManager.class.getDeclaredField("commandMap");
                f.setAccessible(true);

                commandMap = (CommandMap) f.get(Bukkit.getPluginManager());
            }
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
            e.printStackTrace();
        }

        return commandMap;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Command> getKnownCommandMap() throws NoSuchFieldException, IllegalAccessException {
        return (Map<String, Command>) BasicNMSUtils.getPrivateField(BasicNMSUtils.getPrivateField(Bukkit.getServer().getPluginManager(), "commandMap"), "knownCommands");
    }
}
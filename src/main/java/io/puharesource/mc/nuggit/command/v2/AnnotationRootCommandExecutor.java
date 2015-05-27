package io.puharesource.mc.nuggit.command.v2;

import io.puharesource.mc.nuggit.command.v1.RootCommandExecutor;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class AnnotationRootCommandExecutor extends RootCommandExecutor {
    private final Method execute;
    private final CommandContainer container;

    public AnnotationRootCommandExecutor(@NonNull final RootCommand cmd, @NonNull final Method execute, @NonNull final CommandContainer container) {
        super((JavaPlugin) Bukkit.getPluginManager().getPlugin(cmd.name()), cmd.name(), cmd.permission(), cmd.allowedSender(), cmd.usage(), cmd.description(), cmd.executionType(), cmd.errorOnWrongSub(), cmd.aliases());
        this.execute = execute;
        this.container = container;
    }

    @Override
    public void execute(String cmdName, String[] args, CommandSender sender) {
        try {
            execute.invoke(container, cmdName, args, sender);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

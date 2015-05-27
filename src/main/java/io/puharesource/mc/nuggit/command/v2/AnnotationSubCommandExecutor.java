package io.puharesource.mc.nuggit.command.v2;

import io.puharesource.mc.nuggit.command.v1.SubCommandExecutor;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class AnnotationSubCommandExecutor extends SubCommandExecutor {
    private final Method execute;
    private final CommandContainer container;

    public AnnotationSubCommandExecutor(@NonNull final SubCommand cmd, @NonNull final Method execute, @NonNull final CommandContainer container) {
        super(cmd.name(), cmd.permission(), cmd.allowedSender(), cmd.usage(), cmd.description(), cmd.executionType(), cmd.aliases());
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

package io.puharesource.mc.nuggit.command.v1;

import io.puharesource.mc.nuggit.command.AllowedSender;
import io.puharesource.mc.nuggit.command.ExecutionType;
import lombok.Getter;
import org.bukkit.command.CommandSender;

public abstract class SubCommandExecutor {
    private @Getter final String name;
    private @Getter final String permission;
    private @Getter final AllowedSender allowedSender;
    private @Getter final String usage;
    private @Getter final String description;
    private @Getter final ExecutionType executionType;
    private @Getter final String[] aliases;

    public SubCommandExecutor(String name, String permission, AllowedSender allowedSender, String usage, String description, ExecutionType executionType, String... aliases) {
        this.name = name.toUpperCase().trim();
        this.permission = permission.toLowerCase().trim();
        this.allowedSender = allowedSender;
        this.usage = usage;
        this.description = description;
        this.executionType = executionType;
        this.aliases = aliases;
    }

    public abstract void execute(String cmdName, String[] args, CommandSender sender);
}

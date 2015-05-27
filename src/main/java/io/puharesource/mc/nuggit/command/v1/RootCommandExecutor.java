package io.puharesource.mc.nuggit.command.v1;

import io.puharesource.mc.nuggit.command.AllowedSender;
import io.puharesource.mc.nuggit.command.ExecutionType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

public abstract class RootCommandExecutor implements CommandExecutor, TabCompleter {
    private @Getter final JavaPlugin plugin;
    private @Getter final String name;
    private @Getter final String permission;
    private @Getter final AllowedSender allowedSender;
    private @Getter final String usage;
    private @Getter final String description;
    private @Getter final ExecutionType executionType;
    private final boolean errorOnWrongSub;
    private @Getter final List<String> aliases;

    private Map<String, SubCommandExecutor> subCommands = new HashMap<>();

    public RootCommandExecutor(JavaPlugin plugin, String name, String permission, AllowedSender allowedSender, String usage, String description, ExecutionType executionType, boolean errorOnWrongSub, String... aliases) {
        this.plugin = plugin;
        this.name = name.toUpperCase().trim();
        this.permission = permission.toLowerCase().trim();
        this.allowedSender = allowedSender;
        this.usage = usage;
        this.description = description;
        this.executionType = executionType;
        this.errorOnWrongSub = errorOnWrongSub;
        this.aliases = Arrays.asList(aliases);
    }

    public abstract void execute(String cmdName, String[] args, CommandSender sender);

    public void addSubCommand(SubCommandExecutor subCommand) {
        subCommands.put(subCommand.getName(), subCommand);
        for (String alias : subCommand.getAliases())
            subCommands.put(alias.toUpperCase().trim(), subCommand);
    }

    private void commandList(CommandSender sender, ChatColor primaryColor, ChatColor secondaryColor) {
        if (!(usage.isEmpty() && description.isEmpty()))
            sender.sendMessage(primaryColor + "    /" + name.toLowerCase() + " " + usage + secondaryColor + " - " + description);
        List<String> alreadyShown = new ArrayList<>();
        for (SubCommandExecutor cmd : subCommands.values()) {
            if (alreadyShown.contains(cmd.getName())) continue;
            alreadyShown.add(cmd.getName());
            sender.sendMessage(primaryColor + "    /" + name.toLowerCase() + " " + cmd.getName().toLowerCase() + " " + usage + secondaryColor + " - " + description);
        }
    }

    public void syntaxError(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Wrong usage! Correct usage" + ((subCommands.size() > 1 || (subCommands.size() == 1 && !usage.isEmpty() && !description.isEmpty())) ? "s:" : ":"));
        commandList(sender, ChatColor.RED, ChatColor.GRAY);
    }

    public void helpMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY + "\"" + ChatColor.GREEN + name + ChatColor.GRAY + "\"" + ChatColor.GREEN + " Help");
        commandList(sender, ChatColor.GREEN, ChatColor.GRAY);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!cmd.getName().equalsIgnoreCase(name)) return false;
        if (!permission.isEmpty() && !sender.hasPermission(permission)) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
            return true;
        }
        if (args.length > 0 && errorOnWrongSub) {
            final SubCommandExecutor subCommand = subCommands.get(args[0].toUpperCase().trim());
            if (subCommand != null) {
                if (subCommand.getAllowedSender() == AllowedSender.CONSOLE && !(sender instanceof ConsoleCommandSender)) {
                    sender.sendMessage(ChatColor.RED + "This command can only be run from the console!");
                    return true;
                } else if (subCommand.getAllowedSender() == AllowedSender.PLAYER && !(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command can only be run as a player!");
                    return true;
                } else if (subCommand.getAllowedSender() == AllowedSender.COMMAND_BLOCK && !(sender instanceof CommandBlock)) {
                    sender.sendMessage(ChatColor.RED + "This command can only be run through a command block!");
                    return true;
                }

                if (!subCommand.getPermission().isEmpty() && !sender.hasPermission(subCommand.getPermission())) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                    return true;
                }

                if (subCommand.getExecutionType() == ExecutionType.ASYNC) {
                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> subCommand.execute(args[0].toLowerCase(), Arrays.copyOfRange(args, 1, args.length), sender));
                } else subCommand.execute(args[0].toLowerCase(), Arrays.copyOfRange(args, 1, args.length), sender);
            } else syntaxError(sender);
        } else if (args.length > 0) {
            final SubCommandExecutor subCommand = subCommands.get(args[0].toUpperCase().trim());
            if (subCommand != null) {

                if (!subCommand.getPermission().isEmpty() && !sender.hasPermission(subCommand.getPermission())) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                    return true;
                }

                if (subCommand.getExecutionType() == ExecutionType.ASYNC) {
                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> subCommand.execute(args[0].toLowerCase(), Arrays.copyOfRange(args, 1, args.length), sender));
                } else subCommand.execute(args[0].toLowerCase(), Arrays.copyOfRange(args, 1, args.length), sender);
                return true;
            } else {
                if (executionType == ExecutionType.ASYNC) {
                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> execute(cmd.getName().toLowerCase(), args, sender));
                } else execute(cmd.getName().toLowerCase(), args, sender);
            }
        } else {
            if (sender instanceof Player && allowedSender == AllowedSender.CONSOLE) {
                sender.sendMessage(ChatColor.RED + "This command can only be run from the console!");
                return true;
            }
            if (sender instanceof ConsoleCommandSender && allowedSender == AllowedSender.PLAYER) {
                sender.sendMessage(ChatColor.RED + "This command can only be run as a player!");
                return true;
            }
            if (executionType == ExecutionType.ASYNC) {
                Runnable runnable = () -> execute(cmd.getName().toLowerCase(), args, sender);
                Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
            } else execute(cmd.getName().toLowerCase(), args, sender);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final List<String> possibilities = new ArrayList<>();

        if (args.length == 1)
            possibilities.addAll(subCommands.keySet().stream().filter(sub -> sub.toLowerCase().startsWith(args[0].toLowerCase())).map(String::toLowerCase).collect(Collectors.toList()));

        return possibilities.size() <= 0 ? null : possibilities;
    }
}

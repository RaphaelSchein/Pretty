package xyz.raphaelscheinkoenig.pretty.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.util.Util;

public abstract class Command implements CommandExecutor {

    private String command;
    private String[] alias;
    private List<Command> subCommandList;

    public Command(String command) {
        this(command, new String[] { });
    }

    public Command(String command, String... alias) {
    	Objects.requireNonNull(alias);
        this.command = command;
        this.alias = alias;
        this.subCommandList = new ArrayList<Command>();
    }

    public abstract boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args);

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (command == null || command.getName().equalsIgnoreCase(this.command)) {
            if (args.length > 0) {
                for (String arg : args) {
                    for (Command subCommand : subCommandList) {
                        if (subCommand.getCommandName().equalsIgnoreCase(arg) || Arrays.asList(subCommand.getAlias()).contains(arg)) {
                            String[] newArgs = new String[args.length - 1];
                            for (int i = 1; i < args.length; i++) newArgs[i - 1] = args[i];
                            return subCommand.onCommand(sender, null, arg, newArgs);
                        }
                    }
                }
                return this.execute(sender, command, args);
            } else {
                return this.execute(sender, command, args);
            }
        }
        return false;
    }

    public String getCommandName() {
        return this.command;
    }

    public String[] getAlias() {
        return this.alias;
    }

    public boolean addSubCommand(Command subCommand) {
        if (subCommandList.contains(subCommand))
            return false;
        for (Command sc : subCommandList)
            if (sc.getCommandName().equalsIgnoreCase(subCommand.getCommandName()))
                return false;
        subCommandList.add(subCommand);
        return true;
    }

    public void noPermissions(Player p){
    	p.sendMessage(Util.translate(ConfigManager.getConfig("Commands").getString("messages.no_permissions")));
    }
    
}

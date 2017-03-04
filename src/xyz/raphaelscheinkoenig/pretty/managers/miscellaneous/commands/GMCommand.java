package xyz.raphaelscheinkoenig.pretty.managers.miscellaneous.commands;

import xyz.raphaelscheinkoenig.pretty.commands.Command;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;


public class GMCommand extends Command {

    public GMCommand() {
        super("gm");
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(ChatColor.GREEN + "Correct usage: /gm <id>");
        } else {
            String argsString = StringUtils.join(args, " ");
            Bukkit.dispatchCommand(sender, "gamemode " + argsString);
        }
        return true;
    }

//    private GameMode matchGameMode(String modeString)
//    {
//        GameMode mode = null;
//        if (modeString.equalsIgnoreCase("gmc") || modeString.equalsIgnoreCase("egmc")
//                || modeString.contains("creat") || modeString.equalsIgnoreCase("1") || modeString.equalsIgnoreCase("c"))
//        {
//            mode = GameMode.CREATIVE;
//        }
//        else if (modeString.equalsIgnoreCase("gms") || modeString.equalsIgnoreCase("egms")
//                || modeString.contains("survi") || modeString.equalsIgnoreCase("0") || modeString.equalsIgnoreCase("s"))
//        {
//            mode = GameMode.SURVIVAL;
//        }
//        else if (modeString.equalsIgnoreCase("gma") || modeString.equalsIgnoreCase("egma")
//                || modeString.contains("advent") || modeString.equalsIgnoreCase("2") || modeString.equalsIgnoreCase("a"))
//        {
//            mode = GameMode.ADVENTURE;
//        }
//        else if (modeString.equalsIgnoreCase("gmt") || modeString.equalsIgnoreCase("egmt")
//                || modeString.contains("toggle") || modeString.contains("cycle") || modeString.equalsIgnoreCase("t"))
//        return mode;
//    }

}

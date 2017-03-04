package xyz.raphaelscheinkoenig.pretty.managers.miscellaneous.commands;

import xyz.raphaelscheinkoenig.pretty.commands.ArgumentParser;
import xyz.raphaelscheinkoenig.pretty.managers.home.HomeManager;
import xyz.raphaelscheinkoenig.pretty.util.Purchases;
import xyz.raphaelscheinkoenig.pretty.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class PurchaseCommand implements CommandExecutor {

    public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        Bukkit.broadcastMessage("I got performed!");
        if(sender instanceof Player) {
            int powerLevel = Util.getPower((Player) sender);
            if(powerLevel < 640)
                return true;
        }
        ArgumentParser ap = new ArgumentParser(args);
        if(ap.hasExactly(2)) {
            String uuid = ap.get(1);
            int buyId = ap.getInt(2);
            Purchases pur = Purchases.getPurchase(buyId);

            if(pur == null) {
                sender.sendMessage(ChatColor.RED + "Buy id " + buyId + " does not exist.");
                return true;
            }

            switch (pur) {
                case TWO_HOMES:
                    HomeManager.instance().giveHomes(uuid, 2);
                    break;
                case FIVE_HOMES:
                    HomeManager.instance().giveHomes(uuid, 5);
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "Buy id " + buyId + " does not exist.");

            }
        } else {
            sender.sendMessage(ChatColor.RED + "PROBLEM! Wrong argument number should be 2.");
        }
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return execute(sender, command, label, args);
    }
}

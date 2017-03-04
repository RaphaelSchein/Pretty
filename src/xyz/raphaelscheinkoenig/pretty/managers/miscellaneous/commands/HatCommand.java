package xyz.raphaelscheinkoenig.pretty.managers.miscellaneous.commands;

import xyz.raphaelscheinkoenig.pretty.commands.Command;
import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.util.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class HatCommand extends Command {

    public HatCommand() {
        super("hat");
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
        if(sender instanceof Player){

            Player p = (Player) sender;
            if(Util.getPower(p) >= 128){
                ItemStack head = p.getInventory().getHelmet();
                ItemStack hand = p.getInventory().getItemInMainHand();
                p.getInventory().setHelmet(hand);
                p.getInventory().setItemInMainHand(head);
                p.sendMessage(Util.translate(ConfigManager.getConfig("Misc").getString("messages.you_changed_hat")));
            } else {
                p.sendMessage(Util.translate(ConfigManager.getConfig("Misc").getString("messages.you_cannot_chance_head")));
            }
        }
        return true;
    }
}

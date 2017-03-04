package xyz.raphaelscheinkoenig.pretty.managers.miscellaneous.commands;

import xyz.raphaelscheinkoenig.pretty.commands.Command;
import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


public class CCCommand extends Command {

    public CCCommand() {
        super("cc");
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            int power = Util.getPower(p);
            if(power < 512) {
                noPermissions(p);
                return true;
            }
        }
        FileConfiguration conf = ConfigManager.getConfig("Misc");
        String message = conf.getString("someone_cleared_chat");
        int spams = conf.getInt("cc.messages");
        message = Util.translate(message.replace("%name%",  sender.getName()));
        for(int i = 0; i < spams; i++)
            Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(message);
        return true;
    }

}

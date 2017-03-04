package xyz.raphaelscheinkoenig.pretty.managers.miscellaneous.commands;

import xyz.raphaelscheinkoenig.pretty.commands.Command;
import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.managers.teleport.teleportation.CountedTeleportation;
import xyz.raphaelscheinkoenig.pretty.util.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


public class EflyCommand extends Command {

    public EflyCommand() {
        super("efly");
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            int power = Util.getPower(p);
            if(power < 192) {
                noPermissions(p);
                return true;
            }
            FileConfiguration conf = ConfigManager.getConfig("Misc");
            String name = conf.getString("efly.destination_name");
            int delay = conf.getInt("efly.time_delay_in_seconds");
            System.out.println("name:" + name + " delay:" + delay);
            new CountedTeleportation(p, p.getLocation().add(0, 1000, 0), name, delay) {
                @Override
                public void onTeleportDone() {
                    p.setGliding(true);
                }
            }.proceed();
//            tp.proceed();
        }
        return true;
    }
}

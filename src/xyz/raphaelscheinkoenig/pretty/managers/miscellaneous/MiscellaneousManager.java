package xyz.raphaelscheinkoenig.pretty.managers.miscellaneous;

import xyz.raphaelscheinkoenig.pretty.managers.invsee.commands.EnderchestCommand;
import xyz.raphaelscheinkoenig.pretty.managers.invsee.commands.InvseeCommand;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import xyz.raphaelscheinkoenig.pretty.commands.Command;
import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.managers.Manager;
import xyz.raphaelscheinkoenig.pretty.managers.miscellaneous.commands.*;

public class MiscellaneousManager extends Manager {

	public static final String NAME = "Miscellaneous";
	
	@Override
	public void onEnable() {
		initCommands();
		initConfig();
	}
	
	private void initConfig() {
		FileConfiguration conf = ConfigManager.getConfig("Misc");
		conf.addDefault("messages.flying_enabled", "You are allowed to fly now");
		conf.addDefault("messages.flying_disabled", "You aren't allowed to fly anymore");
		conf.addDefault("messages.player_not_online", "The player %name% is not online!");
		conf.addDefault("messages.you_cannot_open_other_inventories", "You don't have rights to open others inventory. Purchase it bla bla");
		conf.addDefault("messages.you_changed_hat", "You changed your head item!");
        conf.addDefault("messages.you_cannot_chance_head", "You don't have enough permissions. buy it amk!");
        conf.addDefault("efly.destination_name", "1000 blocks above you");
        conf.addDefault("efly.time_delay_in_seconds", 5);
        conf.addDefault("cc.someone_cleared_chat", "%name% has cleared the chat.");
        conf.addDefault("cc.messages", 50);
		conf.options().copyDefaults(true);
		ConfigManager.saveConfig("Misc");
	}

	private void initCommands() {
		Command[] cmds = {
		        new FlyCommand(),
                new InvseeCommand(),
                new EnderchestCommand(),
                new HatCommand(),
                new EflyCommand(),
                new GMCommand(),
                new CCCommand()
		};
		for(Command cmd : cmds)
			registerCommand(cmd);
        Bukkit.getPluginCommand("shop").setExecutor(new PurchaseCommand());
    }

	@Override
	public String getName() {
		return NAME;
	}

}

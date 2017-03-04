package xyz.raphaelscheinkoenig.pretty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import xyz.raphaelscheinkoenig.pretty.item.ItemManager;
import xyz.raphaelscheinkoenig.pretty.managers.chat.ChatManager;
import xyz.raphaelscheinkoenig.pretty.managers.clan.ClanManager;
import xyz.raphaelscheinkoenig.pretty.managers.guis.GuiManager;
import xyz.raphaelscheinkoenig.pretty.managers.kits.KitManager;
import xyz.raphaelscheinkoenig.pretty.managers.scoreboard.MyScoreboardManager;
import xyz.raphaelscheinkoenig.pretty.pvp.PvPManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.raphaelscheinkoenig.pretty.commands.Command;
import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.managers.Manager;
import xyz.raphaelscheinkoenig.pretty.managers.home.HomeManager;
import xyz.raphaelscheinkoenig.pretty.managers.miscellaneous.MiscellaneousManager;
import xyz.raphaelscheinkoenig.pretty.managers.regions.RegionManager;
import xyz.raphaelscheinkoenig.pretty.managers.teleport.TeleportManager;
import xyz.raphaelscheinkoenig.pretty.managers.user.UserManager;
import xyz.raphaelscheinkoenig.pretty.managers.vanish.VanishManager;
import xyz.raphaelscheinkoenig.pretty.util.sql.AsyncMySQL;



public class LamaCore extends JavaPlugin implements Listener {

	private static final String DB_NAME = "LamaCore";
	private boolean enabled = false;
	private List<Command> commands;
	private List<Manager> managers;
	private static LamaCore instance;
	private AsyncMySQL sql;
	
	public void onEnable(){
		instance = this;
		boolean established = false;
		try {
			established = initSql();
		} catch(Exception ex){
			System.out.println();
			System.err.println("Could not start plugin because MySQL connection could not be established.");
			System.out.println();
			ex.printStackTrace();
		}
		if(!established)
			return;
		initCommands();
		initManagers();
		initConfigs();
		Bukkit.getPluginManager().registerEvents(this, this);
		enabled = true;
	}
	
	private void initConfigs() {
		FileConfiguration conf = ConfigManager.getConfig("Commands");
		conf.addDefault("messages.no_permissions", "&cYou don't have any permissions to execute this command");
		conf.addDefault("spawn.no_spawn_was_set", "You cannot teleport to spawn because there isn't one");
		conf.addDefault("spawn.name", "&6Spawn");
		ConfigManager.saveConfig("Commands");
	}

	private boolean initSql() throws Exception {
		FileConfiguration conf = ConfigManager.getConfig("config");
		conf.addDefault("mysql.host", "localhost");
		conf.addDefault("mysql.user", "root");
		conf.addDefault("mysql.password", "password");
		conf.addDefault("mysql.port", 3306);
		conf.options().copyDefaults(true);
		ConfigManager.saveConfig("config");
		String host = conf.getString("mysql.host");
		String user = conf.getString("mysql.user");
		String pw = conf.getString("mysql.password");
		int port = conf.getInt("mysql.port");
		sql = new AsyncMySQL(this, host, port, user, pw, DB_NAME);
		sql.getMySQL().openConnection();
		return true;
	}

	@Override
	public void onDisable() {
		if(!enabled)
			return;
		Iterator<Manager> iterator = managers.iterator();
		while(iterator.hasNext()){
			Manager m = iterator.next();
			try {
				m.onDisable();
			} catch (Exception ex) {
				System.err.println("Could not disable Manager: " + m.getName());
			}
		}
	}
	
	private void initManagers() {
		this.managers = new ArrayList<>();
		Manager[] managers = {
		        new ItemManager(),
				new RegionManager(),
				new UserManager(),
				new TeleportManager(),
				new HomeManager(),
				new MiscellaneousManager(),
//				new InvseeManager(),
				new GuiManager(),
				new VanishManager(),
                new ClanManager(),
                new KitManager(),
				new MyScoreboardManager(),
                new ChatManager(),
				new PvPManager()
		};
		for(Manager m : managers){
			try {
				m.onEnable();
				System.out.println("Registered:" + m.getName());
				this.managers.add(m);
			} catch(Exception ex){
				System.err.println("Could not load Manager: " + m.getName());
				ex.printStackTrace();
			}
		}
	}

	public static LamaCore instance(){
		return instance;
	}
	
	public AsyncMySQL sql(){
		return sql;
	}

	private void initCommands() {
		commands = new ArrayList<>();
	}
	
	public boolean registerCommand(Command command) {
		if(command == null)
			return false;
		String commandName = command.getCommandName();
		String[] commandAlias = command.getAlias();

		if (commands.contains(command))
			return false;

		commands.add(command);
		getCommand(commandName).setExecutor(command);
		getCommand(commandName).setAliases(Arrays.asList(commandAlias));
		return true;
	}

	public void registerListener(Manager manager, Listener l) {
		Bukkit.getPluginManager().registerEvents(l, this);
	}
	
}

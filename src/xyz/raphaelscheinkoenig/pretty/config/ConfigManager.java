package xyz.raphaelscheinkoenig.pretty.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import xyz.raphaelscheinkoenig.pretty.LamaCore;


public final class ConfigManager {

	private static HashMap<String, FileConfiguration> configurations = new HashMap<String, FileConfiguration>();

	public static boolean saveConfig(String name) {
		if (!configurations.containsKey(name)) {
			return false;
		}
		FileConfiguration conf = configurations.get(name);
		File cFile = new File(LamaCore.instance().getDataFolder(), name + ".yml");
		try {
			cFile.createNewFile();
			conf.save(cFile);
			return true;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static FileConfiguration getConfig(String name) {
		if (configurations.containsKey(name)) {
			return configurations.get(name);
		}
		LamaCore.instance().getDataFolder().mkdirs();
		File cFile = new File(LamaCore.instance().getDataFolder(), name + ".yml");
		try {
			cFile.createNewFile();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		FileConfiguration conf = YamlConfiguration.loadConfiguration(cFile);
		configurations.put(name, conf);
		return conf;
	}
	
}

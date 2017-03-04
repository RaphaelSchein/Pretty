package xyz.raphaelscheinkoenig.pretty.managers.vanish;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.managers.Manager;
import xyz.raphaelscheinkoenig.pretty.managers.vanish.commands.VanishCommand;
import xyz.raphaelscheinkoenig.pretty.managers.vanish.listeners.VanishPlayerQuitJoinListener;
import xyz.raphaelscheinkoenig.pretty.util.Util;

public class VanishManager extends Manager {

	private Set<String> vanishedPlayersUuid;
	private Set<Player> vanishedPlayers;
	private static VanishManager instance;
	public static final String NAME = "Vanish";
	
	public void onEnable(){
		instance = this;
		vanishedPlayers = new HashSet<>();
		vanishedPlayersUuid = new HashSet<>();
		initConfig();
		registerListeners();
		registerCommands();
	}
	
	private void initConfig() {
		FileConfiguration conf = ConfigManager.getConfig("Vanish");
		conf.addDefault("vanish.you_were_vanished", "You were vanished");
		conf.addDefault("vanish.you_were_unvanished", "You were unvanished");
		conf.options().copyDefaults(true);
		ConfigManager.saveConfig("Vanish");
	}

	private void registerCommands() {
		registerCommand(new VanishCommand());
	}

	private void registerListeners() {
		Listener[] listeners = {
			new VanishPlayerQuitJoinListener()
		};
		for(Listener l : listeners)
			registerListener(l);
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	public void hidePlayersFor(Player p){
		Set<Player> vanished = Collections.synchronizedSet(vanishedPlayers);
		synchronized (vanished) {
			if(!canSeeVanishedPlayer(p)){
				for(Player van : vanished)
					p.hidePlayer(van);
			}
		}
	}
	
	public void vanishPlayer(Player p){
		Set<Player> vanished = Collections.synchronizedSet(vanishedPlayers);
		synchronized (vanished) {
			vanished.add(p);
			vanishedPlayersUuid.add(Util.uuid(p));
		}
		for(Player vanishFor : Bukkit.getOnlinePlayers())
			hidePlayersFor(vanishFor);
	}
	
	public void unvanishPlayer(Player p){
		Set<Player> vanished = Collections.synchronizedSet(vanishedPlayers);
		synchronized (vanished) {
			vanished.remove(p);
			vanishedPlayersUuid.remove(Util.uuid(p));
		}
		for(Player o : Bukkit.getOnlinePlayers())
			o.showPlayer(p);
	}
	
	public boolean isVanished(Player p){
		Set<String> uuids = Collections.synchronizedSet(vanishedPlayersUuid);
		synchronized (uuids) {
			if(uuids.contains(Util.uuid(p)))
				return true;
		}
		return false;
	}
	
	public boolean canVanish(Player p){
		return Util.getPower(p) >= 512;
	}
	
	public boolean canSeeVanishedPlayer(Player p){
		return false;
//		return Util.getPower(p) >= 10;
	}
	
	public static VanishManager instance(){
		return instance;
	}


	public void removePlayer(Player p) {
		Set<Player> syncSet = Collections.synchronizedSet(vanishedPlayers);
		synchronized (syncSet) {
			syncSet.remove(p);
		}
	}

}

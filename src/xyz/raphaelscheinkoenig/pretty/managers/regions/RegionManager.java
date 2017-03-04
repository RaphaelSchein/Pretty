package xyz.raphaelscheinkoenig.pretty.managers.regions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import xyz.raphaelscheinkoenig.pretty.LamaCore;
import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.managers.Manager;
import xyz.raphaelscheinkoenig.pretty.managers.regions.listeners.FlagListener;
import xyz.raphaelscheinkoenig.pretty.managers.regions.listeners.RegionListener;
import xyz.raphaelscheinkoenig.pretty.managers.regions.types.AnyRegion;
import xyz.raphaelscheinkoenig.pretty.managers.regions.types.Region;
import xyz.raphaelscheinkoenig.pretty.util.SerializableLocation;
import xyz.raphaelscheinkoenig.pretty.util.Util;

public class RegionManager extends Manager {

	public static final String FILE_NAME = "mylogo_regions.regions";
	private File regionsFile;
	public static final String NAME = "Region";
	private static RegionManager instance;
	
	private CopyOnWriteArrayList<Region> regions;
//	private CopyOnWriteArrayList<Class<? extends Region>> notToSave;
	private ConcurrentHashMap<String, Region> currentRegion;
	
	public void onEnable(){
		instance = this;
		regions = new CopyOnWriteArrayList<>();
//		notToSave = new CopyOnWriteArrayList<>();
		currentRegion = new ConcurrentHashMap<>();
		initConfig();
		initListeners();
		loadRegions();
		initUsers();
	}
	
	private void initUsers() {
		for(Player p : Bukkit.getOnlinePlayers()){
			setRegion(p, Region.ANY);
		}
	}

	public void onDisable(){
		saveRegions();
	}
	
	@SuppressWarnings("unchecked")
	private void loadRegions() {
//		notToSave.add(AnyRegion.class);
		regionsFile = new File(LamaCore.instance().getDataFolder(), FILE_NAME);
		if(regionsFile.exists()){
			try {
				List<Region> regions = (ArrayList<Region>) Util.loadArrayList(regionsFile);
				if(regions != null)
					this.regions = new CopyOnWriteArrayList<>(regions);
				System.out.println("this.regions.size()=" + this.regions.size());
			} catch(Exception e){
				System.err.println("Error while reading region file!");
			}
		}
		this.regions.forEach(r -> System.out.println(r.getRegions().size()));
		this.regions.add(Region.ANY);
	}
	
//	public void dontSave(Region r){
//		notToSave.add(clazz);
//	}
	
	private boolean shallSave(Region r){
//		for(Class<? extends Region> clazz : notToSave){
//			if(clazz.isInstance(r))
//				return true;
//		}
//		return true;
		return !(r instanceof AnyRegion);
	}
	
	private void saveRegions(){
		if(!regionsFile.exists()){
			try {
				regionsFile.createNewFile(); //don't need to because FileOutputStream automatically generates it but.. will so
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ArrayList<Region> toSave = new ArrayList<>();
		for(Region r : regions){
			if(shallSave(r))
				toSave.add(r);
		}
		Util.writeArrayList(toSave, regionsFile);
	}
	
	public File getRegionsFile(){
		return regionsFile;
	}

	private void initConfig() {
		FileConfiguration conf = ConfigManager.getConfig("Region");
		conf.addDefault("no_pvp_allowed_where_opponent_stands", "Opponent is in a no pvp area!");
		conf.addDefault("no_pvp_allowed_where_you_stand", "You are in a no pvp area!");
		conf.options().copyDefaults(true);
		ConfigManager.saveConfig("Region");
	}

	public Region getRegion(SerializableLocation loc){
		Region current = Region.ANY;
		for(Region r : regions){
			if(r.isInRegion(loc))
				if(current.getPriortiy() < r.getPriortiy())
					current = r;
		}
		return current;
	}
	
	public Region getRegion(Location loc){
		return getRegion(new SerializableLocation(loc));
	}
	
	public Region getRegion(Player p){
		return getRegion(p.getLocation());
	}
	
	public Region getCachedRegion(Player p){
		return currentRegion.get(Util.uuid(p));
	}
	
	public void setRegion(Player p, Region r){
		currentRegion.put(Util.uuid(p), r);
	}
	
	private void initListeners() {
		Listener[] listeners = {
				new RegionListener(),
				new FlagListener()
		};
		for(Listener l : listeners)
			registerListener(l);
	}

	@Override
	public String getName() {
		return NAME;
	}

	public static RegionManager instance() {
		return instance;
	}

	public void addRegion(Region r) {
		regions.add(r);
	}

}

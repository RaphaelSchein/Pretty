package xyz.raphaelscheinkoenig.pretty.managers.home;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import xyz.raphaelscheinkoenig.pretty.LamaCore;
import xyz.raphaelscheinkoenig.pretty.commands.Command;
import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.managers.Manager;
import xyz.raphaelscheinkoenig.pretty.managers.user.SimpleUser;
import xyz.raphaelscheinkoenig.pretty.managers.user.UserManager;
import xyz.raphaelscheinkoenig.pretty.util.Callback;
import xyz.raphaelscheinkoenig.pretty.util.Tuple;
import xyz.raphaelscheinkoenig.pretty.util.Util;
import xyz.raphaelscheinkoenig.pretty.util.sql.AsyncMySQL.MySQL;
import xyz.raphaelscheinkoenig.pretty.managers.home.commands.*;

public class HomeManager extends Manager {

	public static final String NAME = "Home";
	private PreparedStatement selectHomeNames, getHomeLocationsWithName, getHomeLocation, addLocation, addHome, delHomeLocation;
	private static HomeManager instance;
	private Map<String, Integer> homeNumber;
	
	@Override
	public void onEnable() {
		instance = this;
		initConfig();
		initHomeNumbers();
		initSQL();
		registerCommands();
	}

    private void initHomeNumbers() {
        homeNumber = new ConcurrentHashMap<>();
        FileConfiguration conf = ConfigManager.getConfig("IndividualHomes");
        Set<String> uuids = conf.getKeys(false);
        for(String uuid : uuids) {
            homeNumber.put(uuid, conf.getInt(uuid));
        }
    }

    private void saveHomeNumbers() {
        FileConfiguration conf = ConfigManager.getConfig("IndividualHomes");
        for(Map.Entry<String, Integer> e : homeNumber.entrySet()) {
            conf.set(e.getKey(), e.getValue());
        }
        ConfigManager.saveConfig("IndividualHomes");
    }

    private void registerCommands() {
		Command[] cmds = {
			    new SetHomeCommand(),
			    new HomeCommand(),
			    new ListHomeCommand(),
			    new DeleteHomeCommand(),
				new AdmHomeCommand(),
                new AdmTpHomeCommand()
		};
		for(Command cmd : cmds){
			registerCommand(cmd);
		}
	}

	private void initSQL() {
		MySQL sql = LamaCore.instance().sql().getMySQL();
		sql.queryUpdate("create table if not exists locations("
				+ "locationid int(6) primary key auto_increment not null, "
				+ "world varchar(20) not null,"
				+ "x double not null, "
				+ "y double not null, "
				+ "z double not null, "
				+ "pitch float not null, "
				+ "yaw float not null)"
				);
		sql.queryUpdate("create table if not exists homes("
				+ "homeid int(6) primary key auto_increment not null, "
				+ "userid int(6) not null, "
				+ "locationid int(6) not null, "
				+ "homename varchar(20) not null)"
				);
		initStatements(sql);
	}

	private void initStatements(MySQL sql) {
		try {
			Connection con = sql.getConnection();
				 con.prepareStatement("select homes.homename from homes "
					+ "join users on users.userid=homes.userid "
					+ "where users.userid=?"
					);
			getHomeLocationsWithName = con.prepareStatement("select * from homes "
					+ "join users on users.userid=homes.userid "
					+ "join locations on locationid=homes.locationid "
					+ "where users.userid=?");
			selectHomeNames = con.prepareStatement("select homes.homename from homes "
					+ "join users on users.userid=homes.userid "
					+ "where users.userid=?");
			getHomeLocation = con.prepareStatement("select * from homes "
					+ "join users on users.userid=homes.userid "
					+ "join locations on locations.locationid=homes.locationid "
					+ "where users.userid=? and homes.homename=?");
			addLocation = con.prepareStatement("insert into locations(world, x, y, z, pitch, yaw) values(?, ?, ?, ?, ?, ?)", java.sql.Statement.RETURN_GENERATED_KEYS);
//			addHomeUser = con.prepareStatement("insert into homeusers(userid) values(?); select LAST_INSERT_ID();");
			addHome = con.prepareStatement("insert into homes(userid, locationid, homename) values(?, ?, ?)");
			delHomeLocation = con.prepareStatement("delete locations, homes from homes join users on users.userid=homes.userid join locations on locations.locationid=homes.locationid where users.userid=? and homes.homename=?");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void initConfig() {
		FileConfiguration conf = ConfigManager.getConfig("Home");
		conf.addDefault("messages.show_homes", "Your Homes: ");
		conf.addDefault("messages.per_home_design", "&l%home%");
		conf.addDefault("messages.home_separator", ", ");
		conf.addDefault("messages.no_homes", "&cNo Homes");
		conf.addDefault("messages.set_home_usage", "Wrong usage! Correct usage: /sethome <name>");
		conf.addDefault("messages.home_name_too_long", "The specified name %name% is too long");
		conf.addDefault("messages.too_many_homes", "You cannot define any more homes!");
		conf.addDefault("messages.home_already_exists", "You already have a home named %name%");
		conf.addDefault("messages.correct_usage_home", "Wrong usage! Correct usage: /home <name>");
		conf.addDefault("messages.no_home_named", "You don't have a home named %name%!");
		conf.addDefault("messages.correct_usage_delhome", "Wrong usage! Correct usage: /delhome <name>");
		conf.addDefault("messages.home_deleted_successfully", "You successfully deleted your home %name% !");
		conf.addDefault("messages.home_created", "You successfully saved your home %name%");
		conf.addDefault("messages.home_number_changed", "You can now have %num% homes!");
		conf.addDefault("default_home_number", 1);
		conf.options().copyDefaults(true);
		ConfigManager.saveConfig("Home");
	}

	@Override
	public void onDisable() {
//		Bukkit.getOnlinePlayers().forEach(p -> {
//			
//		});
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	public static HomeManager instance(){
		return instance;
	}
	
	public void getHomeNames(Player p, Callback<List<String>> callback){
		try {
			SimpleUser us = UserManager.instance().getUser(p);
			selectHomeNames.setInt(1, us.getUserId());
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		LamaCore.instance().sql().query(selectHomeNames, res -> {
			List<String> homeNames = new ArrayList<>();
			try {
				while(res.next()){
					homeNames.add(res.getString("homename"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			callback.onReceive(homeNames);
		});
	}
	
	public void getHomes(Player p, Callback<List<Tuple<String, Location>>> callback){
		try {
			SimpleUser us = UserManager.instance().getUser(p);
			getHomeLocationsWithName.setInt(1, us.getUserId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		LamaCore.instance().sql().query(getHomeLocationsWithName, res -> {
			List<Tuple<String, Location>> locs = new ArrayList<>();
			try {
				for(int i = 1; res.next(); i++){
					Location loc = Util.fromSql(res, i);
					String homeName = res.getString("homename");
					locs.add(new Tuple<String, Location>(homeName, loc));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			callback.onReceive(locs);
		});
	}
	
	public void getHome(Player p, String homeName, Callback<Location> callback){
		SimpleUser su = UserManager.instance().getUser(p);
		int userId = su.getUserId();
		try {
			getHomeLocation.setInt(1, userId);
			getHomeLocation.setString(2, homeName);
			LamaCore.instance().sql().query(getHomeLocation, res -> {
				try {
//					res.last();
//					int row = res.getRow();
					if(res.next()) {
						Location loc = Util.fromSql(res, 1);
						callback.onReceive(loc);
					} else {
						callback.onReceive(null);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	public void addLocation(Location loc, Callback<ResultSet> id){
		try {
			System.out.println("settings locs");
			addLocation.setString(1, loc.getWorld().getName());
			addLocation.setDouble(2, loc.getX());
			addLocation.setDouble(3, loc.getY());
			addLocation.setDouble(4, loc.getZ());
			addLocation.setFloat(5, loc.getPitch());
			addLocation.setFloat(6, loc.getYaw());
			LamaCore.instance().sql().queryKey(addLocation, res -> {
				id.onReceive(res);
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addHome(Player p, String homeName, Location loc){
		addLocation(loc, id -> {
			SimpleUser us = UserManager.instance().getUser(p);
			int userId = us.getUserId();
			try {
				id.next();
				System.out.println("Add home " + id);
				addHome.setInt(1, userId);
				addHome.setInt(2, id.getInt(1));
				addHome.setString(3, homeName);
				LamaCore.instance().sql().update(addHome);
				System.out.println("added home");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

	public void deleteHome(Player p, String homeName) {
		SimpleUser su = UserManager.instance().getUser(p);
		int userId = su.getUserId();
		try {
			delHomeLocation.setInt(1, userId);
			delHomeLocation.setString(2, homeName);
			LamaCore.instance().sql().update(delHomeLocation);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getMaxHomes(Player p) {
		int power = Util.getPower(p);
		if(power >= 192)
		    return 10;
		if(power >= 640)
		    return 88;
	    return getIndividualHomeNumber(p);
	}

    public int getIndividualHomeNumber(Player p) {
        return getIndividualHomeNumber(Util.uuid(p));
    }

	public int getIndividualHomeNumber(String uuid) {
	    if(homeNumber.containsKey(uuid))
	        return homeNumber.get(uuid);
	    else
	        return ConfigManager.getConfig("Home").getInt("default_home_number");
    }

    public void giveHomes(String uuid, int newHomeNum) {
        if(uuid == null)
            return;
        homeNumber.put(uuid, newHomeNum);
        Player p = Bukkit.getPlayer(UUID.fromString(uuid));
        if(p != null) {
            p.sendMessage(Util.translate(ConfigManager.getConfig("Home").getString("messages.home_number_changed").replace("%num%", ""+newHomeNum)));
        }
    }
}

package xyz.raphaelscheinkoenig.pretty.managers.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import xyz.raphaelscheinkoenig.pretty.LamaCore;
import xyz.raphaelscheinkoenig.pretty.managers.Manager;
import xyz.raphaelscheinkoenig.pretty.util.Util;
import xyz.raphaelscheinkoenig.pretty.util.sql.AsyncMySQL.MySQL;

public class UserManager extends Manager {

	public static final String NAME = "User";
	private Set<SimpleUser> users;
	private PreparedStatement  insertUser;
	private static UserManager instance;

	@Override
	public void onEnable(){
		instance = this;
		initSql();
		initUsers();
		initListeners();
	}
	
	public void onDisable(){
		for(Player p : Bukkit.getOnlinePlayers()){
			unregisterUser(p);
		}
	}
	
	private void initUsers() {
		users = new HashSet<>();
		for(Player p : Bukkit.getOnlinePlayers()){
			initUser(p);
		}
	}
	
	private void initSql() {
		MySQL syncSQL = LamaCore.instance().sql().getMySQL();
		syncSQL.queryUpdate("create table if not exists users("
				+ "userid int(6) primary key auto_increment not null, "
				+ "username varchar(16) not null, "
				+ "useruuid varchar(36) not null)");
		Connection con = syncSQL.getConnection();
		try {
//			getUser = con.prepareStatement("select * from users where useruuid=?");
			insertUser = con.prepareStatement("insert into users(username, useruuid) values(?, ?)", java.sql.Statement.RETURN_GENERATED_KEYS);
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public void initUser(Player p){
//		try {
//			getUser.clearParameters();
//			getUser.setString(1, Util.uuid(p));
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//		}
		ResultSet res = LamaCore.instance().sql().getMySQL().query("select * from users where useruuid='" + Util.uuid(p) + "'");
        System.out.println("Initing user: " + p.getName());
        try {
            if(res.next()){
                int userId = res.getInt("userid");
                SimpleUser su = new SimpleUser(p, userId);
                registerUser(su);
                res.close();
            } else {
                insertUser.setString(1, p.getName());
                insertUser.setString(2, Util.uuid(p));
//                LamaCore.instance().sql().update(insertUser);
                res.close();
                ResultSet key = LamaCore.instance().sql().getMySQL().queryKey(insertUser);
                key.next();
                int userId = key.getInt(1);
                SimpleUser su = new SimpleUser(p, userId);
                registerUser(su);
                key.close();
                initUser(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//		LamaCore.instance().sql().query("select * from users where useruuid='" + Util.uuid(p) + "'", res -> {
//			System.out.println("Initing user: " + p.getName());
//			try {
//				if(res.next()){
//					int userId = res.getInt("userid");
//					SimpleUser su = new SimpleUser(p, userId);
//					registerUser(su);
//					res.close();
//				} else {
//					insertUser.setString(1, p.getName());
//					insertUser.setString(2, Util.uuid(p));
//					LamaCore.instance().sql().update(insertUser);
//					res.close();
//					initUser(p);
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		});
	}

	private void initListeners() {
		Listener[] listeners = {
			new PlayerJoinListener(),
			new PlayerQuitListener()
		};
		for(Listener l : listeners)
			registerListener(l);
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	public SimpleUser getUser(Player p){
		Set<SimpleUser> sync = Collections.synchronizedSet(users);
		synchronized (sync) {
			for(SimpleUser us : sync){
				if(us.isUser(p))
					return us;
			}
		}
		return null;
	}

	public static UserManager instance() {
		return instance;
	}

	private void registerUser(SimpleUser su) {
		Set<SimpleUser> synch = Collections.synchronizedSet(users);
		synchronized (synch) {
			System.out.println("registered " + su.getPlayer().getName());
			synch.add(su);
		}
	}

	public void unregisterUser(Player p) {
		SimpleUser su = getUser(p);
		Set<SimpleUser> synch = Collections.synchronizedSet(users);
		synchronized (synch) {
			synch.remove(su);
		}
	}
	
}

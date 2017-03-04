package xyz.raphaelscheinkoenig.pretty.util;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import xyz.raphaelscheinkoenig.pretty.LamaCore;
import net.sirminer.main.SystemAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Util {

	public static String translate(String toTranslate){
		return ChatColor.translateAlternateColorCodes('&', toTranslate);
	}

	public static String uuid(Player player) {
		return player.getUniqueId().toString();
	}
	private static final boolean DEBUGGING = true;

	public static Location fromSql(ResultSet res, int row){
		try {
			res.absolute(row);
			String world = res.getString("world");
			double x = res.getDouble("x");
			double y = res.getDouble("y");
			double z = res.getDouble("z");
			float yaw = res.getFloat("yaw");
			float pitch = res.getFloat("pitch");
			World w = Bukkit.getWorld(world);
			if(w == null)
				return null;
			return new Location(w, x, y, z, yaw, pitch);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public static int getPower(Player p){
		return SystemAPI.getPermissionManager().getCachePermissionUser(Util.uuid(p)).getPower();
	}
	
	public static void writeArrayList(ArrayList<? extends Serializable> list, File to){
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(to);
			out = new ObjectOutputStream(fos);
			out.writeObject(list);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					//I would freak out if it double threw!
					e.printStackTrace();
				}
			if(out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	public static ArrayList<?> loadArrayList(File from){
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream(from);
			ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();
			if(obj instanceof ArrayList){
				ArrayList<?> undefinedList = (ArrayList<?>) obj;
				if(undefinedList.size() == 0)
					return new ArrayList<>(); //I don't like returning an empty deserialized array..
				return undefinedList;
			} else {
				return null;
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if(ois != null)
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return new ArrayList<>();
	}

	public static void copyResource(String fileName, File output){
		output.getParentFile().mkdirs();
		if(!output.exists()){
			BufferedReader br = new BufferedReader(new InputStreamReader(LamaCore.instance().getResource(fileName)));
			StringBuilder sb = new StringBuilder();
			String temp = null;
			try {
				while((temp = br.readLine()) != null){
					sb.append(temp  + "\n");
				}
				FileWriter fw = new FileWriter(output);
				fw.write(sb.toString());
				fw.flush();
				fw.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void log(String text){
		if(DEBUGGING)
			System.out.println(text);
	}

	public static void logExtremely(String text){
		if(DEBUGGING){
			System.out.println("------------------");
			System.out.println(text);
			System.out.println("------------------");
		}
	}
	
}

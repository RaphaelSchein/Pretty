package xyz.raphaelscheinkoenig.pretty.managers.regions.components;

import java.io.Serializable;

import org.bukkit.Location;

import xyz.raphaelscheinkoenig.pretty.managers.regions.types.SimpleRegion;
import xyz.raphaelscheinkoenig.pretty.util.SerializableLocation;

public class FlatRegion extends SimpleRegion implements Serializable {

	private static final long serialVersionUID = -5646347588115963675L;
	private String worldUuid;
	private int minX, minZ, maxX, maxZ;
	
	public FlatRegion(String worldUuid, int x1, int z1, int x2, int z2){
		this.worldUuid = worldUuid;
		minX = Math.min(x1, x2);
		maxX = Math.max(x1, x2);
		minZ = Math.min(z1, z2);
		maxZ = Math.max(z1, z2);
	}
	
	public FlatRegion(Location loc1, Location loc2){
		String world1 = loc1.getWorld().getUID().toString();
		String world2 = loc2.getWorld().getUID().toString();
		if(!world1.equals(world2))
			throw new IllegalArgumentException("Worlds are not equal");
		worldUuid = world1;
		int x1 = loc1.getBlockX();
		int x2 = loc2.getBlockX();
		int z1 = loc1.getBlockZ();
		int z2 = loc2.getBlockZ();
		minX = Math.min(x1, x2);
		maxX = Math.max(x1, x2);
		minZ = Math.min(z1, z2);
		maxZ = Math.max(z1, z2);
	}
	
	@Override
	public boolean isInRegion(SerializableLocation loc) {
		String other = loc.getWorldUuid();
		if(worldUuid.equals(other)){
			double oX = loc.getX(), oZ = loc.getZ();
			return (oX > minX && oX < maxX) && (oZ > minZ && oZ < maxZ);
		} else
			return false;
	}

}

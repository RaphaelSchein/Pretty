package xyz.raphaelscheinkoenig.pretty.managers.regions.components;

import java.io.Serializable;

import org.bukkit.Location;

import xyz.raphaelscheinkoenig.pretty.util.SerializableLocation;

public class CuboidRegion extends FlatRegion implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int minY, maxY;
	
	public CuboidRegion(Location loc1, Location loc2) {
		super(loc1, loc2);
		int y1 = loc1.getBlockY();
		int y2 = loc2.getBlockY();
		minY = Math.min(y1, y2);
		maxY = Math.max(y1, y2);
	}

	public CuboidRegion(String worldUuid, int x1, int y1, int z1, int x2, int y2, int z2) {
		super(worldUuid, x1, z1, x2, z2);
		minY = Math.min(y1, y2);
		maxY = Math.max(y1, y2);
	}
	
	@Override
	public boolean isInRegion(SerializableLocation loc) {
		double oY = loc.getY();
		return super.isInRegion(loc) && oY > minY && oY < maxY;
	}
	
}

package xyz.raphaelscheinkoenig.pretty.util;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class SerializableLocation implements Serializable {
		
	private static final long serialVersionUID = -1689751781929204742L;
	private double x, y, z;
	private final float pitch, yaw;
	private String worldUuid;

	private transient World world;

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public World getWorld() {
		if (world == null) {
			this.world = Bukkit.getWorld(UUID.fromString(worldUuid));
		}
		return this.world;
	}

	public SerializableLocation add(Location loc) {
		this.x += loc.getBlockX();
		this.y += loc.getBlockY();
		this.z = loc.getBlockZ();
		return this;
	}

	public SerializableLocation subtract(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	public SerializableLocation(Block bl) {
		this.world = bl.getWorld();
		this.worldUuid = bl.getWorld().getUID().toString();
		this.x = bl.getX() + 0.5;
		this.y = bl.getY() + 1.000001; // Because it is so god damn low! <3
		this.z = bl.getZ() + 0.5;
		this.pitch = 0;
		this.yaw = 0;
	}

	public SerializableLocation(World w, double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = w;
		this.worldUuid = w.getUID().toString();
		this.pitch = 0;
		this.yaw = 0;
	}

	public SerializableLocation(Location loc) {
		this.x = loc.getX();
		this.y = loc.getY();
		this.z = loc.getZ();
		this.pitch = loc.getPitch();
		this.yaw = loc.getYaw();
		this.worldUuid = loc.getWorld().getUID().toString();
	}

	public SerializableLocation(Location loc, boolean toBlock) {
		this.world = loc.getWorld();
		if (toBlock) {
			this.x = loc.getBlockX();
			this.y = loc.getBlockY();
			this.z = loc.getBlockZ();
		} else {
			this.x = loc.getX();
			this.y = loc.getY();
			this.z = loc.getZ();
		}
		this.pitch = loc.getPitch();
		this.yaw = loc.getYaw();
		this.worldUuid = loc.getWorld().getUID().toString();
	}

	public Location toLocation() {
		Location loc = new Location(getWorld(), this.x, this.y, this.z);
		loc.setPitch(this.pitch);
		loc.setYaw(this.yaw);
		return loc;
	}

	public Block getBlock() {
		return world.getBlockAt((int) this.x, (int) this.y, (int) this.z);
	}

	public String getWorldUuid(){
		return worldUuid;
	}

	@Override
	public String toString() {
		return "SerializableLocation [x=" + x + ", y=" + y + ", z=" + z + ", pitch=" + pitch + ", yaw=" + yaw
				+ ", worlduuid=" + worldUuid + "]";
	}

}
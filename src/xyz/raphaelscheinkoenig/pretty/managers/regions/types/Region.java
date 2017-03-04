package xyz.raphaelscheinkoenig.pretty.managers.regions.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import xyz.raphaelscheinkoenig.pretty.util.SerializableLocation;

public class Region extends SimpleRegion implements Serializable {

	private static final long serialVersionUID = 6685051050387626809L;
	
	private List<SimpleRegion> regions;
	private int priortiy;
	
	public static Region ANY = new AnyRegion();
	
	public Region(){
		regions = new ArrayList<>();
	}
	
	public Region(Region... regions){
		this.regions = new ArrayList<>(Arrays.asList(regions));
	}
	
	public Region(List<Region> regions){
		this.regions = new ArrayList<>(regions);
	}
	
	@Override
	public boolean isInRegion(SerializableLocation loc) {
		for(SimpleRegion r : regions)
			if(r.isInRegion(loc))
				return true;
		return false;
	}
	
	public void addRegion(SimpleRegion fr){
		List<SimpleRegion> copy = new ArrayList<>(regions);
		copy.add(fr);
		regions = copy;
	}
	
	public int getPriortiy() {
		return priortiy;
	}

	public void setPriortiy(int priortiy) {
		this.priortiy = priortiy;
	}
	
	public void onPlayerMove(PlayerMoveEvent e){
//		System.out.println("Move");
	}
	
	public void onPlayerEnter(PlayerMoveEvent e, Region from) {
		System.out.println("enter");
	}

	public void onPlayerLeave(PlayerMoveEvent e, Region to) {
		System.out.println("leave");
	}

	public List<SimpleRegion> getRegions() {
		return new ArrayList<>(this.regions);
	}

	public void onBlockBreak(BlockBreakEvent e) {}

	public void onBlockPlace(BlockPlaceEvent e) {}
}

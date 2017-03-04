package xyz.raphaelscheinkoenig.pretty.managers.regions.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import xyz.raphaelscheinkoenig.pretty.managers.regions.RegionFlag;

public class FlagRegion extends Region implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<RegionFlag> flags;

	public FlagRegion(Region... regions) {
		super(regions);
	}

	public FlagRegion() {
		super();
	}

	public FlagRegion(List<Region> regions) {
		super(regions);
	}
	
	public void setFlags(RegionFlag... flags){
		this.flags = new ArrayList<RegionFlag>(Arrays.asList(flags)); 
	}

	public boolean hasFlag(RegionFlag flag){
		return flags.contains(flag);
	}
	
	
	
}

package xyz.raphaelscheinkoenig.pretty.managers.regions.types;

import xyz.raphaelscheinkoenig.pretty.util.SerializableLocation;

public class AnyRegion extends Region {
	
	private static final long serialVersionUID = 1L;

	@Override
	public boolean isInRegion(SerializableLocation loc) {
		return true;
	}
	
	@Override
	public int getPriortiy() {
		return Integer.MIN_VALUE;
	}
	
}

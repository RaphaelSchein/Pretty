package xyz.raphaelscheinkoenig.pretty.util;

import org.bukkit.scheduler.BukkitRunnable;

public abstract class CancellableRunnable extends BukkitRunnable {

	private boolean cancelled;
	
	@Override
	public final void run() {
		if (cancelled) {
			onCancel();
			this.cancel();
		} else {
			onRun();
		}
	}
	

	public abstract void onRun();
	

	public void onCancel(){}
	

	public final void cancelMe(){
		cancelled = true;
	}
	
}

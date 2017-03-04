package xyz.raphaelscheinkoenig.pretty.managers.teleport.teleportation;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import xyz.raphaelscheinkoenig.pretty.LamaCore;
import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.managers.teleport.TeleportManager;
import xyz.raphaelscheinkoenig.pretty.util.Util;

public class CountedTeleportation extends Teleportation {

	private int count;

	public CountedTeleportation(Player p, Location destination, String destinationName, int seconds) {
		super(p, destination, destinationName);
		count = seconds;
	}

	@Override
	public TeleportRunnable initRunnable() {
		return new TeleportRunnable(player) {
			public void onRun() {
				if(lostHealthOrHasMoved()){
					CountedTeleportation.this.cancel();
					this.cancel();
					return;
				}
				if (count == 0) {
					if(player == null)
						System.out.println("Player null what");
					if(getDestination() == null)
						System.out.println("dest null dafuq");
					player.teleport(getDestination());
					player.sendMessage(Util.translate(ConfigManager.getConfig("Teleportation")
							.getString("messages.on_teleport").replace("%destination%", destinationName)));
					this.cancel();
					TeleportManager.instance().removeTeleportation(CountedTeleportation.this);
					onTeleportDone();
				} else {
					player.sendMessage(Util.translate(ConfigManager.getConfig("Teleportation")
							.getString("messages.send_per_second").replace("%seconds%", "" + count)));
					count--;
				}
			}
		};
	}

	public void onTeleportDone() {}

	@Override
	protected void onProceed() {
		runnable.runTaskTimer(LamaCore.instance(), 0, 20);
	}

}

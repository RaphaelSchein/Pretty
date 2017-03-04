package xyz.raphaelscheinkoenig.pretty.pvp;

import xyz.raphaelscheinkoenig.pretty.LamaCore;
import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.managers.Manager;
import xyz.raphaelscheinkoenig.pretty.pvp.listeners.CommandListener;
import xyz.raphaelscheinkoenig.pretty.pvp.listeners.PvPPlayerQuitListener;
import xyz.raphaelscheinkoenig.pretty.util.Util;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class PvPManager extends Manager {

    private static PvPManager instance;
    private Map<String, Long> lastPlayerHits;
    private Map<String, PvPRunnable> pvpRunnables;

    @Override
    public void onEnable() {
        instance = this;
        lastPlayerHits = new ConcurrentHashMap<>();
        pvpRunnables = new ConcurrentHashMap<>();
        registerListener(new CommandListener());
        registerListener(new PvPPlayerQuitListener());
        initConfig();
    }

    private void initConfig() {
        FileConfiguration conf = ConfigManager.getConfig("pvp");
        conf.addDefault("no_commands_allowed", "You may not execute commands while fighting");
        conf.addDefault("no_longer_in_fight", "You are no longer in a fight an may log out.");
        conf.addDefault("you_may_not_logout", "You are in PvP Mode! Do not log out or you'll lose all your items :)");
        conf.addDefault("player_in_pvp_in_milliseconds", 1500L);
        conf.options().copyDefaults(true);
        ConfigManager.saveConfig("pvp");
    }

    public void updateLastHit(Player p) {
        String uuid = Util.uuid(p);
        if(!pvpRunnables.containsKey(uuid)) {
            PvPRunnable run = new PvPRunnable(p);
            run.runTaskTimer(LamaCore.instance(), 0, 5);
            pvpRunnables.put(uuid, run);
            p.sendMessage(Util.translate(ConfigManager.getConfig("pvp").getString("you_may_not_logout")));
        }
        lastPlayerHits.put(uuid, System.currentTimeMillis());
    }

    public boolean isInFight(Player p) {
        long hitDelay = ConfigManager.getConfig("pvp").getLong("player_in_pvp_in_milliseconds");
        long difference = System.currentTimeMillis() - getLastHit(p);
        return difference <= hitDelay;
    }

    public long getLastHit(Player p) {
        String uuid = Util.uuid(p);
        if(lastPlayerHits.containsKey(uuid)) {
            return lastPlayerHits.get(uuid);
        } else {
            return -1;
        }
    }

    public void onLogout(Player p) {
        if(isInFight(p)) {
            onPvpEscape(p);
        } else {
            lastPlayerHits.remove(Util.uuid(p));
        }
    }

    public void onPvpEscape(Player p) {
        Location loc = p.getLocation();
        World w = loc.getWorld();
        ItemStack[] items = p.getInventory().getContents();
        for(ItemStack item : items) {
            if(item != null)
                w.dropItem(loc, item.clone());
        }
        p.getInventory().clear();
        p.setHealth(0);
        lastPlayerHits.remove(Util.uuid(p));
        pvpRunnables.remove(Util.uuid(p));
    }

    public static PvPManager instance() {
        return instance;
    }

    @Override
    public String getName() {
        return "PvP";
    }

    public void onNoPvpAnymore(Player player) {
        String uuid = Util.uuid(player);
        lastPlayerHits.remove(uuid);
        pvpRunnables.remove(uuid);
    }
}

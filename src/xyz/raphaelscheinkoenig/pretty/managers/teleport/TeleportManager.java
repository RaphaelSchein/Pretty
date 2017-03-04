package xyz.raphaelscheinkoenig.pretty.managers.teleport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import xyz.raphaelscheinkoenig.pretty.commands.Command;
import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.managers.Manager;
import xyz.raphaelscheinkoenig.pretty.managers.teleport.commands.SetSpawnCommand;
import xyz.raphaelscheinkoenig.pretty.managers.teleport.commands.SpawnCommand;
import xyz.raphaelscheinkoenig.pretty.managers.teleport.commands.TpaCommand;
import xyz.raphaelscheinkoenig.pretty.managers.teleport.commands.TpacceptCommand;
import xyz.raphaelscheinkoenig.pretty.managers.teleport.commands.TpdenyCommand;
import xyz.raphaelscheinkoenig.pretty.managers.teleport.listener.TPPlayerQuitListener;
import xyz.raphaelscheinkoenig.pretty.managers.teleport.teleportation.Teleportation;
import xyz.raphaelscheinkoenig.pretty.util.Util;
import mkremins.fanciful.FancyMessage;

public class TeleportManager extends Manager {

	public static final String NAME = "Teleport";
	private static TeleportManager instance;
	private Map<String, Teleportation> teleportations;
	private Set<TPRequest> requests;

	@Override
	public void onEnable() {
		instance = this;
		super.onEnable();
		initConfig();
		teleportations = new HashMap<>();
		requests = new HashSet<>();
		initCommands();
		initListeners();
	}

	private void initListeners() {
		registerListener(new TPPlayerQuitListener());
	}

	private void initCommands() {
		Command[] cmds = {
			new TpacceptCommand(),
			new TpaCommand(),
			new TpdenyCommand(),
			new SetSpawnCommand(),
			new SpawnCommand()
		};
		for(Command cmd : cmds)
			registerCommand(cmd);
	}

	private void initConfig() {
		FileConfiguration conf = ConfigManager.getConfig("Teleportation");
		conf.addDefault("messages.send_per_second", "Gonna be teleported in %seconds% seconds");
		conf.addDefault("messages.on_teleport", "You were teleported to %destination%");
		conf.addDefault("messages.on_teleport_cancelled", "Your teleport to %destination% was cancelled.");
		conf.addDefault("messages.user_not_online", "The user %name% is not online or does not exist");
		conf.addDefault("messages.you_sent_tpa", "You have successfully sent a tpa request to %to%");
		conf.addDefault("messages.tpa_usage", "Wrong usage! Correct usage: /tpa <name>");
		conf.addDefault("messages.person_does_not_accept", "%name% does not accept any tpa requests!");
		conf.addDefault("messages.someone_sent_you_tpa", "%from% sent you a tpa request!");
		conf.addDefault("messages.no_request", "%from% does not have any open tp requests with you!");
		conf.addDefault("messages.request_accepted", "%to% accepted your request!");
		conf.addDefault("messages.your_request_was_accepted", "%name% accepted your tp request");
		conf.addDefault("messages.you_accepted_his_request", "You have accepted %name%'s tp request");
		conf.addDefault("messages.he_declined_request", "%name% has declined your request!");
		conf.addDefault("messages.you_declined_his_request", "You declined %name%'s tp request!");
		conf.addDefault("messages.teleport_accept", "[Accept]");
		conf.addDefault("messages.teleport_deny", "[Deny]");
		conf.options().copyDefaults(true);
		ConfigManager.saveConfig("Teleportation");
	}

	@Override
	public String getName() {
		return NAME;
	}

	public static TeleportManager instance() {
		return instance;
	}

	public void proceed(Teleportation t) {
		Map<String, Teleportation> sync = Collections.synchronizedMap(teleportations);
		Teleportation current;
		synchronized (sync) {
			current = getCurrentTeleportation(t.getPlayer());
		}
		if (current != null) {
			current.cancelTeleport();
		}
		putTeleportation(sync, t);
	}

	public synchronized Teleportation getCurrentTeleportation(Player p) {
		return teleportations.get(Util.uuid(p));
	}

//	private void putTeleportation(Teleportation t) {
//		putTeleportation(t);
//	}
	
	private void putTeleportation(Map<String, Teleportation> sync, Teleportation t) {
		synchronized (sync) {
			sync.put(Util.uuid(t.getPlayer()), t);
		}
	}
	
	public void removeTeleportation(Teleportation t){
		Map<String, Teleportation> sync = Collections.synchronizedMap(teleportations);
		synchronized (sync) {
			sync.remove(Util.uuid(t.getPlayer()));
		}
	}

	public boolean canReceiveRequest(Player p){
		return true;
	}
	
	public void requestTpa(Player from, Player to){
		FileConfiguration conf = ConfigManager.getConfig("Teleportation");
		if(canReceiveRequest(to)){
			addRequest(from, to);
			from.sendMessage(Util.translate(conf.getString("messages.you_sent_tpa").replace("%to%", to.getDisplayName())));
			FancyMessage message = new FancyMessage(Util.translate(conf.getString("messages.someone_sent_you_tpa").replace("%from%", from.getDisplayName()))).then(" ")
					.then(Util.translate(conf.getString("messages.teleport_accept"))).command("/tpaccept " + from.getName()).then(" ")
					.then(Util.translate(conf.getString("messages.teleport_deny"))).command("/tpdeny " + from.getName());
//			to.sendMessage(Util.translate(conf.getString("messages.someone_sent_you_tpa").replace("%from%", from.getDisplayName())));
			message.send(to);
		} else {
			from.sendMessage(Util.translate(conf.getString("messages.person_does_not_accept").replace("%from%", to.getDisplayName())));
		}
	}
	
	public void tpaccept(Player from, Player to){
		if(from == null || to == null)
			return;
		FileConfiguration conf = ConfigManager.getConfig("Teleportation");
		List<TPRequest> reqs = getRequestsTo(to);
		for(TPRequest req : reqs){
			if(req.isFrom(from)){
				from.sendMessage(Util.translate(conf.getString("messages.request_accepted").replace("%to%", to.getDisplayName())));
				to.sendMessage(Util.translate(conf.getString("messages.you_accepted_his_request").replace("%name%", from.getDisplayName())));
				req.startTeleportation();
				removeTPRequest(req);
				return;
			}
		}
		to.sendMessage(Util.translate(conf.getString("messages.no_request").replace("%from%", from.getDisplayName())));
//		if(req != null && req.isTo(to)){
//			from.sendMessage(Util.translate(conf.getString("messages.request_accepted").replace("%to%", to.getDisplayName())));
//			req.startTeleportation();
//		} else {
//			from.sendMessage(Util.translate(conf.getString("messages.request_withdrawn").replace("%from%", from.getDisplayName())));
//		}
	}
	
	public void tpdeny(Player from, Player to){
		if(from == null || to == null)
			return;
		FileConfiguration conf = ConfigManager.getConfig("Teleportation");
		List<TPRequest> reqs = getRequestsTo(to);
		for(TPRequest req : reqs){
			if(req.isFrom(from)){
				from.sendMessage(Util.translate(conf.getString("messages.he_declined_request").replace("%name%", to.getDisplayName())));
				to.sendMessage(Util.translate(conf.getString("messages.you_declined_his_request").replace("%name%", from.getDisplayName())));
				removeTPRequest(req);
				return;
			}
		}
	}
	
	public void addRequest(Player from, Player to){
		Set<TPRequest> reqs = Collections.synchronizedSet(requests);
		TPRequest current = getRequestFrom(from);
		if(current != null)
			removeTPRequest(current);
		TPRequest req = new TPRequest(from, to);
		synchronized(reqs){
			reqs.add(req);
		}
		System.out.println("Size:" + reqs.size());
	}
	
	public void removeTPRequest(TPRequest req){
		Set<TPRequest> reqs = Collections.synchronizedSet(requests);
		System.out.println("Removing from " + requests.size() + " " + reqs.size());
		synchronized(reqs){
			reqs.remove(req);
		}
		System.out.println("Removing to " + requests.size() + " " + reqs.size());
	}
	
	public TPRequest getRequestFrom(Player p){
		Set<TPRequest> reqs = Collections.synchronizedSet(requests);
		synchronized(reqs){
			for(TPRequest req : reqs){
				if(req.isFrom(p))
					return req;
			}
		}
		return null;
	}
	
	public List<TPRequest> getRequestsTo(Player p){
		Set<TPRequest> reqs = Collections.synchronizedSet(requests);
		List<TPRequest> hisReqs = new ArrayList<>();
		synchronized(reqs){
			for(TPRequest req : reqs){
				if(req.isTo(p))
					hisReqs.add(req);
			}
		}
		return hisReqs;
	}

	public void removeTPRequests(Player p) {
		TPRequest req = getRequestFrom(p);
		List<TPRequest> reqs = getRequestsTo(p);
		reqs.add(req);
		for(TPRequest req2 : reqs){
			removeTPRequest(req2);
		}
	}
	
}

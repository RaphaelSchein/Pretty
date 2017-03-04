package xyz.raphaelscheinkoenig.pretty.managers;

import org.bukkit.event.Listener;

import xyz.raphaelscheinkoenig.pretty.LamaCore;
import xyz.raphaelscheinkoenig.pretty.commands.Command;

public abstract class Manager {

	public void onEnable(){}
	
	public void onDisable(){}
	
	public abstract String getName();
	
	public void registerListener(Listener l){
		LamaCore.instance().registerListener(this, l);
	}
	
	public void registerCommand(Command cmd){
		LamaCore.instance().registerCommand(cmd);
	}
	
}

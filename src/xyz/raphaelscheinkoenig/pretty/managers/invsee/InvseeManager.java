package xyz.raphaelscheinkoenig.pretty.managers.invsee;

import xyz.raphaelscheinkoenig.pretty.commands.Command;
import xyz.raphaelscheinkoenig.pretty.managers.Manager;
import xyz.raphaelscheinkoenig.pretty.managers.invsee.commands.InvseeCommand;

public class InvseeManager extends Manager {

	public static final String NAME = "Invsee";
	
	public void onEnable(){
		registerCommands();
	}
	
	private void registerCommands() {
		Command[] cmds = {
			new InvseeCommand()
		};
		for(Command cmd : cmds)
			registerCommand(cmd);
	}

	@Override
	public String getName() {
		return NAME;
	}

}

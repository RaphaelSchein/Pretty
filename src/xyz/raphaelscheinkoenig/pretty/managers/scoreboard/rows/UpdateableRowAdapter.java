package xyz.raphaelscheinkoenig.pretty.managers.scoreboard.rows;

import xyz.raphaelscheinkoenig.pretty.managers.scoreboard.MyScoreboard;

import java.util.concurrent.Callable;


public class UpdateableRowAdapter extends UpdateableRow {

//	private Callable<String> returner;
	
	public UpdateableRowAdapter(MyScoreboard scoreboard, String entry, int updateIntervall, Callable<String> returner) {
		super(scoreboard, entry, updateIntervall);
	}

	@Override
	public String getTextOnUpdate() {
//		returner.call();
		return "";
	}

	public abstract class StringReturner {
		public abstract String getString();
	}
	
}

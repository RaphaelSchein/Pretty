package xyz.raphaelscheinkoenig.pretty.managers.scoreboard.rows;


import xyz.raphaelscheinkoenig.pretty.managers.scoreboard.MyScoreboard;

public abstract class UpdateableRow extends Row {

	private int tickCounter;
	private int updateIntervall;
	
	public UpdateableRow(MyScoreboard scoreboard, String entry, int updateIntervall) {
		super(scoreboard, entry);
		this.updateIntervall = updateIntervall;
	}
	
	public void tickUpdate(){
		tickCounter++;
		if(tickCounter >= updateIntervall){
			setText(getTextOnUpdate());
			tickCounter = 0;
		}
	}

	abstract public String getTextOnUpdate();
	
}

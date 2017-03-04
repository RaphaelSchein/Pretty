package xyz.raphaelscheinkoenig.pretty.managers.scoreboard.rows;

import xyz.raphaelscheinkoenig.pretty.managers.scoreboard.MyScoreboard;
import org.bukkit.scoreboard.Score;

public class Row implements Comparable<Row> {

	protected MyScoreboard scoreboard;
	protected Score score;
	private String entry;
	private int index;
	
	public Row(MyScoreboard scoreboard, String entry){
		this.scoreboard = scoreboard;
		score = this.scoreboard.getSidebar().getScore(entry);
		this.entry = entry;
	}
	
	public void setText(String con){
		scoreboard.getScoreboard().resetScores(entry);
		this.entry = con;
		score = this.scoreboard.getSidebar().getScore(entry);
		score = this.scoreboard.getSidebar().getScore(entry);
//		System.out.println("scoresss:" + score.getEntry());
//		System.out.println("entry:"+this.score.getEntry());
		scoreboard.update();
//		score.getScoreboard().getEntries().forEach(c -> System.out.println("nr:"+c));
//		scoreboard.getScoreboard().getEntries().forEach(c -> System.out.println(c));
	}
	
	public void setScore(int scoreVal){
		score.setScore(scoreVal);
	}
	
	public void setIndex(int index){
		this.index = index;
	}
	
	public int getIndex(){
		return index;
	}

	@Override
	public int compareTo(Row o) {
		return o.index - this.index;
	}
	
	public void removeMe(){
		scoreboard.removeRow(this);
	}

	public String getText() {
		return entry;
	}
	
}

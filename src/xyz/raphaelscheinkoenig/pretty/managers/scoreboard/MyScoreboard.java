package xyz.raphaelscheinkoenig.pretty.managers.scoreboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xyz.raphaelscheinkoenig.pretty.managers.scoreboard.rows.Row;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class MyScoreboard {
	
	private List<Row> rows;
	private Scoreboard scoreboard;
	private Objective sidebar;
	
	public MyScoreboard(String sidebarName){
		this.rows = new ArrayList<>();
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		this.sidebar = scoreboard.registerNewObjective(sidebarName, "dummy");
		this.sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	
	private void addRow(Row row){
		rows.add(row);
	}
	
	private void updateOrder(){
		Collections.sort(rows);
		for(int i = 0; i < rows.size(); i++){
			Row row = rows.get(i);
			row.setScore(i);
		}
	}
	
	public MyScoreboard addRow(int index, Row row){
		row.setIndex(index);
		addRow(row);
		updateOrder();
		return this;
	}
	
//	public MyScoreboard insertAfter(int index, Row row){
//		return this;
//	}
	
	public void apply(Player p){
		p.setScoreboard(scoreboard);
	}
	
	public Scoreboard getScoreboard(){
		return scoreboard;
	}
	
	public Objective getSidebar(){
		return sidebar;
	}

	public void removeRow(Row row) {
		rows.remove(row);
		scoreboard.resetScores(row.getText());
		updateOrder();
	}

	public void update() {
		updateOrder();
	}

}

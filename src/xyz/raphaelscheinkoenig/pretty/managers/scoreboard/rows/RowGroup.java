package xyz.raphaelscheinkoenig.pretty.managers.scoreboard.rows;

import xyz.raphaelscheinkoenig.pretty.managers.scoreboard.MyScoreboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class RowGroup {

	private List<Row> rows;
	
	public RowGroup(Row ...rows) {
		this.rows = Arrays.asList(rows);
	}
	
	public void insertInto(MyScoreboard sb){
		
	}
	
	public List<Row> getRows(){
		List<Row> copy = new ArrayList<>();
		rows.forEach((el) -> copy.add(el));
		return copy;
	}
	
}

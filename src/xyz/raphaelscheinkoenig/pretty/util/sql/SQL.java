package xyz.raphaelscheinkoenig.pretty.util.sql;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SQL {

	private Connection connection;
	
	public SQL(String host, String username, String password) throws SQLException{
		this(host, username, password, 3306);
	}
	
	public SQL(String host, String username, String password, int port) throws SQLException {
		String url = "jdbc:mysql://localhost:" + port;
		connection = DriverManager.getConnection(url, username, password);
	}
	
	public int selectDB(String databaseName){
		return executeUpdate("use " + databaseName);
	}
	
	public ResultSet executeQuery(String sql){
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			return stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int executeUpdate(String sql){
		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement(sql);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public ResultSet getDatabases(){
		ResultSet res = executeQuery("show databases");
		return res;
	}
	
	public int createTable(String table){
		try {
			PreparedStatement stmt = connection.prepareStatement("create table(" + table + ");");
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

}

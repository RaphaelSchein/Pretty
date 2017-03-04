package xyz.raphaelscheinkoenig.pretty.managers.user;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerJoinListener implements Listener {

//	private PreparedStatement joinStatement, getUserStatement;

	public PlayerJoinListener() {
//		try {
//			getUserStatement = LamaCore.instance().sql().getMySQL().getConnection()
//					.prepareStatement("select users.userid from users where users.useruuid=?");
//			joinStatement = LamaCore.instance().sql().getMySQL().getConnection()
//					.prepareStatement("insert into users(username, useruuid) "
//							+ "values(?, ?) "
//							+ "on duplicate key update username=?");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerLoginEvent e) {
//        System.out.println("Joining");
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e1) {
//            e1.printStackTrace();
//        }
//        System.out.println("Waited");
		System.out.println("Loging in ant initing now!");
		UserManager.instance().initUser(e.getPlayer());
//        System.out.println("Inited");
//		Player p = e.getPlayer();
//		String playerName = p.getName();
//
//		LamaCore.instance().sql().query(getUserStatement, res -> {
//			try {
//				if (res.next()) {
//					int userId = res.getInt("userid");
//					SimpleUser su = new SimpleUser(p, userId);
//					UserManager.instance().registerUser(su);
//				} else {
//					joinStatement.setString(1, playerName);
//					joinStatement.setString(2, Util.uuid(p));
//					joinStatement.setString(3, playerName);
//					LamaCore.instance().sql().update(joinStatement);
//					getUserStatement.setString(1, Util.uuid(p));
//					SimpleUser su = UserManager.instance().getUserFromDatabase(p);
//					UserManager.instance().registerUser(su);
//				}
//			} catch (SQLException e1) {
//				e1.printStackTrace();
//			}
//		});
	}

}

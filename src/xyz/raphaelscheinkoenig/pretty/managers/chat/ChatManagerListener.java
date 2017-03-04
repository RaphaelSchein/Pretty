package xyz.raphaelscheinkoenig.pretty.managers.chat;

import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.managers.clan.Clan;
import xyz.raphaelscheinkoenig.pretty.managers.clan.ClanManager;
import xyz.raphaelscheinkoenig.pretty.util.Util;
import net.sirminer.main.SystemAPI;
import net.sirminer.permissions.PermissionGroup;
import net.sirminer.permissions.PermissionUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class ChatManagerListener implements Listener {

    private final String MESSAGE, MESSAGE_CLAN;

    public ChatManagerListener() {
        MESSAGE = ConfigManager.getConfig("Chat").getString("format_no_clan");
        MESSAGE_CLAN = ConfigManager.getConfig("Chat").getString("format_with_clan");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        String message = e.getMessage();
        if(message.startsWith("#")){
            Clan clan = ClanManager.instance().getClanOf(p);
            if(clan != null){
                clan.sendMessage(p, message.substring(1));
                e.setCancelled(true);
                return;
            }
        }
        PermissionUser pUser = SystemAPI.getPermissionManager().getCachePermissionUser(Util.uuid(p));
        Clan cl = ClanManager.instance().getClanOf(p);
        String toEdit;
        if(cl == null){
            toEdit = MESSAGE;
        } else {
            toEdit = MESSAGE_CLAN.replace("%clanname%", cl.getName()).replace("%abbr%", cl.getAbbreviation());
        }
        toEdit = toEdit.replace("%name%", pUser.getGroup().getPrefix() + p.getName()).replace("%rang%", pUser.getGroup().getColor()).replace("%msg%", e.getMessage());
        e.setCancelled(true);
        for(Player pl : Bukkit.getOnlinePlayers())
            pl.sendMessage(toEdit);
//        System.out.println("Message:" + toEdit);
//        e.setMessage(toEdit);
//        System.out.println(e.getFormat());
//        e.setFormat("%2$s");
    }

}

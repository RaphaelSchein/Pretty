package xyz.raphaelscheinkoenig.pretty.managers.chat;

import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.managers.Manager;
import org.bukkit.configuration.file.FileConfiguration;


public class ChatManager extends Manager {

    @Override
    public void onEnable() {
        super.onEnable();
        FileConfiguration conf = ConfigManager.getConfig("Chat");
        conf.addDefault("format_no_clan", "%rang% %name% > %msg%");
        conf.addDefault("format_with_clan", "[%abbr%] [%clanname%] | %rang% %name% > %msg%");
        conf.options().copyDefaults(true);
        ConfigManager.saveConfig("Chat");
        registerListener(new ChatManagerListener());
    }

    @Override
    public String getName() {
        return "Chat";
    }

}

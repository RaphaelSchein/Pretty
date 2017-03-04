package xyz.raphaelscheinkoenig.pretty.managers.scoreboard;

import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.managers.Manager;
import xyz.raphaelscheinkoenig.pretty.managers.scoreboard.rows.Row;
import xyz.raphaelscheinkoenig.pretty.util.Tuple;
import xyz.raphaelscheinkoenig.pretty.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyScoreboardManager extends Manager {

    private static MyScoreboardManager instance;
    private Map<String, Tuple<Integer, Integer>> playerKd;

    @Override
    public void onEnable() {
        instance = this;
        playerKd = new ConcurrentHashMap<>();
        initConfig();
//        initSql();
        registerListeners();
        for(Player p : Bukkit.getOnlinePlayers()){
            createAndApplyScoreboardFor(p);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        FileConfiguration conf = ConfigManager.getConfig("KDs");
        for(String s : playerKd.keySet()){
            Tuple<Integer, Integer> kd = getKillsAndDeathsOf(s);
            conf.set(s + ".k", kd.getFirst());
            conf.set(s + ".d", kd.getSecond());
        }
        ConfigManager.saveConfig("KDs");
    }

    private void registerListeners() {
        Listener[] listeners = {
                new DeathListener(),
                new KDPlayerJoinListener()
        };
        for(Listener l : listeners)
            registerListener(l);
    }

//    private void initSql() {
//        AsyncMySQL.MySQL sql = LamaCore.instance().sql().getMySQL();
//        sql.query("create table if not exists kd(" +
//                "kdid int(6) primary key auto_increment not null," +
//                "userid int(6) not null," +
//                "kills int(6)," +
//                "deaths int(6)" +
//                ");");
//    }

    private void initConfig() {
        FileConfiguration conf = ConfigManager.getConfig("Scoreboard");
        conf.addDefault("scoreboard_title", "LamaPvP");
        conf.addDefault("kills_design", "Kills: %kills%");
        conf.addDefault("deaths_design", "Deaths: %deaths%");
        conf.addDefault("kd_design", "K/D: %kd%");
        conf.addDefault("ip_design", "play.lamawiese.net");
        conf.options().copyDefaults(true);
        ConfigManager.saveConfig("Scoreboard");
        conf = ConfigManager.getConfig("KDs");
        for(String s : conf.getKeys(false)){
            int kills = conf.getInt(s + ".k");
            int deaths = conf.getInt(s + ".d");
            playerKd.put(s, new Tuple<>(kills, deaths));
        }
    }

    public void createAndApplyScoreboardFor(Player p){
        Tuple<Integer, Integer> kd = getKillsAndDeathsOf(p);
        if(kd == null){
            kd = new Tuple<Integer, Integer>(0, 0);
            updateKd(Util.uuid(p), kd);
        }
        FileConfiguration conf = ConfigManager.getConfig("Scoreboard");
        String boardName = Util.translate(conf.getString("scoreboard_title"));
        String killLayout = Util.translate(conf.getString("kills_design"));
        String deathsLayout = Util.translate(conf.getString("deaths_design"));
        String kdLayout = Util.translate(conf.getString("kd_design"));
        String ipLayout = Util.translate(conf.getString("ip_design"));
        MyScoreboard sb = new MyScoreboard(boardName);
        sb.addRow(1, new Row(sb, "§e"));
        Row killsRow = new Row(sb, killLayout.replace("%kills%", ""+kd.getFirst()));
        sb.addRow(2, killsRow);
        Row deathsRow = new Row(sb, deathsLayout.replace("%deaths%", ""+kd.getSecond()));
        sb.addRow(3, deathsRow);
        sb.addRow(4, new Row(sb, " §e"));
        Row kdRow = new Row(sb, kdLayout.replace("%kd%", calculateKdNice(kd)));
        sb.addRow(5, kdRow);
        sb.addRow(6, new Row(sb, "§f"));
        sb.addRow(7, new Row(sb, ipLayout));
        sb.apply(p);
        System.out.println("applied!");
    }

    public void updateKd(Player p, Tuple<Integer, Integer> newVals){
        updateKd(Util.uuid(p), newVals);
        createAndApplyScoreboardFor(p);
    }

    public void updateKd(String uuid, Tuple<Integer, Integer> newVals){
        playerKd.put(uuid, newVals);
    }

    public Tuple<Integer, Integer> getKillsAndDeathsOf(Player p){
        return getKillsAndDeathsOf(Util.uuid(p));
    }

    public Tuple<Integer, Integer> getKillsAndDeathsOf(String uuid){
        return playerKd.get(uuid);
    }

    public float calculateKd(Tuple<Integer, Integer> kd){
        if(kd.getSecond() == 0)
            return kd.getFirst();
        else
            return (float)kd.getFirst()/(float)kd.getSecond();
    }

    public String calculateKdNice(Tuple<Integer, Integer> kd){
        return String.format("%.2f", calculateKd(kd));
    }

    @Override
    public String getName() {
        return "Scoreboard";
    }

    public static MyScoreboardManager instance() {
        return instance;
    }

    public void increaseDeathsOf(Player p){
        Tuple<Integer, Integer> current = getKillsAndDeathsOf(p);
        current = new Tuple<>(current.getFirst(), current.getSecond()+1);
        updateKd(p, current);
    }

    public void increaseKillsOf(Player p){
        Tuple<Integer, Integer> current = getKillsAndDeathsOf(p);
        current = new Tuple<>(current.getFirst()+1, current.getSecond());
        updateKd(p, current);
    }

}

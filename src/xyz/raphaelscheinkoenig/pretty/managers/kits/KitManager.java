package xyz.raphaelscheinkoenig.pretty.managers.kits;

import xyz.raphaelscheinkoenig.pretty.LamaCore;
import xyz.raphaelscheinkoenig.pretty.commands.Command;
import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.managers.Manager;
import xyz.raphaelscheinkoenig.pretty.managers.kits.commands.KitCommand;
import xyz.raphaelscheinkoenig.pretty.util.Util;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class KitManager extends Manager {

    public static final String NAME = "Kits";
    private static KitManager instance;
    private List<Kit> kits;
    private Map<String, Long> kitsUsed;
    private long waitDelay;

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
        initConfig();
        initKits();
        initTimes();
        registerCommands();
    }

    private void initConfig() {
        FileConfiguration conf = ConfigManager.getConfig("Kits");
        conf.addDefault("kits.delay_in_milliseconds", 1000L*60L*60L*24L);
        conf.addDefault("messages.you_cannot_use_kit_yet", "You must wait at least %time% to use your next kit!");
        conf.addDefault("messages.you_equipped_kit", "You successfully equipped a kit!");
        conf.addDefault("messages.you_dont_have_enough_space", "You don't have enough inventory space for this kit!");
        conf.addDefault("messages.you_dont_have_permissions", "You don't have enough permissions for this kit. Buy it amk!");
        conf.options().copyDefaults(true);
        ConfigManager.saveConfig("Kits");
        waitDelay = ConfigManager.getConfig("Kits").getLong("kits.delay_in_milliseconds");
    }

    public void onDisable(){
        FileConfiguration conf = ConfigManager.getConfig("KitsTimes");
        for(String key : kitsUsed.keySet())
            conf.set(key, kitsUsed.get(key));
        ConfigManager.saveConfig("KitsTimes");
    }

    private void initTimes() {
        FileConfiguration conf = ConfigManager.getConfig("KitsTimes");
        ConfigurationSection sec = conf.getConfigurationSection("");
        Set<String> keys = sec.getKeys(false);
        for(String s : keys){
            kitsUsed.put(s, sec.getLong(s));
        }
    }

    public boolean canReceiveKit(Player p){
        return canReceiveKit(Util.uuid(p));
    }

    public boolean canReceiveKit(String uuid){
        return (System.currentTimeMillis() - getLastReceivedKit(uuid)) >= waitDelay;
    }

    public long getLastReceviedKit(Player p){
        return getLastReceivedKit(Util.uuid(p));
    }

    public long getLastReceivedKit(String uuid) {
        Long current = kitsUsed.get(uuid);
        if (current == null)
            return -1;
        else
            return current;
    }

    public void onKitReceive(Player p){
        onKitReceive(Util.uuid(p));
    }

    public void onKitReceive(String uuid){
        kitsUsed.put(uuid, System.currentTimeMillis());
    }

    public String getKitTimeLeftNice(Player p){
        return getKitTimeLeftNice(Util.uuid(p));
    }

    public String getKitTimeLeftNice(String uuid) {
        final long HOURS_IN_MS = 1000L*60L*60;
        final long MINUTES_IN_MS = 1000L*60L;
        final long SECONDS_IN_MS = 1000L;
        long left = (waitDelay-(System.currentTimeMillis() - getLastReceivedKit(uuid)));
        int hours = (int) (left / HOURS_IN_MS);
        int minutes = (int) ((left - hours*HOURS_IN_MS) / MINUTES_IN_MS);
        int seconds = (int) ((left - hours*HOURS_IN_MS - minutes*MINUTES_IN_MS) / SECONDS_IN_MS);
        return hours + "h:" + minutes + "m:" + seconds + "s";
    }

    private void initKits() {
        kits = new CopyOnWriteArrayList<>();
        kitsUsed = new ConcurrentHashMap<>();
        File kitFile = getKitFile();
        if(!kitFile.exists()){
            Util.copyResource("kits.json", kitFile);
        }
        JSONParser parser = new JSONParser();
        try {
            JSONObject json = (JSONObject) parser.parse(new FileReader(kitFile));
            JSONArray kitArray = (JSONArray) json.get("KITS");
            for(int i = 0; i < kitArray.size(); i++){
                JSONObject kitJson = (JSONObject) kitArray.get(i);
                if(kitJson == null)
                    System.err.println("kitJson was somehow null");
                try {
                    Kit kit = new Kit(kitJson);
                    kits.add(kit);
                } catch (Kit.KitParserException e) {
                    System.err.println("An error occured while parsing Kit Nr. " + i + ":");
                    System.err.println(e.getMistake());
                    System.out.println(" ");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Successfuly loaded " + kits.size() + " kits!");
    }

    private File getKitFile(){
        return new File(LamaCore.instance().getDataFolder(), "kits.json");
    }

    private void registerCommands() {
        Command[] cmds = {
                new KitCommand()
        };
        for(Command cmd : cmds)
            registerCommand(cmd);
    }

    public List<Kit> getKits(){
        return new ArrayList<>(kits);
    }

    public static KitManager instance(){
        return instance;
    }

    @Override
    public String getName() {
        return NAME;
    }
}

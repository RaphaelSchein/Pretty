package xyz.raphaelscheinkoenig.pretty.managers.kits;

import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.managers.guis.GuiManager;
import xyz.raphaelscheinkoenig.pretty.managers.guis.MyGUI;
import xyz.raphaelscheinkoenig.pretty.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;


public class KitSelectGUI extends MyGUI {

    private List<Kit> kits;

    public KitSelectGUI(Player p) {
        super(p);
        kits = KitManager.instance().getKits();
        int highestPos = kits.stream().mapToInt(Kit::getInventoryPosition).max().orElse(-1);
        if(highestPos < 0) {
            p.sendMessage("No guis");
        }
        else {
            Inventory inv = Bukkit.createInventory(null, ((highestPos + 9) / 9)*9);
            for (Kit kit : kits) {
                inv.setItem(kit.getInventoryPosition(), kit.getIcon());
            }
            player.openInventory(inv);
            GuiManager.instance().registerMyGUI(this);
        }
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        int slot = e.getSlot();
//        boolean canReceive = KitManager.instance().canReceiveKit(p);
        FileConfiguration conf = ConfigManager.getConfig("Kits");
        Kit selected = kits.stream().filter(k -> k.getInventoryPosition() == slot).findFirst().orElse(null);
        if(selected != null) {
            int power = Util.getPower(player);
            int requiredPower = selected.getRequiredPower();
            if (power >= requiredPower) {
                if (KitManager.instance().canReceiveKit(player)) {
                    if (selected.hasEnoughInventorySpace(player)) {
                        close();
                        selected.applyKit(player);
                        KitManager.instance().onKitReceive(player);
                        player.sendMessage(Util.translate(conf.getString("messages.you_equipped_kit")));
                    } else {
                        close();
                        player.sendMessage(Util.translate(conf.getString("messages.you_dont_have_enough_space")));
                    }
                } else {
                    close();
                    player.sendMessage(Util.translate(conf.getString("messages.you_cannot_use_kit_yet").replace("%time%", KitManager.instance().getKitTimeLeftNice(player))));
                }
            } else {
                player.sendMessage(Util.translate(conf.getString("messages.you_dont_have_permissions")));
            }
        }
    }
}

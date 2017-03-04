package xyz.raphaelscheinkoenig.pretty.managers.invsee;

import xyz.raphaelscheinkoenig.pretty.managers.guis.GuiManager;
import xyz.raphaelscheinkoenig.pretty.managers.guis.MyGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;


public class InvseeGUI extends MyGUI {

    public InvseeGUI(Player p, Player toStalk) {
        super(p);
        GuiManager.instance().registerMyGUI(this);
        p.openInventory(toStalk.getInventory());
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
    }
}

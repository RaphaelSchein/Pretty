package xyz.raphaelscheinkoenig.pretty.managers.guis.listeners;

import xyz.raphaelscheinkoenig.pretty.managers.guis.GuiManager;
import xyz.raphaelscheinkoenig.pretty.managers.guis.MyGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;


public class GuiListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e){
        MyGUI gui = GuiManager.instance().getGUI(e.getWhoClicked());
        if(gui != null)
            gui.onClick(e);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e){
        MyGUI gui = GuiManager.instance().getGUI(e.getPlayer());
        if(gui != null)
            gui.onClose();
    }

}

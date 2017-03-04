package xyz.raphaelscheinkoenig.pretty.managers.guis;

import xyz.raphaelscheinkoenig.pretty.util.Util;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;


public class MyGUI {

    protected final Player player;

    public MyGUI(Player p){
        this.player = p;
    }

    public boolean isOf(HumanEntity he){
        return Util.uuid(player).equals(he.getUniqueId().toString());
    }

    public void close(){
        player.closeInventory();
        onClose();
    }

    public void onClick(InventoryClickEvent e){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyGUI myGUI = (MyGUI) o;

        return Util.uuid(player).equals(Util.uuid(myGUI.player));
    }

    public void onClose() {
        GuiManager.instance().removeMyGUI(this);
    }

}

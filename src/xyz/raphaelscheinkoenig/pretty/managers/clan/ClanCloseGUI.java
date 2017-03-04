package xyz.raphaelscheinkoenig.pretty.managers.clan;

import xyz.raphaelscheinkoenig.pretty.config.ConfigManager;
import xyz.raphaelscheinkoenig.pretty.item.NBTBuilder;
import xyz.raphaelscheinkoenig.pretty.managers.guis.GuiManager;
import xyz.raphaelscheinkoenig.pretty.managers.guis.MyGUI;
import xyz.raphaelscheinkoenig.pretty.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class ClanCloseGUI extends MyGUI {

    private Clan clan;

    public ClanCloseGUI(Player p, Clan clan) {
        super(p);
        this.clan = clan;
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.RED + "Clan löschen?");
        ItemStack closeItem = new NBTBuilder(Material.WOOL).setId("cl_close").setData((byte)5).setTitle(ChatColor.RED + "ABBRECHEN").build();
        ItemStack cancelItem = new NBTBuilder(Material.WOOL).setId("cl_cancel").setData((byte)14).setTitle(ChatColor.RED + "Clan löschen").build();
        inv.setItem(3, closeItem);
        inv.setItem(5, cancelItem);
        GuiManager.instance().registerMyGUI(this);
        p.openInventory(inv);
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        super.onClick(e);
        ItemStack clicked = e.getCurrentItem();
        if(clicked != null){
            NBTBuilder nbt = new NBTBuilder(clicked);
            String itemId = nbt.getId();
            if(itemId.equals("cl_close")){
                clan.closeClan();
                player.sendMessage(Util.translate(ConfigManager.getConfig("Clan").getString("messages.you_closed_the_clan")));
                close();
            } else {
                itemId.equals("cl_cancel");
                close();
            }
        }
    }
}

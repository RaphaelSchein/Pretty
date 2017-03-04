package xyz.raphaelscheinkoenig.pretty.managers.kits;

import xyz.raphaelscheinkoenig.pretty.item.ItemManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class Kit {

    private ItemStack icon;
    private int position;
    private ItemStack[] items;

    private int requiredPower = 0;

    public Kit(JSONObject json) throws KitParserException {
        Object posObj = json.get("position");
        if (posObj == null)
            throw new KitParserException("No position was set");
        try {
            position = Integer.parseInt(posObj.toString());
        } catch (NumberFormatException e) {
            throw new KitParserException("Could not read position. No number?");
        }
        Object kitIconId = json.get("kit_icon_id");
        if (kitIconId == null)
            throw new KitParserException("No icon was set");
        icon = ItemManager.instance().getItem(kitIconId.toString());
        if (icon == null)
            throw new KitParserException("Icon does not exist: " + kitIconId);
        Object power = json.get("power");
        if(power != null) {
            try {
                int powerLevel = Integer.parseInt(power.toString());
                requiredPower = powerLevel;
            } catch(NumberFormatException e) {
                System.out.println("Could not read power of kit! ");
            }
        }
        try {
            JSONArray array = (JSONArray) json.get("kit_items");
            int itemNum = array.size();
            items = new ItemStack[itemNum];
            for(int i = 0; i < itemNum; i++){
                JSONObject itemJson = (JSONObject) array.get(i);
                String itemId = itemJson.get("item_id").toString();
                ItemStack item = ItemManager.instance().getItem(itemId);
                if(item == null)
                    throw new Exception("Item does not exist: " + itemId);
                Object optAmount = itemJson.get("amount");
                if(optAmount != null){
                    int amount = Integer.parseInt(optAmount.toString());
                    item.setAmount(amount);
                }
                items[i] = item;
            }
        } catch(Exception e){
            throw new KitParserException("Could not read kit_items! Perhaps it does not exist/is not an array?");
        }
    }

    public ItemStack getIcon(){
        return icon.clone();
    }

    public ItemStack[] getContents(){
        return items.clone();
    }

    public int getContentNumber(){
        return items.length;
    }

    public int getInventoryPosition(){
        return position;
    }

    public boolean hasEnoughInventorySpace(Player p){
        PlayerInventory inv = p.getInventory();
        ItemStack[] storage = inv.getStorageContents();
        int needing = getContentNumber();
        int having = 0;
        for(int i = 0; i < storage.length; i++){
            if(having >= needing)
                return true;
            ItemStack current = storage[i];
            if(current == null || current.getType() == Material.AIR)
                having++;
        }
        return false;
    }

    public void applyKit(Player p){
        Inventory inv = p.getInventory();
        inv.addItem(getContents());
    }

    public int getRequiredPower() {
        return requiredPower;
    }

    public class KitParserException extends Exception {
        private String mistake;
        public KitParserException(String mistake){
            this.mistake = mistake;
        }
        public String getMistake(){
            return mistake;
        }
    }

}

package xyz.raphaelscheinkoenig.pretty.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class NBTBuilder extends ItemBuilder {

	private NBTItem nbt;
	
	public NBTBuilder(ItemStack item) {
		super(item);
		nbt = new NBTItem(this.item);
	}

	public NBTBuilder(Material mat, int amount, byte data) {
		super(mat, amount, data);
		nbt = new NBTItem(this.item);
	}

	public NBTBuilder(Material mat, int amount) {
		super(mat, amount);
		nbt = new NBTItem(this.item);
	}

	public NBTBuilder(Material mat) {
		super(mat);
		nbt = new NBTItem(this.item);
	}
	
	public String getId(){
		return nbt.getString("item_id");
	}

	public NBTBuilder setId(String id){
	    nbt.setString("item_id", id);
	    return this;
    }

    @Override
    public ItemStack build() {
        nbt.setItem(super.build());
        return nbt.getItem();
    }
}

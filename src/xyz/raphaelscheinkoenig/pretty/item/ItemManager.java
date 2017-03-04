package xyz.raphaelscheinkoenig.pretty.item;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xyz.raphaelscheinkoenig.pretty.LamaCore;

import xyz.raphaelscheinkoenig.pretty.managers.Manager;
import xyz.raphaelscheinkoenig.pretty.util.Util;
import org.bukkit.inventory.ItemStack;


public class ItemManager extends Manager {
	
	private final List<MyItem> items;
	private static ItemManager instance;
	
	public ItemManager(){
	    instance = this;
		items = new ArrayList<>();
		initItems();
	}
	
	public List<MyItem> getMyItems(){
		return new ArrayList<>(items);
	}
	
	public List<ItemStack> getItems(){
		List<ItemStack> copy = new ArrayList<>();
		items.forEach(i -> copy.add(i.getItem()));
		return copy;
	}
	
	public void registerItem(String name, ItemStack item){
		items.add(new MyItem(name, item));
	}
	
	public ItemStack getItem(String name){
		for(MyItem item : items){
			if(item.name.equalsIgnoreCase(name)){
				return item.item.clone();
			}
		}
		return null;
	}
	
	private void initItems(){
		copyPremadeFiles();
		File itemDir = getItemDir();
		itemDir.mkdirs();
		File[] files = itemDir.listFiles();
		for(File f : files){
			List<ItemFactory.ItemData> datas = ItemFactory.getItemsFrom(f);
			if(datas == null)
				continue;
			for(ItemFactory.ItemData data : datas){
				ItemStack temp = getItem(data.getId());
				if(temp != null){
					System.out.println("An item with the name " + data.getId() + " was registered twice! Will only use the first one.");
				} else {
					items.add(new MyItem(data.getId(), data.getItem()));
				}
			}
		}
		System.out.println("Successfully registered " + items.size() + " items!");
	}
	
	private void copyPremadeFiles(){
		File dir = getItemDir();
		Util.copyResource("items.xml", new File(dir, "items.xml"));
	}
	
	public File getItemDir() {
		return new File(LamaCore.instance().getDataFolder(), "items");
	}

    @Override
    public String getName() {
        return "Items";
    }

    public static ItemManager instance(){
        return instance;
    }

    public static class MyItem {
		
		private String name;
		private ItemStack item;
		
		public MyItem(String name, ItemStack item) {
			this.name = name;
			this.item = item;
		}
		
		public boolean isData(String name){
			return this.name.equalsIgnoreCase(name);
		}
		
		public ItemStack getItem(){
			return item.clone();
		}
		
	}
	
}

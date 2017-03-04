package xyz.raphaelscheinkoenig.pretty.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ItemBuilder {

	public static final char UNICORN = '\u26C9';
	public static final char ARROW = '\u27BC';
	public static final char BIG_ARROW = '\u27BD';
	public static final char COLOR_EXPOSITION = '§';
	public static final String CUSOTM_DATA_SPLITTER = "§" + UNICORN,
								CUSTOM_DATA_REGEX = "\\" + CUSOTM_DATA_SPLITTER;

	public static final Pattern VARIABLE_DETECTOR = Pattern.compile("($\\(+w\\))");
	
	protected ItemStack item;
	protected ItemMeta meta;
	
	public static final String UNDEFINED = "UNDEFIEND_VALUE";
	public static final String SPLITTER = UNICORN+": ";
	
	public ItemBuilder(Material mat){
		this(new ItemStack(mat));
	}
	
	public ItemBuilder(Material mat, int amount){
		this(new ItemStack(mat, amount));
	}
	
	public ItemBuilder(Material mat, int amount, byte data){
		this(new ItemStack(mat, amount, data));
	}
	
	public ItemBuilder(ItemStack item){
		this.item = item; 
		meta = item.getItemMeta();
	}
	
//	public void ItemBuilder init(Map<String, String> map){
//		List<String> list = meta.getLore();
//		for(int i = 0; i < list.size(); i++){
//			String line = list.get(i);
//			Matcher m = VARIABLE_DETECTOR.matcher(line);
//		}
//		TextComponent tc = new TextComponent();
//		
//		World w = null;
//		w.getHigh
//		
//	}

	public ItemBuilder setCustomData(String id){
		String title = getTitle();
		if(title.contains(CUSOTM_DATA_SPLITTER)){
			title = getActualTitle();
		}
		char chars[] = id.toCharArray();
		StringBuilder sb = new StringBuilder();
		sb.append(title);
		sb.append(CUSOTM_DATA_SPLITTER);
		for(char c : chars){
			sb.append(COLOR_EXPOSITION);
			sb.append(c);
		}
		setTitle(sb.toString());
		return this;
	}
	
	public boolean hasCustomData(){
		return getTitle().contains(CUSOTM_DATA_SPLITTER);
	}
	
	public String getCustomData(){
		String[] p = getTitle().split(CUSTOM_DATA_REGEX);
		if(p.length != 2){
			return null;
		}
		char[] chars = p[1].toCharArray();
		StringBuilder sb = new StringBuilder();
		for(int i = 1; i < chars.length; i += 2){
			sb.append(chars[i]);
		}
		return sb.toString();
	}
	
	public ItemBuilder addEnchantEffect(){
		meta.addEnchant(Enchantment.DURABILITY, 0, false);
		item.setItemMeta(meta);
		item.removeEnchantment(Enchantment.DURABILITY);
		return this;
	}
	
	public ItemBuilder setItem(ItemStack item){
		this.item = item;
		this.meta = item.getItemMeta();
		return this;
	}
	
	public ItemBuilder addEnchant(Enchantment ench, int lvl){
		meta.addEnchant(ench, lvl, false);
		item.setItemMeta(meta);
		return this;
		
	}
	
	public String getTitle(){
		return meta.getDisplayName();
	}
	
	public String getActualTitle(){
		String title = getTitle();
		return title == null? null : title.split(CUSTOM_DATA_REGEX)[0];
	}
	
	public ItemBuilder setMaterial(Material mat){
		item.setType(mat);
		return this;
	}
	
	public ItemBuilder setData(byte data){
		item.setDurability(data);
		return this;
	}
	
	public ItemBuilder setAmount(int amount){
		item.setAmount(amount);
		return this;
	}
	
	public ItemBuilder addLore(String lore){
		List<String> current = meta.getLore();
		
		if(current == null){
			current = new ArrayList<String>();
		}
		
		current.add(lore);
		meta.setLore(current);
		
		return this;
	}
	
	public ItemBuilder addLore(String... lore){
		for(String s : lore){
			addLore(s);
		}
		return this;
	}
	
	public ItemBuilder setTitle(String title){
		meta.setDisplayName(title);
		return this;
	}
	
	public ItemBuilder setTitleLore(String title, String lore){
		setTitle(title);
		addLore(lore);
		return this;
	}
	
	public ItemBuilder setTitleLore(String title, String... lore){
		setTitle(title);
		for(String s : lore){
			addLore(s);
		}
		return this;
	}
	
	public ItemStack build(){
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemBuilder addValue(String type, String value){
		addLore(type + SPLITTER + value);
		return this;
	}
	
	public ItemBuilder addValue(String type, double value){
		return addValue(type, ""+value);
	}
	
	public boolean hasValue(String type){
		return getValue(type) != UNDEFINED;
	}
	
	public ItemBuilder setValue(String type, String value){
		List<String> currentLore = meta.getLore();
		if(currentLore == null)
			currentLore = new ArrayList<>();
		
		String correctedType = ChatColor.stripColor(type.toUpperCase());
		
		for (int i = 0; i < currentLore.size(); i++){
			String s = currentLore.get(i);
			String corrected = ChatColor.stripColor(s);
			if(corrected.toUpperCase().startsWith(correctedType)){
				currentLore.set(i, type + SPLITTER + value);
				meta.setLore(currentLore);
				return this;
			}
		}
		return addValue(type, value);
	}
	
	public ItemBuilder setValue(String type, double value){
		return setValue(type,""+value);
	}
	
	public String getValue(String type){
		type = ChatColor.stripColor(type).toUpperCase();
		List<String> lore = meta.getLore();
		
		if(lore == null) return null;
		
		for(String s : lore){
			
			String corrected = ChatColor.stripColor(s);
			if(corrected.toUpperCase().startsWith(type)){
				String[] p = corrected.split(SPLITTER);
				String value = p[1];
				return value;
			}
			
		}
		return null;
	}
	
}

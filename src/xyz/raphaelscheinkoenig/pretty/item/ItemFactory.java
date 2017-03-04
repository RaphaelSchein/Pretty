 package xyz.raphaelscheinkoenig.pretty.item;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import xyz.raphaelscheinkoenig.pretty.LamaCore;
import xyz.raphaelscheinkoenig.pretty.util.Util;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ItemFactory {

	private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	public ItemFactory(){}
	
	static {
		factory.setIgnoringElementContentWhitespace(true);
		factory.setIgnoringComments(true);
	}
	
	public static List<ItemData> getItemsFrom(File f){
		String name = f.getName();
		String[] p = name.split("\\.");
		String extension = p[p.length-1];
		List<ItemData> data = null;
		if (extension.equalsIgnoreCase("xml")) {
			data = getItemsFromXML(f);
		} else if (extension.equalsIgnoreCase("txt")) {
			ItemData itemData = getItemFromTXT(f);
			if (itemData != null) {
				data = new ArrayList<>();
				data.add(itemData);
			}
		} else if (extension.equalsIgnoreCase("yml")) {
			data = getItemsFromYML(f);
		}
		return data;
	}
	
	public static List<ItemData> getItemsFromYML(File f) {
		List<ItemData> data = new ArrayList<>();
		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
		if(!f.exists()){
			System.out.println("File did not exist, returning null");
			return null;
		}
		ConfigurationSection sec = conf.getConfigurationSection("");
		Set<String> keys = sec.getKeys(false);
		for(String s : keys){
			Object obj = sec.get(s);
			if(obj instanceof ItemStack){
				data.add(new ItemData((ItemStack)obj, s));
			}
		}
		return data;
	}

	public static void addItemToConfig(String relativePath, String key, ItemStack item){
		addItemToConfig(new File(LamaCore.instance().getDataFolder().getAbsolutePath() + "/" + relativePath), key, item);
	}
	
	public static void addItemToConfig(File f, String key, ItemStack item){
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
		conf.set(key, item);
		try {
			conf.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public static ItemData getItemFromTXT(File txtFile){
		try {
			BufferedReader br = new BufferedReader(new FileReader(txtFile));
			String id = br.readLine();
			String itemData = br.readLine();
			if(id == null || itemData == null){
				br.close();
				return null;
			}
			String[] p = itemData.split(":");
			byte data = 0;
			if(p.length > 1){
				data = Byte.parseByte(p[1]);
			}
			Material mat = Material.getMaterial(p[0]);
			if(mat == null){
				mat = Material.getMaterial(Integer.parseInt(p[0]));
				if(mat == null){
					br.close();
					throw new Exception("Material type is invalid");
				}
			}
			ItemBuilder builder = new ItemBuilder(mat, data);
			String itemName = br.readLine();
			if(itemName == null){
				br.close();
				return new ItemData(builder.build(), id);
			}
			builder.setTitle(Util.translate(itemName));
			String temp = null;
			while((temp = br.readLine()) != null){
				builder.addLore(Util.translate(temp));
			}
			br.close();
			return new ItemData(builder.build(), id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<ItemData> getItemsFromXML(File xmlFile){
		if(!xmlFile.exists())
			return null;
		List<ItemData> items = new ArrayList<>();
		DocumentBuilder builder = null;
		try {
			 builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		NodeList itemList = null;
		try {
			Document doc = builder.parse(xmlFile);
			Element root = doc.getDocumentElement();
			itemList = root.getElementsByTagName("item");
		} catch (SAXException | IOException e) {
			System.err.println("Something went wrong while parsing the file " + xmlFile.getName());
			e.printStackTrace();
			return null;
		}
		if(itemList == null){
			System.err.println("Could not read item elements. You probably don't have defined an <items> tag!");
		} else {
//			System.out.println("Reading Items...");
			StringBuilder sb = new StringBuilder();
			boolean first = true;
			
//			System.out.println("having " + itemList.getLength());
			
			for(int i = 0; i < itemList.getLength(); i++){
				if(!first){
					sb.append(", ");
				} else {
					first = true;
				}
				Node node = itemList.item(i);
				ItemData data = getItemDataFromNode(node, null);
				items.add(data);				
			}
//			for(ItemData dat : items){
//				if(dat == null)
//					System.out.println("DAT:null");
//				else
//					System.out.println("DAT:"+dat.getId());
//			}
//			System.out.println("Successfully registered " + items.size() + " items!");
		}
		return items;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemData getItemDataFromNode(Node node, Integer index){
		if(node.getNodeType() != Node.ELEMENT_NODE)
			return null;
		ItemData itemData = null;
		Element elem = (Element) node;

		String id = elem.getAttribute("id");

		if (id == null || id.isEmpty()) {
			System.out.println("no id");
			return null;
		}

		Element name = (Element) elem.getElementsByTagName("name").item(0);
		Element matElem = (Element) elem.getElementsByTagName("material").item(0);
		if (matElem == null) {
			return null;
		}
		Material mat = Material.getMaterial(matElem.getTextContent());
		if (mat == null) {
			try {
				mat = Material.getMaterial(Integer.parseInt(matElem.getTextContent()));
				if (mat == null)
					throw new Exception();
			} catch (Exception ex) {
				System.err.println(
						"The Material " + matElem.getTextContent() + " does not exist! Skipping " + id);
				return null;
			}
		}
		Element data = (Element) elem.getElementsByTagName("data").item(0);
		Node enchantmentList = elem.getElementsByTagName("enchantments").item(0);
		Node loreList = elem.getElementsByTagName("lore").item(0);

		ItemBuilder build = new ItemBuilder(mat);
		if (data != null) {
			try {
				byte theData = Byte.parseByte(data.getTextContent());
				build.setData(theData);
			} catch (Exception ex) {
				System.err.println(
						"The data " + data.getTextContent() + " is not a valid number! Ignoring data of " + id);
			}
		}

		if (name != null) {
			if (!name.getTextContent().isEmpty()) {
				build.setTitle(Util.translate(name.getTextContent()));
			} else {
				System.err.println("Your item name is empty! Skipping " + id);
				return null;
			}
		}
		
		if (enchantmentList instanceof NodeList) {
			NodeList castedList = (NodeList) enchantmentList;
//			System.out.println("Length;" + castedList.getLength());
			for (int j = 0; j < castedList.getLength(); j++) {
				try {
					Element enchElem = (Element) castedList.item(j);
					int enchLevel = Integer.parseInt(enchElem.getAttribute("level"));
//					System.out.println("ench:" + enchElem.getTextContent());
					String enchName = enchElem.getTextContent();
					Enchantment ench = null;
					for (Enchantment e : Enchantment.values()) {
						if (e.getName().equalsIgnoreCase(enchName)) {
							ench = e;
							break;
						}
					}
					if (ench == null) {
						System.err.println("The enchantment " + enchElem.getTextContent() + " in item + " + id
								+ " is incorrect! Skipping enchantment.");
						continue;
					}
//					System.out.println("Adding enchantment " + ench.toString());
					build.addEnchant(ench, enchLevel);
				} catch (Exception ex) {

				}
			}
		}
		if (loreList instanceof NodeList) {
			NodeList castedList = (NodeList) loreList;
			for (int j = 0; j < castedList.getLength(); j++) {
				try {
					String loreLine = null;
					loreLine = ((Element) castedList.item(j)).getTextContent().trim();
					if (loreLine == null) {
						// System.err.println("The lore line #" + j + "
						// is empty or incorrect! Skipping it (needs at
						// least a space)");
						continue;
					} else {
						// System.out.println("Lore line:" + loreLine);
						build.addLore(loreLine);
					}
				} catch (Exception ex) {

				}
			}
		}
		ItemStack finalItem = null;
		Node tags = elem.getElementsByTagName("tags").item(0);
		if(tags != null && tags instanceof NodeList){
			NodeList tagList = (NodeList) tags;
			if(tagList.getLength() > 0){
				NBTItem nbtItem = new NBTItem(build.build());
				for(int j = 0; j < tagList.getLength(); j++){
					Node tag = tagList.item(j);
					if(tag.getNodeType() != Node.ELEMENT_NODE)
						continue;
					Element tagElem = (Element) tag;
					String tagKey = tagElem.getAttribute("key");//tagKeyNode.getNodeValue();
					String tagType = tagElem.getAttribute("type");
					System.out.println("Key: " + tagKey + " type:" + tagType + " content:" + tag.getTextContent());
					if(!setNbt(nbtItem, tagType, tagKey, tag.getTextContent()))
						System.err.println("Invalid NBT Tag with " + tagKey);
				}
				finalItem = nbtItem.getItem();
			}
		}
		if(finalItem == null)
			finalItem = build.build();
		itemData = new ItemData(finalItem, id);
		return itemData;
	}
	
	private static boolean setNbt(NBTItem item, String type, String key, String value){
		if(type.equalsIgnoreCase("String")){
			item.setString(key, value);
			return true;
		} else if(type.equalsIgnoreCase("int")){
			try {
				int val = Integer.parseInt(value);
				item.setInt(key, val);
				return true;
			} catch(Exception ex){
				return false;
			}
		} else if(type.equalsIgnoreCase("long")){
			try {
				long val = Long.parseLong(value);
				item.setLong(key, val);
				return true;
			} catch(Exception ex){
				return false;
			}
		} else if(type.equalsIgnoreCase("double")){
			try {
				double val = Double.parseDouble(value);
				item.setDouble(key, val);
				return true;
			} catch(Exception ex){
				return false;
			}
		}
		return false;
	}
	
	public static class ItemData {
		
		private ItemStack item;
		private String id;
		
		public ItemData(ItemStack item, String id) {
			Objects.nonNull(item);
			this.item = item.clone();
			this.id = id;
		}
		
		public ItemStack getItem() {
			return item.clone();
		}
		
		public String getId() {
			return id;
		}

		@Override
		public String toString() {
			return "ItemData [item=" + item.toString() + ", id=" + id + "]";
		}
		
	}
	
}


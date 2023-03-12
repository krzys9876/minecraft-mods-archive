package com.krzyskrzys;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class AreaBackup {
	
	private YamlConfiguration blockBackup;
	private File blockBackupFile;
	private String name;
	private ArrayList<Block> blockList;
	
	private Location location1;
	private Location location2;
	
	public AreaBackup(String nName, File dir) {
		name=nName;
		blockList=new ArrayList<Block>();
		
		try {
			blockBackupFile=new File(dir,name+".cfg");
			if(!blockBackupFile.exists()) {
				blockBackupFile.createNewFile();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		blockBackup=YamlConfiguration.loadConfiguration(blockBackupFile);
		
		location1=null;
		location2=null;
	}
	
	public void clearBlocks() {
		blockList.clear();
	}
	
	public void addBlock(Block bl) {
		blockList.add(bl);
	}
	
	public void addArea(Location nLoc1, Location nLoc2) {
		World world=nLoc1.getWorld();
		
		int x1=Math.min(nLoc1.getBlockX(), nLoc2.getBlockX());
		int y1=Math.min(nLoc1.getBlockY(), nLoc2.getBlockY());
		int z1=Math.min(nLoc1.getBlockZ(), nLoc2.getBlockZ());
		int x2=Math.max(nLoc1.getBlockX(), nLoc2.getBlockX());
		int y2=Math.max(nLoc1.getBlockY(), nLoc2.getBlockY());
		int z2=Math.max(nLoc1.getBlockZ(), nLoc2.getBlockZ());

		// tylko pomocniczo
		location1=new Location(world, x1, y1, z1);
		location2=new Location(world, x2, y2, z2);
				
		clearBlocks();
		
		for(int x=x1; x<=x2; x++) {
			for(int y=y1; y<=y2; y++) {
				for(int z=z1; z<=z2; z++) {
					addBlock(world.getBlockAt(x, y, z));
				}
			}
		}		
	}
	
	private String locationToString(Location loc) {
		return Integer.toString(loc.getBlockX())+"|"+Integer.toString(loc.getBlockY())+"|"+Integer.toString(loc.getBlockZ());
	}

	private Location locationFromString(World world, String str) {
		StringTokenizer strt=new StringTokenizer(str,"|");
		
		Location loc=null;
		
		try {
			int x=Integer.valueOf(strt.nextToken());
			int y=Integer.valueOf(strt.nextToken());
			int z=Integer.valueOf(strt.nextToken());
			
			loc=new Location(world,x,y,z);
		} catch(Exception e) {
			System.out.println("Location string error");
			return null;
		}
				
		return loc;
	}
	
	public int save() {
		ArrayList<String> strList=new ArrayList<String>();
		int cnt=0;
		
		for(Block bl : blockList) {
			strList.add(getBlockString(bl));
			if(bl.getType()==Material.CHEST) {
				Chest ch=(Chest)bl.getState();
				blockBackup.set("chests."+locationToString(bl.getLocation())+".size",ch.getInventory().getSize());
				for(int i=0;i<ch.getInventory().getSize();i++) {
					blockBackup.set("chests."+locationToString(bl.getLocation())+"."+Integer.toString(i),ch.getInventory().getItem(i));
				}				
			}
			cnt++;
		}
		blockBackup.set("blocks", strList);
		
		if(location1!=null && location2!=null) {
			blockBackup.set("location1", locationToString(location1));
			blockBackup.set("location2", locationToString(location2));
		}
		
		try {
			blockBackup.save(blockBackupFile);
		} catch(Exception ex) {
			ex.printStackTrace();
			return -1;
		}
		return cnt;
	}
	
	private String getBlockString(Block bl) {
		String str="";
		
		str=
			Integer.toString(bl.getLocation().getBlockX())+"|"+
			Integer.toString(bl.getLocation().getBlockY())+"|"+
			Integer.toString(bl.getLocation().getBlockZ())+"|"+
			bl.getType().name()+"|"+
			Byte.toString(bl.getData())+"|"+
			(bl.hasMetadata("TP_PROTECT_FLAG")?"Y":"N");
		
		return str;
	}
	
	private Block setBlockFromString(String str, World world, tpArenaPlugin plugin) {
		StringTokenizer strt=new StringTokenizer(str,"|");
		int x=Integer.valueOf(strt.nextToken()).intValue();
		int y=Integer.valueOf(strt.nextToken()).intValue();
		int z=Integer.valueOf(strt.nextToken()).intValue();
		Material mt=Material.getMaterial(strt.nextToken());
		byte bt=Byte.valueOf(strt.nextToken()).byteValue();
		boolean protect=strt.nextToken().equals("Y");

		Block bl=world.getBlockAt(x, y, z);
		bl.setType(mt);
		bl.setData(bt);
		if(protect && !bl.hasMetadata("TP_PROTECT_FLAG")) {
			bl.setMetadata("TP_PROTECT_FLAG", new FixedMetadataValue(plugin,"CUSTOM"));
		} else if(!protect && bl.hasMetadata("TP_PROTECT_FLAG")) {
			bl.removeMetadata("TP_PROTECT_FLAG",plugin);
		}		
		
		return bl;
	}
	
	public int load(World world, tpArenaPlugin plugin, boolean clearItems) {
		int cnt=0;
		
		try {
			blockBackup.load(blockBackupFile);
		} catch(Exception ev) {
			ev.printStackTrace();
			return -1;
		}

		List<String> strList=blockBackup.getStringList("blocks");
		
		ArrayList<Chest> chList=new ArrayList<Chest>(); 
		
		for(String str : strList) {
			//System.out.println(str);
			Block bl=setBlockFromString(str,world,plugin);
			if(bl.getType()==Material.CHEST) {
				chList.add((Chest)bl.getState());
			}
			cnt++;
		}
		
		// Zawartoœæ skrzyñ nale¿y odtwarzaæ osobno w przypadku podwójnych skrzyñ (najpierw nale¿y odtworzyæ same skrzynie)
		for(Chest ch : chList) {
			int chSize=blockBackup.getInt("chests."+locationToString(ch.getLocation())+".size");
			for(int i=0;i<chSize;i++) {
				ItemStack itemSt=blockBackup.getItemStack("chests."+locationToString(ch.getLocation())+"."+Integer.toString(i));
				//System.out.println(itemSt);
				if(itemSt!=null) {
					ch.getInventory().setItem(i, itemSt);
				}
			}
		}
		
		String strLoc1=blockBackup.getString("location1");
		String strLoc2=blockBackup.getString("location2");
		
		if(strLoc1!=null && strLoc2!=null) {
			location1=locationFromString(world, strLoc1);
			location2=locationFromString(world, strLoc2);
			
			if(clearItems) {
				clearItemsInArea(world, location1, location2);
			}
		}
		
		return cnt;
	}
	
	public static int clearItemsInArea(World world, Location nLoc1, Location nLoc2) {
		int x1=Math.min(nLoc1.getBlockX(), nLoc2.getBlockX());
		int y1=Math.min(nLoc1.getBlockY(), nLoc2.getBlockY());
		int z1=Math.min(nLoc1.getBlockZ(), nLoc2.getBlockZ());
		int x2=Math.max(nLoc1.getBlockX(), nLoc2.getBlockX());
		int y2=Math.max(nLoc1.getBlockY(), nLoc2.getBlockY());
		int z2=Math.max(nLoc1.getBlockZ(), nLoc2.getBlockZ());
		
		int cnt=0;
		
		// teoretycznie szybciej by³oby sprawdzaæ wybrane chunks, ale i tak zbiór powinien byæ ma³o liczny
		for(Item item : world.getEntitiesByClass(Item.class)) {
			System.out.println(item.getType()+"|"+item.getLocation());
			int x=item.getLocation().getBlockX();
			int y=item.getLocation().getBlockY();
			int z=item.getLocation().getBlockZ();
	        if(x>=x1 && x<=x2 && y>=y1 && y<=y2 && z>=z1 && z<=z2 ) {
	            item.remove();
	            cnt++;
	        }	        
		}				
		
		return cnt;
	}
	
	public Location getLocation1() {
		return location1;
	}

	public Location getLocation2() {
		return location2;
	}
}


package com.krzyskrzys;

//import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.krzyskrzys.utils.*;

import org.bukkit.plugin.java.*;
import org.bukkit.block.Block;
import org.bukkit.command.*;
import org.bukkit.entity.*;
//import org.bukkit.inventory.*;
import org.bukkit.*;
//import org.bukkit.util.*;
//import org.bukkit.block.*;
//import org.bukkit.enchantments.*;
//import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;


public class tpTextPlugin extends JavaPlugin {

	@Override
    public void onEnable(){
        // WprowadŸ tutaj kod, który ma zostaæ wykonany przy w³¹czeniu siê pluginu
		getLogger().info("tpKitsPlugin enabled");		
    }
 
    @Override
    public void onDisable() {
        // WprowadŸ tutaj kod, który ma zostaæ wykonany przy wy³¹czeniu siê pluginu
    	getLogger().info("tpKitsPlugin disabled");
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	Player player = null;
    	//PlayerInventory plInv = null;
    	    	
    	if(cmd.getName().equalsIgnoreCase("tpLine") ||
    	   cmd.getName().equalsIgnoreCase("tpPict") ||
    	   cmd.getName().equalsIgnoreCase("tpText") ||
    	   cmd.getName().equalsIgnoreCase("tpFontList")) {
    		player = (Player) sender;
    		if (!(sender instanceof Player)) {
    			sender.sendMessage("This command can only be run by a player.");
    			return false;
    		}
    	}
    	    	
    	if (cmd.getName().equalsIgnoreCase("tpLine")) {
			// tylko test
			World world = player.getWorld();
			Location pLoc = player.getLocation();
			double yaw=TpTextUtils.normalizeYaw(pLoc.getYaw());
			double pitch=pLoc.getPitch();
			
			System.out.println("Yaw: " + new Float(yaw).toString() + " Pitch: " + new Float(pitch).toString());
			

			Block bTarget=player.getTargetBlock((Set<Material>)null, 100);
			if(bTarget==null) {
				return true;				
			}			
			
			/*int sizeH=1;
			int sizeV=1;
			
			if(args.length>=2) {
				sizeH=Integer.valueOf(args[0]);
				sizeV=Integer.valueOf(args[1]);
			}*/
			
			Material mat=Material.SANDSTONE;			
			if(args.length>=1) {
				mat=Material.getMaterial(args[0].toUpperCase());
				if(mat==null) {
					mat=Material.SANDSTONE;
				}				
			}
			
			// tmp
			//bTarget.setType(mat);
			
			TpTextUtils.drawLine(pLoc.getBlockX(), pLoc.getBlockY(), pLoc.getBlockZ(),
						bTarget.getX(), bTarget.getY(), bTarget.getZ(), world, mat);
			
						
			/*for(int iV=0;iV<sizeV;iV++) {
				for(int iH=0;iH<sizeH;iH++) {
					//tLoc=bTarget.getLocation();
					tLoc=Utils.moveLoc(bTarget.getLocation(), yaw,  pitch, iH, iV);
					bl=world.getBlockAt(tLoc);
					if(bl!=null) {
						bl.setType(mat);
					}					
				}
			}*/
			
			/*System.out.println(player.toString());
			Location pLoc = player.getLocation();
			System.out.println(pLoc);
			List<Block> locs = player.getLineOfSight(null, 5);
			System.out.println(locs);
				for (Block b : locs) {
				if (b.getLocation().getY() == pLoc.getY() + 1) {
				b.setType(Material.STONE);
				}
			}*/
			
			return true;

		} else if (cmd.getName().equalsIgnoreCase("tpPict")) {
			if(args.length<1) {
				getLogger().info("brak nazwy pliku");
				return true;
			}			
			File fl=new File(this.getDataFolder(),args[0]);
			FileReader reader=null;
			BufferedReader breader=null;
			/*if(!fl.exists()) {
				try {
					fl.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}*/
			
			ArrayList<String> arr=new ArrayList<String>();
			
			try {
				
				reader=new FileReader(fl);		
				breader=new BufferedReader(reader);
				String line;
				while((line=breader.readLine())!=null) {
					arr.add(line);
					System.out.println(line + " - " + new Integer(line.length()).toString());
				}
			
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}					
	            }
			}
			
			System.out.println(arr);
    	
	    	World world = player.getWorld();
			Location pLoc = player.getLocation();
			double yaw=TpTextUtils.normalizeYaw(pLoc.getYaw());
			double pitch=pLoc.getPitch();
			
			System.out.println("Yaw: " + new Float(yaw).toString() + " Pitch: " + new Float(pitch).toString());
			
	
			Block bTarget=player.getTargetBlock((Set<Material>)null, 100);
			if(bTarget==null) {
				return true;				
			}
			System.out.println("Target: "+bTarget.toString());
			
			Map<String,Material> map=new HashMap<String,Material>();
			   
			map.put("A", Material.STONE);
			map.put("B", Material.DIRT);
			map.put("C", Material.GOLD_ORE);
			map.put("D", Material.LAPIS_BLOCK);
			map.put("E", Material.WOOL);
			map.put(".", Material.AIR);
			   
			//System.out.println(map.toString());
			
			drawPict(arr, map, bTarget, yaw, pitch, world);
			
			return true;
		} else if (cmd.getName().equalsIgnoreCase("tpText")) {
			if(args.length<3) {
				getLogger().info("brak argumentów");
				return true;
			}
			File flb=new File(this.getDataFolder(),args[0]+".bmp");
			File flm=new File(this.getDataFolder(),args[0]+".fnt");
			BitmapFont bFont=new BitmapFont(flb,flm);
			
			ArrayList<String> arr=new ArrayList<String>();
			
			boolean[][] charArr=bFont.getStringArr(args[1]);
			
			String row;
			
			for(int h=0;h<bFont.fontH;h++){
				row="";
				for(int w=0;w<bFont.fontW*args[1].length();w++){
					row=row+(charArr[w][h] ? args[2] : " ");
				}
				arr.add(row);
			}
			
			World world = player.getWorld();
			Location pLoc = player.getLocation();
			double yaw=TpTextUtils.normalizeYaw(pLoc.getYaw());
			double pitch=pLoc.getPitch();
			Block bTarget=player.getTargetBlock((Set<Material>)null, 100);
			if(bTarget==null) {
				return true;				
			}
			
			Map<String,Material> map=new HashMap<String,Material>();
			   
			map.put("A", Material.STONE);
			map.put("B", Material.DIRT);
			map.put("C", Material.GOLD_ORE);
			map.put("D", Material.LAPIS_BLOCK);
			map.put("E", Material.WOOL);
			map.put(".", Material.AIR);
			   
			drawPict(arr, map, bTarget, yaw, pitch, world);
			
			return true;
		} else if (cmd.getName().equalsIgnoreCase("tpFontList")) {
			if(args.length!=0) {
				getLogger().info("niepotrzebne argumenty");
				return true;
			}
			File dir=this.getDataFolder();
			
			if(!dir.isDirectory()) {				
				System.out.println(dir.toString()+" is not a directory");
				return false;
			} 
			File[] files=dir.listFiles();
			
			for (int i=0;i<files.length;i++) {
				String fname=files[i].getName();
				if(fname.endsWith(".fnt")) {
					player.sendMessage(files[i].getName());
					getLogger().info(files[i].getName());					
				}
			}			
			
			return true;
		}



    	return false;
    }    
    
    private void drawPict(ArrayList<String> arr, Map<String,Material> map, Block bTarget, double yaw, double pitch, World world) {

    	Location tLoc;
		Block bl;			
		Material mat;
		int sizeH=arr.size();
		for(int iV=0;iV<sizeH;iV++) {
			for(int iH=0;iH<arr.get(iV).length();iH++) {
				tLoc=TpTextUtils.moveLoc(bTarget.getLocation(), yaw,  pitch, iH, sizeH-iV-1);
				bl=world.getBlockAt(tLoc);			
				if(bl!=null) {
					mat=map.get(arr.get(iV).substring(iH, iH+1));
					if(mat!=null) {
						bl.setType(mat);
						if(mat==Material.WOOL) {		
							// losowy kolor we³ny, tylko test
							bl.setData((byte)new Random().nextInt(15));
						}
					}						
				}					
			}
		}

    	
    }    


}    




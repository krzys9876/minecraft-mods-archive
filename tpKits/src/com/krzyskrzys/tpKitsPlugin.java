package com.krzyskrzys;

//import java.awt.*;
import java.util.*;
//import java.io.*;

import com.krzyskrzys.utils.*;

import org.bukkit.plugin.java.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.*;
//import org.bukkit.util.*;
//import org.bukkit.block.*;
import org.bukkit.enchantments.*;
//import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;


public class tpKitsPlugin extends JavaPlugin {

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
    	PlayerInventory plInv = null;
    	    	
    	if(cmd.getName().equalsIgnoreCase("tpCount") ||
    	   //cmd.getName().equalsIgnoreCase("tpSpawn") ||
    	   cmd.getName().equalsIgnoreCase("tpKill") ||
    	   cmd.getName().equalsIgnoreCase("tpInvPrint") ||
    	   cmd.getName().equalsIgnoreCase("tpInvMove") || 
    	   cmd.getName().equalsIgnoreCase("tpInvEnch") ||
    	   cmd.getName().equalsIgnoreCase("tpKitNether") ||
    	   cmd.getName().equalsIgnoreCase("tpKitKrzys") ||
    	   cmd.getName().equalsIgnoreCase("tpKitMaksior") /* ||
    	   cmd.getName().equalsIgnoreCase("tpLine") ||
    	   cmd.getName().equalsIgnoreCase("tpPict") ||
    	   cmd.getName().equalsIgnoreCase("tpText")*/) {
    		player = (Player) sender;
    		if (!(sender instanceof Player)) {
    			sender.sendMessage("This command can only be run by a player.");
    			return false;
    		}
    	}
    	    	
    	if (cmd.getName().equalsIgnoreCase("tpCount")) {   			
   			World world = player.getWorld();
   			sender.sendMessage("all entities: "+Integer.toString(world.getEntities().size()));    			
   			sender.sendMessage("Pigs: "+Integer.toString(world.getEntitiesByClass(Pig.class).size()));
   			sender.sendMessage("Spiders: "+Integer.toString(world.getEntitiesByClass(Spider.class).size()));
   			sender.sendMessage("Zombies: "+Integer.toString(world.getEntitiesByClass(Zombie.class).size()));
   			sender.sendMessage("Creepers: "+Integer.toString(world.getEntitiesByClass(Creeper.class).size()));
   			sender.sendMessage("Skeletons: "+Integer.toString(world.getEntitiesByClass(Skeleton.class).size()));
   			sender.sendMessage("Bats: "+Integer.toString(world.getEntitiesByClass(Bat.class).size()));
   			sender.sendMessage("Villagers: "+Integer.toString(world.getEntitiesByClass(Villager.class).size()));
   			sender.sendMessage("Dragons: "+Integer.toString(world.getEntitiesByClass(EnderDragon.class).size()));
    		return true;
		} else if (cmd.getName().equalsIgnoreCase("tpCount2")) {
			getLogger().info("Sender: "+sender.toString());
			for(World w : sender.getServer().getWorlds()) {
				getLogger().info(w.toString());
				Collection<Entity> entColl=w.getEntities();
				Iterator<Entity> itr=entColl.iterator();
				while(itr.hasNext()) {
					Entity ent=(Entity)itr.next();					
					//getLogger().info(((CraftEntity)ent).getHandle().toString());
					getLogger().info(ent.toString());
				}
				//getLogger().info("Spawning Custom Pig");
				//Entity entity=w.spawnEntity(new Location(w,0,0,0), EntityType.PIG);
				//Entity entity=cw.spawn(new Location(w,0,0,0), CustomPig.class);
				//getLogger().info(((CraftEntity)entity).getHandle().toString());

				//getLogger().info("Spawning Custom Skeleton");
				//Entity entity2=w.spawnEntity(new Location(w,0,0,0), EntityType.SKELETON);
				//CustomSkeleton entity2=w.spawn(new Location(w,0,0,0), CustomSkeleton.class);
				//getLogger().info(((CraftEntity)entity2).getHandle().toString());	

			}
			
			return true;
		} else if (cmd.getName().equalsIgnoreCase("tpKill")) {
			if (args.length!=1) {
				sender.sendMessage("Zla liczba parametrow");
				return false;
			}
			World world=player.getWorld();
			getLogger().info(world.toString());
			for (Entity entity : world.getEntities()) {
				getLogger().info(entity.getCustomName()+" "+entity.toString());
				if(entity instanceof EnderDragon) {
					if(entity.getCustomName().equalsIgnoreCase(args[0])) {
						sender.sendMessage(entity.getCustomName() + " " + entity.toString());
						//((EnderDragon)entity).damage(1000000000); // nie dzia³a
						((EnderDragon)entity).remove();
					}
				}
			return true;
			}
		} else if (cmd.getName().equalsIgnoreCase("tpInvPrint")) {
			if (args.length==0) {
				sender.sendMessage("Zbyt malo parametrow");
				return false;
			}
			plInv=player.getInventory();
			if(args[0].equalsIgnoreCase("Armor")) {
				ItemStack[] istack=plInv.getArmorContents(); // tylko zbroja
				sender.sendMessage("Armor size "+Integer.valueOf(istack.length).toString());
				for(int i=0;i<istack.length;i=i+1) {
					sender.sendMessage("Armor "+Integer.toString(i)+" "+istack[i].getType().toString());
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("Inv")) {
				int invFrom=0;
				if(args.length>=2) {
					invFrom=Integer.valueOf(args[1]);
				}
				int invCnt=36;
				if(args.length>=3) {
					invCnt=Integer.valueOf(args[2]);
				}
				ListIterator<ItemStack> listInv=plInv.iterator(invFrom);
				ItemStack item=null;
				Material mat=null;
				//sender.sendMessage(listInv.size());
				int i=0;
				for(int j=0;listInv.hasNext() && j<invCnt;j++) {
					i=listInv.nextIndex();
					item=listInv.next();
					String txt;
					if(item==null) {
						mat=null;
						txt=null;
					} else {
						mat=item.getType();
						txt=mat.toString()+" amount: "+Integer.toString(item.getAmount())+" durability: "+Integer.toString(item.getDurability());
						Map<Enchantment,Integer> enchMap=item.getEnchantments();
						/*sender.sendMessage(enchMap.toString());*/
						for(Enchantment ench: enchMap.keySet()) {
							txt=txt+"|E|"+enchMap.get(ench).toString()+"|"+ench.toString()+"|"+ench.getMaxLevel()+" ";
						}
						
					}					
					sender.sendMessage("item index "+Integer.toString(i)+" num "+Integer.toString(j)+" "+txt);
				}
				return true;	
			}
		} else if (cmd.getName().equalsIgnoreCase("tpInvMove")) {
			if (args.length!=2) {
				sender.sendMessage("Zla liczba parametrow (inna niz 2)");
				return false;
			}
			plInv=player.getInventory();
			int indFrom=Integer.valueOf(args[0]);
			int indTo=Integer.valueOf(args[1]);
			ItemStack itemFrom=plInv.getItem(indFrom);
			ItemStack itemTo=plInv.getItem(indTo);
			plInv.clear(indFrom);
			plInv.clear(indTo);
			if(itemFrom!=null) {
				plInv.setItem(indTo,itemFrom);
			} 
			if (itemTo!=null) {
				plInv.setItem(indFrom,itemTo);
			} 			
			sender.sendMessage("items moved between slots "+Integer.toString(indFrom)+" and "+Integer.toString(indTo));				
			return true;
		} else if (cmd.getName().equalsIgnoreCase("tpInvEnch")) {
			if (args.length!=2) {
				sender.sendMessage("Zla liczba parametrow (inna niz 2)");
				return false;
			}
			plInv=player.getInventory();
			int ind=Integer.valueOf(args[0]);
			String enchName=args[1];
			ItemStack item=plInv.getItem(ind);
			Enchantment ench=Enchantment.getByName(enchName);
			if(ench==null) {
				sender.sendMessage("no enchantment found for name "+enchName);
				return true;
			}
			item.addEnchantment(ench, ench.getMaxLevel());
			sender.sendMessage("added enchantment "+ench.toString()+" to index "+Integer.valueOf(ind));			 		
			return true;
		} else if (cmd.getName().equalsIgnoreCase("tpKitNether")) {
			plInv=player.getInventory();
						
			TpKitsUtils.createItem(plInv,Material.DIAMOND_SWORD,1,0);			
			TpKitsUtils.createItem(plInv,Material.IRON_PICKAXE,1,1);
			
			TpKitsUtils.createEnchantedItem(plInv,Material.BOW,
					new Enchantment[] {Enchantment.ARROW_INFINITE},
					2);			

			TpKitsUtils.createItem(plInv,Material.DIRT,64,3);
			TpKitsUtils.createItem(plInv,Material.TORCH,64,4);			
			TpKitsUtils.createItem(plInv,Material.TNT,64,5);
			TpKitsUtils.createItem(plInv,Material.FLINT_AND_STEEL,1,6);			
			
			
			plInv.setHelmet(TpKitsUtils.enchantItemMultiF(new ItemStack(Material.IRON_HELMET),
					new Enchantment[] {Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_FALL,
					Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_ENVIRONMENTAL}));
						
			plInv.setChestplate(TpKitsUtils.enchantItemMultiF(new ItemStack(Material.IRON_CHESTPLATE),
					new Enchantment[] {Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_FALL,
					Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_ENVIRONMENTAL}));
			
			plInv.setLeggings(TpKitsUtils.enchantItemMultiF(new ItemStack(Material.IRON_LEGGINGS),
					new Enchantment[] {Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_FALL,
					Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_ENVIRONMENTAL}));
			
			plInv.setBoots(TpKitsUtils.enchantItemMultiF(new ItemStack(Material.IRON_BOOTS),
					new Enchantment[] {Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_FALL,
					Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_ENVIRONMENTAL}));
			
			if(!plInv.contains(Material.ARROW)) {
				int firstEmpty=plInv.firstEmpty();
				if(firstEmpty!=-1) {				
					plInv.clear(firstEmpty);					
					plInv.setItem(firstEmpty, new ItemStack(Material.ARROW,1));
				}
			}
			return true;
		} else if (cmd.getName().equalsIgnoreCase("tpKitKrzys")) {
			plInv=player.getInventory();
						
			TpKitsUtils.createEnchantedItem(plInv,Material.DIAMOND_SWORD,
					new Enchantment[] {Enchantment.DAMAGE_ALL,Enchantment.KNOCKBACK,Enchantment.FIRE_ASPECT},
					0);
			
			TpKitsUtils.createEnchantedItem(plInv,Material.DIAMOND_PICKAXE,
					new Enchantment[] {Enchantment.DURABILITY,Enchantment.DIG_SPEED,Enchantment.LUCK},
					1);			
			
			TpKitsUtils.createEnchantedItem(plInv,Material.BOW,
					new Enchantment[] {Enchantment.ARROW_DAMAGE,Enchantment.ARROW_FIRE,Enchantment.ARROW_INFINITE,Enchantment.ARROW_KNOCKBACK},
					2);
			
			ItemStack item4=new ItemStack(Material.GOLDEN_APPLE,64);
			try { item4.setDurability((short)1);
			} catch(Exception e) {getLogger().info("golden apple");}
			plInv.clear(4);
			plInv.setItem(4, item4);

			
			plInv.setHelmet(TpKitsUtils.enchantItemMultiF(new ItemStack(Material.DIAMOND_HELMET),new Enchantment[] {Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_FALL,
					Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_ENVIRONMENTAL}));
						
			
			plInv.setChestplate(TpKitsUtils.enchantItemMultiF(new ItemStack(Material.DIAMOND_CHESTPLATE),new Enchantment[] {Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_FALL,
				Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_ENVIRONMENTAL}));
						
			plInv.setLeggings(TpKitsUtils.enchantItemMultiF(new ItemStack(Material.DIAMOND_LEGGINGS),new Enchantment[] {Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_FALL,
				Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_ENVIRONMENTAL}));
			
			plInv.setBoots(TpKitsUtils.enchantItemMultiF(new ItemStack(Material.DIAMOND_BOOTS),new Enchantment[] {Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_FALL,
				Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_ENVIRONMENTAL}));		
			
			if(!plInv.contains(Material.ARROW)) {
				int firstEmpty=plInv.firstEmpty();
				if(firstEmpty!=-1) {				
					plInv.clear(firstEmpty);					
					plInv.setItem(firstEmpty, new ItemStack(Material.ARROW,1));
				}
			}
			return true;
		} else if (cmd.getName().equalsIgnoreCase("tpKitMaksior")) {
			plInv=player.getInventory();
			
			TpKitsUtils.createEnchantedItem(plInv,Material.DIAMOND_SWORD,
					new Enchantment[] {Enchantment.DAMAGE_ALL,Enchantment.KNOCKBACK,Enchantment.FIRE_ASPECT},
					0);

			TpKitsUtils.createEnchantedItem(plInv,Material.BOW,
					new Enchantment[] {Enchantment.ARROW_DAMAGE,Enchantment.ARROW_FIRE,Enchantment.ARROW_INFINITE,Enchantment.ARROW_KNOCKBACK},
					1);

			TpKitsUtils.createEnchantedItem(plInv,Material.DIAMOND_PICKAXE,
					new Enchantment[] {Enchantment.DURABILITY,Enchantment.DIG_SPEED,Enchantment.LOOT_BONUS_BLOCKS},
					2);

			TpKitsUtils.createItem(plInv,Material.COOKED_BEEF,64,3);
			
			ItemStack item4=new ItemStack(Material.GOLDEN_APPLE,64);
			try { item4.setDurability((short)1);
			} catch(Exception e) {getLogger().info("golden apple");}
			plInv.clear(4);
			plInv.setItem(4, item4);
			
			TpKitsUtils.createItem(plInv,Material.COBBLESTONE,64,5);
			TpKitsUtils.createItem(plInv,Material.TORCH,64,6);
			TpKitsUtils.createItem(plInv,Material.FLINT_AND_STEEL,1,7);
			TpKitsUtils.createItem(plInv,Material.TNT,64,8);

			TpKitsUtils.createEnchantedItem(plInv,Material.DIAMOND_AXE,
					new Enchantment[] {Enchantment.DURABILITY,Enchantment.DIG_SPEED,Enchantment.LOOT_BONUS_BLOCKS},
					9);

			TpKitsUtils.createEnchantedItem(plInv,Material.DIAMOND_SPADE,
					new Enchantment[] {Enchantment.DURABILITY,Enchantment.DIG_SPEED,Enchantment.LOOT_BONUS_BLOCKS},
					10);

			plInv.setHelmet(TpKitsUtils.enchantItemMultiF(new ItemStack(Material.DIAMOND_HELMET),new Enchantment[] {Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_FALL,
				Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_ENVIRONMENTAL,
				Enchantment.THORNS}));
						
			plInv.setChestplate(TpKitsUtils.enchantItemMultiF(new ItemStack(Material.DIAMOND_CHESTPLATE),new Enchantment[] {Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_FALL,
				Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_ENVIRONMENTAL,
				Enchantment.THORNS}));
			
			plInv.setLeggings(TpKitsUtils.enchantItemMultiF(new ItemStack(Material.DIAMOND_LEGGINGS),new Enchantment[] {Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_FALL,
				Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_ENVIRONMENTAL,
				Enchantment.THORNS}));
			
			plInv.setBoots(TpKitsUtils.enchantItemMultiF(new ItemStack(Material.DIAMOND_BOOTS),new Enchantment[] {Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_FALL,
				Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_ENVIRONMENTAL,
				Enchantment.WATER_WORKER}));						
			
			if(!plInv.contains(Material.ARROW)) {
				int firstEmpty=plInv.firstEmpty();
				if(firstEmpty!=-1) {				
					ItemStack itemArrow=new ItemStack(Material.ARROW,1);
					plInv.clear(firstEmpty);
					plInv.setItem(firstEmpty, itemArrow);
				}
			}
			
			return true;
			
		}


    	return false;
    }
    

}    



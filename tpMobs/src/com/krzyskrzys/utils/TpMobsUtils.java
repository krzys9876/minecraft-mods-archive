package com.krzyskrzys.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class TpMobsUtils {

	public static void main(String[] args) {
	}
	
	public static boolean enchantItemMulti(ItemStack item, Enchantment[] enchArr) {    	
		Enchantment ench;
		for(int i=0; i<enchArr.length; i++) {
			ench=enchArr[i];
			if(ench.canEnchantItem(item)) {
				try {
					item.addEnchantment(ench, ench.getMaxLevel());
				} catch(Exception e) {
					System.out.println("niedozwolone (exception): "+ench.toString()+"|"+item.toString());
				}    			
			} else {
				//System.out.println("niedozwolone: "+ench.toString()+"|"+item.toString());    			
			}
		}
	
		return true;
	}
	
	public static ItemStack enchantItemMultiF(ItemStack item, Enchantment[] enchArr) {
		enchantItemMulti(item, enchArr);
		return item;
	}
	
	public static boolean createEnchantedItem(PlayerInventory plInv, Material mat, Enchantment[] enchArr, int slot) {    	
	
		ItemStack item=new ItemStack(mat);
	    enchantItemMulti(item,enchArr);
	    plInv.clear(slot);
	    plInv.setItem(slot, item);
	    
		return true;
	}
	
	public static boolean createItem(PlayerInventory plInv, Material mat, int quantity, int slot) {    	
	
		ItemStack item=new ItemStack(mat,quantity);
	    plInv.clear(slot);
	    plInv.setItem(slot, item);
	    
		return true;
	}
	
		
}
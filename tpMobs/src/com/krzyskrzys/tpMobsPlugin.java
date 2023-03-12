package com.krzyskrzys;

//import java.awt.*;
//import java.util.*;
//import java.io.*;

//import com.krzyskrzys.utils.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.*;
//import org.bukkit.command.*;
//import org.bukkit.entity.*;
//import org.bukkit.inventory.*;
//import org.bukkit.*;
//import org.bukkit.util.*;
//import org.bukkit.block.*;
//import org.bukkit.enchantments.*;
//import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;


public class tpMobsPlugin extends JavaPlugin {

	@Override
    public void onEnable(){
        // WprowadŸ tutaj kod, który ma zostaæ wykonany przy w³¹czeniu siê pluginu
		System.out.println("tpMobsPlugin registerEntities");
		CustomEntityType.registerEntities();		
		getLogger().info("tpMobsPlugin enabled");		
    }
 
    @Override
    public void onDisable() {
        // WprowadŸ tutaj kod, który ma zostaæ wykonany przy wy³¹czeniu siê pluginu
    	CustomEntityType.unregisterEntities();
    	getLogger().info("tpMobsPlugin disabled");
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if(cmd.getName().equalsIgnoreCase("tpMobsStart")) {
    			CustomEntityType.registerEntities();
    			return true;
    		
    	} else if(cmd.getName().equalsIgnoreCase("tpMobsStop")) {
			CustomEntityType.unregisterEntities();
			return true;
		
    	} 
    	return false;
    }
    
}    



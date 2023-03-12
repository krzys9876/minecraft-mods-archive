package com.krzyskrzys.logo;

//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
import java.util.StringTokenizer;

import org.bukkit.Location;
//import org.bukkit.Material;
import org.bukkit.World;
//import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.krzyskrzys.logo.LogoProgram;

public class tpLogoPlugin extends JavaPlugin {
	
	private LogoOutputMC logoOut; // na przysz�o�� mo�na mie� hash map� per gracz, albo wiele niezale�nych logo output 
	
	public tpLogoPlugin() {
		super();
		logoOut=new LogoOutputMC();
	}
	
	@Override
    public void onEnable(){
        // Wprowad� tutaj kod, kt�ry ma zosta� wykonany przy w��czeniu si� pluginu
		getLogger().info("tpLogoPlugin enabled");		
    }
 
    @Override
    public void onDisable() {
        // Wprowad� tutaj kod, kt�ry ma zosta� wykonany przy wy��czeniu si� pluginu
    	getLogger().info("tpLogoPlugin disabled");
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	Player player = null;
    	World world = null;
    	    	
    	if(cmd.getName().equalsIgnoreCase("tpLogoInit") ||
    	   cmd.getName().equalsIgnoreCase("tpLogoProg") ||
    	   cmd.getName().equalsIgnoreCase("tpLogoExec")) {
    		player = (Player) sender;
    		world = player.getWorld();
    		if (!(sender instanceof Player)) {
    			sender.sendMessage("This command can only be run by a player.");
    			return false;
    		}
    	}
    	    	
    	if (cmd.getName().equalsIgnoreCase("tpLogoInit")) {
			
    		if (args.length>01) {
        		sender.sendMessage("Niepotrzebne parametry");
        		return false;
        	}
    			
    		Location pLoc=player.getLocation();
    		logoOut.setWorld(world);
    		logoOut.setPos(pLoc.getX(), pLoc.getY(), pLoc.getZ());
    		logoOut.setAngle(0, 0);
    									
			return true;

		} else if (cmd.getName().equalsIgnoreCase("tpLogoExec")) {
			if (args.length<1) {
    			sender.sendMessage("Za ma�o parametr�w");
    			return false;
    		}
			
			String prog=args[0];
			for(int i=1;i<args.length;i++) {
				prog=prog+" "+args[i];
			}
			
			StringTokenizer strTok=new StringTokenizer(prog,";");
			
			ArrayList<String> progArr=new ArrayList<String>();
			while(strTok.hasMoreTokens()) {
				progArr.add(strTok.nextToken());
			}
			
			String[] strArr=new String[]{};
			strArr=progArr.toArray(strArr);
			System.out.println(strArr);
			LogoProgram logoProg=new LogoProgram(strArr);
			logoProg.exec(logoOut);
			
			if(logoProg.getResult()) {
				player.sendMessage("ok");
			} else {
				player.sendMessage("error");
			}
			    				
			return true;
		} else if (cmd.getName().equalsIgnoreCase("tpLogoProg")) {
			if (args.length!=1) {
    			sender.sendMessage("nieprawid�owa liczba parametr�w");
    			return false;
    		}
			
			File fl=new File(this.getDataFolder(),args[0]);
			System.out.println(fl);
			LogoProgram logoProg=new LogoProgram(fl);
			
			logoProg.exec(logoOut);
			
			if(logoProg.getResult()) {
				player.sendMessage("ok");
			} else {
				player.sendMessage("error");
			}
			
			return true;
		}  
  

    	return false;
    }


}

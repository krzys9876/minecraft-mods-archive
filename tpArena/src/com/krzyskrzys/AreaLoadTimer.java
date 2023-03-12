package com.krzyskrzys;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AreaLoadTimer extends BukkitRunnable {
	
	private String areaName;
	private Player player;
	private World world;
	private tpArenaPlugin plugin;
	
	public AreaLoadTimer(String name, tpArenaPlugin nPlugin, Player nPlayer, World nWorld) {
		areaName=name;
		plugin=nPlugin;
		player=nPlayer;
		world=nWorld;
	}

	public void run() {
		if(player!=null) {
			player.sendMessage(ChatColor.AQUA+"Start loading area "+ChatColor.RED+areaName+ChatColor.AQUA+".");
		}
		
		AreaBackup blBackup=new AreaBackup(areaName,plugin.getDataFolder());		
		int blockCnt=blBackup.load(world, plugin, true);
		
		if(blockCnt==-1) {
			if(player!=null) {
				player.sendMessage(ChatColor.RED+"Error while loading area");
			}
			return;
		}
		
		if(player!=null) {
			player.sendMessage(ChatColor.AQUA+"Area "+ChatColor.RED+areaName+ChatColor.AQUA+" loaded: "+
						ChatColor.RED+Integer.toString(blockCnt)+ChatColor.AQUA+" blocks.");
		}
	}
}
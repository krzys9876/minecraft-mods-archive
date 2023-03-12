package com.krzyskrzys;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportTimer extends BukkitRunnable {
	
	private PlayerInGame plg;
	private Location location;
	private int seconds;
	
	private int timer;
	
	public TeleportTimer(PlayerInGame nPlg, Location loc, int sec) {
		plg=nPlg;
		location=loc;
		seconds=sec;
		timer=0;
	}
	
	public void run() {
		plg.player.sendMessage(ChatColor.AQUA+"Teleporting "+ChatColor.RED+plg.playerName+ChatColor.AQUA+
					" in "+ChatColor.RED+Integer.toString(seconds-timer)+ChatColor.AQUA+" sec.");	
		
		if(timer>=seconds) {
			plg.finishTeleportTimer();
			cancel();
			return;
		}
		timer++;
	}
	
	public Location getLocation() {
		return location;		
	}
}

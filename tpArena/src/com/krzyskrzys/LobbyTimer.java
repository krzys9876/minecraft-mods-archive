package com.krzyskrzys;

import org.bukkit.scheduler.BukkitRunnable;

public class LobbyTimer extends BukkitRunnable {
	
	private int timer;
	
	public LobbyTimer() {
		timer=0;		
	}
	
	public void run() {
		timer++;
		System.out.println(getTaskId()+" "+timer);
		
		if(timer>=10) {
			cancel();
		}
	}

	
	public String toString() {
		return "lobbyTimer,ID="+Integer.toString(getTaskId());
	}
}

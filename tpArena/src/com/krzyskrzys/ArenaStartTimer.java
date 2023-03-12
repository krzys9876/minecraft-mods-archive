package com.krzyskrzys;

import org.bukkit.scheduler.BukkitRunnable;

public class ArenaStartTimer extends BukkitRunnable {

	private int seconds;
	private Game game;

	private int timer;
	
	public ArenaStartTimer(Game nGame, int sec) {
		game=nGame;
		seconds=sec;
		timer=0;
	}
	
	public void run() {
		game.setArenaStartInfo(seconds-timer);	
	
		if(timer>=seconds) {
			game.startArena();
			cancel();
			return;
		}
		timer++;		
	}
}

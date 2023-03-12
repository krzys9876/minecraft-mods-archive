package com.krzyskrzys;

import org.bukkit.scheduler.BukkitRunnable;

public class FightStartTimer extends BukkitRunnable {

	private int seconds;
	private Game game;

	private int timer;
	
	public FightStartTimer(Game nGame, int sec) {
		game=nGame;
		seconds=sec;
		timer=0;
	}
	
	public void run() {
		game.setFightStartInfo(seconds-timer);	
	
		if(timer>=seconds) {
			game.startFight();
			cancel();
			return;
		}
		timer++;		
	}
}

package com.krzyskrzys;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;

public class PlayerInGame {
	public Game.State state;
	public PlayerBoundaries boundaries=null;
	public Player player;
	public String playerName;
	public Team team;
	public Scoreboard scoreboard=null;
	public TeleportTimer tpTimer=null;
	
	public enum Team {RED, BLUE, NONE};
	
	public PlayerInGame(Player nPlayer) {
		state=Game.State.NONE;
		team=PlayerInGame.Team.NONE;
		player=nPlayer;
		playerName=player.getName();
	}
	
	public void finishTeleportTimer() {
		player.teleport(tpTimer.getLocation());
		tpTimer=null;
		
	}
	
	public boolean startTeleportTimer(Plugin pl, Location location, int seconds) {
		if(tpTimer!=null) {
			return false;
		}
		
		tpTimer=new TeleportTimer(this,location,seconds);
		// start od razu z krokiem co sekundê (20L to 20 jednostek serwera, tyle mieœci siê w sekundzie)
		tpTimer.runTaskTimer(pl, 0, 20L);
		
		return true;
	}
}
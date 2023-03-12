package com.krzyskrzys;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.krzyskrzys.utils.Cube;

public class Game {
	
	private Plugin plugin;
	
	public enum State {NONE, LOBBY, WAITING, PREPARE, FIGHT, END};
		
	public Cube lobbyCube=null;
	public Cube waitingRoomCube=null;
	public Cube arenaCube=null;
	public Cube teamRedCube=null;
	public Cube teamBlueCube=null;
	public Scoreboard scoreboard=null;
	public Score[] scores;
	
	public Location lobbyPoint=null;
	public Location waitingRoomPoint=null;
	public Location teamRedPoint=null;
	public Location teamBluePoint=null;
	public int teamRedMin=1;
	public int teamBlueMin=1;
	public int teamRedMax=2;
	public int teamBlueMax=2;
	
	public String waitingRoomTitle="";
	public String teamRedTitle="";
	public String teamBlueTitle="";
	
	private ArenaStartTimer arenaStartTimer=null;
	private FightStartTimer fightStartTimer=null;
	
	public String name;
	
	public State gameState;
	
	public HashMap<String,PlayerInGame> playerMap;
		
	public Game(Plugin plug, String nName) {
		plugin=plug;
		name=nName;
		gameState=Game.State.LOBBY;
		playerMap=new HashMap<String,PlayerInGame>();		
	}
	
	public void initScoreboard() {
		if(scoreboard==null) {
			return;
		}
		
		Objective obj=scoreboard.registerNewObjective("lobby", "dummy");
		obj.setDisplayName(ChatColor.YELLOW+"WALLS GAME");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		scores=new Score[10];
		String scTxt=" ";
		
		for(int i=0; i<10; i++) {
			//scores[i]=obj.getScore(Integer.toString(i+1));
			scores[i]=obj.getScore(scTxt);			
			scores[i].setScore(i+1);
			scTxt=scTxt+" ";
		}
		
		/*Score score;
		
		score=obj.getScore("");
		score.setScore(4);		
		score=obj.getScore(ChatColor.AQUA+"Choose the arena");
		score.setScore(3);
		score=obj.getScore(ChatColor.AQUA+"and");
		score.setScore(2);
		score=obj.getScore(ChatColor.AQUA+"good luck ;)");
		score.setScore(1);*/
	}
	
	public void clearScoreboard() {
		if(scoreboard==null) {
			return;
		}
		
		for(PlayerInGame plg : playerMap.values()) {
			plg.player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		}
	}
	
	
	public PlayerInGame getPlayerInGame(Player pl, boolean addPlayer) {
		PlayerInGame plg=playerMap.get(pl.getName());
		if(plg==null && addPlayer) {
			plg=new PlayerInGame(pl);
			playerMap.put(pl.getName(), plg);
		}
		return plg;		
	}
	
	public void setPlayerState(Player pl, Game.State plState) {
		PlayerInGame plg=getPlayerInGame(pl,true);
		switch(plState) {
			case LOBBY: {
				pl.setScoreboard(scoreboard);
				if(lobbyPoint!=null) {
					if(!plg.startTeleportTimer(plugin,lobbyPoint,2)) {
						return;
					};
					plg.state=plState;
				}
				return;
			}
			case WAITING: {
				//System.out.println("waiting");
				//pl.setScoreboard(scoreboard);
				if(waitingRoomPoint!=null) {
					if(!plg.startTeleportTimer(plugin,waitingRoomPoint,3)) {
						return;
					};
					plg.state=plState;
				}
				return;
			}
			default:			
		}
	}
	
	public boolean isPlaying() {
		return gameState==Game.State.PREPARE || gameState==Game.State.FIGHT || gameState==Game.State.END;		
	}
	
	public void leaveTeam(Player pl) {
		PlayerInGame plg=getPlayerInGame(pl,false);
		if(plg==null) {
			// b³¹d
			return;
		}
		if(plg.team==PlayerInGame.Team.RED || plg.team==PlayerInGame.Team.BLUE) {
			plg.team=PlayerInGame.Team.NONE;
			//TODO: info o zespole
		}
	}
	
	public void joinRedTeam(Player pl) {
		// tymczasowo
		if(teamRedPoint==null) {
			// punkt nieustawiony
			return;
		}
		PlayerInGame plg=getPlayerInGame(pl,false);
		if(plg==null) {
			// sytuacja b³êdna
			return;
		}
		plg.team=PlayerInGame.Team.RED;
		checkTeams();
	}

	public void joinBlueTeam(Player pl) {
		if(teamBluePoint==null) {
			// punkt nieustawiony
			return;
		}
		PlayerInGame plg=getPlayerInGame(pl,false);
		if(plg==null) {
			// sytuacja b³êdna
			return;
		}
		plg.team=PlayerInGame.Team.BLUE;
		checkTeams();		
	}
	
	private void checkTeams() {
		// sprawdzenie, czy mo¿na zaczynaæ grê
		int redCnt=0;
		int blueCnt=0;
		
		for(PlayerInGame plg : playerMap.values()) {
			switch(plg.team) {
				case RED:
					redCnt++;
					System.out.println("RED: "+plg.playerName);
					break;
				case BLUE:
					blueCnt++;
					System.out.println("BLUE: "+plg.playerName);
					break;	
				default:
			}
		}
		System.out.println("RED cnt: "+Integer.toString(redCnt)+" BLUE cnt: "+Integer.toString(blueCnt));		
		if(redCnt<teamRedMin || blueCnt<teamBlueMin) { // zmieniæ na ||
			// nic siê nie dzieje			
			return;
		}
		
		// rozpoczêcie gry (z opóŸnieniem)
		arenaStartTimer=new ArenaStartTimer(this,10);
		arenaStartTimer.runTaskTimer(plugin, 0, 20L);
		/*for(PlayerInGame plg : playerMap.values()) {
			switch(plg.team) {
				case RED:
					plg.player.teleport(teamRedPoint);
					break;
				case BLUE:
					plg.player.teleport(teamBluePoint);
					break;	
				default:
			}
		}*/
		
	}
	
	public void setFightStartInfo(int seconds) {
		// tymczasowo
		Objective obj=scores[8].getObjective();
		scoreboard.resetScores(scores[8].getEntry());
		scores[8]=obj.getScore(ChatColor.AQUA+"The wall falls in "+ChatColor.WHITE+Integer.toString(seconds));
		scores[8].setScore(9);
	}
	
	public void setArenaStartInfo(int seconds) {
		/*for(PlayerInGame plg : playerMap.values()) {
			if(plg.team!=PlayerInGame.Team.NONE) {
				plg.player.sendMessage(ChatColor.AQUA+"Game starts in "+ChatColor.RED+Integer.toString(seconds)+ChatColor.AQUA+" sec.");
			}
		}*/
		// tymczasowo
		Objective obj=scores[8].getObjective();
		scoreboard.resetScores(scores[8].getEntry());
		scores[8]=obj.getScore(ChatColor.AQUA+"Game starts in "+ChatColor.WHITE+Integer.toString(seconds));
		scores[8].setScore(9);
	}
	
	public void startArena() {
		
		for(PlayerInGame plg : playerMap.values()) {
			switch(plg.team) {
				case RED:
					plg.player.teleport(teamRedPoint);
					break;
				case BLUE:
					plg.player.teleport(teamBluePoint);
					break;	
				default:
			}
		}
		
		gameState=State.PREPARE;
		arenaStartTimer=null;

		// rozpoczêcie przygotowañ (tymczasowo)
		fightStartTimer=new FightStartTimer(this,7*60);
		fightStartTimer.runTaskTimer(plugin, 0, 20L);

		
		
		// TODO: licznik przygotowañ itd.
	}
	
	public void startFight() {
		//TODO
		gameState=State.FIGHT;
		fightStartTimer=null;
		
		// tymczasowo
		Objective obj=scores[8].getObjective();
		scoreboard.resetScores(scores[8].getEntry());
		scores[8]=obj.getScore(ChatColor.AQUA+"FIGHT!");
		scores[8].setScore(9);


		World world=teamRedPoint.getWorld();

		// tymczasowo, mo¿na to zrobiæ losowo i w pêtli
		for(int x=arenaCube.getLocation1().getBlockX();x<=arenaCube.getLocation2().getBlockX();x++) {
			for(int y=arenaCube.getLocation1().getBlockY();y<=arenaCube.getLocation2().getBlockY();y++) {
				for(int z=arenaCube.getLocation1().getBlockZ();z<=arenaCube.getLocation2().getBlockZ();z++) {

					if(world.getBlockAt(x, y, z).getType()==Material.WOOL && world.getBlockAt(x, y, z).getData()==10) {
						world.getBlockAt(x, y, z).setType(Material.AIR);
						world.getBlockAt(x, y, z).removeMetadata("TP_PROTECT_FLAG", plugin);
					}
				}
			}
		}
		
	}

}

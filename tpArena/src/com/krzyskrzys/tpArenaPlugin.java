package com.krzyskrzys;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
//import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
//import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
//import org.bukkit.scheduler.BukkitTask;
import org.bukkit.Location;

import com.krzyskrzys.utils.Cube;

public class tpArenaPlugin extends JavaPlugin implements Listener {
	
	private HashMap<String,PlayerBoundaries> playerBound;
	private HashMap<String,Location> customLoc1;
	private HashMap<String,Location> customLoc2;	
	
	private ScoreboardManager scoreManager;
	private Scoreboard scoreboard;
	//private Team teamRed;
	//private Team teamBlue;
	
	private Game game;

	private ArrayList<Integer> lobbyTimers;
	
	private HashMap<String,Location> gameLocations;
	
	
	@Override
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		playerBound=new HashMap<String,PlayerBoundaries>();
		customLoc1=new HashMap<String,Location>();
		customLoc2=new HashMap<String,Location>();
		lobbyTimers=new ArrayList<Integer>();
		gameLocations=new HashMap<String,Location>();		
		
		//bb=new AreaBackup("bl1",getDataFolder());
		//bb2=new AreaBackup("bl2",getDataFolder());
		
		/*scoreManager=Bukkit.getScoreboardManager();
		scoreboard=scoreManager.getMainScoreboard();
		teamRed=scoreboard.registerNewTeam("Red");
		teamBlue=scoreboard.registerNewTeam("Blue");
		
		teamRed.setPrefix(ChatColor.RED+"#");
		teamRed.setSuffix(ChatColor.RED+"#");
		teamBlue.setPrefix(ChatColor.BLUE+"#");
		teamBlue.setSuffix(ChatColor.BLUE+"#");*/
		
		game=null;
		
		System.out.println("tpArenaPlugin enabled");
	}

	@Override
	public void onDisable() {
		System.out.println("tpArenaPlugin disabled");
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (e.isCancelled()) return;
		
		if (e.getFrom().getBlockX() != e.getTo().getBlockX()
				|| e.getFrom().getBlockY() != e.getTo().getBlockY()
				|| e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
			Location evLoc=e.getTo();
			
			String plName=e.getPlayer().getName();
			
			if(!playerBound.containsKey(plName.toUpperCase())) return;
			PlayerBoundaries plBound=playerBound.get(plName.toUpperCase());
			if(plBound==null) return;
			
			if(!plBound.covers(evLoc)) {
				Location toLoc=plBound.toNearestLocation(evLoc,true);
				e.getPlayer().teleport(toLoc);
				System.out.println(plName+" teleported");
			}
			
			/*Location loc=plBound.getLocation();								
			double radius=plBound.getSizeH();
			
			if(Math.abs(evLoc.getX()-loc.getX())>=radius || Math.abs(evLoc.getZ()-loc.getZ())>=radius) {
				Location toLoc=e.getTo();
				if(toLoc.getX()>=loc.getX()+radius) { 
					toLoc.setX(loc.getX()+radius-0.5);					
				} else if(toLoc.getX()<=loc.getX()-radius) { 
					toLoc.setX(loc.getX()-radius+0.5);					
				} 
				if(toLoc.getZ()>=loc.getZ()+radius) { 
					toLoc.setZ(loc.getZ()+radius-0.5);					
				} else if(toLoc.getZ()<=loc.getZ()-radius) { 
					toLoc.setZ(loc.getZ()-radius+0.5);					
				}
				e.getPlayer().teleport(toLoc);
				System.out.println(plName+" teleported");
			}*/			
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {		
		giveItem(e.getPlayer(),Material.GOLD_AXE);
		giveItem(e.getPlayer(),Material.GOLD_HOE);
		giveItem(e.getPlayer(),Material.GOLD_SWORD);
	}
	
	private void giveItem(Player pl, Material mat) {
		Inventory inv=pl.getInventory();
		if(!inv.contains(mat)) {
			inv.addItem(new ItemStack(mat));
			pl.sendMessage(ChatColor.AQUA+"You have been given a "+ChatColor.BOLD+ChatColor.GOLD+mat.name());
		}
	}
	
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(e.getPlayer().getItemInHand().getType()==Material.GOLD_AXE) {
			if(e.getAction()==Action.LEFT_CLICK_BLOCK) {
				Location loc1=e.getClickedBlock().getLocation().clone();
				customLoc1.put(e.getPlayer().getName().toUpperCase(),loc1);
				e.getPlayer().sendMessage("Point 1 set to "+ChatColor.RED+loc1.getBlockX()+","+loc1.getBlockY()+","+loc1.getBlockZ());
			} else if(e.getAction()==Action.RIGHT_CLICK_BLOCK) {
				Location loc2=e.getClickedBlock().getLocation().clone();
				customLoc2.put(e.getPlayer().getName().toUpperCase(),loc2);
				e.getPlayer().sendMessage("Point 2 set to "+ChatColor.RED+loc2.getBlockX()+","+loc2.getBlockY()+","+loc2.getBlockZ());
			}
		} else if (e.getPlayer().getItemInHand().getType()==Material.GOLD_HOE) {
			// ochrona bloku
			if(e.getAction()==Action.RIGHT_CLICK_BLOCK) {
				Block b=e.getClickedBlock();
				if(!b.hasMetadata("TP_PROTECT_FLAG")) {
					b.setMetadata("TP_PROTECT_FLAG", new FixedMetadataValue(this,"CUSTOM"));
				} 
				System.out.println(e.getClickedBlock().toString());
				
			}
		} else if (e.getPlayer().getItemInHand().getType()==Material.GOLD_SWORD) {
			if(e.getAction()==Action.RIGHT_CLICK_BLOCK) {
				//Block b=e.getClickedBlock();
				//BlockState bs=b.getState();
				
				/*ArrayList<String> tmpList=new ArrayList<String>();
				tmpList.add(Integer.toString(bs.getX()));
				tmpList.add(Integer.toString(bs.getY()));
				tmpList.add(Integer.toString(bs.getZ()));
				
				blockBackup.set("test1", tmpList);
				blockBackup.set("test2", b);
				blockBackup.set("test3", bs);
				blockBackup.set("test4", b.getLocation());
				blockBackup.set("test5", b.getType());
				blockBackup.set("test6", b.hasMetadata("TP_PROTECT_FLAG"));*/
				
				/*bb.addBlock(b);
				bb.save();*/
				
				//bb2.addBlock(b);
				//bb2.save();
				
				
				
			}
		} else if (e.getPlayer().getItemInHand().getType()==Material.GOLD_PICKAXE) {
			if(e.getAction()==Action.RIGHT_CLICK_BLOCK) {
				
				//
				Block b=e.getClickedBlock();
				
				System.out.println(b.getLocation());
				
				Block bUp=b.getRelative(BlockFace.UP);
				
				System.out.println(bUp.getLocation());
				
				for(Item item:e.getPlayer().getWorld().getEntitiesByClass(Item.class)) {
					System.out.println(item.getType()+"|"+item.getLocation());
			        if(item.getLocation().getBlock().equals(bUp)) {
			            item.remove();
			        }
				}				
			}
		} else if((e.getAction()==Action.LEFT_CLICK_BLOCK || e.getAction()==Action.RIGHT_CLICK_BLOCK) 
					&& e.getClickedBlock().getType()==Material.WALL_SIGN) {
			System.out.println(e.getPlayer().getItemInHand());
			Sign sign=(Sign)e.getClickedBlock().getState();
			String[] lines=sign.getLines();			
			
			if(lines.length<1) {
				// nic siê nie dzieje, pusty znak
				return;
			}
			
			String locName=lines[0].replace(" ", "_").toLowerCase();
						
			if(game==null) {
				return;
			}
			System.out.println(locName+" "+game.name);
			PlayerInGame plg=game.playerMap.get(e.getPlayer().getName());
			System.out.println(plg);
			if(plg==null) {
				// gracz nie jest w grze
				return;
			}
			if(plg.state==Game.State.LOBBY && locName.equalsIgnoreCase(game.waitingRoomTitle)) {
				if(game.isPlaying()) {
					e.getPlayer().sendMessage(ChatColor.RED+"Wait until the game is over");
					return;
				}
				game.setPlayerState(e.getPlayer(), Game.State.WAITING);
				return;
			}
			if(plg.state==Game.State.WAITING && (locName.equalsIgnoreCase(game.teamRedTitle) || locName.equalsIgnoreCase(game.teamBlueTitle))) {					
				if(game.isPlaying()) {
					e.getPlayer().sendMessage(ChatColor.RED+"Wait until the game is over");
					return;
				}
				if(locName.equalsIgnoreCase(game.teamRedTitle)) {
					game.joinRedTeam(e.getPlayer());
					return;
				}
				if(locName.equalsIgnoreCase(game.teamBlueTitle)) {
					game.joinBlueTeam(e.getPlayer());
					return;
				}
			}
			//return;
						
			// tymczasowo
			/*if(!gameLocations.containsKey(locName) && locName.equalsIgnoreCase("arena_1")) { 
				//gameLocations.put("arena_1",new Location(e.getPlayer().getWorld(),82.5,90,1022.5));
				//gameLocations.put("team_red",new Location(e.getPlayer().getWorld(),75.5,69,1031.5));
				//gameLocations.put("team_blue",new Location(e.getPlayer().getWorld(),90.5,69,1070.5));
	    		// lokalizacja spawnów - red 75 69 1031
	    		// lokalizacja spawnów - blue 90 69 1070

			}*/
			//TODO			
			// teleport do wybranej lokalizacji (sekcja destinations w pliku lobby1.cfg)
			/*if(gameLocations.containsKey(locName)) {
				//TODO dodaæ opóŸnienie
				e.getPlayer().teleport(gameLocations.get(locName));
				System.out.println("teleported to: "+locName );				
			}*/
		}

	}

	@EventHandler
	public void onBlockDamage(BlockDamageEvent e) {
		if(isProtectedBlock(e.getPlayer(), e.getBlock())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(isProtectedBlock(e.getPlayer(), e.getBlock())) {
			e.setCancelled(true);
			return;			
		}
		if(e.getPlayer().getItemInHand().getType()==Material.GOLD_HOE) {
			e.getBlock().removeMetadata("TP_PROTECT_FLAG", this);
		}
	}
	
	private boolean isProtectedBlock(Player pl, Block bl) {		
		// gracz klikn¹³ na blok, ¿eby ustaliæ jego pozycjê (PlayerInteractEvent), nie nale¿y go niszczyæ			
		if(pl.getItemInHand().getType()==Material.GOLD_AXE) {
			return true;
		}
		// chronione bloki nie mog¹ byæ zniszczone ,chyba, ¿e za pomoc¹ z³otej motyki
		if(pl.getItemInHand().getType()!=Material.GOLD_HOE && bl.hasMetadata("TP_PROTECT_FLAG")) {
			// tylko testowo
			for(MetadataValue mv : bl.getMetadata("TP_PROTECT_FLAG")) {
				System.out.println(mv.asString());
			}
			return true;
		}

		return false;
	}
	
	private void lockPlayer(String plName, Location loc1, Location loc2, boolean lockY) {
		playerBound.put(plName.toUpperCase(), new PlayerBoundaries(loc1,loc2,lockY));
		System.out.println(playerBound);
	}
	
	private void unlockPlayer(String plName) {
		playerBound.remove(plName.toUpperCase());		
		System.out.println(playerBound);
	}

		
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	System.out.println("oncommand");
    	
    	if(cmd.getName().equalsIgnoreCase("tpLock1")) {
    		System.out.println(cmd+" ok");
    		return true;
    	}    	

    	if(cmd.getName().equalsIgnoreCase("tpLock2")) {
    		System.out.println(cmd+" ok");
    		return true;
    	}   
    	
    	if(cmd.getName().equalsIgnoreCase("tpUnlock")) {
    		
        	String plName=null;

    		if(args.length>1) {
    			System.out.println("b³êdna liczba parametrów");
    			return false;    			    		
    		}
    		
    		if(args.length==1) {
    			plName=args[0];
    			if(getPlayerByName(plName)==null) {
        			sender.sendMessage("Unrecognized player of player offline");
    				return false;
    			}
    		} else {
    			if(!(sender instanceof Player)) {
    				sender.sendMessage("You should be a player to run this command w/o [player_name]");
    				return false;
    			}
    			plName=((Player)sender).getName();
    		} 

    		
    		unlockPlayer(plName);
    		
    		return true;
    	}    	

    	    		
    	if(cmd.getName().equalsIgnoreCase("tpLock")) {
    		boolean lockY=false;
    		
        	String plName=null;
        	    		    		
    		if(args.length>2) {
    			System.out.println("b³êdna liczba parametrów");
    			return false;    			    		
    		}
    		
    		int cntPlayerName=0;
    		int cntLockY=0;
    		
    		for(String str : args) {
    			if(str.equalsIgnoreCase("-Y")) {
    				lockY=true;
    				cntLockY++;
    			} else {
    				plName=str;
    				cntPlayerName++;
    			}
    		}
    		
    		if(cntPlayerName>1 || cntLockY>1) {
    			System.out.println("b³êdne parametry");
    			return false;    			    		    			
    		}

    		if(plName==null) {
    			if(!(sender instanceof Player)) {
    				sender.sendMessage("You should be a player to run this command w/o [player_name]");
    				return false;
    			}
    			plName=((Player)sender).getName();
    		} else if(getPlayerByName(plName)==null) {
    			sender.sendMessage("Unrecognized player of player offline");
				return false;
    		}
    		
    		Location loc1=customLoc1.get(plName.toUpperCase());
    		Location loc2=customLoc2.get(plName.toUpperCase());
    		
    		if(loc1==null || loc2==null) {
				sender.sendMessage("You should set points 1 and 2 before locking a player");
				return false;    			
    		}
    		
    		lockPlayer(plName, loc1, loc2, lockY);
    		    		
    		return true;			
    	}
    	
    	if(cmd.getName().equalsIgnoreCase("tpTest1")) {
    		ArrayList<Integer> cancelList=new ArrayList<Integer>(); 
    		for(Integer lt : lobbyTimers) {
    			System.out.println(lt+" cancel");
    			getServer().getScheduler().cancelTask(lt);
    			cancelList.add(lt);    			
    		}
    		for(Integer ltcancel : cancelList) {
    			lobbyTimers.remove(ltcancel);
    		}
    		return true;    		
    	}    	

    	if(cmd.getName().equalsIgnoreCase("tpTest2")) {
    		addLobbyTimer();
    		return true;    		
    	}    	
    	    	
    
    	if(cmd.getName().equalsIgnoreCase("tpTest3")) {
    		for(Integer i : lobbyTimers) {
    			System.out.println(i);
    		
    			//lobbyTimers.remove(lt);			
			}
    		return true;
		}
    	
    	if(cmd.getName().equalsIgnoreCase("tpSave")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage("You should be a player to run this command");
				return false;
			}
			
			if(args.length!=1 && args.length!=7) {
    			System.out.println("b³êdna liczba parametrów");
    			return false;    			    		
    		}
						
			String plName=((Player)sender).getName();			

			Location loc1=null;
			Location loc2=null;
			
			if(args.length==1) {
				loc1=customLoc1.get(plName.toUpperCase());
				loc2=customLoc2.get(plName.toUpperCase());
			} else {
				World world=((Player)sender).getWorld();
				loc1=new Location(world,Integer.valueOf(args[1]),Integer.valueOf(args[2]),Integer.valueOf(args[3]));
				loc2=new Location(world,Integer.valueOf(args[4]),Integer.valueOf(args[5]),Integer.valueOf(args[6]));
			}
    		
    		if(loc1==null || loc2==null) {
				sender.sendMessage("You should set points 1 and 2 before saving an area");
				return false;    			
    		}
    		
    		AreaBackup blBackup=new AreaBackup(args[0],getDataFolder());
    		
    		blBackup.addArea(loc1, loc2);
    		int blockCnt=blBackup.save();
    		
    		if(blockCnt==-1) {
    			sender.sendMessage(ChatColor.RED+"Error while saving area");
    			return true;
    		}
    		
    		//int itemCnt=AreaBackup.clearItemsInArea(((Player)sender).getWorld(), loc1, loc2);
    		
    		sender.sendMessage(ChatColor.AQUA+"Area "+ChatColor.RED+args[0]+ChatColor.AQUA+" saved: "+
    						ChatColor.RED+Integer.toString(blockCnt)+ChatColor.AQUA+" blocks, "/*+
    						ChatColor.RED+Integer.toString(itemCnt)+ChatColor.AQUA+" items removed."*/);
    		    		
    		//bb.addArea(loc1, loc2);
    		//bb.save();
    		return true;    		
    	}
    	
    	if(cmd.getName().equalsIgnoreCase("tpLoad")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage("You should be a player to run this command");
				return false;
			}
			
			if(args.length!=1) {
    			System.out.println("b³êdna liczba parametrów");
    			return false;    			    		
    		}
			
			
			AreaLoadTimer loadTimer=new AreaLoadTimer(args[0], this, (Player)sender, ((Player)sender).getWorld());
			loadTimer.runTaskLater(this, 10L); // pó³ sekundy
    		return true;    		
    	}
    	
    	if(cmd.getName().equalsIgnoreCase("tpLobby")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage("You should be a player to run this command");
				return false;
			}
			
			if(args.length!=0) {
    			System.out.println("b³êdna liczba parametrów");
    			return false;    			    		
    		}
						    		
    		if(!gameLocations.containsKey("lobby")) {
    			gameLocations.put("lobby", new Location(((Player)sender).getWorld(),102,182,963));
    		}
    		
    		if(((Player)sender).teleport(gameLocations.get("lobby"))) {
    			sender.sendMessage(ChatColor.AQUA+"Teleported to "+ChatColor.RED+"main lobby"+ChatColor.AQUA+".");
    		}
    		
    		
    		return true;    		
    	}
    	
    	if(cmd.getName().equalsIgnoreCase("tpStartGame")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage("You should be a player to run this command");
				return false;
			}
			if(args.length!=0) {
    			System.out.println("b³êdna liczba parametrów");
    			return false;    			    		
    		}
			
			if(startGame(((Player)sender).getWorld())) {
				sender.sendMessage(ChatColor.AQUA+"The game started");
			} else {
				if(game!=null) {
					sender.sendMessage(ChatColor.RED+"The game is already started");
				} else {
					sender.sendMessage(ChatColor.RED+"Cannot start a game");					
				}				
			}
			
			return true;
    	}

    	if(cmd.getName().equalsIgnoreCase("tpStopGame")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage("You should be a player to run this command");
				return false;
			}
			if(args.length!=0) {
    			System.out.println("b³êdna liczba parametrów");
    			return false;    			    		
    		}
			
			if(stopGame()) {
				sender.sendMessage(ChatColor.AQUA+"Game stopped");
			} else {
				if(game==null) {
					sender.sendMessage(ChatColor.RED+"Game not started");
				} else {
					sender.sendMessage(ChatColor.RED+"Cannot stop the game");					
				}				
				
			}
			
			return true;
    	}

    	if(cmd.getName().equalsIgnoreCase("tpJoinGame")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage("You should be a player to run this command");
				return false;
			}
			if(args.length!=0) {
    			System.out.println("b³êdna liczba parametrów");
    			return false;    			    		
    		}
			
			Player pl=(Player)sender;
			
			if(joinGame(pl)) {
				sender.sendMessage(ChatColor.WHITE+pl.getName()+ChatColor.AQUA+" joined the game");
			} else {
				if(game==null) {
					sender.sendMessage(ChatColor.RED+"Game not started");
				} else {
					sender.sendMessage(ChatColor.RED+"Cannot join the game");					
				}				
				
			}
			
			return true;
    	}
    	
    	
    	if(cmd.getName().equalsIgnoreCase("tpTeleport")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage("You should be a player to run this command");
				return false;
			}
			
			if(args.length!=3) {
    			System.out.println("b³êdna liczba parametrów");
    			return false;    			    		
    		}
						    		
    		Player pl=(Player)sender;
			
    		pl.teleport(new Location(pl.getWorld(),Integer.valueOf(args[0]),Integer.valueOf(args[1]),Integer.valueOf(args[2])));
    		return true;    		
    	}
    	
    	if(cmd.getName().equalsIgnoreCase("tpTest5")) {
    		Player pl=(Player)sender;
    		System.out.println("tpTest5");
    		
    		scoreManager=Bukkit.getScoreboardManager();
    		scoreboard=scoreManager.getNewScoreboard();
    		
    		pl.setScoreboard(scoreboard);
    		
        	Objective objective = scoreboard.registerNewObjective("test", "dummy");
    		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    		objective.setDisplayName(ChatColor.GRAY+"test objective");
    		Score score=objective.getScore("1"+ChatColor.RED+"test score");
    		score.setScore(10);
    		Score score2=objective.getScore("2"+ChatColor.DARK_PURPLE+"test score2");
    		score2.setScore(9);
    		Score score3=objective.getScore("3"+ChatColor.GREEN+"test score3");
    		score3.setScore(8);    		
    		Score score4=objective.getScore("4"+ChatColor.DARK_PURPLE+"2test score");
    		score4.setScore(7);
    		Score score5=objective.getScore("5"+ChatColor.BLUE+"2test score2");
    		score5.setScore(6);
    		Score score6=objective.getScore("6"+ChatColor.GREEN+"2test score3");
    		score6.setScore(11);    		
    		
    		
    		
    		/*Objective objective2 = scoreboard.registerNewObjective("test2", "dummy");
    		objective2.setDisplaySlot(DisplaySlot.SIDEBAR);
    		objective2.setDisplayName("test objective 2");
    		Score score2=objective2.getScore(ChatColor.BLUE+"test score 2");
    		score2.setScore(15);*/
    		 
    		
    		 
    		/*for(Player online : Bukkit.getOnlinePlayers()){
    			//teamRed.addPlayer(online);
    			online.setScoreboard(scoreboard);
    			online.setHealth(online.getHealth()); //Update health
    		}*/
    		return true;    		
    	}    	
    	
    	if(cmd.getName().equalsIgnoreCase("tpTest6")) {
    		Player pl=(Player)sender;
    		System.out.println("tpTest5");
    		
    		scoreboard.resetScores("6"+ChatColor.GREEN+"2test score3");
    	}
    	

    	
    	return false;
    	
	}    	

	private Player getPlayerByName(String plName) {
    	for(Player pl : this.getServer().getOnlinePlayers()) {
    		if(pl.getName().equalsIgnoreCase(plName)) {
    			return pl;
    		}
    	}
    	
    	return null;
    }
    
    private LobbyTimer addLobbyTimer() {
		LobbyTimer lTimer=new LobbyTimer();
		lTimer.runTaskTimer(this, 20L, 20L);
		lobbyTimers.add(lTimer.getTaskId());
		//
		System.out.println(lTimer.getTaskId()+" started");
		System.out.println(lobbyTimers.toArray().toString());
		
		
		return lTimer;
    }
    
    private boolean startGame(World world) {
		if(game!=null) {
			return false;
		}
		
		// tymczasowo statyczna nazwa
		game=new Game(this, "arena_1");
		// tymczasowo
		game.gameState=Game.State.LOBBY;
		
		new AreaLoadTimer("lobby1", this, null, world).runTaskLater(this, 10L); // pó³ sekundy
		new AreaLoadTimer("waitingRoom1", this, null, world).runTaskLater(this, 10L);
		new AreaLoadTimer("arena_1", this, null, world).runTaskLater(this, 10L);
		
		game.arenaCube=new Cube(new Location(null,68,32,1026),new Location(null,96,108,1077));
		
		game.scoreboard=Bukkit.getScoreboardManager().getNewScoreboard();
		game.initScoreboard();
		game.lobbyPoint=new Location(world,102.5,182,963.5);
		game.waitingRoomPoint=new Location(world,82.5,90,1022.5);
		game.teamRedPoint=new Location(world,75.5,69,1031.5);
		game.teamBluePoint=new Location(world,90.5,69,1070.5);
		
		game.waitingRoomTitle="arena_1";
		game.teamRedTitle="team_red";
		game.teamBlueTitle="team_blue";

				
		return true;
    }
    
    private boolean stopGame() {
		if(game==null) {
			return false;
		}
		
		game.clearScoreboard();
		game=null;
		//TODO
		// Usun¹æ graczy z obszaru gry
				
		return true;
    }
    
    private boolean joinGame(Player pl) {
    	if(game==null) {
    		return false;
    	}
    	
    	game.playerMap.put(pl.getName(),new PlayerInGame(pl));
    	game.setPlayerState(pl,Game.State.LOBBY);
    	
    	
    	return true;
    }
    
    
}

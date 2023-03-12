package com.krzyskrzys.utils;

import org.bukkit.Location;

public class Cube {
	private Location location1;
	private Location location2;
	private int sizeX;
	private int sizeY;
	private int sizeZ;
	
	public Cube() {
		this(new Location(null,0,0,0), new Location(null,0,0,0));
	}
	
	public Cube(Location nLoc1, Location nLoc2) {
		setLocations(nLoc1, nLoc2);
	}
	
	public void setLocations(Location nLoc1, Location nLoc2) {
		int x1=Math.min(nLoc1.getBlockX(), nLoc2.getBlockX());
		int y1=Math.min(nLoc1.getBlockY(), nLoc2.getBlockY());
		int z1=Math.min(nLoc1.getBlockZ(), nLoc2.getBlockZ());
		int x2=Math.max(nLoc1.getBlockX(), nLoc2.getBlockX());
		int y2=Math.max(nLoc1.getBlockY(), nLoc2.getBlockY());
		int z2=Math.max(nLoc1.getBlockZ(), nLoc2.getBlockZ());

		location1=new Location(nLoc1.getWorld(),x1,y1,z1);
		location2=new Location(nLoc2.getWorld(),x2,y2,z2);
		
		sizeX=x2-x1+1;
		sizeY=x2-x1+1;
		sizeZ=x2-x1+1;
	}
	
	public Location getLocation1() {
		return location1;		
	}

	public Location getLocation2() {
		return location2;		
	}
	
	public int getSizeX() {
		return sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public int getSizeZ() {
		return sizeZ;
	}
}

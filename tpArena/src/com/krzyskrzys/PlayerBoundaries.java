package com.krzyskrzys;

import org.bukkit.Location;

public class PlayerBoundaries {
	private Location location;
	private Location location1;
	private Location location2;
	private double sizeH;
	private double sizeV;
	
	private final static double MIN_SNAP=0.5d;
	private final static double MAX_Y=1000.0d;

	// tymczasowe
	public PlayerBoundaries(Location nLoc, double nSize) {
		this(nLoc,nSize,nSize);
	}

	// tymczasowe
	public PlayerBoundaries(Location nLoc, double nSizeH, double nSizeV) {
		location=nLoc;
		sizeH=nSizeH;
		sizeV=nSizeH;
		location1=new Location(location.getWorld(),location.getX()-sizeH,location.getY()-sizeV,location.getZ()-sizeH);
		location2=new Location(location.getWorld(),location.getX()+sizeH,location.getY()+sizeV,location.getZ()+sizeH);
	}
	
	public PlayerBoundaries(Location nLoc1, Location nLoc2) {
		setLocation(nLoc1,nLoc2);
		sizeH=-1;
		sizeV=-1;		
	}
	
	public PlayerBoundaries(Location nLoc1, Location nLoc2, boolean lockY) {
		Location loc2=nLoc2.clone();
		if(!lockY) {
			loc2.setY(MAX_Y);
		}
		setLocation(nLoc1,loc2);
		sizeH=-1;
		sizeV=-1;		
	}

	
	public Location getLocation() {
		return location;
	}
	
	public Location getLocation1() {
		return location1;
	}
	
	public Location getLocation2() {
		return location2;
	}

	public double getSizeH() {
		return sizeH;
	}
	
	public double getSizeV() {
		return sizeV;
	}

	
	public void setLocation(Location nLoc1, Location nLoc2) {
		double x1=Math.min(nLoc1.getBlockX()+0.5, nLoc2.getBlockX()+0.5);
		double y1=Math.min(nLoc1.getBlockY()+0.5, nLoc2.getBlockY()+0.5);
		double z1=Math.min(nLoc1.getBlockZ()+0.5, nLoc2.getBlockZ()+0.5);
		double x2=Math.max(nLoc1.getBlockX()+0.5, nLoc2.getBlockX()+0.5);
		double y2=Math.max(nLoc1.getBlockY()+0.5, nLoc2.getBlockY()+0.5);
		double z2=Math.max(nLoc1.getBlockZ()+0.5, nLoc2.getBlockZ()+0.5);
		location1=new Location(nLoc1.getWorld(),x1,y1,z1);
		location2=new Location(nLoc2.getWorld(),x2,y2,z2);
		// œrodek
		location=new Location(nLoc1.getWorld(),(x1+x2)/2,(y1+y2)/2,(z1+z2)/2);
	}	

	public void setSize(double nSize) {
		sizeH=nSize;
	}
	
	public void setSize(double nSizeH, double nSizeV) {
		sizeH=nSizeH;
		sizeV=nSizeV;
	}

	
	public String toString() {
		return "("+location1.getX()+","+location2.getY()+","+location1.getZ()+")("+
				location2.getX()+","+location2.getY()+","+location2.getZ()+")";
	}
	
	public boolean isInside(Location checkLoc) {
		if(Math.abs(checkLoc.getX()-location.getX())>=sizeH || Math.abs(checkLoc.getZ()-location.getZ())>=sizeH) {
			return false;					
		} 
		
		if(Math.abs(checkLoc.getY()-location.getY())>=sizeV) {
			return false;					
		}
		
		return true;
	}			
		
	// czy punkt le¿y wewn¹trz obszaru
	public boolean covers(Location loc) {
		return loc.getX()>=location1.getX() && loc.getX()<=location2.getX() &&
				loc.getY()>=location1.getY() && loc.getY()<=location2.getY() &&
				loc.getZ()>=location1.getZ() && loc.getZ()<=location2.getZ();
	}
	
	// najbli¿szy punkt wewn¹trz obszaru (snap pozwala na dodatkowe przesuniêcie 0.5 bloku)
	public Location toNearestLocation(Location loc, boolean snap) {
		if(loc.getX()<location1.getX()) {
			loc.setX(location1.getX()+(snap ? MIN_SNAP : 0));
		} else if(loc.getX()>location2.getX()) {
			loc.setX(location2.getX()-(snap ? MIN_SNAP : 0));
		}
		
		if(loc.getY()<location1.getY()) {
			loc.setY(location1.getY()+(snap ? MIN_SNAP : 0));
		} else if(loc.getY()>location2.getY()) {
			loc.setY(location2.getY()-(snap ? MIN_SNAP : 0));
		}
		
		if(loc.getZ()<location1.getZ()) {
			loc.setZ(location1.getZ()+(snap ? MIN_SNAP : 0));
		} else if(loc.getZ()>location2.getZ()) {
			loc.setZ(location2.getZ()-(snap ? MIN_SNAP : 0));
		}		
		
		return loc;
	}
	
		
		
		
		/*if(Math.abs(checkLoc.getX()-location.getX())>=size || Math.abs(checkLoc.getZ()-location.getZ())>=size) {
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


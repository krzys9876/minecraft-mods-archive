package com.krzyskrzys.logo;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class LogoOutputMC extends LogoOutput {
	protected Material mat;
	protected World world;
	
	public LogoOutputMC() {
		super();
		mat=Material.STONE; // tymczasowo, potem nale�y umo�liwi� wyb�r
		world=null; // czeka na inicjalizacj�
	}
	
	public void setWorld(World nWorld) {
		world=nWorld;
	}
	
	public String toString() {
		return super.toString()+"mat:"+mat.toString();
	}
	
	public void drawLine(double len) {
		// przechowanie starej pozycji
		double oX=x;
		double oY=y;
		double oZ=z;
		
		System.out.println(toString());
		
		movePos(len);
		
		System.out.println(toString());
		
		// linia mi�dzy punktami zaokr�glonymi do pe�nych blok�w
		drawLine((int)oX,(int)oY,(int)oZ,(int)x,(int)y,(int)z);
	}
	
    private void drawLine(int locStartX, int locStartY, int locStartZ, int locEndX, int locEndY, int locEndZ) {
    	
    	System.out.println(locStartX+";"+locStartY+";"+locStartZ+";"+locEndX+";"+locEndY+";"+locEndZ);
    	
     	int dx=locEndX-locStartX;
		int dy=locEndY-locStartY;
		int dz=locEndZ-locStartZ;			
		
		double incX;
		double incY;
		double incZ;
		
		int distBlock;
		
		if(dx==0 && dy==0 && dz==0) {
			distBlock=0;
			dx=1;
			dy=1;
			dz=1;
			incX=1;
			incY=1;
			incZ=1;
		} else {
			if(Math.abs(dx)>=Math.abs(dy) && Math.abs(dx)>=Math.abs(dz)) {
				// linia po osi X
				distBlock=Math.abs(dx);
			} else if(Math.abs(dy)>=Math.abs(dz) && Math.abs(dy)>=Math.abs(dz)) {
				// linia po osi Y
				distBlock=Math.abs(dy);
			} else if(Math.abs(dz)>=Math.abs(dx) && Math.abs(dz)>=Math.abs(dy)) {
				// linia po osi Z
				distBlock=Math.abs(dz);
			} else {
				System.out.println("b��d - nie da si� wyznaczy� przebiegu linii");
				return;
			}
			incX=(double)dx/(double)distBlock;
			incY=(double)dy/(double)distBlock;
			incZ=(double)dz/(double)distBlock;
		}
		
		//System.out.println(locStart.toString()+" - "+locEnd.toString());
		System.out.println(dx+" "+dy+" "+dz);
		//System.out.println(distBlock);
		//System.out.println(incX+" "+incY+" "+incZ);
		
		
		// Pocz�tek w �rodku bloku
		double lX=(double)(locStartX)+0.5;
		double lY=(double)(locStartY)+0.5;
		double lZ=(double)(locStartZ)+0.5;
		
		Location tLoc=new Location(world,lX,lY,lZ);
		Location tprevLoc=new Location(world,lX,lY,lZ);
		Location tempLoc;
		Block bl,blt;
		Block blp=null;
					
		// p�tla wyznaczaj�ca punkty linii - punkt�w jest o 1 wi�cej, ni� wynika�oby z r�nicy, bo wliczamy pocz�tek i koniec
		for(int i=0;i<distBlock+1;i++) {
			tLoc.setX(lX);
			tLoc.setY(lY);
			tLoc.setZ(lZ);
			//System.out.println("n="+i+" "+lX+" "+lY+" "+lZ);
			bl=world.getBlockAt(tLoc);
			if(penDown) {
					bl.setType(mat);
			}
			// je�eli zmieniaj� si� warto�ci we wszystkich osiach, to trzeba wype�ni� dziur�
			// kolejne bloki nie powinny styka� si� tylko wierzcho�kiem
			// dziury wype�niamy tylko na p�aszczy�nie, czyli osiach X-Z, Y ignorujemy, bo to �le wygl�da
			if(blp!=null && Math.abs(bl.getX()-blp.getX())>0 &&
			   Math.abs(bl.getY()-blp.getY())>0 &&
			   Math.abs(bl.getZ()-blp.getZ())>0) {
				tempLoc=new Location(world,(double)blp.getX(),(double)blp.getY(),(double)blp.getZ());
				if(incX<incZ) {
					tempLoc=new Location(world,(double)bl.getX(),(double)blp.getY(),(double)blp.getZ());
				} else {
					tempLoc=new Location(world,(double)blp.getX(),(double)blp.getY(),(double)bl.getZ());
				} 
				//System.out.println(tempLoc);
				blt=world.getBlockAt(tempLoc);					
				if(penDown) {
					blt.setType(mat);
				}
			}
			tprevLoc=tLoc.clone();
			blp=world.getBlockAt(tprevLoc);								
			tLoc.setX(lX);
			tLoc.setY(lY);
			tLoc.setZ(lZ);
			lX=lX+incX;
			lY=lY+incY;
			lZ=lZ+incZ;
		}
    	
    }
    
	public void setPen(String nPen) {
		super.setPen(nPen);
		System.out.println(pen);
		Material mTemp=Material.getMaterial(pen);		
		System.out.println(mTemp);
		if(mTemp==null) {
			mat=Material.STONE;
			return;
		}
			
		mat=mTemp;
	}


}

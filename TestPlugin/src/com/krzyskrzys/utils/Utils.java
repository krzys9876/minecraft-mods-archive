package com.krzyskrzys.utils;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.Location;

public class Utils {

	public static void main(String[] args) {
		   /*Random randomno = new Random();		   
		   System.out.println("Next int value: " + randomno.nextInt(5));
		   int i=3;
		   float j=i*2.0F;
		   System.out.println("int value: " + i);
		   System.out.println("float value: " + j);*/
		   
		  /* ArrayList<ArrayList<String>> arr=new ArrayList<ArrayList<String>>();
		   ArrayList<String> row;
		   
		   row=new ArrayList<String>();
		   row.add("A");
		   row.add("B");
		   row.add("C");
		   arr.add(row);
		   row=new ArrayList<String>(); // nowy obiekt
		   row.add("X");
		   row.add("Y");
		   row.add("Z");
		   arr.add(row);
		   
		   ArrayList<String> row2;
		   for(int i=0;i<arr.size();i=i+1) {
			   row2=arr.get(i);
			   for(int j=0;j<row2.size();j=j+1) {
				   System.out.println("i="+i+" j="+j+" value: "+row2.get(j));
			   }
		   }
		   
		   Map<String,Material> map=new HashMap<String,Material>();
		   
		   map.put("A", Material.STONE);
		   map.put("B", Material.DIRT);
		   map.put(".", Material.AIR);
		   
		   System.out.println(map.toString());
		   
		   System.out.println(map.toString().substring(0, 1));
		   */
		
		
		/*byte[] fileArr=new byte[0];
		FileInputStream in=null;
				
		try {
			File file=new File("c:\\data\\b42.bmp");
			int size=(int)file.length();
			fileArr=new byte[size];			
			in=new FileInputStream(file);
			DataInputStream din=new DataInputStream(in);
			// plik jest ma³y, wiêc mo¿emy czytaæ go w ca³oœci
			din.readFully(fileArr);
		
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}					
            }
		}*/
			
		
		BitmapFont bFont=new BitmapFont(new File("c:\\data\\8x8_1.bmp"),new File("c:\\data\\8x8_1.fnt"));
		byte[] fileArr=bFont.getArr();
				
		System.out.println(fileArr.length);
		System.out.println("file size: "+bFont.bmpFileSize);
		System.out.println("offset: "+bFont.bmpOffset);
		System.out.println("width: "+bFont.bmpWidth);
		System.out.println("height: "+bFont.bmpHeight);
		System.out.println("font width: "+bFont.fontW);
		System.out.println("font height: "+bFont.fontH);
		System.out.println("string: "+bFont.fontMap);
		
		int ind=0;
		for(int h=(int)bFont.bmpHeight-1;h>=0;h--) {
			String row="";
			for(int w=0;w<(int)bFont.bmpWidth;w++) {
				//System.out.println(h+" "+w+" "+w/8+" "+w%8);
				ind=(int)(h*bFont.bmpWidth+w);
				int bit=getBit(fileArr[(int)bFont.bmpOffset+ind/8],7-(ind)%8);
				if(bit==1) {
					row=row+" ";
				} else {
					row=row+"X";
				} 
			}
			System.out.println(row);
		}
		
		String str="ABCD_E";

		boolean[][] strArr=bFont.getStringArr(str);
		
		System.out.println(strArr.length);
		
		for(int h=0;h<bFont.fontH;h++) {
			String row="";
			for(int w=0;w<bFont.fontW*str.length();w++) {
				row=row+(strArr[w][h] ? "X":" ");
			}
			System.out.println(row);
		}

		
	}

	
	/*    private ItemStack enchantItem(ItemStack item, Enchantment ench) {
	if(ench.canEnchantItem(item)) {
		item.addEnchantment(ench, ench.getMaxLevel());
	}
	return item;
}
*/
	
	public static long getLong(byte[] arr, int start) {
		return (arr[start] & 0xff) | 
				((arr[start+1] & 0xff) << 8) | 
				((arr[start+2] & 0xff) << 16) |
				((arr[start+3] & 0xff) << 24);
	}

	public static int getBit(byte b, int bit) {
		switch(bit) {
		case 0: return b & 1; 
		case 1: return (b & 2) >> 1; 
		case 2: return (b & 4) >> 2; 
		case 3: return (b & 8) >> 3; 
		case 4: return (b & 16) >> 4; 
		case 5: return (b & 32) >> 5; 
		case 6: return (b & 64) >> 6; 
		case 7: return (b & 128) >> 7; 		
		}
		return 0;
	}
	

	public static String getBinary(byte b) {
		String bin=Integer.toBinaryString(b);
		for(int k=bin.length();k<8;k++) {
			bin="0"+bin;
		}
		return bin;
	}


	public static boolean enchantItemMulti(ItemStack item, Enchantment[] enchArr) {    	
		Enchantment ench;
		for(int i=0; i<enchArr.length; i++) {
			ench=enchArr[i];
			if(ench.canEnchantItem(item)) {
				try {
					item.addEnchantment(ench, ench.getMaxLevel());
				} catch(Exception e) {
					System.out.println("niedozwolone (exception): "+ench.toString()+"|"+item.toString());
				}    			
			} else {
				//System.out.println("niedozwolone: "+ench.toString()+"|"+item.toString());    			
			}
		}
	
		return true;
	}
	
	public static ItemStack enchantItemMultiF(ItemStack item, Enchantment[] enchArr) {
		enchantItemMulti(item, enchArr);
		return item;
	}
	
	public static boolean createEnchantedItem(PlayerInventory plInv, Material mat, Enchantment[] enchArr, int slot) {    	
	
		ItemStack item=new ItemStack(mat);
	    enchantItemMulti(item,enchArr);
	    plInv.clear(slot);
	    plInv.setItem(slot, item);
	    
		return true;
	}
	
	public static boolean createItem(PlayerInventory plInv, Material mat, int quantity, int slot) {    	
	
		ItemStack item=new ItemStack(mat,quantity);
	    plInv.clear(slot);
	    plInv.setItem(slot, item);
	    
		return true;
	}
	
	public static double normalizeYaw(double yaw) {
		// normalizacja Yaw do zakresu -180..180
		while(yaw<-180) {
			yaw=yaw+360;
		}
		while(yaw>180) {
			yaw=yaw-360;
		}
		return yaw;
	}	
	
	public static Location moveLoc(Location loc,double yaw, double pitch, int mRight,int mUp) {

		Location retLoc=new Location(loc.getWorld(),loc.getX(),loc.getY(),loc.getZ());
		int mX=0;
		int mY=0;
		int mZ=0;
		
		boolean flat=(pitch<-75 || pitch>75);
		if(!flat) {
			mUp=mUp+1;
		}
		
		/* 
			X+ -135..-45
			Z+ -45..45
			X- 45..135
			Z- 135..180/-180..-135
			
			0..-90 góra
			0..90 dó³
		*/
		
		if(yaw>=-45 && yaw<45) {
			// Z+ ok
			mX=-mRight;
			if(!flat) {
				mY=mUp;
			} else {
				mZ=mUp;
			}
			//System.out.println("Z+");
		} else if(yaw>=135 || yaw<-135) {
			// Z- ok
			mX=mRight;
			if(!flat) {
				mY=mUp;
			} else {
				mZ=-mUp;
			}
			//System.out.println("Z-");
		} else if(yaw>=-135 && yaw<-45) {
			// X+
			mZ=mRight;
			if(!flat) {
				mY=mUp;
			} else {
				mX=mUp;
			}
			//System.out.println("X+");
		} else if(yaw>=45 && yaw<135) {
			// X-
			mZ=-mRight;
			if(!flat) {
				mY=mUp;
			} else {
				mX=-mUp;
			}
			//System.out.println("X-");
		}		
		return retLoc.add(mX, mY, mZ);		
	}
		
}
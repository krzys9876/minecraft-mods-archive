package com.krzyskrzys.utils;

/*import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;*/

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class TpKitsUtils {

	public static void main(String[] args) {
		
		System.out.println("Hello world :)");
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
			
		
/*		BitmapFont bFont=new BitmapFont(new File("c:\\data\\8x8_1.bmp"),new File("c:\\data\\8x8_1.fnt"));
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
		}*/

		
	}

	
	/*    private ItemStack enchantItem(ItemStack item, Enchantment ench) {
	if(ench.canEnchantItem(item)) {
		item.addEnchantment(ench, ench.getMaxLevel());
	}
	return item;
}
*/

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
	
		
}
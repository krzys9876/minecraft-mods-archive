package com.krzyskrzys;

import net.minecraft.server.v1_8_R2.EntitySpider;
import net.minecraft.server.v1_8_R2.Item;
import net.minecraft.server.v1_8_R2.Items;
import net.minecraft.server.v1_8_R2.World;

public class CustomSpider extends EntitySpider {
	
	public CustomSpider(World world) {
		super(world);
		//this.setCustomName("CustomSpider"+Integer.toString(this.random.nextInt()));
		//this.setCustomNameVisible(true);
		//System.out.println(this.getCustomName());
	}
	
	protected Item getLoot() {
		//return Items.STRING;
		return super.getLoot();
	}
	  
	protected void dropDeathLoot(boolean flag, int i) {
		
		super.dropDeathLoot(flag, i);
	    
		// Dodatkowa nagroda
	    int reward=this.random.nextInt(2); // 0..1	    
	    System.out.println("Spider reward: "+reward);
	    
    	if(reward==0) {
			a(Items.IRON_INGOT, 1); // 50%
		    System.out.println("CustomSpider Iron Ingot");
	    } 
	}
	
	
	
	public String toString() {
		return "CustomSpider";
	}
	
}


package com.krzyskrzys;

import net.minecraft.server.v1_8_R2.EntityCaveSpider;
import net.minecraft.server.v1_8_R2.Item;
import net.minecraft.server.v1_8_R2.Items;
import net.minecraft.server.v1_8_R2.World;

public class CustomCaveSpider extends EntityCaveSpider {
	
	public CustomCaveSpider(World world) {
		super(world);
		//this.setCustomName("CustomCaveSpider"+Integer.toString(this.random.nextInt()));
		//this.setCustomNameVisible(true);
		//System.out.println(this.getCustomName());
	}
	
	protected Item getLoot() {
		return super.getLoot();
	}
	  
	protected void dropDeathLoot(boolean flag, int i) {
		
		super.dropDeathLoot(flag, i);
	    
		// Dodatkowa nagroda
	    int reward=this.random.nextInt(2); // 0..1	    
	    System.out.println("CaveSpider reward: "+reward);
	    
    	if(reward==0) {
			a(Items.DIAMOND, 1); // 50%
		    System.out.println("CustomCaveSpider Diamond");
	    } 
	}
	
	
	
	public String toString() {
		return "CustomCaveSpider";
	}
	
}


package com.krzyskrzys;

//import java.util.Random;

import net.minecraft.server.v1_8_R1.EntityCow;
import net.minecraft.server.v1_8_R1.Item;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.Items;


public class CustomCow extends EntityCow {
	
	public CustomCow(World world) {
		super(world);
		//this.setCustomName("CustomCow"+Integer.toString(new Random().nextInt()));
		//this.setCustomNameVisible(true);
		//this.getBukkitEntity().setCustomNameVisible(true);
		//System.out.println(this.getCustomName());
		
	}
	
	protected Item getLoot() {
		//return Items.LEATHER;
		return super.getLoot();
	}	
	
	protected void dropDeathLoot(boolean flag, int i) {
	    /*int j = this.random.nextInt(3) + this.random.nextInt(1 + i);
	    for (int k = 0; k < j; k++) {
	       a(Items.LEATHER, 1);
	    }
	    
	    j = this.random.nextInt(3) + 1 + this.random.nextInt(1 + i);
	    
	    for (int k = 0; k < j; k++) {
	    	if(isBurning()) {
	    		a(Items.COOKED_BEEF, 1);
	    	} else {
	    		a(Items.BEEF, 1);
	    	}  			    			    
	    }*/
		
		super.dropDeathLoot(flag, i);
	    
	    // Dodatkowa nagroda
	    int reward=this.random.nextInt(5); // 0..4	    
	    System.out.println("Reward: "+reward);
	    
    	if(reward==0) {
			a(Items.GOLDEN_APPLE, 1); // 20%
		    System.out.println("CustomCow Golden Apple");
	    } else if(reward==1) {
			a(Items.DIAMOND, 1); // 20%
		    System.out.println("CustomCow Diamond");
	    } 
	}	
	
	public String toString() {
		return "CustomCow";
	}
}

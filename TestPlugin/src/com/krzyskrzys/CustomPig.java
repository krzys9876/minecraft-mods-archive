package com.krzyskrzys;

//import java.util.Random;

import net.minecraft.server.v1_8_R1.EntityPig;
import net.minecraft.server.v1_8_R1.Item;
import net.minecraft.server.v1_8_R1.Items;
import net.minecraft.server.v1_8_R1.World;

public class CustomPig extends EntityPig {
	
	public CustomPig(World world) {
		super(world);
		//this.setCustomName("CustomPig"+Integer.toString(new Random().nextInt()));
		//this.setCustomNameVisible(true);
		//System.out.println(this.getCustomName());
	}
	
	protected Item getLoot() {
		//return isBurning() ? Items.COOKED_PORKCHOP : Items.PORKCHOP;
		return super.getLoot();
	}
	  
	protected void dropDeathLoot(boolean flag, int i) {
		/*int j = this.random.nextInt(3) + 1 + this.random.nextInt(1 + i);
		for (int k = 0; k < j; k++) {
			if (isBurning()) {
				a(Items.COOKED_PORKCHOP, 1);
			} else {
				a(Items.PORKCHOP, 1);
			}
		}
		if (hasSaddle()) {
			a(Items.SADDLE, 1);
		}*/
		
		super.dropDeathLoot(flag, i);
	    
		// Dodatkowa nagroda
	    int reward=this.random.nextInt(5); // 0..4	    
	    System.out.println("Reward: "+reward);
	    
    	if(reward==0) {
			a(Items.EMERALD, 1); // 20%
		    System.out.println("CustomPig Emerald");
	    } else if(reward==1) {
			a(Items.GOLD_NUGGET, 1); // 20%
		    System.out.println("CustomPig Gold");
	    } 
	}
	
	
	
	public String toString() {
		return "CustomPig";
	}
	
}


package com.krzyskrzys;

//import java.util.Random;

import net.minecraft.server.v1_8_R2.EntityChicken;
import net.minecraft.server.v1_8_R2.World;


public class CustomChicken extends EntityChicken {
	
	public CustomChicken(World world) {
		super(world);
		//this.setCustomName("CustomChicken"+Integer.toString(new Random().nextInt()));
		//this.setCustomNameVisible(true);
		//System.out.println(this.getCustomName());
		
	}
	
	public String toString() {
		return "CustomChicken";
	}
}

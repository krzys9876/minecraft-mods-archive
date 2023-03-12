package com.krzyskrzys;

import java.util.Random;

import net.minecraft.server.v1_8_R1.EntitySheep;
import net.minecraft.server.v1_8_R1.World;


public class CustomSheep extends EntitySheep {
	
	public CustomSheep(World world) {
		super(world);
		//this.setCustomName("CustomSheep"+Integer.toString(new Random().nextInt()));
		//this.setCustomNameVisible(true);
		//System.out.println(this.getCustomName());
		
	}
	
	public String toString() {
		return "CustomSheep";
	}
}

package com.krzyskrzys;

import java.util.Random;

import com.krzyskrzys.utils.*;

import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.EntitySkeleton;
import net.minecraft.server.v1_8_R1.World;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;


public class CustomSkeleton extends EntitySkeleton {
	
	public int customLevel=1;
	public int customLevelMultiplier=1;
	public int customID=0;
	
	public CustomSkeleton(World world) {
		super(world);
		
		int rand=new Random().nextInt(10)+1;
		
		if(rand>4) {
			rand=1;
		}
		customLevel=rand;
		customLevelMultiplier=rand; // nadmiarowe
		customID=new Random().nextInt();
		this.setCustomName("Skeleton level "+Integer.toString(customLevel));
		this.setCustomNameVisible(true);
		
		
		LivingEntity livEntity=(LivingEntity)this.getBukkitEntity();
		
		if(customLevel==4) {		
			livEntity.getEquipment().setHelmet(Utils.enchantItemMultiF(new ItemStack(Material.DIAMOND_HELMET),
					new Enchantment[] {Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_FALL,
					Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_ENVIRONMENTAL}));
						
			livEntity.getEquipment().setChestplate(Utils.enchantItemMultiF(new ItemStack(Material.DIAMOND_CHESTPLATE),
					new Enchantment[] {Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_FALL,
					Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_ENVIRONMENTAL}));
			
			livEntity.getEquipment().setLeggings(Utils.enchantItemMultiF(new ItemStack(Material.DIAMOND_LEGGINGS),
					new Enchantment[] {Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_FALL,
					Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_ENVIRONMENTAL}));
			
			livEntity.getEquipment().setBoots(Utils.enchantItemMultiF(new ItemStack(Material.DIAMOND_BOOTS),
					new Enchantment[] {Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_FALL,
					Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_ENVIRONMENTAL}));			
			customLevelMultiplier=10;
			
			livEntity.getEquipment().setItemInHand(Utils.enchantItemMultiF(new ItemStack(Material.BOW),
					new Enchantment[] {Enchantment.ARROW_DAMAGE,Enchantment.ARROW_FIRE,Enchantment.ARROW_INFINITE,Enchantment.ARROW_KNOCKBACK}));

		} else if(customLevel==3) {
			livEntity.getEquipment().setHelmet(Utils.enchantItemMultiF(new ItemStack(Material.GOLD_HELMET),
					new Enchantment[] {Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_FALL,
					Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_ENVIRONMENTAL}));
						
			livEntity.getEquipment().setChestplate(Utils.enchantItemMultiF(new ItemStack(Material.GOLD_CHESTPLATE),
					new Enchantment[] {Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_FALL,
					Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_ENVIRONMENTAL}));
			
			livEntity.getEquipment().setLeggings(Utils.enchantItemMultiF(new ItemStack(Material.GOLD_LEGGINGS),
					new Enchantment[] {Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_FALL,
					Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_ENVIRONMENTAL}));
			
			livEntity.getEquipment().setBoots(Utils.enchantItemMultiF(new ItemStack(Material.GOLD_BOOTS),
					new Enchantment[] {Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_FALL,
					Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_ENVIRONMENTAL}));			
			customLevelMultiplier=8;
			
			livEntity.getEquipment().setItemInHand(Utils.enchantItemMultiF(new ItemStack(Material.BOW),
					new Enchantment[] {Enchantment.ARROW_DAMAGE,Enchantment.ARROW_FIRE,Enchantment.ARROW_INFINITE,Enchantment.ARROW_KNOCKBACK}));
			
		} else if(customLevel==2) {
			livEntity.getEquipment().setHelmet(Utils.enchantItemMultiF(new ItemStack(Material.IRON_HELMET),
					new Enchantment[] {Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_FALL,
					Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_ENVIRONMENTAL}));
						
			livEntity.getEquipment().setChestplate(Utils.enchantItemMultiF(new ItemStack(Material.IRON_CHESTPLATE),
					new Enchantment[] {Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_FALL,
					Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_ENVIRONMENTAL}));
			
			livEntity.getEquipment().setLeggings(Utils.enchantItemMultiF(new ItemStack(Material.IRON_LEGGINGS),
					new Enchantment[] {Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_FALL,
					Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_ENVIRONMENTAL}));
			
			livEntity.getEquipment().setBoots(Utils.enchantItemMultiF(new ItemStack(Material.IRON_BOOTS),
					new Enchantment[] {Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_FALL,
					Enchantment.PROTECTION_FIRE,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_ENVIRONMENTAL}));			
			customLevelMultiplier=6;
			
			livEntity.getEquipment().setItemInHand(Utils.enchantItemMultiF(new ItemStack(Material.BOW),
					new Enchantment[] {Enchantment.ARROW_DAMAGE,Enchantment.ARROW_FIRE,Enchantment.ARROW_INFINITE,Enchantment.ARROW_KNOCKBACK}));
		}		

		if(customLevel==1 && livEntity.getEquipment().getItemInHand()==null) {
			livEntity.getEquipment().setItemInHand(new ItemStack(Material.BOW));
		}
		
		//System.out.println(this.getCustomName());		
		
	}
	
	// metoda odpowiadaj¹ca za wystrzelenie strza³y, f reprezentuje poziom obra¿eñ
	public void a(EntityLiving entityliving, float f) {
		super.a(entityliving, f * customLevelMultiplier);
	}
	
	public String toString() {
		return "CustomSkeleton";
	}
}

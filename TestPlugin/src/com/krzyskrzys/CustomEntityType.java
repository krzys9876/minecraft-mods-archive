package com.krzyskrzys;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import net.minecraft.server.v1_8_R1.BiomeBase;
import net.minecraft.server.v1_8_R1.BiomeMeta;
import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.EntityTypes;
import net.minecraft.server.v1_8_R1.EntitySkeleton;
import net.minecraft.server.v1_8_R1.EntitySpider;
import net.minecraft.server.v1_8_R1.EntityCaveSpider;
import net.minecraft.server.v1_8_R1.EntityZombie;
import net.minecraft.server.v1_8_R1.EntityPig;
import net.minecraft.server.v1_8_R1.EntityCow;
import net.minecraft.server.v1_8_R1.EntityChicken;
import net.minecraft.server.v1_8_R1.EntitySheep;

import org.bukkit.entity.EntityType;

public enum CustomEntityType {
	SKELETON("Skeleton", 51, EntityType.SKELETON, EntitySkeleton.class, CustomSkeleton.class),
	SPIDER("Spider", 52, EntityType.SPIDER, EntitySpider.class, CustomSpider.class),
	CAVE_SPIDER("CaveSpider", 59, EntityType.CAVE_SPIDER, EntityCaveSpider.class, CustomCaveSpider.class),
	ZOMBIE("Zombie", 54, EntityType.ZOMBIE, EntityZombie.class, CustomZombie.class),
	PIG("Pig", 90, EntityType.PIG, EntityPig.class, CustomPig.class),
	SHEEP("Sheep", 91, EntityType.SHEEP, EntitySheep.class, CustomSheep.class),
	COW("Cow", 92, EntityType.COW, EntityCow.class, CustomCow.class),
	CHICKEN("Chicken", 93, EntityType.CHICKEN, EntityChicken.class, CustomChicken.class);
	
	private String name;
	private int id;
	private EntityType entityType;
	private Class<? extends EntityInsentient> nmsClass;
	private Class<? extends EntityInsentient> customClass;
	 
	private CustomEntityType(String name, int id, EntityType entityType, Class<? extends EntityInsentient> nmsClass,
	Class<? extends EntityInsentient> customClass) {
		this.name = name;
		this.id = id;
		this.entityType = entityType;
		this.nmsClass = nmsClass;
		this.customClass = customClass;
	}
	 
	public String getName() {
		return name;
	}
	 
	public int getID() {
		return id;
	}
	 
	public EntityType getEntityType() {
		return entityType;
	}
	 
	public Class<? extends EntityInsentient> getNMSClass() {
		return nmsClass;
	}
	 
	public Class<? extends EntityInsentient> getCustomClass() {
		return customClass;
	}
	 
	/**
	* Register our entities.
	*/
	public static void registerEntities() {
		System.out.println("registerEntities (inner)");
		for (CustomEntityType entity : values()) {
			//System.out.println("reg entity "+entity.toString());
			a(entity.getCustomClass(), entity.getName(), entity.getID());
		}
		// BiomeBase#biomes became private.
		BiomeBase[] biomes;
		try {
			biomes = (BiomeBase[]) getPrivateStatic(BiomeBase.class, "biomes");
		} catch (Exception exc) {
			// Unable to fetch.
			return;
		}
	
	for (BiomeBase biomeBase : biomes) {
		if (biomeBase == null) {
			break;
		}
		// This changed names from J, K, L and M.
		for (String field : new String[] { "at", "au", "av", "aw" })
			try {
				Field list = BiomeBase.class.getDeclaredField(field);
				list.setAccessible(true);
				@SuppressWarnings("unchecked")
				List<BiomeMeta> mobList = (List<BiomeMeta>) list.get(biomeBase);
		 
				// Write in our custom class.
				for (BiomeMeta meta : mobList) {
					for (CustomEntityType entity : values()) {
						if (entity.getNMSClass().equals(meta.b)) {
							meta.b = entity.getCustomClass();
							
							//System.out.println("register class: "+field+" "+meta.b.getName());
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	 
	/**
	* Unregister our entities to prevent memory leaks. Call on disable.
	*/
	public static void unregisterEntities() {
		for (CustomEntityType entity : values()) {
			// Remove our class references.
			try {								
				((Map)getPrivateStatic(EntityTypes.class, "d")).remove(entity.getCustomClass());
			} catch (Exception e) {
				e.printStackTrace();
			}
			 
			try {
				((Map) getPrivateStatic(EntityTypes.class, "f")).remove(entity.getCustomClass());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		 
		for (CustomEntityType entity : values()) { 
			try {
				// Unregister each entity by writing the NMS back in place of the custom class.
				a(entity.getNMSClass(), entity.getName(), entity.getID());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		 
		// Biomes#biomes was made private so use reflection to get it.
		BiomeBase[] biomes;
		try {
			biomes = (BiomeBase[]) getPrivateStatic(BiomeBase.class, "biomes");
		} catch (Exception exc) {
			// Unable to fetch.
			return;
		}
		for (BiomeBase biomeBase : biomes) {
			if (biomeBase == null) {
				break;
			}
			
			// The list fields changed names but update the meta regardless.
			for (String field : new String[] { "at", "au", "av", "aw" }) {
				try {
					Field list = BiomeBase.class.getDeclaredField(field);
					list.setAccessible(true);
					@SuppressWarnings("unchecked")
					List<BiomeMeta> mobList = (List<BiomeMeta>) list.get(biomeBase);
		 
					// Make sure the NMS class is written back over our custom class.
					for (BiomeMeta meta : mobList) { 
						for (CustomEntityType entity : values()) {
							if (entity.getCustomClass().equals(meta.b)) {
								meta.b = entity.getNMSClass();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	 
	/**
	* A convenience method.
	* @param clazz The class.
	* @param f The string representation of the private static field.
	* @return The object found
	* @throws Exception if unable to get the object.
	*/
	private static Object getPrivateStatic(Class clazz, String f) throws Exception {
		Field field = clazz.getDeclaredField(f);
		field.setAccessible(true);
		return field.get(null);
	}
	 
	/*
	* Since 1.7.2 added a check in their entity registration, simply bypass it and write to the maps ourself.
	*/	
	private static void a(Class paramClass, String paramString, int paramInt) {
		try {			
			((Map)getPrivateStatic(EntityTypes.class, "c")).put(paramString, paramClass);
			//System.out.println("map c "+((Map)getPrivateStatic(EntityTypes.class, "c")).toString());
			((Map)getPrivateStatic(EntityTypes.class, "d")).put(paramClass, paramString);
			((Map)getPrivateStatic(EntityTypes.class, "e")).put(Integer.valueOf(paramInt), paramClass);
			((Map)getPrivateStatic(EntityTypes.class, "f")).put(paramClass, Integer.valueOf(paramInt));
			((Map)getPrivateStatic(EntityTypes.class, "g")).put(paramString, Integer.valueOf(paramInt));
		} catch (Exception exc) {
			// Unable to register the new class.
			System.out.println("registerError");
		}
	}
}


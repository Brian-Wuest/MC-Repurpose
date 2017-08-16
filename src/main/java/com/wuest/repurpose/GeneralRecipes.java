package com.wuest.repurpose;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GeneralRecipes 
{
	public static void LoadRecipies()
	{
		if (Repurpose.proxy.getServerConfiguration().recipeConfiguration.get(
				Repurpose.proxy.getServerConfiguration().addArmorRecipesName))
		{
			GeneralRecipes.LoadArmorRecipes();
		}

		if (Repurpose.proxy.getServerConfiguration().recipeConfiguration.get(
				Repurpose.proxy.getServerConfiguration().addMiscRecipesName))
		{
			GeneralRecipes.LoadMiscRecipes();
		}
		
		if (Repurpose.proxy.getServerConfiguration().enableExtraGrassDrops)
		{
			GeneralRecipes.AddSeedsToGrassDrop();
		}
	}

	private static void LoadArmorRecipes()
	{
		// Smelt iron armor to ingots.
		Map<ItemStack, ItemStack> smeltingList = FurnaceRecipes.instance().getSmeltingList();
		ArrayList<Entry<ItemStack, ItemStack>> itemsToRemove = new ArrayList<Entry<ItemStack, ItemStack>>();
		
		for (Entry<ItemStack, ItemStack> entry : smeltingList.entrySet())
		{
			if ((entry.getKey().getItem() == Items.IRON_HELMET && entry.getValue().getItem() == Items.IRON_NUGGET)
					|| (entry.getKey().getItem() == Items.IRON_CHESTPLATE && entry.getValue().getItem() == Items.IRON_NUGGET)
					|| (entry.getKey().getItem() == Items.IRON_LEGGINGS && entry.getValue().getItem() == Items.IRON_NUGGET)
					|| (entry.getKey().getItem() == Items.IRON_BOOTS && entry.getValue().getItem() == Items.IRON_NUGGET)
					|| (entry.getKey().getItem() == Items.GOLDEN_HELMET && entry.getValue().getItem() == Items.GOLD_NUGGET)
					|| (entry.getKey().getItem() == Items.GOLDEN_CHESTPLATE && entry.getValue().getItem() == Items.GOLD_NUGGET)
					|| (entry.getKey().getItem() == Items.GOLDEN_LEGGINGS && entry.getValue().getItem() == Items.GOLD_NUGGET)
					|| (entry.getKey().getItem() == Items.GOLDEN_BOOTS && entry.getValue().getItem() == Items.GOLD_NUGGET))
			{
				itemsToRemove.add(entry);
			}
		}
		
		for (Entry<ItemStack, ItemStack> entry : itemsToRemove)
		{
			FurnaceRecipes.instance().getSmeltingList().remove(entry.getKey(), entry.getValue());
		}
		
		GameRegistry.addSmelting(Items.IRON_HELMET, new ItemStack(Items.IRON_INGOT, 5), 1f);
		GameRegistry.addSmelting(Items.IRON_CHESTPLATE, new ItemStack(Items.IRON_INGOT, 8), 1f);
		GameRegistry.addSmelting(Items.IRON_LEGGINGS, new ItemStack(Items.IRON_INGOT, 7), 1f);
		GameRegistry.addSmelting(Items.IRON_BOOTS, new ItemStack(Items.IRON_INGOT, 4), 1f);

		// Smelt gold armor to ingots.
		GameRegistry.addSmelting(Items.GOLDEN_HELMET, new ItemStack(Items.GOLD_INGOT, 5), 1f);
		GameRegistry.addSmelting(Items.GOLDEN_CHESTPLATE, new ItemStack(Items.GOLD_INGOT, 8), 1f);
		GameRegistry.addSmelting(Items.GOLDEN_LEGGINGS, new ItemStack(Items.GOLD_INGOT, 7), 1f);
		GameRegistry.addSmelting(Items.GOLDEN_BOOTS, new ItemStack(Items.GOLD_INGOT, 4), 1f);

		// Smelt diamond armor to diamonds.
		GameRegistry.addSmelting(Items.DIAMOND_HELMET, new ItemStack(Items.DIAMOND, 5), 1f);
		GameRegistry.addSmelting(Items.DIAMOND_CHESTPLATE, new ItemStack(Items.DIAMOND, 8), 1f);
		GameRegistry.addSmelting(Items.DIAMOND_LEGGINGS, new ItemStack(Items.DIAMOND, 7), 1f);
		GameRegistry.addSmelting(Items.DIAMOND_BOOTS, new ItemStack(Items.DIAMOND, 4), 1f);
	}

	private static void LoadMiscRecipes()
	{
		// Rotten Flesh to leather.
		GameRegistry.addSmelting(Items.ROTTEN_FLESH, new ItemStack(Items.LEATHER), 1f);

		// Make a recipe for Village Eggs.
		ItemStack eggReturnStack = new ItemStack(Items.SPAWN_EGG, 1);

		EntityEggInfo eggInfo = EntityList.ENTITY_EGGS.get(EntityList.getKey(EntityVillager.class));
		ItemStack potionOfWeakness = new ItemStack(Items.POTIONITEM);
		
		potionOfWeakness = PotionUtils.addPotionToItemStack(potionOfWeakness, PotionTypes.WEAKNESS);
		
		NBTTagCompound nbttagcompound = eggReturnStack.hasTagCompound() ? eggReturnStack.getTagCompound() : new NBTTagCompound();
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.setString("id", eggInfo.spawnedID.toString());
        nbttagcompound.setTag("EntityTag", nbttagcompound1);
        eggReturnStack.setTagCompound(nbttagcompound);
        
        ModRegistry.addShapedRecipe("villager_egg", eggReturnStack,
        		"abc",
        		"def",
        		"ghi",
        		'a', Items.STRING,
        		'b', new ItemStack(Items.SKULL, 1, 2),
        		'c', potionOfWeakness,
        		'd', Items.ROTTEN_FLESH,
        		'e', Items.LEATHER_CHESTPLATE,
        		'f', Items.GOLDEN_APPLE,
        		'g', Items.BONE,
        		'h', Items.LEATHER_LEGGINGS,
        		'i', Items.EMERALD);
        
        // Make mushroom and rabbit stew bowls stack.
        Items.MUSHROOM_STEW.setMaxStackSize(16);
        Items.RABBIT_STEW.setMaxStackSize(16);
	}


	private static void AddSeedsToGrassDrop()
	{
		MinecraftForge.addGrassSeed(new ItemStack(Items.BEETROOT_SEEDS), 5);
		MinecraftForge.addGrassSeed(new ItemStack(Items.MELON_SEEDS), 5);
		MinecraftForge.addGrassSeed(new ItemStack(Items.PUMPKIN_SEEDS), 5);
		MinecraftForge.addGrassSeed(new ItemStack(Items.DYE, 1, 3), 5);
	}
}

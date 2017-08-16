package com.wuest.repurpose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class GeneralRecipes 
{
	public static void LoadRecipies()
	{
		if (Repurpose.proxy.proxyConfiguration.recipeConfiguration.get(
				Repurpose.proxy.getServerConfiguration().addArmorRecipesName))
		{
			GeneralRecipes.LoadArmorRecipes();
		}

		if (Repurpose.proxy.proxyConfiguration.addMiscRecipes)
		{
			GeneralRecipes.LoadMiscRecipes();
		}

		if (Repurpose.proxy.proxyConfiguration.addNetherStarRecipe)
		{
			GeneralRecipes.LoadNetherStarRecipe();
		}
		
		if (Repurpose.proxy.proxyConfiguration.enableExtraGrassDrops)
		{
			GeneralRecipes.AddSeedsToGrassDrop();
		}
	}

	private static void LoadArmorRecipes()
	{
		// Start with leather armor back to leather.
		ModRegistry.addShapelessRecipe("leather_helm_to_leather", new ItemStack(Items.LEATHER, 5), Ingredient.fromStacks(new ItemStack(Items.LEATHER_HELMET, 1, 0)));
		ModRegistry.addShapelessRecipe("leather_chest_to_leather", new ItemStack(Items.LEATHER, 8), Ingredient.fromStacks(new ItemStack(Items.LEATHER_CHESTPLATE, 1, 0)));
		ModRegistry.addShapelessRecipe("leather_legs_to_leather", new ItemStack(Items.LEATHER, 7), Ingredient.fromStacks(new ItemStack(Items.LEATHER_LEGGINGS, 1, 0)));
		ModRegistry.addShapelessRecipe("leather_boots_to_leather", new ItemStack(Items.LEATHER, 4), Ingredient.fromStacks(new ItemStack(Items.LEATHER_BOOTS, 1, 0)));

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

		// Chainmail recipes.
		ModRegistry.addShapedRecipe("chain_helmet", new ItemStack(Items.CHAINMAIL_HELMET, 1), 
				"xxx",
				"y y",
				"   ",
				'x', Items.IRON_INGOT, 
				'y', Items.LEATHER);

		ModRegistry.addShapedRecipe("chain_chest", new ItemStack(Items.CHAINMAIL_CHESTPLATE, 1), 
				"y y",
				"yxy",
				"xxx",
				'x', Items.IRON_INGOT, 
				'y', Items.LEATHER);

		ModRegistry.addShapedRecipe("chain_legs", new ItemStack(Items.CHAINMAIL_LEGGINGS, 1), 
				"xxx",
				"y y",
				"y y",
				'x', Items.IRON_INGOT, 
				'y', Items.LEATHER);

		ModRegistry.addShapedRecipe("chain_boots", new ItemStack(Items.CHAINMAIL_BOOTS, 1), 
				"   ",
				"y y",
				"x x",
				'x', Items.IRON_INGOT, 
				'y', Items.LEATHER);
	}

	private static void LoadMiscRecipes()
	{
		// Rotten Flesh to leather.
		GameRegistry.addSmelting(Items.ROTTEN_FLESH, new ItemStack(Items.LEATHER), 1f);

		// 4 Feathers and 1 string to 1 wool.
		ModRegistry.addShapedRecipe("string_to_wool", new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1), 
				" x ",
				"xyx",
				" x ",
				'x', 
				Items.FEATHER, 'y', Items.STRING);

		// Make a recipe for Clay: Sand + Water Bucket = Clay.
		ModRegistry.addShapelessRecipe("clay", new ItemStack(Item.getItemFromBlock(Blocks.CLAY), 2), 
				Ingredient.fromStacks(new ItemStack(Items.WATER_BUCKET)),
				Ingredient.fromStacks(new ItemStack(Item.getItemFromBlock(Blocks.SAND))),
				Ingredient.fromStacks(new ItemStack(Item.getItemFromBlock(Blocks.GRAVEL))));

		// Make a recipe for glowstone: redstone + gun powder + yellow dye.
		ModRegistry.addShapelessRecipe("glowstone", new ItemStack(Items.GLOWSTONE_DUST, 2), 
				Ingredient.fromStacks(new ItemStack(Items.REDSTONE)),
				Ingredient.fromStacks(new ItemStack(Items.GUNPOWDER)),
				Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 11)));
		
		// Make a recipe for slimeballs: Clay Ball + Lime Dye + Water Bucket
		ModRegistry.addShapedRecipe("slimeball", new ItemStack(Items.SLIME_BALL, 2),
				" x",
				"yz",
				'x', Items.WATER_BUCKET,
				'y', new ItemStack(Items.DYE, 1, 10),
				'z', Items.CLAY_BALL);
		
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

	private static void LoadNetherStarRecipe()
	{
		// 4 Quartz Blocks + 4 wither skulls + 1 Diamond Block = Nether Star
		ModRegistry.addShapedRecipe("nether_star", new ItemStack(Items.NETHER_STAR, 1), 
				"yxy",
				"xzx",
				"yxy",
				'x', new ItemStack(Item.getItemFromBlock(Blocks.QUARTZ_BLOCK)), 
				'y', new ItemStack(Items.SKULL, 1, 1), 
				'z',  new ItemStack(Item.getItemFromBlock(Blocks.DIAMOND_BLOCK)));
	}

	private static void AddSeedsToGrassDrop()
	{
		MinecraftForge.addGrassSeed(new ItemStack(Items.BEETROOT_SEEDS), 5);
		MinecraftForge.addGrassSeed(new ItemStack(Items.MELON_SEEDS), 5);
		MinecraftForge.addGrassSeed(new ItemStack(Items.PUMPKIN_SEEDS), 5);
		MinecraftForge.addGrassSeed(new ItemStack(Items.DYE, 1, 3), 5);
	}
}

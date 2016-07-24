package com.wuest.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class GeneralRecipes 
{
	public static void LoadRecipies()
	{
		if (WuestUtilities.proxy.proxyConfiguration.addMetalRecipes)
		{
			GeneralRecipes.LoadMetalRecipes();
		}

		if (WuestUtilities.proxy.proxyConfiguration.addWoodRecipes)
		{
			GeneralRecipes.LoadWoodRecipes();
		}

		if (WuestUtilities.proxy.proxyConfiguration.addStoneRecipes)
		{
			GeneralRecipes.LoadStoneRecipes();
		}

		if (WuestUtilities.proxy.proxyConfiguration.addArmorRecipes)
		{
			GeneralRecipes.LoadArmorRecipes();
		}

		if (WuestUtilities.proxy.proxyConfiguration.addMiscRecipes)
		{
			GeneralRecipes.LoadMiscRecipes();
		}

		if (WuestUtilities.proxy.proxyConfiguration.addNetherStarRecipe)
		{
			GeneralRecipes.LoadNetherStarRecipe();
		}
		
		if (WuestUtilities.proxy.proxyConfiguration.enableExtraGrassDrops)
		{
			GeneralRecipes.AddSeedsToGrassDrop();
		}
	}

	private static void LoadMetalRecipes()
	{
		ArrayList<String> ingots = new ArrayList<String>();

		ingots.add("ingotCopper");
		ingots.add("ingotTin");
		ingots.add("ingotAluminum");

		for (String ingot : ingots)
		{
			// Add recipes for each type of basic metal.
			// Since multiple ores can register this metal, get all instances of that from the dictionary and create recipies for it.
			for (ItemStack stack : OreDictionary.getOres(ingot))
			{
				GeneralRecipes.LoadBucketRecipes(stack);

				GeneralRecipes.LoadShearsRecipes(stack);
			}
		}
	}

	private static void LoadBucketRecipes(ItemStack stack)
	{
		GameRegistry.addRecipe(new ItemStack(Items.BUCKET),
				"x x",
				" x ",
				'x', stack);

		GameRegistry.addRecipe(new ItemStack(Items.BUCKET),
				"   ",
				"x x",
				" x ",
				'x', stack);
	}

	private static void LoadShearsRecipes(ItemStack stack)
	{
		GameRegistry.addShapelessRecipe(new ItemStack(Items.SHEARS), stack, stack);
	}

	private static void LoadWoodRecipes()
	{
		for (int i = 0; i < 6; i++)
		{
			// wood slabs into sticks.
			GameRegistry.addRecipe(new ItemStack(Items.STICK, 4), 
					"xx",
					"xx", 
					'x', 
					new ItemStack(Blocks.WOODEN_SLAB, 1, i));

			// Wooden slabs into planks.
			GameRegistry.addRecipe(new ItemStack(Blocks.PLANKS, 1, i),
					"x",
					"x",
					'x',
					new ItemStack(Blocks.WOODEN_SLAB, 1, i));
		}

		// Stairs to planks
		HashMap<Block, ItemStack> stairs = new HashMap<Block, ItemStack>();
		stairs.put(Blocks.OAK_STAIRS, new ItemStack(Blocks.PLANKS, 3, 0));
		stairs.put(Blocks.STONE_STAIRS, new ItemStack(Blocks.COBBLESTONE, 3));
		stairs.put(Blocks.BRICK_STAIRS, new ItemStack(Blocks.BRICK_BLOCK, 3));
		stairs.put(Blocks.STONE_BRICK_STAIRS, new ItemStack(Blocks.STONEBRICK, 3));
		stairs.put(Blocks.NETHER_BRICK_STAIRS, new ItemStack(Blocks.NETHER_BRICK, 3));
		stairs.put(Blocks.SANDSTONE_STAIRS, new ItemStack(Blocks.SANDSTONE, 3));
		stairs.put(Blocks.RED_SANDSTONE_STAIRS, new ItemStack(Blocks.RED_SANDSTONE, 3));
		stairs.put(Blocks.SPRUCE_STAIRS, new ItemStack(Blocks.PLANKS, 3,1));
		stairs.put(Blocks.BIRCH_STAIRS, new ItemStack(Blocks.PLANKS, 3, 2));
		stairs.put(Blocks.JUNGLE_STAIRS, new ItemStack(Blocks.PLANKS, 3, 3));
		stairs.put(Blocks.QUARTZ_STAIRS, new ItemStack(Blocks.QUARTZ_BLOCK, 3));
		stairs.put(Blocks.ACACIA_STAIRS, new ItemStack(Blocks.PLANKS, 3, 4));
		stairs.put(Blocks.DARK_OAK_STAIRS, new ItemStack(Blocks.PLANKS, 3, 5));

		for(Map.Entry<Block, ItemStack> set : stairs.entrySet())
		{
			// Add recipe to turn 2 stairs into 3 blocks. This gets us back to 6 blocks used to make 4 stairs.
			GameRegistry.addRecipe(set.getValue(),
					"x",
					"x",
					'x', new ItemStack(set.getKey()));
		}
	}

	private static void LoadStoneRecipes()
	{
		// Make slabs back into full blocks.
		for (int i = 0; i < 8; i++)
		{
			ItemStack currentBlock = null;
			ItemStack currentSlab = new ItemStack(Blocks.STONE_SLAB, 1, i);

			// 2 is stone wood and is no longer craftable.
			if (i == 2)
			{
				continue;
			}

			switch (i)
			{
			case (0):
			{
				currentBlock = new ItemStack(Item.getItemFromBlock(Blocks.STONE));
				break;
			}

			case (1):
			{
				currentBlock = new ItemStack(Item.getItemFromBlock(Blocks.SANDSTONE));
				break;
			}

			case (3):
			{
				currentBlock = new ItemStack(Item.getItemFromBlock(Blocks.COBBLESTONE));
				break;
			}

			case (4):
			{
				currentBlock = new ItemStack(Item.getItemFromBlock(Blocks.BRICK_BLOCK));
				break;
			}

			case (5):
			{
				currentBlock = new ItemStack(Item.getItemFromBlock(Blocks.STONEBRICK));
				break;
			}

			case (6):
			{
				currentBlock = new ItemStack(Item.getItemFromBlock(Blocks.NETHER_BRICK));
				break;
			}

			case (7):
			{
				currentBlock = new ItemStack(Item.getItemFromBlock(Blocks.QUARTZ_BLOCK));
				break;
			}
			}

			GeneralRecipes.AddSlabRecipe(currentBlock, currentSlab, i);
		}

		GeneralRecipes.AddSlabRecipe(new ItemStack(Item.getItemFromBlock(Blocks.RED_SANDSTONE)), new ItemStack(Item.getItemFromBlock(Blocks.STONE_SLAB2)), 1);
	}

	private static void AddSlabRecipe(ItemStack currentBlock, ItemStack currentSlab, int i)
	{
		// Need a different recipe for stone brick as this will interfere with the chiseled stone.
		if (i == 5 || i == 1 || i == 7)
		{
			GameRegistry.addRecipe(currentBlock, 
					"xx",
					"xx",
					'x', currentSlab);

			return;
		}

		GameRegistry.addRecipe(currentBlock, 
				"x",
				"x",
				'x', currentSlab);
	}

	private static void LoadArmorRecipes()
	{
		// Start with leather armor back to leather.
		GameRegistry.addShapelessRecipe(new ItemStack(Items.LEATHER, 5), new ItemStack(Items.LEATHER_HELMET, 1, 0));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.LEATHER, 8), new ItemStack(Items.LEATHER_CHESTPLATE, 1, 0));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.LEATHER, 7), new ItemStack(Items.LEATHER_LEGGINGS, 1, 0));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.LEATHER, 4), new ItemStack(Items.LEATHER_BOOTS, 1, 0));

		// Smelt iron armor to ingots.
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
		GameRegistry.addRecipe(new ItemStack(Items.CHAINMAIL_HELMET, 1), 
				"xxx",
				"y y",
				"   ",
				'x', Items.IRON_INGOT, 
				'y', Items.LEATHER);

		GameRegistry.addRecipe(new ItemStack(Items.CHAINMAIL_CHESTPLATE, 1), 
				"y y",
				"yxy",
				"xxx",
				'x', Items.IRON_INGOT, 
				'y', Items.LEATHER);

		GameRegistry.addRecipe(new ItemStack(Items.CHAINMAIL_LEGGINGS, 1), 
				"xxx",
				"y y",
				"y y",
				'x', Items.IRON_INGOT, 
				'y', Items.LEATHER);

		GameRegistry.addRecipe(new ItemStack(Items.CHAINMAIL_BOOTS, 1), 
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
		GameRegistry.addRecipe(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1), 
				" x ",
				"xyx",
				" x ",
				'x', 
				Items.FEATHER, 'y', Items.STRING);

		// Make a recipe for Clay: Sand + Water Bucket = Clay.
		GameRegistry.addShapelessRecipe(new ItemStack(Item.getItemFromBlock(Blocks.CLAY), 2), 
				new ItemStack(Items.WATER_BUCKET),
				new ItemStack(Item.getItemFromBlock(Blocks.SAND)),
				new ItemStack(Item.getItemFromBlock(Blocks.GRAVEL)));

		// Make a recipe for glowstone: redstone + gun powder + yellow dye.
		GameRegistry.addShapelessRecipe(new ItemStack(Items.GLOWSTONE_DUST, 2), 
				new ItemStack(Items.REDSTONE),
				new ItemStack(Items.GUNPOWDER),
				new ItemStack(Items.DYE, 1, 11));
		
		// Make a recipe for slimeballs: Clay Ball + Lime Dye + Water Bucket
		GameRegistry.addRecipe(new ItemStack(Items.SLIME_BALL, 2),
				" x",
				"yz",
				'x', Items.WATER_BUCKET,
				'y', new ItemStack(Items.DYE, 1, 10),
				'z', Items.CLAY_BALL);
		
		// Make a recipe for Village Eggs.
		ItemStack eggReturnStack = new ItemStack(Items.SPAWN_EGG, 1);
		EntityEggInfo eggInfo = EntityList.ENTITY_EGGS.get("Villager");
		ItemStack potionOfWeakness = new ItemStack(Items.POTIONITEM);
		
		potionOfWeakness = PotionUtils.addPotionToItemStack(potionOfWeakness, PotionTypes.WEAKNESS);
		
		NBTTagCompound nbttagcompound = eggReturnStack.hasTagCompound() ? eggReturnStack.getTagCompound() : new NBTTagCompound();
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.setString("id", eggInfo.spawnedID);
        nbttagcompound.setTag("EntityTag", nbttagcompound1);
        eggReturnStack.setTagCompound(nbttagcompound);
        
        GameRegistry.addRecipe(eggReturnStack,
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
	}

	private static void LoadNetherStarRecipe()
	{
		// 4 Quartz Blocks + 4 wither skulls + 1 Diamond Block = Nether Star
		GameRegistry.addRecipe(new ItemStack(Items.NETHER_STAR, 1), 
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
		MinecraftForge.addGrassSeed(new ItemStack(Items.POTATO), 5);
		MinecraftForge.addGrassSeed(new ItemStack(Items.CARROT), 5);
	}
}

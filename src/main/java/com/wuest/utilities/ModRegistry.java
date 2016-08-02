package com.wuest.utilities;

import java.util.ArrayList;

import com.wuest.utilities.Blocks.*;
import com.wuest.utilities.Items.*;
import com.wuest.utilities.Proxy.CommonProxy;
import com.wuest.utilities.Proxy.Messages.*;
import com.wuest.utilities.Proxy.Messages.Handlers.*;
import com.wuest.utilities.Tiles.TileEntityRedstoneClock;

import net.minecraft.block.Block;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class ModRegistry
{
	public static ArrayList<Item> ModItems = new ArrayList<Item>();
	public static ArrayList<Block> ModBlocks = new ArrayList<Block>();
	
	public static BlockCustomWall DirtWall()
	{
		for (Block entry : ModRegistry.ModBlocks)
		{
			if (entry instanceof BlockCustomWall
					&& ((BlockCustomWall)entry).BlockVariant == BlockCustomWall.EnumType.DIRT)
			{
				return (BlockCustomWall)entry;
			}
		}
		
		return null;
	}
	
	public static BlockCustomWall GrassWall()
	{
		for (Block entry : ModRegistry.ModBlocks)
		{
			if (entry instanceof BlockCustomWall
					&& ((BlockCustomWall)entry).BlockVariant == BlockCustomWall.EnumType.GRASS)
			{
				return (BlockCustomWall)entry;
			}
		}
		
		return null;
	}
	
	public static ItemBedCompass BedCompass()
	{
		return ModRegistry.GetItem(ItemBedCompass.class);
	}
	
	public static RedstoneClock RedStoneClock()
	{
		return ModRegistry.GetBlock(RedstoneClock.class);
	}
	
	public static ItemSwiftBlade SwiftBlade(ToolMaterial toolMaterial)
	{
		for (Item item : ModRegistry.ModItems)
		{
			if (item instanceof ItemSwiftBlade
					&& ((ItemSwiftBlade)item).getToolMaterial() == toolMaterial)
			{
				return (ItemSwiftBlade)item;
			}
		}
		
		return null;
	}
	
	public static BlockDirtStairs DirtStairs()
	{
		return ModRegistry.GetBlock(BlockDirtStairs.class);
	}
	
	/**
	 * Gets the item from the ModItems collections.
	 * @param genericClass The class of item to get from the collection.
	 * @return Null if the item could not be found otherwise the item found.
	 */
	public static <T extends Item> T GetItem(Class<T> genericClass)
	{
		for (Item entry : ModRegistry.ModItems)
		{
			if (entry.getClass().isAssignableFrom(genericClass))
			{
				return (T)entry;
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the block from the ModBlockss collections.
	 * @param genericClass The class of block to get from the collection.
	 * @return Null if the block could not be found otherwise the block found.
	 */
	public static <T extends Block> T GetBlock(Class<T> genericClass)
	{
		for (Block entry : ModRegistry.ModBlocks)
		{
			if (entry.getClass().isAssignableFrom(genericClass))
			{
				return (T)entry;
			}
		}
		
		return null;
	}
	
	/**
	 * This is where all in-game mod components (Items, Blocks) will be registered.
	 */
	public static void RegisterModComponents()
	{
		ModRegistry.registerBlock(new BlockCustomWall(Blocks.DIRT, BlockCustomWall.EnumType.DIRT));
		ModRegistry.registerBlock(new BlockCustomWall(Blocks.GRASS, BlockCustomWall.EnumType.GRASS));
		
		ModRegistry.registerItem(new ItemBedCompass("itemBedCompass"));
		
		ModRegistry.registerBlock(new RedstoneClock("redstoneClock"));
		GameRegistry.registerTileEntity(TileEntityRedstoneClock.class, "RedstoneClock");
		
		ModRegistry.registerBlock(new BlockDirtStairs());
	}
	
	/**
	 * This is where the mod recipes are registered.
	 */
	public static void RegisterRecipes()
	{
		GameRegistry.addRecipe(new ItemStack(ModRegistry.DirtWall(), 6, BlockCustomWall.EnumType.DIRT.getMetadata()),
				"xxx",
				"xxx",
				'x', Item.getItemFromBlock(Blocks.DIRT));
		
		GameRegistry.addRecipe(new ItemStack(Blocks.DIRT, 1),
				"x",
				'x', new ItemStack(Item.getItemFromBlock(ModRegistry.DirtWall()), 1, BlockCustomWall.EnumType.DIRT.getMetadata()));
		
		GameRegistry.addRecipe(new ItemStack(ModRegistry.GrassWall(), 6),
				"xxx",
				"xxx",
				'x', Item.getItemFromBlock(Blocks.GRASS));
		
		GameRegistry.addRecipe(new ItemStack(Blocks.GRASS, 1),
				"x",
				'x', new ItemStack(Item.getItemFromBlock(ModRegistry.GrassWall()), 1));
		
		if (WuestUtilities.proxy.proxyConfiguration.addBedCompassRecipe)
		{
			// Register recipe.
			GameRegistry.addShapelessRecipe(new ItemStack(ModRegistry.BedCompass()), 
					Items.BED, Items.COMPASS);
		}
		
		if (WuestUtilities.proxy.proxyConfiguration.addRedstoneClockRecipe)
		{
			// Register recipe.
			GameRegistry.addRecipe(new ItemStack(ModRegistry.RedStoneClock()),
					"xzx",
					"xyz",
					"xxx",
					'x', Item.getItemFromBlock(Blocks.STONE),
					'y', Items.REPEATER,
					'z', Item.getItemFromBlock(Blocks.REDSTONE_TORCH));
		}
		
		GeneralRecipes.LoadRecipies();
		
		ModRegistry.RegisterSwiftBlades();
		
		// Dirt Stairs.
		GameRegistry.addRecipe(
				new ItemStack(ModRegistry.DirtStairs(), 4),
				"  x",
				" xx",
				"xxx",
				'x', Blocks.DIRT);

		GameRegistry.addRecipe(
				new ItemStack(Blocks.DIRT, 3),
				"x",
				"x",
				'x', ModRegistry.DirtStairs());
	}
	
	/**
	 * This is where the mod messages are registered.
	 */
	public static void RegisterMessages()
	{
		WuestUtilities.network.registerMessage(RedstoneClockHandler.class, RedstoneClockMessage.class, 2, Side.SERVER);
		WuestUtilities.network.registerMessage(BedLocationHandler.class, BedLocationMessage.class, 3, Side.CLIENT);
		WuestUtilities.network.registerMessage(RedstoneScannerHandler.class, RedstoneScannerMessage.class, 4, Side.SERVER);
	}
	
	/**
	 * Register an Item
	 *
	 * @param item The Item instance
	 * @param <T> The Item type
	 * @return The Item instance
	 */
	public static <T extends Item> T registerItem(T item)
	{
		GameRegistry.register(item);
		ModRegistry.ModItems.add(item);

		return item;
	}
	
	public static <T extends Block> T registerBlock(T block)
	{
		GameRegistry.register(block);
		GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
		ModRegistry.ModBlocks.add(block);
		
		return block;
	}
	
	public static <T extends Block, I extends ItemBlock> T registerBlock(T block, I itemBlock)
	{
		GameRegistry.register(block);
		ModRegistry.ModBlocks.add(block);
		
		if (itemBlock != null)
		{
			GameRegistry.register(itemBlock);
			ModRegistry.ModItems.add(itemBlock);
		}
		
		return block;
	}
	
	/**
	 * Set the registry name of {@code item} to {@code itemName} and the un-localised name to the full registry name.
	 *
	 * @param item     The item
	 * @param itemName The item's name
	 */
	public static void setItemName(Item item, String itemName) 
	{
		item.setRegistryName(itemName);
		item.setUnlocalizedName(item.getRegistryName().toString());
	}
	
	/**
	 * Set the registry name of {@code block} to {@code blockName} and the un-localised name to the full registry name.
	 *
	 * @param block     The block
	 * @param blockName The block's name
	 */
	public static void setBlockName(Block block, String blockName) 
	{
		block.setRegistryName(blockName);
		block.setUnlocalizedName(block.getRegistryName().toString());
	}

	private static void RegisterSwiftBlades()
	{
		for (int i = 0; i < 5; i++)
		{
			ItemSwiftBlade itemToRegister = null;
			Object bladeItem = null;
			
			switch (i)
			{
				case 0:
				{
					itemToRegister = new ItemSwiftBlade(ToolMaterial.WOOD);
					bladeItem = Blocks.PLANKS;
					break;
				}
				
				case 1:
				{
					itemToRegister = new ItemSwiftBlade(ToolMaterial.STONE);
					bladeItem = Item.getItemFromBlock(Blocks.COBBLESTONE);
					break;
				}
				
				case 2:
				{
					itemToRegister = new ItemSwiftBlade(ToolMaterial.IRON);
					bladeItem = Items.IRON_INGOT;
					break;
				}
				
				case 3:
				{
					itemToRegister = new ItemSwiftBlade(ToolMaterial.GOLD);
					bladeItem = Items.GOLD_INGOT;
					break;
				}
				
				case 4:
				{
					itemToRegister = new ItemSwiftBlade(ToolMaterial.DIAMOND);
					bladeItem = Items.DIAMOND;
					break;
				}
			}
			
			CommonProxy.registerItem(itemToRegister);
			
			if (WuestUtilities.proxy.proxyConfiguration.addSwiftBladeRecipe)
			{
				// Register recipe.
				GameRegistry.addShapedRecipe(
						new ItemStack(itemToRegister),
						"  x",
						" x ",
						"y  ",
						'x', bladeItem,
						'y', Items.STICK);

				GameRegistry.addShapedRecipe(
						new ItemStack(itemToRegister),
						"x  ",
						" x ",
						"  y",
						'x', bladeItem,
						'y', Items.STICK);
			}
		}
	}
}
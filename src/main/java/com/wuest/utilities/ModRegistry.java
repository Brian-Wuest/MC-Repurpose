package com.wuest.utilities;

import java.util.ArrayList;

import com.wuest.utilities.Capabilities.*;
import com.wuest.utilities.Capabilities.Storage.BlockModelStorage;
import com.wuest.utilities.Capabilities.Storage.DimensionHomeStorage;
import com.wuest.utilities.Blocks.*;
import com.wuest.utilities.Items.*;
import com.wuest.utilities.Proxy.CommonProxy;
import com.wuest.utilities.Proxy.Messages.*;
import com.wuest.utilities.Proxy.Messages.Handlers.*;
import com.wuest.utilities.Tiles.*;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class ModRegistry
{
	/**
	 * This capability is used to store the block model resource location for the redstone infusion feature.
	 */
	@CapabilityInject(IBlockModelCapability.class)
	public static Capability<IBlockModelCapability> BlockModel = null;

	/**
	 * This capability is used to save the locations where a player spawns when transferring dimensions.
	 */
	@CapabilityInject(IDimensionHome.class)
	public static Capability<IDimensionHome> DimensionHomes = null;

	public static ArrayList<Item> ModItems = new ArrayList<Item>();
	public static ArrayList<Block> ModBlocks = new ArrayList<Block>();

	public static BlockCustomWall DirtWall()
	{
		for (Block entry : ModRegistry.ModBlocks)
		{
			if (entry instanceof BlockCustomWall && ((BlockCustomWall) entry).BlockVariant == BlockCustomWall.EnumType.DIRT)
			{
				return (BlockCustomWall) entry;
			}
		}

		return null;
	}

	public static BlockCustomWall GrassWall()
	{
		for (Block entry : ModRegistry.ModBlocks)
		{
			if (entry instanceof BlockCustomWall && ((BlockCustomWall) entry).BlockVariant == BlockCustomWall.EnumType.GRASS)
			{
				return (BlockCustomWall) entry;
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
			if (item instanceof ItemSwiftBlade && ((ItemSwiftBlade) item).getToolMaterial() == toolMaterial)
			{
				return (ItemSwiftBlade) item;
			}
		}

		return null;
	}

	public static BlockDirtStairs DirtStairs()
	{
		return ModRegistry.GetBlock(BlockDirtStairs.class);
	}

	public static BlockGrassStairs GrassStairs()
	{
		return ModRegistry.GetBlock(BlockGrassStairs.class);
	}

	public static BlockHalfDirtSlab DirtSlab()
	{
		return ModRegistry.GetBlock(BlockHalfDirtSlab.class);
	}

	public static BlockDoubleDirtSlab DoubleDirtSlab()
	{
		return ModRegistry.GetBlock(BlockDoubleDirtSlab.class);
	}

	public static BlockHalfGrassSlab GrassSlab()
	{
		return ModRegistry.GetBlock(BlockHalfGrassSlab.class);
	}

	public static BlockDoubleGrassSlab DoubleGrassSlab()
	{
		return ModRegistry.GetBlock(BlockDoubleGrassSlab.class);
	}

	public static BlockEnrichedFarmland EnrichedFarmland()
	{
		return ModRegistry.GetBlock(BlockEnrichedFarmland.class);
	}

	public static BlockMiniRedstone MiniRedstone()
	{
		return ModRegistry.GetBlock(BlockMiniRedstone.class);
	}

	public static BlockRedstoneScanner RedstoneScanner()
	{
		return ModRegistry.GetBlock(BlockRedstoneScanner.class);
	}

	public static ItemDiamondShard DiamondShard()
	{
		return ModRegistry.GetItem(ItemDiamondShard.class);
	}

	public static ItemFluffyFabric FluffyFabric()
	{
		return ModRegistry.GetItem(ItemFluffyFabric.class);
	}

	public static ItemSnorkel Snorkel()
	{
		return ModRegistry.GetItem(ItemSnorkel.class);
	}

	public static ItemWhetStone WhetStone()
	{
		return ModRegistry.GetItem(ItemWhetStone.class);
	}

	public static BlockHalfGlowstoneSlab GlowstoneSlab()
	{
		return ModRegistry.GetBlock(BlockHalfGlowstoneSlab.class);
	}
	
	/**
	 * Gets the item from the ModItems collections.
	 * 
	 * @param genericClass The class of item to get from the collection.
	 * @return Null if the item could not be found otherwise the item found.
	 */
	public static <T extends Item> T GetItem(Class<T> genericClass)
	{
		for (Item entry : ModRegistry.ModItems)
		{
			if (entry.getClass().isAssignableFrom(genericClass))
			{
				return (T) entry;
			}
		}

		return null;
	}

	/**
	 * Gets the block from the ModBlockss collections.
	 * 
	 * @param genericClass The class of block to get from the collection.
	 * @return Null if the block could not be found otherwise the block found.
	 */
	public static <T extends Block> T GetBlock(Class<T> genericClass)
	{
		for (Block entry : ModRegistry.ModBlocks)
		{
			if (entry.getClass().isAssignableFrom(genericClass))
			{
				return (T) entry;
			}
		}

		return null;
	}

	/**
	 * This is where all in-game mod components (Items, Blocks) will be
	 * registered.
	 */
	public static void RegisterModComponents()
	{
		ModRegistry.registerBlock(new BlockCustomWall(Blocks.DIRT, BlockCustomWall.EnumType.DIRT));
		ModRegistry.registerBlock(new BlockCustomWall(Blocks.GRASS, BlockCustomWall.EnumType.GRASS));

		ModRegistry.registerItem(new ItemBedCompass("itemBedCompass"));

		ModRegistry.registerBlock(new RedstoneClock("redstoneClock"));
		GameRegistry.registerTileEntity(TileEntityRedstoneClock.class, "RedstoneClock");

		ModRegistry.RegisterSwiftBlades();

		ModRegistry.registerBlock(new BlockDirtStairs());
		ModRegistry.registerBlock(new BlockGrassStairs());

		// Dirt Slab.
		BlockHalfDirtSlab registeredHalfDirtBlock = new BlockHalfDirtSlab();
		BlockDoubleDirtSlab registeredDoubleDirtSlab = new BlockDoubleDirtSlab();

		ItemBlockDirtSlab itemHalfDirtSlab = new ItemBlockDirtSlab(registeredHalfDirtBlock, registeredHalfDirtBlock, registeredDoubleDirtSlab, true);

		itemHalfDirtSlab = (ItemBlockDirtSlab) itemHalfDirtSlab.setRegistryName("blockHalfDirtSlab");

		ModRegistry.registerBlock(registeredHalfDirtBlock, itemHalfDirtSlab);
		ModRegistry.registerBlock(registeredDoubleDirtSlab, false);

		// Grass Slab
		BlockHalfGrassSlab registeredHalfGrassBlock = new BlockHalfGrassSlab();
		BlockDoubleGrassSlab registeredDoubleGrassSlab = new BlockDoubleGrassSlab();

		ItemBlockGrassSlab itemHalfGrassSlab = new ItemBlockGrassSlab(registeredHalfGrassBlock, registeredHalfGrassBlock, registeredDoubleGrassSlab, true);

		itemHalfGrassSlab = (ItemBlockGrassSlab) itemHalfGrassSlab.setRegistryName("blockHalfGrassSlab");

		ModRegistry.registerBlock(registeredHalfGrassBlock, itemHalfGrassSlab);
		ModRegistry.registerBlock(registeredDoubleGrassSlab, false);

		ModRegistry.registerBlock(new BlockEnrichedFarmland());
		ModRegistry.registerBlock(new BlockMiniRedstone());

		ModRegistry.registerBlock(new BlockRedstoneScanner());
		GameRegistry.registerTileEntity(TileEntityRedstoneScanner.class, "RedstoneScanner");

		// Diamond Shard
		ModRegistry.registerItem(new ItemDiamondShard("itemDiamondShard"));

		// Fluffy Fabric
		ModRegistry.registerItem(new ItemFluffyFabric("itemFluffyFabric"));

		// Snorkel
		ModRegistry.registerItem(new ItemSnorkel("itemSnorkel"));

		// Whetstone
		ModRegistry.registerItem(new ItemWhetStone("itemWhetStone"));

		// Glowstone Slabs.
		BlockHalfGlowstoneSlab registeredHalfGlowstoneBlock = new BlockHalfGlowstoneSlab();
		BlockDoubleGlowstoneSlab registeredDoubleGlowstoneSlab = new BlockDoubleGlowstoneSlab();

		ItemBlockGlowstoneSlab itemHalfGlowstoneSlab = new ItemBlockGlowstoneSlab(registeredHalfGlowstoneBlock, registeredHalfGlowstoneBlock,
				registeredDoubleGlowstoneSlab, true);

		itemHalfGlowstoneSlab = (ItemBlockGlowstoneSlab) itemHalfGlowstoneSlab.setRegistryName("blockHalfGlowstoneSlab");

		ModRegistry.registerBlock(registeredHalfGlowstoneBlock, itemHalfGlowstoneSlab);
		ModRegistry.registerBlock(registeredDoubleGlowstoneSlab, false);
	}

	/**
	 * This is where the mod recipes are registered.
	 */
	public static void RegisterRecipes()
	{
		GameRegistry.addRecipe(new ItemStack(ModRegistry.DirtWall(), 6, BlockCustomWall.EnumType.DIRT.getMetadata()), "xxx", "xxx", 'x',
				Item.getItemFromBlock(Blocks.DIRT));

		GameRegistry.addRecipe(new ItemStack(Blocks.DIRT, 1), "x", 'x',
				new ItemStack(Item.getItemFromBlock(ModRegistry.DirtWall()), 1, BlockCustomWall.EnumType.DIRT.getMetadata()));

		GameRegistry.addRecipe(new ItemStack(ModRegistry.GrassWall(), 6), "xxx", "xxx", 'x', Item.getItemFromBlock(Blocks.GRASS));

		GameRegistry.addRecipe(new ItemStack(Blocks.GRASS, 1), "x", 'x', new ItemStack(Item.getItemFromBlock(ModRegistry.GrassWall()), 1));

		if (WuestUtilities.proxy.proxyConfiguration.addBedCompassRecipe)
		{
			// Register recipe.
			GameRegistry.addShapelessRecipe(new ItemStack(ModRegistry.BedCompass()), Items.BED, Items.COMPASS);
		}

		if (WuestUtilities.proxy.proxyConfiguration.addRedstoneClockRecipe)
		{
			// Register recipe.
			GameRegistry.addRecipe(new ItemStack(ModRegistry.RedStoneClock()), "xzx", "xyz", "xxx", 'x', Item.getItemFromBlock(Blocks.STONE), 'y',
					Items.REPEATER, 'z', Item.getItemFromBlock(Blocks.REDSTONE_TORCH));
		}

		GeneralRecipes.LoadRecipies();

		// Dirt Stairs.
		GameRegistry.addRecipe(new ItemStack(ModRegistry.DirtStairs(), 4), "  x", " xx", "xxx", 'x', Blocks.DIRT);

		GameRegistry.addRecipe(new ItemStack(Blocks.DIRT, 3), "x", "x", 'x', ModRegistry.DirtStairs());

		// Grass Stairs.
		GameRegistry.addRecipe(new ItemStack(ModRegistry.GrassStairs(), 4), "  x", " xx", "xxx", 'x', Blocks.GRASS);

		GameRegistry.addRecipe(new ItemStack(Blocks.GRASS, 3), "x", "x", 'x', ModRegistry.GrassStairs());

		// Dirt Slab
		GameRegistry.addRecipe(new ItemStack(ModRegistry.DirtSlab(), 6), "xxx", 'x', Item.getItemFromBlock(Blocks.DIRT));

		GameRegistry.addRecipe(new ItemStack(Blocks.DIRT, 1), "x", "x", 'x', Item.getItemFromBlock(ModRegistry.DirtSlab()));

		// Grass Slab
		GameRegistry.addRecipe(new ItemStack(ModRegistry.GrassSlab(), 6), "xxx", 'x', Item.getItemFromBlock(Blocks.GRASS));

		GameRegistry.addRecipe(new ItemStack(Blocks.GRASS, 1), "x", "x", 'x', Item.getItemFromBlock(ModRegistry.GrassSlab()));

		// Enriched Farmland
		if (WuestUtilities.proxy.proxyConfiguration.addEnrichedFarmlandRecipe)
		{
			GameRegistry.addRecipe(new ItemStack(ModRegistry.EnrichedFarmland(), 3), "xxx", "aby", "xxx", 'x', Item.getItemFromBlock(Blocks.DIRT), 'a',
					Items.WHEAT, 'b', Items.WATER_BUCKET, 'y', Items.BONE);
		}

		// Mini-redstone
		GameRegistry.addShapedRecipe(new ItemStack(ModRegistry.MiniRedstone()), "xx", "xx", 'x', Items.REDSTONE);

		// Redstone Scanner
		GameRegistry.addShapedRecipe(new ItemStack(ModRegistry.RedstoneScanner()), "x x", "zyz", "x x", 'x', Items.REPEATER, 'y',
				Item.getItemFromBlock(Blocks.REDSTONE_BLOCK), 'z', Item.getItemFromBlock(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE));

		// Diamond Shard
		GameRegistry.addShapedRecipe(new ItemStack(Items.DIAMOND), "xx", "xx", 'x', ModRegistry.DiamondShard());

		// Fluffy Fabric
		GameRegistry.addShapedRecipe(new ItemStack(ModRegistry.FluffyFabric()), "xyx", "yyy", "xyx", 'x', Items.STRING, 'y',
				Item.getItemFromBlock(Blocks.WOOL));

		// Snorkel
		GameRegistry.addShapedRecipe(new ItemStack(ModRegistry.Snorkel()), "  x", "zzx", "yyx", 'x', Items.REEDS, 'y', Item.getItemFromBlock(Blocks.GLASS_PANE),
				'z', Items.STRING);

		// Whetstone
		GameRegistry.addShapedRecipe(new ItemStack(ModRegistry.WhetStone()), "xxx", "xyx", "xxx", 'x', Items.FLINT, 'y', Item.getItemFromBlock(Blocks.CLAY));

		// Glowstone Slab
		GameRegistry.addRecipe(new ItemStack(ModRegistry.GlowstoneSlab(), 6), "xxx", 'x', Item.getItemFromBlock(Blocks.GLOWSTONE));

		GameRegistry.addRecipe(new ItemStack(Blocks.GLOWSTONE, 1), "x", "x", 'x', Item.getItemFromBlock(ModRegistry.GlowstoneSlab()));
	}

	/**
	 * This is where the mod messages are registered.
	 */
	public static void RegisterMessages()
	{
		WuestUtilities.network.registerMessage(RedstoneClockHandler.class, RedstoneClockMessage.class, 2, Side.SERVER);
		WuestUtilities.network.registerMessage(BedLocationHandler.class, BedLocationMessage.class, 3, Side.CLIENT);
		WuestUtilities.network.registerMessage(RedstoneScannerHandler.class, RedstoneScannerMessage.class, 4, Side.SERVER);
		WuestUtilities.network.registerMessage(ConfigSyncHandler.class, ConfigSyncMessage.class, 5, Side.CLIENT);
	}

	/**
	 * This is where mod capabilities are registered.
	 */
	public static void RegisterCapabilities()
	{
		// Register the dimension home capability.
		CapabilityManager.INSTANCE.register(IDimensionHome.class, new DimensionHomeStorage(), DimensionHome.class);
		CapabilityManager.INSTANCE.register(IBlockModelCapability.class, new BlockModelStorage(), BlockModelCapability.class);
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
		return ModRegistry.registerBlock(block, true);
	}

	public static <T extends Block> T registerBlock(T block, boolean includeItemBlock)
	{
		GameRegistry.register(block);

		if (includeItemBlock)
		{
			GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
		}

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
	 * Set the registry name of {@code item} to {@code itemName} and the
	 * un-localised name to the full registry name.
	 *
	 * @param item The item
	 * @param itemName The item's name
	 */
	public static void setItemName(Item item, String itemName)
	{
		item.setRegistryName(itemName);
		item.setUnlocalizedName(item.getRegistryName().toString());
	}

	/**
	 * Set the registry name of {@code block} to {@code blockName} and the
	 * un-localised name to the full registry name.
	 *
	 * @param block The block
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

			ModRegistry.registerItem(itemToRegister);

			if (WuestUtilities.proxy.proxyConfiguration.addSwiftBladeRecipe)
			{
				// Register recipe.
				GameRegistry.addShapedRecipe(new ItemStack(itemToRegister), "  x", " x ", "y  ", 'x', bladeItem, 'y', Items.STICK);

				GameRegistry.addShapedRecipe(new ItemStack(itemToRegister), "x  ", " x ", "  y", 'x', bladeItem, 'y', Items.STICK);
			}
		}
	}
}

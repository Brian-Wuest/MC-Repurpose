package com.wuest.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wuest.utilities.Capabilities.*;
import com.wuest.utilities.Capabilities.Storage.BlockModelStorage;
import com.wuest.utilities.Capabilities.Storage.DimensionHomeStorage;
import com.wuest.utilities.Enchantment.EnchantmentStepAssist;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wuest.utilities.Blocks.*;
import com.wuest.utilities.Items.*;
import com.wuest.utilities.Proxy.CommonProxy;
import com.wuest.utilities.Proxy.Messages.*;
import com.wuest.utilities.Proxy.Messages.Handlers.*;
import com.wuest.utilities.Tiles.*;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.init.*;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
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

	private static EnchantmentStepAssist stepAssist;
	
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

	public static ItemStoneShears StoneShears()
	{
		return ModRegistry.GetItem(ItemStoneShears.class);
	}
	
	public static BlockHalfGlowstoneSlab GlowstoneSlab()
	{
		return ModRegistry.GetBlock(BlockHalfGlowstoneSlab.class);
	}
	
	public static EnchantmentStepAssist StepAssist()
	{
		return ModRegistry.stepAssist;
	}
	
	/**
	 * Static constructor for the mod registry.
	 */
	static
	{
		ModRegistry.RegisterModComponents();
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

		ModRegistry.registerItem(new ItemBedCompass("itembedcompass"));

		ModRegistry.registerBlock(new RedstoneClock("redstoneclock"));
		GameRegistry.registerTileEntity(TileEntityRedstoneClock.class, "RedstoneClock");

		ModRegistry.RegisterSwiftBlades();

		ModRegistry.registerBlock(new BlockDirtStairs());
		ModRegistry.registerBlock(new BlockGrassStairs());

		// Dirt Slab.
		BlockHalfDirtSlab registeredHalfDirtBlock = new BlockHalfDirtSlab();
		BlockDoubleDirtSlab registeredDoubleDirtSlab = new BlockDoubleDirtSlab();

		ItemBlockDirtSlab itemHalfDirtSlab = new ItemBlockDirtSlab(registeredHalfDirtBlock, registeredHalfDirtBlock, registeredDoubleDirtSlab, true);

		itemHalfDirtSlab = (ItemBlockDirtSlab) itemHalfDirtSlab.setRegistryName("blockhalfdirtslab");

		ModRegistry.registerBlock(registeredHalfDirtBlock, itemHalfDirtSlab);
		ModRegistry.registerBlock(registeredDoubleDirtSlab, false);

		// Grass Slab
		BlockHalfGrassSlab registeredHalfGrassBlock = new BlockHalfGrassSlab();
		BlockDoubleGrassSlab registeredDoubleGrassSlab = new BlockDoubleGrassSlab();

		ItemBlockGrassSlab itemHalfGrassSlab = new ItemBlockGrassSlab(registeredHalfGrassBlock, registeredHalfGrassBlock, registeredDoubleGrassSlab, true);

		itemHalfGrassSlab = (ItemBlockGrassSlab) itemHalfGrassSlab.setRegistryName("blockhalfgrassslab");

		ModRegistry.registerBlock(registeredHalfGrassBlock, itemHalfGrassSlab);
		ModRegistry.registerBlock(registeredDoubleGrassSlab, false);

		ModRegistry.registerBlock(new BlockEnrichedFarmland());
		ModRegistry.registerBlock(new BlockMiniRedstone());

		ModRegistry.registerBlock(new BlockRedstoneScanner());
		GameRegistry.registerTileEntity(TileEntityRedstoneScanner.class, "RedstoneScanner");

		// Diamond Shard
		ModRegistry.registerItem(new ItemDiamondShard("itemdiamondshard"));

		// Fluffy Fabric
		ModRegistry.registerItem(new ItemFluffyFabric("itemfluffyfabric"));

		// Snorkel
		ModRegistry.registerItem(new ItemSnorkel("itemsnorkel"));

		// Whetstone
		ModRegistry.registerItem(new ItemWhetStone("itemwhetstone"));

		// Glowstone Slabs.
		BlockHalfGlowstoneSlab registeredHalfGlowstoneBlock = new BlockHalfGlowstoneSlab();
		BlockDoubleGlowstoneSlab registeredDoubleGlowstoneSlab = new BlockDoubleGlowstoneSlab();

		ItemBlockGlowstoneSlab itemHalfGlowstoneSlab = new ItemBlockGlowstoneSlab(registeredHalfGlowstoneBlock, registeredHalfGlowstoneBlock,
				registeredDoubleGlowstoneSlab, true);

		itemHalfGlowstoneSlab = (ItemBlockGlowstoneSlab) itemHalfGlowstoneSlab.setRegistryName("blockhalfglowstoneslab");

		ModRegistry.registerBlock(registeredHalfGlowstoneBlock, itemHalfGlowstoneSlab);
		ModRegistry.registerBlock(registeredDoubleGlowstoneSlab, false);
		
		// Stone shears.
		ModRegistry.registerItem(new ItemStoneShears("item_stone_shears"));
	}

	public static void RegisterEnchantments()
	{
		ModRegistry.stepAssist = new EnchantmentStepAssist(Rarity.COMMON, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[] {EntityEquipmentSlot.FEET});
		ForgeRegistries.ENCHANTMENTS.register(ModRegistry.stepAssist);
	}
	
	/**
	 * This is where the mod recipes are registered.
	 */
	public static void RegisterRecipes()
	{
		ModRegistry.addShapedRecipe("dirt_wall", new ItemStack(ModRegistry.DirtWall(), 6, BlockCustomWall.EnumType.DIRT.getMetadata()), "xxx", "xxx", 'x',
				Item.getItemFromBlock(Blocks.DIRT));

		ModRegistry.addShapedRecipe("dirt_wass_reverse", new ItemStack(Blocks.DIRT, 1), "x", 'x',
				new ItemStack(Item.getItemFromBlock(ModRegistry.DirtWall()), 1, BlockCustomWall.EnumType.DIRT.getMetadata()));

		ModRegistry.addShapedRecipe("grass_wall", new ItemStack(ModRegistry.GrassWall(), 6), "xxx", "xxx", 'x', Item.getItemFromBlock(Blocks.GRASS));

		ModRegistry.addShapedRecipe("grass_wall_reverse", new ItemStack(Blocks.GRASS, 1), "x", 'x', new ItemStack(Item.getItemFromBlock(ModRegistry.GrassWall()), 1));

		if (WuestUtilities.proxy.proxyConfiguration.addBedCompassRecipe)
		{
			// Register recipe.
			ModRegistry.addShapelessRecipe("bed_compass", new ItemStack(ModRegistry.BedCompass()), Ingredient.fromItem(Items.BED), Ingredient.fromItem(Items.COMPASS));
		}

		if (WuestUtilities.proxy.proxyConfiguration.addRedstoneClockRecipe)
		{
			// Register recipe.
			ModRegistry.addShapedRecipe("redstone_clock", new ItemStack(ModRegistry.RedStoneClock()), "xzx", "xyz", "xxx", 'x', Item.getItemFromBlock(Blocks.STONE), 'y',
					Items.REPEATER, 'z', Item.getItemFromBlock(Blocks.REDSTONE_TORCH));
		}

		GeneralRecipes.LoadRecipies();

		// Dirt Stairs.
		ModRegistry.addShapedRecipe("dirt_stairs", new ItemStack(ModRegistry.DirtStairs(), 4), "  x", " xx", "xxx", 'x', Blocks.DIRT);

		ModRegistry.addShapedRecipe("dirt_stairs_reverse", new ItemStack(Blocks.DIRT, 3), "x", "x", 'x', ModRegistry.DirtStairs());

		// Grass Stairs.
		ModRegistry.addShapedRecipe("grass_stairs", new ItemStack(ModRegistry.GrassStairs(), 4), "  x", " xx", "xxx", 'x', Blocks.GRASS);

		ModRegistry.addShapedRecipe("grass_stairs_reverse", new ItemStack(Blocks.GRASS, 3), "x", "x", 'x', ModRegistry.GrassStairs());

		// Dirt Slab
		ModRegistry.addShapedRecipe("dirt_slab", new ItemStack(ModRegistry.DirtSlab(), 6), "xxx", 'x', Item.getItemFromBlock(Blocks.DIRT));

		ModRegistry.addShapedRecipe("dirt_slab_reverse", new ItemStack(Blocks.DIRT, 1), "x", "x", 'x', Item.getItemFromBlock(ModRegistry.DirtSlab()));

		// Grass Slab
		ModRegistry.addShapedRecipe("grass_slab", new ItemStack(ModRegistry.GrassSlab(), 6), "xxx", 'x', Item.getItemFromBlock(Blocks.GRASS));

		ModRegistry.addShapedRecipe("grass_slab_reverse", new ItemStack(Blocks.GRASS, 1), "x", "x", 'x', Item.getItemFromBlock(ModRegistry.GrassSlab()));

		// Enriched Farmland
		if (WuestUtilities.proxy.proxyConfiguration.addEnrichedFarmlandRecipe)
		{
			ModRegistry.addShapedRecipe("enriched_farmland", new ItemStack(ModRegistry.EnrichedFarmland(), 3), "xxx", "aby", "xxx", 'x', Item.getItemFromBlock(Blocks.DIRT), 'a',
					Items.WHEAT, 'b', Items.WATER_BUCKET, 'y', Items.BONE);
		}

		// Mini-redstone
		ModRegistry.addShapedRecipe("mini_redstone", new ItemStack(ModRegistry.MiniRedstone()), "xx", "xx", 'x', Items.REDSTONE);

		// Redstone Scanner
		ModRegistry.addShapedRecipe("redstone_scanner", new ItemStack(ModRegistry.RedstoneScanner()), "x x", "zyz", "x x", 'x', Items.REPEATER, 'y',
				Item.getItemFromBlock(Blocks.REDSTONE_BLOCK), 'z', Item.getItemFromBlock(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE));

		// Diamond Shard
		ModRegistry.addShapedRecipe("diamond_shard", new ItemStack(Items.DIAMOND), "xx", "xx", 'x', ModRegistry.DiamondShard());

		// Fluffy Fabric
		ModRegistry.addShapedRecipe("fluffy_fabric", new ItemStack(ModRegistry.FluffyFabric()), "xyx", "yyy", "xyx", 'x', Items.STRING, 'y',
				Item.getItemFromBlock(Blocks.WOOL));

		// Snorkel
		ModRegistry.addShapedRecipe("snorkel", new ItemStack(ModRegistry.Snorkel()), "  x", "zzx", "yyx", 'x', Items.REEDS, 'y', Item.getItemFromBlock(Blocks.GLASS_PANE),
				'z', Items.STRING);

		// Whetstone
		ModRegistry.addShapedRecipe("whetstone", new ItemStack(ModRegistry.WhetStone()), "xxx", "xyx", "xxx", 'x', Items.FLINT, 'y', Item.getItemFromBlock(Blocks.CLAY));

		// Glowstone Slab
		ModRegistry.addShapedRecipe("glowstone_slab", new ItemStack(ModRegistry.GlowstoneSlab(), 6), "xxx", 'x', Item.getItemFromBlock(Blocks.GLOWSTONE));

		ModRegistry.addShapedRecipe("glowstone_slab_reverse", new ItemStack(Blocks.GLOWSTONE, 1), "x", "x", 'x', Item.getItemFromBlock(ModRegistry.GlowstoneSlab()));
		
		// Stone Shears
		ModRegistry.addShapedRecipe("stone_shears", new ItemStack(ModRegistry.StoneShears()),
				"a a",
				" b ",
				'a', Items.FLINT,
				'b', Item.getItemFromBlock(Blocks.COBBLESTONE));
		
		for (int i = 0; i < 5; i++)
		{
			Object bladeItem = null;

			ToolMaterial material = ToolMaterial.WOOD;
			switch (i)
			{
				case 0:
				{
					bladeItem = Blocks.PLANKS;
					break;
				}

				case 1:
				{
					material = ToolMaterial.STONE;
					bladeItem = Item.getItemFromBlock(Blocks.COBBLESTONE);
					break;
				}

				case 2:
				{
					material = ToolMaterial.IRON;
					bladeItem = Items.IRON_INGOT;
					break;
				}

				case 3:
				{
					material = ToolMaterial.GOLD;
					bladeItem = Items.GOLD_INGOT;
					break;
				}

				case 4:
				{
					material = ToolMaterial.DIAMOND;
					bladeItem = Items.DIAMOND;
					break;
				}
			}

			if (WuestUtilities.proxy.proxyConfiguration.addSwiftBladeRecipe)
			{
				Item recipeOutput = null;
				
				for (Item item : ModRegistry.ModItems)
				{
					if (item instanceof ItemSwiftBlade && ((ItemSwiftBlade) item).getToolMaterial() == material)
					{
						recipeOutput = item;
						break;
					}
				}
				
				if (recipeOutput != null)
				{
					// Register recipe.
					ModRegistry.addShapedRecipe("swift_blade_" + ((Integer)i).toString(), new ItemStack(recipeOutput), "  x", " x ", "y  ", 'x', bladeItem, 'y', Items.STICK);
	
					ModRegistry.addShapedRecipe("swift_blade_reverse_" + ((Integer)i).toString(), new ItemStack(recipeOutput), "x  ", " x ", "  y", 'x', bladeItem, 'y', Items.STICK);
				}
			}
		}
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
		//ForgeRegistries.ITEMS.register(item);
		ModRegistry.ModItems.add(item);

		return item;
	}

	public static <T extends Block> T registerBlock(T block)
	{
		return ModRegistry.registerBlock(block, true);
	}

	public static <T extends Block> T registerBlock(T block, boolean includeItemBlock)
	{
		if (includeItemBlock)
		{
			ModRegistry.ModItems.add(new ItemBlock(block).setRegistryName(block.getRegistryName()));
		}

		ModRegistry.ModBlocks.add(block);
		return block;
	}

	public static <T extends Block, I extends ItemBlock> T registerBlock(T block, I itemBlock)
	{
		ModRegistry.ModBlocks.add(block);

		if (itemBlock != null)
		{
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
		}
	}
	
	/**
	 * This should only be used for registering recipes for vanilla objects and not mod-specific objects.
	 * @param name The name of the recipe. ModID is pre-pended to it.
	 * @param stack The output of the recipe.
	 * @param recipeComponents The recipe components.
	 */
	public static void addShapedRecipe(String name, ItemStack stack, Object... recipeComponents)
	{	
		ResourceLocation resourceLocation = new ResourceLocation(WuestUtilities.MODID.toLowerCase(), name);
		
		GameRegistry.addShapedRecipe(resourceLocation, resourceLocation, stack, recipeComponents);
	}
	
    /**
     * This should only be used for registering recipes for vanilla objects and not mod-specific objects.
     * @param name The name of the recipe.
     * @param stack The output stack.
     * @param recipeComponents The recipe components.
     */
    public static void addShapelessRecipe(String name, ItemStack stack, Ingredient... recipeComponents)
    {
    	ResourceLocation resourceLocation = new ResourceLocation(WuestUtilities.MODID.toLowerCase(), name);

		GameRegistry.addShapelessRecipe(resourceLocation, resourceLocation, stack, recipeComponents);
    }
}

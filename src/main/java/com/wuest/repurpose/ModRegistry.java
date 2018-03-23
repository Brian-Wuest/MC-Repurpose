package com.wuest.repurpose;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wuest.repurpose.Blocks.*;
import com.wuest.repurpose.Capabilities.*;
import com.wuest.repurpose.Capabilities.Storage.BlockModelStorage;
import com.wuest.repurpose.Capabilities.Storage.DimensionHomeStorage;
import com.wuest.repurpose.Enchantment.EnchantmentStepAssist;
import com.wuest.repurpose.Items.*;
import com.wuest.repurpose.Proxy.CommonProxy;
import com.wuest.repurpose.Proxy.Messages.*;
import com.wuest.repurpose.Proxy.Messages.Handlers.*;
import com.wuest.repurpose.Tiles.*;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
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
import net.minecraftforge.oredict.OreDictionary;

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
	
	public static ItemScroll Scroll()
	{
		return ModRegistry.GetItem(ItemScroll.class);
	}
	
	public static BlockHalfGlowstoneSlab GlowstoneSlab()
	{
		return ModRegistry.GetBlock(BlockHalfGlowstoneSlab.class);
	}
	
	public static EnchantmentStepAssist StepAssist()
	{
		return ModRegistry.stepAssist;
	}
	
	public static BlockCoffer Coffer()
	{
		return ModRegistry.GetBlock(BlockCoffer.class);
	}
	
	public static ItemWoodenCrate WoodenCrate()
	{
		return ModRegistry.GetItem(ItemWoodenCrate.class);
	}
	
	public static ItemGardnersPouch GardnersPounch()
	{
		return ModRegistry.GetItem(ItemGardnersPouch.class);
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
	
	public static <T extends Item> T GetItemSpecific(Class<T> genericClass)
	{
		for (Item entry : ModRegistry.ModItems)
		{
			if (entry.getClass() == genericClass)
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

		ModRegistry.registerItem(new ItemBedCompass("item_bed_compass"));

		ModRegistry.registerBlock(new RedstoneClock("block_redstone_clock"));
		GameRegistry.registerTileEntity(TileEntityRedstoneClock.class, "RedstoneClock");

		ModRegistry.registerBlock(new BlockDirtStairs());
		ModRegistry.registerBlock(new BlockGrassStairs());

		// Dirt Slab.
		BlockHalfDirtSlab registeredHalfDirtBlock = new BlockHalfDirtSlab();
		BlockDoubleDirtSlab registeredDoubleDirtSlab = new BlockDoubleDirtSlab();

		ItemBlockDirtSlab itemHalfDirtSlab = new ItemBlockDirtSlab(registeredHalfDirtBlock, registeredHalfDirtBlock, registeredDoubleDirtSlab, true);

		itemHalfDirtSlab = (ItemBlockDirtSlab) itemHalfDirtSlab.setRegistryName("block_half_dirt_slab");

		ModRegistry.registerBlock(registeredHalfDirtBlock, itemHalfDirtSlab);
		ModRegistry.registerBlock(registeredDoubleDirtSlab, false);

		// Grass Slab
		BlockHalfGrassSlab registeredHalfGrassBlock = new BlockHalfGrassSlab();
		BlockDoubleGrassSlab registeredDoubleGrassSlab = new BlockDoubleGrassSlab();

		ItemBlockGrassSlab itemHalfGrassSlab = new ItemBlockGrassSlab(registeredHalfGrassBlock, registeredHalfGrassBlock, registeredDoubleGrassSlab, true);

		itemHalfGrassSlab = (ItemBlockGrassSlab) itemHalfGrassSlab.setRegistryName("block_half_grass_slab");

		ModRegistry.registerBlock(registeredHalfGrassBlock, itemHalfGrassSlab);
		ModRegistry.registerBlock(registeredDoubleGrassSlab, false);

		ModRegistry.registerBlock(new BlockEnrichedFarmland());
		ModRegistry.registerBlock(new BlockMiniRedstone());

		ModRegistry.registerBlock(new BlockRedstoneScanner());
		GameRegistry.registerTileEntity(TileEntityRedstoneScanner.class, "RedstoneScanner");

		// Diamond Shard
		ModRegistry.registerItem(new ItemDiamondShard("item_diamond_shard"));

		// Fluffy Fabric
		ModRegistry.registerItem(new ItemFluffyFabric("item_fluffy_fabric"));

		// Snorkel
		ModRegistry.registerItem(new ItemSnorkel("item_snorkel"));

		// Whetstone
		ModRegistry.registerItem(new ItemWhetStone("item_whetstone"));

		// Glowstone Slabs.
		BlockHalfGlowstoneSlab registeredHalfGlowstoneBlock = new BlockHalfGlowstoneSlab();
		BlockDoubleGlowstoneSlab registeredDoubleGlowstoneSlab = new BlockDoubleGlowstoneSlab();

		ItemBlockGlowstoneSlab itemHalfGlowstoneSlab = new ItemBlockGlowstoneSlab(registeredHalfGlowstoneBlock, registeredHalfGlowstoneBlock,
				registeredDoubleGlowstoneSlab, true);

		itemHalfGlowstoneSlab = (ItemBlockGlowstoneSlab) itemHalfGlowstoneSlab.setRegistryName("block_half_glowstone_slab");

		ModRegistry.registerBlock(registeredHalfGlowstoneBlock, itemHalfGlowstoneSlab);
		ModRegistry.registerBlock(registeredDoubleGlowstoneSlab, false);
		
		// Stone shears.
		ModRegistry.registerItem(new ItemStoneShears("item_stone_shears"));
		
		// Sickles.
		ModRegistry.registerItem(new ItemSickle(ToolMaterial.WOOD, "item_wood_sickle"));
		ModRegistry.registerItem(new ItemSickle(ToolMaterial.STONE, "item_stone_sickle"));
		ModRegistry.registerItem(new ItemSickle(ToolMaterial.IRON, "item_iron_sickle"));
		ModRegistry.registerItem(new ItemSickle(ToolMaterial.DIAMOND, "item_diamond_sickle"));
		ModRegistry.registerItem(new ItemSickle(ToolMaterial.GOLD, "item_gold_sickle"));
		
		// Swift Blades.
		ModRegistry.registerItem(new ItemSwiftBlade(ToolMaterial.WOOD));
		ModRegistry.registerItem(new ItemSwiftBlade(ToolMaterial.STONE));
		ModRegistry.registerItem(new ItemSwiftBlade(ToolMaterial.IRON));
		ModRegistry.registerItem(new ItemSwiftBlade(ToolMaterial.DIAMOND));
		ModRegistry.registerItem(new ItemSwiftBlade(ToolMaterial.GOLD));
		
		// Iron lump.
		ModRegistry.registerItem(new ItemIronLump("item_iron_lump"));
		
		// Charcoal block.
		Block block = new BlockCharcoal("block_charcoal");
		ItemBlock itemBlock = (new ItemBlockBurnable(block)).setBurnTime(16000);
		ModRegistry.registerBlock(block, itemBlock);
		
		// Scroll
		ModRegistry.registerItem(new ItemScroll("item_scroll"));
		
		// Wooden Crate
		ModRegistry.registerItem(new ItemWoodenCrate("item_wooden_crate"));
		ModRegistry.WoodenCrate().setContainerItem(ModRegistry.WoodenCrate());
		
		// Gardner's Pouch
		ModRegistry.registerItem(new ItemGardnersPouch("item_gardners_pouch"));
		
		// Coffers.
		//block = new BlockCoffer();
		//itemBlock = new ItemBlockCoffer(block);
		//ModRegistry.registerBlock(block, itemBlock);
		//GameRegistry.registerTileEntity(TileEntityCoffer.class, "Coffer");
	}

	public static void RegisterEnchantments()
	{
		ModRegistry.stepAssist = new EnchantmentStepAssist(Rarity.COMMON, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[] {EntityEquipmentSlot.FEET});
		ForgeRegistries.ENCHANTMENTS.register(ModRegistry.stepAssist);
	}
	
	/**
	 * Registers records into the ore dictionary.
	 */
	public static void RegisterOreDictionaryRecords()
	{
		// Register certain blocks into the ore dictionary.
		OreDictionary.registerOre("blockCharcoal", ModRegistry.GetBlock(BlockCharcoal.class));
	}
	
	/**
	 * This is where the mod recipes are registered.
	 */
	public static void RegisterRecipes()
	{
		GeneralRecipes.LoadRecipies();
	}

	/**
	 * This is where the mod messages are registered.
	 */
	public static void RegisterMessages()
	{
		Repurpose.network.registerMessage(RedstoneClockHandler.class, RedstoneClockMessage.class, 2, Side.SERVER);
		Repurpose.network.registerMessage(BedLocationHandler.class, BedLocationMessage.class, 3, Side.CLIENT);
		Repurpose.network.registerMessage(RedstoneScannerHandler.class, RedstoneScannerMessage.class, 4, Side.SERVER);
		Repurpose.network.registerMessage(ConfigSyncHandler.class, ConfigSyncMessage.class, 5, Side.CLIENT);
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
	
	/**
	 * This should only be used for registering recipes for vanilla objects and not mod-specific objects.
	 * @param name The name of the recipe. ModID is pre-pended to it.
	 * @param stack The output of the recipe.
	 * @param recipeComponents The recipe components.
	 */
	public static void addShapedRecipe(String name, ItemStack stack, Object... recipeComponents)
	{	
		ResourceLocation resourceLocation = new ResourceLocation(Repurpose.MODID.toLowerCase(), name);
		
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
    	ResourceLocation resourceLocation = new ResourceLocation(Repurpose.MODID.toLowerCase(), name);

		GameRegistry.addShapelessRecipe(resourceLocation, resourceLocation, stack, recipeComponents);
    }
}

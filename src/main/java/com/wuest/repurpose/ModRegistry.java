package com.wuest.repurpose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.wuest.repurpose.Blocks.BlockCharcoal;
import com.wuest.repurpose.Blocks.BlockCoffer;
import com.wuest.repurpose.Blocks.BlockCustomWall;
import com.wuest.repurpose.Blocks.BlockDirtStairs;
import com.wuest.repurpose.Blocks.BlockDoubleDirtSlab;
import com.wuest.repurpose.Blocks.BlockDoubleGlowstoneSlab;
import com.wuest.repurpose.Blocks.BlockDoubleGrassSlab;
import com.wuest.repurpose.Blocks.BlockEnrichedFarmland;
import com.wuest.repurpose.Blocks.BlockGrassStairs;
import com.wuest.repurpose.Blocks.BlockHalfDirtSlab;
import com.wuest.repurpose.Blocks.BlockHalfGlowstoneSlab;
import com.wuest.repurpose.Blocks.BlockHalfGrassSlab;
import com.wuest.repurpose.Blocks.BlockMiniRedstone;
import com.wuest.repurpose.Blocks.BlockRedstoneScanner;
import com.wuest.repurpose.Blocks.RedstoneClock;
import com.wuest.repurpose.Capabilities.DimensionHome;
import com.wuest.repurpose.Capabilities.IDimensionHome;
import com.wuest.repurpose.Capabilities.Storage.DimensionHomeStorage;
import com.wuest.repurpose.Enchantment.EnchantmentStepAssist;
import com.wuest.repurpose.Items.ItemBagOfHolding;
import com.wuest.repurpose.Items.ItemBedCompass;
import com.wuest.repurpose.Items.ItemBlockBurnable;
import com.wuest.repurpose.Items.ItemBlockDirtSlab;
import com.wuest.repurpose.Items.ItemBlockGlowstoneSlab;
import com.wuest.repurpose.Items.ItemBlockGrassSlab;
import com.wuest.repurpose.Items.ItemDiamondShard;
import com.wuest.repurpose.Items.ItemFluffyFabric;
import com.wuest.repurpose.Items.ItemIronLump;
import com.wuest.repurpose.Items.ItemScroll;
import com.wuest.repurpose.Items.ItemSickle;
import com.wuest.repurpose.Items.ItemSnorkel;
import com.wuest.repurpose.Items.ItemStoneShears;
import com.wuest.repurpose.Items.ItemSwiftBlade;
import com.wuest.repurpose.Items.ItemWhetStone;
import com.wuest.repurpose.Items.ItemWoodenCrate;
import com.wuest.repurpose.Proxy.Messages.BagOfHoldingUpdateMessage;
import com.wuest.repurpose.Proxy.Messages.BedLocationMessage;
import com.wuest.repurpose.Proxy.Messages.ConfigSyncMessage;
import com.wuest.repurpose.Proxy.Messages.CurrentSlotUpdateMessage;
import com.wuest.repurpose.Proxy.Messages.RedstoneClockMessage;
import com.wuest.repurpose.Proxy.Messages.RedstoneScannerMessage;
import com.wuest.repurpose.Proxy.Messages.Handlers.BagOfHoldingUpdateMessageHandler;
import com.wuest.repurpose.Proxy.Messages.Handlers.BedLocationHandler;
import com.wuest.repurpose.Proxy.Messages.Handlers.ConfigSyncHandler;
import com.wuest.repurpose.Proxy.Messages.Handlers.CurrentSlotUpdateHandler;
import com.wuest.repurpose.Proxy.Messages.Handlers.RedstoneClockHandler;
import com.wuest.repurpose.Proxy.Messages.Handlers.RedstoneScannerHandler;
import com.wuest.repurpose.Tiles.TileEntityRedstoneClock;
import com.wuest.repurpose.Tiles.TileEntityRedstoneScanner;

import net.minecraft.block.Block;
import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class ModRegistry
{
	/**
	 * This capability is used to save the locations where a player spawns when transferring dimensions.
	 */
	@CapabilityInject(IDimensionHome.class)
	public static Capability<IDimensionHome> DimensionHomes = null;

	public static ArrayList<Item> ModItems = new ArrayList<Item>();
	public static ArrayList<Block> ModBlocks = new ArrayList<Block>();

	public static Map<String, ToolMaterial> CustomMaterials = new HashMap<String, ToolMaterial>();
	public static Map<String, Boolean> FoundMaterials = new HashMap<String, Boolean>();

	private static EnchantmentStepAssist stepAssist;

	public static BlockCustomWall DirtWall()
	{
		for (Block entry : ModRegistry.ModBlocks)
		{
			if (entry instanceof BlockCustomWall
				&& ((BlockCustomWall) entry).BlockVariant == BlockCustomWall.EnumType.DIRT)
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
			if (entry instanceof BlockCustomWall
				&& ((BlockCustomWall) entry).BlockVariant == BlockCustomWall.EnumType.GRASS)
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

	public static ItemBagOfHolding BagofHolding()
	{
		return ModRegistry.GetItem(ItemBagOfHolding.class);
	}

	public static ItemSwiftBlade CustomMaterialBlade(String materialName)
	{
		ToolMaterial material = ModRegistry.CustomMaterials.get(materialName);

		for (Item item : ModRegistry.ModItems)
		{
			if (item.getClass().isAssignableFrom(ItemSwiftBlade.class)
				&& ((ItemSwiftBlade) item).getToolMaterial() == material)
			{
				return ((ItemSwiftBlade) item);
			}
		}

		return null;
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
	 * This is where all in-game mod components (Items, Blocks) will be registered.
	 */
	public static void RegisterModComponents()
	{
		ModRegistry.RegisterToolMaterials();

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

		ItemBlockDirtSlab itemHalfDirtSlab = new ItemBlockDirtSlab(registeredHalfDirtBlock, registeredHalfDirtBlock,
			registeredDoubleDirtSlab, true);

		itemHalfDirtSlab = (ItemBlockDirtSlab) itemHalfDirtSlab.setRegistryName("block_half_dirt_slab");

		ModRegistry.registerBlock(registeredHalfDirtBlock, itemHalfDirtSlab);
		ModRegistry.registerBlock(registeredDoubleDirtSlab, false);

		// Grass Slab
		BlockHalfGrassSlab registeredHalfGrassBlock = new BlockHalfGrassSlab();
		BlockDoubleGrassSlab registeredDoubleGrassSlab = new BlockDoubleGrassSlab();

		ItemBlockGrassSlab itemHalfGrassSlab = new ItemBlockGrassSlab(registeredHalfGrassBlock,
			registeredHalfGrassBlock, registeredDoubleGrassSlab, true);

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

		ItemBlockGlowstoneSlab itemHalfGlowstoneSlab = new ItemBlockGlowstoneSlab(registeredHalfGlowstoneBlock,
			registeredHalfGlowstoneBlock, registeredDoubleGlowstoneSlab, true);

		itemHalfGlowstoneSlab = (ItemBlockGlowstoneSlab) itemHalfGlowstoneSlab
			.setRegistryName("block_half_glowstone_slab");

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

		Item item = new ItemSwiftBlade(ModRegistry.CustomMaterials.get("Copper"));
		ModRegistry.registerItem(item);

		item = new ItemSwiftBlade(ModRegistry.CustomMaterials.get("Osmium"));
		ModRegistry.registerItem(item);

		item = new ItemSwiftBlade(ModRegistry.CustomMaterials.get("Bronze"));
		ModRegistry.registerItem(item);
		
		item = new ItemSwiftBlade(ModRegistry.CustomMaterials.get("Steel"));
		ModRegistry.registerItem(item);
		
		item = new ItemSwiftBlade(ModRegistry.CustomMaterials.get("Obsidian"));
		ModRegistry.registerItem(item);

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

		// Bag of Holding
		ModRegistry.registerItem(new ItemBagOfHolding("item_bag_of_holding"));

		// Coffers.
		// block = new BlockCoffer();
		// itemBlock = new ItemBlockCoffer(block);
		// ModRegistry.registerBlock(block, itemBlock);
		// GameRegistry.registerTileEntity(TileEntityCoffer.class, "Coffer");
	}

	public static void RegisterEnchantments()
	{
		ModRegistry.stepAssist = new EnchantmentStepAssist(Rarity.COMMON, EnumEnchantmentType.ARMOR_FEET,
			new EntityEquipmentSlot[]
			{ EntityEquipmentSlot.FEET });

		ForgeRegistries.ENCHANTMENTS.register(ModRegistry.stepAssist);
	}

	/**
	 * Registers tool materials if certain materials are available.
	 */
	public static void RegisterToolMaterials()
	{
		// Copper
		ModRegistry.CustomMaterials.put("Copper",
			EnumHelper.addToolMaterial("Copper", 
				ToolMaterial.STONE.getHarvestLevel(), 
				ToolMaterial.STONE.getMaxUses(),
				ToolMaterial.STONE.getEfficiency(), 
				ToolMaterial.STONE.getAttackDamage(),
				ToolMaterial.STONE.getEnchantability()));

		// Osmium
		ModRegistry.CustomMaterials.put("Osmium",
			EnumHelper.addToolMaterial("Osmium", ToolMaterial.IRON.getHarvestLevel(), 
				500,
				ToolMaterial.IRON.getEfficiency(), 
				ToolMaterial.IRON.getAttackDamage() + .5f,
				ToolMaterial.IRON.getEnchantability()));

		// Bronze
		ModRegistry.CustomMaterials.put("Bronze",
			EnumHelper.addToolMaterial("Bronze", 
				ToolMaterial.IRON.getHarvestLevel(), 
				ToolMaterial.IRON.getMaxUses(),
				ToolMaterial.IRON.getEfficiency(), 
				ToolMaterial.IRON.getAttackDamage(),
				ToolMaterial.IRON.getEnchantability()));
		
		// Steel
		ModRegistry.CustomMaterials.put("Steel",
			EnumHelper.addToolMaterial("Steel", 
				ToolMaterial.DIAMOND.getHarvestLevel(), 
				(int)(ToolMaterial.IRON.getMaxUses() * 1.5),
				ToolMaterial.DIAMOND.getEfficiency(), 
				ToolMaterial.DIAMOND.getAttackDamage(),
				ToolMaterial.DIAMOND.getEnchantability()));
		
		// Obsidian
		ModRegistry.CustomMaterials.put("Obsidian",
			EnumHelper.addToolMaterial("Obsidian", 
				ToolMaterial.DIAMOND.getHarvestLevel() + 1, 
				(int)(ToolMaterial.DIAMOND.getMaxUses() * 1.5),
				ToolMaterial.DIAMOND.getEfficiency(), 
				ToolMaterial.DIAMOND.getAttackDamage() + 1,
				ToolMaterial.DIAMOND.getEnchantability()));
		
		ModRegistry.CustomMaterials.get("Obsidian")
			.setRepairItem(new ItemStack(Item.getItemFromBlock(Blocks.OBSIDIAN)));
	}

	/**
	 * This is called during the Ore Dictionary add event. This will make sure that we mark certain material types as
	 * found to enable/disable items later.
	 * 
	 * @param oreName The ore dictionary name.
	 * @param stack The item stack being registered.
	 */
	public static void RegisterRepairableMaterials(String oreName, ItemStack stack)
	{
		if (oreName.equals("ingotCopper")
			&& ModRegistry.CustomMaterials.get("Copper").getRepairItemStack() == ItemStack.EMPTY)
		{
			if (OreDictionary.doesOreNameExist("ingotCopper"))
			{
				ModRegistry.CustomMaterials.get("Copper")
					.setRepairItem(new ItemStack(stack.getItem(), 1, OreDictionary.WILDCARD_VALUE));

				ModRegistry.FoundMaterials.put("ingotCopper", true);
			}
			else
			{
				ModRegistry.FoundMaterials.put("ingotCopper", false);
			}
		}

		if (oreName.equals("ingotOsmium")
			&& ModRegistry.CustomMaterials.get("Osmium").getRepairItemStack() == ItemStack.EMPTY)
		{
			if (OreDictionary.doesOreNameExist("ingotOsmium"))
			{
				ModRegistry.CustomMaterials.get("Osmium")
					.setRepairItem(new ItemStack(stack.getItem(), 1, OreDictionary.WILDCARD_VALUE));

				ModRegistry.FoundMaterials.put("ingotOsmium", true);
			}
			else
			{
				ModRegistry.FoundMaterials.put("ingotOsmium", false);
			}
		}

		if (oreName.equals("ingotBronze")
			&& ModRegistry.CustomMaterials.get("Bronze").getRepairItemStack() == ItemStack.EMPTY)
		{
			if (OreDictionary.doesOreNameExist("ingotBronze"))
			{
				ModRegistry.CustomMaterials.get("Bronze")
					.setRepairItem(new ItemStack(stack.getItem(), 1, OreDictionary.WILDCARD_VALUE));

				ModRegistry.FoundMaterials.put("ingotBronze", true);
			}
			else
			{
				ModRegistry.FoundMaterials.put("ingotBronze", false);

			}
		}
		
		if (oreName.equals("ingotSteel")
			&& ModRegistry.CustomMaterials.get("Steel").getRepairItemStack() == ItemStack.EMPTY)
		{
			if (OreDictionary.doesOreNameExist("ingotSteel"))
			{
				ModRegistry.CustomMaterials.get("Steel")
					.setRepairItem(new ItemStack(stack.getItem(), 1, OreDictionary.WILDCARD_VALUE));

				ModRegistry.FoundMaterials.put("ingotSteel", true);
			}
			else
			{
				ModRegistry.FoundMaterials.put("ingotSteel", false);

			}
		}
	}

	/**
	 * Removes invalid tool registrations if certain ore dictionary records were not found.
	 */
	public static void RemoveInvalidEntries()
	{
		boolean recipesUpdated = false;

		if (ModRegistry.FoundMaterials.containsKey("ingotCopper") && !ModRegistry.FoundMaterials.get("ingotCopper"))
		{
			// Remove Items
			IForgeRegistryModifiable<Item> items = (IForgeRegistryModifiable<Item>) ForgeRegistries.ITEMS;
			items.remove(ModRegistry.CustomMaterialBlade("Copper").getRegistryName());
			ModRegistry.ModItems.remove(ModRegistry.CustomMaterialBlade("Copper"));

			// Remove Recipes
			IForgeRegistryModifiable<IRecipe> recipes = (IForgeRegistryModifiable<IRecipe>) ForgeRegistries.RECIPES;

			if (recipes.containsKey(new ResourceLocation("repurpose:swift_blade_copper")))
			{
				recipes.remove(new ResourceLocation("repurpose:swift_blade_copper"));
				recipesUpdated = true;
			}
		}

		if (ModRegistry.FoundMaterials.containsKey("ingotOsmium") && !ModRegistry.FoundMaterials.get("ingotOsmium"))
		{
			// Remove Items
			IForgeRegistryModifiable<Item> items = (IForgeRegistryModifiable<Item>) ForgeRegistries.ITEMS;
			items.remove(ModRegistry.CustomMaterialBlade("Osmium").getRegistryName());
			ModRegistry.ModItems.remove(ModRegistry.CustomMaterialBlade("Osmium"));

			// Remove Recipes
			IForgeRegistryModifiable<IRecipe> recipes = (IForgeRegistryModifiable<IRecipe>) ForgeRegistries.RECIPES;

			if (recipes.containsKey(new ResourceLocation("repurpose:swift_blade_osmium")))
			{
				recipes.remove(new ResourceLocation("repurpose:swift_blade_osmium"));
			}

			recipesUpdated = true;
		}

		if (ModRegistry.FoundMaterials.containsKey("ingotBronze") && !ModRegistry.FoundMaterials.get("ingotBronze"))
		{
			// Remove Items
			IForgeRegistryModifiable<Item> items = (IForgeRegistryModifiable<Item>) ForgeRegistries.ITEMS;
			items.remove(ModRegistry.CustomMaterialBlade("Bronze").getRegistryName());
			ModRegistry.ModItems.remove(ModRegistry.CustomMaterialBlade("Bronze"));

			// Remove Recipes
			IForgeRegistryModifiable<IRecipe> recipes = (IForgeRegistryModifiable<IRecipe>) ForgeRegistries.RECIPES;

			if (recipes.containsKey(new ResourceLocation("repurpose:swift_blade_bronze")))
			{
				recipes.remove(new ResourceLocation("repurpose:swift_blade_bronze"));
			}

			recipesUpdated = true;
		}
		
		if (ModRegistry.FoundMaterials.containsKey("ingotSteel") && !ModRegistry.FoundMaterials.get("ingotSteel"))
		{
			// Remove Items
			IForgeRegistryModifiable<Item> items = (IForgeRegistryModifiable<Item>) ForgeRegistries.ITEMS;
			items.remove(ModRegistry.CustomMaterialBlade("Steel").getRegistryName());
			ModRegistry.ModItems.remove(ModRegistry.CustomMaterialBlade("Steel"));

			// Remove Recipes
			IForgeRegistryModifiable<IRecipe> recipes = (IForgeRegistryModifiable<IRecipe>) ForgeRegistries.RECIPES;

			if (recipes.containsKey(new ResourceLocation("repurpose:swift_blade_steel")))
			{
				recipes.remove(new ResourceLocation("repurpose:swift_blade_steel"));
			}

			recipesUpdated = true;
		}

		if (recipesUpdated)
		{
			RecipeBookClient.rebuildTable();
		}
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
		Repurpose.network.registerMessage(CurrentSlotUpdateHandler.class, CurrentSlotUpdateMessage.class, 6,
			Side.SERVER);
		Repurpose.network.registerMessage(BagOfHoldingUpdateMessageHandler.class, BagOfHoldingUpdateMessage.class, 7,
			Side.CLIENT);
	}

	/**
	 * This is where mod capabilities are registered.
	 */
	public static void RegisterCapabilities()
	{
		// Register the dimension home capability.
		CapabilityManager.INSTANCE.register(IDimensionHome.class, new DimensionHomeStorage(), DimensionHome.class);
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
		// ForgeRegistries.ITEMS.register(item);
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
	 * Set the registry name of {@code item} to {@code itemName} and the un-localised name to the full registry name.
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
	 * Set the registry name of {@code block} to {@code blockName} and the un-localised name to the full registry name.
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
	 * 
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
	 * 
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

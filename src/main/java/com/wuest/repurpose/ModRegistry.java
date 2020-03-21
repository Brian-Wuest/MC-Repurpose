package com.wuest.repurpose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import com.wuest.repurpose.Blocks.BlockCharcoal;
import com.wuest.repurpose.Blocks.BlockCustomWall;
import com.wuest.repurpose.Blocks.BlockDirtSlab;
import com.wuest.repurpose.Blocks.BlockDirtStairs;
import com.wuest.repurpose.Blocks.BlockEnrichedFarmland;
import com.wuest.repurpose.Blocks.BlockGlowstoneSlab;
import com.wuest.repurpose.Blocks.BlockGrassSlab;
import com.wuest.repurpose.Blocks.BlockGrassStairs;
import com.wuest.repurpose.Blocks.BlockMiniRedstone;
import com.wuest.repurpose.Blocks.BlockRedstoneScanner;
import com.wuest.repurpose.Blocks.RedstoneClock;
import com.wuest.repurpose.Capabilities.DimensionHome;
import com.wuest.repurpose.Capabilities.IDimensionHome;
import com.wuest.repurpose.Capabilities.Storage.DimensionHomeStorage;
import com.wuest.repurpose.Crafting.ExtendedCookingRecipeSerializer;
import com.wuest.repurpose.Enchantment.EnchantmentStepAssist;
import com.wuest.repurpose.Items.ItemBagOfHolding;
import com.wuest.repurpose.Items.ItemBedCompass;
import com.wuest.repurpose.Items.ItemBlockBurnable;
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
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRegistry {
	/**
	 * This capability is used to save the locations where a player spawns when
	 * transferring dimensions.
	 */
	@CapabilityInject(IDimensionHome.class)
	public static Capability<IDimensionHome> DimensionHomes = null;

	public static ArrayList<Item> ModItems = new ArrayList<Item>();
	public static ArrayList<Block> ModBlocks = new ArrayList<Block>();
	public static Map<String, Boolean> FoundMaterials = new HashMap<String, Boolean>();
	public static ExtendedCookingRecipeSerializer ExtendedSmelting = null;

	public static EnchantmentStepAssist stepAssist;

	public static BlockCustomWall DirtWall() {
		for (Block entry : ModRegistry.ModBlocks) {
			if (entry instanceof BlockCustomWall
					&& ((BlockCustomWall) entry).BlockVariant == BlockCustomWall.EnumType.DIRT) {
				return (BlockCustomWall) entry;
			}
		}

		return null;
	}

	public static BlockCustomWall GrassWall() {
		for (Block entry : ModRegistry.ModBlocks) {
			if (entry instanceof BlockCustomWall
					&& ((BlockCustomWall) entry).BlockVariant == BlockCustomWall.EnumType.GRASS) {
				return (BlockCustomWall) entry;
			}
		}

		return null;
	}

	public static ItemBedCompass BedCompass() {
		return ModRegistry.GetItem(ItemBedCompass.class);
	}

	public static RedstoneClock RedStoneClock() {
		return ModRegistry.GetBlock(RedstoneClock.class);
	}

	public static BlockDirtStairs DirtStairs() {
		return ModRegistry.GetBlock(BlockDirtStairs.class);
	}

	public static BlockGrassStairs GrassStairs() {
		return ModRegistry.GetBlock(BlockGrassStairs.class);
	}

	public static BlockDirtSlab DirtSlab() {
		return ModRegistry.GetBlock(BlockDirtSlab.class);
	}

	public static BlockGrassSlab GrassSlab() {
		return ModRegistry.GetBlock(BlockGrassSlab.class);
	}

	public static BlockEnrichedFarmland EnrichedFarmland() {
		return ModRegistry.GetBlock(BlockEnrichedFarmland.class);
	}

	public static BlockMiniRedstone MiniRedstone() {
		return ModRegistry.GetBlock(BlockMiniRedstone.class);
	}

	public static BlockRedstoneScanner RedstoneScanner() {
		return ModRegistry.GetBlock(BlockRedstoneScanner.class);
	}

	public static ItemDiamondShard DiamondShard() {
		return ModRegistry.GetItem(ItemDiamondShard.class);
	}

	public static ItemFluffyFabric FluffyFabric() {
		return ModRegistry.GetItem(ItemFluffyFabric.class);
	}

	public static ItemSnorkel Snorkel() {
		return ModRegistry.GetItem(ItemSnorkel.class);
	}

	public static ItemWhetStone WhetStone() {
		return ModRegistry.GetItem(ItemWhetStone.class);
	}

	public static ItemStoneShears StoneShears() {
		return ModRegistry.GetItem(ItemStoneShears.class);
	}

	public static ItemScroll Scroll() {
		return ModRegistry.GetItem(ItemScroll.class);
	}

	public static BlockGlowstoneSlab GlowstoneSlab() {
		return ModRegistry.GetBlock(BlockGlowstoneSlab.class);
	}

	public static EnchantmentStepAssist StepAssist() {
		return ModRegistry.stepAssist;
	}

	public static ItemWoodenCrate WoodenCrate() {
		return ModRegistry.GetItem(ItemWoodenCrate.class);
	}

	public static ItemBagOfHolding BagofHolding() {
		return ModRegistry.GetItem(ItemBagOfHolding.class);
	}

	public static ItemWoodenCrate EmptyWoodenCrate()
	{
		for (Item item : ModRegistry.ModItems)
		{
			if (item.getClass().isAssignableFrom(ItemWoodenCrate.class)
				&&  ((ItemWoodenCrate)item).crateType == ItemWoodenCrate.CrateType.Empty)
			{
				return (ItemWoodenCrate)item;
			}
		}

		return null;
	}

	public static ItemSwiftBlade CustomMaterialBlade(String materialName) {
		IItemTier material = CustomItemTier.getByName(materialName);

		if (material == null) {
			return null;
		}

		for (Item item : ModRegistry.ModItems) {
			if (item.getClass().isAssignableFrom(ItemSwiftBlade.class)
					&& ((ItemSwiftBlade) item).getTier() == material) {
				return ((ItemSwiftBlade) item);
			}
		}

		return null;
	}

	/**
	 * Gets the item from the ModItems collections.
	 * 
	 * @param genericClass The class of item to get from the collection.
	 * @return Null if the item could not be found otherwise the item found.
	 */
	public static <T extends Item> T GetItem(Class<T> genericClass) {
		for (Item entry : ModRegistry.ModItems) {
			if (entry.getClass().isAssignableFrom(genericClass)) {
				return (T) entry;
			}
		}

		return null;
	}

	public static <T extends Item> T GetItemSpecific(Class<T> genericClass) {
		for (Item entry : ModRegistry.ModItems) {
			if (entry.getClass() == genericClass) {
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
	public static <T extends Block> T GetBlock(Class<T> genericClass) {
		for (Block entry : ModRegistry.ModBlocks) {
			if (entry.getClass().isAssignableFrom(genericClass)) {
				return (T) entry;
			}
		}

		return null;
	}

	/**
	 * This is where all in-game mod components (Items, Blocks) will be registered.
	 */
	public static void RegisterModComponents() {
		ModRegistry.registerBlock(new BlockCustomWall(Blocks.DIRT, BlockCustomWall.EnumType.DIRT));
		ModRegistry.registerBlock(new BlockCustomWall(Blocks.GRASS, BlockCustomWall.EnumType.GRASS));

		ModRegistry.registerItem(new ItemBedCompass("item_bed_compass"));

		ModRegistry.registerBlock(new RedstoneClock("block_redstone_clock"));

		ForgeRegistries.TILE_ENTITIES.register(TileEntityRedstoneClock.TileType);

		ModRegistry.registerBlock(new BlockDirtStairs());
		ModRegistry.registerBlock(new BlockGrassStairs());

		// Dirt Slab.
		ModRegistry.registerBlock(new BlockDirtSlab(), "block_dirt_slab");

		// Grass Slab
		ModRegistry.registerBlock(new BlockGrassSlab(), "block_grass_slab");

		ModRegistry.registerBlock(new BlockEnrichedFarmland());
		ModRegistry.registerBlock(new BlockMiniRedstone());

		ModRegistry.registerBlock(new BlockRedstoneScanner());
		ForgeRegistries.TILE_ENTITIES.register(TileEntityRedstoneScanner.TileType);

		// Diamond Shard
		ModRegistry.registerItem(new ItemDiamondShard("item_diamond_shard"));

		// Fluffy Fabric
		ModRegistry.registerItem(new ItemFluffyFabric("item_fluffy_fabric"));

		// Snorkel
		ModRegistry.registerItem(new ItemSnorkel("item_snorkel"));

		// Whetstone
		ModRegistry.registerItem(new ItemWhetStone("item_whetstone"));

		// Glowstone Slabs.
		ModRegistry.registerBlock(new BlockGlowstoneSlab(), "block_glowstone_slab");

		// Stone shears.
		ModRegistry.registerItem(new ItemStoneShears("item_stone_shears"));

		// Sickles.
		ModRegistry.registerItem(new ItemSickle(ItemTier.WOOD, "item_wood_sickle"));
		ModRegistry.registerItem(new ItemSickle(ItemTier.STONE, "item_stone_sickle"));
		ModRegistry.registerItem(new ItemSickle(ItemTier.IRON, "item_iron_sickle"));
		ModRegistry.registerItem(new ItemSickle(ItemTier.DIAMOND, "item_diamond_sickle"));
		ModRegistry.registerItem(new ItemSickle(ItemTier.GOLD, "item_gold_sickle"));

		// Swift Blades.
		ModRegistry.registerItem(new ItemSwiftBlade(ItemTier.WOOD, 3, 10, "wood"));
		ModRegistry.registerItem(new ItemSwiftBlade(ItemTier.STONE, 3, 10, "stone"));
		ModRegistry.registerItem(new ItemSwiftBlade(ItemTier.IRON, 3, 10, "iron"));
		ModRegistry.registerItem(new ItemSwiftBlade(ItemTier.DIAMOND, 3, 10, "diamond"));
		ModRegistry.registerItem(new ItemSwiftBlade(ItemTier.GOLD, 3, 10, "gold"));

		Item item = new ItemSwiftBlade(CustomItemTier.COPPER, 3, 10, "copper");
		ModRegistry.registerItem(item);

		item = new ItemSwiftBlade(CustomItemTier.OSMIUM, 3, 10, "osmium");
		ModRegistry.registerItem(item);

		item = new ItemSwiftBlade(CustomItemTier.BRONZE, 3, 10, "bronze");
		ModRegistry.registerItem(item);

		item = new ItemSwiftBlade(CustomItemTier.STEEL, 3, 10, "steel");
		ModRegistry.registerItem(item);

		item = new ItemSwiftBlade(CustomItemTier.OBSIDIAN, 3, 10, "obsidian");
		ModRegistry.registerItem(item);

		// Iron lump.
		ModRegistry.registerItem(new ItemIronLump("item_iron_lump"));

		// Charcoal block.
		Block block = new BlockCharcoal("block_charcoal");
		BlockItem itemBlock = (new ItemBlockBurnable(block, ItemGroup.MISC)).setBurnTime(16000);
		ModRegistry.registerBlock(block, itemBlock);

		// Scroll
		ModRegistry.registerItem(new ItemScroll("item_scroll"));

		// Wooden Crate
		for (ItemWoodenCrate.CrateType crateType : ItemWoodenCrate.CrateType.values()) {
			String name = crateType.toString().toLowerCase();

			if (crateType == ItemWoodenCrate.CrateType.Empty) {
				name = "item_wooden_crate";
			}

			ModRegistry.registerItem(new ItemWoodenCrate(name, crateType));
		}

		// Bag of Holding
		ModRegistry.registerItem(new ItemBagOfHolding("item_bag_of_holding"));
	}

	/**
	 * This is where the mod messages are registered.
	 */
	public static void RegisterMessages() {
		AtomicInteger index = new AtomicInteger();
		Repurpose.network.messageBuilder(RedstoneClockMessage.class, index.getAndIncrement())
				.encoder(RedstoneClockMessage::encode).decoder(RedstoneClockMessage::decode)
				.consumer(RedstoneClockHandler::handle).add();

		Repurpose.network.messageBuilder(BedLocationMessage.class, index.getAndIncrement())
				.encoder(BedLocationMessage::encode).decoder(BedLocationMessage::decode)
				.consumer(BedLocationHandler::handle).add();

		Repurpose.network.messageBuilder(RedstoneScannerMessage.class, index.getAndIncrement())
				.encoder(RedstoneScannerMessage::encode).decoder(RedstoneScannerMessage::decode)
				.consumer(RedstoneScannerHandler::handle).add();

		Repurpose.network.messageBuilder(ConfigSyncMessage.class, index.getAndIncrement())
				.encoder(ConfigSyncMessage::encode).decoder(ConfigSyncMessage::decode)
				.consumer(ConfigSyncHandler::handle).add();

		Repurpose.network.messageBuilder(CurrentSlotUpdateMessage.class, index.getAndIncrement())
				.encoder(CurrentSlotUpdateMessage::encode).decoder(CurrentSlotUpdateMessage::decode)
				.consumer(CurrentSlotUpdateHandler::handle).add();

		Repurpose.network.messageBuilder(BagOfHoldingUpdateMessage.class, index.getAndIncrement())
				.encoder(BagOfHoldingUpdateMessage::encode).decoder(BagOfHoldingUpdateMessage::decode)
				.consumer(BagOfHoldingUpdateMessageHandler::handle).add();
	}

	/**
	 * This is where mod capabilities are registered.
	 */
	public static void RegisterCapabilities() {
		// Register the dimension home capability.
		CapabilityManager.INSTANCE.register(IDimensionHome.class, new DimensionHomeStorage(),
				() -> new DimensionHome());
	}

	/**
	 * Register an Item
	 *
	 * @param item The Item instance
	 * @param <T>  The Item type
	 * @return The Item instance
	 */
	public static <T extends Item> T registerItem(T item) {
		// ForgeRegistries.ITEMS.register(item);
		ModRegistry.ModItems.add(item);

		return item;
	}

	public static <T extends Block> T registerBlock(T block) {
		return ModRegistry.registerBlock(block, true);
	}

	public static <T extends Block> T registerBlock(T block, boolean includeItemBlock) {
		if (includeItemBlock) {
			ModRegistry.ModItems.add(new BlockItem(block, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS))
					.setRegistryName(block.getRegistryName()));
		}

		ModRegistry.ModBlocks.add(block);
		return block;
	}

	/**
	 * Registers a block in the game registry.
	 *
	 * @param <T>   The type of block to register.
	 * @param block The block to register.
	 * @return The block which was registered.
	 */
	private static <T extends Block> T registerBlock(T block, String name) {
		ModItems.add(
				new BlockItem(block, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(name));

		ModRegistry.ModBlocks.add(block);

		return block;
	}

	public static <T extends Block, I extends BlockItem> T registerBlock(T block, I itemBlock) {
		ModRegistry.ModBlocks.add(block);

		if (itemBlock != null) {
			ModRegistry.ModItems.add(itemBlock);
		}

		return block;
	}

	/**
	 * Set the registry name of {@code item} to {@code itemName} and the
	 * un-localised name to the full registry name.
	 *
	 * @param item     The item
	 * @param itemName The item's name
	 */
	public static void setItemName(Item item, String itemName) {
		if (itemName != null) {
			item.setRegistryName(itemName);
		}
	}

	/**
	 * Set the registry name of {@code block} to {@code blockName} and the
	 * un-localised name to the full registry name.
	 *
	 * @param block     The block
	 * @param blockName The block's name
	 */
	public static void setBlockName(Block block, String blockName) {
		block.setRegistryName(Repurpose.MODID, blockName);
	}

	public enum CustomItemTier implements IItemTier {
		COPPER("Copper", ItemTier.STONE.getHarvestLevel(), ItemTier.STONE.getMaxUses(), ItemTier.STONE.getEfficiency(),
				ItemTier.STONE.getAttackDamage(), ItemTier.STONE.getEnchantability(), () -> {
					return Ingredient
							.fromTag(ItemTags.getCollection().get(new ResourceLocation("forge", "ingots/copper")));
				}),
		OSMIUM("Osmium", ItemTier.IRON.getHarvestLevel(), 500, ItemTier.IRON.getEfficiency(),
				ItemTier.IRON.getAttackDamage() + .5f, ItemTier.IRON.getEnchantability(), () -> {
					return Ingredient
							.fromTag(ItemTags.getCollection().get(new ResourceLocation("forge", "ingots/osmium")));
				}),
		BRONZE("Bronze", ItemTier.IRON.getHarvestLevel(), ItemTier.IRON.getMaxUses(), ItemTier.IRON.getEfficiency(),
				ItemTier.IRON.getAttackDamage(), ItemTier.IRON.getEnchantability(), () -> {
					return Ingredient
							.fromTag(ItemTags.getCollection().get(new ResourceLocation("forge", "ingots/bronze")));
				}),
		STEEL("Steel", ItemTier.DIAMOND.getHarvestLevel(), (int) (ItemTier.IRON.getMaxUses() * 1.5),
				ItemTier.DIAMOND.getEfficiency(), ItemTier.DIAMOND.getAttackDamage(),
				ItemTier.DIAMOND.getEnchantability(), () -> {
					return Ingredient
							.fromTag(ItemTags.getCollection().get(new ResourceLocation("forge", "ingots/steel")));
				}),
		OBSIDIAN("Obsidian", ItemTier.DIAMOND.getHarvestLevel() + 1, (int) (ItemTier.DIAMOND.getMaxUses() * 1.5),
				ItemTier.DIAMOND.getEfficiency(), ItemTier.DIAMOND.getAttackDamage() + 2,
				ItemTier.DIAMOND.getEnchantability(), () -> {
					return Ingredient.fromItems(Item.getItemFromBlock(Blocks.OBSIDIAN));
				});

		private final String name;
		private final int harvestLevel;
		private final int maxUses;
		private final float efficiency;
		private final float attackDamage;
		private final int enchantability;
		private final LazyLoadBase<Ingredient> repairMaterial;

		private CustomItemTier(String name, int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn,
				int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
			this.name = name;
			this.harvestLevel = harvestLevelIn;
			this.maxUses = maxUsesIn;
			this.efficiency = efficiencyIn;
			this.attackDamage = attackDamageIn;
			this.enchantability = enchantabilityIn;
			this.repairMaterial = new LazyLoadBase<>(repairMaterialIn);
		}

		public String getName() {
			return this.name;
		}

		public int getMaxUses() {
			return this.maxUses;
		}

		public float getEfficiency() {
			return this.efficiency;
		}

		public float getAttackDamage() {
			return this.attackDamage;
		}

		public int getHarvestLevel() {
			return this.harvestLevel;
		}

		public int getEnchantability() {
			return this.enchantability;
		}

		public Ingredient getRepairMaterial() {
			return this.repairMaterial.getValue();
		}

		public static CustomItemTier getByName(String name) {
			for (CustomItemTier item : CustomItemTier.values()) {
				if (item.getName().equals(name)) {
					return item;
				}
			}

			return null;
		}
	}
}

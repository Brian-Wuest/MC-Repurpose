package com.wuest.repurpose;

import com.wuest.repurpose.Blocks.*;
import com.wuest.repurpose.Capabilities.DimensionHome;
import com.wuest.repurpose.Capabilities.IDimensionHome;
import com.wuest.repurpose.Capabilities.Storage.DimensionHomeStorage;
import com.wuest.repurpose.Crafting.ExtendedCookingRecipeSerializer;
import com.wuest.repurpose.Enchantment.EnchantmentStepAssist;
import com.wuest.repurpose.Items.Containers.BagOfHoldingContainer;
import com.wuest.repurpose.Items.*;
import com.wuest.repurpose.Proxy.Messages.*;
import com.wuest.repurpose.Proxy.Messages.Handlers.*;
import com.wuest.repurpose.Tiles.TileEntityRedstoneClock;
import com.wuest.repurpose.Tiles.TileEntityRedstoneScanner;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@SuppressWarnings("ConstantConditions")
public class ModRegistry {

	/**
	 * Deferred registry for items.
	 */
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Repurpose.MODID);

	/**
	 * Deferred registry for blocks.
	 */
	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Repurpose.MODID);

	/**
	 * Deferred registry for tile entities.
	 */
	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Repurpose.MODID);

	/**
	 * Deferred registry for enchantments.
	 */
	public static final DeferredRegister<Enchantment> ENCHANTMENTS = new DeferredRegister<>(ForgeRegistries.ENCHANTMENTS, Repurpose.MODID);

	/**
	 * Deferred registry for recipe serializers.
	 */
	public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = new DeferredRegister<>(ForgeRegistries.RECIPE_SERIALIZERS, Repurpose.MODID);

	/**
	 * Deferred registry for container types.
	 */
	public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = new DeferredRegister<>(ForgeRegistries.CONTAINERS, Repurpose.MODID);

	/* *********************************** Blocks *********************************** */
	public static final RegistryObject<BlockCustomWall> DirtWall = BLOCKS.register("block_dirt_wall", () -> new BlockCustomWall(Blocks.DIRT, BlockCustomWall.EnumType.DIRT));
	public static final RegistryObject<BlockCustomWall> GrassWall = BLOCKS.register("block_grass_wall", () -> new BlockCustomWall(Blocks.GRASS, BlockCustomWall.EnumType.GRASS));
	public static final RegistryObject<RedstoneClock> RedStoneClock = BLOCKS.register("block_redstone_clock", com.wuest.repurpose.Blocks.RedstoneClock::new);
	public static final RegistryObject<BlockDirtStairs> DirtStairs = BLOCKS.register("block_dirt_stairs", BlockDirtStairs::new);
	public static final RegistryObject<BlockGrassStairs> GrassStairs = BLOCKS.register("block_grass_stairs", BlockGrassStairs::new);
	public static final RegistryObject<BlockDirtSlab> DirtSlab = BLOCKS.register("block_dirt_slab", BlockDirtSlab::new);
	public static final RegistryObject<BlockGrassSlab> GrassSlab = BLOCKS.register("block_grass_slab", BlockGrassSlab::new);
	public static final RegistryObject<BlockEnrichedFarmland> EnrichedFarmland = BLOCKS.register("block_enriched_farmland", BlockEnrichedFarmland::new);
	public static final RegistryObject<BlockMiniRedstone> MiniRedstone = BLOCKS.register("block_mini_redstone", BlockMiniRedstone::new);
	public static final RegistryObject<BlockRedstoneScanner> RedstoneScanner = BLOCKS.register("block_redstone_scanner", BlockRedstoneScanner::new);
	public static final RegistryObject<BlockGlowstoneSlab> GlowstoneSlab = BLOCKS.register("block_glowstone_slab", BlockGlowstoneSlab::new);


	/* *********************************** Item Blocks *********************************** */
	public static final RegistryObject<BlockItem> DirtWallItem = ITEMS.register("block_dirt_wall", () -> new BlockItem(DirtWall.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<BlockItem> GrassWallItem = ITEMS.register("block_grass_wall", () -> new BlockItem(GrassWall.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<BlockItem> RedstoneClockItem = ITEMS.register("block_redstone_clock", () -> new BlockItem(RedStoneClock.get(), new Item.Properties().group(ItemGroup.REDSTONE)));
	public static final RegistryObject<BlockItem> DirtStairsItem = ITEMS.register("block_dirt_stairs", () -> new BlockItem(DirtStairs.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<BlockItem> GrassStairsItem = ITEMS.register("block_grass_stairs", () -> new BlockItem(GrassStairs.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<BlockItem> DirtSlabItem = ITEMS.register("block_dirt_slab", () -> new BlockItem(DirtSlab.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<BlockItem> GrassSlabItem = ITEMS.register("block_grass_slab", () -> new BlockItem(GrassSlab.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<BlockItem> EnrichedFarmlandItem = ITEMS.register("block_enriched_farmland", () -> new BlockItem(EnrichedFarmland.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
	public static final RegistryObject<BlockItem> MiniRedstoneItem = ITEMS.register("block_mini_redstone", () -> new BlockItem(MiniRedstone.get(), new Item.Properties().group(ItemGroup.REDSTONE)));
	public static final RegistryObject<BlockItem> RedstoneScannerItem = ITEMS.register("block_redstone_scanner", () -> new BlockItem(RedstoneScanner.get(), new Item.Properties().group(ItemGroup.REDSTONE)));
	public static final RegistryObject<BlockItem> GlowstoneSlabItem = ITEMS.register("block_glowstone_slab", () -> new BlockItem(GlowstoneSlab.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));

	/* *********************************** Items *********************************** */
	public static final RegistryObject<ItemBedCompass> BedCompass = ITEMS.register("item_bed_compass", ItemBedCompass::new);
	public static final RegistryObject<ItemDiamondShard> DiamondShard = ITEMS.register("item_diamond_shard", ItemDiamondShard::new);
	public static final RegistryObject<ItemFluffyFabric> FluffyFabric = ITEMS.register("item_fluffy_fabric", ItemFluffyFabric::new);
	public static final RegistryObject<ItemSnorkel> Snorkel = ITEMS.register("item_snorkel", ItemSnorkel::new);
	public static final RegistryObject<ItemWhetStone> WhetStone = ITEMS.register("item_whetstone", ItemWhetStone::new);
	public static final RegistryObject<ItemStoneShears> StoneShears = ITEMS.register("item_stone_shears", ItemStoneShears::new);

	public static final RegistryObject<ItemSickle> WoodSickle = ITEMS.register("item_wood_sickle", () -> new ItemSickle(ItemTier.WOOD));
	public static final RegistryObject<ItemSickle> StoneSickle = ITEMS.register("item_stone_sickle", () -> new ItemSickle(ItemTier.STONE));
	public static final RegistryObject<ItemSickle> IronSickle = ITEMS.register("item_iron_sickle", () -> new ItemSickle(ItemTier.IRON));
	public static final RegistryObject<ItemSickle> DiamondSickle = ITEMS.register("item_diamond_sickle", () -> new ItemSickle(ItemTier.DIAMOND));
	public static final RegistryObject<ItemSickle> GoldSickles = ITEMS.register("item_gold_sickle", () -> new ItemSickle(ItemTier.GOLD));

	public static final RegistryObject<ItemSwiftBlade> SwiftBladeWood = ITEMS.register("item_swift_blade_wood", () -> new ItemSwiftBlade(ItemTier.WOOD, 3, 10));
	public static final RegistryObject<ItemSwiftBlade> SwiftBladeStone = ITEMS.register("item_swift_blade_stone", () -> new ItemSwiftBlade(ItemTier.STONE, 3, 10));
	public static final RegistryObject<ItemSwiftBlade> SwiftBladeIron = ITEMS.register("item_swift_blade_iron", () -> new ItemSwiftBlade(ItemTier.IRON, 3, 10));
	public static final RegistryObject<ItemSwiftBlade> SwiftBladeDiamond = ITEMS.register("item_swift_blade_diamond", () -> new ItemSwiftBlade(ItemTier.DIAMOND, 3, 10));
	public static final RegistryObject<ItemSwiftBlade> SwiftBladeGold = ITEMS.register("item_swift_blade_gold", () -> new ItemSwiftBlade(ItemTier.GOLD, 3, 10));
	public static final RegistryObject<ItemSwiftBlade> SwiftBladeCopper = ITEMS.register("item_swift_blade_copper", () -> new ItemSwiftBlade(CustomItemTier.COPPER, 3, 10));
	public static final RegistryObject<ItemSwiftBlade> SwiftBladeOsmium = ITEMS.register("item_swift_blade_osmium", () -> new ItemSwiftBlade(CustomItemTier.OSMIUM, 3, 10));
	public static final RegistryObject<ItemSwiftBlade> SwiftBladeBronze = ITEMS.register("item_swift_blade_bronze", () -> new ItemSwiftBlade(CustomItemTier.BRONZE, 3, 10));
	public static final RegistryObject<ItemSwiftBlade> SwiftBladeSteel = ITEMS.register("item_swift_blade_steel", () -> new ItemSwiftBlade(CustomItemTier.STEEL, 3, 10));
	public static final RegistryObject<ItemSwiftBlade> SwiftBladeObsidian = ITEMS.register("item_swift_blade_obsidian", () -> new ItemSwiftBlade(CustomItemTier.OBSIDIAN, 3, 10));

	public static final RegistryObject<ItemIronLump> IronLump = ITEMS.register("item_iron_lump", ItemIronLump::new);
	public static final RegistryObject<ItemScroll> Scroll = ITEMS.register("item_scroll", ItemScroll::new);
	public static final RegistryObject<ItemBagOfHolding> BagOfHolding = ITEMS.register("item_bag_of_holding", ItemBagOfHolding::new);

	public static final RegistryObject<ItemWoodenCrate> WoodenCrate = ITEMS.register("item_wooden_crate", () -> new ItemWoodenCrate(ItemWoodenCrate.CrateType.Empty));
	public static final RegistryObject<ItemWoodenCrate> ClutchOfEggs = ITEMS.register("clutch_of_eggs", () -> new ItemWoodenCrate(ItemWoodenCrate.CrateType.Clutch_Of_Eggs));
	public static final RegistryObject<ItemWoodenCrate> CartonOfEggs = ITEMS.register("carton_of_eggs", () -> new ItemWoodenCrate(ItemWoodenCrate.CrateType.Carton_Of_Eggs));
	public static final RegistryObject<ItemWoodenCrate> BunchOfPotatoes = ITEMS.register("bunch_of_potatoes", () -> new ItemWoodenCrate(ItemWoodenCrate.CrateType.Bunch_Of_Potatoes));
	public static final RegistryObject<ItemWoodenCrate> CrateOfPotatoes = ITEMS.register("crate_of_potatoes", () -> new ItemWoodenCrate(ItemWoodenCrate.CrateType.Crate_Of_Potatoes));
	public static final RegistryObject<ItemWoodenCrate> BunchOfCarrots = ITEMS.register("bunch_of_carrots", () -> new ItemWoodenCrate(ItemWoodenCrate.CrateType.Bunch_Of_Carrots));
	public static final RegistryObject<ItemWoodenCrate> CrateOfCarrots = ITEMS.register("crate_of_carrots", () -> new ItemWoodenCrate(ItemWoodenCrate.CrateType.Crate_Of_Carrots));
	public static final RegistryObject<ItemWoodenCrate> BunchOfBeets = ITEMS.register("bunch_of_beets", () -> new ItemWoodenCrate(ItemWoodenCrate.CrateType.Bunch_Of_Beets));
	public static final RegistryObject<ItemWoodenCrate> CrateOfBeets = ITEMS.register("crate_of_beets", () -> new ItemWoodenCrate(ItemWoodenCrate.CrateType.Crate_Of_Beets));

	/* *********************************** Tile Entities *********************************** */
	public static final RegistryObject<TileEntityType<TileEntityRedstoneClock>> RedstoneClockTileEntity = TILE_ENTITIES.register("redstone_clock", () -> TileEntityRedstoneClock.TileType);
	public static final RegistryObject<TileEntityType<TileEntityRedstoneScanner>> RedstoneScannerTileEntity = TILE_ENTITIES.register("redstone_scanner", () -> TileEntityRedstoneScanner.TileType);

	/* *********************************** Enchantments *********************************** */
	public static final RegistryObject<EnchantmentStepAssist> StepAssist = ENCHANTMENTS.register("step_assist", () -> new EnchantmentStepAssist(Enchantment.Rarity.COMMON, EnchantmentType.ARMOR_FEET,
			new EquipmentSlotType[]{EquipmentSlotType.FEET}));

	/* *********************************** Recipe Serializers *********************************** */
	public static final RegistryObject<ExtendedCookingRecipeSerializer> ExtendedSmelting = RECIPE_SERIALIZERS.register("extended_smelting", () -> new ExtendedCookingRecipeSerializer(200));

	/* *********************************** Container Types *********************************** */
	public static final RegistryObject<ContainerType<BagOfHoldingContainer>> BagOfHoldingContainer = CONTAINER_TYPES.register("item_bag_of_holding", () -> IForgeContainerType.create(com.wuest.repurpose.Items.Containers.BagOfHoldingContainer::fromNetwork));

	/**
	 * This capability is used to save the locations where a player spawns when
	 * transferring dimensions.
	 */
	@CapabilityInject(IDimensionHome.class)
	public static Capability<IDimensionHome> DimensionHomes = null;

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
		private final LazyValue<Ingredient> repairMaterial;

		CustomItemTier(String name, int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn,
							   int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
			this.name = name;
			this.harvestLevel = harvestLevelIn;
			this.maxUses = maxUsesIn;
			this.efficiency = efficiencyIn;
			this.attackDamage = attackDamageIn;
			this.enchantability = enchantabilityIn;
			this.repairMaterial = new LazyValue<>(repairMaterialIn);
		}

		public static CustomItemTier getByName(String name) {
			for (CustomItemTier item : CustomItemTier.values()) {
				if (item.getName().equals(name)) {
					return item;
				}
			}

			return null;
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
	}
}

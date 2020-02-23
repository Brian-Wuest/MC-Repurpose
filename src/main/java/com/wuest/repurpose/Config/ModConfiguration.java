package com.wuest.repurpose.Config;

import static com.wuest.repurpose.Proxy.CommonProxy.proxyConfiguration;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map.Entry;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.wuest.repurpose.Repurpose;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ModConfiguration {
	public static String OPTIONS = "general.";
	public static String RecipeOptions = "general.recipes.";
	public static String GuiOptions = "general.gui.";
	public static String ChestContentOptions = "general.chest contents.";
	public static String ExtraDropOptions = "general.extra drops.";
	public static String tagKey = "WuestConfig";

	// Config file option names.
	public static String rightClickCropHarvestName = "Right Click Crop Harvest";
	public static String enableGrassSpreadToCustomDirtName = "Enable Grass Spreading To Custom Dirt";
	public static String enableVersionCheckMessageName = "Enable Version Checking";
	public static String enableStepAssistEnchantmentName = "Enable Step Assist Enchantment";
	public static String enableFlatBedrockGenerationName = "Enable Flat Bedrock Generation";
	public static String enableVerboseLogginName = "Enable Verbose Logging";
	public static String enableMobileLightName = "Enable Mobile Light";

	public static String diamondShardDropChanceName = "Diamond Shard Drop Chance";

	public static String beetRootSeedDropChanceName = "Beetroot Seed Drop Chance";
	public static String melonSeedDropChanceName = "Melon Seed Drop Chance";
	public static String pumpkinSeedDropChanceName = "Pumpkin Seed Drop Chance";
	public static String cocoaSeedDropChanceName = "Cocoa Seed Drop Chance";

	public static String beetRootDropChanceName = "Beetroot Drop Chance";
	public static String potatoDropChanceName = "Potato Drop Chance";
	public static String carrotDropChanceName = "Carrot Drop Chance Name";
	public static String boneDropChanceName = "Bone Drop Chance";
	public static String clayBallDropChanceName = "Clay Item Drop Chance";

	public static String coalDropChanceName = "Coal Drop Chance";
	public static String flintDropChanceName = "Flint Drop Chance";
	public static String ironNuggetDropChanceName = "Iron Nugget Drop Chance";
	public static String goldNuggetDropChanceName = "Gold Nugget Drop Chance";

	public static String appleDropChanceName = "Apple Drop Chance";
	public static String stickDropChanceName = "Stick Drop Chance";
	public static String monsterHeadDropChanceName = "Monster Head Drop Chance";

	public static String addMetalRecipesName = "Metal Recipes";
	public static String addWoodRecipesName = "Wood Recipes";
	public static String addStoneRecipesName = "Stone Recipes";
	public static String addArmorRecipesName = "Armor Recipes";
	public static String addMiscRecipesName = "Misc Recipes";
	public static String addNetherStarRecipeName = "Nether Star";
	public static String enableHomeCommandName = "Enable Home Command";
	public static String enableRedstoneClockName = "Redstone Clock";
	public static String enableBedCompassName = "Bed Compass";
	public static String enableEnchrichedFarmlandName = "Enriched Farmland";
	public static String enableMiniRedstoneBlockName = "Mini Redstone";
	public static String addDirtRecipesName = "Dirt Recipes";
	public static String addSnorkelName = "Snorkel";
	public static String addWhetstoneName = "Whetstone";
	public static String addGlowstoneSlabName = "Glowstone Slab";
	public static String addFluffyFabricName = "Fluffy Fabric";
	public static String addStoneShearsName = "Stone Shears";
	public static String addDiamonShardName = "Diamond Shard";
	public static String addRedstoneScannerName = "Redstone Scanner";
	public static String sickleRecipeName = "Sickle";
	public static String swiftBladeName = "Swift Blade";
	public static String ironLumpName = "Iron Lump";
	public static String charcoalBlockName = "Charcoal Block";
	public static String saddleName = "Saddle";
	public static String stringName = "String";
	public static String scrollName = "Scroll";
	public static String woodenCrateName = "Wooden Crate";
	public static String Clutch_Of_EggsName = "Clutch of Eggs";
	public static String Carton_Of_EggsName = "Carton of Eggs";
	public static String Bunch_Of_PotatoesName = "Bunch of Potatoes";
	public static String Crate_Of_PotatoesName = "Crate of Potatoes";
	public static String Bunch_Of_CarrotsName = "Bunch of Carrots";
	public static String Crate_Of_CarrotsName = "Crate of Carrots";
	public static String Bunch_Of_BeetsName = "Bunch of Beets";
	public static String Crate_Of_BeetsName = "Crate of Beets";
	public static String Bag_Of_Holding_Name = "Bag of Holding";

	// Recipe options.
	public static String[] recipeKeys = new String[] { addMetalRecipesName, addArmorRecipesName, addMiscRecipesName,
			addNetherStarRecipeName, addStoneRecipesName, addWoodRecipesName, enableBedCompassName,
			enableEnchrichedFarmlandName, enableMiniRedstoneBlockName, enableRedstoneClockName, addDirtRecipesName,
			addSnorkelName, addWhetstoneName, addGlowstoneSlabName, addFluffyFabricName, addStoneShearsName,
			addDiamonShardName, addRedstoneScannerName, sickleRecipeName, swiftBladeName, ironLumpName,
			charcoalBlockName, saddleName, stringName, scrollName, woodenCrateName, Clutch_Of_EggsName,
			Carton_Of_EggsName, Bunch_Of_BeetsName, Bunch_Of_CarrotsName, Bunch_Of_PotatoesName, Crate_Of_BeetsName,
			Crate_Of_CarrotsName, Crate_Of_PotatoesName, Bag_Of_Holding_Name };

	private ConfigFileSettings configFileSettings;

	// Configuration Options.
	public boolean rightClickCropHarvest;
	public boolean enableHomeCommand;
	public boolean enableGrassSpreadToCustomDirt;
	public boolean enableStepAssistEnchantment;
	public boolean enableSwiftCombat;
	public boolean enableFlatBedrockGeneration;
	public boolean enableVerboseLogging;
	public boolean enableMobileLight;

	public HashMap<String, Boolean> recipeConfiguration;

	// Extra Drop Options.
	public int diamondShardDropChance;

	public int beetRootSeedDropChance;
	public int melonSeedDropChance;
	public int pumpkinSeedDropChance;
	public int cocoaSeedDropChance;

	public int potatoDropChance;
	public int beetRootDropChance;
	public int carrotDropChance;
	public int boneDropChance;
	public int clayBallDropChance;

	public int coalDropChance;
	public int flintDropChance;
	public int ironNuggetDropChance;
	public int goldNuggetDropChance;
	public int appleDropChance;
	public int stickDropChance;
	public int monsterHeadDropChance;

	private ModConfiguration() {
		this.rightClickCropHarvest = false;
		this.enableHomeCommand = true;
		this.enableGrassSpreadToCustomDirt = true;
		this.enableSwiftCombat = true;
		this.recipeConfiguration = new HashMap<String, Boolean>();
		this.enableFlatBedrockGeneration = true;
		this.enableMobileLight = true;
	}

	public ModConfiguration(ForgeConfigSpec.Builder builder) {
		this();

		this.configFileSettings = new ConfigFileSettings();
		ModConfiguration.buildOptions(this, builder);
	}

	public static void buildOptions(ModConfiguration config, ForgeConfigSpec.Builder builder) {
		proxyConfiguration = config;

		builder.comment("General");

		// General settings.
		proxyConfiguration.configFileSettings.rightClickCropHarvest = builder
				.comment("Determines if right-clicking crops will harvest them. Server configuration overrides client.")
				.define(ModConfiguration.OPTIONS + ModConfiguration.rightClickCropHarvestName, false);

		proxyConfiguration.configFileSettings.enableGrassSpreadToCustomDirt = builder.comment(
				"Determines if grass will spread to the custom dirt blocks added by this mod. Sever configuration overrides client.")
				.define(ModConfiguration.OPTIONS + ModConfiguration.enableGrassSpreadToCustomDirtName, true);

		proxyConfiguration.configFileSettings.enableFlatBedrockGeneration = builder
				.comment("Determines if overworld bedrock is flat. Server configuration overrides client.")
				.define(ModConfiguration.OPTIONS + ModConfiguration.enableFlatBedrockGenerationName, true);

		proxyConfiguration.configFileSettings.enableVerboseLogging = builder.comment(
				"Determines if more events are printed to the minecraft log. Only use this when submitting a log for an issue. Only a few areas will have this functionality enabled.")
				.define(ModConfiguration.OPTIONS + ModConfiguration.enableVerboseLogginName, false);

		proxyConfiguration.configFileSettings.enableMobileLight = builder.comment(
				"Determines if light source blocks will illuminate the player's area when held in either hand. This does not affect monster generation. Server overrides client.")
				.define(ModConfiguration.OPTIONS + ModConfiguration.enableMobileLightName, false);

		proxyConfiguration.configFileSettings.enableStepAssistEnchantment = builder.comment(
				"Determines if the Step Assist family of enchantments is enabled. \r\nRequires World Restart. Server Configuration overrides client.")
				.define(ModConfiguration.OPTIONS + ModConfiguration.enableStepAssistEnchantmentName, true);

		proxyConfiguration.configFileSettings.enableHomeCommand = builder.comment(
				"Determines if home command is enabled. This command will allow the player to teleport to the last bed they slept in or where they entered the dimension from. Server configuration overrides client.")
				.define(ModConfiguration.OPTIONS + ModConfiguration.enableHomeCommandName, true);

		builder.comment("Extra Drop Options");

		// Extra Drop Options.
		proxyConfiguration.configFileSettings.diamondShardDropChance = builder.comment(
				"Defines the drop chance of diamond shards from coal ore blocks. This is percent based so 1 = 1%. Server configuration overrides client.")
				.defineInRange(ModConfiguration.ExtraDropOptions + ModConfiguration.diamondShardDropChanceName, 1, 0,
						100);

		proxyConfiguration.configFileSettings.beetRootSeedDropChance = builder.comment(
				"Defines the drop chance of beet root seeds from tall grass blocks. This is percent based so 1 = 1%. Server configuration overrides client.")
				.defineInRange(ModConfiguration.ExtraDropOptions + ModConfiguration.beetRootSeedDropChanceName, 4, 0,
						100);

		proxyConfiguration.configFileSettings.melonSeedDropChance = builder.comment(
				"Defines the drop chance of melon seeds from tall grass blocks. This is percent based so 1 = 1%. Server configuration overrides client.")
				.defineInRange(ModConfiguration.ExtraDropOptions + ModConfiguration.melonSeedDropChanceName, 4, 0, 100);

		proxyConfiguration.configFileSettings.pumpkinSeedDropChance = builder.comment(
				"Defines the drop chance of pumpkin seeds from tall grass blocks. This is percent based so 1 = 1%. Server configuration overrides client.")
				.defineInRange(ModConfiguration.ExtraDropOptions + ModConfiguration.pumpkinSeedDropChanceName, 4, 0,
						100);

		proxyConfiguration.configFileSettings.cocoaSeedDropChance = builder.comment(
				"Defines the drop chance of cocoa seeds from tall grass blocks. This is percent based so 1 = 1%. Server configuration overrides client.")
				.defineInRange(ModConfiguration.ExtraDropOptions + ModConfiguration.cocoaSeedDropChanceName, 4, 0,
						100);

		proxyConfiguration.configFileSettings.potatoDropChance = builder.comment(
				"Defines the drop chance of potatoes from dirt/grass blocks. This is percent based so 1 = 1%. Server configuration overrides client.")
				.defineInRange(ModConfiguration.ExtraDropOptions + ModConfiguration.potatoDropChanceName, 4, 0, 100);

		proxyConfiguration.configFileSettings.carrotDropChance = builder.comment(
				"Defines the drop chance of carrots from dirt/grass blocks. This is percent based so 1 = 1%. Server configuration overrides client.")
				.defineInRange(ModConfiguration.ExtraDropOptions + ModConfiguration.carrotDropChanceName, 4, 0, 100);

		proxyConfiguration.configFileSettings.beetRootDropChance = builder.comment(
				"Defines the drop chance of beet roots from dirt/grass blocks. This is percent based so 1 = 1%. Server configuration overrides client.")
				.defineInRange(ModConfiguration.ExtraDropOptions + ModConfiguration.beetRootDropChanceName, 4, 0, 100);

		proxyConfiguration.configFileSettings.clayBallDropChance = builder.comment(
				"Defines the drop chance of clay items from dirt/grass blocks. This is percent based so 1 = 1%. Server configuration overrides client.")
				.defineInRange(ModConfiguration.ExtraDropOptions + ModConfiguration.clayBallDropChanceName, 4, 0, 100);

		proxyConfiguration.configFileSettings.boneDropChance = builder.comment(
				"Defines the drop chance of bones from dirt/grass blocks. This is percent based so 1 = 1%. Server configuration overrides client.")
				.defineInRange(ModConfiguration.ExtraDropOptions + ModConfiguration.boneDropChanceName, 4, 0, 100);

		proxyConfiguration.configFileSettings.coalDropChance = builder.comment(
				"Defines the drop chance of coal from stone (all vanilla varieties) blocks. This is percent based so 1 = 1%. Server configuration overrides client.")
				.defineInRange(ModConfiguration.ExtraDropOptions + ModConfiguration.coalDropChanceName, 4, 0, 100);

		proxyConfiguration.configFileSettings.flintDropChance = builder.comment(
				"Defines the drop chance of flint from stone (all vanilla varieties) blocks. This is percent based so 1 = 1%. Server configuration overrides client.")
				.defineInRange(ModConfiguration.ExtraDropOptions + ModConfiguration.flintDropChanceName, 4, 0, 100);

		proxyConfiguration.configFileSettings.ironNuggetDropChance = builder.comment(
				"Defines the drop chance of iron nuggets from stone (all vanilla varieties) blocks. This is percent based so 1 = 1%. Server configuration overrides client.")
				.defineInRange(ModConfiguration.ExtraDropOptions + ModConfiguration.ironNuggetDropChanceName, 4, 0,
						100);

		proxyConfiguration.configFileSettings.goldNuggetDropChance = builder.comment(
				"Defines the drop chance of gold nuggets from stone (all vanilla varieties) blocks. This is percent based so 1 = 1%. Server configuration overrides client.")
				.defineInRange(ModConfiguration.ExtraDropOptions + ModConfiguration.goldNuggetDropChanceName, 2, 0,
						100);

		proxyConfiguration.configFileSettings.appleDropChance = builder.comment(
				"Defines the drop chance of apples from leaves (all varieties) blocks. This is percent based so 1 = 1%. Server configuration overrides client.")
				.defineInRange(ModConfiguration.ExtraDropOptions + ModConfiguration.appleDropChanceName, 4, 0, 100);

		proxyConfiguration.configFileSettings.stickDropChance = builder.comment(
				"Defines the drop chance of sticks from leaves (all varieties) blocks. This is percent based so 1 = 1%. Server configuration overrides client.")
				.defineInRange(ModConfiguration.ExtraDropOptions + ModConfiguration.stickDropChanceName, 6, 0, 100);

		proxyConfiguration.configFileSettings.monsterHeadDropChance = builder.comment(
				"Defines the drop chance of monster heads when zombies or skeletons die. This is percent based so 1 = 1%. Server configuration overrides client.")
				.defineInRange(ModConfiguration.ExtraDropOptions + ModConfiguration.monsterHeadDropChanceName, 4, 0,
						100);

		builder.comment("Recipe Options");

		for (String key : ModConfiguration.recipeKeys) {
			BooleanValue value = builder
					.comment("Determines if the recipe(s) associated with the " + key
							+ " are enabled. Server configuration overrides client.")
					.define(ModConfiguration.RecipeOptions + key, true);

			proxyConfiguration.configFileSettings.recipeConfiguration.put(key, value);
		}
	}

	public static void loadConfig(ForgeConfigSpec spec, Path path) {
		Repurpose.LOGGER.debug("Loading config file {}", path);

		final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave()
				.writingMode(WritingMode.REPLACE).build();

		Repurpose.LOGGER.debug("Built TOML config for {}", path.toString());
		configData.load();
		Repurpose.LOGGER.debug("Loaded TOML config file {}", path.toString());
		spec.setConfig(configData);

		ModConfiguration.UpdateServerConfig();
	}

	private static void UpdateServerConfig() {
		proxyConfiguration.rightClickCropHarvest = proxyConfiguration.configFileSettings.rightClickCropHarvest.get();
		proxyConfiguration.enableHomeCommand = proxyConfiguration.configFileSettings.enableHomeCommand.get();
		proxyConfiguration.enableGrassSpreadToCustomDirt = proxyConfiguration.configFileSettings.enableGrassSpreadToCustomDirt
				.get();
		proxyConfiguration.enableStepAssistEnchantment = proxyConfiguration.configFileSettings.enableStepAssistEnchantment
				.get();
		proxyConfiguration.enableSwiftCombat = proxyConfiguration.configFileSettings.enableSwiftCombat.get();
		proxyConfiguration.enableFlatBedrockGeneration = proxyConfiguration.configFileSettings.enableFlatBedrockGeneration
				.get();
		proxyConfiguration.enableVerboseLogging = proxyConfiguration.configFileSettings.enableVerboseLogging.get();
		proxyConfiguration.enableMobileLight = proxyConfiguration.configFileSettings.enableMobileLight.get();

		proxyConfiguration.diamondShardDropChance = proxyConfiguration.configFileSettings.diamondShardDropChance.get();

		proxyConfiguration.beetRootSeedDropChance = proxyConfiguration.configFileSettings.beetRootSeedDropChance.get();
		proxyConfiguration.melonSeedDropChance = proxyConfiguration.configFileSettings.melonSeedDropChance.get();
		proxyConfiguration.pumpkinSeedDropChance = proxyConfiguration.configFileSettings.pumpkinSeedDropChance.get();
		proxyConfiguration.cocoaSeedDropChance = proxyConfiguration.configFileSettings.cocoaSeedDropChance.get();

		proxyConfiguration.potatoDropChance = proxyConfiguration.configFileSettings.potatoDropChance.get();
		proxyConfiguration.beetRootDropChance = proxyConfiguration.configFileSettings.beetRootDropChance.get();
		proxyConfiguration.carrotDropChance = proxyConfiguration.configFileSettings.carrotDropChance.get();
		proxyConfiguration.boneDropChance = proxyConfiguration.configFileSettings.boneDropChance.get();

		proxyConfiguration.clayBallDropChance = proxyConfiguration.configFileSettings.clayBallDropChance.get();
		proxyConfiguration.coalDropChance = proxyConfiguration.configFileSettings.coalDropChance.get();
		proxyConfiguration.flintDropChance = proxyConfiguration.configFileSettings.flintDropChance.get();
		proxyConfiguration.ironNuggetDropChance = proxyConfiguration.configFileSettings.ironNuggetDropChance.get();
		proxyConfiguration.goldNuggetDropChance = proxyConfiguration.configFileSettings.goldNuggetDropChance.get();
		proxyConfiguration.appleDropChance = proxyConfiguration.configFileSettings.appleDropChance.get();
		proxyConfiguration.stickDropChance = proxyConfiguration.configFileSettings.stickDropChance.get();
		proxyConfiguration.monsterHeadDropChance = proxyConfiguration.configFileSettings.monsterHeadDropChance.get();
	}

	public CompoundNBT ToNBTTagCompound() {
		CompoundNBT tag = new CompoundNBT();

		tag.putBoolean(ModConfiguration.rightClickCropHarvestName, this.rightClickCropHarvest);
		tag.putBoolean(ModConfiguration.enableHomeCommandName, this.enableHomeCommand);
		tag.putBoolean(ModConfiguration.enableGrassSpreadToCustomDirtName, this.enableGrassSpreadToCustomDirt);
		tag.putBoolean(ModConfiguration.enableStepAssistEnchantmentName, this.enableStepAssistEnchantment);
		tag.putBoolean(ModConfiguration.enableFlatBedrockGenerationName, this.enableFlatBedrockGeneration);
		tag.putBoolean(ModConfiguration.enableVerboseLogginName, this.enableVerboseLogging);
		tag.putBoolean(ModConfiguration.enableMobileLightName, this.enableMobileLight);

		for (Entry<String, Boolean> entry : this.recipeConfiguration.entrySet()) {
			tag.putBoolean(entry.getKey(), entry.getValue());
		}

		tag.putInt(ModConfiguration.diamondShardDropChanceName, this.diamondShardDropChance);

		tag.putInt(ModConfiguration.beetRootSeedDropChanceName, this.beetRootSeedDropChance);
		tag.putInt(ModConfiguration.melonSeedDropChanceName, this.melonSeedDropChance);
		tag.putInt(ModConfiguration.pumpkinSeedDropChanceName, this.pumpkinSeedDropChance);
		tag.putInt(ModConfiguration.cocoaSeedDropChanceName, this.cocoaSeedDropChance);

		tag.putInt(ModConfiguration.potatoDropChanceName, this.potatoDropChance);
		tag.putInt(ModConfiguration.carrotDropChanceName, this.carrotDropChance);
		tag.putInt(ModConfiguration.beetRootDropChanceName, this.beetRootDropChance);
		tag.putInt(ModConfiguration.clayBallDropChanceName, this.clayBallDropChance);
		tag.putInt(ModConfiguration.boneDropChanceName, this.boneDropChance);

		tag.putInt(ModConfiguration.coalDropChanceName, this.coalDropChance);
		tag.putInt(ModConfiguration.flintDropChanceName, this.flintDropChance);
		tag.putInt(ModConfiguration.ironNuggetDropChanceName, this.ironNuggetDropChance);
		tag.putInt(ModConfiguration.goldNuggetDropChanceName, this.goldNuggetDropChance);
		tag.putInt(ModConfiguration.appleDropChanceName, this.appleDropChance);
		tag.putInt(ModConfiguration.stickDropChanceName, this.stickDropChance);

		return tag;
	}

	public static ModConfiguration getFromNBTTagCompound(CompoundNBT tag) {
		ModConfiguration config = new ModConfiguration();

		config.rightClickCropHarvest = tag.getBoolean(ModConfiguration.rightClickCropHarvestName);

		config.enableHomeCommand = tag.getBoolean(ModConfiguration.enableHomeCommandName);
		config.enableGrassSpreadToCustomDirt = tag.getBoolean(ModConfiguration.enableGrassSpreadToCustomDirtName);
		config.enableStepAssistEnchantment = tag.getBoolean(ModConfiguration.enableStepAssistEnchantmentName);

		config.enableFlatBedrockGeneration = tag.getBoolean(ModConfiguration.enableFlatBedrockGenerationName);
		config.enableVerboseLogging = tag.getBoolean(ModConfiguration.enableVerboseLogginName);
		config.enableMobileLight = tag.getBoolean(ModConfiguration.enableMobileLightName);

		config.diamondShardDropChance = tag.getInt(ModConfiguration.diamondShardDropChanceName);

		config.beetRootSeedDropChance = tag.getInt(ModConfiguration.beetRootSeedDropChanceName);
		config.melonSeedDropChance = tag.getInt(ModConfiguration.melonSeedDropChanceName);
		config.pumpkinSeedDropChance = tag.getInt(ModConfiguration.pumpkinSeedDropChanceName);
		config.cocoaSeedDropChance = tag.getInt(ModConfiguration.cocoaSeedDropChanceName);

		config.potatoDropChance = tag.getInt(ModConfiguration.potatoDropChanceName);
		config.carrotDropChance = tag.getInt(ModConfiguration.carrotDropChanceName);
		config.beetRootDropChance = tag.getInt(ModConfiguration.beetRootDropChanceName);
		config.clayBallDropChance = tag.getInt(ModConfiguration.clayBallDropChanceName);
		config.boneDropChance = tag.getInt(ModConfiguration.boneDropChanceName);

		config.coalDropChance = tag.getInt(ModConfiguration.coalDropChanceName);
		config.flintDropChance = tag.getInt(ModConfiguration.flintDropChanceName);
		config.ironNuggetDropChance = tag.getInt(ModConfiguration.ironNuggetDropChanceName);
		config.goldNuggetDropChance = tag.getInt(ModConfiguration.goldNuggetDropChanceName);
		config.appleDropChance = tag.getInt(ModConfiguration.appleDropChanceName);
		config.stickDropChance = tag.getInt(ModConfiguration.stickDropChanceName);

		for (String key : ModConfiguration.recipeKeys) {
			config.recipeConfiguration.put(key, tag.getBoolean(key));
		}

		return config;
	}

	private class ConfigFileSettings {
		// Configuration Options.
		public BooleanValue rightClickCropHarvest;
		public BooleanValue enableHomeCommand;
		public BooleanValue enableGrassSpreadToCustomDirt;
		public BooleanValue enableStepAssistEnchantment;
		public BooleanValue enableSwiftCombat;
		public BooleanValue enableFlatBedrockGeneration;
		public BooleanValue enableVerboseLogging;
		public BooleanValue enableMobileLight;

		public HashMap<String, BooleanValue> recipeConfiguration;

		// Extra Drop Options.
		public IntValue diamondShardDropChance;

		public IntValue beetRootSeedDropChance;
		public IntValue melonSeedDropChance;
		public IntValue pumpkinSeedDropChance;
		public IntValue cocoaSeedDropChance;

		public IntValue potatoDropChance;
		public IntValue beetRootDropChance;
		public IntValue carrotDropChance;
		public IntValue boneDropChance;
		public IntValue clayBallDropChance;

		public IntValue coalDropChance;
		public IntValue flintDropChance;
		public IntValue ironNuggetDropChance;
		public IntValue goldNuggetDropChance;
		public IntValue appleDropChance;
		public IntValue stickDropChance;
		public IntValue monsterHeadDropChance;

		public ConfigFileSettings() {
			this.recipeConfiguration = new HashMap<>();
		}
	}
}
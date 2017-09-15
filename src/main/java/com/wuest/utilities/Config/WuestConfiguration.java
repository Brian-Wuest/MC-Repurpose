package com.wuest.utilities.Config;

import com.wuest.utilities.UpdateChecker;
import com.wuest.utilities.WuestUtilities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class WuestConfiguration 
{
	public static String OPTIONS = "general.options";
	public static String RecipeOptions = "general.options.recipes";
	public static String GuiOptions = "general.options.gui";
	public static String ChestContentOptions = "general.options.chest contents";
	public static String ExtraDropOptions = "general.options.extra drops";
	public static String tagKey = "WuestConfig";

	// Config file option names.
	private static String rightClickCropHarvestName = "Right Click Crop Harvest";
	private static String enableGrassSpreadToCustomDirtName = "Enable Grass Spreading To Custom Dirt";
	private static String enableExtraGrassDropsName = "Enable Extra Grass Drops";
	private static String enableVersionCheckMessageName = "Enable Version Checking";
	private static String enableStepAssistEnchantmentName = "Enable Step Assist Enchantment";

	private static String addMetalRecipesName = "Add Metal Recipes";
	private static String addWoodRecipesName = "Add Wood Recipes";
	private static String addStoneRecipesName = "Add Stone Recipes";
	private static String addArmorRecipesName = "Add Armor Recipes";
	private static String addMiscRecipesName = "Add Misc Recipes";
	private static String addNetherStarRecipeName = "Add Nether Star Recipe";
	private static String enableHomeCommandName = "Enable Home Command";
	private static String enableRedstoneClockName = "Enable Redstone Clock Recipe";
	private static String enableBedCompassName = "Enable Bed Compass Recipe";
	private static String enableSwiftBladeName = "Enable Swift Blade Recipes";
	private static String enableEnchrichedFarmlandName = "Enable Enriched Farmland Recipes";
	private static String enableMiniRedstoneBlockName = "Enable Mini Redstone Block Recipe";
	private static String enableAppleStickExtraDropsName = "Enable Extra Leaf Drops";
	private static String enableExtraDropsFromDirtName = "Enable Extra Dirt Drops";
	private static String enableExtraDropsFromStoneName = "Enable Extra Stone Drops";
	private static String enableFlatBedrockGenerationName  = "Enable Flat Bedrock Generation";
	
	private static String addSwordName = "Add Sword";
	private static String addAxeName = "Add Axe";
	private static String addHoeName = "Add Hoe";
	private static String addShovelName = "Add Shovel";
	private static String addPickAxeName = "Add Pickaxe";
	private static String addArmorName = "Add Armor";
	private static String addFoodName = "Add Food";
	private static String addCropsName = "Add Crops";
	private static String addDirtName = "Add Dirt";
	private static String addCobbleName = "Add Cobblestone";
	private static String addSaplingsName = "Add Saplings";
	private static String addTorchesName = "Add Torches";
	
	private static String diamondShardDropChanceName = "Diamond Shard Drop Chance";
	private static String potatoDropChanceName = "Potato Drop Chance";
	private static String beetRootDropChanceName = "Beetroot Drop Chance";
	private static String carrotDropChanceName = "Carrot Drop Chance Name";
	private static String boneDropChanceName = "Bone Drop Chance";
	private static String clayBallDropChanceName = "Clay Item Drop Chance";
	private static String coalDropChanceName = "Coal Drop Chance";
	private static String flintDropChanceName = "Flint Drop Chance";
	private static String ironNuggetDropChanceName = "Iron Nugget Drop Chance";
	private static String goldNuggetDropChanceName = "Gold Nugget Drop Chance";
	private static String appleDropChanceName = "Apple Drop Chance";
	private static String stickDropChanceName = "Stick Drop Chance";
	
	private static String versionMessageName = "Version Message";
	private static String showMessageName = "Show Message";

	// Configuration Options.
	public boolean rightClickCropHarvest;
	public boolean enableHomeCommand;
	public boolean enableGrassSpreadToCustomDirt;
	public boolean enableExtraGrassDrops;
	public boolean enableVersionCheckMessage;
	public boolean enableStepAssistEnchantment;
	public boolean enableAppleStickExtraDrops;
	public boolean enableExtraDropsFromDirt;
	public boolean enableExtraDropsFromStone;
	public static boolean enableFlatBedrockGeneration;

	// Recipe options.
	public boolean addMetalRecipes;
	public boolean addWoodRecipes;
	public boolean addStoneRecipes;
	public boolean addArmorRecipes;
	public boolean addMiscRecipes;
	public boolean addNetherStarRecipe;
	public boolean addRedstoneClockRecipe;
	public boolean addBedCompassRecipe;
	public boolean addSwiftBladeRecipe;
	public boolean addEnrichedFarmlandRecipe;
	public boolean addMiniRedstoneBlockRecipe;
	
	// Extra Drop Options.
	public int diamondShardDropChance;
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
	
	// Version Check Message Info
	public String versionMessage = "";
	public boolean showMessage = false;

	public WuestConfiguration()
	{
		this.rightClickCropHarvest = false;
		this.addMetalRecipes = true;
		this.addWoodRecipes = true;
		this.addStoneRecipes = true;
		this.addArmorRecipes = true;
		this.addMiscRecipes = true;
		this.enableHomeCommand = true;
		this.enableGrassSpreadToCustomDirt = true;
		this.enableExtraGrassDrops = true;
		this.enableVersionCheckMessage = true;
	}

	public static void syncConfig()
	{
		Configuration config = WuestUtilities.config;

		if (WuestUtilities.proxy.proxyConfiguration == null)
		{
			WuestUtilities.proxy.proxyConfiguration = new WuestConfiguration();
		}

		// General settings.
		WuestUtilities.proxy.proxyConfiguration.rightClickCropHarvest = config.getBoolean(WuestConfiguration.rightClickCropHarvestName, WuestConfiguration.OPTIONS, false, "Determines if right-clicking crops will harvest them. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.enableHomeCommand = config.getBoolean(WuestConfiguration.enableHomeCommandName, WuestConfiguration.OPTIONS, true, "Determines if home command is enabled. This command will allow the player to teleport to the last bed they slept in. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.enableGrassSpreadToCustomDirt = config.getBoolean(WuestConfiguration.enableGrassSpreadToCustomDirtName, WuestConfiguration.OPTIONS, true, "Determines if grass will spread to the custom dirt blocks added by this mod. Sever configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.enableVersionCheckMessage = config.getBoolean(WuestConfiguration.enableVersionCheckMessageName, WuestConfiguration.OPTIONS, true, "Determines if version checking is enabled when application starts. Also determines if the chat message about old versions is shown when joining a world. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.enableAppleStickExtraDrops = config.getBoolean(WuestConfiguration.enableAppleStickExtraDropsName, WuestConfiguration.OPTIONS, true, "Determines if all types of leaves will drop apples and/or sticks.");
		WuestUtilities.proxy.proxyConfiguration.enableExtraDropsFromDirt = config.getBoolean(WuestConfiguration.enableExtraDropsFromDirtName, WuestConfiguration.OPTIONS, true, "Determines if Potatoes, Carrots, Bones, Clay and Beetroots will drop when breaking dirt/grasss blocks.");
		WuestUtilities.proxy.proxyConfiguration.enableExtraDropsFromStone = config.getBoolean(WuestConfiguration.enableExtraDropsFromStoneName, WuestConfiguration.OPTIONS, true, "Determines if Coal, Iron Nuggets and Flint will drop when breaking stone");
		WuestUtilities.proxy.proxyConfiguration.enableFlatBedrockGeneration = config.getBoolean(WuestConfiguration.enableFlatBedrockGenerationName, WuestConfiguration.OPTIONS, true, "Determines if overworld bedrock is flat. Server configuration overrides client.");
		
		// This one is special since it requires a minecraft restart.
		Property prop = config.get(WuestConfiguration.OPTIONS, WuestConfiguration.enableExtraGrassDropsName, true, "Determines if tall grass can also drop: melon, pumpkin, cocoa and beetroot seeds. Server configuration overrides client.");
		prop.setRequiresMcRestart(true);
		WuestUtilities.proxy.proxyConfiguration.enableExtraGrassDrops = prop.getBoolean();
		
		prop = config.get(WuestConfiguration.OPTIONS, WuestConfiguration.enableStepAssistEnchantmentName, true, "Determines if the Step Assist family of enchantments is enabled. \r\nRequires World Restart. Server Configuration overrides client.");
		prop.setRequiresWorldRestart(true);
		WuestUtilities.proxy.proxyConfiguration.enableStepAssistEnchantment = prop.getBoolean();
		
		config.setCategoryComment(WuestConfiguration.RecipeOptions, "This category is to turn on or off the various categories of recipes this mod adds.");

		// Recipe settings.
		WuestUtilities.proxy.proxyConfiguration.addMetalRecipes = config.getBoolean(WuestConfiguration.addMetalRecipesName, WuestConfiguration.RecipeOptions, true, "Determines if the metal recipes are added. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.addWoodRecipes = config.getBoolean(WuestConfiguration.addWoodRecipesName, WuestConfiguration.RecipeOptions, true, "Determines if the wood recipes are added. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.addStoneRecipes = config.getBoolean(WuestConfiguration.addStoneRecipesName, WuestConfiguration.RecipeOptions, true, "Determines if the stone recipes are added. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.addArmorRecipes = config.getBoolean(WuestConfiguration.addArmorRecipesName, WuestConfiguration.RecipeOptions, true, "Determines if the armor recipes are added. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.addMiscRecipes = config.getBoolean(WuestConfiguration.addMiscRecipesName, WuestConfiguration.RecipeOptions, true, "Determines if the misc recipes are added. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.addNetherStarRecipe = config.getBoolean(WuestConfiguration.addNetherStarRecipeName, WuestConfiguration.RecipeOptions, true, "Determines if the Nether Star recipe are added. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.addRedstoneClockRecipe = config.getBoolean(WuestConfiguration.enableRedstoneClockName, WuestConfiguration.RecipeOptions, true, "Determines if the Redstone Clock block recipe is enabled. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.addBedCompassRecipe = config.getBoolean(WuestConfiguration.enableBedCompassName, WuestConfiguration.RecipeOptions, true, "Determines if the Bed Compass item recipe is enabled. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.addSwiftBladeRecipe = config.getBoolean(WuestConfiguration.enableSwiftBladeName, WuestConfiguration.RecipeOptions, true, "Determines if the Swift Blade item recipes are enabled. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.addEnrichedFarmlandRecipe = config.getBoolean(WuestConfiguration.enableEnchrichedFarmlandName, WuestConfiguration.RecipeOptions, true, "Determines if the Enriched Farmland recipe is enabled. Server Configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.addMiniRedstoneBlockRecipe = config.getBoolean(WuestConfiguration.enableMiniRedstoneBlockName, WuestConfiguration.RecipeOptions, true, "Determines if the Mini Redstone Block recipe is enabled. Server Configuration overrides client.");
		
		// This entire category requires a minecraft restart.
		config.setCategoryRequiresMcRestart(WuestConfiguration.RecipeOptions, true);
		
		config.setCategoryComment(WuestConfiguration.ExtraDropOptions, "This category determines the drop chances for the various extra drop options.");
		config.setCategoryRequiresMcRestart(WuestConfiguration.ExtraDropOptions, true);
		
		// Extra Drop Options.
		WuestUtilities.proxy.proxyConfiguration.diamondShardDropChance = config.getInt(WuestConfiguration.diamondShardDropChanceName, WuestConfiguration.ExtraDropOptions, 1, 0, 100, "Defines the drop chance of diamond shards from coal ore blocks. This is percent based so 1 = 1%. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.potatoDropChance = config.getInt(WuestConfiguration.potatoDropChanceName, WuestConfiguration.ExtraDropOptions, 4, 0, 100, "Defines the drop chance of potatoes from dirt/grass blocks. This is percent based so 1 = 1%. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.carrotDropChance = config.getInt(WuestConfiguration.carrotDropChanceName, WuestConfiguration.ExtraDropOptions, 4, 0, 100, "Defines the drop chance of carrots from dirt/grass blocks. This is percent based so 1 = 1%. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.beetRootDropChance = config.getInt(WuestConfiguration.beetRootDropChanceName, WuestConfiguration.ExtraDropOptions, 4, 0, 100, "Defines the drop chance of beet roots from dirt/grass blocks. This is percent based so 1 = 1%. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.clayBallDropChance = config.getInt(WuestConfiguration.clayBallDropChanceName, WuestConfiguration.ExtraDropOptions, 4, 0, 100, "Defines the drop chance of clay items from dirt/grass blocks. This is percent based so 1 = 1%. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.boneDropChance = config.getInt(WuestConfiguration.boneDropChanceName, WuestConfiguration.ExtraDropOptions, 4, 0, 100, "Defines the drop chance of bones from dirt/grass blocks. This is percent based so 1 = 1%. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.coalDropChance = config.getInt(WuestConfiguration.coalDropChanceName, WuestConfiguration.ExtraDropOptions, 4, 0, 100, "Defines the drop chance of coal from stone (all vanilla varieties) blocks. This is percent based so 1 = 1%. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.flintDropChance = config.getInt(WuestConfiguration.flintDropChanceName, WuestConfiguration.ExtraDropOptions, 4, 0, 100, "Defines the drop chance of flint from stone (all vanilla varieties) blocks. This is percent based so 1 = 1%. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.ironNuggetDropChance = config.getInt(WuestConfiguration.ironNuggetDropChanceName, WuestConfiguration.ExtraDropOptions, 4, 0, 100, "Defines the drop chance of iron nuggets from stone (all vanilla varieties) blocks. This is percent based so 1 = 1%. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.goldNuggetDropChance = config.getInt(WuestConfiguration.goldNuggetDropChanceName, WuestConfiguration.ExtraDropOptions, 2, 0, 100, "Defines the drop chance of gold nuggets from stone (all vanilla varieties) blocks. This is percent based so 1 = 1%. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.appleDropChance = config.getInt(WuestConfiguration.appleDropChanceName, WuestConfiguration.ExtraDropOptions, 4, 0, 100, "Defines the drop chance of apples from leaves (all varieties) blocks. This is percent based so 1 = 1%. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.stickDropChance = config.getInt(WuestConfiguration.stickDropChanceName, WuestConfiguration.ExtraDropOptions, 6, 0, 100, "Defines the drop chance of sticks from leaves (all varieties) blocks. This is percent based so 1 = 1%. Server configuration overrides client.");
		
		// GUI Options
		//config.setCategoryComment(WuestConfiguration.GuiOptions, "This category is to configure the various GUI options for this mod.");
		
		if (config.hasChanged()) 
		{
			config.save();
		}
	}

	public NBTTagCompound ToNBTTagCompound()
	{
		NBTTagCompound tag = new NBTTagCompound();
		
		tag.setBoolean(WuestConfiguration.rightClickCropHarvestName, this.rightClickCropHarvest);
		tag.setBoolean(WuestConfiguration.enableHomeCommandName, this.enableHomeCommand);
		tag.setBoolean(WuestConfiguration.enableGrassSpreadToCustomDirtName, this.enableGrassSpreadToCustomDirt);
		tag.setBoolean(WuestConfiguration.enableVersionCheckMessageName, this.enableVersionCheckMessage);
		tag.setBoolean(WuestConfiguration.enableExtraGrassDropsName, this.enableExtraGrassDrops);
		tag.setBoolean(WuestConfiguration.enableStepAssistEnchantmentName, this.enableStepAssistEnchantment);
		tag.setBoolean(WuestConfiguration.addMetalRecipesName, this.addMetalRecipes);
		tag.setBoolean(WuestConfiguration.addWoodRecipesName, this.addWoodRecipes);
		tag.setBoolean(WuestConfiguration.addStoneRecipesName, this.addStoneRecipes);
		tag.setBoolean(WuestConfiguration.addArmorRecipesName, this.addArmorRecipes);
		tag.setBoolean(WuestConfiguration.addMiscRecipesName, this.addMiscRecipes);
		tag.setBoolean(WuestConfiguration.addNetherStarRecipeName, this.addNetherStarRecipe);
		tag.setBoolean(WuestConfiguration.enableRedstoneClockName, this.addRedstoneClockRecipe);
		tag.setBoolean(WuestConfiguration.enableBedCompassName, this.addBedCompassRecipe);
		tag.setBoolean(WuestConfiguration.enableSwiftBladeName, this.addSwiftBladeRecipe);
		tag.setBoolean(WuestConfiguration.enableEnchrichedFarmlandName, this.addEnrichedFarmlandRecipe);
		tag.setBoolean(WuestConfiguration.enableMiniRedstoneBlockName, this.addMiniRedstoneBlockRecipe);
		tag.setBoolean(WuestConfiguration.enableAppleStickExtraDropsName, this.enableAppleStickExtraDrops);
		tag.setBoolean(WuestConfiguration.enableExtraDropsFromDirtName, this.enableExtraDropsFromDirt);
		tag.setBoolean(WuestConfiguration.enableExtraDropsFromStoneName, this.enableExtraDropsFromStone);
		
		tag.setString(WuestConfiguration.versionMessageName, UpdateChecker.messageToShow);
		tag.setBoolean(WuestConfiguration.showMessageName, UpdateChecker.showMessage);
		
		return tag;
	}
	
	public static WuestConfiguration getFromNBTTagCompound(NBTTagCompound tag)
	{
		WuestConfiguration config = new WuestConfiguration();
		
		config.rightClickCropHarvest = tag.getBoolean(WuestConfiguration.rightClickCropHarvestName);
		
		config.enableHomeCommand = tag.getBoolean(WuestConfiguration.enableHomeCommandName);
		config.enableGrassSpreadToCustomDirt = tag.getBoolean(WuestConfiguration.enableGrassSpreadToCustomDirtName);
		config.enableVersionCheckMessage = tag.getBoolean(WuestConfiguration.enableVersionCheckMessageName);
		config.enableExtraGrassDrops = tag.getBoolean(WuestConfiguration.enableExtraGrassDropsName);
		config.addMetalRecipes = tag.getBoolean(WuestConfiguration.addMetalRecipesName);
		config.enableStepAssistEnchantment = tag.getBoolean(WuestConfiguration.enableStepAssistEnchantmentName);
		
		config.addWoodRecipes = tag.getBoolean(WuestConfiguration.addWoodRecipesName);
		config.addStoneRecipes = tag.getBoolean(WuestConfiguration.addStoneRecipesName);
		config.addArmorRecipes = tag.getBoolean(WuestConfiguration.addArmorRecipesName);
		config.addMiscRecipes = tag.getBoolean(WuestConfiguration.addMiscRecipesName);
		config.addNetherStarRecipe = tag.getBoolean(WuestConfiguration.addNetherStarRecipeName);
		
		config.addRedstoneClockRecipe = tag.getBoolean(WuestConfiguration.enableRedstoneClockName);
		config.addBedCompassRecipe = tag.getBoolean(WuestConfiguration.enableBedCompassName);
		config.addSwiftBladeRecipe = tag.getBoolean(WuestConfiguration.enableSwiftBladeName);
		config.addEnrichedFarmlandRecipe = tag.getBoolean(WuestConfiguration.enableEnchrichedFarmlandName);
		config.addMiniRedstoneBlockRecipe = tag.getBoolean(WuestConfiguration.enableMiniRedstoneBlockName);
		
		config.versionMessage = tag.getString(WuestConfiguration.versionMessageName);
		config.showMessage = tag.getBoolean(WuestConfiguration.showMessageName);
		
		config.enableAppleStickExtraDrops = tag.getBoolean(WuestConfiguration.enableAppleStickExtraDropsName);
		config.enableExtraDropsFromDirt = tag.getBoolean(WuestConfiguration.enableExtraDropsFromDirtName);
		config.enableExtraDropsFromStone = tag.getBoolean(WuestConfiguration.enableExtraDropsFromStoneName);
		
		return config;
	}
}
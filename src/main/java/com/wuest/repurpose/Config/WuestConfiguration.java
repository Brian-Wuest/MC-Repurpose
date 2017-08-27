package com.wuest.repurpose.Config;

import java.util.HashMap;
import java.util.Map.Entry;

import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.UpdateChecker;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class WuestConfiguration 
{
	public static String OPTIONS = "general.options";
	public static String RecipeOptions = "general.options.recipes";
	public static String GuiOptions = "general.options.gui";
	public static String ChestContentOptions = "general.options.chest contents";
	public static String tagKey = "WuestConfig";

	// Config file option names.
	public static String rightClickCropHarvestName = "Right Click Crop Harvest";
	public static String enableGrassSpreadToCustomDirtName = "Enable Grass Spreading To Custom Dirt";
	public static String enableExtraGrassDropsName = "Enable Extra Grass Drops";
	public static String enableVersionCheckMessageName = "Enable Version Checking";
	public static String enableStepAssistEnchantmentName = "Enable Step Assist Enchantment";
	public static String enableFlatBedrockGenerationName = "Enable Flat Bedrock Generation";

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
	public static String enableAppleStickExtraDropsName = "Enable Extra Leaf Drops";
	public static String enableExtraDropsFromDirtName = "Enable Extra Dirt Drops";
	public static String enableExtraDropsFromStoneName = "Enable Extra Stone Drops";
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
	
	public static String versionMessageName = "Version Message";
	public static String showMessageName = "Show Message";

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
	public boolean enableSwiftCombat;
	public boolean enableFlatBedrockGeneration;

	public HashMap<String, Boolean> recipeConfiguration;
	
	// Recipe options.
	public static String[] recipeKeys = new String[] 
	{ 
		addMetalRecipesName,
		addArmorRecipesName,
		addMiscRecipesName,
		addNetherStarRecipeName,
		addStoneRecipesName,
		addWoodRecipesName,
		enableBedCompassName,
		enableEnchrichedFarmlandName,
		enableMiniRedstoneBlockName,
		enableRedstoneClockName,
		addDirtRecipesName,
		addSnorkelName,
		addWhetstoneName,
		addGlowstoneSlabName,
		addFluffyFabricName,
		addStoneShearsName,
		addDiamonShardName,
		addRedstoneScannerName,
		sickleRecipeName,
		swiftBladeName,
		ironLumpName,
		charcoalBlockName
	};
	
	// Version Check Message Info
	public String versionMessage = "";
	public boolean showMessage = false;

	public WuestConfiguration()
	{
		this.rightClickCropHarvest = false;
		this.enableHomeCommand = true;
		this.enableGrassSpreadToCustomDirt = true;
		this.enableExtraGrassDrops = true;
		this.enableVersionCheckMessage = true;
		this.enableSwiftCombat = true;
		this.recipeConfiguration = new HashMap<String, Boolean>();
		this.enableFlatBedrockGeneration = true;
	}

	public static void syncConfig()
	{
		Configuration config = Repurpose.config;

		if (Repurpose.proxy.proxyConfiguration == null)
		{
			Repurpose.proxy.proxyConfiguration = new WuestConfiguration();
		}

		// General settings.
		Repurpose.proxy.proxyConfiguration.rightClickCropHarvest = config.getBoolean(WuestConfiguration.rightClickCropHarvestName, WuestConfiguration.OPTIONS, false, "Determines if right-clicking crops will harvest them. Server configuration overrides client.");
		Repurpose.proxy.proxyConfiguration.enableHomeCommand = config.getBoolean(WuestConfiguration.enableHomeCommandName, WuestConfiguration.OPTIONS, true, "Determines if home command is enabled. This command will allow the player to teleport to the last bed they slept in. Server configuration overrides client.");
		Repurpose.proxy.proxyConfiguration.enableGrassSpreadToCustomDirt = config.getBoolean(WuestConfiguration.enableGrassSpreadToCustomDirtName, WuestConfiguration.OPTIONS, true, "Determines if grass will spread to the custom dirt blocks added by this mod. Sever configuration overrides client.");
		Repurpose.proxy.proxyConfiguration.enableVersionCheckMessage = config.getBoolean(WuestConfiguration.enableVersionCheckMessageName, WuestConfiguration.OPTIONS, true, "Determines if version checking is enabled when application starts. Also determines if the chat message about old versions is shown when joining a world. Server configuration overrides client.");
		Repurpose.proxy.proxyConfiguration.enableAppleStickExtraDrops = config.getBoolean(WuestConfiguration.enableAppleStickExtraDropsName, WuestConfiguration.OPTIONS, true, "Determines if all types of leaves will drop apples and/or sticks.");
		Repurpose.proxy.proxyConfiguration.enableExtraDropsFromDirt = config.getBoolean(WuestConfiguration.enableExtraDropsFromDirtName, WuestConfiguration.OPTIONS, true, "Determines if Potatoes, Carrots, Bones, Clay and Beetroots will drop when breaking dirt/grasss blocks.");
		Repurpose.proxy.proxyConfiguration.enableExtraDropsFromStone = config.getBoolean(WuestConfiguration.enableExtraDropsFromStoneName, WuestConfiguration.OPTIONS, true, "Determines if Coal, Iron Nuggets and Flint will drop when breaking stone");
		Repurpose.proxy.proxyConfiguration.enableFlatBedrockGeneration = config.getBoolean(WuestConfiguration.enableFlatBedrockGenerationName, WuestConfiguration.OPTIONS, true, "Determines if overworld bedrock is flat. Server configuration overrides client.");
		
		// This one is special since it requires a minecraft restart.
		Property prop = config.get(WuestConfiguration.OPTIONS, WuestConfiguration.enableExtraGrassDropsName, true, "Determines if tall grass can also drop: melon, pumpkin, cocoa and beetroot seeds. Server configuration overrides client.");
		prop.setRequiresMcRestart(true);
		Repurpose.proxy.proxyConfiguration.enableExtraGrassDrops = prop.getBoolean();
		
		prop = config.get(WuestConfiguration.OPTIONS, WuestConfiguration.enableStepAssistEnchantmentName, true, "Determines if the Step Assist family of enchantments is enabled. \r\nRequires World Restart. Server Configuration overrides client.");
		prop.setRequiresWorldRestart(true);
		Repurpose.proxy.proxyConfiguration.enableStepAssistEnchantment = prop.getBoolean();
		
		config.setCategoryComment(WuestConfiguration.RecipeOptions, "This category is to turn on or off the various categories of recipes this mod adds.");

		// Recipe settings.
		// Recipe configuration.
		for (String key : WuestConfiguration.recipeKeys)
		{
			boolean value = config.getBoolean(key, RecipeOptions, true, "Determines if the recipe(s) associated with the " + key + " are enabled. Server configuration overrides client.");
			Repurpose.proxy.proxyConfiguration.recipeConfiguration.put(key, value);
		}
		
		// This entire category requires a minecraft restart.
		config.setCategoryRequiresMcRestart(WuestConfiguration.RecipeOptions, true);
		
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
		tag.setBoolean(WuestConfiguration.enableAppleStickExtraDropsName, this.enableAppleStickExtraDrops);
		tag.setBoolean(WuestConfiguration.enableExtraDropsFromDirtName, this.enableExtraDropsFromDirt);
		tag.setBoolean(WuestConfiguration.enableExtraDropsFromStoneName, this.enableExtraDropsFromStone);
		tag.setBoolean(WuestConfiguration.enableFlatBedrockGenerationName, this.enableFlatBedrockGeneration);
		
		for (Entry<String, Boolean> entry : this.recipeConfiguration.entrySet())
		{
			tag.setBoolean(entry.getKey(), entry.getValue());
		}
		
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
		config.enableStepAssistEnchantment = tag.getBoolean(WuestConfiguration.enableStepAssistEnchantmentName);
		
		config.versionMessage = tag.getString(WuestConfiguration.versionMessageName);
		config.showMessage = tag.getBoolean(WuestConfiguration.showMessageName);
		
		config.enableAppleStickExtraDrops = tag.getBoolean(WuestConfiguration.enableAppleStickExtraDropsName);
		config.enableExtraDropsFromDirt = tag.getBoolean(WuestConfiguration.enableExtraDropsFromDirtName);
		config.enableExtraDropsFromStone = tag.getBoolean(WuestConfiguration.enableExtraDropsFromStoneName);
		
		config.enableFlatBedrockGeneration = tag.getBoolean(WuestConfiguration.enableFlatBedrockGenerationName);
		
		for (String key : WuestConfiguration.recipeKeys)
		{
			config.recipeConfiguration.put(key, tag.getBoolean(key));
		}
		
		return config;
	}
}
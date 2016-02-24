package wuest.utilities.Gui;

import java.io.File;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import wuest.utilities.*;

public class WuestConfiguration 
{
	public static String OPTIONS = "general.options";
	public static String RecipeOptions = "general.options.recipes";
	public static String ChestContentOptions = "general.options.chest contents";
	public static String tagKey = "WuestConfig";
	
	// Config file option names.
	private static String addHouseItemName = "Add House Item On New Player Join";
	private static String rightClickCropHarvestName = "Right Click Crop Harvest";
	
	private static String addTorchesName = "Add Torches";
	private static String addBedName = "Add Bed";
	private static String addCraftingTableName = "Add Crafting Table";
	private static String addChestName = "Add Chest";
	private static String addChestContentsName = "Add Chest Contents";
	private static String addFarmName = "Add Farm";
	private static String floorBlockName = "Floor Stone Type";
	private static String ceilingBlockName = "Ceiling Stone Type";
	private static String wallWoodTypeName = "Wall Wood Type";
	private static String isCeilingFlatName = "Is Ceiling Flat";
	private static String addMineShaftName = "Build Mineshaft";
	
	private static String addMetalRecipesName = "Add Metal Recipes";
	private static String addWoodRecipesName = "Add Wood Recipes";
	private static String addStoneRecipesName = "Add Stone Recipes";
	private static String addArmorRecipesName = "Add Armor Recipes";
	private static String addMiscRecipesName = "Add Misc Recipes";
	private static String addNetherStarRecipeName = "Add Nether Star Recipe";
	private static String enableHomeCommandName = "Enable Home Command";
	private static String enableRedstoneClockName = "Enable Redstone Clock Recipe";
	
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
	private static String addChestTorchesName = "Add Torches";
	
	// Configuration Options.
	public boolean addHouseItem;
	public boolean rightClickCropHarvest;
	public boolean enableHomeCommand;
	
	// Recipe options.
	public boolean addMetalRecipes;
	public boolean addWoodRecipes;
	public boolean addStoneRecipes;
	public boolean addArmorRecipes;
	public boolean addMiscRecipes;
	public boolean addNetherStarRecipe;
	public boolean addRedstoneClockRecipe;
	
	// Chest content options.
	public boolean addSword;
	public boolean addAxe;
	public boolean addHoe;
	public boolean addShovel;
	public boolean addPickAxe;
	
	public boolean addArmor;
	public boolean addFood;
	public boolean addCrops;
	public boolean addDirt;
	public boolean addCobble;
	public boolean addSaplings;
	public boolean addTorches;
	
	public WuestConfiguration()
	{
		this.addHouseItem = true;
		this.rightClickCropHarvest = false;
		this.addMetalRecipes = true;
		this.addWoodRecipes = true;
		this.addStoneRecipes = true;
		this.addArmorRecipes = true;
		this.addMiscRecipes = true;
		this.enableHomeCommand = true;
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
		WuestUtilities.proxy.proxyConfiguration.addHouseItem = config.getBoolean(WuestConfiguration.addHouseItemName, WuestConfiguration.OPTIONS, true, "Determines if the house item is added to player inventory when joining the world for the first time. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.enableHomeCommand = config.getBoolean(WuestConfiguration.enableHomeCommandName, WuestConfiguration.OPTIONS, true, "Determines if home command is enabled. This command will allow the player to teleport to the last bed they slept in. Server configuration overrides client.");
		
		// Remove the House options.
		config = WuestConfiguration.RemoveOldHouseOptions(config);
		
		config.setCategoryComment(WuestConfiguration.RecipeOptions, "This category is to turn on or off the various categories of recipes this mod adds.");
		
		// Recipe settings.
		WuestUtilities.proxy.proxyConfiguration.addMetalRecipes = config.getBoolean(WuestConfiguration.addMetalRecipesName, WuestConfiguration.RecipeOptions, true, "Determines if the metal recipes are added. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.addWoodRecipes = config.getBoolean(WuestConfiguration.addWoodRecipesName, WuestConfiguration.RecipeOptions, true, "Determines if the wood recipes are added. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.addStoneRecipes = config.getBoolean(WuestConfiguration.addStoneRecipesName, WuestConfiguration.RecipeOptions, true, "Determines if the stone recipes are added. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.addArmorRecipes = config.getBoolean(WuestConfiguration.addArmorRecipesName, WuestConfiguration.RecipeOptions, true, "Determines if the armor recipes are added. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.addMiscRecipes = config.getBoolean(WuestConfiguration.addMiscRecipesName, WuestConfiguration.RecipeOptions, true, "Determines if the misc recipes are added. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.addNetherStarRecipe = config.getBoolean(WuestConfiguration.addNetherStarRecipeName, WuestConfiguration.RecipeOptions, true, "Determines if the Nether Star recipe are added. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.addRedstoneClockRecipe = config.getBoolean(WuestConfiguration.enableRedstoneClockName, WuestConfiguration.RecipeOptions, true, "Determines if the Redstone Clock block recipe is enabled. Server configuration overrides client.");
		
		config.setCategoryComment(WuestConfiguration.ChestContentOptions, "This category is to determine the contents of the chest created by the house item. When playing on a server, the server configuration is used.");
		
		WuestUtilities.proxy.proxyConfiguration.addSword = config.getBoolean(WuestConfiguration.addSwordName, WuestConfiguration.ChestContentOptions, true, "Determines if a Stone Sword is added the the chest when the house is created.");
		WuestUtilities.proxy.proxyConfiguration.addAxe = config.getBoolean(WuestConfiguration.addAxeName, WuestConfiguration.ChestContentOptions, true, "Determines if a Stone Axe is added the the chest when the house is created.");
		WuestUtilities.proxy.proxyConfiguration.addShovel = config.getBoolean(WuestConfiguration.addShovelName, WuestConfiguration.ChestContentOptions, true, "Determines if a Stone Shovel is added the the chest when the house is created.");
		WuestUtilities.proxy.proxyConfiguration.addHoe = config.getBoolean(WuestConfiguration.addHoeName, WuestConfiguration.ChestContentOptions, true, "Determines if a Stone Hoe is added the the chest when the house is created.");
		WuestUtilities.proxy.proxyConfiguration.addPickAxe = config.getBoolean(WuestConfiguration.addPickAxeName, WuestConfiguration.ChestContentOptions, true, "Determines if a Stone Pickaxe is added the the chest when the house is created.");
		WuestUtilities.proxy.proxyConfiguration.addArmor = config.getBoolean(WuestConfiguration.addArmorName, WuestConfiguration.ChestContentOptions, true, "Determines if Leather Armor is added the the chest when the house is created.");
		WuestUtilities.proxy.proxyConfiguration.addFood = config.getBoolean(WuestConfiguration.addFoodName, WuestConfiguration.ChestContentOptions, true, "Determines if Bread is added the the chest when the house is created.");
		WuestUtilities.proxy.proxyConfiguration.addCrops = config.getBoolean(WuestConfiguration.addCropsName, WuestConfiguration.ChestContentOptions, true, "Determines if seeds, potatoes and carros are added the the chest when the house is created.");
		WuestUtilities.proxy.proxyConfiguration.addDirt = config.getBoolean(WuestConfiguration.addDirtName, WuestConfiguration.ChestContentOptions, true, "Determines if a stack of dirt is added the the chest when the house is created.");
		WuestUtilities.proxy.proxyConfiguration.addCobble = config.getBoolean(WuestConfiguration.addCobbleName, WuestConfiguration.ChestContentOptions, true, "Determines if a stack of cobble is added the the chest when the house is created.");
		WuestUtilities.proxy.proxyConfiguration.addSaplings = config.getBoolean(WuestConfiguration.addSaplingsName, WuestConfiguration.ChestContentOptions, true, "Determines if a set of oak saplings are added the the chest when the house is created.");
		WuestUtilities.proxy.proxyConfiguration.addTorches = config.getBoolean(WuestConfiguration.addTorchesName, WuestConfiguration.ChestContentOptions, true, "Determines if a set of torches are added the the chest when the house is created.");
		
	    if (config.hasChanged()) 
	    {
	    	config.save();
	    }
	}
	
	private static Configuration RemoveOldHouseOptions(Configuration config)
	{
		if (config.getCategory(WuestConfiguration.OPTIONS).containsKey(WuestConfiguration.addTorchesName))
		{
			config.getCategory(WuestConfiguration.OPTIONS).remove(WuestConfiguration.addTorchesName);
		}
		
		if (config.getCategory(WuestConfiguration.OPTIONS).containsKey(WuestConfiguration.addBedName))
		{
			config.getCategory(WuestConfiguration.OPTIONS).remove(WuestConfiguration.addBedName);
		}
		
		if (config.getCategory(WuestConfiguration.OPTIONS).containsKey(WuestConfiguration.addCraftingTableName))
		{
			config.getCategory(WuestConfiguration.OPTIONS).remove(WuestConfiguration.addCraftingTableName);
		}
		
		if (config.getCategory(WuestConfiguration.OPTIONS).containsKey(WuestConfiguration.addChestName))
		{
			config.getCategory(WuestConfiguration.OPTIONS).remove(WuestConfiguration.addChestName);
		}
		
		if (config.getCategory(WuestConfiguration.OPTIONS).containsKey(WuestConfiguration.addChestContentsName))
		{
			config.getCategory(WuestConfiguration.OPTIONS).remove(WuestConfiguration.addChestContentsName);
		}
		
		if (config.getCategory(WuestConfiguration.OPTIONS).containsKey(WuestConfiguration.addFarmName))
		{
			config.getCategory(WuestConfiguration.OPTIONS).remove(WuestConfiguration.addFarmName);
		}
		
		if (config.getCategory(WuestConfiguration.OPTIONS).containsKey(WuestConfiguration.floorBlockName))
		{
			config.getCategory(WuestConfiguration.OPTIONS).remove(WuestConfiguration.floorBlockName);
		}
		
		if (config.getCategory(WuestConfiguration.OPTIONS).containsKey(WuestConfiguration.ceilingBlockName))
		{
			config.getCategory(WuestConfiguration.OPTIONS).remove(WuestConfiguration.ceilingBlockName);
		}
		
		if (config.getCategory(WuestConfiguration.OPTIONS).containsKey(WuestConfiguration.wallWoodTypeName))
		{
			config.getCategory(WuestConfiguration.OPTIONS).remove(WuestConfiguration.wallWoodTypeName);
		}
		
		if (config.getCategory(WuestConfiguration.OPTIONS).containsKey(WuestConfiguration.isCeilingFlatName))
		{
			config.getCategory(WuestConfiguration.OPTIONS).remove(WuestConfiguration.isCeilingFlatName);
		}
		
		if (config.getCategory(WuestConfiguration.OPTIONS).containsKey(WuestConfiguration.addMineShaftName))
		{
			config.getCategory(WuestConfiguration.OPTIONS).remove(WuestConfiguration.addMineShaftName);
		}
		
		
		return config;
	}


	public enum CeilingFloorBlockType
	{
		StoneBrick(0),
		Brick(1),
		SandStone(2);
		
        private final int value;

        CeilingFloorBlockType(int newValue) 
        {
            value = newValue;
        }

        public int getValue() { return value; }
        
        public static CeilingFloorBlockType ValueOf(int value)
        {
        	switch (value)
        	{
	        	case 1:
	        	{
	        		return CeilingFloorBlockType.Brick;
	        	}
	        	
	        	case 2:
	        	{
	        		return CeilingFloorBlockType.SandStone;
	        	}
	        	
	        	default:
	        	{
	        		return CeilingFloorBlockType.StoneBrick;
	        	}
        	}
        }
	}
	
	public enum WallBlockType
	{
		Oak(0),
		Spruce(1),
		Birch(2),
		Jungle(3),
		Acacia(4),
		DarkOak(5);
		
        private final int value;

        WallBlockType(final int newValue) 
        {
            value = newValue;
        }

        public int getValue() { return value; }
        
        public static WallBlockType ValueOf(int value)
        {
        	switch (value)
        	{
	        	case 1:
	        	{
	        		return WallBlockType.Spruce;
	        	}
	        	
	        	case 2:
	        	{
	        		return WallBlockType.Birch;
	        	}
	        	
	        	case 3:
	        	{
	        		return WallBlockType.Jungle;
	        	}
	        	
	        	case 4:
	        	{
	        		return WallBlockType.Acacia;
	        	}
	        	
	        	case 5:
	        	{
	        		return WallBlockType.DarkOak;
	        	}
	        	
	        	default:
	        	{
	        		return WallBlockType.Oak;
	        	}
        	}
        }
	}
}

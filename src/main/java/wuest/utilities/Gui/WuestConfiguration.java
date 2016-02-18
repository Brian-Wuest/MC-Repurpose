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
	public static String tagKey = "WuestConfig";
	
	// Configuration Option strings.
	private static String addHouseItemTag = "addHouseItem";
	private static String addTorchesTag = "addTorches";
	private static String addBedTag = "addBed";
	private static String addCraftingTableTag = "addCraftingTable";
	private static String addChestTag = "addChest";
	private static String addChestContentsTag = "addChestContents";
	private static String addFarmTag = "addFarm";
	private static String rightClickCropHarvestTag = "rightClickCropHarvest";
	private static String floorBlockTag = "floorBlock";
	private static String ceilingBlockTag = "ceilingBlock";
	private static String wallWoodTypeTag = "wallWoodType";
	private static String isCeilingFlatTag = "isCeilingFlat";
	private static String addMineShaftTag = "addMineShaft";
	private static String addMetalRecipesTag = "addMetalRecipes";
	private static String addWoodRecipesTag = "addWoodRecipes";
	private static String addStoneRecipesTag = "addStoneRecipes";
	private static String addArmorRecipesTag = "addArmorRecipes";
	private static String addMiscRecipesTag = "addMiscRecipes";
	private static String addNetherStarRecipeTag = "addNetherStarRecipe";
	private static String enableHomeCommandTag = "enableHomeCommand";
	
	// Config file option names.
	private static String addHouseItemName = "Add House Item On New Player Join";
	private static String addTorchesName = "Add Torches";
	private static String addBedName = "Add Bed";
	private static String addCraftingTableName = "Add Crafting Table";
	private static String addChestName = "Add Chest";
	private static String addChestContentsName = "Add Chest Contents";
	private static String addFarmName = "Add Farm";
	private static String rightClickCropHarvestName = "Right Click Crop Harvest";
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
	
	// Configuration Options.
	public boolean addHouseItem;
	public boolean addTorches;
	public boolean addBed;
	public boolean addCraftingTable;
	public boolean addChest;
	public boolean addChestContents;
	public boolean addFarm;
	public boolean rightClickCropHarvest;
	public CeilingFloorBlockType floorBlock;
	public CeilingFloorBlockType ceilingBlock;
	public WallBlockType wallWoodType;
	public boolean isCeilingFlat;
	public boolean addMineShaft;
	public boolean addMetalRecipes;
	public boolean addWoodRecipes;
	public boolean addStoneRecipes;
	public boolean addArmorRecipes;
	public boolean addMiscRecipes;
	public boolean addNetherStarRecipe;
	public boolean enableHomeCommand;
	
	public WuestConfiguration()
	{
		this.addHouseItem = true;
		this.addTorches = true;
		this.addBed = true;
		this.addCraftingTable = true;
		this.addChest = true;
		this.addChestContents = true;
		this.addFarm = true;
		this.rightClickCropHarvest = false;
		this.floorBlock = CeilingFloorBlockType.StoneBrick;
		this.ceilingBlock = CeilingFloorBlockType.StoneBrick;
		this.wallWoodType = WallBlockType.Oak;
		this.isCeilingFlat = true;
		this.addMineShaft = true;
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
		
		// House options.
		WuestUtilities.proxy.proxyConfiguration.addTorches = config.getBoolean(WuestConfiguration.addTorchesName, WuestConfiguration.OPTIONS, true, "Option to include torches in the house");
		WuestUtilities.proxy.proxyConfiguration.addBed = config.getBoolean(WuestConfiguration.addBedName, WuestConfiguration.OPTIONS, true, "Option to include a bed in the house");
		WuestUtilities.proxy.proxyConfiguration.addCraftingTable = config.getBoolean(WuestConfiguration.addCraftingTableName, WuestConfiguration.OPTIONS, true, "Option to include a crafting table and a furnace in the house");
		WuestUtilities.proxy.proxyConfiguration.addChest = config.getBoolean(WuestConfiguration.addChestName, WuestConfiguration.OPTIONS, true, "Option to include a chest in the house");
		WuestUtilities.proxy.proxyConfiguration.addChestContents = config.getBoolean(WuestConfiguration.addChestContentsName, WuestConfiguration.OPTIONS, true, "Option to include some starting contents in the chest");
		WuestUtilities.proxy.proxyConfiguration.addFarm = config.getBoolean(WuestConfiguration.addFarmName, WuestConfiguration.OPTIONS , true, "Option to include a small farm outside of the house.");
		WuestUtilities.proxy.proxyConfiguration.floorBlock = CeilingFloorBlockType.ValueOf(config.getInt(WuestConfiguration.floorBlockName, WuestConfiguration.OPTIONS, 0, 0, 2, "Determines the floor material type.\r\n0 = Stone Brick, 1 = Brick, 2 = SandStone"));
		WuestUtilities.proxy.proxyConfiguration.ceilingBlock = CeilingFloorBlockType.ValueOf(config.getInt(WuestConfiguration.ceilingBlockName, WuestConfiguration.OPTIONS, 0, 0, 2, "Determines the ceiling material type.\r\n0 = Stone Brick, 1 = Brick, 2 = SandStone"));
		WuestUtilities.proxy.proxyConfiguration.wallWoodType = WallBlockType.ValueOf(config.getInt(WuestConfiguration.wallWoodTypeName, WuestConfiguration.OPTIONS, 0, 0, 5, "Determines what type of wood the walls (and door/stair) are made of.\r\n0 = Oak, 1 = Spruce, 2 = Birch, 3 = Jungle, 4 = Acacia, 5 = Dark Oak"));
		WuestUtilities.proxy.proxyConfiguration.isCeilingFlat = config.getBoolean(WuestConfiguration.isCeilingFlatName, WuestConfiguration.OPTIONS, true, "Determines if the ceiling is flat or if it is made of stairs.");
		WuestUtilities.proxy.proxyConfiguration.addMineShaft = config.getBoolean(WuestConfiguration.addMineShaftName, WuestConfiguration.OPTIONS, true, "Determines if a mineshaft is built from the house to Y10.\r\nAll blocks broken are added to a chest at the bottom of the shaft.");		
		
		config.setCategoryComment(WuestConfiguration.RecipeOptions, "This category is to turn on or off the various categories of recipes this mod adds.");
		
		// Recipe settings.
		WuestUtilities.proxy.proxyConfiguration.addMetalRecipes = config.getBoolean(WuestConfiguration.addMetalRecipesName, WuestConfiguration.RecipeOptions, true, "Determines if the metal recipes are added. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.addWoodRecipes = config.getBoolean(WuestConfiguration.addWoodRecipesName, WuestConfiguration.RecipeOptions, true, "Determines if the wood recipes are added. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.addStoneRecipes = config.getBoolean(WuestConfiguration.addStoneRecipesName, WuestConfiguration.RecipeOptions, true, "Determines if the stone recipes are added. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.addArmorRecipes = config.getBoolean(WuestConfiguration.addArmorRecipesName, WuestConfiguration.RecipeOptions, true, "Determines if the armor recipes are added. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.addMiscRecipes = config.getBoolean(WuestConfiguration.addMiscRecipesName, WuestConfiguration.RecipeOptions, true, "Determines if the misc recipes are added. Server configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.addNetherStarRecipe = config.getBoolean(WuestConfiguration.addNetherStarRecipeName, WuestConfiguration.RecipeOptions, true, "Determines if the Nether Star recipe are added. Server configuration overrides client.");
		
	    if (config.hasChanged()) 
	    {
	    	config.save();
	    }
	}
	
	public static String GetIntegerOptionStringValue(String name, int value)
	{
		if (name.equals(WuestConfiguration.ceilingBlockName)
				|| name.equals(WuestConfiguration.floorBlockName))
		{
			return " - " + CeilingFloorBlockType.ValueOf(value).name();
		}
		else if (name.equals(WuestConfiguration.wallWoodTypeName))
		{
			return " - " + WallBlockType.ValueOf(value).name();
		}
		
		return "";
	}

	public NBTTagCompound WriteToNBTTagCompound()
	{
		NBTTagCompound tag = new NBTTagCompound();
		
		// This tag should only be written for options which will NOT be overwritten by server options.
		// Server configuration settings will be used for all other options.
		// This is so the server admin can force a player to not use something.
		tag.setBoolean(WuestConfiguration.addTorchesTag, this.addTorches);
		tag.setBoolean(WuestConfiguration.addBedTag, this.addBed);
		tag.setBoolean(WuestConfiguration.addCraftingTableTag, this.addCraftingTable);
		tag.setBoolean(WuestConfiguration.addChestTag, this.addChest);
		tag.setBoolean(WuestConfiguration.addChestContentsTag, this.addChestContents);
		tag.setBoolean(WuestConfiguration.addFarmTag, this.addFarm);
		tag.setInteger(WuestConfiguration.floorBlockTag, this.floorBlock.getValue());
		tag.setInteger(WuestConfiguration.ceilingBlockTag, this.ceilingBlock.getValue());
		tag.setInteger(WuestConfiguration.wallWoodTypeTag, this.wallWoodType.getValue());
		tag.setBoolean(WuestConfiguration.isCeilingFlatTag, this.isCeilingFlat);
		tag.setBoolean(WuestConfiguration.addMineShaftTag, this.addMineShaft);
		
		return tag;
	}
	
	public static WuestConfiguration ReadFromNBTTagCompound(NBTTagCompound tag)
	{
		WuestConfiguration config = new WuestConfiguration();
		
		if (tag.hasKey(WuestConfiguration.addTorchesTag))
		{
			config.addTorches = tag.getBoolean(WuestConfiguration.addTorchesTag);
		}
		
		if (tag.hasKey(WuestConfiguration.addBedTag))
		{
			config.addBed = tag.getBoolean(WuestConfiguration.addBedTag);
		}
		
		if (tag.hasKey(WuestConfiguration.addCraftingTableTag))
		{
			config.addCraftingTable = tag.getBoolean(WuestConfiguration.addCraftingTableTag);
		}
		
		if (tag.hasKey(WuestConfiguration.addChestTag))
		{
			config.addChest = tag.getBoolean(WuestConfiguration.addChestTag);
		}
		
		if (tag.hasKey(WuestConfiguration.addChestContentsTag))
		{
			config.addChestContents = tag.getBoolean(WuestConfiguration.addChestContentsTag);
		}
		
		if (tag.hasKey(WuestConfiguration.addFarmTag))
		{
			config.addFarm = tag.getBoolean(WuestConfiguration.addFarmTag);
		}
		
		if (tag.hasKey(WuestConfiguration.floorBlockTag))
		{
			config.floorBlock = CeilingFloorBlockType.ValueOf(tag.getInteger(WuestConfiguration.floorBlockTag));
		}
		
		if (tag.hasKey(WuestConfiguration.ceilingBlockTag))
		{
			config.ceilingBlock = CeilingFloorBlockType.ValueOf(tag.getInteger(WuestConfiguration.ceilingBlockTag));
		}
		
		if (tag.hasKey(WuestConfiguration.wallWoodTypeTag))
		{
			config.wallWoodType = WallBlockType.ValueOf(tag.getInteger(WuestConfiguration.wallWoodTypeTag));
		}
		
		if (tag.hasKey(WuestConfiguration.isCeilingFlatTag))
		{
			config.isCeilingFlat = tag.getBoolean(WuestConfiguration.isCeilingFlatTag);
		}
		
		if (tag.hasKey(WuestConfiguration.addMineShaftTag))
		{
			config.addMineShaft = tag.getBoolean(WuestConfiguration.addMineShaftTag);
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

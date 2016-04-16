package wuest.utilities.Gui;

import net.minecraftforge.common.config.Configuration;
import wuest.utilities.WuestUtilities;

public class WuestConfiguration 
{
	public static String OPTIONS = "general.options";
	public static String RecipeOptions = "general.options.recipes";
	public static String GuiOptions = "general.options.gui";
	public static String ChestContentOptions = "general.options.chest contents";
	public static String tagKey = "WuestConfig";

	// Config file option names.
	private static String addHouseItemName = "Add House Item On New Player Join";
	private static String rightClickCropHarvestName = "Right Click Crop Harvest";
	private static String enableGrassSpreadToCustomDirtName = "Enable Grass Spreading To Custom Dirt";
	private static String enableHouseGenerationRestrictionName = "Enable House Generation Restrictions";

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

	// Configuration Options.
	public boolean addHouseItem;
	public boolean rightClickCropHarvest;
	public boolean enableHomeCommand;
	public boolean enableGrassSpreadToCustomDirt;
	public boolean enableHouseGenerationRestrictions;

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
		this.enableGrassSpreadToCustomDirt = true;
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
		WuestUtilities.proxy.proxyConfiguration.enableGrassSpreadToCustomDirt = config.getBoolean(WuestConfiguration.enableGrassSpreadToCustomDirtName, WuestConfiguration.OPTIONS, true, "Determines if grass will spread to the custom dirt blocks added by this mod. Sever configuration overrides client.");
		WuestUtilities.proxy.proxyConfiguration.enableHouseGenerationRestrictions = config.getBoolean(WuestConfiguration.enableHouseGenerationRestrictionName, WuestConfiguration.OPTIONS, false, "When true this option causes the Crafting Table, Furnace and Chest to not be added when creating a house, regardless of options chosen. Server Configuration overrides client.");
		
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
		
		// This entire category requires a minecraft restart.
		config.setCategoryRequiresMcRestart(WuestConfiguration.RecipeOptions, true);
		
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

		// GUI Options
		//config.setCategoryComment(WuestConfiguration.GuiOptions, "This category is to configure the various GUI options for this mod.");
		
		if (config.hasChanged()) 
		{
			config.save();
		}
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

package wuest.utilities;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class WuestConfiguration 
{
	public static boolean addHouseItem;
	public static boolean addTorches;
	public static boolean addBed;
	public static boolean addCrafingtable;
	public static boolean addChest;
	public static boolean addChestContents;
	public static boolean addFarm;
	public static boolean rightClickCropHarvest;
	public static int floorBlock;
	public static int ceilingBlock;
	public static int wallWoodType;
	
	public static void syncConfig()
	{
		Configuration config = WuestUtilities.config;
		config.load();
    
		String OPTIONS = "general" + "." + "options";

		WuestConfiguration.addTorches = config.getBoolean("Add Torches", OPTIONS, true, "Option to include torches in the house");
		WuestConfiguration.addBed = config.getBoolean("Add Bed", OPTIONS, true, "Option to include a bed in the house");
		WuestConfiguration.addCrafingtable = config.getBoolean("Add Crafting Table", OPTIONS, true, "Option to include a crafting table and a furnace in the house");
		WuestConfiguration.addChest = config.getBoolean("Add Chest", OPTIONS, true, "Option to include a chest in the house");
		WuestConfiguration.addChestContents = config.getBoolean("Add Chest Contents", OPTIONS, true, "Option to include some starting contents in the chest");
		WuestConfiguration.addFarm = config.getBoolean("Add Farm", OPTIONS , true, "Option to include a small farm outside of the house.");
		WuestConfiguration.rightClickCropHarvest = config.getBoolean("Right Click Crop Harvest", OPTIONS, false, "Determines if right-clicking crops will harvest them.");
		WuestConfiguration.addHouseItem = config.getBoolean("Add House Item On New Player Join", OPTIONS, true, "Determines if the house item is added to player inventory when joining the world for the first time.");
		WuestConfiguration.floorBlock = config.getInt("Floor Stone Type", OPTIONS, 0, 0, 2, "Determines the floor material type.\r\n0 = Stone Brick, 1 = Brick, 2 = SandStone");
		WuestConfiguration.ceilingBlock = config.getInt("Ceiling Stone Type", OPTIONS, 0, 0, 2, "Determines the ceiling material type.\r\n0 = Stone Brick, 1 = Brick, 2 = SandStone");
		WuestConfiguration.wallWoodType = config.getInt("Wall Wood Type", OPTIONS, 0, 0, 5, "Determines what type of wood the walls (and door/stair) are made of.\r\n0 = Oak, 1 = Spruce, 2 = Birch, 3 = Jungle, 4 = Acacia, 5 = Dark Oak");

	    if (config.hasChanged()) 
	    {
	      config.save();
	    }
	}
}

package wuest.utilities;

import net.minecraftforge.common.config.Configuration;

public class WuestConfiguration 
{
	public static boolean addTorches;
	public static boolean addBed;
	public static boolean addCrafingtable;
	public static boolean addChest;
	public static boolean addChestContents;
	public static boolean addFarm;
  
	public static void syncConfig()
	{
		Configuration config = WuestUtilities.config;
		config.load();
    
		String OPTIONS = "general" + "." + "options";

		addTorches = config.getBoolean("Add Torches", OPTIONS, true, "Option to include torches in the house");
		addBed = config.getBoolean("Add Bed", OPTIONS, true, "Option to include a bed in the house");
		addCrafingtable = config.getBoolean("Add Crafting Table", OPTIONS, true, "Option to include a crafting table and a furnace in the house");
		addChest = config.getBoolean("Add Chest", OPTIONS, true, "Option to include a chest in the house");
		addChestContents = config.getBoolean("Add Chest Contents", OPTIONS, true, "Option to include some starting contents in the chest");
		addFarm = config.getBoolean("Add Farm", OPTIONS , true, "Option to include a small farm outside of the house");
		    
	    if (config.hasChanged()) 
	    {
	      config.save();
	    }
	}
}

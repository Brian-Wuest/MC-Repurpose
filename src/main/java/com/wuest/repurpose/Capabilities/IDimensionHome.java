package com.wuest.repurpose.Capabilities;

import java.util.HashMap;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

/**
 * This is the DimensionHome capability. This is used to save the spawn
 * locations for the specified dimension.
 * 
 * @author WuestMan
 *
 */
public interface IDimensionHome extends ITransferable<IDimensionHome> {
	/**
	 * Gets the hashMap of dimensions and their spawn points.
	 * 
	 * @return The map of all saved dimensions and their home points.
	 */
	HashMap<DimensionType, BlockPos> getDimensionHomes();

	/**
	 * Set the hashmap of dimensions and their home positions.
	 * 
	 * @param dimensionHomeMap
	 * @return This instance for easy setting.
	 */
	IDimensionHome setDimensionHomes(HashMap<DimensionType, BlockPos> dimensionHomeMap);

	/**
	 * Gets the home position for this dimension.
	 * 
	 * @param dimensionID The dimension to get the home position for.
	 * @return The home position for this dimension.
	 */
	BlockPos getHomePosition(DimensionType dimensionID);

	/**
	 * Sets the home position for this dimension.
	 * 
	 * @param dimensionID the dimension to set the home position for.
	 * @param pos         The home position for this dimension.
	 * @return This instance for easy setting.
	 */
	IDimensionHome setHomePosition(DimensionType dimensionID, BlockPos pos);
}
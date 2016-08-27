package com.wuest.utilities.Capabilities;

import java.util.HashMap;

import net.minecraft.util.math.BlockPos;

/**
 * This is the dimension home capability which is stored for each player.
 * 
 * @author WuestMan
 *
 */
public class DimensionHome implements IDimensionHome
{
	private HashMap<Integer, BlockPos> dimensionHomes;

	/**
	 * Initializes a new instance of the DimensionHome class.
	 */
	public DimensionHome()
	{
		this.dimensionHomes = new HashMap<Integer, BlockPos>();

	}

	@Override
	public HashMap<Integer, BlockPos> getDimensionHomes()
	{
		return this.dimensionHomes;
	}

	@Override
	public BlockPos getHomePosition(int dimensionID)
	{
		return this.dimensionHomes.get(dimensionID);
	}

	@Override
	public IDimensionHome setHomePosition(int dimensionID, BlockPos pos)
	{
		this.dimensionHomes.put(dimensionID, pos);

		return this;
	}

	@Override
	public IDimensionHome setDimensionHomes(HashMap<Integer, BlockPos> dimensionHomeMap)
	{
		this.dimensionHomes = dimensionHomeMap;
		return this;
	}
}
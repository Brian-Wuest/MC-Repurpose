package com.wuest.repurpose.Capabilities;

import java.util.HashMap;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

/**
 * This is the dimension home capability which is stored for each player.
 * 
 * @author WuestMan
 *
 */
public class DimensionHome implements IDimensionHome
{
	private HashMap<DimensionType, BlockPos> dimensionHomes;

	/**
	 * Initializes a new instance of the DimensionHome class.
	 */
	public DimensionHome()
	{
		this.dimensionHomes = new HashMap<DimensionType, BlockPos>();

	}

	@Override
	public HashMap<DimensionType, BlockPos> getDimensionHomes()
	{
		return this.dimensionHomes;
	}

	@Override
	public BlockPos getHomePosition(DimensionType dimensionID)
	{
		return this.dimensionHomes.get(dimensionID);
	}

	@Override
	public IDimensionHome setHomePosition(DimensionType dimensionID, BlockPos pos)
	{
		this.dimensionHomes.put(dimensionID, pos);

		return this;
	}

	@Override
	public IDimensionHome setDimensionHomes(HashMap<DimensionType, BlockPos> dimensionHomeMap)
	{
		this.dimensionHomes = dimensionHomeMap;
		return this;
	}

	@Override
	public void Transfer(IDimensionHome transferable)
	{
		this.setDimensionHomes(transferable.getDimensionHomes());
	}
}
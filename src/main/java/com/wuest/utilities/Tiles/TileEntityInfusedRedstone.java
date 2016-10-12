package com.wuest.utilities.Tiles;

import com.wuest.utilities.ModRegistry;
import com.wuest.utilities.Base.TileEntityBase;
import com.wuest.utilities.Config.InfusedRedstoneConfig;

/**
 * The tile entity for the infused redstone.
 * @author WuestMan
 */
public class TileEntityInfusedRedstone extends TileEntityBase<InfusedRedstoneConfig>
{
	/**
	 * Initializes a new instance of the TileEntityInfusedRedstone class.
	 */
	public TileEntityInfusedRedstone()
	{
		super();
	}
	
	@Override
	protected void addAllowedCapabilities()
	{
		this.getAllowedCapabilities().add(ModRegistry.BlockModel);
	}
}
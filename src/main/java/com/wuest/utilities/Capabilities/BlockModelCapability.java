package com.wuest.utilities.Capabilities;

import net.minecraft.util.ResourceLocation;

/**
 * The block model capability for the redstone infusion feature.
 * @author WuestMan
 */
public class BlockModelCapability implements IBlockModelCapability
{
	private ResourceLocation blockResourceLocation;
	
	@Override
	public void Transfer(IBlockModelCapability transferable)
	{
		this.setBlockResourceLocation(transferable.getBlockResourceLocation());
		
	}

	@Override
	public ResourceLocation getBlockResourceLocation()
	{
		return this.blockResourceLocation;
	}

	@Override
	public IBlockModelCapability setBlockResourceLocation(ResourceLocation resourceLocation)
	{
		this.blockResourceLocation = resourceLocation;
		return this;
	}

}
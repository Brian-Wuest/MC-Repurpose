package com.wuest.repurpose.Capabilities;

import net.minecraft.init.Blocks;
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
		if (this.blockResourceLocation == null)
		{
			// Return the dirt texture if there isn't one assigned.
			return new ResourceLocation("minecraft", "textures/blocks/dirt.png");
		}
		
		return this.blockResourceLocation;
	}

	@Override
	public IBlockModelCapability setBlockResourceLocation(ResourceLocation resourceLocation)
	{
		this.blockResourceLocation = resourceLocation;
		return this;
	}

}
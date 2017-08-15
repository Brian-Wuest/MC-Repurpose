package com.wuest.repurpose.Capabilities;

import net.minecraft.util.ResourceLocation;

/**
 * The interface for the BlockModelCapability.
 * @author WuestMan
 */
public interface IBlockModelCapability extends ITransferable<IBlockModelCapability>
{
	/**
	 * Gets the resource location for this block model.
	 * @return The resource location for the associated block.
	 */
	ResourceLocation getBlockResourceLocation();
	
	/**
	 * Sets the resource location of the parent block.
	 * @param resourceLocation The resource location of the parent block.
	 * @return The instance of this class for ease of instantiation.
	 */
	IBlockModelCapability setBlockResourceLocation(ResourceLocation resourceLocation);
}
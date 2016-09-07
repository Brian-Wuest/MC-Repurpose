package com.wuest.utilities.Config;

import com.wuest.utilities.Base.BaseConfig;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

/**
 * The tile entity configuration class for the redstone infused tile entity.
 * @author WuestMan
 *
 */
public class InfusedRedstoneConfig extends BaseConfig
{
	private BlockPos pos;

	/**
	 * Initializes a new instance of the InfusedRedstoneConfig class.
	 */
	public InfusedRedstoneConfig()
	{
		
	}
	
	/**
	 * Get's the block pos associated with this configuration.
	 * @return This tile entities block pos.
	 */
	public BlockPos getPos()
	{
		return this.pos;
	}
	
	/**
	 * Sets the block position for this tile entity configuration.
	 * @param pos The block pos for this tile entity configuration.
	 * @return The instance of this class for easy setting.
	 */
	public InfusedRedstoneConfig setBlockPos(BlockPos pos)
	{
		this.pos = pos;
		
		return this;
	}
	
	@Override
	public void WriteToNBTCompound(NBTTagCompound compound)
	{
		NBTTagCompound infusedCompound = new NBTTagCompound();
		
		infusedCompound.setInteger("posX", this.pos.getX());
		infusedCompound.setInteger("posY", this.pos.getY());
		infusedCompound.setInteger("posZ", this.pos.getZ());
		
		compound.setTag("infusedCompound", infusedCompound);
		
	}

	@Override
	public InfusedRedstoneConfig ReadFromNBTTagCompound(NBTTagCompound compound)
	{
		InfusedRedstoneConfig config = new InfusedRedstoneConfig();
		
		if (compound.hasKey("infusedCompound"))
		{
			NBTTagCompound infusedCompound = compound.getCompoundTag("infusedCompound");
			
			config.pos = new BlockPos(infusedCompound.getInteger("posX"), infusedCompound.getInteger("posY"), infusedCompound.getInteger("posZ"));
		}
		
		return config;
	}

}

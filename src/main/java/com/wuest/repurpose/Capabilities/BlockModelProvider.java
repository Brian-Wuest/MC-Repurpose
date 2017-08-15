package com.wuest.repurpose.Capabilities;

import com.wuest.repurpose.ModRegistry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class BlockModelProvider implements ICapabilitySerializable<NBTTagCompound>
{
	private IBlockModelCapability blockModel;
	
	public BlockModelProvider(IBlockModelCapability blockModel)
	{
		this.blockModel = blockModel;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == ModRegistry.BlockModel;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == ModRegistry.BlockModel ? ModRegistry.BlockModel.cast(this.blockModel) : null;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return (NBTTagCompound)ModRegistry.BlockModel.getStorage().writeNBT(ModRegistry.BlockModel, this.blockModel, null);
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		ModRegistry.BlockModel.getStorage().readNBT(ModRegistry.BlockModel, this.blockModel, null, nbt);
	}

}

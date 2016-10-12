package com.wuest.utilities.Capabilities.Storage;

import java.util.HashMap;
import java.util.Map.Entry;

import com.wuest.utilities.Capabilities.IBlockModelCapability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Storage class for the block model capability used in the redstone infusion feature.
 * @author WuestMan
 */
public class BlockModelStorage implements Capability.IStorage<IBlockModelCapability>
{

	@Override
	public NBTBase writeNBT(Capability<IBlockModelCapability> capability, IBlockModelCapability instance, EnumFacing side)
	{
		NBTTagCompound tag = new NBTTagCompound();
		ResourceLocation resourceLocation = instance.getBlockResourceLocation();
		
		if (resourceLocation != null)
		{
			tag.setString("resourcePath", resourceLocation.getResourcePath());
			tag.setString("resourceDomain", resourceLocation.getResourceDomain());
		}
		
		return tag;
	}

	@Override
	public void readNBT(Capability<IBlockModelCapability> capability, IBlockModelCapability instance, EnumFacing side, NBTBase nbt)
	{
		NBTTagCompound tag = (NBTTagCompound) nbt;
		
		if (tag.hasKey("resourcePath"))
		{
			ResourceLocation location = new ResourceLocation(tag.getString("resourceDomain"), tag.getString("resourcePath"));
			instance.setBlockResourceLocation(location);
		}
	}
}
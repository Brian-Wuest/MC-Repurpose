package com.wuest.utilities.Capabilities.Storage;

import java.util.HashMap;
import java.util.Map.Entry;

import com.wuest.utilities.Capabilities.IDimensionHome;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

/**
 * This is the storage class for the DimensionHome capability.
 * 
 * @author WuestMan
 */
public class DimensionHomeStorage implements Capability.IStorage<IDimensionHome>
{
	private String dimensionIDTag = "DimensionID";
	private String posXTag = "posx";
	private String posYTag = "posY";
	private String posZTag = "posZ";

	@Override
	public NBTBase writeNBT(Capability<IDimensionHome> capability, IDimensionHome instance, EnumFacing side)
	{
		NBTTagCompound tag = new NBTTagCompound();

		HashMap<Integer, BlockPos> dimensionHomes = instance.getDimensionHomes();

		// Write each dimension to the tag.
		for (Entry<Integer, BlockPos> entry : dimensionHomes.entrySet())
		{
			NBTTagCompound dimensionTag = new NBTTagCompound();
			dimensionTag.setInteger(posXTag, entry.getValue().getX());
			dimensionTag.setInteger(posYTag, entry.getValue().getY());
			dimensionTag.setInteger(posZTag, entry.getValue().getZ());

			dimensionTag.setInteger(dimensionIDTag, entry.getKey());

			tag.setTag(entry.getKey().toString(), dimensionTag);
		}

		return tag;
	}

	@Override
	public void readNBT(Capability<IDimensionHome> capability, IDimensionHome instance, EnumFacing side, NBTBase nbt)
	{
		NBTTagCompound tag = (NBTTagCompound) nbt;

		for (String key : tag.getKeySet())
		{
			NBTTagCompound dimensionTag = tag.getCompoundTag(key);
			int dimensionID = dimensionTag.getInteger(dimensionIDTag);
			BlockPos pos = new BlockPos(dimensionTag.getInteger(posXTag), dimensionTag.getInteger(posYTag), dimensionTag.getInteger(posZTag));

			instance.setHomePosition(dimensionID, pos);
		}
	}
}
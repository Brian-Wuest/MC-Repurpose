package com.wuest.repurpose.Capabilities.Storage;

import com.wuest.repurpose.Capabilities.IDimensionHome;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * This is the storage class for the DimensionHome capability.
 *
 * @author WuestMan
 */
public class DimensionHomeStorage implements Capability.IStorage<IDimensionHome> {
	private String dimensionIDTag = "DimensionID";
	private String posXTag = "posx";
	private String posYTag = "posY";
	private String posZTag = "posZ";

	@Override
	public INBT writeNBT(Capability<IDimensionHome> capability, IDimensionHome instance, Direction side) {
		CompoundNBT tag = new CompoundNBT();

		HashMap<DimensionType, BlockPos> dimensionHomes = instance.getDimensionHomes();

		// Write each dimension to the tag.
		for (Entry<DimensionType, BlockPos> entry : dimensionHomes.entrySet()) {
			CompoundNBT dimensionTag = new CompoundNBT();
			dimensionTag.putInt(posXTag, entry.getValue().getX());
			dimensionTag.putInt(posYTag, entry.getValue().getY());
			dimensionTag.putInt(posZTag, entry.getValue().getZ());

			// TODO: Fix this later
			//dimensionTag.putInt(dimensionIDTag, entry.getKey().getId());

			tag.put(entry.getKey().toString(), dimensionTag);
		}

		return tag;
	}

	@Override
	public void readNBT(Capability<IDimensionHome> capability, IDimensionHome instance, Direction side, INBT nbt) {
		CompoundNBT tag = (CompoundNBT) nbt;

		for (String key : tag.keySet()) {
			CompoundNBT dimensionTag = tag.getCompound(key);
			int dimensionID = dimensionTag.getInt(dimensionIDTag);
			BlockPos pos = new BlockPos(dimensionTag.getInt(posXTag), dimensionTag.getInt(posYTag),
					dimensionTag.getInt(posZTag));


			// TODO: Fix this later
			//DimensionType dimensionType = DimensionType.getById(dimensionID);
			//instance.setHomePosition(dimensionType, pos);
		}
	}
}
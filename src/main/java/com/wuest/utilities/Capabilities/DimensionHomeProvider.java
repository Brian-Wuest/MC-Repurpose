package com.wuest.utilities.Capabilities;

import com.wuest.utilities.ModRegistry;
import com.wuest.utilities.Capabilities.*;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * The capability provider for the DimensionHome capability.
 * This class MUST implement INBTSerializable in order for the capability to be saved when the world is saved.
 * @author WuestMan
 */
public class DimensionHomeProvider implements ICapabilitySerializable<NBTTagCompound>
{
	private Entity entity;
	private IDimensionHome dimensionHome;

	/**
	 * Initializes a new instance of the DimensionHomeProvider class.
	 * 
	 * @param entity The entity to associated with the capability.
	 * @param dimensionHome The capability associated with the entity.
	 */
	public DimensionHomeProvider(Entity entity, IDimensionHome dimensionHome)
	{
		this.entity = entity;
		this.dimensionHome = dimensionHome;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == ModRegistry.DimensionHomes;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == ModRegistry.DimensionHomes ? ModRegistry.DimensionHomes.cast(this.dimensionHome) : null;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return (NBTTagCompound)ModRegistry.DimensionHomes.getStorage().writeNBT(ModRegistry.DimensionHomes, this.dimensionHome, null);
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		ModRegistry.DimensionHomes.getStorage().readNBT(ModRegistry.DimensionHomes, this.dimensionHome, null, nbt);
	}
}
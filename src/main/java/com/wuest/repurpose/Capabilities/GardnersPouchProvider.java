package com.wuest.repurpose.Capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class GardnersPouchProvider	implements ICapabilityProvider, ICapabilitySerializable
{
	private final ItemStackHandler inventory;

	public GardnersPouchProvider()
	{
		this.inventory = new ItemStackHandler(54);
	}

	@Override
	public NBTBase serializeNBT()
	{
		return this.inventory.serializeNBT();
	}

	@Override
	public void deserializeNBT(NBTBase nbt)
	{
		this.inventory.deserializeNBT((NBTTagCompound) nbt);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return true;
		}
		
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return (T) inventory;
		}
		
		return null;
	}
}

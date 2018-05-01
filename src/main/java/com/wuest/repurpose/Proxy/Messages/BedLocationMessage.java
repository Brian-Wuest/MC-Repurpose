package com.wuest.repurpose.Proxy.Messages;

import net.minecraft.nbt.NBTTagCompound;

public class BedLocationMessage extends TagMessage
{
	/**
	 * This class is just here to distinguish the bed location tag message from other messages in the mod.
	 */
	public BedLocationMessage(NBTTagCompound writeToNBTTagCompound) 
	{
		super(writeToNBTTagCompound);
	}

	public BedLocationMessage()
	{
		super();
	}
}

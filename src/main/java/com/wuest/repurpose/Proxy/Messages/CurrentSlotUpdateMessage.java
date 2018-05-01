package com.wuest.repurpose.Proxy.Messages;

import net.minecraft.nbt.NBTTagCompound;

public class CurrentSlotUpdateMessage extends TagMessage
{
	public CurrentSlotUpdateMessage(NBTTagCompound writeToNBTTagCompound) 
	{
		super(writeToNBTTagCompound);
	}

	public CurrentSlotUpdateMessage()
	{
		super();
	}
}

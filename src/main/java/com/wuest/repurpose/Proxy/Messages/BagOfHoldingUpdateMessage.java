package com.wuest.repurpose.Proxy.Messages;

import net.minecraft.nbt.NBTTagCompound;

public class BagOfHoldingUpdateMessage extends TagMessage
{
	public BagOfHoldingUpdateMessage(NBTTagCompound writeToNBTTagCompound) 
	{
		super(writeToNBTTagCompound);
	}

	public BagOfHoldingUpdateMessage()
	{
		super();
	}
}

package com.wuest.repurpose.Proxy.Messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class TagMessage implements IMessage
{
	private NBTTagCompound tagMessage;

	public TagMessage() 
	{
	}

	public TagMessage(NBTTagCompound tagMessage) 
	{
		this.tagMessage = tagMessage;
	}

	public NBTTagCompound getMessageTag()
	{
		return this.tagMessage;
	}

	public void setMessageTag(NBTTagCompound value)
	{
		this.tagMessage = value;
	}

	@Override
	public void fromBytes(ByteBuf buf) 
	{
		// This class is very useful in general for writing more complex
		// objects.
		this.tagMessage = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		ByteBufUtils.writeTag(buf, this.tagMessage);
	}
}

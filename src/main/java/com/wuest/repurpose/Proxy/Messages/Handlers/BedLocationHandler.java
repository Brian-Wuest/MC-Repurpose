package com.wuest.repurpose.Proxy.Messages.Handlers;

import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Proxy.ClientProxy;
import com.wuest.repurpose.Proxy.Messages.BedLocationMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BedLocationHandler implements
IMessageHandler<BedLocationMessage, IMessage>
{
	@Override
	public IMessage onMessage(final BedLocationMessage message,
			final MessageContext ctx) 
	{
		// Or Minecraft.getMinecraft() on the client.
		IThreadListener mainThread = null;
		
		if (ctx.side.isClient())
		{
			mainThread = Minecraft.getMinecraft();
		}
		else
		{
			mainThread = (WorldServer) ctx.getServerHandler().player.world;
		}
		
		mainThread.addScheduledTask(new Runnable() 
		{
			@Override
			public void run() 
			{
				if (ctx.side.isClient())
				{
					// This is server side.
					NBTTagCompound tag = message.getMessageTag();
					
					if (tag != null && tag.hasKey("bedX"))
					{
						((ClientProxy)Repurpose.proxy).clientEventHandler.bedLocation = new BlockPos(tag.getInteger("bedX"), tag.getInteger("bedY"), tag.getInteger("bedZ"));
					}
					else
					{
						((ClientProxy)Repurpose.proxy).clientEventHandler.bedLocation = null;
					}
				}
			}
		});

		return null;
	}
}

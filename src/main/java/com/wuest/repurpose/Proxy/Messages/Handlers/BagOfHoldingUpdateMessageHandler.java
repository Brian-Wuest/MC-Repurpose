package com.wuest.repurpose.Proxy.Messages.Handlers;

import com.wuest.repurpose.Capabilities.ItemBagOfHoldingProvider;
import com.wuest.repurpose.Items.ItemBagOfHolding;
import com.wuest.repurpose.Proxy.Messages.BagOfHoldingUpdateMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BagOfHoldingUpdateMessageHandler implements
IMessageHandler<BagOfHoldingUpdateMessage, IMessage>
{
	@Override
	public IMessage onMessage(final BagOfHoldingUpdateMessage message,
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
					EntityPlayerSP player = Minecraft.getMinecraft().player;
					
					if (tag != null)
					{
						ItemStack stack = player.getHeldItemOffhand();
						
						if (!stack.isEmpty() && stack.getItem() instanceof ItemBagOfHolding)
						{
							ItemBagOfHoldingProvider.UpdateStackFromNbt(stack, tag);
						}
					}
				}
			}
		});

		return null;
	}
}

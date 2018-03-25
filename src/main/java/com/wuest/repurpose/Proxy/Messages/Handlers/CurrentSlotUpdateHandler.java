package com.wuest.repurpose.Proxy.Messages.Handlers;

import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Items.ItemBagOfHolding;
import com.wuest.repurpose.Proxy.ClientProxy;
import com.wuest.repurpose.Proxy.Messages.CurrentSlotUpdateMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CurrentSlotUpdateHandler implements
IMessageHandler<CurrentSlotUpdateMessage, IMessage>
{
	@Override
	public IMessage onMessage(final CurrentSlotUpdateMessage message,
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
				if (ctx.side.isServer())
				{
					// This is server side.
					NBTTagCompound tag = message.getMessageTag();
					EntityPlayerMP player = ctx.getServerHandler().player;
					
					if (tag != null && tag.hasKey("slot"))
					{
						int currentSlot = tag.getInteger("slot");
						ItemStack stack = player.getHeldItemOffhand();
						
						if (!stack.isEmpty() && stack.getItem() instanceof ItemBagOfHolding)
						{
							ItemBagOfHolding.setCurrentSlotForStack(stack, currentSlot);
						}
					}
				}
			}
		});

		return null;
	}
}

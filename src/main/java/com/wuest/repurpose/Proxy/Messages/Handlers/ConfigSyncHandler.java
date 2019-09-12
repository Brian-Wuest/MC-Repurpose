package com.wuest.repurpose.Proxy.Messages.Handlers;

import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.UpdateChecker;
import com.wuest.repurpose.Config.ModConfiguration;
import com.wuest.repurpose.Proxy.ClientProxy;
import com.wuest.repurpose.Proxy.Messages.ConfigSyncMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class ConfigSyncHandler implements
IMessageHandler<ConfigSyncMessage, IMessage>
{
	@Override
	public IMessage onMessage(final ConfigSyncMessage message,
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
				// This is client side. Update the configuration.
				((ClientProxy)Repurpose.proxy).serverConfiguration =  ModConfiguration.getFromNBTTagCompound(message.getMessageTag());
				
				ModConfiguration config = ((ClientProxy)Repurpose.proxy).getServerConfiguration();
				
				// Show a message to this player if their version is old.
				if (config.showMessage && config.enableVersionCheckMessage)
				{
					Minecraft.getMinecraft().player.sendMessage(new TextComponentString(config.versionMessage));
				}
			}
		});

		// no response in this case
		return null;
	}
}
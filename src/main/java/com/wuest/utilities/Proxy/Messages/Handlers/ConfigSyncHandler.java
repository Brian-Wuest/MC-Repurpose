package com.wuest.utilities.Proxy.Messages.Handlers;

import com.wuest.utilities.UpdateChecker;
import com.wuest.utilities.WuestUtilities;
import com.wuest.utilities.Config.WuestConfiguration;
import com.wuest.utilities.Proxy.ClientProxy;
import com.wuest.utilities.Proxy.Messages.ConfigSyncMessage;

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
			mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
		} 

		mainThread.addScheduledTask(new Runnable()
		{
			@Override
			public void run() 
			{
				// This is client side. Update the configuration.
				((ClientProxy)WuestUtilities.proxy).serverConfiguration =  WuestConfiguration.getFromNBTTagCompound(message.getMessageTag());
				
				WuestConfiguration config = ((ClientProxy)WuestUtilities.proxy).getServerConfiguration();
				
				// Show a message to this player if their version is old.
				if (config.showMessage && config.enableVersionCheckMessage)
				{
					Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString(config.versionMessage));
				}
			}
		});

		// no response in this case
		return null;
	}
}
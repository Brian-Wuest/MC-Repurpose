package wuest.utilities.Proxy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import wuest.utilities.Gui.WuestConfiguration;

public class WuestHandler implements
IMessageHandler<TagMessage, IMessage> 
{
	@Override
	public IMessage onMessage(final TagMessage message, final MessageContext ctx) 
	{
		// Or Minecraft.getMinecraft() on the client.
		IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj; 

		mainThread.addScheduledTask(new Runnable() 
		{
			@Override
			public void run() 
			{
				// This is server side. Write the tag to the player entity to be reteived later.
				System.out.println(String.format("WuestUtilities: Player specific configuration packet received from %s", 
						ctx.getServerHandler().playerEntity.getDisplayName().getUnformattedText()));

				// Set this player's configuration tag. This way the players(client) configuration overrides the servers.
				NBTTagCompound tag = ctx.getServerHandler().playerEntity.getEntityData();
				tag.setTag(WuestConfiguration.tagKey, message.getMessageTag());
			}
		});

		// no response in this case
		return null;
	}
}
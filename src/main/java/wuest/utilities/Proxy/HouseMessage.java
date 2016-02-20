package wuest.utilities.Proxy;

import io.netty.buffer.ByteBuf;
import wuest.utilities.Gui.HouseConfiguration;
import wuest.utilities.Gui.WuestConfiguration;
import wuest.utilities.Items.ItemStartHouse;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HouseMessage implements IMessage
{
	private NBTTagCompound tagMessage;

	public HouseMessage() 
	{
	}

	public HouseMessage(NBTTagCompound tagMessage) 
	{
		this.tagMessage = tagMessage;
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

	public static class HouseHandler implements
			IMessageHandler<HouseMessage, IMessage> 
	{
		@Override
		public IMessage onMessage(final HouseMessage message,
				final MessageContext ctx) 
		{
			// Or Minecraft.getMinecraft() on the client.
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj; 

			mainThread.addScheduledTask(new Runnable() 
			{
				@Override
				public void run() 
				{
					// This is server side. Build the house.
					HouseConfiguration configuration = HouseConfiguration.ReadFromNBTTagCompound(message.tagMessage);
					ItemStartHouse.BuildHouse(ctx.getServerHandler().playerEntity, ctx.getServerHandler().playerEntity.worldObj, configuration);
				}
			});

			// no response in this case
			return null;
		}
	}
}

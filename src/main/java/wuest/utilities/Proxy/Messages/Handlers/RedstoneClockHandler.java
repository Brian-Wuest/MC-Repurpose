package wuest.utilities.Proxy.Messages.Handlers;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import wuest.utilities.Blocks.RedstoneClock;
import wuest.utilities.Config.*;
import wuest.utilities.Proxy.Messages.RedstoneClockMessage;
import wuest.utilities.Tiles.TileEntityRedstoneClock;

public class RedstoneClockHandler implements
IMessageHandler<RedstoneClockMessage, IMessage> 
{
	@Override
	public IMessage onMessage(final RedstoneClockMessage message,
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
				RedstoneClockPowerConfiguration configuration = null;
				
				try
				{
					configuration = RedstoneClockPowerConfiguration.class.newInstance().ReadFromNBTTagCompound(message.getMessageTag());
				}
				catch (InstantiationException e)
				{
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}

				World world = ctx.getServerHandler().playerEntity.worldObj;
				IBlockState state = world.getBlockState(configuration.getPos());
				Block block = state.getBlock();

				if (block.getClass() == RedstoneClock.class)
				{
					TileEntity tileEntity = world.getTileEntity(configuration.getPos());

					if (tileEntity != null && tileEntity.getClass() == TileEntityRedstoneClock.class)
					{
						((TileEntityRedstoneClock)tileEntity).setConfig(configuration);
					}

					// Make sure the block updates.
					world.scheduleUpdate(configuration.getPos(), block, 2);
				}
			}
		});

		// no response in this case
		return null;
	}
}

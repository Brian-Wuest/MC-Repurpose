package wuest.utilities.Proxy;

import wuest.utilities.Blocks.RedstoneClock;
import wuest.utilities.Blocks.RedstoneClock.PowerConfiguration;
import wuest.utilities.Gui.HouseConfiguration;
import wuest.utilities.Items.ItemStartHouse;
import wuest.utilities.Tiles.TileEntityRedstoneClock;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
				PowerConfiguration configuration = PowerConfiguration.ReadFromNBTTagCompound(message.getMessageTag());
				
				World world = ctx.getServerHandler().playerEntity.worldObj;
				IBlockState state = world.getBlockState(configuration.getPos());
				Block block = state.getBlock();
				
				if (block.getClass() == RedstoneClock.class)
				{
					TileEntity tileEntity = world.getTileEntity(configuration.getPos());
					
					if (tileEntity != null && tileEntity.getClass() == TileEntityRedstoneClock.class)
					{
						((TileEntityRedstoneClock)tileEntity).setPowerConfiguration(configuration);
						((RedstoneClock)block).localTileEntity = (TileEntityRedstoneClock) tileEntity;
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

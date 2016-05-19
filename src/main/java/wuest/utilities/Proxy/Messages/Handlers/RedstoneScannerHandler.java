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
import wuest.utilities.Blocks.BlockRedstoneScanner;
import wuest.utilities.Blocks.RedstoneClock;
import wuest.utilities.Config.*;
import wuest.utilities.Proxy.Messages.RedstoneClockMessage;
import wuest.utilities.Proxy.Messages.RedstoneScannerMessage;
import wuest.utilities.Tiles.TileEntityRedstoneClock;
import wuest.utilities.Tiles.TileEntityRedstoneScanner;

public class RedstoneScannerHandler implements
IMessageHandler<RedstoneScannerMessage, IMessage> 
{
	@Override
	public IMessage onMessage(final RedstoneScannerMessage message,
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
				RedstoneScannerConfig configuration = RedstoneScannerConfig.ReadFromNBTTagCompound(message.getMessageTag());

				World world = ctx.getServerHandler().playerEntity.worldObj;
				IBlockState state = world.getBlockState(configuration.getBlockPos());
				Block block = state.getBlock();

				if (block.getClass() == BlockRedstoneScanner.class)
				{
					TileEntity tileEntity = world.getTileEntity(configuration.getBlockPos());

					if (tileEntity != null && tileEntity.getClass() == TileEntityRedstoneScanner.class)
					{
						((TileEntityRedstoneScanner)tileEntity).setConfig(configuration);
					}

					// Make sure the block updates.
					world.scheduleUpdate(configuration.getBlockPos(), block, 2);
				}
			}
		});

		// no response in this case
		return null;
	}
}
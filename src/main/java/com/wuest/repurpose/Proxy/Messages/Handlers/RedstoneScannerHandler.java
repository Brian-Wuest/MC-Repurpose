package com.wuest.repurpose.Proxy.Messages.Handlers;

import com.wuest.repurpose.Blocks.BlockRedstoneScanner;
import com.wuest.repurpose.Config.RedstoneScannerConfig;
import com.wuest.repurpose.Proxy.Messages.RedstoneScannerMessage;
import com.wuest.repurpose.Tiles.TileEntityRedstoneScanner;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RedstoneScannerHandler implements
IMessageHandler<RedstoneScannerMessage, IMessage> 
{
	@Override
	public IMessage onMessage(final RedstoneScannerMessage message,
			final MessageContext ctx) 
	{
		// Or Minecraft.getMinecraft() on the client.
		IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.world; 

		mainThread.addScheduledTask(new Runnable() 
		{
			@Override
			public void run() 
			{
				// This is server side. Build the house.
				RedstoneScannerConfig configuration = null;
				
				try
				{
					configuration = RedstoneScannerConfig.class.newInstance().ReadFromNBTTagCompound(message.getMessageTag());
				}
				catch (InstantiationException e)
				{
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}

				World world = ctx.getServerHandler().player.world;
				IBlockState state = world.getBlockState(configuration.getBlockPos());
				Block block = state.getBlock();

				if (block.getClass() == BlockRedstoneScanner.class)
				{
					TileEntity tileEntity = world.getTileEntity(configuration.getBlockPos());

					if (tileEntity != null && tileEntity.getClass() == TileEntityRedstoneScanner.class)
					{
						((TileEntityRedstoneScanner)tileEntity).setConfig(configuration);
					}
					else
					{
						TileEntityRedstoneScanner scanner = new TileEntityRedstoneScanner();
						scanner.setConfig(configuration);
						scanner.setPos(configuration.getBlockPos());
						
						// Make sure that the tile exists at this position on the server.
						world.setTileEntity(configuration.getBlockPos(), scanner);
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
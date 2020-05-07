package com.wuest.repurpose.Proxy.Messages.Handlers;

import com.wuest.repurpose.Blocks.BlockRedstoneScanner;
import com.wuest.repurpose.Config.RedstoneScannerConfig;
import com.wuest.repurpose.Proxy.Messages.RedstoneScannerMessage;
import com.wuest.repurpose.Tiles.TileEntityRedstoneScanner;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class RedstoneScannerHandler {

	public static void handle(final RedstoneScannerMessage message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();

		context.enqueueWork(() -> {
			// This is server side. Build the house.
			RedstoneScannerConfig configuration = null;

			try {
				configuration = RedstoneScannerConfig.class.newInstance().ReadFromCompoundNBT(message.getMessageTag());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			World world = context.getSender().getEntityWorld();
			BlockState state = world.getBlockState(configuration.getBlockPos());
			Block block = state.getBlock();

			if (block.getClass() == BlockRedstoneScanner.class) {
				TileEntity tileEntity = world.getTileEntity(configuration.getBlockPos());

				if (tileEntity != null && tileEntity.getClass() == TileEntityRedstoneScanner.class) {
					((TileEntityRedstoneScanner) tileEntity).setConfig(configuration);
				} else {
					TileEntityRedstoneScanner scanner = new TileEntityRedstoneScanner();
					scanner.setConfig(configuration);
					scanner.setPos(configuration.getBlockPos());

					// Make sure that the tile exists at this position on the server.
					world.setTileEntity(configuration.getBlockPos(), scanner);
				}

				// Make sure the block updates.
				world.getPendingBlockTicks().scheduleTick(configuration.getBlockPos(), block, 2);
			}
		});

		context.setPacketHandled(true);
	}
}
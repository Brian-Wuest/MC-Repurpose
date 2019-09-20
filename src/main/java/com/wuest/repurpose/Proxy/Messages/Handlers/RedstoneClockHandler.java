package com.wuest.repurpose.Proxy.Messages.Handlers;

import java.util.function.Supplier;

import javax.xml.ws.handler.MessageContext;

import com.wuest.repurpose.Blocks.RedstoneClock;
import com.wuest.repurpose.Config.RedstoneClockPowerConfiguration;
import com.wuest.repurpose.Proxy.Messages.RedstoneClockMessage;
import com.wuest.repurpose.Tiles.TileEntityRedstoneClock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class RedstoneClockHandler {
	public static void handle(final RedstoneClockMessage message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();

		context.enqueueWork(() -> {
			// This is server side. Build the house.
			RedstoneClockPowerConfiguration configuration = null;

			try {
				configuration = RedstoneClockPowerConfiguration.class.newInstance()
						.ReadFromCompoundNBT(message.getMessageTag());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			World world = context.getSender().getEntityWorld();
			BlockState state = world.getBlockState(configuration.getPos());
			Block block = state.getBlock();

			if (block.getClass() == RedstoneClock.class) {
				TileEntity tileEntity = world.getTileEntity(configuration.getPos());

				if (tileEntity != null && tileEntity.getClass() == TileEntityRedstoneClock.class) {
					((TileEntityRedstoneClock) tileEntity).setConfig(configuration);
				}

				// Make sure the block updates.
				world.getPendingBlockTicks().scheduleTick(configuration.getPos(), block, 2);
			}
		});

		context.setPacketHandled(true);
	}
}

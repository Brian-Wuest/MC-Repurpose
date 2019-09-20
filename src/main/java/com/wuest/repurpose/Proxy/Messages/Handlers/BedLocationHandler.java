package com.wuest.repurpose.Proxy.Messages.Handlers;

import java.util.function.Supplier;

import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Proxy.ClientProxy;
import com.wuest.repurpose.Proxy.Messages.BedLocationMessage;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

public class BedLocationHandler {
	public static void handle(final BedLocationMessage message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();

		context.enqueueWork(() -> {
			if (context.getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
				// This is server side.
				CompoundNBT tag = message.getMessageTag();

				if (tag != null && tag.contains("bedX")) {
					((ClientProxy) Repurpose.proxy).clientEventHandler.bedLocation = new BlockPos(tag.getInt("bedX"),
							tag.getInt("bedY"), tag.getInt("bedZ"));
				} else {
					((ClientProxy) Repurpose.proxy).clientEventHandler.bedLocation = null;
				}
			}
		});

		context.setPacketHandled(true);
	}
}

package com.wuest.repurpose.Proxy.Messages.Handlers;

import java.util.function.Supplier;

import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Config.ModConfiguration;
import com.wuest.repurpose.Proxy.ClientProxy;
import com.wuest.repurpose.Proxy.Messages.ConfigSyncMessage;

import net.minecraftforge.fml.network.NetworkEvent;

public class ConfigSyncHandler {
	public static void handle(final ConfigSyncMessage message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();

		context.enqueueWork(() -> {
			// This is client side. Update the configuration.
			((ClientProxy) Repurpose.proxy).serverConfiguration = ModConfiguration
					.getFromNBTTagCompound(message.getMessageTag());
		});

		// no response in this case
		context.setPacketHandled(true);
	}
}
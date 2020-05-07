package com.wuest.repurpose.Proxy.Messages.Handlers;

import com.wuest.repurpose.Config.ModConfiguration;
import com.wuest.repurpose.Proxy.ClientProxy;
import com.wuest.repurpose.Proxy.Messages.ConfigSyncMessage;
import com.wuest.repurpose.Repurpose;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

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
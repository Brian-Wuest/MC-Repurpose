package com.wuest.repurpose.Proxy.Messages.Handlers;

import java.util.function.Supplier;

import com.wuest.repurpose.Capabilities.ItemBagOfHoldingProvider;
import com.wuest.repurpose.Items.ItemBagOfHolding;
import com.wuest.repurpose.Proxy.Messages.BagOfHoldingUpdateMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

public class BagOfHoldingUpdateMessageHandler {

	public static void handle(final BagOfHoldingUpdateMessage message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();

		context.enqueueWork(() -> {
			if (context.getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
				// This is server side.
				CompoundNBT tag = message.getMessageTag();
				ClientPlayerEntity player = Minecraft.getInstance().player;

				if (tag != null) {
					ItemStack stack = player.getHeldItemOffhand();

					if (!stack.isEmpty() && stack.getItem() instanceof ItemBagOfHolding) {
						ItemBagOfHoldingProvider.UpdateStackFromNbt(stack, tag);
					}
				}
			}
		});

		context.setPacketHandled(true);
	}
}

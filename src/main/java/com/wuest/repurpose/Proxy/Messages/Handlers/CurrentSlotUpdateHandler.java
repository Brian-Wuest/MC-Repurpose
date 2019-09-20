package com.wuest.repurpose.Proxy.Messages.Handlers;

import java.util.function.Supplier;

import com.wuest.repurpose.Items.ItemBagOfHolding;
import com.wuest.repurpose.Proxy.Messages.CurrentSlotUpdateMessage;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

public class CurrentSlotUpdateHandler {

	public static void handle(final CurrentSlotUpdateMessage message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();

		context.enqueueWork(() -> {
			if (context.getDirection() == NetworkDirection.PLAY_TO_SERVER) {
				// This is server side.
				CompoundNBT tag = message.getMessageTag();
				ServerPlayerEntity player = context.getSender();

				if (tag != null && tag.contains("slot")) {
					int currentSlot = tag.getInt("slot");
					ItemStack stack = player.getHeldItemOffhand();

					if (!stack.isEmpty() && stack.getItem() instanceof ItemBagOfHolding) {
						ItemBagOfHolding.setCurrentSlotForStack(player, stack, currentSlot);
					}
				}
			}
		});

		context.setPacketHandled(true);
	}
}

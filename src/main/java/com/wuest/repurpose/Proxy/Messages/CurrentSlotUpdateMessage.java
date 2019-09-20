package com.wuest.repurpose.Proxy.Messages;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public class CurrentSlotUpdateMessage extends TagMessage {
	public CurrentSlotUpdateMessage(CompoundNBT writeToNBTTagCompound) {
		super(writeToNBTTagCompound);
	}

	public CurrentSlotUpdateMessage() {
		super();
	}

	public static CurrentSlotUpdateMessage decode(PacketBuffer buf) {
		return TagMessage.decode(buf, CurrentSlotUpdateMessage.class);
	}
}

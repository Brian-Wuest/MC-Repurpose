package com.wuest.repurpose.Proxy.Messages;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public class BagOfHoldingUpdateMessage extends TagMessage {
	public BagOfHoldingUpdateMessage(CompoundNBT writeToNBTTagCompound) {
		super(writeToNBTTagCompound);
	}

	public BagOfHoldingUpdateMessage() {
		super();
	}

	public static BagOfHoldingUpdateMessage decode(PacketBuffer buf) {
		return TagMessage.decode(buf, BagOfHoldingUpdateMessage.class);
	}
}

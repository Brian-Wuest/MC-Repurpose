package com.wuest.repurpose.Proxy.Messages;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public class BedLocationMessage extends TagMessage {
	/**
	 * This class is just here to distinguish the bed location tag message from
	 * other messages in the mod.
	 */
	public BedLocationMessage(CompoundNBT writeToNBTTagCompound) {
		super(writeToNBTTagCompound);
	}

	public BedLocationMessage() {
		super();
	}

	public static BedLocationMessage decode(PacketBuffer buf) {
		return TagMessage.decode(buf, BedLocationMessage.class);
	}
}

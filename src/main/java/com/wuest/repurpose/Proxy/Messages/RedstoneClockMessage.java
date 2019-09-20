package com.wuest.repurpose.Proxy.Messages;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public class RedstoneClockMessage extends TagMessage {
	/**
	 * This class is just here to distinguish the redstone clock tag message from
	 * other messages in the mod.
	 */
	public RedstoneClockMessage(CompoundNBT compound) {
		super(compound);
	}

	public RedstoneClockMessage() {
		super();
	}

	public static RedstoneClockMessage decode(PacketBuffer buf) {
		return TagMessage.decode(buf, RedstoneClockMessage.class);
	}
}

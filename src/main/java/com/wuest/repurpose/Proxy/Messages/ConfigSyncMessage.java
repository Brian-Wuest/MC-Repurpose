package com.wuest.repurpose.Proxy.Messages;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public class ConfigSyncMessage extends TagMessage {
	/**
	 * This class is just here to distinguish the configuration sync message from
	 * other messages in the mod.
	 */
	public ConfigSyncMessage(CompoundNBT writeToNBTTagCompound) {
		super(writeToNBTTagCompound);
	}

	public ConfigSyncMessage() {
		super();
	}

	public static ConfigSyncMessage decode(PacketBuffer buf) {
        return TagMessage.decode(buf, ConfigSyncMessage.class);
    }
}

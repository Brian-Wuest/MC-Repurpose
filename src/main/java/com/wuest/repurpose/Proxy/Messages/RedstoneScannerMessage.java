package com.wuest.repurpose.Proxy.Messages;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

/**
 * The unique message for the RedstoneScanner.
 *
 * @author WuestMan
 */
public class RedstoneScannerMessage extends TagMessage {
	/**
	 * This class is just here to distinguish the redstone clock tag message from
	 * other messages in the mod.
	 */
	public RedstoneScannerMessage(CompoundNBT compound) {
		super(compound);
	}

	/**
	 * Initializes a new instance of the RedstoneScannerMessage class.
	 */
	public RedstoneScannerMessage() {
		super();
	}

	public static RedstoneScannerMessage decode(PacketBuffer buf) {
		return TagMessage.decode(buf, RedstoneScannerMessage.class);
	}
}
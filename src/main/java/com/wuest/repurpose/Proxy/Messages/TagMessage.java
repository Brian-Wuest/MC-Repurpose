package com.wuest.repurpose.Proxy.Messages;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public class TagMessage {
	protected CompoundNBT tagMessage;

	public TagMessage() {
	}

	public TagMessage(CompoundNBT tagMessage) {
		this.tagMessage = tagMessage;
	}

	public CompoundNBT getMessageTag() {
		return this.tagMessage;
	}

	public void setMessageTag(CompoundNBT value) {
		this.tagMessage = value;
	}

	public static <T extends TagMessage> T decode(PacketBuffer buf, Class<T> clazz) {
		T message = null;

		try {
			message = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assert message != null;
		message.tagMessage = buf.readCompoundTag();
		return message;
	}

	public static <T extends TagMessage> void encode(T message, PacketBuffer buf) {
		buf.writeCompoundTag(message.tagMessage);
	}
}

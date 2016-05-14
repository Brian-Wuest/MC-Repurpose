package wuest.utilities.Proxy.Messages;

import net.minecraft.nbt.NBTTagCompound;

public class RedstoneClockMessage extends TagMessage 
{
	/**
	 * This class is just here to distinguish the redstone clock tag message from other messages in the mod.
	 */
	public RedstoneClockMessage(NBTTagCompound compound)
	{
		super(compound);
	}

	public RedstoneClockMessage()
	{
		super();
	}
}

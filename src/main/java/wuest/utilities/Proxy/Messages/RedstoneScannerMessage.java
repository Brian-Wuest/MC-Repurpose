package wuest.utilities.Proxy.Messages;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The unique message for the RedstoneScanner.
 * @author WuestMan
 *
 */
public class RedstoneScannerMessage extends TagMessage
{
	/**
	 * This class is just here to distinguish the redstone clock tag message from other messages in the mod.
	 */
	public RedstoneScannerMessage(NBTTagCompound compound)
	{
		super(compound);
	}

	/**
	 * Initializes a new instance of the RedstoneScannerMessage class.
	 */
	public RedstoneScannerMessage()
	{
		super();
	}
}
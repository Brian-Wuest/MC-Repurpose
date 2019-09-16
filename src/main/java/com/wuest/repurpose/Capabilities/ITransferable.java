package com.wuest.repurpose.Capabilities;

/**
 * Transfers property data from one capability to another.
 * @author WuestMan
 *
 */
public interface ITransferable<T>
{
	/**
	 * Transfers properties from one transferable to another.
	 * @param transferable The other transferable to get the properties from.
	 */
	void Transfer(T transferable);
}

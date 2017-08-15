package com.wuest.repurpose.Items;

import com.wuest.repurpose.ModRegistry;

import net.minecraft.item.ItemShears;

/**
 * 
 * @author WuestMan
 *
 */
public class ItemStoneShears extends ItemShears
{
	/**
	 * Initializes a new instance of the ItemStoneShears class.
	 * @param name The name of the item in the registry.
	 */
	public ItemStoneShears(String name)
	{
		super();
		this.setMaxDamage(124);
		ModRegistry.setItemName(this, name);
	}
}

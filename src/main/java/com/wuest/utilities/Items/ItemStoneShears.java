package com.wuest.utilities.Items;

import com.wuest.utilities.ModRegistry;

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
		this.setMaxDamage(179);
		ModRegistry.setItemName(this, name);
	}
}

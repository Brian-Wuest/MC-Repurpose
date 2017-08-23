package com.wuest.repurpose.Items;

import com.wuest.repurpose.ModRegistry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

/**
 * 
 * @author WuestMan
 *
 */
public class ItemIronLump extends Item
{
	public ItemIronLump(String name)
	{
		this.setCreativeTab(CreativeTabs.MATERIALS);
		ModRegistry.setItemName(this, name);
	}
}

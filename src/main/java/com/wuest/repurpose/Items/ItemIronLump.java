package com.wuest.repurpose.Items;

import com.wuest.repurpose.ModRegistry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

/**
 * 
 * @author WuestMan
 *
 */
public class ItemIronLump extends Item {
	public ItemIronLump(String name) {
		super(new Item.Properties().group(ItemGroup.MATERIALS));
		ModRegistry.setItemName(this, name);
	}
}

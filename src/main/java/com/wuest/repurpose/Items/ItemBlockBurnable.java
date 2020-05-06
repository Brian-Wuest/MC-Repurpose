package com.wuest.repurpose.Items;

import com.wuest.repurpose.ModRegistry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

/**
 * 
 * @author WuestMan
 *
 */
public class ItemBlockBurnable extends BlockItem {
	private int burnTime;

	public ItemBlockBurnable(Block block, ItemGroup itemGroup) {
		super(block, new Item.Properties().group(itemGroup));
	}

	public ItemBlockBurnable setBurnTime(int burnTime) {
		this.burnTime = burnTime;
		return this;
	}

	/**
	 * @return the fuel burn time for this itemStack in a furnace. Return 0 to make
	 *         it not act as a fuel. Return -1 to let the default vanilla logic
	 *         decide.
	 */
	@Override
	public int getBurnTime(ItemStack itemStack) {
		return this.burnTime;
	}
}

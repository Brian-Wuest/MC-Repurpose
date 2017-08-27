package com.wuest.repurpose.Items;

import com.wuest.repurpose.ModRegistry;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * 
 * @author WuestMan
 *
 */
public class ItemBlockBurnable extends ItemBlock
{
	private int burnTime;
	
	public ItemBlockBurnable(Block block)
	{
		super(block);
		ModRegistry.setItemName(this, block.getRegistryName().getResourcePath());
	}

	
	public ItemBlockBurnable setBurnTime(int burnTime)
	{
		this.burnTime = burnTime;
		return this;
	}
	
    /**
     * @return the fuel burn time for this itemStack in a furnace.
     * Return 0 to make it not act as a fuel.
     * Return -1 to let the default vanilla logic decide.
     */
    public int getItemBurnTime(ItemStack itemStack)
    {
    	return this.burnTime;
    }
}

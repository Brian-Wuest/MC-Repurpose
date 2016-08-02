package com.wuest.utilities.Blocks;

import com.wuest.utilities.ModRegistry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockHalfDirtSlab extends BlockDirtSlab
{
	@Override
	public boolean isDouble()
	{
		return false;
	}
	
	@Override
    protected ItemStack createStackedBlock(IBlockState state)
    {
        Item item = Item.getItemFromBlock(ModRegistry.DirtSlab());

        if (item == null)
        {
            return null;
        }
        else
        {
            int i = 0;

            if (item.getHasSubtypes())
            {
                i = this.getMetaFromState(this.getDefaultState());
            }

            return new ItemStack(item, 1, i);
        }
    }
}

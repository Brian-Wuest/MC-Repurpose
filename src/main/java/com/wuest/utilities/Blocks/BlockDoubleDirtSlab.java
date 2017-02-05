package com.wuest.utilities.Blocks;

import com.wuest.utilities.ModRegistry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockDoubleDirtSlab extends BlockDirtSlab
{
	@Override
	public boolean isDouble()
	{
		return true;
	}
}
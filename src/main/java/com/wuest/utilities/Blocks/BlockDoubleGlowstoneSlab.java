package com.wuest.utilities.Blocks;

import com.wuest.utilities.ModRegistry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockDoubleGlowstoneSlab extends BlockGlowstoneSlab
{
	@Override
	public boolean isDouble()
	{
		return true;
	}
}

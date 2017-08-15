package com.wuest.repurpose.Blocks;

import com.wuest.repurpose.ModRegistry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockDoubleGrassSlab extends BlockGrassSlab
{
	@Override
	public boolean isDouble()
	{
		return true;
	}
}

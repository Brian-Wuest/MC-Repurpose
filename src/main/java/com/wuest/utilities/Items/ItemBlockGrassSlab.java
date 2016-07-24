package com.wuest.utilities.Items;

import com.wuest.utilities.Blocks.BlockDoubleDirtSlab;
import com.wuest.utilities.Blocks.BlockDoubleGrassSlab;
import com.wuest.utilities.Blocks.BlockHalfDirtSlab;
import com.wuest.utilities.Blocks.BlockHalfGrassSlab;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

public class ItemBlockGrassSlab extends ItemSlab
{
	public ItemBlockGrassSlab(Block block, BlockHalfGrassSlab slab, BlockDoubleGrassSlab doubleSlab, Boolean stacked)
	{
		super(block, slab, doubleSlab);
	}
}

package com.wuest.utilities.Items;

import com.wuest.utilities.Blocks.BlockDoubleDirtSlab;
import com.wuest.utilities.Blocks.BlockHalfDirtSlab;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

public class ItemBlockDirtSlab extends ItemSlab
{
	public ItemBlockDirtSlab(Block block, BlockHalfDirtSlab slab, BlockDoubleDirtSlab doubleSlab, Boolean stacked)
	{
		super(block, slab, doubleSlab);
	}
}

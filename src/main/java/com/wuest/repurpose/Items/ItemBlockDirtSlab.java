package com.wuest.repurpose.Items;

import com.wuest.repurpose.Blocks.BlockDoubleDirtSlab;
import com.wuest.repurpose.Blocks.BlockHalfDirtSlab;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

public class ItemBlockDirtSlab extends ItemSlab
{
	public ItemBlockDirtSlab(Block block, BlockHalfDirtSlab slab, BlockDoubleDirtSlab doubleSlab, Boolean stacked)
	{
		super(block, slab, doubleSlab);
	}
}

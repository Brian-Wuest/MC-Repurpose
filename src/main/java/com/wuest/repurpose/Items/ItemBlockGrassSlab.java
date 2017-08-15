package com.wuest.repurpose.Items;

import com.wuest.repurpose.Blocks.BlockDoubleDirtSlab;
import com.wuest.repurpose.Blocks.BlockDoubleGrassSlab;
import com.wuest.repurpose.Blocks.BlockHalfDirtSlab;
import com.wuest.repurpose.Blocks.BlockHalfGrassSlab;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

public class ItemBlockGrassSlab extends ItemSlab
{
	public ItemBlockGrassSlab(Block block, BlockHalfGrassSlab slab, BlockDoubleGrassSlab doubleSlab, Boolean stacked)
	{
		super(block, slab, doubleSlab);
	}
}

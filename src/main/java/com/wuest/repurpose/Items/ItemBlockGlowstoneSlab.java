package com.wuest.repurpose.Items;

import com.wuest.repurpose.Blocks.BlockDoubleGlowstoneSlab;
import com.wuest.repurpose.Blocks.BlockHalfGlowstoneSlab;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

public class ItemBlockGlowstoneSlab extends ItemSlab
{
	public ItemBlockGlowstoneSlab(Block block, BlockHalfGlowstoneSlab slab, BlockDoubleGlowstoneSlab doubleSlab, Boolean stacked)
	{
		super(block, slab, doubleSlab);
	}
}

package com.wuest.utilities.Items;

import com.wuest.utilities.Blocks.BlockDoubleGlowstoneSlab;
import com.wuest.utilities.Blocks.BlockHalfGlowstoneSlab;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

public class ItemBlockGlowstoneSlab extends ItemSlab
{
	public ItemBlockGlowstoneSlab(Block block, BlockHalfGlowstoneSlab slab, BlockDoubleGlowstoneSlab doubleSlab, Boolean stacked)
	{
		super(block, slab, doubleSlab);
	}
}

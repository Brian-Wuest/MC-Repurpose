package wuest.utilities.Items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;
import wuest.utilities.Blocks.BlockDoubleDirtSlab;
import wuest.utilities.Blocks.BlockHalfDirtSlab;

public class ItemBlockDirtSlab extends ItemSlab
{
	public ItemBlockDirtSlab(Block block, BlockHalfDirtSlab slab, BlockDoubleDirtSlab doubleSlab, Boolean stacked)
	{
		super(block, slab, doubleSlab);
	}
}

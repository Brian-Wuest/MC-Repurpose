package wuest.utilities.Items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;
import wuest.utilities.Blocks.BlockDoubleDirtSlab;
import wuest.utilities.Blocks.BlockDoubleGrassSlab;
import wuest.utilities.Blocks.BlockHalfDirtSlab;
import wuest.utilities.Blocks.BlockHalfGrassSlab;

public class ItemBlockGrassSlab extends ItemSlab
{
	public ItemBlockGrassSlab(Block block, BlockHalfGrassSlab slab, BlockDoubleGrassSlab doubleSlab, Boolean stacked)
	{
		super(block, slab, doubleSlab);
	}
}

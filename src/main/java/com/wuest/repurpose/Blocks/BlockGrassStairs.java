package com.wuest.repurpose.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;

/**
 * This class defines a set of grass stairs.
 *
 * @author WuestMan
 */
public class BlockGrassStairs extends StairsBlock implements IModBlock {
	public BlockGrassStairs() {
		super(Blocks.GRASS_BLOCK.getDefaultState(), Block.Properties.from(Blocks.GRASS_BLOCK));
	}
}

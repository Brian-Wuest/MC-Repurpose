package com.wuest.repurpose.Blocks;

import java.util.Random;

import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Repurpose;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This class is used to define a set of dirt stairs.
 * 
 * @author WuestMan
 *
 */
public class BlockDirtStairs extends StairsBlock implements IModBlock {
	/**
	 * Initializes a new instance of the BlockDirtStairs class.
	 * 
	 * @param modelState
	 */
	public BlockDirtStairs() {
		super(Blocks.DIRT.getDefaultState(), Block.Properties.from(Blocks.GRASS_BLOCK));
		ModRegistry.setBlockName(this, "block_dirt_stairs");
	}

	/**
	 * Returns whether or not this block is of a type that needs random ticking.
	 * Called for ref-counting purposes by ExtendedBlockStorage in order to broadly
	 * cull a chunk from the random chunk update list for efficiency's sake.
	 */
	@Override
	public boolean ticksRandomly(BlockState state) {
		return true;
	}

	@Override
	public void randomTick(BlockState state, World worldIn, BlockPos pos, Random random) {
		if (!worldIn.isRemote && Repurpose.proxy.proxyConfiguration.enableGrassSpreadToCustomDirt) {
			if (worldIn.getLight(pos.up()) >= 9) {
				for (int i = 0; i < 4; ++i) {
					BlockPos blockpos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);

					if (blockpos.getY() >= 0 && blockpos.getY() < 256 && !worldIn.isBlockLoaded(blockpos)) {
						return;
					}

					BlockState iblockstate1 = worldIn.getBlockState(blockpos);

					if ((iblockstate1.getBlock() == Blocks.GRASS_BLOCK
							|| iblockstate1.getBlock() == ModRegistry.GrassStairs()
							|| iblockstate1.getBlock() == ModRegistry.GrassWall()
							|| iblockstate1.getBlock() == ModRegistry.GrassSlab())
							&& worldIn.getLight(blockpos.up()) >= 4) {
						BlockState grassStairsState = ModRegistry.GrassStairs().getDefaultState()
								.with(StairsBlock.FACING, state.get(StairsBlock.FACING))
								.with(StairsBlock.HALF, state.get(StairsBlock.HALF))
								.with(StairsBlock.SHAPE, state.get(StairsBlock.SHAPE));
						worldIn.setBlockState(pos, grassStairsState, 3);
					}
				}
			}
		}
	}
}
package com.wuest.repurpose.Blocks;

import java.util.Random;

import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Repurpose;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public abstract class BlockDirtSlab extends SlabBlock implements IModBlock {
	public BlockDirtSlab() {
		super(Block.Properties.create(Material.EARTH, MaterialColor.DIRT).sound(SoundType.GROUND)
				.hardnessAndResistance(0.5f, 0.5f).harvestTool(ToolType.SHOVEL).harvestLevel(0));
		ModRegistry.setBlockName(this, "block_dirt_slab");
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
						BlockState grassSlabsState = null;

						grassSlabsState = ModRegistry.GrassSlab().getDefaultState().with(SlabBlock.TYPE,
								state.get(SlabBlock.TYPE));

						worldIn.setBlockState(pos, grassSlabsState, 3);
					}
				}
			}
		}
	}
}

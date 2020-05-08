package com.wuest.repurpose.Blocks;

import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Proxy.CommonProxy;
import com.wuest.repurpose.Repurpose;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import java.util.Random;

public class BlockDirtSlab extends SlabBlock implements IModBlock {
	public BlockDirtSlab() {
		super(Block.Properties.create(Material.EARTH, MaterialColor.DIRT).sound(SoundType.GROUND)
				.hardnessAndResistance(0.5f, 0.5f).harvestTool(ToolType.SHOVEL).harvestLevel(0));
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
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if (!worldIn.isRemote && CommonProxy.proxyConfiguration.enableGrassSpreadToCustomDirt) {
			if (worldIn.getLight(pos.up()) >= 9) {
				for (int i = 0; i < 4; ++i) {
					BlockPos blockpos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);

					if (blockpos.getY() >= 0 && blockpos.getY() < 256 && !worldIn.isBlockLoaded(blockpos)) {
						return;
					}

					BlockState iblockstate1 = worldIn.getBlockState(blockpos);

					if ((iblockstate1.getBlock() == Blocks.GRASS_BLOCK
							|| iblockstate1.getBlock() == ModRegistry.GrassStairs.get()
							|| iblockstate1.getBlock() == ModRegistry.GrassWall.get()
							|| iblockstate1.getBlock() == ModRegistry.GrassSlab.get())
							&& worldIn.getLight(blockpos.up()) >= 4) {
						BlockState grassSlabsState = null;

						grassSlabsState = ModRegistry.GrassSlab.get().getDefaultState().with(SlabBlock.TYPE,
								state.get(SlabBlock.TYPE));

						worldIn.setBlockState(pos, grassSlabsState, 3);
					}
				}
			}
		}
	}
}

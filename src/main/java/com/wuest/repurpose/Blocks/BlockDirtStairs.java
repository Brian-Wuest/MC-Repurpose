package com.wuest.repurpose.Blocks;

import java.util.Random;

import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Proxy.CommonProxy;

import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * This class is used to define a set of dirt stairs.
 * @author WuestMan
 *
 */
public class BlockDirtStairs extends BlockStairs
{
	/**
	 * Initializes a new instance of the BlockDirtStairs class.
	 * @param modelState
	 */
	public BlockDirtStairs()
	{
		super(Blocks.DIRT.getDefaultState());
		this.setTickRandomly(true);
		this.useNeighborBrightness = true;
		this.setHardness(0.5F);
		this.setHarvestLevel("shovel", 0);
		ModRegistry.setBlockName(this, "blockdirtstairs");
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote && Repurpose.proxy.proxyConfiguration.enableGrassSpreadToCustomDirt)
		{
			if (worldIn.getLightFromNeighbors(pos.up()) >= 9)
			{
				for (int i = 0; i < 4; ++i)
				{
					BlockPos blockpos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);

					if (blockpos.getY() >= 0 && blockpos.getY() < 256 && !worldIn.isBlockLoaded(blockpos))
					{
						return;
					}

					IBlockState iblockstate = worldIn.getBlockState(blockpos.up());
					IBlockState iblockstate1 = worldIn.getBlockState(blockpos);

					if ((iblockstate1.getBlock() == Blocks.GRASS
							|| iblockstate1.getBlock() == ModRegistry.GrassStairs()
							|| iblockstate1.getBlock() == ModRegistry.GrassWall()
							|| iblockstate1.getBlock() == ModRegistry.GrassSlab()
							|| iblockstate1.getBlock() == ModRegistry.DoubleGrassSlab())
							&& worldIn.getLightFromNeighbors(blockpos.up()) >= 4)
					{
						IBlockState grassStairsState = ModRegistry.GrassStairs().getStateFromMeta(this.getMetaFromState(state));
						worldIn.setBlockState(pos, grassStairsState, 3);
					}
				}
			}
		}
	}
}
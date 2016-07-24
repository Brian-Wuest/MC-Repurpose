package com.wuest.utilities.Blocks;

import java.util.Random;

import com.wuest.utilities.WuestUtilities;
import com.wuest.utilities.Proxy.CommonProxy;

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
	public static BlockDirtStairs RegisteredBlock;

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
		CommonProxy.setBlockName(this, "blockDirtStairs");
	}

	/**
	 * Registers this block in the game registry and sets the static field.
	 */
	public static void RegisterBlock()
	{
		BlockDirtStairs.RegisteredBlock = CommonProxy.registerBlock(new BlockDirtStairs());

		// Register the block and add the recipe for it.
		GameRegistry.addRecipe(
				new ItemStack(BlockDirtStairs.RegisteredBlock, 4),
				"  x",
				" xx",
				"xxx",
				'x', Blocks.DIRT);

		GameRegistry.addRecipe(
				new ItemStack(Blocks.DIRT, 3),
				"x",
				"x",
				'x', BlockDirtStairs.RegisteredBlock);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote && WuestUtilities.proxy.proxyConfiguration.enableGrassSpreadToCustomDirt)
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
							|| iblockstate1.getBlock() == BlockGrassStairs.RegisteredBlock
							|| iblockstate1.getBlock() == BlockCustomWall.RegisteredGrassBlock
							|| iblockstate1.getBlock() == BlockGrassSlab.RegisteredHalfBlock
							|| iblockstate1.getBlock() == BlockGrassSlab.RegisteredDoubleSlab)
							&& worldIn.getLightFromNeighbors(blockpos.up()) >= 4)
					{
						IBlockState grassStairsState = BlockGrassStairs.RegisteredBlock.getStateFromMeta(this.getMetaFromState(state));
						worldIn.setBlockState(pos, grassStairsState, 3);
					}
				}
			}
		}
	}
}
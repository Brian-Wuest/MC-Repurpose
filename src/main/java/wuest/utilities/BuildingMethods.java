package wuest.utilities;

import java.lang.*;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import wuest.utilities.Gui.HouseConfiguration;
import wuest.utilities.Items.ItemStartHouse;

/**
 * This class is used to hold he generalized building methods used by the
 * starting house.
 * 
 * @author WuestMan
 *
 */
public class BuildingMethods
{
	/**
	 * Clears the given space without returning any of the drops.
	 * 
	 * @param world - The world used to clear the space.
	 * @param startingPosition - The starting position, note the width, depth
	 *            will be used to determine the starting corner(s).
	 * @param width - The radius x-axis of how wide to clear. (East/West)
	 * @param height - How high from the starting position to clear (this
	 *            includes starting position).
	 * @param depth - The radius z-axis of how deep to clear. (North/South)
	 */
	public static void ClearSpace(World world, BlockPos startingPosition, int width, int height, int depth)
	{
		BlockPos northSide = startingPosition.north(depth).east(width);
		int wallLength = depth;
		int clearedWidth = width;
		northSide = northSide.east();

		for (int i = 0; i < clearedWidth; i++)
		{
			northSide = northSide.west();

			BuildingMethods.CreateWall(world, height, wallLength, EnumFacing.SOUTH, northSide, Blocks.air);
		}
	}

	public static ArrayList<ItemStack> ConsolidateDrops(Block block, World world, BlockPos pos, IBlockState state, ArrayList<ItemStack> originalStacks)
	{
		for (ItemStack stack : block.getDrops(world, pos, state, 1))
		{
			// Check to see if this stack's item is equal to an existing item
			// stack. If it is just add the count.
			Boolean foundStack = false;

			for (ItemStack existingStack : originalStacks)
			{
				if (ItemStack.areItemsEqual(existingStack, stack))
				{
					// Make sure that this combined stack is at or smaller than
					// the max.
					if (existingStack.stackSize + stack.stackSize <= stack.getMaxStackSize())
					{
						existingStack.stackSize = existingStack.stackSize + stack.stackSize;
						foundStack = true;
						break;
					}
				}
			}

			if (!foundStack)
			{
				originalStacks.add(stack);
			}
		}

		return originalStacks;
	}

	public static ArrayList<ItemStack> CreateWall(World world, int height, int length, EnumFacing direction, BlockPos startingPosition, Block replacementBlock)
	{
		ArrayList<ItemStack> itemsDropped = new ArrayList<ItemStack>();

		BlockPos wallPos = null;

		// i height, j is the actual wall counter.
		for (int i = 0; i < height; i++)
		{
			// Reset wall building position to the starting position up by the
			// height counter.
			wallPos = startingPosition.up(i);

			for (int j = 0; j < length; j++)
			{
				for (ItemStack stack : world.getBlockState(wallPos).getBlock().getDrops(world, wallPos, world.getBlockState(wallPos), 1))
				{
					itemsDropped.add(stack);
				}

				// j is the north/south counter.
				BuildingMethods.ReplaceBlock(world, wallPos, replacementBlock);

				wallPos = wallPos.offset(direction);
			}
		}

		return itemsDropped;
	}

	public static ArrayList<ItemStack> CreateWall(World world, int height, int length, EnumFacing direction, BlockPos startingPosition,
			IBlockState replacementBlock)
	{
		ArrayList<ItemStack> itemsDropped = new ArrayList<ItemStack>();

		BlockPos wallPos = null;

		// i height, j is the actual wall counter.
		for (int i = 0; i < height; i++)
		{
			// Reset wall building position to the starting position up by the
			// height counter.
			wallPos = startingPosition.up(i);

			for (int j = 0; j < length; j++)
			{
				for (ItemStack stack : world.getBlockState(wallPos).getBlock().getDrops(world, wallPos, world.getBlockState(wallPos), 1))
				{
					itemsDropped.add(stack);
				}

				// j is the north/south counter.
				BuildingMethods.ReplaceBlock(world, wallPos, replacementBlock);

				wallPos = wallPos.offset(direction);
			}
		}

		return itemsDropped;
	}

	public static ArrayList<ItemStack> SetFloor(World world, BlockPos pos, Block block, int width, int depth, ArrayList<ItemStack> originalStack)
	{
		pos = pos.north((int) Math.floor(depth / 2)).east((int) Math.floor(width / 2));

		for (int i = 0; i <= width; i++)
		{
			originalStack.addAll(BuildingMethods.CreateWall(world, 1, depth, EnumFacing.SOUTH, pos, block));

			pos = pos.west();
		}

		return originalStack;
	}

	public static void SetCeiling(World world, BlockPos pos, Block block, int width, int depth, Block stairs, HouseConfiguration configuration, EnumFacing houseFacing)
	{
		// If the ceiling is flat, call SetFloor since it's laid out the same.
		if (configuration.isCeilingFlat)
		{
			BuildingMethods.SetFloor(world, pos, block, width, depth, new ArrayList<ItemStack>());
			return;
		}

		// Get to the north east corner.
		pos = pos.north(4).east(4);

		// Get the stairs state without the facing since it will change.
		IBlockState stateWithoutFacing = stairs.getBlockState().getBaseState().withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM)
				.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT);

		int wallLength = 7;

		while (wallLength > 0)
		{
			for (int j = 0; j < 4; j++)
			{
				// I is the wall side starting on the east side.
				EnumFacing facing = EnumFacing.WEST;
				EnumFacing flowDirection = EnumFacing.SOUTH;

				switch (j)
				{
					case 1:
					{
						facing = EnumFacing.NORTH;
						flowDirection = EnumFacing.WEST;
						break;
					}

					case 2:
					{
						facing = EnumFacing.EAST;
						flowDirection = EnumFacing.NORTH;
						break;
					}

					case 3:
					{
						facing = EnumFacing.SOUTH;
						flowDirection = EnumFacing.EAST;
						break;
					}
				}

				for (int k = 0; k <= wallLength; k++)
				{
					// j is the north/south counter.
					BuildingMethods.ReplaceBlock(world, pos, stateWithoutFacing.withProperty(BlockStairs.FACING, facing));

					pos = pos.offset(flowDirection);
				}
			}

			pos = pos.west().south().up();
			wallLength = wallLength - 2;
		}

		BuildingMethods.ReplaceBlock(world, pos, block);

		IBlockState blockState = Blocks.torch.getStateFromMeta(5);
		BuildingMethods.ReplaceBlock(world, pos.up(), blockState);
	}

	public static void ReplaceBlock(World world, BlockPos pos, Block replacementBlock)
	{
		BuildingMethods.ReplaceBlock(world, pos, replacementBlock.getDefaultState(), 3);
	}

	public static void ReplaceBlock(World world, BlockPos pos, IBlockState replacementBlockState)
	{
		BuildingMethods.ReplaceBlock(world, pos, replacementBlockState, 3);
	}

	public static void ReplaceBlock(World world, BlockPos pos, IBlockState replacementBlockState, int flags)
	{
		world.setBlockToAir(pos);
		world.setBlockState(pos, replacementBlockState, flags);
	}
	
	/**
	 * Gets the integer representation of a torch facing.
	 * @param facing The facing that the torch should look.
	 * @return The integer used for the block state of a BlockTorch object.
	 */
	public static int GetTorchFacing(EnumFacing facing)
	{
		/*
		 * Torch Facings: 1 = East, 2 = West, 3 = South, 4 = North, 5 = Up
		 */
		switch (facing)
		{
			case NORTH:
			{
				return 4;
			}
			
			case EAST:
			{
				return 1;
			}
			
			case SOUTH:
			{
				return 3;
			}
			
			case WEST:
			{
				return 2;
			}
			
			default:
			{
				return 5;
			}
		}
	}
}

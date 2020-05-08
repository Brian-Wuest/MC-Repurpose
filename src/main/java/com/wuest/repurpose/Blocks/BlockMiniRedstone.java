package com.wuest.repurpose.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class BlockMiniRedstone extends RedstoneBlock implements IModBlock {
	protected static final AxisAlignedBB bounds = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.50D, 0.75D);
	protected static final VoxelShape shape = VoxelShapes.create(BlockMiniRedstone.bounds);

	/**
	 * Initializes a new instance of the BlockMiniRedstone.
	 */
	public BlockMiniRedstone() {
		super(Block.Properties.create(Material.CLAY, MaterialColor.TNT)
				.hardnessAndResistance(.5f, 10.0f).sound(SoundType.METAL));
	}

	public ItemGroup getItemGroup() {
		return ItemGroup.REDSTONE;
	}

	/**
	 * @deprecated call via
	 * {@link IBlockState#getWeakPower(IBlockAccess, BlockPos, EnumFacing)}
	 * whenever possible. Implementing/overriding is fine.
	 */
	@Override
	public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
		return 8;
	}

	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return BlockMiniRedstone.shape;
	}
}

package com.wuest.repurpose.Blocks;

import com.wuest.repurpose.ModRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;;

public class BlockMiniRedstone extends RedstoneBlock implements IModBlock {
	protected static final AxisAlignedBB bounds = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.50D, 0.75D);
	protected static final VoxelShape shape = VoxelShapes.create(BlockMiniRedstone.bounds);

	/**
	 * Initializes a new instance of the BlockMiniRedstone.
	 */
	public BlockMiniRedstone() {
		super(Block.Properties.create(Material.IRON, MaterialColor.TNT).harvestLevel(0).harvestTool(null)
				.hardnessAndResistance(.5f, 10.0f).sound(SoundType.METAL));

		ModRegistry.setBlockName(this, "block_mini_redstone");
	}

	public ItemGroup getItemGroup() {
		return ItemGroup.REDSTONE;
	}

	/**
	 * @deprecated call via
	 *             {@link IBlockState#getWeakPower(IBlockAccess,BlockPos,EnumFacing)}
	 *             whenever possible. Implementing/overriding is fine.
	 */
	public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
		return 15;
	}

	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return BlockMiniRedstone.shape;
	}

	/**
	 * Gets the render layer this block will render on. SOLID for solid blocks,
	 * CUTOUT or CUTOUT_MIPPED for on-off transparency (glass, reeds), TRANSLUCENT
	 * for fully blended transparency (stained glass)
	 */
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}
}

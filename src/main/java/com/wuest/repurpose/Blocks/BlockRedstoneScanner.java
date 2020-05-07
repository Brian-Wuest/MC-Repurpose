package com.wuest.repurpose.Blocks;

import com.wuest.repurpose.Base.TileBlockBase;
import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Tiles.TileEntityRedstoneScanner;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

/**
 * This class is to provide a way to scan for entities and if a particular
 * entity was found, generate a redstone signal.
 *
 * @author WuestMan
 */
public class BlockRedstoneScanner extends TileBlockBase<TileEntityRedstoneScanner> {
	public static final BooleanProperty POWERED = BooleanProperty.create("powered");

	protected static final AxisAlignedBB BOUNDING_AABB = new AxisAlignedBB(0.0625, 0.00001D, 0.0625D, 0.9375, 0.625D,
			0.9375D);

	protected static final VoxelShape shape = VoxelShapes.create(BlockRedstoneScanner.BOUNDING_AABB);

	protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.125D, 0.0625D, 0.125D, 0.875, 0.1875, 0.875D);
	protected static final AxisAlignedBB STICK1_AABB = new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.3125D, 0.625D,
			0.3125D);
	protected static final AxisAlignedBB STICK2_AABB = new AxisAlignedBB(0.6875D, 0.0D, 0.6875D, 0.8125D, 0.625D,
			0.8125D);
	protected static final AxisAlignedBB STICK3_AABB = new AxisAlignedBB(0.1875D, 0.0D, 0.6875D, 0.3125D, 0.625D,
			0.8125D);
	protected static final AxisAlignedBB STICK4_AABB = new AxisAlignedBB(0.6875D, 0.0D, 0.1875D, 0.8125D, 0.625D,
			0.3125D);

	protected int tickRate = 20;

	/**
	 * Initializes a new instance of the BlockMiniRedstone.
	 */
	public BlockRedstoneScanner() {
		super(Block.Properties.create(Material.CLAY, MaterialColor.TNT).hardnessAndResistance(.5f, 10.0f)
				.sound(SoundType.METAL), TileEntityRedstoneScanner.TileType);

		this.setDefaultState(this.stateContainer.getBaseState().with(POWERED, Boolean.valueOf(true)));
	}

	public ItemGroup getItemGroup() {
		return ItemGroup.REDSTONE;
	}

	/*
	 * @Override public void addCollisionBoxToList(BlockState state, World worldIn,
	 * BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB>
	 * collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
	 * this.addCollisionBoxToList(pos, entityBox, collidingBoxes, STICK1_AABB);
	 * this.addCollisionBoxToList(pos, entityBox, collidingBoxes, STICK2_AABB);
	 * this.addCollisionBoxToList(pos, entityBox, collidingBoxes, STICK3_AABB);
	 * this.addCollisionBoxToList(pos, entityBox, collidingBoxes, STICK4_AABB);
	 * this.addCollisionBoxToList(pos, entityBox, collidingBoxes, BASE_AABB); }
	 */
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return BlockRedstoneScanner.shape;
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public int getStrongPower(BlockState blockState, IBlockReader worldIn, BlockPos pos, Direction side) {
		TileEntityRedstoneScanner tileEntity = this.getLocalTileEntity((World) worldIn, pos);

		return tileEntity.getRedstoneStrength();
	}

	@Override
	public int getWeakPower(BlockState blockState, IBlockReader worldIn, BlockPos pos, Direction side) {
		TileEntityRedstoneScanner tileEntity = this.getLocalTileEntity((World) worldIn, pos);

		return tileEntity.getRedstoneStrength();
	}

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
									BlockRayTraceResult hit) {
		if (worldIn.isRemote) {
			Repurpose.proxy.openGuiForBlock(state, worldIn, pos, player, handIn);
		}

		return true;
	}

	/**
	 * How many world ticks before ticking
	 */
	@Override
	public int tickRate(IWorldReader worldIn) {
		return this.tickRate;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BlockRedstoneScanner.POWERED);
	}

	/**
	 * Processes custom update state.
	 *
	 * @param worldIn    The world this state is being updated in.
	 * @param pos        The block position.
	 * @param state      The block state.
	 * @param tileEntity The tile entity associated with this class.
	 * @return The number of ticks to delay until the next update.
	 */
	@Override
	public int customUpdateState(World worldIn, BlockPos pos, BlockState state, TileEntityRedstoneScanner tileEntity) {
		state = tileEntity.setRedstoneStrength(state);
		worldIn.setBlockState(pos, state, 3);

		this.updateNeighbors(worldIn, pos);

		return tileEntity.getTickDelay();
	}

	@Override
	public void customBreakBlock(TileEntityRedstoneScanner tileEntity, World worldIn, BlockPos pos, BlockState state) {
		if (tileEntity.getRedstoneStrength() > 0) {
			this.updateNeighbors(worldIn, pos);
		}
	}

	/**
	 * Notify block and block below of changes
	 */
	public void updateNeighbors(World worldIn, BlockPos pos) {
		this.notifyNeighborsOfStateChange(worldIn, pos, this);
		this.notifyNeighborsOfStateChange(worldIn, pos.down(), this);
	}

	public void notifyNeighborsOfStateChange(World worldIn, BlockPos pos, Block blockType) {
		if (net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(worldIn, pos, worldIn.getBlockState(pos),
				java.util.EnumSet.allOf(Direction.class), true).isCanceled()) {
			return;
		}

		this.updateNeighbors(worldIn.getBlockState(pos), worldIn, pos, 3);
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip,
							   ITooltipFlag advanced) {
		super.addInformation(stack, worldIn, tooltip, advanced);

		boolean advancedKeyDown = Screen.hasShiftDown();

		if (!advancedKeyDown) {
			tooltip.add(new StringTextComponent(
					"Hold" + TextFormatting.BLUE + " Shift " + TextFormatting.GRAY + "for advanced information."));
		} else {
			tooltip.add(new StringTextComponent(
					"This configurable machine is able to connect to nearby redstone sensitive devices, and send a signal to them based on nearby entities. Right Click to configure."));
		}
	}
}
package com.wuest.repurpose.Blocks;

import com.wuest.repurpose.Base.TileBlockBase;
import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Tiles.TileEntityRedstoneClock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
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

public class RedstoneClock extends TileBlockBase<TileEntityRedstoneClock> implements IModBlock {
	public static final BooleanProperty POWERED = BooleanProperty.create("powered");
	protected int tickRate = 20;

	/**
	 * A simple block that emits redstone signals at regular intervals.
	 */
	public RedstoneClock() {
		super(Block.Properties.create(Material.CLAY, MaterialColor.TNT)
				.hardnessAndResistance(.5f, 10.0f), null);
		this.setDefaultState(this.stateContainer.getBaseState().with(POWERED, Boolean.TRUE));
	}

	public ItemGroup getItemGroup() {
		return ItemGroup.REDSTONE;
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
	public int getStrongPower(BlockState blockState, IBlockReader worldIn, BlockPos pos, Direction side) {
		TileEntityRedstoneClock tileEntity = this.getLocalTileEntity((World) worldIn, pos);

		return tileEntity.getRedstoneStrength(blockState, side);
	}

	@Override
	public int getWeakPower(BlockState blockState, IBlockReader worldIn, BlockPos pos, Direction side) {
		TileEntityRedstoneClock tileEntity = this.getLocalTileEntity((World) worldIn, pos);

		return tileEntity.getRedstoneStrength(blockState, side);
	}

	@Override
	public void customBreakBlock(TileEntityRedstoneClock tileEntity, World worldIn, BlockPos pos, BlockState state) {
		if (tileEntity.getRedstoneStrength(state, null) > 0) {
			this.updateNeighbors(worldIn, pos);
		}
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(RedstoneClock.POWERED);
	}

	/**
	 * Notify block and block below of changes
	 */
	public void updateNeighbors(World worldIn, BlockPos pos) {
		this.notifyNeighborsOfStateChange(worldIn, pos, this);
	}

	public void notifyNeighborsOfStateChange(World worldIn, BlockPos pos, Block blockType) {
		if (net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(worldIn, pos, worldIn.getBlockState(pos),
				java.util.EnumSet.allOf(Direction.class), true).isCanceled()) {
			return;
		}

		this.updateNeighbors(worldIn.getBlockState(pos), worldIn, pos, 3);
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
	public int customUpdateState(World worldIn, BlockPos pos, BlockState state, TileEntityRedstoneClock tileEntity) {
		// Get the old redstone strength.
		int i = tileEntity.getRedstoneStrength(state, null) == 0 ? 15 : 0;

		// If this is going to be powered, check to make sure that it should be powered.
		if (i != 0) {
			// Check each side of this block to see if it's powered.
			for (Direction facing : Direction.values()) {
				if (worldIn.isSidePowered(pos.offset(facing), facing.getOpposite())) {
					i = 0;
					tileEntity.setRedstoneStrength(state, i, null);
					worldIn.setBlockState(pos, state, 3);

					this.updateNeighbors(worldIn, pos);

					// Delay a few ticks.
					return 5;
				}
			}
		}

		// Set the new redstone strength and provide an updated state.
		state = tileEntity.setRedstoneStrength(state, i, null);
		worldIn.setBlockState(pos, state, 3);

		this.updateNeighbors(worldIn, pos);

		return i == 0 ? tileEntity.getConfig().getUnPoweredTick() : tileEntity.getConfig().getPoweredTick();
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
					"This simple clock is able to produce a redstone signal at configurable intervals.  Right Click to configure."));
		}
	}
}
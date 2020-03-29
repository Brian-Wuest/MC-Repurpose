package com.wuest.repurpose.Blocks;

import com.wuest.repurpose.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.List;

public class BlockEnrichedFarmland extends FarmlandBlock implements IModBlock {
	public BlockEnrichedFarmland() {
		super(Block.Properties.create(Material.EARTH).hardnessAndResistance(0.6F).sound(SoundType.GROUND)
				.harvestLevel(0).harvestTool(ToolType.SHOVEL));

		// Make sure that this block is always wet.
		this.setDefaultState(this.stateContainer.getBaseState().with(MOISTURE, Integer.valueOf(7)));

		ModRegistry.setBlockName(this, "block_enriched_farmland");
	}

	/**
	 * Block's chance to react to a living entity falling on it.
	 */
	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		// Falling on this block won't turn it to dirt.
		entityIn.onLivingFall(fallDistance, 1.0F);
	}

	/**
	 * Update the provided state given the provided neighbor facing and neighbor
	 * state, returning a new state. For example, fences make their connections to
	 * the passed in state if possible, and wet concrete powder immediately returns
	 * its solidified counterpart. Note that this method should ideally consider
	 * only the specific face passed in.
	 */
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
										  BlockPos currentPos, BlockPos facingPos) {
		return stateIn;
	}

	/**
	 * Determines if this block can support the passed in plant, allowing it to be
	 * planted and grow. Some examples: Reeds check if its a reed, or if its
	 * sand/dirt/grass and adjacent to water Cacti checks if its a cacti, or if its
	 * sand Nether types check for soul sand Crops check for tilled soil Caves check
	 * if it's a solid surface Plains check if its grass or dirt Water check if its
	 * still water
	 *
	 * @param state     The Current state
	 * @param world     The current world
	 * @param pos       Block position in world
	 * @param facing    The direction relative to the given position the plant wants
	 *                  to be, typically its UP
	 * @param plantable The plant that wants to check
	 * @return True to allow the plant to be planted/stay.
	 */
	@Override
	public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing,
								   net.minecraftforge.common.IPlantable plantable) {
		// This block can sustain everything.
		return true;
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip,
							   ITooltipFlag advanced) {
		super.addInformation(stack, world, tooltip, advanced);

		boolean advancedKeyDown = Screen.hasShiftDown();

		if (!advancedKeyDown) {
			tooltip.add(new StringTextComponent(
					"Hold" + TextFormatting.BLUE + " Shift " + TextFormatting.GRAY + "for advanced information."));
		} else {
			tooltip.add(new StringTextComponent(
					"This hardy soil always remains hydrated and tilled, no matter the climate!"));
		}
	}
}

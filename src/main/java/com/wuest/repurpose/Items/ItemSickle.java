package com.wuest.repurpose.Items;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;


/**
 * @author WuestMan
 */
public class ItemSickle extends ToolItem {
	public static HashSet<Block> effectiveBlocks = new HashSet<>();
	protected int breakRadius = 0;

	/**
	 * Initializes a new instance of the ItemSickle class.
	 *
	 * @param tier The type of tool material.
	 */
	public ItemSickle(IItemTier tier) {
		super(1.0f, -2.4000000953674316f, tier, effectiveBlocks, new Item.Properties().group(ItemGroup.TOOLS));
		this.breakRadius = 1 + tier.getHarvestLevel();
	}

	public static void setEffectiveBlocks() {
		effectiveBlocks.clear();

		effectiveBlocks.addAll(BlockTags.LEAVES.getAllElements());
		effectiveBlocks.addAll(BlockTags.SMALL_FLOWERS.getAllElements());
		effectiveBlocks.add(Blocks.TALL_GRASS);
		effectiveBlocks.add(Blocks.DEAD_BUSH);
		effectiveBlocks.add(Blocks.ROSE_BUSH);
		effectiveBlocks.add(Blocks.PEONY);
		effectiveBlocks.add(Blocks.GRASS);
		effectiveBlocks.add(Blocks.SEAGRASS);
		effectiveBlocks.add(Blocks.TALL_SEAGRASS);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		Block block = state.getBlock();

		if (block != Blocks.COBWEB && state.getMaterial() != Material.LEAVES) {
			return super.getDestroySpeed(stack, state);
		} else {
			return 15.0F;
		}
	}

	/**
	 * Called when a Block is destroyed using this Item. Return true to trigger the
	 * "Use Item" statistic.
	 */
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos,
									LivingEntity entityLiving) {
		if (!worldIn.isRemote) {
			stack.damageItem(1, entityLiving, (livingEntity) -> {
				livingEntity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
			});

			if ((double) state.getBlockHardness(worldIn, pos) != 0.0D && !(state.getBlock() instanceof LeavesBlock)) {
				stack.damageItem(1, entityLiving, (livingEntity) -> {
					livingEntity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
				});
			} else if ((state.getBlock() instanceof BushBlock || state.getBlock() instanceof LeavesBlock)
					&& entityLiving instanceof PlayerEntity) {
				BlockPos corner1 = pos.north(this.breakRadius).east(this.breakRadius).up(this.breakRadius);
				BlockPos corner2 = pos.south(this.breakRadius).west(this.breakRadius).down(this.breakRadius);
				PlayerEntity player = (PlayerEntity) entityLiving;

				for (BlockPos currentPos : BlockPos.getAllInBoxMutable(corner1, corner2)) {
					BlockState currentState = worldIn.getBlockState(currentPos);

					if (currentState != null && ItemSickle.effectiveBlocks.contains(currentState.getBlock())) {
						boolean canHarvest = currentState.getBlock().canHarvestBlock(currentState, worldIn, currentPos,
								player);

						if (canHarvest) {
							worldIn.destroyBlock(currentPos, true);
						}
					}
				}
			}
		}

		return true;
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
							   ITooltipFlag advanced) {
		super.addInformation(stack, worldIn, tooltip, advanced);

		boolean advancedKeyDown = Screen.hasShiftDown();

		if (!advancedKeyDown) {
			tooltip.add(new StringTextComponent(
					"Hold" + TextFormatting.BLUE + " Shift " + TextFormatting.GRAY + "for advanced information."));
		} else {
			tooltip.add(new StringTextComponent("Cut grass, flowers and leaves down with ease!"));
		}
	}
}

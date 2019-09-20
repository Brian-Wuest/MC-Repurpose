package com.wuest.repurpose.Items;

import java.util.HashSet;
import java.util.List;

import javax.annotation.Nullable;

import com.wuest.repurpose.ModRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;;

/**
 * 
 * @author WuestMan
 *
 */
public class ItemSickle extends ToolItem {
	public static HashSet<Block> effectiveBlocks = ItemSickle.getEffectiveBlocks();
	protected int breakRadius = 0;

	public static HashSet<Block> getEffectiveBlocks() {
		HashSet<Block> returnBlocks = new HashSet<>();

		returnBlocks.addAll(BlockTags.LEAVES.getAllElements());
		returnBlocks.addAll(BlockTags.SMALL_FLOWERS.getAllElements());
		returnBlocks.add(Blocks.TALL_GRASS);
		returnBlocks.add(Blocks.DEAD_BUSH);
		returnBlocks.add(Blocks.ROSE_BUSH);
		returnBlocks.add(Blocks.PEONY);

		return returnBlocks;
	}

	/**
     * Initializes a new instance of the ItemSickle class.
     * @param material The type of tool material.
     * @param name The name to register.
     */
    public ItemSickle(IItemTier tier, String name)
    {
    	super(1.0f, -2.4000000953674316f, tier, effectiveBlocks, new Item.Properties());
        this.breakRadius = 1 + tier.getHarvestLevel();

        ModRegistry.setItemName(this, name);
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

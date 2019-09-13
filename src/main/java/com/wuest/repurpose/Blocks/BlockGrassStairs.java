package com.wuest.repurpose.Blocks;

import java.util.List;

import com.wuest.repurpose.ModRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;;

/**
 * This class defines a set of grass stairs.
 * 
 * @author WuestMan
 *
 */
public class BlockGrassStairs extends StairsBlock implements IModBlock {
	public BlockGrassStairs() {
		super(Blocks.GRASS_BLOCK.getDefaultState(), Block.Properties.from(Blocks.GRASS_BLOCK));

		ModRegistry.setBlockName(this, "block_grass_stairs");
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> drops = super.getDrops(state, builder);

		if (drops.size() > 0) {
			ItemStack tool = builder.get(LootParameters.TOOL);
			int silklevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, tool);

			drops.clear();

			if (silklevel > 0) {
				drops.add(new ItemStack(ModRegistry.GrassStairs()));
			} else {
				drops.add(new ItemStack(ModRegistry.DirtStairs()));
			}
		}

		return drops;
	}
}

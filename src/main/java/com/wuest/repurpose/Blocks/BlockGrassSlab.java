package com.wuest.repurpose.Blocks;

import com.wuest.repurpose.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraftforge.common.ToolType;

import java.util.List;

public class BlockGrassSlab extends SlabBlock implements IModBlock {
	public BlockGrassSlab() {
		super(Block.Properties.create(Material.EARTH, MaterialColor.GRASS).sound(SoundType.GROUND)
				.hardnessAndResistance(0.5f, 0.5f).harvestTool(ToolType.SHOVEL).harvestLevel(0));

		ModRegistry.setBlockName(this, "block_grass_slab");
	}

	/**
	 * Gets the render layer this block will render on. SOLID for solid blocks, CUTOUT or CUTOUT_MIPPED for on-off
	 * transparency (glass, reeds), TRANSLUCENT for fully blended transparency (stained glass)
	 */
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
}

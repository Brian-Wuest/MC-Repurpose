package com.wuest.repurpose.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.common.ToolType;

public class BlockGrassSlab extends SlabBlock implements IModBlock {
	public BlockGrassSlab() {
		super(Block.Properties.create(Material.EARTH, MaterialColor.GRASS).sound(SoundType.GROUND)
				.hardnessAndResistance(0.5f, 0.5f).harvestTool(ToolType.SHOVEL).harvestLevel(0));
	}
}

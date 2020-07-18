package com.wuest.repurpose.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.common.ToolType;

public class BlockGlowstoneSlab extends SlabBlock implements IModBlock {
	public BlockGlowstoneSlab() {
		super(Block.Properties.create(Material.GLASS, MaterialColor.SAND).sound(SoundType.GLASS)
				.hardnessAndResistance(0.5f, 0.5f).harvestTool(ToolType.PICKAXE).harvestLevel(0).setLightLevel(value -> 15));
	}
}

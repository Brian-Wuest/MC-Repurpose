package com.wuest.repurpose.Blocks;

import com.wuest.repurpose.ModRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * 
 * @author WuestMan
 *
 */
public class BlockCharcoal extends Block implements IModBlock
{
	public BlockCharcoal(String name)
	{
		super(Block.Properties.create(Material.CLAY)
		.hardnessAndResistance(.5F, 10.0F)
		.sound(SoundType.STONE));

		ModRegistry.setBlockName(this, name);
	}

}
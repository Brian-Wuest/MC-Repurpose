package com.wuest.repurpose.Blocks;

import com.wuest.repurpose.ModRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

/**
 * 
 * @author WuestMan
 *
 */
public class BlockCharcoal extends Block
{

	public BlockCharcoal(String name)
	{
		super(Material.ROCK);
		ModRegistry.setBlockName(this, name);
		this.setHardness(5.0F);
		this.setResistance(10.0F);
		this.setSoundType(SoundType.STONE);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}

}
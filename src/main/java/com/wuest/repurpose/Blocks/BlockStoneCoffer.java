package com.wuest.repurpose.Blocks;

import com.wuest.repurpose.ModRegistry;

import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * @author WuestMan
 *
 */
public class BlockStoneCoffer extends BlockChest
{

	/**
	 * Initializes a new instance of the BlockStoneCoffer class.
	 * @param materialIn
	 */
	public BlockStoneCoffer(String name) 
	{
		super(BlockChest.Type.BASIC);
		
		ModRegistry.setBlockName(this, name);
	}

}
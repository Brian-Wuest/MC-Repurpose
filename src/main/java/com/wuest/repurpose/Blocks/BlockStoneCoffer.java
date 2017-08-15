package com.wuest.repurpose.Blocks;

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
public class BlockStoneCoffer extends BlockContainer
{

	/**
	 * Initializes a new instance of the BlockStoneCoffer class.
	 * @param materialIn
	 */
	protected BlockStoneCoffer(Material materialIn) 
	{
		super(materialIn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    @Override
	public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasCustomBreakingProgress(IBlockState state)
    {
        return true;
    }


}
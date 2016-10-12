package com.wuest.utilities.Blocks;

import com.wuest.utilities.ModRegistry;
import com.wuest.utilities.Base.TileBlockBase;
import com.wuest.utilities.Tiles.TileEntityInfusedRedstone;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This is the infused redstone block which will appear as any other block.
 * @author WuestMan
 *
 */
public class BlockInfusedRedstone extends TileBlockBase<TileEntityInfusedRedstone>
{

	public BlockInfusedRedstone(Material materialIn, String name)
	{
		super(materialIn);
		
		ModRegistry.setBlockName(this, name);
	}

	@Override
	public int customUpdateState(World worldIn, BlockPos pos, IBlockState state, TileEntityInfusedRedstone tileEntity)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void customBreakBlock(TileEntityInfusedRedstone tileEntity, World worldIn, BlockPos pos, IBlockState state)
	{
		// TODO Auto-generated method stub
		
	}
	
    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

}

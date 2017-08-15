package com.wuest.repurpose.Blocks;

import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Proxy.CommonProxy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCompressedPowered;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMiniRedstone extends BlockCompressedPowered
{
	protected static final AxisAlignedBB bounds = new AxisAlignedBB(
			0.25D, 
			0.0D, 
			0.25D, 
			0.75D, 
			0.50D, 
			0.75D);

	/**
	 * Initializes a new instance of the BlockMiniRedstone.
	 */
	public BlockMiniRedstone()
	{
		super(Material.IRON, MapColor.TNT);
		this.setCreativeTab(CreativeTabs.REDSTONE);
		ModRegistry.setBlockName(this, "blockminiredstone");
		this.setHarvestLevel(null, 0);
		this.setHardness(.5f);
		this.setResistance(10.0f);
		this.setSoundType(SoundType.METAL);
	}
		
	@Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return 8;
    }
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
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
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
    	return BlockMiniRedstone.bounds;
    }

	@Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return BlockMiniRedstone.bounds;
    }
	
	/**
     * Determines if the player can harvest this block, obtaining it's drops when the block is destroyed.
     *
     * @param player The player damaging the block, may be null
     * @param meta The block's current metadata
     * @return True to spawn the drops
     */
    @Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
    {
        return true;
    }
	
	/**
	 * Determines if a torch can be placed on the top surface of this block.
	 * Useful for creating your own block that torches can be on, such as fences.
	 *
	 * @param state The current state
	 * @param world The current world
	 * @param pos Block position in world
	 * @return True to allow the torch to be placed
	 */
	@Override
	public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return false;
	}
	
    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
}

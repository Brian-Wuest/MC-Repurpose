package com.wuest.utilities.Blocks;

import java.util.Random;

import com.wuest.utilities.ModRegistry;
import com.wuest.utilities.Proxy.CommonProxy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class defines a set of grass stairs.
 * @author WuestMan
 *
 */
public class BlockGrassStairs extends BlockStairs
{
	public BlockGrassStairs()
	{
		super(Blocks.GRASS.getDefaultState());
		
		this.setHardness(0.5F);
		this.setHarvestLevel("shovel", 0);
		this.useNeighborBrightness = true;
		ModRegistry.setBlockName(this, "blockgrassstairs");
	}

	@SideOnly(Side.CLIENT)
	public static void RegisterBlockRenderer()
	{
		// Register the block renderer.
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor()
		{
			public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex)
			{
				return worldIn != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(worldIn, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D);
			}
		}, new Block[] {ModRegistry.GrassStairs()});

		// Register the item renderer.
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor()
		{
			public int getColorFromItemstack(ItemStack stack, int tintIndex)
			{
				// Get the item for this stack.
				Item item = stack.getItem();
				
				if (item instanceof ItemBlock)
				{
					// Get the block for this item and determine if it's a grass stairs.
					ItemBlock itemBlock = (ItemBlock)item;
					
					if (itemBlock.getBlock() instanceof BlockGrassStairs)
					{
						BlockPos pos = Minecraft.getMinecraft().thePlayer.getPosition();
						WorldClient world = Minecraft.getMinecraft().theWorld;
						return pos != null ? BiomeColorHelper.getGrassColorAtPos(world, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D);
					}
				}
				
				return -1;
			}
		}, new Block[] { ModRegistry.GrassStairs() });
	}
	
    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(ModRegistry.DirtStairs());
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
	
    /**
     * Return true from this function if the player with silk touch can harvest this block directly, and not it's normal drops.
     *
     * @param world The world
     * @param pos Block position in world
     * @param state current block state
     * @param player The player doing the harvesting
     * @return True if the block can be directly harvested using silk touch
     */
    @Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
    	return true;
    }
	
    /**
     * Get the MapColor for this Block and the given BlockState
     */
	@Override
	@SideOnly(Side.CLIENT)
	public MapColor getMapColor(IBlockState state)
    {
        return this.blockMapColor;
    }
}

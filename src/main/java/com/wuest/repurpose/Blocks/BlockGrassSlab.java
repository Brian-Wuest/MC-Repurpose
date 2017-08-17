package com.wuest.repurpose.Blocks;

import java.util.Random;

import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Items.ItemBlockGrassSlab;
import com.wuest.repurpose.Proxy.CommonProxy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
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

public abstract class BlockGrassSlab extends BlockSlab
{
	/**
	 * The property used for the variant.
	 * Needed for interactions with ItemSlab.
	 */
	private static final PropertyBool VARIANT_PROPERTY = PropertyBool.create("variant");

	public BlockGrassSlab()
	{
		super(Material.GROUND);

		this.setSoundType(SoundType.GROUND);
		IBlockState iblockstate = this.blockState.getBaseState();
		this.setHardness(0.5F);
		this.setHarvestLevel("shovel", 0);
		
		if (!this.isDouble())
		{
			iblockstate = iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
			ModRegistry.setBlockName(this, "block_half_grass_slab");
			this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		}
		else
		{
			ModRegistry.setBlockName(this, "block_grass_slab");
		}
		
		iblockstate = iblockstate.withProperty(VARIANT_PROPERTY, false);

		this.setDefaultState(iblockstate);
		this.useNeighborBrightness = !this.isDouble();
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
		}, new Block[] {ModRegistry.GrassSlab()});
		
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor()
		{
			public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex)
			{
				return worldIn != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(worldIn, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D);
			}
		}, new Block[] {ModRegistry.DoubleGrassSlab()});

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
					
					if (itemBlock.getBlock() instanceof BlockGrassSlab)
					{
						BlockPos pos = Minecraft.getMinecraft().player.getPosition();
						WorldClient world = Minecraft.getMinecraft().world;
						return pos != null ? BiomeColorHelper.getGrassColorAtPos(world, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D);
					}
				}
				
				return -1;
			}
		}, new Block[] { ModRegistry.GrassSlab() });
	}
	
	@Override
	public String getUnlocalizedName(int meta)
	{
		return super.getUnlocalizedName();
	}

	@Override
	public boolean isDouble()
	{
		return false;
	}

	@Override
	public IProperty<?> getVariantProperty()
	{
		return VARIANT_PROPERTY;
	}

	@Override
	public Comparable<?> getTypeForItem(ItemStack stack)
	{
		return false;
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState blockState = this.getDefaultState();
		blockState = blockState.withProperty(VARIANT_PROPERTY, false);
		if (!this.isDouble()) 
		{
			EnumBlockHalf value = EnumBlockHalf.BOTTOM;

			if ((meta & 8) != 0) 
			{
				value = EnumBlockHalf.TOP;
			}

			blockState = blockState.withProperty(HALF, value);
		}

		return blockState;
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
	
	@Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
    
	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		int i = 0;

		if (!this.isDouble() && state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP)
		{
			i |= 8;
		}

		return i;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return this.isDouble() ? new BlockStateContainer(this, new IProperty[] {VARIANT_PROPERTY}): new BlockStateContainer(this, new IProperty[] {HALF, VARIANT_PROPERTY});
	}

	/**
	 * Get the MapColor for this Block and the given BlockState
	 */
	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return ((BlockGrass)Blocks.GRASS).getMapColor(Blocks.GRASS.getDefaultState(), world, pos);
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(ModRegistry.GrassSlab());
	}

	/**
	 * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
	 * returns the metadata of the dropped item based on the old metadata of the block.
	 */
	@Override
	public int damageDropped(IBlockState state)
	{
		return 0;
	}

}

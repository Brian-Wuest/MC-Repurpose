package wuest.utilities.Blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStoneSlabNew;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import wuest.utilities.Items.ItemBlockDirtSlab;
import wuest.utilities.Proxy.CommonProxy;

public abstract class BlockDirtSlab extends BlockSlab
{
	/**
	 * The property used for the variant.
	 * Needed for interactions with ItemSlab.
	 */
	private static final PropertyBool VARIANT_PROPERTY = PropertyBool.create("variant");
	public static BlockHalfDirtSlab RegisteredHalfBlock; 
	public static BlockDoubleDirtSlab RegisteredDoubleSlab;
	
	public BlockDirtSlab()
	{
		super(Material.ground);

		this.setStepSound(SoundType.GROUND);
		IBlockState iblockstate = this.blockState.getBaseState();
		this.setHardness(0.5F);
		this.setHarvestLevel("shovel", 0);
		
		if (!this.isDouble())
		{
			iblockstate = iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
			CommonProxy.setBlockName(this, "blockHalfDirtSlab");
			this.setCreativeTab(CreativeTabs.tabBlock);
		}
		else
		{
			CommonProxy.setBlockName(this, "blockDirtSlab");
		}
		
		iblockstate = iblockstate.withProperty(VARIANT_PROPERTY, false);

		this.setDefaultState(iblockstate);
		this.useNeighborBrightness = !this.isDouble();
	}

	public static void RegisterBlock()
	{
		BlockDirtSlab.RegisteredHalfBlock = new BlockHalfDirtSlab();
		BlockDirtSlab.RegisteredDoubleSlab = new BlockDoubleDirtSlab();
		
		ItemBlockDirtSlab itemHalfDirtSlab = new ItemBlockDirtSlab(
				BlockDirtSlab.RegisteredHalfBlock, 
				BlockDirtSlab.RegisteredHalfBlock, 
				BlockDirtSlab.RegisteredDoubleSlab, 
				true);
		
		itemHalfDirtSlab = (ItemBlockDirtSlab) itemHalfDirtSlab.setRegistryName("blockHalfDirtSlab");
		
		ItemBlockDirtSlab itemDoubleDirtSlab = new ItemBlockDirtSlab(
				BlockDirtSlab.RegisteredDoubleSlab, 
				BlockDirtSlab.RegisteredHalfBlock, 
				BlockDirtSlab.RegisteredDoubleSlab, 
				true);
		
		itemDoubleDirtSlab = (ItemBlockDirtSlab) itemDoubleDirtSlab.setRegistryName("blockDirtSlab");
		itemDoubleDirtSlab.setCreativeTab(null);
		
		CommonProxy.registerBlock(BlockDirtSlab.RegisteredHalfBlock, itemHalfDirtSlab);
		
		CommonProxy.registerBlock(BlockDirtSlab.RegisteredDoubleSlab, itemDoubleDirtSlab);
		
		// Register recipe.
		GameRegistry.addRecipe(new ItemStack(BlockDirtSlab.RegisteredHalfBlock, 6),
				"xxx",
				'x', Item.getItemFromBlock(Blocks.dirt));
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
	public MapColor getMapColor(IBlockState state)
	{
		return Blocks.dirt.getMapColor(Blocks.dirt.getDefaultState());
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(BlockDirtSlab.RegisteredHalfBlock);
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

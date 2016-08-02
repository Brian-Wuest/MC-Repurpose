package com.wuest.utilities.Blocks;

import java.util.List;
import java.util.Random;

import com.wuest.utilities.ModRegistry;
import com.wuest.utilities.WuestUtilities;
import com.wuest.utilities.Proxy.CommonProxy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCustomWall extends Block
{
	public static final PropertyBool UP = PropertyBool.create("up");
	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool EAST = PropertyBool.create("east");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool WEST = PropertyBool.create("west");
	public static final PropertyEnum<BlockCustomWall.EnumType> VARIANT = PropertyEnum.<BlockCustomWall.EnumType>create("variant", BlockCustomWall.EnumType.class);
	protected static final AxisAlignedBB[] axis1 = new AxisAlignedBB[] {new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.3125D, 0.0D, 0.0D, 0.6875D, 0.875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.3125D, 1.0D, 0.875D, 0.6875D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};
	protected static final AxisAlignedBB[] axis2 = new AxisAlignedBB[] {axis1[0].setMaxY(1.5D), axis1[1].setMaxY(1.5D), axis1[2].setMaxY(1.5D), axis1[3].setMaxY(1.5D), axis1[4].setMaxY(1.5D), axis1[5].setMaxY(1.5D), axis1[6].setMaxY(1.5D), axis1[7].setMaxY(1.5D), axis1[8].setMaxY(1.5D), axis1[9].setMaxY(1.5D), axis1[10].setMaxY(1.5D), axis1[11].setMaxY(1.5D), axis1[12].setMaxY(1.5D), axis1[13].setMaxY(1.5D), axis1[14].setMaxY(1.5D), axis1[15].setMaxY(1.5D)};

	public BlockCustomWall.EnumType BlockVariant;

	public BlockCustomWall(Block modelBlock, BlockCustomWall.EnumType variant)
	{
		super(variant.getMaterial());

		this.setDefaultState(this.blockState.getBaseState().withProperty(
				UP, Boolean.valueOf(false)).withProperty(
						NORTH, Boolean.valueOf(false)).withProperty(
								EAST, Boolean.valueOf(false)).withProperty(
										SOUTH, Boolean.valueOf(false)).withProperty(
												WEST, Boolean.valueOf(false)).withProperty(
														VARIANT, variant));

		this.setHardness(modelBlock.getBlockHardness(null, null, null));
		this.setResistance(modelBlock.getExplosionResistance(null) * 5.0F / 3.0F);
		this.setSoundType(modelBlock.getSoundType());
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		this.setHarvestLevel("shovel", 0);
		this.BlockVariant = variant;
		
		if (this.BlockVariant == EnumType.DIRT)
		{
			this.setTickRandomly(true);
		}
		
		ModRegistry.setBlockName(this, variant.getUnlocalizedName());
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
		}, new Block[] {ModRegistry.GrassWall()});

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

					if (itemBlock.getBlock() instanceof BlockCustomWall)
					{
						BlockCustomWall customWall = (BlockCustomWall)itemBlock.getBlock();

						if (customWall.BlockVariant == EnumType.GRASS)
						{
							BlockPos pos = Minecraft.getMinecraft().thePlayer.getPosition();
							WorldClient world = Minecraft.getMinecraft().theWorld;
							return pos != null ? BiomeColorHelper.getGrassColorAtPos(world, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D);
						}
					}
				}

				return -1;
			}
		}, new Block[] { ModRegistry.GrassWall() });
	}

	public static int getBlockIndex(IBlockState blockState)
	{
		int i = 0;

		if (((Boolean)blockState.getValue(NORTH)).booleanValue())
		{
			i |= 1 << EnumFacing.NORTH.getHorizontalIndex();
		}

		if (((Boolean)blockState.getValue(EAST)).booleanValue())
		{
			i |= 1 << EnumFacing.EAST.getHorizontalIndex();
		}

		if (((Boolean)blockState.getValue(SOUTH)).booleanValue())
		{
			i |= 1 << EnumFacing.SOUTH.getHorizontalIndex();
		}

		if (((Boolean)blockState.getValue(WEST)).booleanValue())
		{
			i |= 1 << EnumFacing.WEST.getHorizontalIndex();
		}

		return i;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote && WuestUtilities.proxy.proxyConfiguration.enableGrassSpreadToCustomDirt)
		{
			if (worldIn.getLightFromNeighbors(pos.up()) >= 9)
			{
				for (int i = 0; i < 4; ++i)
				{
					BlockPos blockpos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);

					if (blockpos.getY() >= 0 && blockpos.getY() < 256 && !worldIn.isBlockLoaded(blockpos))
					{
						return;
					}

					IBlockState iblockstate = worldIn.getBlockState(blockpos.up());
					IBlockState iblockstate1 = worldIn.getBlockState(blockpos);

					if ((iblockstate1.getBlock() == Blocks.GRASS
							|| iblockstate1.getBlock() == ModRegistry.GrassStairs()
							|| iblockstate1.getBlock() == ModRegistry.GrassWall()
							|| iblockstate1.getBlock() == ModRegistry.GrassSlab()
							|| iblockstate1.getBlock() == ModRegistry.DoubleGrassSlab())
							&& worldIn.getLightFromNeighbors(blockpos.up()) >= 4)
					{
						IBlockState grassStairsState = ModRegistry.GrassWall().getDefaultState();
						worldIn.setBlockState(pos, grassStairsState, 3);
					}
				}
			}
		}
	}
	
	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(ModRegistry.DirtWall());
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
		return this.BlockVariant == EnumType.GRASS;
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

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
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
		state = this.getActualState(state, source, pos);
		return axis1[getBlockIndex(state)];
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
	{
		blockState = this.getActualState(blockState, worldIn, pos);
		return axis2[getBlockIndex(blockState)];
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
		if (state.isSideSolid(world, pos, EnumFacing.UP))
		{
			return true;
		}
		else
		{
			return this instanceof BlockCustomWall;
		}
	}

	/**
	 * Gets the localized name of this block. Used for the statistics page.
	 */
	@Override
	public String getLocalizedName()
	{
		return I18n.translateToLocal(this.getUnlocalizedName() + "." + BlockWall.EnumType.NORMAL.getUnlocalizedName() + ".name");
	}

	public boolean canConnectTo(IBlockAccess worldIn, BlockPos pos)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos);
		Block block = iblockstate.getBlock();
		Material material = block.getMaterial(iblockstate);
		
		if (block == Blocks.BARRIER)
		{
			return false;
		}
		
		// Make sure all custom walls can be connected.
		return (!(block instanceof BlockCustomWall) && !(block instanceof BlockFenceGate) ? (material.isOpaque() && iblockstate.isFullCube() ? material != Material.GOURD : false) : true);
	}

	/**
	 * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
	 * returns the metadata of the dropped item based on the old metadata of the block.
	 */
	@Override
	public int damageDropped(IBlockState state)
	{
		return ((BlockCustomWall.EnumType)state.getValue(VARIANT)).getMetadata();
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(VARIANT, BlockCustomWall.EnumType.byMetadata(meta));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((BlockCustomWall.EnumType)state.getValue(VARIANT)).getMetadata();
	}

	/**
	 * Get the actual Block state of this Block at the given position. This applies properties not visible in the
	 * metadata, such as fence connections.
	 */
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		boolean flag = this.canConnectTo(worldIn, pos.north());
		boolean flag1 = this.canConnectTo(worldIn, pos.east());
		boolean flag2 = this.canConnectTo(worldIn, pos.south());
		boolean flag3 = this.canConnectTo(worldIn, pos.west());
		boolean flag4 = flag && !flag1 && flag2 && !flag3 || !flag && flag1 && !flag2 && flag3;
		return state.withProperty(UP, Boolean.valueOf(!flag4 || !worldIn.isAirBlock(pos.up()))).withProperty(NORTH, Boolean.valueOf(flag)).withProperty(EAST, Boolean.valueOf(flag1)).withProperty(SOUTH, Boolean.valueOf(flag2)).withProperty(WEST, Boolean.valueOf(flag3));
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {UP, NORTH, EAST, WEST, SOUTH, VARIANT});
	}

	public static enum EnumType implements IStringSerializable
	{
		DIRT(0, "blockdirtwall", "blockDirtWall", Material.GROUND),
		GRASS(1, "blockgrasswall", "blockGrassWall", Material.GROUND);

		private static final BlockCustomWall.EnumType[] META_LOOKUP = new BlockCustomWall.EnumType[values().length];
		private final int meta;
		private final String name;
		private String unlocalizedName;
		private Material material;

		private EnumType(int meta, String name, String unlocalizedName, Material blockMaterial)
		{
			this.meta = meta;
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			this.material = blockMaterial;
		}

		public int getMetadata()
		{
			return this.meta;
		}

		public String toString()
		{
			return this.name;
		}

		public static BlockCustomWall.EnumType byMetadata(int meta)
		{
			if (meta < 0 || meta >= META_LOOKUP.length)
			{
				meta = 0;
			}

			return META_LOOKUP[meta];
		}

		public String getName()
		{
			return this.name;
		}

		public Material getMaterial()
		{
			return this.material;
		}

		public String getUnlocalizedName()
		{
			return this.unlocalizedName;
		}

		static
		{
			for (BlockCustomWall.EnumType customwall$enumtype : values())
			{
				META_LOOKUP[customwall$enumtype.getMetadata()] = customwall$enumtype;
			}
		}
	}
}

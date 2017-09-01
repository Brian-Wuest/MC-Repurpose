package com.wuest.repurpose.Blocks;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nullable;

import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Gui.CustomSlot;
import com.wuest.repurpose.Gui.GuiCoffer;
import com.wuest.repurpose.Tiles.TileEntityCoffer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockCoffer extends Block
{
	public static final PropertyEnum<IronChestType> VARIANT_PROP = PropertyEnum
			.create("variant", IronChestType.class);

	protected static final AxisAlignedBB IRON_CHEST_AABB = new AxisAlignedBB(
			0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
	
	private static final EnumFacing[] validRotationAxes = new EnumFacing[]
	{ 
		EnumFacing.UP, 
		EnumFacing.DOWN 
	};

	public BlockCoffer()
	{
		super(Material.IRON);

		this.setDefaultState(this.blockState.getBaseState()
				.withProperty(VARIANT_PROP, IronChestType.IRON));
		this.setHardness(3.0F);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		ModRegistry.setBlockName(this, "block_coffer");
		
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source,
			BlockPos pos)
	{
		return IRON_CHEST_AABB;
	}

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
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Nullable
	public ILockableContainer getLockableContainer(World worldIn, BlockPos pos)
	{
		return this.getContainer(worldIn, pos, false);
	}

	@Nullable
	public ILockableContainer getContainer(World worldIn, BlockPos pos,
			boolean allowBlocking)
	{
		return null;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos,
			IBlockState state, EntityPlayer playerIn, EnumHand hand,
			EnumFacing heldItem, float side, float hitX, float hitY)
	{
		TileEntity te = worldIn.getTileEntity(pos);

		if (te == null || !(te instanceof TileEntityCoffer))
		{
			return true;
		}

		if (worldIn.isSideSolid(pos.add(0, 1, 0), EnumFacing.DOWN))
		{
			return true;
		}

		if (worldIn.isRemote)
		{
			return true;
		}

		playerIn.openGui(Repurpose.instance,
				GuiCoffer.GUI_ID, worldIn,
				pos.getX(), pos.getY(), pos.getZ());

		return true;
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return state.getValue(VARIANT_PROP).makeEntity();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(CreativeTabs tab,	NonNullList<ItemStack> list)
	{
		for (IronChestType type : IronChestType.VALUES)
		{
			if (type.isValidForCreativeMode())
			{
				list.add(new ItemStack(this, 1, type.ordinal()));
			}
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(VARIANT_PROP,
				IronChestType.VALUES[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState blockState)
	{
		return blockState.getValue(VARIANT_PROP).ordinal();
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, VARIANT_PROP);
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(world, pos, state);
		world.notifyBlockUpdate(pos, state, state, 3);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state,
			EntityLivingBase placer, ItemStack stack)
	{
		TileEntity te = worldIn.getTileEntity(pos);

		if (te != null && te instanceof TileEntityCoffer)
		{
			TileEntityCoffer teic = (TileEntityCoffer) te;

			teic.wasPlaced(placer, stack);
			teic.setFacing(placer.getHorizontalFacing().getOpposite());

			worldIn.notifyBlockUpdate(pos, state, state, 3);
		}
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return state.getValue(VARIANT_PROP).ordinal();
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntityCoffer tileentity = (TileEntityCoffer) worldIn
				.getTileEntity(pos);

		if (tileentity != null)
		{
			tileentity.removeAdornments();

			InventoryHelper.dropInventoryItems(worldIn, pos, tileentity);
		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos,
			Entity exploder, Explosion explosion)
	{
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof TileEntityCoffer)
		{
			TileEntityCoffer teic = (TileEntityCoffer) te;

			if (teic.getType().isExplosionResistant())
			{
				return 10000F;
			}
		}

		return super.getExplosionResistance(world, pos, exploder, explosion);
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state)
	{
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn,
			BlockPos pos)
	{
		return Container.calcRedstone(worldIn.getTileEntity(pos));
	}

	@Override
	public EnumFacing[] getValidRotations(World worldObj, BlockPos pos)
	{
		return validRotationAxes;
	}

	@Override
	public boolean rotateBlock(World worldObj, BlockPos pos, EnumFacing axis)
	{
		if (worldObj.isRemote)
		{
			return false;
		}

		if (axis == EnumFacing.UP || axis == EnumFacing.DOWN)
		{
			TileEntity tileEntity = worldObj.getTileEntity(pos);

			if (tileEntity instanceof TileEntityCoffer)
			{
				TileEntityCoffer icte = (TileEntityCoffer) tileEntity;

				icte.rotateAround();
			}

			return true;
		}
		return false;
	}

	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos,
			int id, int param)
	{
		super.eventReceived(state, worldIn, pos, id, param);

		TileEntity tileentity = worldIn.getTileEntity(pos);

		return tileentity != null && tileentity.receiveClientEvent(id, param);
	}

	public enum IronChestType implements IStringSerializable
	{
		IRON(90, 9, true, "block_coffer.png",
				Arrays.asList("ingotIron", "ingotRefinedIron"),
				TileEntityCoffer.class, 184, 202);

		public static final IronChestType VALUES[] = values();
		public final String name;
		public final int size;
		public final int rowLength;
		public final boolean tieredChest;
		public final ResourceLocation modelTexture;
		public final Class<? extends TileEntityCoffer> clazz;
		public final Collection<String> matList;
		public final int xSize;
		public final int ySize;

		IronChestType(int size, int rowLength, boolean tieredChest,
				String modelTexture, Collection<String> mats,
				Class<? extends TileEntityCoffer> clazz, int xSize, int ySize)
		{
			this.name = this.name().toLowerCase();
			this.size = size;
			this.rowLength = rowLength;
			this.tieredChest = tieredChest;
			this.modelTexture = new ResourceLocation("repurpose",
					"textures/blocks/" + modelTexture);
			this.matList = Collections.unmodifiableCollection(mats);
			this.clazz = clazz;
			this.xSize = xSize;
			this.ySize = ySize;
		}

		@Override
		public String getName()
		{
			return this.name;
		}

		public int getRowCount()
		{
			return this.size / this.rowLength;
		}

		public boolean isTransparent()
		{
			return false;
		}

		public boolean isValidForCreativeMode()
		{
			return true;
		}

		public boolean isExplosionResistant()
		{
			return false;
		}

		public CustomSlot makeSlot(IInventory chestInventory, int index, int x, int y)
		{
			return new CustomSlot(chestInventory, index, x, y);
		}

		public boolean acceptsStack(ItemStack itemstack)
		{
			return true;
		}

		public TileEntityCoffer makeEntity()
		{
			switch (this)
			{
				case IRON:
				{
					return new TileEntityCoffer();
				}
				
				default:
				{
					return null;
				}
			}
		}
	}
}
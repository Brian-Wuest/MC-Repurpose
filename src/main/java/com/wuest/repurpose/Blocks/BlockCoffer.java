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
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockCoffer extends Block {
	public static final EnumProperty<CofferType> VARIANT_PROP = EnumProperty.create("variant", CofferType.class);

	protected static final AxisAlignedBB IRON_CHEST_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D,
			0.9375D);

	private static final Direction[] validRotationAxes = new Direction[] { Direction.UP, Direction.DOWN };

	private BlockState blockState;

	public BlockCoffer() {
		super(Material.IRON);

		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT_PROP, CofferType.IRON));
		this.setHardness(3.0F);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		ModRegistry.setBlockName(this, "block_coffer");

	}

	@Override
	public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
		return IRON_CHEST_AABB;
	}

	@Override
	public boolean isOpaqueCube(BlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(BlockState state) {
		return false;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Nullable
	@Override
	public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
		return null;
	}

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult hit) {
		TileEntity te = worldIn.getTileEntity(pos);

		if (te == null || !(te instanceof TileEntityCoffer)) {
			return true;
		}

		if (worldIn.isRemote) {
			return true;
		}

		INamedContainerProvider inamedcontainerprovider = this.getContainer(state, worldIn, pos);
		if (inamedcontainerprovider != null) {
			player.openContainer(inamedcontainerprovider);
		}

		return true;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, BlockState state) {
		return state.get(VARIANT_PROP).makeEntity();
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, BlockState state) {
		super.onBlockAdded(world, pos, state);
		world.notifyBlockUpdate(pos, state, state, 3);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, EntityLivingBase placer,
			ItemStack stack) {
		TileEntity te = worldIn.getTileEntity(pos);

		if (te != null && te instanceof TileEntityCoffer) {
			TileEntityCoffer teic = (TileEntityCoffer) te;

			teic.wasPlaced(placer, stack);
			teic.setFacing(placer.getHorizontalFacing().getOpposite());

			worldIn.notifyBlockUpdate(pos, state, state, 3);
		}
	}

	@Override
	public int damageDropped(BlockState state) {
		return state.get(VARIANT_PROP).ordinal();
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, BlockState state) {
		TileEntityCoffer tileentity = (TileEntityCoffer) worldIn.getTileEntity(pos);

		if (tileentity != null) {
			tileentity.removeAdornments();

			InventoryHelper.dropInventoryItems(worldIn, pos, tileentity);
		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof TileEntityCoffer) {
			TileEntityCoffer teic = (TileEntityCoffer) te;

			if (teic.getType().isExplosionResistant()) {
				return 10000F;
			}
		}

		return super.getExplosionResistance(world, pos, exploder, explosion);
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
		this.blockState = blockState;
		return net.minecraft.inventory.container.Container.calcRedstone(worldIn.getTileEntity(pos));
	}

	@Override
	public Direction[] getValidRotations(World worldObj, BlockPos pos) {
		return validRotationAxes;
	}

	@Override
	public boolean rotateBlock(World worldObj, BlockPos pos, Direction axis) {
		if (worldObj.isRemote) {
			return false;
		}

		if (axis == Direction.UP || axis == Direction.DOWN) {
			TileEntity tileEntity = worldObj.getTileEntity(pos);

			if (tileEntity instanceof TileEntityCoffer) {
				TileEntityCoffer icte = (TileEntityCoffer) tileEntity;

				icte.rotateAround();
			}

			return true;
		}
		return false;
	}

	@Override
	public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
		super.eventReceived(state, worldIn, pos, id, param);

		TileEntity tileentity = worldIn.getTileEntity(pos);

		return tileentity != null && tileentity.receiveClientEvent(id, param);
	}

	public enum CofferType implements IStringSerializable {
		IRON(90, 9, true, "block_coffer.png", Arrays.asList("ingotIron", "ingotRefinedIron"), TileEntityCoffer.class,
				184, 202);

		public static final CofferType VALUES[] = values();
		public final String name;
		public final int size;
		public final int rowLength;
		public final boolean tieredChest;
		public final ResourceLocation modelTexture;
		public final Class<? extends TileEntityCoffer> clazz;
		public final Collection<String> matList;
		public final int xSize;
		public final int ySize;

		CofferType(int size, int rowLength, boolean tieredChest, String modelTexture, Collection<String> mats,
				Class<? extends TileEntityCoffer> clazz, int xSize, int ySize) {
			this.name = this.name().toLowerCase();
			this.size = size;
			this.rowLength = rowLength;
			this.tieredChest = tieredChest;
			this.modelTexture = new ResourceLocation("repurpose", "textures/blocks/" + modelTexture);
			this.matList = Collections.unmodifiableCollection(mats);
			this.clazz = clazz;
			this.xSize = xSize;
			this.ySize = ySize;
		}

		@Override
		public String getName() {
			return this.name;
		}

		public int getRowCount() {
			return this.size / this.rowLength;
		}

		public boolean isTransparent() {
			return false;
		}

		public boolean isValidForCreativeMode() {
			return true;
		}

		public boolean isExplosionResistant() {
			return false;
		}

		public CustomSlot makeSlot(IInventory chestInventory, int index, int x, int y) {
			return new CustomSlot(chestInventory, index, x, y);
		}

		public boolean acceptsStack(ItemStack itemstack) {
			return true;
		}

		public TileEntityCoffer makeEntity() {
			switch (this) {
			case IRON: {
				return new TileEntityCoffer();
			}

			default: {
				return null;
			}
			}
		}
	}
}
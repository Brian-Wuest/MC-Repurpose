package wuest.utilities.Blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraftforge.fml.common.registry.GameRegistry;
import wuest.utilities.WuestUtilities;
import wuest.utilities.Gui.GuiRedstoneClock;
import wuest.utilities.Items.ItemBedCompass;
import wuest.utilities.Proxy.CommonProxy;
import wuest.utilities.Tiles.TileEntityRedstoneClock;

public class RedstoneClock extends Block implements ITileEntityProvider
{
	public static final PropertyBool POWERED = PropertyBool.create("powered");
	public static RedstoneClock RegisteredBlock;

	protected int tickRate = 20;

	/**
	 * A simple block that emits redstone signals at regular intervals.
	 */
	public RedstoneClock(String name) 
	{
		super(Material.wood);
		this.setCreativeTab(CreativeTabs.tabRedstone);
		this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, Boolean.valueOf(true)));
		//this.setUnlocalizedName(name);
		CommonProxy.setBlockName(this, "redstoneClock");
	}

	public static void RegisterBlock()
	{
		RedstoneClock.RegisteredBlock = CommonProxy.registerBlock(new RedstoneClock("redstoneClock"));
		
		GameRegistry.registerTileEntity(TileEntityRedstoneClock.class, "RedstoneClock");

		if (WuestUtilities.proxy.proxyConfiguration.addRedstoneClockRecipe)
		{
			// Register recipe.
			GameRegistry.addRecipe(new ItemStack(RedstoneClock.RegisteredBlock),
					"xzx",
					"xyz",
					"xxx",
					'x', Item.getItemFromBlock(Blocks.stone),
					'y', Items.repeater,
					'z', Item.getItemFromBlock(Blocks.redstone_torch));
		}
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		for (EnumFacing enumfacing : EnumFacing.values())
		{
			worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
		}

		worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));

		if (worldIn.getTileEntity(pos) == null)
		{
			TileEntityRedstoneClock clock = new TileEntityRedstoneClock();
			worldIn.setTileEntity(pos, clock);
		}
	}

	/**
	 * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
	 * IBlockstate
	 */
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState();
	}

	@Override
	public boolean canProvidePower(IBlockState state)
	{
		return true;
	}

	/**
	 * Determine if this block can make a redstone connection on the side provided,
	 * Useful to control which sides are inputs and outputs for redstone wires.
	 *
	 * @param world The current world
	 * @param pos Block position in world
	 * @param side The side that is trying to make the connection, CAN BE NULL
	 * @return True to make the connection
	 */
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		/**
		 * Can this block provide power. Only wire currently seems to have this change based on its state.
		 */
		return this.canProvidePower(state) && side != null;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote) 
		{
			player.openGui(WuestUtilities.instance, GuiRedstoneClock.GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}

	/**
	 * How many world ticks before ticking
	 */
	@Override
	public int tickRate(World worldIn)
	{
		return this.tickRate;
	}

	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess worldIn, BlockPos pos,EnumFacing side)
	{
		TileEntityRedstoneClock tileEntity = this.getLocalTileEntity((World)worldIn, pos);

		return tileEntity.getRedstoneStrength(blockState, side);
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		TileEntityRedstoneClock tileEntity = this.getLocalTileEntity((World)worldIn, pos);

		return tileEntity.getRedstoneStrength(blockState, side);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		int i = this.getLocalTileEntity(worldIn, pos).getRedstoneStrength(state, null);
		this.updateState(worldIn, pos, state, i);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntityRedstoneClock tileEntity = this.getLocalTileEntity(worldIn, pos);

		if (tileEntity.getRedstoneStrength(state, null) > 0)
		{
			this.updateNeighbors(worldIn, pos);
		}

		super.breakBlock(worldIn, pos, state);
		worldIn.removeTileEntity(pos);
	}

	@Override
	public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam) 
	{
		super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity == null ? false : tileentity.receiveClientEvent(eventID, eventParam);
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(POWERED, Boolean.valueOf(meta == 1));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((Boolean)state.getValue(POWERED)).booleanValue() ? 1 : 0;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {POWERED});
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityRedstoneClock();
	}

	/**
	 * Gets the redstone clock entity at the current position.
	 * @param worldIn The world to the entity for.
	 * @param pos The position in the world to get the entity for.
	 * @return Null if the tile was not found or if one was found and is not a redstone clock entity. Otherwise the TileEntityRedstoneClock instance.
	 */
	public TileEntityRedstoneClock getLocalTileEntity(World worldIn, BlockPos pos)
	{
		TileEntity entity = worldIn.getTileEntity(pos);

		if (entity != null && entity.getClass() == TileEntityRedstoneClock.class)
		{
			return (TileEntityRedstoneClock) entity;
		}
		else
		{
			TileEntityRedstoneClock tileEntity = new TileEntityRedstoneClock();
			worldIn.setTileEntity(pos, tileEntity);
			tileEntity.setPos(pos);

			return tileEntity;
		}
	}

	/**
	 * Notify block and block below of changes
	 */
	public void updateNeighbors(World worldIn, BlockPos pos)
	{
		this.notifyNeighborsOfStateChange(worldIn, pos, this);
		this.notifyNeighborsOfStateChange(worldIn, pos.down(), this);
	}

	public void notifyNeighborsOfStateChange(World worldIn, BlockPos pos, Block blockType)
	{
		if(net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(worldIn, pos, worldIn.getBlockState(pos), java.util.EnumSet.allOf(EnumFacing.class)).isCanceled())
		{
			return;
		}

		worldIn.notifyBlockOfStateChange(pos.west(), blockType);
		worldIn.notifyBlockOfStateChange(pos.east(), blockType);
		worldIn.notifyBlockOfStateChange(pos.down(), blockType);
		worldIn.notifyBlockOfStateChange(pos.up(), blockType);
		worldIn.notifyBlockOfStateChange(pos.north(), blockType);
		worldIn.notifyBlockOfStateChange(pos.south(), blockType);
	}

	public void updateState(World worldIn, BlockPos pos, IBlockState state, int oldRedstoneStrength)
	{
		int i = 0;

		if (oldRedstoneStrength == 0)
		{
			i = 15;
		}

		TileEntityRedstoneClock tileEntity = this.getLocalTileEntity(worldIn, pos);
		state = tileEntity.setRedstoneStrength(state, i, null);
		worldIn.setBlockState(pos, state, 2);
		this.updateNeighbors(worldIn, pos);
		worldIn.markBlockRangeForRenderUpdate(pos, pos);

		int tickDelay = i == 0 ? tileEntity.getPowerConfiguration().getUnPoweredTick() : tileEntity.getPowerConfiguration().getPoweredTick();

		worldIn.scheduleUpdate(pos, this, tickDelay);
	}
}






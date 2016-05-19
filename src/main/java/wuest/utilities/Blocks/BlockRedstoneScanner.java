package wuest.utilities.Blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import wuest.utilities.WuestUtilities;
import wuest.utilities.Gui.GuiRedstoneScanner;
import wuest.utilities.Proxy.CommonProxy;
import wuest.utilities.Tiles.TileEntityRedstoneScanner;

/**
 * This class is to provide a way to scan for entities and if a particular entity was found, generate a redstone signal.
 * @author WuestMan
 *
 */
public class BlockRedstoneScanner extends Block implements ITileEntityProvider
{
	public static BlockRedstoneScanner RegisteredBlock;
	
	/**
	 * Registers this block in the game registry and adds the block recipe.
	 */
	public static void RegisterBlock()
	{
		//BlockRedstoneScanner.RegisteredBlock = new BlockRedstoneScanner();
		
		//CommonProxy.registerBlock(BlockRedstoneScanner.RegisteredBlock);
		
		//GameRegistry.registerTileEntity(TileEntityRedstoneScanner.class, "RedstoneScanner");
		
		/*
		GameRegistry.addShapedRecipe(new ItemStack(BlockRedstoneScanner.RegisteredBlock), 
				"xyx",
				"zaz",
				"xyx",
				'x', Items.repeater,
				'y', Item.getItemFromBlock(Blocks.redstone_block),
				'z', Item.getItemFromBlock(Blocks.light_weighted_pressure_plate),
				'a', Item.getItemFromBlock(Blocks.redstone_torch));
		
		*/
	}
	
	protected int tickRate = 20;
	
	/**
	 * Initializes a new instance of the BlockMiniRedstone.
	 */
	public BlockRedstoneScanner()
	{
		super(Material.iron, MapColor.tntColor);
		this.setCreativeTab(CreativeTabs.tabRedstone);
		CommonProxy.setBlockName(this, "blockRedstoneScanner");
		this.setHarvestLevel(null, 0);
		this.setHardness(.5f);
		this.setResistance(10.0f);
		this.setStepSound(SoundType.METAL);
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntityRedstoneScanner tileEntity = this.getLocalTileEntity(worldIn, pos);

		if (tileEntity.getRedstoneStrength() > 0)
		{
			this.updateNeighbors(worldIn, pos);
		}

		super.breakBlock(worldIn, pos, state);
		worldIn.removeTileEntity(pos);
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
	
	@Override
	public boolean canProvidePower(IBlockState state)
	{
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityRedstoneScanner();
	}
	
	/**
	 * Gets the redstone scanner entity at the current position.
	 * @param worldIn The world to the entity for.
	 * @param pos The position in the world to get the entity for.
	 * @return Null if the tile was not found or if one was found and is not a redstone scanner entity. Otherwise the TileEntityRedstoneScanner instance.
	 */
	public TileEntityRedstoneScanner getLocalTileEntity(World worldIn, BlockPos pos)
	{
		TileEntity entity = worldIn.getTileEntity(pos);

		if (entity != null && entity.getClass() == TileEntityRedstoneScanner.class)
		{
			return (TileEntityRedstoneScanner) entity;
		}
		else
		{
			TileEntityRedstoneScanner tileEntity = new TileEntityRedstoneScanner();
			worldIn.setTileEntity(pos, tileEntity);
			tileEntity.setPos(pos);

			return tileEntity;
		}
	}
	
	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess worldIn, BlockPos pos,EnumFacing side)
	{
		TileEntityRedstoneScanner tileEntity = this.getLocalTileEntity((World)worldIn, pos);

		return tileEntity.getRedstoneStrength();
	}
	
	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		TileEntityRedstoneScanner tileEntity = this.getLocalTileEntity((World)worldIn, pos);

		return tileEntity.getRedstoneStrength();
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
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

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote) 
		{
			player.openGui(WuestUtilities.instance, GuiRedstoneScanner.GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
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
			TileEntityRedstoneScanner scanner = new TileEntityRedstoneScanner();
			worldIn.setTileEntity(pos, scanner);
		}
	}
	
	@Override
	public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam) 
	{
		super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity == null ? false : tileentity.receiveClientEvent(eventID, eventParam);
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
	
	/**
	 * How many world ticks before ticking
	 */
	@Override
	public int tickRate(World worldIn)
	{
		return this.tickRate;
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		this.updateState(worldIn, pos, state);
	}
	
	/**
	 * Notify block and block below of changes
	 */
	public void updateNeighbors(World worldIn, BlockPos pos)
	{
		this.notifyNeighborsOfStateChange(worldIn, pos, this);
		this.notifyNeighborsOfStateChange(worldIn, pos.down(), this);
	}

	public void updateState(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntityRedstoneScanner tileEntity = this.getLocalTileEntity(worldIn, pos);
		state = tileEntity.setRedstoneStrength(state);
		worldIn.setBlockState(pos, state, 2);
		this.updateNeighbors(worldIn, pos);
		worldIn.markBlockRangeForRenderUpdate(pos, pos);
		worldIn.scheduleUpdate(pos, this, tileEntity.getTickDelay());
	}

}
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
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import wuest.utilities.WuestUtilities;
import wuest.utilities.Gui.GuiRedstoneClock;
import wuest.utilities.Tiles.TileEntityRedstoneClock;

/* 
 * TODO: Add GUI Container to determine tick length and facing power.
 * Should have separate settings for on duration and off duration.
*/
public class RedstoneClock extends Block implements ITileEntityProvider
{
	public static final PropertyBool POWERED = PropertyBool.create("powered");
	public static RedstoneClock RegisteredBlock;
	
	protected int tickRate = 20;
	protected PowerConfiguration powerConfiguration;
	
	/**
	 * A simple block that emits restone signals at regular intervals.
	 */
 	public RedstoneClock() 
	{
		super(Material.wood);
		this.setCreativeTab(CreativeTabs.tabRedstone);
		this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, Boolean.valueOf(true)));
		this.setUnlocalizedName("redstoneClock");
		this.powerConfiguration = new PowerConfiguration();
	}
	
	public static void RegisterBlock()
	{
		RedstoneClock.RegisteredBlock = new RedstoneClock();
		WuestUtilities.ModBlocks.add(RedstoneClock.RegisteredBlock);
		GameRegistry.registerBlock(RedstoneClock.RegisteredBlock, "redstoneClock");
		GameRegistry.registerTileEntity(TileEntityRedstoneClock.class, "redstoneClock_tile_entity");
		
		// Register recipe.
		GameRegistry.addRecipe(new ItemStack(RedstoneClock.RegisteredBlock),
				"xyx",
				"xyy",
				"xxx",
				'x', Item.getItemFromBlock(Blocks.stone),
				'y', Items.redstone);
	}
	
	/**
	 * Gets the power configuration for this block.
	 * @return The instance of power configuration for this class.
	 */
	public PowerConfiguration getPowerConfiguration()
	{
		return this.powerConfiguration;
	}
	
	public void setPowerConfiguration(PowerConfiguration value)
	{
		this.powerConfiguration = value;
	}
	
	@Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        for (EnumFacing enumfacing : EnumFacing.values())
        {
            worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
        }
        
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
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
	public boolean canProvidePower()
	{
		return true;
	}
	
	@Override
    public boolean func_181623_g()
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
	public boolean canConnectRedstone(IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        /**
         * Can this block provide power. Only wire currently seems to have this change based on its state.
         */
        return this.canProvidePower() && side != null;
    }
	
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) 
        {
            player.openGui(WuestUtilities.instance, GuiRedstoneClock.GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
        }
        
        return true;
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
    
    public int getRedstoneStrength(IBlockState state, EnumFacing side)
    {
    	boolean powered = ((Boolean)state.getValue(POWERED)).booleanValue();
    	
    	// If the block is set to powered and this side is powered or the side doesn't matter.
    	if (powered && (side == null || this.powerConfiguration.getSidePower(side.getOpposite())))
    	{
    		return 15;
    	}
    	
    	return 0;
    }
    
    public IBlockState setRedstoneStrength(IBlockState state, int strength, EnumFacing side)
    {
    	if (side != null)
    	{
    		this.powerConfiguration.setSidePower(side, strength > 0);
    	}
    	
        return state.withProperty(POWERED, Boolean.valueOf(strength > 0));
    }
    
    /**
     * How many world ticks before ticking
     */
    @Override
    public int tickRate(World worldIn)
    {
        return this.tickRate;
    }
    
    public void setTickRate(int value)
    {
    	this.tickRate = value;
    }
    
    @Override
    public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        return this.getRedstoneStrength(state, side);
    }
    
    @Override
    public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        return this.getRedstoneStrength(state, side);
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
    	
    	return null;
    }
    
    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
    	int i = this.getRedstoneStrength(state, null);
    	this.updateState(worldIn, pos, state, i);
    }
    
    public void updateState(World worldIn, BlockPos pos, IBlockState state, int oldRedstoneStrength)
    {
        int i = 0;
        
        if (oldRedstoneStrength == 0)
        {
        	i = 15;
        }
        
        state = this.setRedstoneStrength(state, i, null);
        worldIn.setBlockState(pos, state, 2);
        this.updateNeighbors(worldIn, pos);
        worldIn.markBlockRangeForRenderUpdate(pos, pos);

        int tickDelay = i == 0 ? this.powerConfiguration.getUnPoweredTick() : this.powerConfiguration.getPoweredTick();
        
        worldIn.scheduleUpdate(pos, this, tickDelay);
    }
    
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (this.getRedstoneStrength(state, null) > 0)
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
    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {POWERED});
    }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		TileEntityRedstoneClock entity = new TileEntityRedstoneClock();
		entity.setPowerConfiguration(this.powerConfiguration);
		
		return entity;
	}
    
    public static class PowerConfiguration
    {
    	private HashMap<EnumFacing, Boolean> facingPower = new HashMap<EnumFacing, Boolean>();
    	private int poweredTick = 20;
    	private int unPoweredTick = 20;
    	
    	/**
    	 * Initializes a new instance of the PowerConfiguration class.
    	 */
    	public PowerConfiguration()
    	{
    		for (EnumFacing facing : EnumFacing.values())
    		{
    			this.facingPower.put(facing, true);
    		}
    	}
    	
    	/**
    	 * Gets the powered tick value.
    	 * @return The amount of ticks a redstone clock should be powered.
    	 */
    	public int getPoweredTick()
    	{
    		return this.poweredTick;
    	}
    	
    	/**
    	 * Sets the amount of ticks the block should be powered.
    	 * @param value The amount of powered ticks. If the value is less than 1 the value will be 1. 
    	 */
    	public void setPoweredTick(int value)
    	{
    		if (value <= 0)
    		{
    			value = 1;
    		}
    		
    		this.poweredTick = value;
    	}
    	
    	/**
    	 * Gets the unpowered tick value.
    	 * @return The amount of ticks a redstone clock should be unpowered.
    	 */
    	public int getUnPoweredTick()
    	{
    		return this.unPoweredTick;
    	}
    	
    	/**
    	 * Sets the amount of ticks the block should be unpowered.
    	 * @param value The amount of unpowered ticks. If the value is less than 1 the value will be 1.
    	 */
    	public void setUnPoweredTick(int value)
    	{
    		if (value < 0)
    		{
    			value = 1;
    		}
    		
    		this.unPoweredTick = value;
    	}
    
    	/**
    	 * Gets the power for a particular side.
    	 * @param facing The side to get the power for.
    	 * @return The power for the given side.
    	 */
    	public boolean getSidePower(EnumFacing facing)
    	{
    		return this.facingPower.get(facing);
    	}
    	
    	/**
    	 * Gets the sides which are producing power.
    	 * @return The sides which are sending power.
    	 */
    	public ArrayList<EnumFacing> getPoweredSides()
    	{
    		ArrayList<EnumFacing> poweredFacings = new ArrayList<EnumFacing>();
    		
    		for (Entry<EnumFacing, Boolean> facing : this.facingPower.entrySet())
    		{
    			if (facing.getValue())
    			{
    				poweredFacings.add(facing.getKey());
    			}
    		}
    		
    		return poweredFacings;
    	}
    	
    	/**
    	 * Sets whether a side is powered.
    	 * @param facing The side to set.
    	 * @param value The value determining if the side is powered.
    	 */
    	public void setSidePower(EnumFacing facing, boolean value)
    	{
    		this.facingPower.put(facing, value);
    	}
    
    	public void WriteToNBTCompound(NBTTagCompound compound)
    	{
    		// Add the power configuration tag.
    		NBTTagCompound powerCompound = new NBTTagCompound();
    		powerCompound.setInteger("poweredTick", this.poweredTick);
    		powerCompound.setInteger("unPoweredTick", this.unPoweredTick);
    		
    		for (Entry<EnumFacing, Boolean> entry : this.facingPower.entrySet())
    		{
    			powerCompound.setBoolean(entry.getKey().getName2(), entry.getValue());
    		}
    		
    		compound.setTag("powerTag", powerCompound);
    	}
    	
    	public static PowerConfiguration ReadFromNBTTagCompound(NBTTagCompound compound)
    	{
    		PowerConfiguration configuration = new PowerConfiguration();
    		
    		if (compound.hasKey("powerTag"))
    		{
    			NBTTagCompound powerCompound = compound.getCompoundTag("powerTag");
    			
    			configuration.poweredTick = powerCompound.getInteger("poweredTick");
    			configuration.unPoweredTick = powerCompound.getInteger("unPoweredTick");
    			
    			for (EnumFacing facing : EnumFacing.values())
    			{
    				configuration.facingPower.put(facing, powerCompound.getBoolean(facing.getName2()));
    			}
    		}
    		
    		return configuration;
    	}
    }
}
    
    

    


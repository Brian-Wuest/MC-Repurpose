package wuest.utilities.Blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

/* 
 * TODO: Add GUI Container to determine tick length and facing power.
 * Should have separate settings for on duration and off duration.
*/
public class RedstoneClock extends Block
{
	public static final PropertyBool POWERED = PropertyBool.create("powered");
	public static RedstoneClock RegisteredBlock;
	
	private int tickRate = 20;
	
	/**
	 * A simple block that emits restone signals at regular intervals.
	 */
	public RedstoneClock() 
	{
		super(Material.wood);
		this.setCreativeTab(CreativeTabs.tabRedstone);
		this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, Boolean.valueOf(true)));
		this.setUnlocalizedName("redstoneClock");
	}
	
	public static void RegisterBlock()
	{
		RedstoneClock.RegisteredBlock = new RedstoneClock();
		GameRegistry.registerBlock(RedstoneClock.RegisteredBlock, "redstoneClock");
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
        
        // TODO: Only notify neighbors on powered sides.
        worldIn.notifyBlockOfStateChange(pos.west(), blockType);
        worldIn.notifyBlockOfStateChange(pos.east(), blockType);
        worldIn.notifyBlockOfStateChange(pos.down(), blockType);
        worldIn.notifyBlockOfStateChange(pos.up(), blockType);
        worldIn.notifyBlockOfStateChange(pos.north(), blockType);
        worldIn.notifyBlockOfStateChange(pos.south(), blockType);
    }
    
    public int getRedstoneStrength(IBlockState state, EnumFacing side)
    {
    	// TODO: Get the strength for the appropriate side. Null get general power 15.
        return ((Boolean)state.getValue(POWERED)).booleanValue() ? 15 : 0;
    }
    
    public IBlockState setRedstoneStrength(IBlockState state, int strength, EnumFacing side)
    {
    	// TODO: Set the strength for the appropriate side. Null set power 15 to all sides.
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

        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }
    
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (this.getRedstoneStrength(state, null) > 0)
        {
            this.updateNeighbors(worldIn, pos);
        }

        super.breakBlock(worldIn, pos, state);
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
}

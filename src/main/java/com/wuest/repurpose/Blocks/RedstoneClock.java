package com.wuest.repurpose.Blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Base.TileBlockBase;
import com.wuest.repurpose.Gui.GuiRedstoneClock;
import com.wuest.repurpose.Items.ItemBedCompass;
import com.wuest.repurpose.Proxy.CommonProxy;
import com.wuest.repurpose.Tiles.TileEntityRedstoneClock;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RedstoneClock extends TileBlockBase<TileEntityRedstoneClock>
{
	public static final PropertyBool POWERED = PropertyBool.create("powered");
	protected int tickRate = 20;

	/**
	 * A simple block that emits redstone signals at regular intervals.
	 */
	public RedstoneClock(String name) 
	{
		super(Material.IRON, MapColor.TNT);
		this.setCreativeTab(CreativeTabs.REDSTONE);
		this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, Boolean.valueOf(true)));
		ModRegistry.setBlockName(this, name);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote) 
		{
			player.openGui(Repurpose.instance, GuiRedstoneClock.GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
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
	public void customBreakBlock(TileEntityRedstoneClock tileEntity, World worldIn, BlockPos pos, IBlockState state)
	{
		if (tileEntity.getRedstoneStrength(state, null) > 0)
		{
			this.updateNeighbors(worldIn, pos);
		}
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

	/**
	 * Notify block and block below of changes
	 */
	public void updateNeighbors(World worldIn, BlockPos pos)
	{
		this.notifyNeighborsOfStateChange(worldIn, pos, this);
	}

	public void notifyNeighborsOfStateChange(World worldIn, BlockPos pos, Block blockType)
	{
		if (net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(worldIn, pos, worldIn.getBlockState(pos), java.util.EnumSet.allOf(EnumFacing.class), true).isCanceled())
		{
			return;
		}

		for (EnumFacing enumfacing : EnumFacing.values())
        {
            worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, true);
        }
	}

	/**
	 * Processes custom update state.
	 * @param worldIn The world this state is being updated in.
	 * @param pos The block position.
	 * @param state The block state.
	 * @param tileEntity The tile entity associated with this class.
	 * @return The number of ticks to delay until the next update.
	 */
	@Override
	public int customUpdateState(World worldIn, BlockPos pos, IBlockState state, TileEntityRedstoneClock tileEntity)
	{
		// Get the old redstone strength.
		int i = tileEntity.getRedstoneStrength(state, null) == 0 ? 15 : 0;
		
		// If this is going to be powered, check to make sure that it should be powered.
		if (i != 0)
		{
			// Check each side of this block to see if it's powered.
			for (EnumFacing facing : EnumFacing.values())
			{
				if (worldIn.isSidePowered(pos.offset(facing), facing.getOpposite()))
				{
					i = 0;
					tileEntity.setRedstoneStrength(state, i, null);
					worldIn.setBlockState(pos, state, 3);
					
					this.updateNeighbors(worldIn, pos);
					
					// Delay a few ticks.
					return 5;
				}
			}
		}
		
		// Set the new redstone strength and provide an updated state.
		state = tileEntity.setRedstoneStrength(state, i, null);
		worldIn.setBlockState(pos, state, 3);
		
		this.updateNeighbors(worldIn, pos);
		
		return i == 0 ? tileEntity.getConfig().getUnPoweredTick() : tileEntity.getConfig().getPoweredTick();
	}
	
    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced)
    {
    	super.addInformation(stack, world, tooltip, advanced);
    	
    	boolean advancedKeyDown = Minecraft.getMinecraft().currentScreen.isShiftKeyDown();
    	
    	if (!advancedKeyDown)
    	{
    		tooltip.add("Hold" + TextFormatting.BLUE + " Shift " + TextFormatting.GRAY +  "for advanced information.");
    	}
    	else
    	{
    		tooltip.add("This simple clock is able to produce a redstone signal at configurable intervals.  Right Click to configure.");
    	}
    }
}
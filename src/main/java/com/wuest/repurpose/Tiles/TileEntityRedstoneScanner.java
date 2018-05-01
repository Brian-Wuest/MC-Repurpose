package com.wuest.repurpose.Tiles;

import java.util.List;

import com.wuest.repurpose.Base.TileEntityBase;
import com.wuest.repurpose.Blocks.BlockRedstoneScanner;
import com.wuest.repurpose.Config.RedstoneScannerConfig;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.INpc;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This class is the TileEntity responsible for a lot of the work done with the Redstone Scanner.
 * @author WuestMan
 */
public class TileEntityRedstoneScanner extends TileEntityBase<RedstoneScannerConfig>
{
	protected boolean foundEntity = false;
	
	/**
	 * Initializes a new instance of the TileEntityRedstoneScanner class.
	 */
	public TileEntityRedstoneScanner()
	{
		super();
		this.config = new RedstoneScannerConfig();
	}

	/**
	 * This method is used to determine if an entity was found within the scanning range.
	 * @param state - The current blocks state.
	 * @return The redstone strength the block associated with this tile entity should provide.
	 */
	public int getRedstoneStrength()
	{
		return this.foundEntity ? 15 : 0;
	}
	
	/**
	 * Determines the tick delay from the block configuration.
	 * @return The tick delay from the configuration.
	 */
	public int getTickDelay()
	{
		return this.config.getTickDelay();
	}

	/**
	 * This is the initial method used to start the scan.
	 * The scan distance and sides are based on the configuration.
	 * @param state The curent state of the block.
	 * @return An un-modified state if there was nothing to change. Otherwise this method will provide a powered or unpowered state.
	 */
	public IBlockState setRedstoneStrength(IBlockState state)
	{
		this.ScanForEntities();
		return state.withProperty(BlockRedstoneScanner.POWERED, Boolean.valueOf(this.foundEntity));
	}

	/**
	 * Processes the scanning, sets the class level bool for determining if an entity was found and if the block associated with this tile should provide power.
	 */
	protected void ScanForEntities()
	{
		int verticalRange = (this.config.IsFacingActive(EnumFacing.UP) ? this.config.GetFacingScanLength(EnumFacing.UP) : 0) 
				+ (this.config.IsFacingActive(EnumFacing.DOWN) ? this.config.GetFacingScanLength(EnumFacing.DOWN) : 0);
		
		// Gets the starting position. Start at the highest point and work down.
		BlockPos startingPos = this.pos.up((this.config.IsFacingActive(EnumFacing.UP) 
				? this.config.GetFacingScanLength(EnumFacing.UP) : 0));
		
		int northScanRange = this.config.IsFacingActive(EnumFacing.NORTH) ? this.config.GetFacingScanLength(EnumFacing.NORTH) : 0;
		int eastScanRange = this.config.IsFacingActive(EnumFacing.EAST) ? this.config.GetFacingScanLength(EnumFacing.EAST) : 0;
		int southScanRange = this.config.IsFacingActive(EnumFacing.SOUTH) ? this.config.GetFacingScanLength(EnumFacing.SOUTH) : 0;
		int westScanRange = this.config.IsFacingActive(EnumFacing.WEST) ? this.config.GetFacingScanLength(EnumFacing.WEST) : 0;
		boolean foundATarget = false;
		
		// Loop through each Y level for scanning.
		for (int i = 0; i <= verticalRange; i++)
		{
			foundATarget = this.ScanLevel(northScanRange, eastScanRange, southScanRange, westScanRange, startingPos);
			
			//BlockPos.getAllInBox(from, to)
			// Break if a target was found.
			if (foundATarget)
			{
				break;
			}
			
			// After scanning this level, go down a level.
			startingPos = startingPos.down();
		}
		
		// Update the boolean.
		this.foundEntity = foundATarget;
	}
	
	/**
	 * Scans this Y level for the targeted entities.
	 * @param northScanRange The north axis scanning range.
	 * @param eastScanRange The east axis scanning range.
	 * @param southScanRange The south axis scanning range.
	 * @param westScanRange The west axis scanning range.
	 * @param startingPos The initial starting position.
	 * @return True if a target entity was found, otherwise false.
	 */
	protected boolean ScanLevel(int northScanRange, int eastScanRange, int southScanRange, int westScanRange, BlockPos startingPos)
	{
		boolean foundATarget = false;
		
		BlockPos endingPos = startingPos;
		
		if (northScanRange > 0)
		{
			startingPos = startingPos.north(northScanRange);
		}
		
		if (eastScanRange > 0)
		{
			startingPos = startingPos.east(eastScanRange);
		}
		
		if (southScanRange > 0)
		{
			endingPos = endingPos.south(southScanRange);
		}
		
		if (westScanRange > 0)
		{
			endingPos = endingPos.west(westScanRange);
		}
		
		// We have the 2 corners.
		for (BlockPos currentPos : BlockPos.getAllInBox(startingPos, endingPos))
		{
			// We want to include the full block when trying to get the entities within this block position.
			AxisAlignedBB axisPos = Block.FULL_BLOCK_AABB.offset(currentPos);
			List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity((Entity)null, axisPos);
			
	        if (!list.isEmpty())
	        {
	        	// The first entity found matching the searched for targets will trigger the power.
	            for (Entity entity : list)
	            {
	            	if (this.config.getAnimalsDetected() && entity instanceof IAnimals)
	            	{
	            		foundATarget = true;
	            		break;
	            	}
	            	
	            	if (this.config.getNonPlayersDetected() && entity instanceof INpc)
	            	{
	            		foundATarget = true;
	            		break;
	            	}
	            	
	            	if (this.config.getMonstersDetected() && entity instanceof IMob)
	            	{
	            		foundATarget = true;
	            		break;
	            	}
	            	
	            	if (this.config.getPlayersDetected() && entity instanceof EntityPlayer)
	            	{
	            		foundATarget = true;
	            		break;
	            	}
	            }
	        }
	        
	        if (foundATarget)
	        {
	        	break;
	        }
		}
		
		return foundATarget;
	}
}
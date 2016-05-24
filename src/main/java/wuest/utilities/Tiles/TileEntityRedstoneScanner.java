package wuest.utilities.Tiles;

import java.util.List;

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
import wuest.utilities.Config.RedstoneScannerConfig;

/**
 * This class is the TileEntity responsible for a lot of the work done with the Redstone Scanner.
 * @author WuestMan
 */
public class TileEntityRedstoneScanner extends TileEntity
{
	protected RedstoneScannerConfig config;
	protected boolean foundEntity = false;
	
	/**
	 * Initializes a new instance of the TileEntityRedstoneScanner class.
	 */
	public TileEntityRedstoneScanner()
	{
		this.config = new RedstoneScannerConfig();
	}

	/**
	 * Gets the configuration.
	 * @return The redstone scanner configuration.
	 */
	public RedstoneScannerConfig getConfig()
	{
		return this.config;
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
	 * Allows for a specialized description packet to be created. This is often used to sync tile entity data from the
	 * server to the client easily. For example this is used by signs to synchronise the text to be displayed.
	 */
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		// Don't send the packet until the position has been set.
		if (this.pos.getX() == 0 && this.pos.getY() == 0 && this.pos.getZ() == 0)
		{
			return super.getUpdatePacket();
		}

		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);

		return new SPacketUpdateTileEntity(this.getPos(), 1, tag);
	}

	/**
	 * Called when the chunk this TileEntity is on is Unloaded.
	 */
	@Override
	public void onChunkUnload()
	{
		// Make sure to write the tile to the tag.
		this.writeToNBT(this.getTileData());
	}
	
	@Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

	/**
	 * Called when you receive a TileEntityData packet for the location this
	 * TileEntity is currently in. On the client, the NetworkManager will always
	 * be the remote server. On the server, it will be whomever is responsible for
	 * sending the packet.
	 *
	 * @param net The NetworkManager the packet originated from
	 * @param pkt The data packet
	 */
	@Override
	public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SPacketUpdateTileEntity pkt)
	{
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		//System.out.println("Reading Scanner config.");
		super.readFromNBT(compound);

		if (compound.hasKey("configCompound"))
		{
			this.config = RedstoneScannerConfig.ReadFromNBTTagCompound(compound);
		}
	}

	@Override
	public boolean receiveClientEvent(int id, int type)
	{
		return true;
	}
	
	/**
	 * Sets the configuration to the passed in value.
	 * @param value The new redstone configuration.
	 */
	public void setConfig(RedstoneScannerConfig value)
	{
		this.config = value;
		
		// Make sure to mark this as dirty so it's saved.
		this.markDirty();
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
	}

	/**
	 * Called from Chunk.setBlockIDWithMetadata and Chunk.fillChunk, determines if this tile entity should be re-created when the ID, or Metadata changes.
	 * Use with caution as this will leave straggler TileEntities, or create conflicts with other TileEntities if not used properly.
	 *
	 * @param world Current world
	 * @param pos Tile's world position
	 * @param oldState The old ID of the block
	 * @param newState The new ID of the block (May be the same)
	 * @return true forcing the invalidation of the existing TE, false not to invalidate the existing TE
	 */
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		// This tile needs to persist so the data can be saved.
		return (oldState.getBlock() != newSate.getBlock());
	}
	
	@Override
	public void updateContainingBlockInfo()
	{
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		//System.out.println("Writing scanner config.");
		super.writeToNBT(compound);

		this.config.WriteToNBTCompound(compound);
		
		return compound;
	}

	/**
	 * Processes the scanning, sets the class level bool for determining if an entity was found and if the block associated with this tile should provide power.
	 */
	protected void ScanForEntities()
	{
		// TODO: Finish scan processing.
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
			List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity((Entity)null, axisPos);
			
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
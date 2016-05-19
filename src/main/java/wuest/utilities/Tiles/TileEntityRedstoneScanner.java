package wuest.utilities.Tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wuest.utilities.Config.RedstoneScannerConfig;

/**
 * This class is the TileEntity responsible for alot of the work done with the Redstone Scanner.
 * @author WuestMan
 *
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
	 * Gets the configuration.
	 * @return The restone scanner configuration.
	 */
	public RedstoneScannerConfig getConfig()
	{
		return this.config;
	}
	
	/**
	 * Sets the configuration to the passed in value.
	 * @param value The new redstone configuration.
	 */
	public void setConfig(RedstoneScannerConfig value)
	{
		this.config = value;
	}
	
	/**
	 * This is the initial method used to start the scan.
	 * The scan distance and sides are based on the configuration.
	 * @param state The curent state of the block.
	 * @return An un-modified state if there was nothing to change. Otherwise this method will provide a powered or unpowered state.
	 */
	public IBlockState setRedstoneStrength(IBlockState state)
	{
		// TODO Auto-generated method stub
		return state;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{		
		super.writeToNBT(compound);

		this.config.WriteToNBTCompound(compound);
		
		return compound;
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
	public boolean receiveClientEvent(int id, int type)
	{
		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		super.readFromNBT(compound);

		this.config = RedstoneScannerConfig.ReadFromNBTTagCompound(compound);
	}

	@Override
	public void updateContainingBlockInfo()
	{
	}
}
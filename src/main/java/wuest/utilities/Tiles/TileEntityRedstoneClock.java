package wuest.utilities.Tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wuest.utilities.Blocks.RedstoneClock;
import wuest.utilities.Config.*;

public class TileEntityRedstoneClock extends TileEntity 
{
	protected RedstoneClockPowerConfiguration powerConfiguration;

	public TileEntityRedstoneClock()
	{
		this.powerConfiguration = new RedstoneClockPowerConfiguration();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{		
		super.writeToNBT(compound);

		this.powerConfiguration.WriteToNBTCompound(compound);
		
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

	@Override
	public boolean receiveClientEvent(int id, int type)
	{
		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		super.readFromNBT(compound);

		this.powerConfiguration = RedstoneClockPowerConfiguration.ReadFromNBTTagCompound(compound);
	}

	@Override
	public void updateContainingBlockInfo()
	{
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
	 * Gets the power configuration for this tile.
	 * @return The power configuration for this tile.
	 */
	public RedstoneClockPowerConfiguration getPowerConfiguration()
	{
		return this.powerConfiguration;
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

	/**
	 * Sets the power configuration for this tile.
	 * @param value The value of the power configuration to set.
	 */
	public void setPowerConfiguration(RedstoneClockPowerConfiguration value)
	{
		this.powerConfiguration = value;

		// Make sure to mark this as dirty so it's saved.
		this.markDirty();
	}

	public int getRedstoneStrength(IBlockState state, EnumFacing side)
	{
		boolean powered = ((Boolean)state.getValue(RedstoneClock.POWERED)).booleanValue();

		// If the block is set to powered and this side is powered or the side doesn't matter.
		if (powered && (side == null || this.getPowerConfiguration().getSidePower(side.getOpposite())))
		{
			return 15;
		}

		return 0;
	}

	public IBlockState setRedstoneStrength(IBlockState state, int strength, EnumFacing side)
	{
		if (side != null)
		{
			this.getPowerConfiguration().setSidePower(side, strength > 0);
		}

		return state.withProperty(RedstoneClock.POWERED, Boolean.valueOf(strength > 0));
	}
}

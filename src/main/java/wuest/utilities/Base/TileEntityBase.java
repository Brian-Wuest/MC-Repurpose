package wuest.utilities.Base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This is the base tile entity used by the mod.
 * @author WuestMan
 *
 * @param <T> The base configuration used by this tile entity.
 */
public abstract class TileEntityBase<T extends BaseConfig> extends TileEntity
{
	protected T config;
	
	/**
	 * @return Gets the configuration class used by this tile entity.
	 */
	public T getConfig()
	{
		return this.config;
	}
	
	/**
	 * Sets the configuration class used by this tile entity.
	 * @param value The updated tile entity.
	 */
	public void setConfig(T value)
	{
		this.config = value;
		this.markDirty();
	}
	
    @SuppressWarnings ("unchecked")
    public Class<T> getTypeParameterClass()
    {
        Type type = getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) type;
        return (Class<T>) paramType.getActualTypeArguments()[0];
    }
	
	/**
	 * Allows for a specialized description packet to be created. This is often used to sync tile entity data from the
	 * server to the client easily. For example this is used by signs to synchronize the text to be displayed.
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
	public boolean receiveClientEvent(int id, int type)
	{
		return true;
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
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		//System.out.println("Writing Clock Data.");
		super.writeToNBT(compound);

		this.config.WriteToNBTCompound(compound);
		
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		//System.out.println("Reading Tag Data.");
		super.readFromNBT(compound);

		try
		{
			this.config = this.getTypeParameterClass().newInstance().ReadFromNBTTagCompound(compound);
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

}

package wuest.utilities.Tiles;

import wuest.utilities.WuestUtilities;
import wuest.utilities.Blocks.RedstoneClock;
import wuest.utilities.Blocks.RedstoneClock.PowerConfiguration;
import wuest.utilities.Gui.GuiRedstoneClock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class TileEntityRedstoneClock extends TileEntity 
{
	protected RedstoneClock.PowerConfiguration powerConfiguration;
	
	public TileEntityRedstoneClock()
	{
	}
	
	/**
	 * Gets the power configuration for this tile.
	 * @return The power configuration for this tile.
	 */
	public RedstoneClock.PowerConfiguration getPowerConfiguration()
	{
		return this.powerConfiguration;
	}
	
	/**
	 * Sets the power configuration for this tile.
	 * @param value The value of the power configuration to set.
	 */
	public void setPowerConfiguration(RedstoneClock.PowerConfiguration value)
	{
		this.powerConfiguration = value;
		
		// Make sure to mark this as dirty so it's saved.
		this.markDirty();
	}
	
	@Override
    public void writeToNBT(NBTTagCompound compound) 
	{		
        super.writeToNBT(compound);
        
		this.powerConfiguration.WriteToNBTCompound(compound);
	}
	
	@Override
	public void onLoad()
	{
		this.readFromNBT(this.getTileData());
		
		if (this.blockType != null)
		{
			RedstoneClock clock = (RedstoneClock)this.blockType;
			
			clock.localTileEntity = this;
		}
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
    public Packet getDescriptionPacket() 
    {
    	S35PacketUpdateTileEntity packet = (S35PacketUpdateTileEntity) super.getDescriptionPacket();
        NBTTagCompound tag = packet != null ? packet.getNbtCompound() : new NBTTagCompound();

        this.writeToNBT(tag);

        return new S35PacketUpdateTileEntity(this.getPos(), 1, tag);
    }
    
    @Override
    public boolean receiveClientEvent(int id, int type)
    {
        return true;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) 
    {
        super.onDataPacket(net, pkt);
        
        NBTTagCompound tag = pkt.getNbtCompound();
        this.readFromNBT(tag);
    }
	
	@Override
    public void readFromNBT(NBTTagCompound compound) 
	{
		super.readFromNBT(compound);
		
		this.powerConfiguration = PowerConfiguration.ReadFromNBTTagCompound(compound);
	}
}

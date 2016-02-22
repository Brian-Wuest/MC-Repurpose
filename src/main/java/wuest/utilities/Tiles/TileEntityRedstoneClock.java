package wuest.utilities.Tiles;

import wuest.utilities.WuestUtilities;
import wuest.utilities.Blocks.RedstoneClock;
import wuest.utilities.Blocks.RedstoneClock.PowerConfiguration;
import wuest.utilities.Gui.GuiRedstoneClock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class TileEntityRedstoneClock extends TileEntity 
{
	protected RedstoneClock.PowerConfiguration powerConfiguration;
	
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
			
			clock.setPowerConfiguration(this.powerConfiguration);
		}
	}
	
	@Override
    public void readFromNBT(NBTTagCompound compound) 
	{
        super.readFromNBT(compound);
        
        this.powerConfiguration = PowerConfiguration.ReadFromNBTTagCompound(compound);
	}
}

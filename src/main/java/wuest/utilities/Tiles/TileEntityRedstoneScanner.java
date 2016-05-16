package wuest.utilities.Tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
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
		// TODO Auto-generated method stub
		return state;
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) 
	{		
		super.writeToNBT(compound);

		this.config.WriteToNBTCompound(compound);
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

		this.config = RedstoneScannerConfig.ReadFromNBTTagCompound(compound);
	}

	@Override
	public void updateContainingBlockInfo()
	{
	}
}
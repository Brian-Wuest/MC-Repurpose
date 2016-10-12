package com.wuest.utilities.Tiles;

import com.wuest.utilities.Base.TileEntityBase;
import com.wuest.utilities.Blocks.RedstoneClock;
import com.wuest.utilities.Config.*;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This is the tile entity which controls the redstone strength and holds the configuration data for the redstone clock.
 * @author WuestMan
 */
public class TileEntityRedstoneClock extends TileEntityBase<RedstoneClockPowerConfiguration>
{
	/**
	 * Initializes a new instance of the TileEntityRedstoneClock class.
	 */
	public TileEntityRedstoneClock()
	{
		super();
		this.config = new RedstoneClockPowerConfiguration();
	}

	/**
	 * Gets the redstone strength for this state and side.
	 * @param state The current state of the block.
	 * @param side The facing to get the power from.
	 * @return 15 if the side and block are powered, otherwise 0;
	 */
	public int getRedstoneStrength(IBlockState state, EnumFacing side)
	{
		boolean powered = ((Boolean)state.getValue(RedstoneClock.POWERED)).booleanValue();

		// If the block is set to powered and this side is powered or the side doesn't matter.
		if (powered && (side == null || this.getConfig().getSidePower(side.getOpposite())))
		{
			return 15;
		}

		return 0;
	}

	/**
	 * Sets the redstone strength for a particular side and returns a powered or unpowered state.
	 * @param state The state to return as powered or unpowered.
	 * @param strength The new strength of the side.
	 * @param side The side to update.
	 * @return An updated state.
	 */
	public IBlockState setRedstoneStrength(IBlockState state, int strength, EnumFacing side)
	{
		if (side != null)
		{
			this.config.setSidePower(side, strength > 0);
		}

		return state.withProperty(RedstoneClock.POWERED, Boolean.valueOf(strength > 0));
	}
}

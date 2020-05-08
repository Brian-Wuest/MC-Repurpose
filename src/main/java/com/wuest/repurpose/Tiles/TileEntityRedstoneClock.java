package com.wuest.repurpose.Tiles;

import com.wuest.repurpose.Base.TileEntityBase;
import com.wuest.repurpose.Blocks.RedstoneClock;
import com.wuest.repurpose.Config.RedstoneClockPowerConfiguration;
import com.wuest.repurpose.ModRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

import java.util.Arrays;
import java.util.HashSet;

/**
 * This is the tile entity which controls the redstone strength and holds the
 * configuration data for the redstone clock.
 *
 * @author WuestMan
 */
public class TileEntityRedstoneClock extends TileEntityBase<RedstoneClockPowerConfiguration> {
	public static TileEntityType<TileEntityRedstoneClock> TileType = null;

	/**
	 * Initializes a new instance of the TileEntityRedstoneClock class.
	 */
	public TileEntityRedstoneClock() {
		this(TileEntityRedstoneClock.TileType);
	}

	public TileEntityRedstoneClock(TileEntityType<?> tileEntityType) {
		super(tileEntityType);
		this.config = new RedstoneClockPowerConfiguration();
	}

	/**
	 * Gets the redstone strength for this state and side.
	 *
	 * @param state The current state of the block.
	 * @param side  The facing to get the power from.
	 * @return 15 if the side and block are powered, otherwise 0;
	 */
	public int getRedstoneStrength(BlockState state, Direction side) {
		boolean powered = state.get(RedstoneClock.POWERED);

		// If the block is set to powered and this side is powered or the side doesn't
		// matter.
		if (powered && (side == null || this.getConfig().getSidePower(side.getOpposite()))) {
			return 15;
		}

		return 0;
	}

	/**
	 * Sets the redstone strength for a particular side and returns a powered or
	 * unpowered state.
	 *
	 * @param state    The state to return as powered or unpowered.
	 * @param strength The new strength of the side.
	 * @param side     The side to update.
	 * @return An updated state.
	 */
	public BlockState setRedstoneStrength(BlockState state, int strength, Direction side) {
		if (side != null) {
			this.config.setSidePower(side, strength > 0);
		}

		return state.with(RedstoneClock.POWERED, Boolean.valueOf(strength > 0));
	}
}

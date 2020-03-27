package com.wuest.repurpose.Config;

import com.wuest.repurpose.Base.BaseConfig;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class RedstoneClockPowerConfiguration extends BaseConfig {
	private HashMap<Direction, Boolean> facingPower = new HashMap<Direction, Boolean>();
	private int poweredTick = 20;
	private int unPoweredTick = 20;
	private BlockPos pos;

	/**
	 * Initializes a new instance of the RedstoneClockPowerConfiguration class.
	 */
	public RedstoneClockPowerConfiguration() {
		for (Direction facing : Direction.values()) {
			this.facingPower.put(facing, true);
		}
	}

	/**
	 * Gets the powered tick value.
	 *
	 * @return The amount of ticks a redstone clock should be powered.
	 */
	public int getPoweredTick() {
		return this.poweredTick;
	}

	/**
	 * Sets the amount of ticks the block should be powered.
	 *
	 * @param value The amount of powered ticks. If the value is less than 1 the
	 *              value will be 1.
	 */
	public void setPoweredTick(int value) {
		if (value <= 0) {
			value = 1;
		}

		this.poweredTick = value;
	}

	/**
	 * Gets the unpowered tick value.
	 *
	 * @return The amount of ticks a redstone clock should be unpowered.
	 */
	public int getUnPoweredTick() {
		return this.unPoweredTick;
	}

	/**
	 * Sets the amount of ticks the block should be unpowered.
	 *
	 * @param value The amount of unpowered ticks. If the value is less than 1 the
	 *              value will be 1.
	 */
	public void setUnPoweredTick(int value) {
		if (value < 0) {
			value = 1;
		}

		this.unPoweredTick = value;
	}

	/**
	 * Gets the power for a particular side.
	 *
	 * @param facing The side to get the power for.
	 * @return The power for the given side.
	 */
	public boolean getSidePower(Direction facing) {
		return this.facingPower.get(facing);
	}

	/**
	 * Gets the sides which are producing power.
	 *
	 * @return The sides which are sending power.
	 */
	public ArrayList<Direction> getPoweredSides() {
		ArrayList<Direction> poweredFacings = new ArrayList<Direction>();

		for (Entry<Direction, Boolean> facing : this.facingPower.entrySet()) {
			if (facing.getValue()) {
				poweredFacings.add(facing.getKey());
			}
		}

		return poweredFacings;
	}

	/**
	 * Gets the block position.
	 *
	 * @return The block position for this configuration.
	 */
	public BlockPos getPos() {
		return this.pos;
	}

	/**
	 * Sets the block position for this configuration.
	 *
	 * @param value The block position.
	 */
	public void setPos(BlockPos value) {
		this.pos = value;
	}

	/**
	 * Sets whether a side is powered.
	 *
	 * @param facing The side to set.
	 * @param value  The value determining if the side is powered.
	 */
	public void setSidePower(Direction facing, boolean value) {
		this.facingPower.put(facing, value);
	}

	@Override
	public void WriteToNBTCompound(CompoundNBT compound) {
		CompoundNBT powerCompound = new CompoundNBT();

		// Add the power configuration tag.
		powerCompound.putInt("poweredTick", this.poweredTick);
		powerCompound.putInt("unPoweredTick", this.unPoweredTick);

		for (Entry<Direction, Boolean> entry : this.facingPower.entrySet()) {
			powerCompound.putBoolean(entry.getKey().getName2(), entry.getValue());
		}

		if (this.pos != null) {
			powerCompound.putInt("x", this.pos.getX());
			powerCompound.putInt("y", this.pos.getY());
			powerCompound.putInt("z", this.pos.getZ());
		}

		compound.put("powerCompound", powerCompound);
	}

	public RedstoneClockPowerConfiguration ReadFromCompoundNBT(CompoundNBT compound) {
		RedstoneClockPowerConfiguration configuration = new RedstoneClockPowerConfiguration();

		if (compound.contains("powerCompound")) {
			CompoundNBT powerCompound = compound.getCompound("powerCompound");

			configuration.poweredTick = powerCompound.getInt("poweredTick");
			configuration.unPoweredTick = powerCompound.getInt("unPoweredTick");

			for (Direction facing : Direction.values()) {
				configuration.facingPower.put(facing, powerCompound.getBoolean(facing.getName2()));
			}

			if (powerCompound.contains("x")) {
				configuration.pos = new BlockPos(powerCompound.getInt("x"), powerCompound.getInt("y"),
						powerCompound.getInt("z"));
			}
		}

		return configuration;
	}
}
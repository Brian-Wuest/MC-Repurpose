package wuest.utilities.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import wuest.utilities.Base.BaseConfig;

public class RedstoneClockPowerConfiguration extends BaseConfig
{
	private HashMap<EnumFacing, Boolean> facingPower = new HashMap<EnumFacing, Boolean>();
	private int poweredTick = 20;
	private int unPoweredTick = 20;
	private BlockPos pos;

	/**
	 * Initializes a new instance of the RedstoneClockPowerConfiguration class.
	 */
	public RedstoneClockPowerConfiguration()
	{
		for (EnumFacing facing : EnumFacing.values())
		{
			this.facingPower.put(facing, true);
		}
	}

	/**
	 * Gets the powered tick value.
	 * @return The amount of ticks a redstone clock should be powered.
	 */
	public int getPoweredTick()
	{
		return this.poweredTick;
	}

	/**
	 * Sets the amount of ticks the block should be powered.
	 * @param value The amount of powered ticks. If the value is less than 1 the value will be 1. 
	 */
	public void setPoweredTick(int value)
	{
		if (value <= 0)
		{
			value = 1;
		}

		this.poweredTick = value;
	}

	/**
	 * Gets the unpowered tick value.
	 * @return The amount of ticks a redstone clock should be unpowered.
	 */
	public int getUnPoweredTick()
	{
		return this.unPoweredTick;
	}

	/**
	 * Sets the amount of ticks the block should be unpowered.
	 * @param value The amount of unpowered ticks. If the value is less than 1 the value will be 1.
	 */
	public void setUnPoweredTick(int value)
	{
		if (value < 0)
		{
			value = 1;
		}

		this.unPoweredTick = value;
	}

	/**
	 * Gets the power for a particular side.
	 * @param facing The side to get the power for.
	 * @return The power for the given side.
	 */
	public boolean getSidePower(EnumFacing facing)
	{
		return this.facingPower.get(facing);
	}

	/**
	 * Gets the sides which are producing power.
	 * @return The sides which are sending power.
	 */
	public ArrayList<EnumFacing> getPoweredSides()
	{
		ArrayList<EnumFacing> poweredFacings = new ArrayList<EnumFacing>();

		for (Entry<EnumFacing, Boolean> facing : this.facingPower.entrySet())
		{
			if (facing.getValue())
			{
				poweredFacings.add(facing.getKey());
			}
		}

		return poweredFacings;
	}

	/**
	 * Gets the block position.
	 * @return The block position for this configuration.
	 */
	public BlockPos getPos()
	{
		return this.pos;
	}

	/**
	 * Sets the block position for this configuration.
	 * @param value The block position.
	 */
	public void setPos(BlockPos value)
	{
		this.pos = value;
	}

	/**
	 * Sets whether a side is powered.
	 * @param facing The side to set.
	 * @param value The value determining if the side is powered.
	 */
	public void setSidePower(EnumFacing facing, boolean value)
	{
		this.facingPower.put(facing, value);
	}

	@Override
	public void WriteToNBTCompound(NBTTagCompound compound)
	{
		NBTTagCompound powerCompound = new NBTTagCompound();
		
		// Add the power configuration tag.
		powerCompound.setInteger("poweredTick", this.poweredTick);
		powerCompound.setInteger("unPoweredTick", this.unPoweredTick);

		for (Entry<EnumFacing, Boolean> entry : this.facingPower.entrySet())
		{
			powerCompound.setBoolean(entry.getKey().getName2(), entry.getValue());
		}

		if (this.pos != null)
		{
			powerCompound.setInteger("x", this.pos.getX());
			powerCompound.setInteger("y", this.pos.getY());
			powerCompound.setInteger("z", this.pos.getZ());
		}

		compound.setTag("powerCompound", powerCompound);
	}

	public RedstoneClockPowerConfiguration ReadFromNBTTagCompound(NBTTagCompound compound)
	{
		RedstoneClockPowerConfiguration configuration = new RedstoneClockPowerConfiguration();

		if (compound.hasKey("powerCompound"))
		{
			NBTTagCompound powerCompound = compound.getCompoundTag("powerCompound");

			configuration.poweredTick = powerCompound.getInteger("poweredTick");
			configuration.unPoweredTick = powerCompound.getInteger("unPoweredTick");

			for (EnumFacing facing : EnumFacing.values())
			{
				configuration.facingPower.put(facing, powerCompound.getBoolean(facing.getName2()));
			}

			if (powerCompound.hasKey("x"))
			{
				configuration.pos = new BlockPos(powerCompound.getInteger("x"), powerCompound.getInteger("y"), powerCompound.getInteger("z"));
			}
		}

		return configuration;
	}
}
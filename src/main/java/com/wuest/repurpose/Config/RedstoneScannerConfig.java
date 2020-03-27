package com.wuest.repurpose.Config;

import com.wuest.repurpose.Base.BaseConfig;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

/**
 * This is the class responsible for holding the configuration options for the
 * redstone scanner.
 *
 * @author WuestMan
 */
public class RedstoneScannerConfig extends BaseConfig {
	private ArrayList<FacingConfig> facingConfigs;
	private int tickDelay;
	private BlockPos pos;
	private boolean animalsDetected;
	private boolean nonPlayersDetected;
	private boolean monstersDetected;
	private boolean playersDetected;

	/**
	 * Initializes a new instance of the RedstoneScannerConfig class.
	 */
	public RedstoneScannerConfig() {
		this.Initialize();
	}

	/**
	 * @return Gets the facing configs associated with this class.
	 */
	public ArrayList<FacingConfig> getFacingConfigs() {
		return this.facingConfigs;
	}

	/**
	 * Gets a facing based off of a facing name.
	 *
	 * @param name The name of the facing to get.
	 * @return Null if the facing wasn't found or the facing found.
	 */
	public FacingConfig getFacingConfig(String name) {
		for (FacingConfig config : this.facingConfigs) {
			if (config.getFacing().getName2().equals(name)) {
				return config;
			}
		}

		return null;
	}

	/**
	 * Gets a facing based off of a facing enum value.
	 *
	 * @param facing The Direction value to get the Facing config for.
	 * @return Null if the facing wasn't found or the facing found.
	 */
	public FacingConfig getFacingConfig(Direction facing) {
		for (FacingConfig config : this.facingConfigs) {
			if (config.getFacing() == facing) {
				return config;
			}
		}

		return null;
	}

	/**
	 * @return The tick delay for this class.
	 */
	public int getTickDelay() {
		return this.tickDelay;
	}

	/**
	 * The new value of the tick delay.
	 *
	 * @param value The new tick delay.
	 */
	public void setTickDelay(int value) {
		if (value < 1) {
			value = 1;
		}

		this.tickDelay = value;
	}

	/**
	 * Gets the block pos of this class.
	 *
	 * @return The block pos saved in this class.
	 */
	public BlockPos getBlockPos() {
		return this.pos;
	}

	/**
	 * Sets the block pos for this class.
	 *
	 * @param value The new block pos for this class.
	 */
	public void setBlockPos(BlockPos value) {
		this.pos = value;
	}

	/**
	 * Sets the block pos for this class.
	 *
	 * @param x The X-Coordinate for this block.
	 * @param y The Y-Coordinate for this block.
	 * @param z The Z=Coordinate for this block.
	 */
	public void setBlockPos(int x, int y, int z) {
		this.pos = new BlockPos(x, y, z);
	}

	/**
	 * Determines if animals are detected as part of the scan.
	 *
	 * @return A value indicating whether animals are a detected during a scan.
	 */
	public boolean getAnimalsDetected() {
		return this.animalsDetected;
	}

	/**
	 * Determines if animals are detected as a part of the scan.
	 *
	 * @param value The new value of the boolean.
	 */
	public void setAnimalsDetected(boolean value) {
		this.animalsDetected = value;
	}

	/**
	 * Determines if the facing is active.
	 *
	 * @param facing The facing to check.
	 * @return True if the facing is active otherwise false.
	 */
	public boolean IsFacingActive(Direction facing) {
		FacingConfig config = this.getFacingConfig(facing);

		return config.active;
	}

	/**
	 * Gets the scan length for a particular facing.
	 *
	 * @param facing The facing to get the scanning range for.
	 * @return The scanning range for this facing.
	 */
	public int GetFacingScanLength(Direction facing) {
		FacingConfig config = this.getFacingConfig(facing);

		return config.scanLength;
	}

	/**
	 * Determines if non-players are detected as part of the scan.
	 *
	 * @return A value indicating whether non-players are detected.
	 */
	public boolean getNonPlayersDetected() {
		return this.nonPlayersDetected;
	}

	/**
	 * Determines if non-players are detected as part of the scan.
	 *
	 * @param value The new value of the boolean.
	 */
	public void setNonPlayersDetected(boolean value) {
		this.nonPlayersDetected = value;
	}

	/**
	 * Determines if monsters are detected as part of the scan.
	 *
	 * @return A value indicating whether monsters are detected.
	 */
	public boolean getMonstersDetected() {
		return this.monstersDetected;
	}

	/**
	 * Determines if monsters are detected as part of the scan.
	 *
	 * @param value The new value of the boolean.
	 */
	public void setMonstersDetected(boolean value) {
		this.monstersDetected = value;
	}

	/**
	 * Determines if players are detected as part of the scan.
	 *
	 * @return A value indicating whether players are detected.
	 */
	public boolean getPlayersDetected() {
		return this.playersDetected;
	}

	/**
	 * Determines if players are detected as part of the scan.
	 *
	 * @param value The new value of the boolean.
	 */
	public void setPlayersDetected(boolean value) {
		this.playersDetected = value;
	}

	/**
	 * Adds a pre-configured facing configuration to the arraylist.
	 *
	 * @param value the pre-configured value to add.
	 */
	public void AddFacingConfig(FacingConfig value) {
		this.facingConfigs.add(value);
	}

	/**
	 * Adds a facing to the arraylist.
	 *
	 * @param facing     The facing to add.
	 * @param active     Determines if the facing is active.
	 * @param scanLength The distance for the scan for this facing.
	 */
	public void AddFacingConfig(Direction facing, boolean active, int scanLength) {
		FacingConfig config = new FacingConfig();
		this.AddFacingConfig(config.setFacing(facing).setActive(active).setScanLength(scanLength));
	}

	/**
	 * Sets the active flag for the facing's config.
	 *
	 * @param facing The facing to look for in the facing config.
	 * @param active The new value of the active flag.
	 */
	public void SetFacingConfig(Direction facing, boolean active) {
		this.SetFacingConfig(facing, active, -1);
	}

	/**
	 * Sets the scan length flag for the facing's config.
	 *
	 * @param facing     The facing to look for in the facing config.
	 * @param scanLength The distance for the scan for this facing.
	 */
	public void SetFacingConfig(Direction facing, int scanLength) {
		for (FacingConfig config : this.facingConfigs) {
			if (config.facing == facing) {
				config.setScanLength(scanLength);
				break;
			}
		}
	}

	/**
	 * Sets the active and scan length properties of the facing's config.
	 *
	 * @param facing     The facing to look for in the facing config.
	 * @param active     The new value of the active flag.
	 * @param scanLength The distance for the scan for this facing.
	 */
	public void SetFacingConfig(Direction facing, boolean active, int scanLength) {
		for (FacingConfig config : this.facingConfigs) {
			if (config.facing == facing) {
				config.active = active;

				if (scanLength >= 0) {
					config.scanLength = scanLength;
				}

				break;
			}
		}
	}

	/**
	 * Initializes the properties of this class.
	 */
	public void Initialize() {
		this.facingConfigs = new ArrayList<FacingConfig>();
		this.tickDelay = 5;
		this.animalsDetected = false;
		this.nonPlayersDetected = false;
		this.playersDetected = false;
		this.monstersDetected = false;

		for (Direction facing : Direction.values()) {
			FacingConfig config = new FacingConfig();
			config.setFacing(facing);
			config.setScanLength(1);
			config.setActive(true);

			this.facingConfigs.add(config);
		}
	}

	/**
	 * Writes the values of this class to an CompoundNBT.
	 *
	 * @param compound The compound to write the tag values too.
	 */
	@Override
	public void WriteToNBTCompound(CompoundNBT compound) {
		CompoundNBT configCompound = new CompoundNBT();
		configCompound.putInt("tickDelay", this.tickDelay);

		if (this.pos != null) {
			compound.putInt("x", this.pos.getX());
			compound.putInt("y", this.pos.getY());
			compound.putInt("z", this.pos.getZ());
		}

		configCompound.putBoolean("detectAnimals", this.animalsDetected);
		configCompound.putBoolean("detectNonPlayers", this.nonPlayersDetected);
		configCompound.putBoolean("detectMonsters", this.monstersDetected);
		configCompound.putBoolean("detectPlayers", this.playersDetected);

		for (FacingConfig config : this.facingConfigs) {
			CompoundNBT facing = new CompoundNBT();
			facing.putString("facing", config.facing.getName2());
			facing.putBoolean("active", config.active);
			facing.putInt("scanLength", config.scanLength);

			configCompound.put(config.facing.getName2(), facing);
		}

		compound.put("configCompound", configCompound);
	}

	/**
	 * Builds a RedstoneScannerConfig from an CompoundNBT.
	 *
	 * @param compound The compound to build the class from.
	 * @return A new instance of the RedstoneScannerConfig build with all of the
	 * values loaded from the tag (if any).
	 */
	public RedstoneScannerConfig ReadFromCompoundNBT(CompoundNBT compound) {
		RedstoneScannerConfig config = new RedstoneScannerConfig();

		if (compound.contains("configCompound")) {
			CompoundNBT configCompound = compound.getCompound("configCompound");

			if (configCompound.contains("tickDelay")) {
				config.tickDelay = configCompound.getInt("tickDelay");
			}

			if (configCompound.contains("detectAnimals")) {
				config.animalsDetected = configCompound.getBoolean("detectAnimals");
			}

			if (configCompound.contains("detectNonPlayers")) {
				config.nonPlayersDetected = configCompound.getBoolean("detectNonPlayers");
			}

			if (configCompound.contains("detectMonsters")) {
				config.monstersDetected = configCompound.getBoolean("detectMonsters");
			}

			if (configCompound.contains("detectPlayers")) {
				config.playersDetected = configCompound.getBoolean("detectPlayers");
			}

			if (compound.contains("x") && compound.contains("y") && compound.contains("z")) {
				config.pos = new BlockPos(compound.getInt("x"), compound.getInt("y"), compound.getInt("z"));
			}

			for (Direction facing : Direction.values()) {
				if (configCompound.contains(facing.getName2())) {
					CompoundNBT tag = configCompound.getCompound(facing.getName2());
					FacingConfig facingConfig = new FacingConfig();

					if (tag.contains("facing")) {
						facingConfig.facing = Direction.byName(tag.getString("facing"));
					}

					if (tag.contains("active")) {
						facingConfig.active = tag.getBoolean("active");
					}

					if (tag.contains("scanLength")) {
						facingConfig.scanLength = tag.getInt("scanLength");
					}

					config.SetFacingConfig(facingConfig.facing, facingConfig.active, facingConfig.scanLength);
				}
			}
		}

		return config;
	}

	/**
	 * This class is used to define the scanning configuration for a particular
	 * side.
	 *
	 * @author WuestMan
	 */
	public static class FacingConfig {
		private Direction facing;
		private boolean active;
		private int scanLength;

		/**
		 * Initializes a new instance of the FacingConfig class.
		 */
		public FacingConfig() {
			this.Initialize();
		}

		/**
		 * Gets the facing value.
		 *
		 * @return Gets the Direction value of this class.
		 */
		public Direction getFacing() {
			return this.facing;
		}

		/**
		 * Set the facing value.
		 *
		 * @param value The new value of the facing.
		 * @return The updated facing config for ease of setup.
		 */
		public FacingConfig setFacing(Direction value) {
			this.facing = value;
			return this;
		}

		/**
		 * Gets whether this facing is active.
		 *
		 * @return A bool representing the status of this facing.
		 */
		public boolean getActive() {
			return this.active;
		}

		/**
		 * Sets the facing active status.
		 *
		 * @param value The new status of this facing.
		 * @return The updated facing config for ease of setup.
		 */
		public FacingConfig setActive(boolean value) {
			this.active = value;
			return this;
		}

		/**
		 * Gets the length (in blocks) that this scan will cover.
		 *
		 * @return The number of blocks to scan.
		 */
		public int getScanLength() {
			return this.scanLength;
		}

		/**
		 * Sets the number of blocks to scan.
		 *
		 * @param value The number of blocks to scan for this facing when active.
		 * @return The updated facing config for ease of setup.
		 */
		public FacingConfig setScanLength(int value) {
			if (value <= 0) {
				value = 0;
				this.active = false;
			} else if (this.scanLength == 0 && value >= 1) {
				this.active = true;
			}

			this.scanLength = value;
			return this;
		}

		/**
		 * Initializes the properties of this class.
		 */
		public void Initialize() {
			this.facing = Direction.NORTH;
			this.active = false;
			this.scanLength = 1;
		}
	}

}

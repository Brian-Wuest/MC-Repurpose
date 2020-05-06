package com.wuest.repurpose.Tiles;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Base.TileEntityBase;
import com.wuest.repurpose.Blocks.BlockRedstoneScanner;
import com.wuest.repurpose.Config.RedstoneScannerConfig;

import com.wuest.repurpose.Repurpose;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.INPC;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShapes;

/**
 * This class is the TileEntity responsible for a lot of the work done with the
 * Redstone Scanner.
 * 
 * @author WuestMan
 */
public class TileEntityRedstoneScanner extends TileEntityBase<RedstoneScannerConfig> {
	public static TileEntityType<TileEntityRedstoneScanner> TileType = new TileEntityType<TileEntityRedstoneScanner>(
			TileEntityRedstoneScanner::new, new HashSet<>(Arrays.asList(ModRegistry.RedstoneScanner.get())), null);

	protected boolean foundEntity = false;

	/**
	 * Initializes a new instance of the TileEntityRedstoneScanner class.
	 */
	public TileEntityRedstoneScanner() {
		this(TileEntityRedstoneScanner.TileType);
	}

	public TileEntityRedstoneScanner(TileEntityType<?> tileEntityType) {
		super(tileEntityType);
		this.config = new RedstoneScannerConfig();
	}

	/**
	 * This method is used to determine if an entity was found within the scanning
	 * range.
	 *
	 * @return The redstone strength the block associated with this tile entity
	 *         should provide.
	 */
	public int getRedstoneStrength() {
		return this.foundEntity ? 15 : 0;
	}

	/**
	 * Determines the tick delay from the block configuration.
	 * 
	 * @return The tick delay from the configuration.
	 */
	public int getTickDelay() {
		return this.config.getTickDelay();
	}

	/**
	 * This is the initial method used to start the scan. The scan distance and
	 * sides are based on the configuration.
	 * 
	 * @param state The curent state of the block.
	 * @return An un-modified state if there was nothing to change. Otherwise this
	 *         method will provide a powered or unpowered state.
	 */
	public BlockState setRedstoneStrength(BlockState state) {
		this.ScanForEntities();
		return state.with(BlockRedstoneScanner.POWERED, Boolean.valueOf(this.foundEntity));
	}

	/**
	 * Processes the scanning, sets the class level bool for determining if an
	 * entity was found and if the block associated with this tile should provide
	 * power.
	 */
	protected void ScanForEntities() {
		int verticalRange = (this.config.IsFacingActive(Direction.UP) ? this.config.GetFacingScanLength(Direction.UP)
				: 0)
				+ (this.config.IsFacingActive(Direction.DOWN) ? this.config.GetFacingScanLength(Direction.DOWN) : 0);

		// Gets the starting position. Start at the highest point and work down.
		BlockPos startingPos = this.pos
				.up((this.config.IsFacingActive(Direction.UP) ? this.config.GetFacingScanLength(Direction.UP) : 0));

		int northScanRange = this.config.IsFacingActive(Direction.NORTH)
				? this.config.GetFacingScanLength(Direction.NORTH)
				: 0;
		int eastScanRange = this.config.IsFacingActive(Direction.EAST) ? this.config.GetFacingScanLength(Direction.EAST)
				: 0;
		int southScanRange = this.config.IsFacingActive(Direction.SOUTH)
				? this.config.GetFacingScanLength(Direction.SOUTH)
				: 0;
		int westScanRange = this.config.IsFacingActive(Direction.WEST) ? this.config.GetFacingScanLength(Direction.WEST)
				: 0;
		boolean foundATarget = false;

		// Loop through each Y level for scanning.
		for (int i = 0; i <= verticalRange; i++) {
			foundATarget = this.ScanLevel(northScanRange, eastScanRange, southScanRange, westScanRange, startingPos);

			// BlockPos.getAllInBox(from, to)
			// Break if a target was found.
			if (foundATarget) {
				break;
			}

			// After scanning this level, go down a level.
			startingPos = startingPos.down();
		}

		// Update the boolean.
		this.foundEntity = foundATarget;
	}

	/**
	 * Scans this Y level for the targeted entities.
	 * 
	 * @param northScanRange The north axis scanning range.
	 * @param eastScanRange  The east axis scanning range.
	 * @param southScanRange The south axis scanning range.
	 * @param westScanRange  The west axis scanning range.
	 * @param startingPos    The initial starting position.
	 * @return True if a target entity was found, otherwise false.
	 */
	protected boolean ScanLevel(int northScanRange, int eastScanRange, int southScanRange, int westScanRange,
			BlockPos startingPos) {
		boolean foundATarget = false;

		BlockPos endingPos = startingPos;

		if (northScanRange > 0) {
			startingPos = startingPos.north(northScanRange);
		}

		if (eastScanRange > 0) {
			startingPos = startingPos.east(eastScanRange);
		}

		if (southScanRange > 0) {
			endingPos = endingPos.south(southScanRange);
		}

		if (westScanRange > 0) {
			endingPos = endingPos.west(westScanRange);
		}

		// We have the 2 corners.
		for (BlockPos currentPos : BlockPos.getAllInBoxMutable(startingPos, endingPos)) {
			// We want to include the full block when trying to get the entities within this
			// block position.
			AxisAlignedBB axisPos = VoxelShapes.fullCube().getBoundingBox().offset(currentPos);
			List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity((Entity) null, axisPos);

			if (!list.isEmpty()) {
				// The first entity found matching the searched for targets will trigger the
				// power.
				for (Entity entity : list) {
					if (this.config.getAnimalsDetected() && entity instanceof AnimalEntity) {
						foundATarget = true;
						break;
					}

					if (this.config.getNonPlayersDetected() && entity instanceof INPC) {
						foundATarget = true;
						break;
					}

					if (this.config.getMonstersDetected() && entity instanceof IMob) {
						foundATarget = true;
						break;
					}

					if (this.config.getPlayersDetected() && entity instanceof PlayerEntity) {
						foundATarget = true;
						break;
					}
				}
			}

			if (foundATarget) {
				break;
			}
		}

		return foundATarget;
	}
}
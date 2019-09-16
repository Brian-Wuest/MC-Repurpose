package com.wuest.repurpose.Events;

import com.mojang.brigadier.CommandDispatcher;
import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Capabilities.IDimensionHome;

import net.minecraft.block.BlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

/**
 * This class is used to create a command which will send the user back to the
 * last bed they slept in.
 */
public class HomeCommand {
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(Commands.literal("home").executes((commandContext) -> {
			CommandSource commandSource = commandContext.getSource();

			HomeCommand.execute(commandSource.getWorld(), commandSource.getEntity());
			return 1;
		}));
	}

	private static void execute(ServerWorld server, Entity sender) throws CommandException {
		/*
		 * If it was a player that sent the command, then the sender should originate
		 * from an EntityPlayer, we can use this to check in an If statement to verify
		 * it's a player.
		 */
		if (sender instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) sender;

			if (!Repurpose.proxy.proxyConfiguration.enableHomeCommand) {
				return;
			}

			BlockPos bedLocation = player.getBedLocation(player.dimension);

			if (player.dimension != DimensionType.OVERWORLD) {
				// Check the player's capability for this dimension.
				IDimensionHome dimensionHome = player.getCapability(ModRegistry.DimensionHomes).orElse(null);

				if (dimensionHome != null) {
					bedLocation = dimensionHome.getHomePosition(player.dimension.getId());
				}
			}

			if (bedLocation != null) {
				BlockPos blockpos1 = null;

				if (player.dimension == DimensionType.OVERWORLD) {
					blockpos1 = player.getBedLocation(player.dimension);
				} else {
					blockpos1 = bedLocation;
				}

				if (blockpos1 != null) {
					HomeCommand.attemptTeleport(player, true, (double) ((float) blockpos1.getX() + 0.5F),
							(double) ((float) blockpos1.getY() + 0.1F), (double) ((float) blockpos1.getZ() + 0.5F));
				} else {
					if (player.dimension == DimensionType.OVERWORLD) {
						// Send the player saying that the bed could not be
						// found.
						player.sendMessage(new StringTextComponent("Bed Not Found."));
					} else {
						// Send the player a chat saying that the original
						// starting position is blocked.
						player.sendMessage(new StringTextComponent(
								"The entrance you can in from for this dimension is blocked. You need to find another way out."));
					}
				}
			} else {
				// Send the player saying that the bed could not be found.
				player.sendMessage(new StringTextComponent("Bed Not Found."));
			}
		}
	}

	/**
	 * Teleports the entity to the specified location.
	 */
	private static boolean attemptTeleport(PlayerEntity player, boolean ignoreCollisions, double x, double y,
			double z) {
		double d0 = player.posX;
		double d1 = player.posY;
		double d2 = player.posZ;
		player.posX = x;
		player.posY = y;
		player.posZ = z;
		boolean flag = false;
		BlockPos blockpos = new BlockPos(player);
		World world = player.world;

		if (world.isBlockLoaded(blockpos)) {
			boolean flag1 = false;

			while (!flag1 && blockpos.getY() > 0) {
				BlockPos blockpos1 = blockpos.down();
				BlockState iblockstate = world.getBlockState(blockpos1);

				if (iblockstate.getMaterial().blocksMovement()) {
					flag1 = true;
				} else {
					--player.posY;
					blockpos = blockpos1;
				}
			}

			if (flag1) {
				player.setPositionAndUpdate(player.posX, player.posY, player.posZ);
				flag = true;
			}
		}

		if (!flag) {
			player.setPositionAndUpdate(d0, d1, d2);
			return false;
		}

		return true;
	}

}
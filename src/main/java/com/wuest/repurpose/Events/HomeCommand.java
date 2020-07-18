package com.wuest.repurpose.Events;

import com.mojang.brigadier.CommandDispatcher;
import com.wuest.repurpose.Capabilities.IDimensionHome;
import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Proxy.CommonProxy;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
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

			if (!CommonProxy.proxyConfiguration.enableHomeCommand) {
				return;
			}

			BlockPos bedLocation = player.getBedPosition(player.dimension);

			if (player.dimension != DimensionType.OVERWORLD) {
				// Check the player's capability for this dimension.
				IDimensionHome dimensionHome = player.getCapability(ModRegistry.DimensionHomes).orElse(null);

				if (dimensionHome != null) {
					bedLocation = dimensionHome.getHomePosition(player.dimension);
				}
			}

			if (bedLocation != null) {
				BlockPos blockpos1 = null;

				if (player.dimension == DimensionType.OVERWORLD) {
					blockpos1 = player.getBedPosition(player.dimension);
				} else {
					blockpos1 = bedLocation;
				}

				if (blockpos1 != null) {
					HomeCommand.attemptTeleport(player, true, (float) blockpos1.getX() + 0.5F,
							(float) blockpos1.getY() + 0.1F, (float) blockpos1.getZ() + 0.5F);
				} else {
					if (player.dimension == DimensionType.OVERWORLD) {
						// Send the player saying that the bed could not be
						// found.
						player.sendMessage(new StringTextComponent("Bed Not Found."));
					} else {
						// Send the player a chat saying that the original
						// starting position is blocked.
						player.sendMessage(new StringTextComponent(
								"The entrance you can in from for this dimension is blocked. You need to find another way out."), player.getUniqueID());
					}
				}
			} else {
				// Send the player saying that the bed could not be found.
				player.sendMessage(new StringTextComponent("Bed Not Found."), player.getUniqueID());
			}
		}
	}

	/**
	 * Teleports the entity to the specified location.
	 */
	private static boolean attemptTeleport(PlayerEntity player, boolean ignoreCollisions, double x, double y,
										   double z) {
		double d0 = player.getPosX();
		double d1 = player.getPosY();
		double d2 = player.getPosZ();

		boolean flag = false;
		BlockPos blockpos = new BlockPos(d0, d1, d2);
		World world = player.world;

		if (world.isBlockLoaded(blockpos)) {
			boolean flag1 = false;

			while (!flag1 && blockpos.getY() > 0) {
				BlockPos blockpos1 = blockpos.down();
				BlockState iblockstate = world.getBlockState(blockpos1);

				if (iblockstate.getMaterial().blocksMovement()) {
					flag1 = true;
				} else {
					--y;
					blockpos = blockpos1;
				}
			}

			player.setRawPosition(x, y, z);

			if (flag1) {
				player.setPositionAndUpdate(player.getPosX(), player.getPosY(), player.getPosZ());
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
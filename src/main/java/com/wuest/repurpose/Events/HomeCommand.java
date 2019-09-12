package com.wuest.repurpose.Events;

import java.util.Map;
import java.util.Random;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Capabilities.IDimensionHome;

import net.minecraft.block.BlockState;
import net.minecraft.command.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * This class is used to create a command which will send the user back to the
 * last bed they slept in.
 */
public class HomeCommand
{
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(Commands.literal("home").executes((commandContext) -> {
			CommandSource commandSource = commandContext.getSource();

			HomeCommand.execute(commandSource.getWorld(), commandSource.getEntity());
			return 1;
		}));
	 }
	 
	private static void execute(ServerWorld server, Entity sender) throws CommandException
	{
		/*
		 * If it was a player that sent the command, then the sender should
		 * originate from an EntityPlayer, we can use this to check in an If
		 * statement to verify it's a player.
		 */
		if (sender instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity) sender;

			if (!Repurpose.proxy.proxyConfiguration.enableHomeCommand)
			{
				return;
			}

			BlockPos bedLocation = player.getBedLocation();

			if (player.dimension != 0)
			{
				// Check the player's capability for this dimension.
				IDimensionHome dimensionHome = player.getCapability(ModRegistry.DimensionHomes, null);

				if (dimensionHome != null)
				{
					bedLocation = dimensionHome.getHomePosition(player.dimension);
				}
			}

			if (bedLocation != null)
			{
				World world = player.world;

				BlockPos blockpos1 = null;

				if (player.dimension == 0)
				{
					blockpos1 = PlayerEntity.getBedSpawnLocation(server.getWorld(player.dimension), bedLocation, true);
				}
				else
				{
					blockpos1 = bedLocation;
				}

				if (blockpos1 != null)
				{
					HomeCommand.attemptTeleport(player, true, (double) ((float) blockpos1.getX() + 0.5F), (double) ((float) blockpos1.getY() + 0.1F),
							(double) ((float) blockpos1.getZ() + 0.5F));
				}
				else
				{
					if (player.dimension == 0)
					{
						// Send the player saying that the bed could not be
						// found.	
						player.sendMessage(new StringTextComponent("Bed Not Found."));
					}
					else
					{
						// Send the player a chat saying that the original
						// starting position is blocked.
						player.sendMessage(
								new StringTextComponent("The entrance you can in from for this dimension is blocked. You need to find another way out."));
					}
				}
			}
			else
			{
				// Send the player saying that the bed could not be found.
				player.sendMessage(new StringTextComponent("Bed Not Found."));
			}
		}
	}

	/**
	 * Teleports the entity to the specified location.
	 */
	private static boolean attemptTeleport(PlayerEntity player, boolean ignoreCollisions, double x, double y, double z)
	{
		double d0 = player.posX;
		double d1 = player.posY;
		double d2 = player.posZ;
		player.posX = x;
		player.posY = y;
		player.posZ = z;
		boolean flag = false;
		BlockPos blockpos = new BlockPos(player);
		World world = player.world;
		Random random = player.getRNG();

		if (world.isBlockLoaded(blockpos))
		{
			boolean flag1 = false;

			while (!flag1 && blockpos.getY() > 0)
			{
				BlockPos blockpos1 = blockpos.down();
				BlockState iblockstate = world.getBlockState(blockpos1);

				if (iblockstate.getMaterial().blocksMovement())
				{
					flag1 = true;
				}
				else
				{
					--player.posY;
					blockpos = blockpos1;
				}
			}

			if (flag1)
			{
				player.setPositionAndUpdate(player.posX, player.posY, player.posZ);

				if (ignoreCollisions
						|| world.getCollisionBoxes(player, player.getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(player.getEntityBoundingBox()))
				{
					flag = true;
				}
			}
		}

		if (!flag)
		{
			player.setPositionAndUpdate(d0, d1, d2);
			return false;
		}

		return true;
	}

}
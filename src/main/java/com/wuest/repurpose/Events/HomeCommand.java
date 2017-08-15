package com.wuest.repurpose.Events;

import java.util.Random;

import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Capabilities.IDimensionHome;

import net.minecraft.block.state.IBlockState;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 * This class is used to create a command which will send the user back to the
 * last bed they slept in.
 */
public class HomeCommand extends CommandBase
{
	@Override
	public String getName()
	{
		return "home";
	}

	/**
	 * Return the required permission level for this command.
	 */
	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}

	/**
	 * Check if the given ICommandSender has permission to execute this command
	 * 
	 * @param server The Minecraft server instance
	 * @param sender The command sender who we are checking permission on
	 */
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		return true;
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		// Message to show when the user uses "/help test"
		return "Teleports the player to the last bed they slept in.";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		/*
		 * If it was a player that sent the command, then the sender should
		 * originate from an EntityPlayer, we can use this to check in an If
		 * statement to verify it's a player.
		 */
		if (sender instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) sender;

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
					blockpos1 = EntityPlayer.getBedSpawnLocation(server.getWorld(player.dimension), bedLocation, true);
				}
				else
				{
					blockpos1 = bedLocation;
				}

				if (blockpos1 != null)
				{
					this.attemptTeleport(player, true, (double) ((float) blockpos1.getX() + 0.5F), (double) ((float) blockpos1.getY() + 0.1F),
							(double) ((float) blockpos1.getZ() + 0.5F));
				}
				else
				{
					if (player.dimension == 0)
					{
						// Send the player saying that the bed could not be
						// found.	
						player.sendMessage(new TextComponentString("Bed Not Found."));
					}
					else
					{
						// Send the player a chat saying that the original
						// starting position is blocked.
						player.sendMessage(
								new TextComponentString("The entrance you can in from for this dimension is blocked. You need to find another way out."));
					}
				}
			}
			else
			{
				// Send the player saying that the bed could not be found.
				player.sendMessage(new TextComponentString("Bed Not Found."));
			}
		}
	}

	/**
	 * Teleports the entity to the specified location.
	 */
	public boolean attemptTeleport(EntityPlayer player, boolean ignoreCollisions, double x, double y, double z)
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
				IBlockState iblockstate = world.getBlockState(blockpos1);

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
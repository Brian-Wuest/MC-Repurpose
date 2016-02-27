package wuest.utilities.Events;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import wuest.utilities.WuestUtilities;

/**
 * This class is used to create a command which will send the user back to the last bed they slept in. 
 */
public class HomeCommand extends CommandBase 
{
	@Override
	public String getCommandName() 
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
	 * Returns true if the given command sender is allowed to use this command.
	 */
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender)
	{
		return true;
	}

	@Override
	public String getCommandUsage(ICommandSender sender) 
	{
		// Message to show when the user uses "/help test"
		return "Teleports the player to the last bed they slept in.";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args)
			throws CommandException 
	{
		/*
		 * If it was a player that sent the command, then the sender should originate from an EntityPlayer,
		 * we can use this to check in an If statement to verify it's a player.
		 */
		if(sender instanceof EntityPlayer) 
		{
			EntityPlayer player = (EntityPlayer)sender;

			if (!WuestUtilities.proxy.proxyConfiguration.enableHomeCommand)
			{
				player.addChatComponentMessage(new ChatComponentText("This command has not been enabled on the server."));
			}

			BlockPos bedLocation = player.getBedLocation().east().south();

			if (bedLocation != null)
			{
				World world = player.worldObj;

				// Teleport the player back to their bed.
				player.setPositionAndUpdate(bedLocation.getX(), bedLocation.getY(), bedLocation.getZ());
			}
			else
			{
				// Send the player saying that the bed could not be found.
				player.addChatComponentMessage(new ChatComponentText("Bed Not Found."));
			}
		}		
	}
}
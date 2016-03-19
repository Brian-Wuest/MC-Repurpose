package wuest.utilities.Events;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
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
	public String getCommandUsage(ICommandSender sender) 
	{
		// Message to show when the user uses "/help test"
		return "Teleports the player to the last bed they slept in.";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args)
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
				player.addChatComponentMessage(new TextComponentString("This command has not been enabled on the server."));
			}

			BlockPos bedLocation = player.getBedLocation();

			if (bedLocation != null)
			{
				World world = player.worldObj;
				bedLocation = bedLocation.east().south();

				// Teleport the player back to their bed.
				player.setPositionAndUpdate(bedLocation.getX(), bedLocation.getY(), bedLocation.getZ());
			}
			else
			{
				// Send the player saying that the bed could not be found.
				player.addChatComponentMessage(new TextComponentString("Bed Not Found."));
			}
		}		
	}
}
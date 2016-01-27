package wuest.utilities;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class is used to create a command which will send the user back to the last bed they slept in. 
 */
public class HomeCommand extends CommandBase 
{

	@Override
	public String getName() 
	{
		// Name of the command "home" will be called by "/home"
		return "home";	
	}

	@Override
	public String getCommandUsage(ICommandSender sender) 
	{
		// Message to show when the user uses "/help test"
		return "Teleports the player to the last bed they slept in.";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void execute(ICommandSender sender, String[] args)
			throws CommandException 
	{
		/*
		 * If it was a player that sent the command, then the sender should originate from an EntityPlayer,
		 * we can use this to check in an If statement to verify it's a player.
		 */
		if(sender instanceof EntityPlayer) 
		{
			// Turn the sender into a player entity.
			EntityPlayer player = (EntityPlayer)sender;
			BlockPos bedLocation = player.getBedLocation();
			
			if (bedLocation != null)
			{
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
package wuest.utilities.Events;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
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
				return;
			}

			BlockPos bedLocation = player.getBedLocation();

			if (bedLocation != null)
			{
				World world = player.worldObj;
				
				BlockPos blockpos1 = EntityPlayer.getBedSpawnLocation(server.worldServerForDimension(player.dimension), bedLocation, true);
				
				if (blockpos1 != null)
				{
					this.attemptTeleport(player, true, (double)((float)blockpos1.getX() + 0.5F), (double)((float)blockpos1.getY() + 0.1F), (double)((float)blockpos1.getZ() + 0.5F));
				}
				else
				{
					// Send the player saying that the bed could not be found.
					player.addChatComponentMessage(new TextComponentString("Bed Not Found."));	
				}
			}
			else
			{
				// Send the player saying that the bed could not be found.
				player.addChatComponentMessage(new TextComponentString("Bed Not Found."));
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
        World world = player.worldObj;
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

                if (ignoreCollisions || world.getCollisionBoxes(player, player.getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(player.getEntityBoundingBox()))
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
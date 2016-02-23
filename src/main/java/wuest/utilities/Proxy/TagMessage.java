package wuest.utilities.Proxy;

import io.netty.buffer.ByteBuf;
import wuest.utilities.Gui.HouseConfiguration;
import wuest.utilities.Gui.WuestConfiguration;
import wuest.utilities.Items.ItemStartHouse;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TagMessage implements IMessage
{
	private NBTTagCompound tagMessage;

	public TagMessage() 
	{
	}

	public TagMessage(NBTTagCompound tagMessage) 
	{
		this.tagMessage = tagMessage;
	}
	
	public NBTTagCompound getMessageTag()
	{
		return this.tagMessage;
	}
	
	public void setMessageTag(NBTTagCompound value)
	{
		this.tagMessage = value;
	}

	@Override
	public void fromBytes(ByteBuf buf) 
	{
		// This class is very useful in general for writing more complex
		// objects.
		this.tagMessage = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		ByteBufUtils.writeTag(buf, this.tagMessage);
	}
}

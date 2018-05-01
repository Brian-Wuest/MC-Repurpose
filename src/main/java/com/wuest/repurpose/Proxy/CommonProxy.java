package com.wuest.repurpose.Proxy;

import com.wuest.repurpose.GeneralRecipes;
import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.UpdateChecker;
import com.wuest.repurpose.Blocks.*;
import com.wuest.repurpose.Blocks.BlockCoffer.IronChestType;
import com.wuest.repurpose.Capabilities.ItemBagOfHoldingProvider;
import com.wuest.repurpose.Config.WuestConfiguration;
import com.wuest.repurpose.Events.WuestEventHandler;
import com.wuest.repurpose.Gui.*;
import com.wuest.repurpose.Items.*;
import com.wuest.repurpose.Items.Containers.BagOfHoldingContainer;
import com.wuest.repurpose.Proxy.Messages.*;
import com.wuest.repurpose.Proxy.Messages.Handlers.*;
import com.wuest.repurpose.Tiles.TileEntityCoffer;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;

public class CommonProxy implements IGuiHandler
{
	// This should be static in order for the events to be processed on server
	// and client.
	private static WuestEventHandler eventHandler = new WuestEventHandler();

	public static WuestConfiguration proxyConfiguration;

	/*
	 * Methods for ClientProxy to Override
	 */
	public void registerRenderers()
	{
	}

	public void preInit(FMLPreInitializationEvent event)
	{
		ModRegistry.RegisterModComponents();
		
		Repurpose.config = new Configuration(event.getSuggestedConfigurationFile());
		Repurpose.config.load();
		WuestConfiguration.syncConfig();

		Repurpose.network = NetworkRegistry.INSTANCE.newSimpleChannel("RepurposeChnl");
		
		if (this.proxyConfiguration.enableVersionCheckMessage)
		{
			// Pull the repository information.
			UpdateChecker.checkVersion();
		}
		
		ModRegistry.RegisterMessages();
		
		// Register the capabilities.
		ModRegistry.RegisterCapabilities();
	}

	public void init(FMLInitializationEvent event)
	{	
		ModRegistry.RegisterEnchantments();

		NetworkRegistry.INSTANCE.registerGuiHandler(Repurpose.instance, Repurpose.proxy);
		this.RegisterEventListeners();
	}

	public void postinit(FMLPostInitializationEvent event)
	{
	}

	private void RegisterEventListeners()
	{
		// DEBUG
		System.out.println("Registering event listeners");

		MinecraftForge.EVENT_BUS.register(eventHandler);
	}

	public IThreadListener getThreadFromContext(MessageContext ctx)
	{
		return ctx.getServerHandler().player.getServer();
	}

	/**
	 * Returns a side-appropriate EntityPlayer for use during message handling
	 */
	public EntityPlayer getPlayerEntity(MessageContext ctx)
	{
		System.out.println("Retrieving player from CommonProxy for message on side " + ctx.side);
		return ctx.getServerHandler().player;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		
		if (ID == GuiCoffer.GUI_ID)
		{
			return new ContainerCoffer(player.inventory, (TileEntityCoffer)tileEntity, ((TileEntityCoffer)tileEntity).getType(), 0, 0);
		}
		else if (ID == GuiItemBagOfHolding.GUI_ID)
		{
			ItemStack stack = player.getHeldItemOffhand();
			ItemBagOfHoldingProvider handler = ItemBagOfHoldingProvider.GetFromStack(stack);
			((ItemBagOfHolding)stack.getItem()).RefreshItemStack(player, stack);
			return new BagOfHoldingContainer(handler, player);
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}

	public void generateParticles(EntityPlayer players)
	{
	}
	
	public WuestConfiguration getServerConfiguration()
	{
		return CommonProxy.proxyConfiguration;
	}
}
package com.wuest.utilities.Proxy;

import com.wuest.utilities.GeneralRecipes;
import com.wuest.utilities.WuestUtilities;
import com.wuest.utilities.Blocks.*;
import com.wuest.utilities.Config.WuestConfiguration;
import com.wuest.utilities.Events.WuestEventHandler;
import com.wuest.utilities.Gui.*;
import com.wuest.utilities.Items.*;
import com.wuest.utilities.Proxy.Messages.*;
import com.wuest.utilities.Proxy.Messages.Handlers.*;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.IThreadListener;
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
		WuestUtilities.config = new Configuration(event.getSuggestedConfigurationFile());
		WuestUtilities.config.load();
		WuestConfiguration.syncConfig();

		WuestUtilities.network = NetworkRegistry.INSTANCE.newSimpleChannel("MyChannel");
		WuestUtilities.network.registerMessage(RedstoneClockHandler.class, RedstoneClockMessage.class, 2, Side.SERVER);
		WuestUtilities.network.registerMessage(BedLocationHandler.class, BedLocationMessage.class, 3, Side.CLIENT);
		WuestUtilities.network.registerMessage(RedstoneScannerHandler.class, RedstoneScannerMessage.class, 4, Side.SERVER);
	}

	public void init(FMLInitializationEvent event)
	{
		// Register items here.
		ItemBedCompass.RegisterItem();
		RedstoneClock.RegisterBlock();
		GeneralRecipes.LoadRecipies();
		ItemSwiftBlade.RegisterItem();
		BlockDirtStairs.RegisterBlock();
		BlockGrassStairs.RegisterBlock();
		BlockDirtSlab.RegisterBlock();
		BlockGrassSlab.RegisterBlock();
		BlockCustomWall.RegisterBlock();
		BlockEnrichedFarmland.RegisterBlock();
		BlockMiniRedstone.RegisterBlock();
		BlockRedstoneScanner.RegisterBlock();
		ItemFluffyFabric.RegisterItem();
		ItemWhetStone.RegisterItem();
		ItemSnorkel.RegisterItem();
		ItemDiamondShard.RegisterItem();

		NetworkRegistry.INSTANCE.registerGuiHandler(WuestUtilities.instance, WuestUtilities.proxy);
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
		return ctx.getServerHandler().playerEntity.getServer();
	}

	/**
	 * Returns a side-appropriate EntityPlayer for use during message handling
	 */
	public EntityPlayer getPlayerEntity(MessageContext ctx)
	{
		System.out.println("Retrieving player from CommonProxy for message on side " + ctx.side);
		return ctx.getServerHandler().playerEntity;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == GuiRedstoneClock.GUI_ID)
		{
			return new GuiRedstoneClock(x, y, z);
		}
		else if (ID == GuiRedstoneScanner.GUI_ID)
		{
			return new GuiRedstoneScanner(x, y, z);
		}

		return null;
	}

	/**
	 * Register an Item
	 *
	 * @param item The Item instance
	 * @param <T> The Item type
	 * @return The Item instance
	 */
	public static <T extends Item> T registerItem(T item)
	{
		GameRegistry.register(item);
		WuestUtilities.ModItems.add(item);

		return item;
	}
	
	public static <T extends Block> T registerBlock(T block)
	{
		GameRegistry.register(block);
		GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
		WuestUtilities.ModBlocks.add(block);
		
		return block;
	}
	
	public static <T extends Block, I extends ItemBlock> T registerBlock(T block, I itemBlock)
	{
		GameRegistry.register(block);
		WuestUtilities.ModBlocks.add(block);
		
		if (itemBlock != null)
		{
			GameRegistry.register(itemBlock);
			WuestUtilities.ModItems.add(itemBlock);
		}
		
		return block;
	}
	
	/**
	 * Set the registry name of {@code item} to {@code itemName} and the unlocalised name to the full registry name.
	 *
	 * @param item     The item
	 * @param itemName The item's name
	 */
	public static void setItemName(Item item, String itemName) 
	{
		item.setRegistryName(itemName);
		item.setUnlocalizedName(item.getRegistryName().toString());
	}
	
	public static void setBlockName(Block block, String blockName) 
	{
		block.setRegistryName(blockName);
		block.setUnlocalizedName(block.getRegistryName().toString());
	}
}
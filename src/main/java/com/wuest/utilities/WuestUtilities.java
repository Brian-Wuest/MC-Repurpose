package com.wuest.utilities;

import java.util.ArrayList;

import com.wuest.utilities.Events.HomeCommand;
import com.wuest.utilities.Proxy.CommonProxy;

import net.minecraft.block.Block;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid=WuestUtilities.MODID, name="Wuest Utilities", version=WuestUtilities.VERSION, acceptedMinecraftVersions="[1.10],[1.10.2]", guiFactory = "com.wuest.utilities.Gui.ConfigGuiFactory")
public class WuestUtilities 
{
	public static final String MODID = "wuestutilities";
	public static final String VERSION = "1.10.2.10";
	
	// compilation flag used for debugging purposes.
	public static boolean isDebug = false;

	@Instance(value = WuestUtilities.MODID)
	public static WuestUtilities instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "com.wuest.utilities.Proxy.ClientProxy", serverSide = "com.wuest.utilities.Proxy.CommonProxy")
	public static CommonProxy proxy;

	public static SimpleNetworkWrapper network;
	public static Configuration config;
	
	static
	{
		WuestUtilities.isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().
			    getInputArguments().toString().contains("-agentlib:jdwp");
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		WuestUtilities.proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		WuestUtilities.proxy.init(event);
	}

	@EventHandler
	public void postinit(FMLPostInitializationEvent event)
	{
		WuestUtilities.proxy.postinit(event);
	}

	// The method that gets called when a server starts up(Singleplayer and multiplayer are both affected)
	@EventHandler
	public void serverStart(FMLServerStartingEvent event)
	{
		// Get's the current server instance.
		MinecraftServer server = event.getServer();

		// Get's the Command manager for the server, but it's in a form we cannot use.
		ICommandManager command = server.getCommandManager();

		// Turns the useless to us ICommandManager into a now useful ServerCommandManager.
		ServerCommandManager manager = (ServerCommandManager)command;

		// Registers the command
		manager.registerCommand(new HomeCommand());    	
	}
}
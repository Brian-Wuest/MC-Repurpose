package com.wuest.repurpose;

import java.util.ArrayList;

import com.wuest.repurpose.Events.HomeCommand;
import com.wuest.repurpose.Proxy.CommonProxy;

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

@Mod(modid=Repurpose.MODID, name="Repurpose", version=Repurpose.VERSION, acceptedMinecraftVersions="[1.12, 1.12.2]", 
guiFactory = "com.wuest.repurpose.Gui.ConfigGuiFactory",
updateJSON = "https://raw.githubusercontent.com/Brian-Wuest/MC-Repurpose/master/changeLog.json")
public class Repurpose 
{
	public static final String MODID = "repurpose";
	public static final String VERSION = "@VERSION@";
	
	// compilation flag used for debugging purposes.
	public static boolean isDebug = false;

	@Instance(value = Repurpose.MODID)
	public static Repurpose instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "com.wuest.repurpose.Proxy.ClientProxy", serverSide = "com.wuest.repurpose.Proxy.CommonProxy")
	public static CommonProxy proxy;

	public static SimpleNetworkWrapper network;
	public static Configuration config;
	
	static
	{
		Repurpose.isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().
			    getInputArguments().toString().contains("-agentlib:jdwp");
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Repurpose.proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		Repurpose.proxy.init(event);
	}

	@EventHandler
	public void postinit(FMLPostInitializationEvent event)
	{
		Repurpose.proxy.postinit(event);
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
		if (Repurpose.proxy.proxyConfiguration.enableHomeCommand)
		{		
			manager.registerCommand(new HomeCommand());
		}
	}
}
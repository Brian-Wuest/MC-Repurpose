package com.wuest.repurpose;

import com.wuest.repurpose.Events.HomeCommand;
import com.wuest.repurpose.Proxy.ClientProxy;
import com.wuest.repurpose.Proxy.CommonProxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.loading.FMLServerLaunchProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.command.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod(Repurpose.MODID)
public class Repurpose 
{
	public static final String MODID = "repurpose";

	public static final Logger LOGGER = LogManager.getLogger();
    public static final String PROTOCOL_VERSION = Integer.toString(1);
	
	// compilation flag used for debugging purposes.
	public static boolean isDebug = false;

	// Says where the client and server 'proxy' code is loaded.
	public static CommonProxy proxy;

	public static SimpleChannel network;
	
	static
	{
		Repurpose.isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().
			    getInputArguments().toString().contains("-agentlib:jdwp");
	}

	public Repurpose() {
        // Register the setup method for mod-loading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		MinecraftForge.EVENT_BUS.addListener(this::serverStart);

        Repurpose.proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

        ModRegistry.RegisterModComponents();
    }

	private void setup(final FMLCommonSetupEvent event) {
        Repurpose.proxy.preInit(event);
        Repurpose.proxy.init(event);
        Repurpose.proxy.postinit(event);
    }

	// The method that gets called when a server starts up(Singleplayer and multiplayer are both affected)
	public void serverStart(FMLServerStartingEvent event)
	{
		// Get's the current server instance.
		MinecraftServer server = event.getServer();

		// Get's the Command manager for the server.
		Commands command = server.getCommandManager();

		// Registers the command
		if (Repurpose.proxy.getServerConfiguration().enableHomeCommand)
		{	
			HomeCommand.register(command.getDispatcher());
		}
	}
}
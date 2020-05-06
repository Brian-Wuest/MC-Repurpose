package com.wuest.repurpose;

import com.wuest.repurpose.Events.HomeCommand;
import com.wuest.repurpose.Items.ItemSickle;
import com.wuest.repurpose.Proxy.ClientProxy;
import com.wuest.repurpose.Proxy.CommonProxy;
import net.minecraft.command.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Repurpose.MODID)
public class Repurpose {
	public static final String MODID = "repurpose";

	public static final Logger LOGGER = LogManager.getLogger();
	public static final String PROTOCOL_VERSION = Integer.toString(1);

	// compilation flag used for debugging purposes.
	public static boolean isDebug = false;

	// Says where the client and server 'proxy' code is loaded.
	public static CommonProxy proxy;

	public static SimpleChannel network;

	static {
		Repurpose.isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().
				getInputArguments().toString().contains("-agentlib:jdwp");
	}

	public Repurpose() {
		// Register the blocks and items for this mod.
		ModRegistry.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ModRegistry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ModRegistry.TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
		ModRegistry.ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ModRegistry.RECIPE_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ModRegistry.CONTAINER_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());

		// Register the setup method for mod-loading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		MinecraftForge.EVENT_BUS.addListener(this::serverStart);

		Repurpose.proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
	}

	private void setup(final FMLCommonSetupEvent event) {
		Repurpose.proxy.preInit(event);
		Repurpose.proxy.init(event);
		Repurpose.proxy.postinit(event);
	}

	// The method that gets called when a server starts up(Singleplayer and multiplayer are both affected)
	public void serverStart(FMLServerStartingEvent event) {
		// Get's the current server instance.
		MinecraftServer server = event.getServer();

		// Get's the Command manager for the server.
		Commands command = server.getCommandManager();

		// Registers the command
		if (Repurpose.proxy.getServerConfiguration().enableHomeCommand) {
			HomeCommand.register(command.getDispatcher());
		}

		ItemSickle.setEffectiveBlocks();
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		Repurpose.proxy.clientSetup(event);
	}
}
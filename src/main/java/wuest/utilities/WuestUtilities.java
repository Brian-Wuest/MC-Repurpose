package wuest.utilities;

import java.util.ArrayList;

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
import wuest.utilities.Events.HomeCommand;
import wuest.utilities.Proxy.CommonProxy;

@Mod(modid=WuestUtilities.MODID, name="Wuest Utilities", version=WuestUtilities.VERSION, acceptedMinecraftVersions="[1.10]", guiFactory = "wuest.utilities.Gui.ConfigGuiFactory")
public class WuestUtilities 
{
	public static final String MODID = "wuestUtilities";
	public static final String VERSION = "1.10";

	@Instance(value = WuestUtilities.MODID)
	public static WuestUtilities instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "wuest.utilities.Proxy.ClientProxy", serverSide = "wuest.utilities.Proxy.CommonProxy")
	public static CommonProxy proxy;

	public static SimpleNetworkWrapper network;

	public static ArrayList<Item> ModItems = new ArrayList<Item>();
	public static ArrayList<Block> ModBlocks = new ArrayList<Block>();
	public static Configuration config;

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
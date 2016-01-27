package wuest.utilities;

import java.util.*;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ExistingSubstitutionException;
import net.minecraftforge.fml.common.registry.GameRegistry.Type;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.RegistryDelegate;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.*;

@Mod(modid=WuestUtilities.MODID, name="Wuest Utilities", version=WuestUtilities.VERSION, acceptedMinecraftVersions="[1.8], [1.8.9]")
public class WuestUtilities 
{
	public static final String MODID = "wuestUtilities";
	public static final String VERSION = "1.0";
	
	@Instance(value = WuestUtilities.MODID)
	public static WuestUtilities instance;
	
	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "wuest.utilities.ClientProxy", serverSide = "wuest.utilities.CommonProxy")
	public static CommonProxy proxy;
	
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
	
    // The method that gets called when a server starts up(Singleplayer and multiplayer are both affected)
    @EventHandler
    public void serverStart(FMLServerStartingEvent event)
    {
    	// Get's the current server instance.
    	MinecraftServer server = MinecraftServer.getServer();
    	
    	// Get's the Command manager for the server, but it's in a form we cannot use.
    	ICommandManager command = server.getCommandManager();
    	
    	// Turns the useless to us ICommandManager into a now useful ServerCommandManager.
    	ServerCommandManager manager = (ServerCommandManager)command;
    	
    	// Registers the command
    	manager.registerCommand(new HomeCommand());    	
    }
}
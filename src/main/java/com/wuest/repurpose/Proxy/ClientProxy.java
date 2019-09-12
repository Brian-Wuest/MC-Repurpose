package com.wuest.repurpose.Proxy;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;

import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Blocks.BlockCoffer.CofferType;
import com.wuest.repurpose.Blocks.BlockCustomWall;
import com.wuest.repurpose.Blocks.BlockGrassSlab;
import com.wuest.repurpose.Blocks.BlockGrassStairs;
import com.wuest.repurpose.Config.ModConfiguration;
import com.wuest.repurpose.Events.ClientEventHandler;
import com.wuest.repurpose.Renderer.TileEntityCofferRenderer;
import com.wuest.repurpose.Tiles.TileEntityCoffer;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ClientProxy extends CommonProxy
{
	public ModConfiguration serverConfiguration = null;
	public static ClientEventHandler clientEventHandler = new ClientEventHandler();

	@Override
	public void preInit(FMLCommonSetupEvent event)
	{	 
		super.preInit(event);
	}

	@Override
	public void init(FMLCommonSetupEvent event)
	{
		super.init(event);

		// After all items have been registered and all recipes loaded, register
		// any necessary renderer.
		Repurpose.proxy.registerRenderers();
		this.RegisterEventListeners();
		
		this.RegisterKeyBindings();
	}

	@Override
	public void postinit(FMLCommonSetupEvent event)
	{
		super.postinit(event);
	}

	@Override
	public void registerRenderers()
	{
		// Register block colors.
		BlockGrassStairs.RegisterBlockRenderer();

		BlockGrassSlab.RegisterBlockRenderer();

		BlockCustomWall.RegisterBlockRenderer();
		
		for (CofferType type : CofferType.values())
		{
			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCoffer.class, new TileEntityCofferRenderer());
		}
	}

	@Override
	public void generateParticles(PlayerEntity player)
	{
	}
	
/* 	@Override
	public Object getClientGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		 
		if (ID == GuiRedstoneClock.GUI_ID)
		{
			return new GuiRedstoneClock(x, y, z);
		}
		else if (ID == GuiRedstoneScanner.GUI_ID)
		{
			return new GuiRedstoneScanner(x, y, z);
		}
		else if (tileEntity != null && tileEntity instanceof TileEntityCoffer)
		{
			return GuiCoffer.GUI.buildGUI(((TileEntityCoffer)tileEntity).getType(), player.inventory, (TileEntityCoffer) tileEntity);
		}
		else if (ID == GuiItemBagOfHolding.GUI_ID)
		{
			ItemStack stack = player.getHeldItemOffhand();
			ItemBagOfHoldingProvider handler = ItemBagOfHoldingProvider.GetFromStack(stack);
			
			return new GuiItemBagOfHolding(handler, player);
		}

		return null;
	} */
	
	@Override
	public ModConfiguration getServerConfiguration()
	{
		if (this.serverConfiguration == null)
		{
			// Get the server configuration.
			return CommonProxy.proxyConfiguration;
		}
		else
		{
			return this.serverConfiguration;
		}
	}

	private void RegisterEventListeners()
	{
	}
	
	private void RegisterKeyBindings()
	{
		KeyBinding binding = new KeyBinding(
			"Previous Item", 
			net.minecraftforge.client.settings.KeyConflictContext.UNIVERSAL, 
			KeyModifier.ALT, 
			InputMappings.Type.KEYSYM,
			GLFW_KEY_Z, 
			"Repurpose - Bag of Holding");

		ClientEventHandler.keyBindings.add(binding);
		ClientRegistry.registerKeyBinding(binding);
		
		binding = new KeyBinding(
			"Next Item", 
			net.minecraftforge.client.settings.KeyConflictContext.UNIVERSAL, 
			KeyModifier.ALT,
			InputMappings.Type.KEYSYM,  
			GLFW_KEY_X,
			"Repurpose - Bag of Holding");
		ClientEventHandler.keyBindings.add(binding);
		ClientRegistry.registerKeyBinding(binding);
	}
}
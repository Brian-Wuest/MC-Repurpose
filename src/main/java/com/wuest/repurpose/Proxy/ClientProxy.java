package com.wuest.repurpose.Proxy;

import org.lwjgl.input.Keyboard;

import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Blocks.BlockCoffer;
import com.wuest.repurpose.Blocks.BlockCoffer.IronChestType;
import com.wuest.repurpose.Capabilities.ItemBagOfHoldingProvider;
import com.wuest.repurpose.Blocks.BlockCustomWall;
import com.wuest.repurpose.Blocks.BlockGrassSlab;
import com.wuest.repurpose.Blocks.BlockGrassStairs;
import com.wuest.repurpose.Config.WuestConfiguration;
import com.wuest.repurpose.Events.ClientEventHandler;
import com.wuest.repurpose.Events.WuestEventHandler;
import com.wuest.repurpose.Gui.GuiCoffer;
import com.wuest.repurpose.Gui.GuiItemBagOfHolding;
import com.wuest.repurpose.Gui.GuiRedstoneClock;
import com.wuest.repurpose.Gui.GuiRedstoneScanner;
import com.wuest.repurpose.Items.ItemBlockCoffer;
import com.wuest.repurpose.Renderer.TileEntityCofferRenderer;
import com.wuest.repurpose.Tiles.TileEntityCoffer;
import com.wuest.repurpose.particle.MysteriousParticle;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ClientProxy extends CommonProxy
{
	public WuestConfiguration serverConfiguration = null;
	public static ClientEventHandler clientEventHandler = new ClientEventHandler();

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{	 
		super.preInit(event);
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);

		// After all items have been registered and all recipes loaded, register
		// any necessary renderer.
		Repurpose.proxy.registerRenderers();
		this.RegisterEventListeners();
		
		this.RegisterKeyBindings();
	}

	@Override
	public void postinit(FMLPostInitializationEvent event)
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
		
		for (IronChestType type : IronChestType.values())
		{
			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCoffer.class, new TileEntityCofferRenderer());
		}
	}

	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx)
	{
		// Note that if you simply return 'Minecraft.getMinecraft().thePlayer',
		// your packets will not work as expected because you will be getting a
		// client player even when you are on the server!
		// Sounds absurd, but it's true.

		// Solution is to double-check side before returning the player:
		System.out.println("Retrieving player from ClientProxy for message on side " + ctx.side);
		return (ctx.side.isClient() ? Minecraft.getMinecraft().player : super.getPlayerEntity(ctx));
	}

	@Override
	public IThreadListener getThreadFromContext(MessageContext ctx)
	{
		return (ctx.side.isClient() ? Minecraft.getMinecraft() : super.getThreadFromContext(ctx));
	}

	@Override
	public void generateParticles(EntityPlayer player)
	{
		double motionX = player.world.rand.nextGaussian() * 0.02D;
		double motionY = player.world.rand.nextGaussian() * 0.02D;
		double motionZ = player.world.rand.nextGaussian() * 0.02D;
		Particle particleMysterious = new MysteriousParticle(player.world,
				player.posX + player.world.rand.nextFloat() * player.width * 2.0F - player.width,
				player.posY + 0.5D + player.world.rand.nextFloat() * player.height,
				player.posZ + player.world.rand.nextFloat() * player.width * 2.0F - player.width, motionX, motionY, motionZ);
		
		Minecraft.getMinecraft().effectRenderer.addEffect(particleMysterious);
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
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
	}
	
	@Override
	public WuestConfiguration getServerConfiguration()
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
		// DEBUG
		System.out.println("Registering event listeners");

		MinecraftForge.EVENT_BUS.register(clientEventHandler);
	}
	
	private void RegisterKeyBindings()
	{
		KeyBinding binding = new KeyBinding("Previous Item", net.minecraftforge.client.settings.KeyConflictContext.UNIVERSAL, KeyModifier.ALT, Keyboard.KEY_Z, "Repurpose - Bag of Holding");
		ClientEventHandler.keyBindings.add(binding);
		ClientRegistry.registerKeyBinding(binding);
		
		binding = new KeyBinding("Next Item", net.minecraftforge.client.settings.KeyConflictContext.UNIVERSAL, KeyModifier.ALT, Keyboard.KEY_X, "Repurpose - Bag of Holding");
		ClientEventHandler.keyBindings.add(binding);
		ClientRegistry.registerKeyBinding(binding);
	}
}
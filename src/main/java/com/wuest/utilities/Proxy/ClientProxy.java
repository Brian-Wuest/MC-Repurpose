package com.wuest.utilities.Proxy;

import com.wuest.utilities.ItemRenderRegister;
import com.wuest.utilities.WuestUtilities;
import com.wuest.utilities.Blocks.BlockCustomWall;
import com.wuest.utilities.Blocks.BlockGrassSlab;
import com.wuest.utilities.Blocks.BlockGrassStairs;
import com.wuest.utilities.Config.WuestConfiguration;
import com.wuest.utilities.Events.ClientEventHandler;
import com.wuest.utilities.Events.WuestEventHandler;
import com.wuest.utilities.particle.MysteriousParticle;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

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
		WuestUtilities.proxy.registerRenderers();
		this.RegisterEventListeners();
	}

	@Override
	public void postinit(FMLPostInitializationEvent event)
	{
	}

	@Override
	public void registerRenderers()
	{
		ItemRenderRegister.registerItemRenderer();

		// Register block colors.
		BlockGrassStairs.RegisterBlockRenderer();

		BlockGrassSlab.RegisterBlockRenderer();

		BlockCustomWall.RegisterBlockRenderer();
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
		return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
	}

	@Override
	public IThreadListener getThreadFromContext(MessageContext ctx)
	{
		return (ctx.side.isClient() ? Minecraft.getMinecraft() : super.getThreadFromContext(ctx));
	}

	@Override
	public void generateParticles(EntityPlayer player)
	{
		double motionX = player.worldObj.rand.nextGaussian() * 0.02D;
		double motionY = player.worldObj.rand.nextGaussian() * 0.02D;
		double motionZ = player.worldObj.rand.nextGaussian() * 0.02D;
		Particle particleMysterious = new MysteriousParticle(player.worldObj,
				player.posX + player.worldObj.rand.nextFloat() * player.width * 2.0F - player.width,
				player.posY + 0.5D + player.worldObj.rand.nextFloat() * player.height,
				player.posZ + player.worldObj.rand.nextFloat() * player.width * 2.0F - player.width, motionX, motionY, motionZ);
		
		Minecraft.getMinecraft().effectRenderer.addEffect(particleMysterious);
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
}
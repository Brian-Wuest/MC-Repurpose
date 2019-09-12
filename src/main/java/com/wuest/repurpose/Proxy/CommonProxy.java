package com.wuest.repurpose.Proxy;

import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Config.ModConfiguration;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.NetworkRegistry;

public class CommonProxy
{
	public static ModConfiguration proxyConfiguration;
	public static ForgeConfigSpec COMMON_SPEC;

    public CommonProxy() {
        // Builder.build is called during this method.
        Pair<ModConfiguration, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(ModConfiguration::new);
        COMMON_SPEC = commonPair.getRight();
        proxyConfiguration = commonPair.getLeft();
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, COMMON_SPEC);

        ModConfiguration.loadConfig(CommonProxy.COMMON_SPEC, FMLPaths.CONFIGDIR.get().resolve("repurpose.toml"));
    }

	/*
	 * Methods for ClientProxy to Override
	 */
	public void registerRenderers()
	{
	}

	public void preInit(FMLCommonSetupEvent event)
	{
		Repurpose.network = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Repurpose.MODID, "main_channel"))
			.clientAcceptedVersions(Repurpose.PROTOCOL_VERSION::equals)
			.serverAcceptedVersions(Repurpose.PROTOCOL_VERSION::equals)
			.networkProtocolVersion(() -> Repurpose.PROTOCOL_VERSION)
			.simpleChannel();

		ModRegistry.RegisterMessages();
		
		// Register the capabilities.
		ModRegistry.RegisterCapabilities();

		ModRegistry.RegisterEnchantments();
	}

	public void init(FMLCommonSetupEvent event)
	{
	}

	public void postinit(FMLCommonSetupEvent event)
	{
	}

/* 	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		
		if (ID == GuiCoffer.GUI_ID)
		{
			return new ContainerCoffer(player.inventory, (TileEntityCoffer)tileEntity, ((TileEntityCoffer)tileEntity).getType(), 0, 0);
		}
		else if (ID == GuiItemBagOfHolding.GUI_ID)
		{
			ItemStack stack = player.getHeldItemOffhand();
			ItemBagOfHoldingProvider handler = ItemBagOfHoldingProvider.GetFromStack(stack);
			((ItemBagOfHolding)stack.getItem()).RefreshItemStack(player, stack);
			return new BagOfHoldingContainer(handler, player);
		}
		
		return null;
	} */

	public void generateParticles(PlayerEntity players)
	{
	}
	
	public ModConfiguration getServerConfiguration()
	{
		return CommonProxy.proxyConfiguration;
	}
}
package com.wuest.repurpose.Proxy;

import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Config.ModConfiguration;
import com.wuest.repurpose.Crafting.RecipeCondition;
import com.wuest.repurpose.Crafting.SmeltingCondition;
import com.wuest.repurpose.Loot.Conditions.ConfigRandomChance;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.NetworkRegistry;

public class CommonProxy {
	public static ModConfiguration proxyConfiguration;
	public static ForgeConfigSpec COMMON_SPEC;

	public CommonProxy() {
		// Builder.build is called during this method.
		Pair<ModConfiguration, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder()
				.configure(ModConfiguration::new);
		COMMON_SPEC = commonPair.getRight();
		proxyConfiguration = commonPair.getLeft();
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, COMMON_SPEC);

		ModConfiguration.loadConfig(CommonProxy.COMMON_SPEC, FMLPaths.CONFIGDIR.get().resolve("repurpose.toml"));
	}

	/*
	 * Methods for ClientProxy to Override
	 */
	public void registerRenderers() {
	}

	public void preInit(FMLCommonSetupEvent event) {
		// Register recipe conditions
		CraftingHelper.register(new RecipeCondition.Serializer());
		CraftingHelper.register(new SmeltingCondition.Serializer());

		// Register loot table conditions.
		LootConditionManager.registerCondition(new ConfigRandomChance.Serializer());

		Repurpose.network = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Repurpose.MODID, "main_channel"))
				.clientAcceptedVersions(Repurpose.PROTOCOL_VERSION::equals)
				.serverAcceptedVersions(Repurpose.PROTOCOL_VERSION::equals)
				.networkProtocolVersion(() -> Repurpose.PROTOCOL_VERSION).simpleChannel();

		ModRegistry.RegisterMessages();

		// Register the capabilities.
		ModRegistry.RegisterCapabilities();
	}

	public void init(FMLCommonSetupEvent event) {
	}

	public void postinit(FMLCommonSetupEvent event) {
	}

	public void openGuiForItem(ItemUseContext itemUseContext) {
	}

	public void openGuiForBlock(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
	}

	/*
	 * @Override public Object getServerGuiElement(int ID, EntityPlayer player,
	 * World world, int x, int y, int z) { TileEntity tileEntity =
	 * world.getTileEntity(new BlockPos(x, y, z));
	 * 
	 * if (ID == GuiItemBagOfHolding.GUI_ID) { ItemStack stack =
	 * player.getHeldItemOffhand(); ItemBagOfHoldingProvider handler =
	 * ItemBagOfHoldingProvider.GetFromStack(stack);
	 * ((ItemBagOfHolding)stack.getItem()).RefreshItemStack(player, stack); return
	 * new BagOfHoldingContainer(handler, player); }
	 * 
	 * return null; }
	 */

	public void generateParticles(PlayerEntity players) {
	}

	public ModConfiguration getServerConfiguration() {
		return CommonProxy.proxyConfiguration;
	}
}
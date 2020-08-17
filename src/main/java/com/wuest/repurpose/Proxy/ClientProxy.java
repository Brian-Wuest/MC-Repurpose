package com.wuest.repurpose.Proxy;

import com.wuest.repurpose.Blocks.BlockCustomWall;
import com.wuest.repurpose.Blocks.BlockCustomWall.EnumType;
import com.wuest.repurpose.Blocks.BlockGrassSlab;
import com.wuest.repurpose.Blocks.BlockGrassStairs;
import com.wuest.repurpose.Config.ModConfiguration;
import com.wuest.repurpose.Events.ClientEventHandler;
import com.wuest.repurpose.Gui.BasicGui;
import com.wuest.repurpose.Gui.GuiRedstoneClock;
import com.wuest.repurpose.Gui.GuiRedstoneScanner;
import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Repurpose;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GrassColors;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;

public class ClientProxy extends CommonProxy {
	public static ClientEventHandler clientEventHandler = new ClientEventHandler();
	/**
	 * The hashmap of mod item guis.
	 */
	public static HashMap<Item, BasicGui> ModItemGuis = new HashMap<>();
	/**
	 * The hashmap of mod block guis.
	 */
	public static HashMap<Block, BasicGui> ModBlockGuis = new HashMap<>();
	public ModConfiguration serverConfiguration = null;

	public ClientProxy() {
		super();
	}

	/**
	 * Adds all of the Mod Guis to the HasMap.
	 */
	public static void AddGuis() {
		ClientProxy.ModBlockGuis.put(ModRegistry.RedStoneClock.get(), new GuiRedstoneClock());
		ClientProxy.ModBlockGuis.put(ModRegistry.RedstoneScanner.get(), new GuiRedstoneScanner());
	}

	@OnlyIn(Dist.CLIENT)
	public static void RegisterBlockRenderer() {
		// Register the block renderer.
		Minecraft.getInstance().getBlockColors().register((state, worldIn, pos, tintIndex) -> worldIn != null && pos != null
				? BiomeColors.getGrassColor(worldIn, pos)
				: GrassColors.get(0.5D, 1.0D), ModRegistry.GrassWall.get(), ModRegistry.GrassSlab.get(), ModRegistry.GrassStairs.get());

		// Register the item renderer.
		Minecraft.getInstance().getItemColors().register((stack, tintIndex) -> {
			// Get the item for this stack.
			Item item = stack.getItem();

			if (item instanceof BlockItem) {
				// Get the block for this item and determine if it's a grass stairs.
				BlockItem itemBlock = (BlockItem) item;
				boolean paintBlock = false;

				if (itemBlock.getBlock() instanceof BlockCustomWall) {
					BlockCustomWall customWall = (BlockCustomWall) itemBlock.getBlock();

					if (customWall.BlockVariant == EnumType.GRASS) {
						paintBlock = true;
					}
				} else if (itemBlock.getBlock() instanceof BlockGrassSlab) {
					paintBlock = true;
				} else if (itemBlock.getBlock() instanceof BlockGrassStairs) {
					paintBlock = true;
				}

				if (paintBlock) {
					// TODO: This used to be the getPosition method.
					BlockPos pos = Minecraft.getInstance().player.getPosition();
					ClientWorld world = Minecraft.getInstance().world;
					return BiomeColors.getGrassColor(world, pos);
				}
			}

			return -1;
		}, new Block[]{ModRegistry.GrassWall.get(), ModRegistry.GrassSlab.get(), ModRegistry.GrassStairs.get()});
	}

	@Override
	public void preInit(FMLCommonSetupEvent event) {
		super.preInit(event);
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
		super.init(event);

		// After all items have been registered and all recipes loaded, register
		// any necessary renderer.
		Repurpose.proxy.registerRenderers();
		this.RegisterEventListeners();
	}

	@Override
	public void postinit(FMLCommonSetupEvent event) {
		super.postinit(event);

		ClientProxy.AddGuis();
	}

	@Override
	public void clientSetup(FMLClientSetupEvent event) {
		this.RegisterKeyBindings();

		// This render type (func_228643_e_) is the "cutout" render type.
		RenderTypeLookup.setRenderLayer(ModRegistry.DirtWall.get(), RenderType.getCutoutMipped());

		RenderTypeLookup.setRenderLayer(ModRegistry.GrassWall.get(), RenderType.getCutoutMipped());

		RenderTypeLookup.setRenderLayer(ModRegistry.GrassStairs.get(), RenderType.getCutoutMipped());

		RenderTypeLookup.setRenderLayer(ModRegistry.DirtStairs.get(), RenderType.getCutoutMipped());

		RenderTypeLookup.setRenderLayer(ModRegistry.EnrichedFarmland.get(), RenderType.getCutoutMipped());

		RenderTypeLookup.setRenderLayer(ModRegistry.GrassSlab.get(), RenderType.getCutoutMipped());

		RenderTypeLookup.setRenderLayer(ModRegistry.MiniRedstone.get(), RenderType.getCutout());

		RenderTypeLookup.setRenderLayer(ModRegistry.RedstoneScanner.get(), RenderType.getCutout());

		DeferredWorkQueue.runLater(ClientSetup::init);
	}

	@Override
	public void registerRenderers() {
		// Register block colors.
		ClientProxy.RegisterBlockRenderer();
	}

	@Override
	public void openGuiForItem(ItemUseContext itemUseContext, Container container) {
		ItemStack stack = itemUseContext.getPlayer().getHeldItemOffhand();
		Screen screenToShow = null;

		Minecraft.getInstance().displayGuiScreen(screenToShow);
	}

	@Override
	public void openGuiForBlock(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
		for (Map.Entry<Block, BasicGui> entry : ClientProxy.ModBlockGuis.entrySet()) {
			if (entry.getKey() == state.getBlock()) {
				BasicGui screen = entry.getValue();
				screen.pos = pos;

				Minecraft.getInstance().displayGuiScreen(screen);
			}
		}
	}

	@Override
	public ModConfiguration getServerConfiguration() {
		if (this.serverConfiguration == null) {
			// Get the server configuration.
			return CommonProxy.proxyConfiguration;
		} else {
			return this.serverConfiguration;
		}
	}

	private void RegisterEventListeners() {
	}

	private void RegisterKeyBindings() {
		DeferredWorkQueue.runLater(() -> {
			KeyBinding binding = new KeyBinding("Previous Item",
					KeyConflictContext.IN_GAME, KeyModifier.ALT,
					InputMappings.Type.KEYSYM, GLFW_KEY_Z, "Repurpose - Bag of Holding");

			ClientEventHandler.keyBindings.add(binding);
			ClientRegistry.registerKeyBinding(binding);

			binding = new KeyBinding("Next Item", KeyConflictContext.IN_GAME,
					KeyModifier.ALT, InputMappings.Type.KEYSYM, GLFW_KEY_X, "Repurpose - Bag of Holding");
			ClientEventHandler.keyBindings.add(binding);
			ClientRegistry.registerKeyBinding(binding);
		});
	}
}
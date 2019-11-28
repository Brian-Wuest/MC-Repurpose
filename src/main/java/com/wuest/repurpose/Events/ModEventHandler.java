package com.wuest.repurpose.Events;

import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Repurpose;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author WuestMan The eventbus for this has to be set to "Mod" instead of the
 *         standard forge in order for it to register the blocks.
 */
@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Repurpose.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandler {
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		for (Block block : ModRegistry.ModBlocks) {
			Repurpose.LOGGER
					.debug("Logging Block With Name: " + block.getRegistryName() + " and type: " + block.toString());
			event.getRegistry().register(block);
		}

	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(ModRegistry.ModItems.toArray(new Item[0]));
	}
}

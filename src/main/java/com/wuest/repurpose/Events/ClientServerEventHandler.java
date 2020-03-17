package com.wuest.repurpose.Events;

import com.wuest.repurpose.Enchantment.EnchantmentStepAssist;
import com.wuest.repurpose.Gui.GuiItemBagOfHolding;
import com.wuest.repurpose.Items.Containers.BagOfHoldingContainer;
import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Repurpose;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * @author WuestMan The eventbus for this has to be set to "Mod" instead of the
 * standard forge in order for it to register the blocks.
 */
@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Repurpose.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientServerEventHandler {
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

	@SubscribeEvent
	public static void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
		ModRegistry.stepAssist = new EnchantmentStepAssist(Enchantment.Rarity.COMMON, EnchantmentType.ARMOR_FEET,
				new EquipmentSlotType[]{EquipmentSlotType.FEET});

		event.getRegistry().register(ModRegistry.stepAssist);
	}

	@SubscribeEvent
	public static void registerContainers(RegistryEvent.Register<ContainerType<?>> evt) {
		IForgeRegistry<ContainerType<?>> registry = evt.getRegistry();

		ContainerType<BagOfHoldingContainer> bag = IForgeContainerType.create(BagOfHoldingContainer::fromNetwork);
		bag.setRegistryName(ModRegistry.BagofHolding().getRegistryName());
		BagOfHoldingContainer.containerType = bag;

		registry.register(bag);

		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> ScreenManager.registerFactory(bag, GuiItemBagOfHolding::new));
	}
}

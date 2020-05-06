package com.wuest.repurpose.Events;

import com.wuest.repurpose.Gui.GuiItemBagOfHolding;
import com.wuest.repurpose.Items.Containers.BagOfHoldingContainer;
import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Repurpose;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
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

/*	@SubscribeEvent
	public static void registerContainers(RegistryEvent.Register<ContainerType<?>> evt) {
		IForgeRegistry<ContainerType<?>> registry = evt.getRegistry();

		ContainerType<BagOfHoldingContainer> bag = IForgeContainerType.create(BagOfHoldingContainer::fromNetwork);
		bag.setRegistryName(ModRegistry.BagOfHolding.get().getRegistryName());
		BagOfHoldingContainer.containerType = bag;

		registry.register(bag);

		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> ScreenManager.registerFactory(bag, GuiItemBagOfHolding::new));
	}*/
}

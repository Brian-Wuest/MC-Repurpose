package com.wuest.repurpose.Proxy;

import com.wuest.repurpose.Gui.GuiItemBagOfHolding;
import com.wuest.repurpose.ModRegistry;
import net.minecraft.client.gui.ScreenManager;

public class ClientSetup {
	public static void init() {
		registerScreenFactories();
	}

	private static void registerScreenFactories() {
		ScreenManager.registerFactory(ModRegistry.BagOfHoldingContainer.get(), GuiItemBagOfHolding::new);
	}
}

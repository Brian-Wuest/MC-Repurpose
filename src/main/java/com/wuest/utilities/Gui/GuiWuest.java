package com.wuest.utilities.Gui;

import com.wuest.utilities.WuestUtilities;
import com.wuest.utilities.Config.WuestConfiguration;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.ConfigGuiType;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.IConfigEntry;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.config.IConfigElement;

public class GuiWuest extends GuiConfig
{
	public GuiWuest(GuiScreen parent)
	{
		super(parent,
				new ConfigElement(WuestUtilities.config.getCategory(WuestConfiguration.OPTIONS)).getChildElements(),
				WuestUtilities.MODID, null, false, false, GuiConfig.getAbridgedConfigPath(WuestUtilities.config.toString()), null);

		ConfigCategory category = WuestUtilities.config.getCategory(WuestConfiguration.OPTIONS);
		String abridgedConfigPath = GuiConfig.getAbridgedConfigPath(WuestUtilities.config.toString());
	}

	@Override
	public void initGui()
	{
		if (this.entryList == null || this.needsRefresh)
		{
			this.entryList = new GuiConfigEntries(this, mc);
			this.needsRefresh = false;
		}

		super.initGui();
	}
}
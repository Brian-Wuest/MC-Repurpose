package com.wuest.repurpose.Gui;

import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Config.ModConfiguration;

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
				new ConfigElement(Repurpose.config.getCategory(ModConfiguration.OPTIONS)).getChildElements(),
				Repurpose.MODID, null, false, false, GuiConfig.getAbridgedConfigPath(Repurpose.config.toString()), null);

		ConfigCategory category = Repurpose.config.getCategory(ModConfiguration.OPTIONS);
		String abridgedConfigPath = GuiConfig.getAbridgedConfigPath(Repurpose.config.toString());
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
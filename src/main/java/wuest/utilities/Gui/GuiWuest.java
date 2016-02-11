package wuest.utilities.Gui;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import wuest.utilities.*;

public class GuiWuest extends GuiConfig
{
	public GuiWuest(GuiScreen parent)
	{
		super(parent,
                new ConfigElement(WuestUtilities.config.getCategory(WuestConfiguration.OPTIONS)).getChildElements(),
                WuestUtilities.MODID, false, false, GuiConfig.getAbridgedConfigPath(WuestUtilities.config.toString()));
		
		ConfigCategory category = WuestUtilities.config.getCategory(WuestConfiguration.OPTIONS);
		String abridgedConfigPath = GuiConfig.getAbridgedConfigPath(WuestUtilities.config.toString());
	}
}

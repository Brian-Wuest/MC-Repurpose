package com.wuest.utilities.Gui;

import java.util.Set;

import com.wuest.utilities.WuestUtilities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.DefaultGuiFactory;
import net.minecraftforge.fml.client.IModGuiFactory;

public class ConfigGuiFactory extends DefaultGuiFactory 
{
	public ConfigGuiFactory() 
	{
		super(WuestUtilities.MODID, "WuestUtilities");
	}
	
	@Override
	public void initialize(Minecraft minecraftInstance) 
	{
		super.initialize(minecraftInstance);
	}

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen)
    {  
        return new GuiWuest(parentScreen);
    }
}
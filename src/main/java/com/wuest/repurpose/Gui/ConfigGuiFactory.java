package com.wuest.repurpose.Gui;

import java.util.Set;

import com.wuest.repurpose.Repurpose;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.DefaultGuiFactory;
import net.minecraftforge.fml.client.IModGuiFactory;

public class ConfigGuiFactory extends DefaultGuiFactory 
{
	public ConfigGuiFactory() 
	{
		super(Repurpose.MODID, "Repurpose");
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
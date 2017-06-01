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
		super("", "");
		
		this.modid = WuestUtilities.MODID;
		this.title = "WuestUtilities";
	}
	
	@Override
	public void initialize(Minecraft minecraftInstance) 
	{
	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() 
	{
		return GuiWuest.class;
	}
	
    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen)
    {  
        return new GuiWuest(parentScreen);
    }
}

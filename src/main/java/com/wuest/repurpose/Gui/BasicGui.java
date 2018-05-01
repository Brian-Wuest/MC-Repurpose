package com.wuest.repurpose.Gui;

import net.minecraft.client.gui.Gui;

public class BasicGui extends Gui
{

	public float getZLevel()
	{
		return this.zLevel;
	}
	
	public BasicGui setZLevel(float value)
	{
		this.zLevel = value;
		return this;
	}
}
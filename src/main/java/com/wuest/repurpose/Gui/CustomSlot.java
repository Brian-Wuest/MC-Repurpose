package com.wuest.repurpose.Gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class CustomSlot extends Slot
{
	private boolean enabled;
	
    public CustomSlot(IInventory inventoryIn, int index, int xPosition, int yPosition)
    {
        super(inventoryIn, index, xPosition, yPosition);
        this.enabled = true;
    }
    
    @Override
    public boolean isEnabled()
    {
    	return this.enabled;
    }
    
    public CustomSlot SetEnabled(boolean value)
    {
    	this.enabled = value;
    	return this;
    }
}

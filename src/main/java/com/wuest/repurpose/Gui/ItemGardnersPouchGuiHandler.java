package com.wuest.repurpose.Gui;

import com.wuest.repurpose.Items.Containers.ItemGardnersPouchContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * 
 * @author WuestMan
 *
 */
public class ItemGardnersPouchGuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer p, World w, int x, int y, int z) {
		return new ItemGardnersPouchContainer((IItemHandler)p.getHeldItemMainhand().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null ), p );
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer p, World w, int x, int y, int z) {
		return new GuiItemGardnersPouch((IItemHandler)p.getHeldItemMainhand().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null ), p );
	}
}

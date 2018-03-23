package com.wuest.repurpose.Items.Containers;

import com.wuest.repurpose.Capabilities.GardnersPouchProvider;
import com.wuest.repurpose.Items.ItemGardnersPouch;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * 
 * @author WuestMan
 *
 */
public class ItemGardnersPouchContainer extends Container
{
	private IItemHandler handler;
	
	public ItemGardnersPouchContainer(IItemHandler i, EntityPlayer p)
	{
		int xPos = 8;
		int yPos = 18;
		int iid = 0;

		for (int y = 0; y < 6; ++y)
		{
			for (int x = 0; x < 9; ++x)
			{
				addSlotToContainer(new SlotItemHandler(i, iid, xPos + x * 18, yPos + y * 18));
				iid++;
			}
		}

		yPos = 140;

		for (int y = 0; y < 3; ++y)
		{
			for (int x = 0; x < 9; ++x)
			{
				addSlotToContainer(new Slot(p.inventory, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
			}
		}

		for (int x = 0; x < 9; ++x)
		{
			addSlotToContainer(new Slot(p.inventory, x, xPos + x * 18, 198));
		}
	}

	/**
	 * Allow for SHIFT click transfers
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot)
	{
		ItemStack previous = ItemStack.EMPTY;
		Slot slot = (Slot) this.inventorySlots.get(fromSlot);

		if (slot != null && slot.getHasStack())
		{
			ItemStack current = slot.getStack();
			previous = current.copy();

			if (fromSlot < this.handler.getSlots())
			{
				// From the energy cell inventory to the player's inventory
				if (!this.mergeItemStack(current, this.handler.getSlots(), handler.getSlots() + 36, true))
				{
					return ItemStack.EMPTY;
				}
			}
			else
			{
				// From the player's inventory to the block breaker's inventory
				if (!this.mergeItemStack(current, 0, this.handler.getSlots(), false))
				{
					return ItemStack.EMPTY;
				}
			}

			if (current.getCount() == 0)
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (current.getCount() == previous.getCount())
			{
				return ItemStack.EMPTY;
			}
			
			slot.onTake(playerIn, current);
		}
		
		return previous;
	}

	@Override
	public boolean canInteractWith(EntityPlayer p)
	{
		return true;
	}
}

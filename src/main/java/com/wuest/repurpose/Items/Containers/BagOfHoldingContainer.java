package com.wuest.repurpose.Items.Containers;

import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Capabilities.ItemBagOfHoldingProvider;
import com.wuest.repurpose.Items.ItemBagOfHolding;
import com.wuest.repurpose.Proxy.Messages.BagOfHoldingUpdateMessage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
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
public class BagOfHoldingContainer extends Container
{
	private IItemHandler handler;
	private EntityPlayer player;
	
	public BagOfHoldingContainer(IItemHandler itemHandler, EntityPlayer player)
	{
		int xPos = 8;
		int yPos = 18;
		int iid = 0;

		for (int y = 0; y < 6; ++y)
		{
			for (int x = 0; x < 9; ++x)
			{
				addSlotToContainer(new BagOfHoldingSlot(itemHandler, iid, xPos + x * 18, yPos + y * 18));
				iid++;
			}
		}

		yPos = 140;

		for (int y = 0; y < 3; ++y)
		{
			for (int x = 0; x < 9; ++x)
			{
				addSlotToContainer(new Slot(player.inventory, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
			}
		}

		for (int x = 0; x < 9; ++x)
		{
			addSlotToContainer(new Slot(player.inventory, x, xPos + x * 18, 198));
		}
		
		this.handler = itemHandler;
		this.player = player;
	}
	
    /**
     * Called when the container is closed.
     */
    @Override
	public void onContainerClosed(EntityPlayer playerIn)
    {
    	super.onContainerClosed(playerIn);
    	
    	this.UpdateStack();
    }

	/**
	 * Allow for SHIFT click transfers
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot)
	{
		try
		{
			ItemStack previous = ItemStack.EMPTY;
			Slot slot = (Slot) this.inventorySlots.get(fromSlot);
	
			if (slot != null && slot.getHasStack())
			{
				ItemStack current = slot.getStack();
				previous = current.copy();
	
				if (this.handler == null)
				{
					return ItemStack.EMPTY;
				}
				
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
		finally
		{
	    	this.UpdateStack();
		}
	}

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
	public void detectAndSendChanges()
    {
    	boolean foundChangesToSend = false;
        for (int i = 0; i < this.inventorySlots.size(); ++i)
        {
            ItemStack itemstack = ((Slot)this.inventorySlots.get(i)).getStack();
            ItemStack itemstack1 = this.inventoryItemStacks.get(i);

            if (!ItemStack.areItemStacksEqual(itemstack1, itemstack))
            {
                boolean clientStackChanged = !ItemStack.areItemStacksEqualUsingNBTShareTag(itemstack1, itemstack);
                itemstack1 = itemstack.isEmpty() ? ItemStack.EMPTY : itemstack.copy();
                this.inventoryItemStacks.set(i, itemstack1);

                if (clientStackChanged) 
                {
	                for (int j = 0; j < this.listeners.size(); ++j)
	                {
	                    ((IContainerListener)this.listeners.get(j)).sendSlotContents(this, i, itemstack1);
	                }
	                
	                foundChangesToSend = true;
                }
            }
        }
    	
        if (foundChangesToSend)
        {
        	this.UpdateStack();
        }
    }
	
	@Override
	public boolean canInteractWith(EntityPlayer p)
	{
		return true;
	}
	
	public void UpdateStack()
	{
		ItemStack heldStack = this.player.getHeldItemOffhand();
    	
    	if (this.handler instanceof ItemBagOfHoldingProvider)
    	{
    		((ItemBagOfHoldingProvider)this.handler).UpdateStack(heldStack);
    		
    		if (player instanceof EntityPlayerMP)
    		{
	    		BagOfHoldingUpdateMessage message = new BagOfHoldingUpdateMessage(heldStack.getTagCompound().getCompoundTag(ItemBagOfHoldingProvider.handlerKey));
	    		Repurpose.network.sendTo(message, (EntityPlayerMP)player);
    		}
    	}
	}
}

package com.wuest.repurpose.Items.Containers;

import com.wuest.repurpose.Items.ItemBagOfHolding;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BagOfHoldingSlot extends SlotItemHandler
{
    public BagOfHoldingSlot(IItemHandler inventory, int slotIndexIn, int xPosition, int yPosition)
    {
        super(inventory, slotIndexIn, xPosition, yPosition);
    }
    
    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    @Override
    public boolean isItemValid(ItemStack stack)
    {
    	// Don't allow block containers, bags of holding or anything which has an item_handler_capability into this slot.
    	return BagOfHoldingContainer.validForContainer(stack);
    }
}

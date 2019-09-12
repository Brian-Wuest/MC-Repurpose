package com.wuest.repurpose.Gui;

import java.io.IOException;

import com.wuest.repurpose.Blocks.BlockCoffer.CofferType;
import com.wuest.repurpose.Tiles.TileEntityCoffer;

import net.minecraft.entity.player.PlayerInventory;;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

/**
 * 
 * @author WuestMan
 *
 */
public class ContainerCoffer extends Container
{
	public static final ContainerType<ContainerCoffer> Coffer = ContainerCoffer.register("coffer", ContainerCoffer::new);

    private CofferType type;

    private PlayerEntity player;

    private IInventory chest;
    
	private Inventory basicInventory;
	
	public static <T extends Container> ContainerType<T> register(String key, ContainerType.IFactory<T> factory)
	{
		return Registry.register(Registry.MENU, key, new ContainerType<>(factory));
	}

	public ContainerCoffer(int id, PlayerInventory playerInventory) {
		this(id, playerInventory, new Inventory(CofferType.IRON.size), CofferType.IRON, CofferType.IRON.xSize, CofferType.IRON.ySize);
	}

    public ContainerCoffer(int id, IInventory playerInventory, IInventory chestInventory, CofferType type, int xSize, int ySize)
    {
		super(ContainerCoffer.Coffer, id);
        this.chest = chestInventory;
        this.player = ((PlayerInventory) playerInventory).player;
        this.type = type;
        chestInventory.openInventory(this.player);
        this.layoutContainer(playerInventory, chestInventory, type, xSize, ySize);
        this.basicInventory = new Inventory(54);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn)
    {
        return this.chest.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < this.type.size)
            {
                if (!this.mergeItemStack(itemstack1, this.type.size, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.type.acceptsStack(itemstack1))
            {
                return ItemStack.EMPTY;
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.type.size, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn)
    {
        super.onContainerClosed(playerIn);

        this.chest.closeInventory(playerIn);
    }

    protected void layoutContainer(IInventory playerInventory, IInventory chestInventory, CofferType type, int xSize, int ySize)
    {
        for (int chestRow = 0; chestRow < type.getRowCount(); chestRow++)
        {
            for (int chestCol = 0; chestCol < type.rowLength; chestCol++)
            {
                this.addSlot(type.makeSlot(chestInventory, chestCol + chestRow * type.rowLength, 12 + chestCol * 18, 8 + chestRow * 18));
            }
        }

        int leftCol = (xSize - 162) / 2 + 1;

        for (int playerInvRow = 0; playerInvRow < 3; playerInvRow++)
        {
            for (int playerInvCol = 0; playerInvCol < 9; playerInvCol++)
            {
                this.addSlot(
                        new CustomSlot(playerInventory, playerInvCol + playerInvRow * 9 + 9, leftCol + playerInvCol * 18, ySize - (4 - playerInvRow) * 18 - 10));
            }

        }

        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++)
        {
            this.addSlot(new CustomSlot(playerInventory, hotbarSlot, leftCol + hotbarSlot * 18, ySize - 24));
        }
    }
    
    /**
     * Updates the gui slots ItemStack's based on scroll position.
     */
    public void scrollTo(float pos)
    {
    	// The number 10 here is for the number of rows in this coffer.
        int i = (this.type.size + 10 - 1) / 10 - 5;
        int j = (int)((double)(pos * (float)i) + 0.5D);

        if (j < 0)
        {
            j = 0;
        }
        
        for (Slot slot : this.inventorySlots)
        {
        	if (slot.inventory instanceof TileEntityCoffer)
        	{
        		((CustomSlot)slot).SetEnabled(false);
        	}
        }

        for (int k = 0; k < 6; ++k)
        {
            for (int l = 0; l < 9; ++l)
            {
                int i1 = l + (k + j) * 9;

                CustomSlot currentSlot = (CustomSlot)this.inventorySlots.get(i1);
                
                if (currentSlot.inventory instanceof TileEntityCoffer)
                {
                	currentSlot.SetEnabled(true);
                	currentSlot.yPos = 8 + k * 18;
                }
                
                if (i1 >= 0 && i1 < this.type.size)
                {
                    this.basicInventory.setInventorySlotContents(l + k * 9, this.inventorySlots.get(i1).getStack());
                }
                else
                {
                    this.basicInventory.setInventorySlotContents(l + k * 9, ItemStack.EMPTY);
                }
            }
        }
    }

    public PlayerEntity getPlayer()
    {
        return this.player;
    }

    public int getNumColumns()
    {
        return this.type.rowLength;
    }
}
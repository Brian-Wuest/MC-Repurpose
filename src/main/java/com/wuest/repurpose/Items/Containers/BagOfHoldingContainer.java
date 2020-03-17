package com.wuest.repurpose.Items.Containers;

import java.util.List;

import com.google.common.collect.Lists;
import com.sun.org.apache.bcel.internal.generic.RET;
import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Capabilities.ItemBagOfHoldingProvider;
import com.wuest.repurpose.Gui.GuiItemBagOfHolding;
import com.wuest.repurpose.Items.ItemBagOfHolding;
import com.wuest.repurpose.Proxy.Messages.BagOfHoldingUpdateMessage;

import net.minecraft.block.Block;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.items.CapabilityItemHandler;

/**
 * 
 * @author WuestMan
 *
 */
public class BagOfHoldingContainer extends Container {
	private ItemBagOfHoldingProvider inventory;
	private PlayerInventory playerInventory;

	public static ContainerType<BagOfHoldingContainer> containerType = new ContainerType<>(BagOfHoldingContainer::new);

	public static BagOfHoldingContainer fromNetwork(int windowId, PlayerInventory inv, PacketBuffer buf) {
		return new BagOfHoldingContainer(windowId, inv);
	}

	public BagOfHoldingContainer(int windowId, PlayerInventory playerInventory) {
		super(BagOfHoldingContainer.containerType, windowId);
		int xPos = 8;
		int yPos = 18;
		int iid = 0;

		ItemStack stack = playerInventory.offHandInventory.get(0);

		this.inventory = ItemBagOfHoldingProvider.HasProvider(stack);

		if (this.inventory == null) {
			// Not in the off-hand, use the current item.
			this.inventory = ItemBagOfHoldingProvider.GetFromStack(playerInventory.getCurrentItem());
		}

		for (int y = 0; y < 6; ++y) {
			for (int x = 0; x < 9; ++x) {
				this.addSlot(new BagOfHoldingSlot(inventory, iid, xPos + x * 18, yPos + y * 18));
				iid++;
			}
		}

		yPos = 140;

		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				this.addSlot(new Slot(playerInventory, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
			}
		}

		for (int x = 0; x < 9; ++x) {
			this.addSlot(new Slot(playerInventory, x, xPos + x * 18, 198));
		}

		this.playerInventory = playerInventory;
	}

	/**
	 * Determines if the stack is valid for this container.
	 * 
	 * @param stack - The stack to check.
	 * @return True if the stack is valid for the bag of holding, otherwise false.
	 */
	public static boolean validForContainer(ItemStack stack) {
		if (Block.getBlockFromItem(stack.getItem()) instanceof ContainerBlock
				|| stack.getItem() instanceof ItemBagOfHolding
				|| stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()
				|| stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.DOWN).isPresent()
				|| stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.EAST).isPresent()
				|| stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.NORTH).isPresent()
				|| stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.SOUTH).isPresent()
				|| stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).isPresent()
				|| stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.WEST).isPresent()) {
			return false;
		}

		return true;
	}

	/**
	 * Called when the container is closed.
	 */
	@Override
	public void onContainerClosed(PlayerEntity playerIn) {
		super.onContainerClosed(playerIn);

		this.UpdateStack();
	}

	/**
	 * Allow for SHIFT click transfers
	 */
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int fromSlot) {
		try {
			ItemStack previous = ItemStack.EMPTY;
			Slot slot = (Slot) this.inventorySlots.get(fromSlot);

			if (slot != null && slot.getHasStack()) {
				ItemStack current = slot.getStack();
				previous = current.copy();

				if (this.inventory == null) {
					return ItemStack.EMPTY;
				}

				if (fromSlot < this.inventory.getSlots()) {
					// From the energy cell inventory to the player's inventory
					if (!this.mergeItemStack(current, this.inventory.getSlots(), this.inventory.getSlots() + 36,
							true)) {
						return ItemStack.EMPTY;
					}
				} else {
					// From the player's inventory to the block breaker's inventory
					if (!this.mergeItemStack(current, 0, this.inventory.getSlots(), false)) {
						return ItemStack.EMPTY;
					}
				}

				if (current.getCount() == 0) {
					slot.putStack(ItemStack.EMPTY);
				} else {
					slot.onSlotChanged();
				}

				if (current.getCount() == previous.getCount()) {
					return ItemStack.EMPTY;
				}

				slot.onTake(playerIn, current);
			}

			return previous;
		} finally {
			this.UpdateStack();
		}
	}

	/**
	 * Looks for changes made in the container, sends them to every listener.
	 */
	@Override
	public void detectAndSendChanges() {
		boolean foundChangesToSend = false;

		// Only do the evaulation on whether or not the inventory has changed. Let the base method update the inventory and notify the listeners.
		for (int i = 0; i < this.inventorySlots.size(); ++i) {
			ItemStack itemstack = this.inventorySlots.get(i).getStack();
			ItemStack itemstack1 = this.inventoryItemStacks.get(i);

			if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
				foundChangesToSend = !itemstack1.equals(itemstack, true);

				if (foundChangesToSend) {
					break;
				}
			}
		}

		super.detectAndSendChanges();

		if (foundChangesToSend) {
			this.UpdateStack();
		}
	}

	@Override
	public boolean canInteractWith(PlayerEntity p) {
		return true;
	}

	public void UpdateStack() {
		ItemStack heldStack = this.playerInventory.offHandInventory.get(0);

		if (this.inventory instanceof ItemBagOfHoldingProvider) {
			((ItemBagOfHoldingProvider) this.inventory).UpdateStack(heldStack);

			if (this.playerInventory.player instanceof ServerPlayerEntity) {
				BagOfHoldingUpdateMessage message = new BagOfHoldingUpdateMessage(
						heldStack.getTag().getCompound(ItemBagOfHoldingProvider.handlerKey));

				Repurpose.network.sendTo(message, ((ServerPlayerEntity) playerInventory.player).connection.netManager,
						NetworkDirection.PLAY_TO_CLIENT);
			}
		}
	}
}

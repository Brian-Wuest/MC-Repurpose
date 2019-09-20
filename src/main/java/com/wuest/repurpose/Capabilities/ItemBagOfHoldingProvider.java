package com.wuest.repurpose.Capabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemStackHandler;

public class ItemBagOfHoldingProvider extends ItemStackHandler {
	public static final String handlerKey = "bagOfHoldingItems";
	public static final String inventoryKey = "inventory";
	public static final String refreshValueKey = "refreshValue";
	public static final String slotIndexKey = "slotIndex";
	public static final String openedKey = "opened";

	public boolean refreshValue;
	public int slotIndex;
	public boolean opened;

	public ItemBagOfHoldingProvider() {
		super(54);

		this.refreshValue = false;
		this.slotIndex = 0;
		this.opened = false;
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT inventoryTag = super.serializeNBT();
		CompoundNBT returnTag = new CompoundNBT();

		returnTag.putBoolean(ItemBagOfHoldingProvider.refreshValueKey, this.refreshValue);
		returnTag.putInt(ItemBagOfHoldingProvider.slotIndexKey, this.slotIndex);
		returnTag.put(ItemBagOfHoldingProvider.inventoryKey, inventoryTag);
		returnTag.putBoolean(ItemBagOfHoldingProvider.openedKey, this.opened);

		return returnTag;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		if (nbt.contains(ItemBagOfHoldingProvider.inventoryKey)) {
			super.deserializeNBT(nbt.getCompound(ItemBagOfHoldingProvider.inventoryKey));
		}

		if (nbt.contains(ItemBagOfHoldingProvider.refreshValueKey)) {
			this.refreshValue = nbt.getBoolean(ItemBagOfHoldingProvider.refreshValueKey);
		}

		if (nbt.contains(ItemBagOfHoldingProvider.slotIndexKey)) {
			this.slotIndex = nbt.getInt(ItemBagOfHoldingProvider.slotIndexKey);
		}

		if (nbt.contains(ItemBagOfHoldingProvider.openedKey)) {
			this.opened = nbt.getBoolean(ItemBagOfHoldingProvider.openedKey);
		}
	}

	public void UpdateStack(ItemStack stack) {
		if (!stack.hasTag()) {
			stack.setTag(new CompoundNBT());
		}

		CompoundNBT stackTag = stack.getTag();
		stackTag.put(ItemBagOfHoldingProvider.handlerKey, this.serializeNBT());
	}

	public static void UpdateStackFromNbt(ItemStack stack, CompoundNBT tagCompound) {
		if (!stack.hasTag()) {
			stack.setTag(new CompoundNBT());
		}

		CompoundNBT stackTag = stack.getTag();
		stackTag.put(ItemBagOfHoldingProvider.handlerKey, tagCompound);
	}

	public static void UpdateRefreshValue(ItemStack stack) {
		CompoundNBT tagCompound = stack.getItem().getShareTag(stack);

		if (tagCompound.contains(ItemBagOfHoldingProvider.handlerKey)) {
			CompoundNBT itemsTag = tagCompound.getCompound(ItemBagOfHoldingProvider.handlerKey);

			boolean value = true;

			if (itemsTag.contains(ItemBagOfHoldingProvider.refreshValueKey)) {
				value = !itemsTag.getBoolean(ItemBagOfHoldingProvider.refreshValueKey);
			}

			itemsTag.putBoolean(ItemBagOfHoldingProvider.refreshValueKey, value);
		}
	}

	public static void AttachNewStackHandlerToStack(ItemStack stack) {
		if (!stack.hasTag() || (stack.hasTag() && stack.getChildTag(ItemBagOfHoldingProvider.handlerKey) == null)) {
			ItemBagOfHoldingProvider handler = new ItemBagOfHoldingProvider();
			CompoundNBT items = handler.serializeNBT();

			if (!stack.hasTag()) {
				stack.setTag(new CompoundNBT());
			}

			CompoundNBT stackTag = stack.getTag();
			stackTag.put(ItemBagOfHoldingProvider.handlerKey, items);
		}
	}

	public static ItemBagOfHoldingProvider GetFromStack(ItemStack stack) {
		if (stack.hasTag()) {
			CompoundNBT stackTag = stack.getChildTag(ItemBagOfHoldingProvider.handlerKey);

			if (stackTag != null) {
				ItemBagOfHoldingProvider handler = new ItemBagOfHoldingProvider();
				handler.deserializeNBT(stackTag);

				return handler;
			}
		}

		return new ItemBagOfHoldingProvider();
	}

	/**
	 * The difference between this one and the GetFromStack method is that this will return null when the provider doesn't exist.
	 * This is to check to see if it's in the player's current main hand or off-hand.
	 * @param stack
	 * @return
	 */
	public static ItemBagOfHoldingProvider HasProvider(ItemStack stack)
	{
		if (stack.hasTag()) {
			CompoundNBT stackTag = stack.getChildTag(ItemBagOfHoldingProvider.handlerKey);

			if (stackTag != null) {
				ItemBagOfHoldingProvider handler = new ItemBagOfHoldingProvider();
				handler.deserializeNBT(stackTag);

				return handler;
			}
		}

		return null;
	}
}

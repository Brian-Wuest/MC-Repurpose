package com.wuest.repurpose.Capabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemBagOfHoldingProvider extends ItemStackHandler
{
	public static final String handlerKey = "bagOfHoldingItems";
	public static final String inventoryKey = "inventory";
	public static final String refreshValueKey = "refreshValue";
	public static final String slotIndexKey = "slotIndex";
	public static final String openedKey = "opened";
		
	public boolean refreshValue;
	public int slotIndex;
	public boolean opened;
	
	public ItemBagOfHoldingProvider()
	{
		super(54);
		
		this.refreshValue = false;
		this.slotIndex = 0;
		this.opened = false;
	}
	
    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound inventoryTag = super.serializeNBT();
        NBTTagCompound returnTag = new NBTTagCompound();
        
        returnTag.setBoolean(ItemBagOfHoldingProvider.refreshValueKey, this.refreshValue);
        returnTag.setInteger(ItemBagOfHoldingProvider.slotIndexKey, this.slotIndex);
        returnTag.setTag(ItemBagOfHoldingProvider.inventoryKey, inventoryTag);
        returnTag.setBoolean(ItemBagOfHoldingProvider.openedKey, this.opened);
        
        return returnTag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
    	if (nbt.hasKey(ItemBagOfHoldingProvider.inventoryKey))
    	{
    		super.deserializeNBT(nbt.getCompoundTag(ItemBagOfHoldingProvider.inventoryKey));
    	}
    	
    	if (nbt.hasKey(ItemBagOfHoldingProvider.refreshValueKey))
    	{
    		this.refreshValue = nbt.getBoolean(ItemBagOfHoldingProvider.refreshValueKey);
    	}
    	
    	if (nbt.hasKey(ItemBagOfHoldingProvider.slotIndexKey))
    	{
    		this.slotIndex = nbt.getInteger(ItemBagOfHoldingProvider.slotIndexKey);
    	}
    	
    	if (nbt.hasKey(ItemBagOfHoldingProvider.openedKey))
    	{
    		this.opened = nbt.getBoolean(ItemBagOfHoldingProvider.openedKey);
    	}
    }
	
	public void UpdateStack(ItemStack stack)
	{
		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
		}
		
		NBTTagCompound stackTag = stack.getTagCompound();
		stackTag.setTag(ItemBagOfHoldingProvider.handlerKey, this.serializeNBT());
	}
	
	public static void UpdateStackFromNbt(ItemStack stack, NBTTagCompound tagCompound)
	{
		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
		}
		
		NBTTagCompound stackTag = stack.getTagCompound();
		stackTag.setTag(ItemBagOfHoldingProvider.handlerKey, tagCompound);
	}
	
	public static void UpdateRefreshValue(ItemStack stack)
	{
		NBTTagCompound tagCompound = stack.getItem().getNBTShareTag(stack);
		
		if (tagCompound.hasKey(ItemBagOfHoldingProvider.handlerKey))
		{
			NBTTagCompound itemsTag = tagCompound.getCompoundTag(ItemBagOfHoldingProvider.handlerKey);
			
			boolean value = true;
			
			if (itemsTag.hasKey(ItemBagOfHoldingProvider.refreshValueKey))
			{
				value = !itemsTag.getBoolean(ItemBagOfHoldingProvider.refreshValueKey);
			}
			
			itemsTag.setBoolean(ItemBagOfHoldingProvider.refreshValueKey, value);
		}
	}
	
	public static void AttachNewStackHandlerToStack(ItemStack stack)
	{
		if (!stack.hasTagCompound() || (stack.hasTagCompound() && stack.getSubCompound(ItemBagOfHoldingProvider.handlerKey) == null))
		{
			ItemBagOfHoldingProvider handler = new ItemBagOfHoldingProvider();
			NBTTagCompound items = handler.serializeNBT();
			
			if (!stack.hasTagCompound())
			{
				stack.setTagCompound(new NBTTagCompound());
			}
			
			NBTTagCompound stackTag = stack.getTagCompound();
			stackTag.setTag(ItemBagOfHoldingProvider.handlerKey, items);
		}
	}
	
	public static ItemBagOfHoldingProvider GetFromStack(ItemStack stack)
	{
		if (stack.hasTagCompound())
		{
			NBTTagCompound stackTag = stack.getSubCompound(ItemBagOfHoldingProvider.handlerKey);
			
			if (stackTag != null)
			{
				ItemBagOfHoldingProvider handler = new ItemBagOfHoldingProvider();
				handler.deserializeNBT(stackTag);
				
				return handler;
			}
		}
		
		return new ItemBagOfHoldingProvider();
	}
}

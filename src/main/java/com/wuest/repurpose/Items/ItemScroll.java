package com.wuest.repurpose.Items;

import com.wuest.repurpose.ModRegistry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;

/**
 * 
 * @author WuestMan
 *
 */
public class ItemScroll extends ItemBook
{
	public ItemScroll(String name)
	{
		this.setCreativeTab(CreativeTabs.MISC);
		ModRegistry.setItemName(this, name);
	}
	
    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    @Override
	public int getItemEnchantability()
    {
        return 22;
    }
    
    /**
     * Checks whether an item can be enchanted with a certain enchantment. This applies specifically to enchanting an item in the enchanting table and is called when retrieving the list of possible enchantments for an item.
     * Enchantments may additionally (or exclusively) be doing their own checks in {@link net.minecraft.enchantment.Enchantment#canApplyAtEnchantingTable(ItemStack)}; check the individual implementation for reference.
     * By default this will check if the enchantment type is valid for this item type.
     * @param stack the item stack to be enchanted
     * @param enchantment the enchantment to be applied
     * @return true if the enchantment can be applied to this item
     */
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.enchantment.Enchantment enchantment)
    {
        return stack.getItem() instanceof ItemBook;
    }
}
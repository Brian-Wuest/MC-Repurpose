package com.wuest.repurpose.Items;

import com.wuest.repurpose.ModRegistry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * @author WuestMan
 *
 */
public class ItemWoodenCrate extends Item
{
	/**
	 * Creates a new instance of the ItemWoodenCrateClass.
	 * @param name
	 */
	public ItemWoodenCrate(String name)
	{
		this.setCreativeTab(CreativeTabs.FOOD)
		.setHasSubtypes(true);
		
		ModRegistry.setItemName(this, name);
	}
	
	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (this.isInCreativeTab(tab))
		{
			for (CrateType type : CrateType.values())
			{
				items.add(new ItemStack(ModRegistry.WoodenCrate(), 1, type.meta));
			}
		}
	}
	
    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    @Override
	public String getUnlocalizedName(ItemStack stack)
    {
        return this.getUnlocalizedName() + "." + CrateType.getValueFromMeta(stack.getMetadata()).toString();
    }
	
    /**
     * ItemStack sensitive version of hasContainerItem
     * @param stack The current item stack
     * @return True if this item has a 'container'
     */
    @Override
    public boolean hasContainerItem(ItemStack stack)
    {
        int meta = stack.getMetadata();
        
        return meta == CrateType.Crate_Of_Beets.meta 
        		|| meta == CrateType.Crate_Of_Carrots.meta
        		|| meta == CrateType.Crate_Of_Potatoes.meta
        		|| meta == CrateType.Carton_Of_Eggs.meta;
    }
	
	/**
	 * This enum is used to identify the crate types.
	 * @author WuestMan
	 *
	 */
	public enum CrateType
	{
		Empty(0),
		Clutch_Of_Eggs(1),
		Carton_Of_Eggs(2),
		Bunch_Of_Potatoes(3),
		Crate_Of_Potatoes(4),
		Bunch_Of_Carrots(5),
		Crate_Of_Carrots(6),
		Bunch_Of_Beets(7),
		Crate_Of_Beets(8);
		
		public final int meta;
		
		CrateType(int meta)
		{
			this.meta = meta;
		}
		
		@Override
		public String toString()
		{
			return this.name().toLowerCase();
		}
		
		public static CrateType getValueFromMeta(int meta)
		{
			for (CrateType type : CrateType.values())
			{
				if (type.meta == meta)
				{
					return type;
				}
			}
			
			return CrateType.Empty;
		}
	}
}

package com.wuest.repurpose.Items;

import java.util.List;

import javax.annotation.Nullable;

import com.wuest.repurpose.ModRegistry;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class is used in the drop event for coal ore as there is a chance for this item to drop along with coal.
 * @author WuestMan
 */
public class ItemDiamondShard extends Item
{
	public ItemDiamondShard(String name)
	{
		super();
		
		this.setCreativeTab(CreativeTabs.MATERIALS);
		ModRegistry.setItemName(this, name);
	}
	
    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced)
    {
    	tooltip.add("Shards of something shiny");
    }
}
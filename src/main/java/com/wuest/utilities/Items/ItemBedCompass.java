package com.wuest.utilities.Items;

import java.time.LocalDateTime;

import com.wuest.utilities.ModRegistry;
import com.wuest.utilities.Events.ClientEventHandler;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBedCompass extends Item 
{
    /**
     * Initializes a new instance of the ItemBedCompass class.
     */
    public ItemBedCompass(String itemName)
    {
    	super();
    	
    	ModRegistry.setItemName(this, itemName);
    	this.setCreativeTab(CreativeTabs.MISC);
    }
	
    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
    	if (worldIn.isRemote)
    	{
    		// Show the location of the bed in relation to the current position
    		ClientEventHandler.bedCompassTime = LocalDateTime.now();
    	}
    	
    	return new ActionResult(EnumActionResult.PASS, playerIn.getHeldItem(hand));
    }
    
    /**
     * Returns true if this item should be rotated by 180 degrees around the Y axis when being held in an entities
     * hands.
     */
    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldRotateAroundWhenRendering()
    {
        return true;
    }
}

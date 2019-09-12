package com.wuest.repurpose.Items;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Nullable;

import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Events.ClientEventHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand)
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
    
    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced)
    {
    	super.addInformation(stack, world, tooltip, advanced);
    	
    	boolean advancedKeyDown = Minecraft.getMinecraft().currentScreen.isShiftKeyDown();
    	
    	if (!advancedKeyDown)
    	{
    		tooltip.add("Hold" + TextFormatting.BLUE + " Shift " + TextFormatting.GRAY +  "for advanced information.");
    	}
    	else
    	{
    		tooltip.add("Right-click to find out where your bed is!");
    	}
    }
}

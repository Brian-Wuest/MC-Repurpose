package wuest.utilities.Items;

import java.time.LocalDateTime;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wuest.utilities.WuestUtilities;
import wuest.utilities.Blocks.RedstoneClock;
import wuest.utilities.Events.WuestEventHandler;
import wuest.utilities.Proxy.ClientEventHandler;

public class ItemBedCompass extends Item 
{
	/**
	 * The instance of the registered item in the game.
	 */
    public static ItemBedCompass RegisteredItem;
    
    /**
     * Initializes a new instance of the ItemBedCompass class.
     */
    public ItemBedCompass()
    {
    	super();
    	
    	this.setUnlocalizedName("itemBedCompass");
    	this.setCreativeTab(CreativeTabs.tabMisc);
    }
	
    /**
     * Registers this item in the game.
     */
    public static void RegisterItem()
    {
    	ItemBedCompass.RegisteredItem = new ItemBedCompass();

		GameRegistry.registerItem(ItemBedCompass.RegisteredItem,
				"itemBedCompass");
		WuestUtilities.ModItems.add(ItemBedCompass.RegisteredItem);
		
		if (WuestUtilities.proxy.proxyConfiguration.addBedCompassRecipe)
		{
			// Register recipe.
			GameRegistry.addShapelessRecipe(new ItemStack(ItemBedCompass.RegisteredItem), 
					Items.bed, Items.compass);
		}
    }
    
    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
    	if (worldIn.isRemote)
    	{
    		// Show the location of the bed in relation to the current position
    		ClientEventHandler.bedCompassTime = LocalDateTime.now();
    	}
    	
        return itemStackIn;
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

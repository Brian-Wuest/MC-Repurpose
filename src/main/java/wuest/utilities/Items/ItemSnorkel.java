package wuest.utilities.Items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wuest.utilities.Proxy.CommonProxy;

/**
 * This item is just used to create a "Silk Touch" enchantment via the Anvil. 
 * @author WuestMan
 */
public class ItemSnorkel extends Item
{
	public static ItemSnorkel RegisteredItem;
	
	/**
	 * Initializes a new instance of the ItemFluffyFabric class.
	 */
	public ItemSnorkel(String name)
	{
		super();
		
		this.setCreativeTab(CreativeTabs.MISC);
		CommonProxy.setItemName(this, name);
	}
	
	/**
	 * Registers this item with the game registry.
	 */
	public static void RegisterItem()
	{
		ItemSnorkel.RegisteredItem = CommonProxy.registerItem(new ItemSnorkel("itemSnorkel"));
		
		GameRegistry.addShapedRecipe(
				new ItemStack(ItemSnorkel.RegisteredItem),
				"  x",
				"zzx",
				"yyx",
				'x', Items.REEDS,
				'y', Item.getItemFromBlock(Blocks.GLASS_PANE),
				'z', Items.STRING);
	}
	
    /**
     * Allow or forbid the specific book/item combination as an anvil enchant
     *
     * @param stack The item
     * @param book The book
     * @return if the enchantment is allowed
     */
    @Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book)
    {
        return false;
    }
    
    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
    	tooltip.add("Enchantment Item: A breath of fresh air where there is none");
    }
}
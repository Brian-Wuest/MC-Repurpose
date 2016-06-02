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
 * This item is just used to create the Sharpness 1 enchantment book.
 * @author WuestMan
 */
public class ItemWhetStone extends Item
{
	public static ItemWhetStone RegisteredItem;

	/**
	 * Initializes a new instance of the ItemWhetStone class.
	 * @param name the item name.
	 */
	public ItemWhetStone(String name)
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
		ItemWhetStone.RegisteredItem = CommonProxy.registerItem(new ItemWhetStone("itemWhetStone"));
		
		GameRegistry.addShapedRecipe(
				new ItemStack(ItemWhetStone.RegisteredItem),
				"xxx",
				"xyx",
				"xxx",
				'x', Items.FLINT,
				'y', Item.getItemFromBlock(Blocks.CLAY));
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
    	tooltip.add("Enchantment Item: Feels edgy!");
    }
}

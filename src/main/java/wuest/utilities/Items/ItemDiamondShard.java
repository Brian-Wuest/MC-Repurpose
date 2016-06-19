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
 * This class is used in the drop event for coal ore as there is a chance for this item to drop along with coal.
 * @author WuestMan
 */
public class ItemDiamondShard extends Item
{
	public static ItemDiamondShard RegisteredItem;
	
	public ItemDiamondShard(String name)
	{
		super();
		
		this.setCreativeTab(CreativeTabs.MATERIALS);
		CommonProxy.setItemName(this, name);
	}
	
	/**
	 * Registers this item with the game registry.
	 */
	public static void RegisterItem()
	{
		ItemDiamondShard.RegisteredItem = CommonProxy.registerItem(new ItemDiamondShard("itemDiamondShard"));
		
		GameRegistry.addShapedRecipe(
				new ItemStack(Items.DIAMOND),
				"xx",
				"xx",
				'x', ItemDiamondShard.RegisteredItem);
	}
	
    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
    	tooltip.add("Shards of something shiny");
    }
}
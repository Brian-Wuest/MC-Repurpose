package wuest.utilities.Items;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import wuest.utilities.WuestUtilities;

public final class ItemRenderRegister 
{
	/**
	 * Registers the blocks and items from the main class in the item renderer.
	 */
	public static void registerItemRenderer()
	{
		// Blocks.
		for(Block currentBlock : WuestUtilities.ModBlocks)
		{
			ItemRenderRegister.regBlock(currentBlock);
		}

		// Items.
		for (Item currentItem : WuestUtilities.ModItems)
		{
			ItemRenderRegister.regItem(currentItem);
		}
	}

	/**
	 * Registers an item to be rendered. This is needed for textures.
	 * @param item The item to register.
	 */
	public static void regItem(Item item) 
	{
		String temp = item.getUnlocalizedName().substring(5);
		ModelResourceLocation location = new ModelResourceLocation(WuestUtilities.MODID + ":" + temp, "inventory");

		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, location);
	}

	/**
	 * Registers a block to be rendered. This is needed for textures.
	 * @param block The block to register.
	 */
	public static void regBlock(Block block)
	{
		Item itemBlock = Item.getItemFromBlock(block);
		ItemRenderRegister.regItem(itemBlock);
	}
}

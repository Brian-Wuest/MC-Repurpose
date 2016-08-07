package com.wuest.utilities;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

public final class ItemRenderRegister 
{
	/**
	 * Registers the blocks and items from the main class in the item renderer.
	 */
	public static void registerItemRenderer()
	{
		// Blocks.
		for(Block currentBlock : ModRegistry.ModBlocks)
		{
			ItemRenderRegister.regBlock(currentBlock);
		}

		// Items.
		for (Item currentItem : ModRegistry.ModItems)
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
		ModelResourceLocation location = new ModelResourceLocation(temp, "inventory");
		//System.out.println("Registering Item: " + location.getResourceDomain() + "[" + location.getResourcePath() + "]");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, location);
	}

	/**
	 * Registers a block to be rendered. This is needed for textures.
	 * @param block The block to register.
	 */
	public static void regBlock(Block block)
	{
		Item itemBlock = Item.getItemFromBlock(block);
		
		if (itemBlock != null)
		{
			ItemRenderRegister.regItem(itemBlock);
		}
	}
}

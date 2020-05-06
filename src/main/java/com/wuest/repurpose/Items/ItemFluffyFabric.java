package com.wuest.repurpose.Items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

/**
 * This item is just used to create a "Silk Touch" enchantment via the Anvil.
 *
 * @author WuestMan
 */
public class ItemFluffyFabric extends Item {
	/**
	 * Initializes a new instance of the ItemFluffyFabric class.
	 */
	public ItemFluffyFabric() {
		super(new Item.Properties().group(ItemGroup.MISC));
	}

	/**
	 * Allow or forbid the specific book/item combination as an anvil enchant
	 *
	 * @param stack The item
	 * @param book  The book
	 * @return if the enchantment is allowed
	 */
	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
							   ITooltipFlag advanced) {
		super.addInformation(stack, worldIn, tooltip, advanced);

		boolean advancedKeyDown = Screen.hasShiftDown();

		if (!advancedKeyDown) {
			tooltip.add(new StringTextComponent(
					"Hold" + TextFormatting.BLUE + " Shift " + TextFormatting.GRAY + "for advanced information."));
		} else {
			tooltip.add(new StringTextComponent(
					"Enchantment Item: Use in the anvil with a book to make something special."));
		}
	}
}
package com.wuest.repurpose.Items;

import com.wuest.repurpose.ModRegistry;
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
 * This class is used in the drop event for coal ore as there is a chance for
 * this item to drop along with coal.
 *
 * @author WuestMan
 */
public class ItemDiamondShard extends Item {
	public ItemDiamondShard(String name) {
		super(new Item.Properties().group(ItemGroup.MATERIALS));

		ModRegistry.setItemName(this, name);
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
					"Found occasionally when mining coal blocks.  Maybe with a few more you could make a whole diamond..."));
		}
	}
}
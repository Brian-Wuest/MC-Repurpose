package com.wuest.repurpose.Items;

import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Capabilities.GardnersPouchProvider;
import com.wuest.repurpose.Gui.GuiCoffer;
import com.wuest.repurpose.Gui.GuiItemGardnersPouch;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * 
 * @author WuestMan
 *
 */
public class ItemGardnersPouch extends Item
{
	public ItemGardnersPouch(String name)
	{
		setCreativeTab(CreativeTabs.TOOLS);
		this.setMaxStackSize(1);
		ModRegistry.setItemName(this, name);
	}

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		player.openGui(Repurpose.instance,
				GuiItemGardnersPouch.GUI_ID, world, 
				player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
		
		return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItemMainhand());
	}

	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{
		// inventory.serializeNBT();
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack item, 	NBTTagCompound nbt)
	{
		if (item.getItem() == ModRegistry.GardnersPounch())
		{
			return new GardnersPouchProvider();
		}
		
		return null;
	}
}
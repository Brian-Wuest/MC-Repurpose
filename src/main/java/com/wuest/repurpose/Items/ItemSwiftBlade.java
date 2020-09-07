package com.wuest.repurpose.Items;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SwordItem;

/**
 * This class is used to create a sword which has the same speed as pre-1.9
 * swords.
 *
 * @author WuestMan
 */
public class ItemSwiftBlade extends SwordItem {
	/*
	 * Initializes a new instance of the ItemSwiftBlade class.
	 */
	public ItemSwiftBlade(IItemTier tier, int attackDamageIn, float attackSpeedIn) {
		super(tier, attackDamageIn, attackSpeedIn,
				new Item.Properties().maxStackSize(1).maxDamage(tier.getMaxUses()).group(ItemGroup.COMBAT));
	}

	/*
	 * Gets the unlocalized name for the specified material.
	 */
	public static String GetUnlocalizedName(String name) {
		return "item_swift_blade_" + name;
	}

	/**
	 * Returns the amount of damage this item will deal. One heart of damage is
	 * equal to 2 damage points.
	 */
	public float getDamageVsEntity() {
		return this.getTier().getAttackDamage();
	}

	/**
	 * Return the name for this tool's material.
	 */
	public String getToolMaterialName() {
		return this.getTier().toString();
	}

	/**
	 * Return the enchantability factor of the item, most of the time is based on
	 * material.
	 */
	@Override
	public int getItemEnchantability() {
		return this.getTier().getEnchantability();
	}

}

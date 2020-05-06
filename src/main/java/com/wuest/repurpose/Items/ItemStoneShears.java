package com.wuest.repurpose.Items;

import net.minecraft.item.Item;
import net.minecraft.item.ShearsItem;

import java.util.UUID;

/**
 * @author WuestMan
 */
public class ItemStoneShears extends ShearsItem {
	/**
	 * Initializes a new instance of the ItemStoneShears class.
	 */
	public ItemStoneShears() {
		super(new Item.Properties().maxDamage(124));
	}

	public static final UUID getAttackSpeedID() {
		return Item.ATTACK_SPEED_MODIFIER;
	}

	public static final UUID getAttackDamageID() {
		return Item.ATTACK_DAMAGE_MODIFIER;
	}
}

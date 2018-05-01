package com.wuest.repurpose.Items;

import java.util.UUID;

import com.wuest.repurpose.ModRegistry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;

/**
 * 
 * @author WuestMan
 *
 */
public class ItemStoneShears extends ItemShears
{
	/**
	 * Initializes a new instance of the ItemStoneShears class.
	 * @param name The name of the item in the registry.
	 */
	public ItemStoneShears(String name)
	{
		super();
		this.setMaxDamage(124);
		ModRegistry.setItemName(this, name);
	}
	
	public static final UUID getAttackSpeedID()
	{
		return Item.ATTACK_SPEED_MODIFIER;
	}
	
	public static final UUID getAttackDamageID()
	{
		return Item.ATTACK_DAMAGE_MODIFIER;
	}
}

package com.wuest.repurpose.Items;

import com.google.common.collect.Multimap;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
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
	protected final float attackDamage;
	protected final IItemTier material;
	protected final float attackSpeed;

	/*
	 * Initializes a new instance of the ItemSwiftBlade class.
	 */
	public ItemSwiftBlade(IItemTier tier, int attackDamageIn, float attackSpeedIn) {
		super(tier, attackDamageIn, attackSpeedIn,
				new Item.Properties().maxStackSize(1).maxDamage(tier.getMaxUses()).group(ItemGroup.COMBAT));
		this.material = tier;
		this.attackDamage = 3.0F + material.getAttackDamage();
		this.attackSpeed = attackSpeedIn;
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
		return this.material.getAttackDamage();
	}

	/**
	 * Return the name for this tool's material.
	 */
	public String getToolMaterialName() {
		return this.material.toString();
	}

	@Override
	public IItemTier getTier() {
		return this.material;
	}

	/**
	 * Return the enchantability factor of the item, most of the time is based on
	 * material.
	 */
	@Override
	public int getItemEnchantability() {
		return this.material.getEnchantability();
	}

	/**
	 * Gets a map of item attribute modifiers, used by ItemSword to increase hit
	 * damage.
	 */
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot);
		if (equipmentSlot == EquipmentSlotType.MAINHAND) {
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER,
					"Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER,
					"Weapon modifier", this.attackSpeed, AttributeModifier.Operation.ADDITION));
		}

		return multimap;
	}
}

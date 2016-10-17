package com.wuest.utilities.Enchantment;

import com.wuest.utilities.WuestUtilities;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

/**
 * This enchantment is used to change the step height of a player.
 * @author WuestMan
 *
 */
public class EnchantmentStepAssist extends Enchantment
{

	public EnchantmentStepAssist(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots)
	{
		super(rarityIn, typeIn, slots);
		
		this.setName("step_assist");
		this.setRegistryName(WuestUtilities.MODID, "step_assist");
	}
	
	@Override
	public int getMaxLevel()
	{
		return 3;
	}
	
    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    @Override
	public int getMinEnchantability(int enchantmentLevel)
    {
        return 5 + (enchantmentLevel - 1) * 7;
    }
    
    /**
     * Returns the maximum value of enchantability nedded on the enchantment level passed.
     */
    @Override
    public int getMaxEnchantability(int enchantmentLevel)
    {
        return this.getMinEnchantability(enchantmentLevel) + 20;
    }

}

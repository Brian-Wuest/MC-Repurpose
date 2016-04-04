package wuest.utilities.Enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLootBonus;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

public class EnchantmentLooting extends EnchantmentLootBonus
{
	/**
	 * Initializes a new instance of the EnchantmentLooting class.
	 * @param rarityIn The rarity of the enchantment.
	 * @param typeIn The Enchantment type.
	 * @param slots The equipment slots for the enchantment.
	 */
	public EnchantmentLooting(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots)
	{
		super(rarityIn, typeIn, slots);
		
		this.setName("bow_looting");
	}
	
	/**
	 * Registers this enchantment.
	 */
	public static void RegisterEnchantment()
	{
		EnchantmentLooting looting = new EnchantmentLooting(Enchantment.Rarity.RARE, EnumEnchantmentType.BOW, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
		
		// Start at 71 as that is the first vanilla open enchantment. If other mods add enchantments, keep going until an open slot is found.
		int registryID = 71;
		
		while (Enchantment.enchantmentRegistry.getObjectById(registryID) != null)
		{
			registryID++;
		}
		
		Enchantment.enchantmentRegistry.register(registryID, new ResourceLocation("bow_looting"), looting);
	}
}

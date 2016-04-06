package wuest.utilities.Enchantment;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLootBonus;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Enchantments;
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

		EnchantmentLootBonus test = (EnchantmentLootBonus) super.setName("bow_looting");
		this.setName("bow_looting");
	}

	public static EnchantmentLootBonus GetBase(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots)
	{
		try
		{
			Class<?> clazz = Class.forName("net.minecraft.enchantment.EnchantmentLootBonus");

			for (Constructor<?> c : clazz.getDeclaredConstructors()) 
			{
				// Make sure to set the constructor to accessible.
				c.setAccessible(true);

				return (EnchantmentLootBonus) c.newInstance(rarityIn, typeIn, slots);
			}
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new EnchantmentLooting(rarityIn, typeIn, slots);
	}

	/**
	 * Registers this enchantment.
	 */
	public static void RegisterEnchantment()
	{
		// This currently doesn't work unless forge addresses this.
		EnchantmentLootBonus lootBase = EnchantmentLooting.GetBase(Enchantment.Rarity.RARE, EnumEnchantmentType.BOW, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
		lootBase.setName("bow_looting");
		
		Enchantment.enchantmentRegistry.register(85, new ResourceLocation("wuestUtilities:bow_looting"), lootBase);
	}
}

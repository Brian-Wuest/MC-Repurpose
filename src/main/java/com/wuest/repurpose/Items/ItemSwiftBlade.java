package com.wuest.repurpose.Items;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.wuest.repurpose.ModRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class is used to create a sword which has the same speed as pre-1.9 swords.
 * 
 * @author WuestMan
 *
 */
public class ItemSwiftBlade extends ItemSword
{
	protected final float attackDamage;
	protected final Item.ToolMaterial material;

	/*
	 * Initializes a new instance of the ItemSwiftBlade class.
	 */
	public ItemSwiftBlade(ToolMaterial material)
	{
		super(material);
		this.material = material;
		this.maxStackSize = 1;
		this.setMaxDamage(material.getMaxUses());
		this.setCreativeTab(CreativeTabs.COMBAT);
		this.attackDamage = 3.0F + material.getAttackDamage();

		ModRegistry.setItemName(this, ItemSwiftBlade.GetUnlocalizedName(material));
	}

	/*
	 * Gets the unlocalized name for the specified material.
	 */
	public static String GetUnlocalizedName(ToolMaterial material)
	{
		String itemName = material.name().toLowerCase();

		return "item_swift_blade_" + itemName;
	}

	/**
	 * Returns the amount of damage this item will deal. One heart of damage is equal to 2 damage points.
	 */
	public float getDamageVsEntity()
	{
		return this.material.getAttackDamage();
	}

	/**
	 * Return the name for this tool's material.
	 */
	public String getToolMaterialName()
	{
		return this.material.toString();
	}

	public ToolMaterial getToolMaterial()
	{
		return this.material;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state)
	{
		Block block = state.getBlock();

		if (block == Blocks.WEB)
		{
			return 15.0F;
		}
		else
		{
			Material material = state.getMaterial();
			return material != Material.PLANTS && material != Material.VINE && material != Material.CORAL
				&& material != Material.LEAVES && material != Material.GOURD ? 1.0F : 1.5F;
		}
	}

	/**
	 * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
	 * the damage on the stack.
	 */
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		stack.damageItem(1, attacker);
		return true;
	}

	/**
	 * Called when a Block is destroyed using this Item. Return true to trigger the "Use Item" statistic.
	 */
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos,
		EntityLivingBase entityLiving)
	{
		if ((double) blockIn.getBlockHardness(worldIn, pos) != 0.0D)
		{
			stack.damageItem(2, entityLiving);
		}

		return true;
	}

	/**
	 * Check whether this Item can harvest the given Block
	 */
	@Override
	public boolean canHarvestBlock(IBlockState blockIn)
	{
		return blockIn.getBlock() == Blocks.WEB;
	}

	/**
	 * Returns True is the item is renderer in full 3D when hold.
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public boolean isFull3D()
	{
		return true;
	}

	/**
	 * Return the enchantability factor of the item, most of the time is based on material.
	 */
	@Override
	public int getItemEnchantability()
	{
		return this.material.getEnchantability();
	}

	/**
	 * Return whether this item is reparable in an anvil.
	 */
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		ItemStack mat = this.material.getRepairItemStack();
		
		if (mat != null && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false))
		{
			return true;
		}
		
		return super.getIsRepairable(toRepair, repair);
	}

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
	{
		Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier> create();

		if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
		{
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
				new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double) this.attackDamage, 0));
			
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
				new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", 6, 0));
		}

		return multimap;
	}
}

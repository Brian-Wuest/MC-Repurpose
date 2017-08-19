package com.wuest.repurpose.Items;

import com.google.common.collect.Multimap;
import com.wuest.repurpose.ModRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author WuestMan
 *
 */
public class ItemSickle extends Item
{
	private final float attackDamage;
    protected Item.ToolMaterial toolMaterial;
    protected int breakRadius = 0;

    public ItemSickle(Item.ToolMaterial material, String name)
    {
        this.toolMaterial = material;
        this.maxStackSize = 1;
        this.setMaxDamage(material.getMaxUses());
        this.setCreativeTab(CreativeTabs.TOOLS);
        this.attackDamage = 1.0F + material.getDamageVsEntity();
        this.breakRadius = 2 + material.getHarvestLevel();
        ModRegistry.setItemName(this, name);
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
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
    {
        if ((double)state.getBlockHardness(worldIn, pos) != 0.0D)
        {
            stack.damageItem(2, entityLiving);
        }
        else if (state.getBlock() instanceof BlockBush && !worldIn.isRemote && entityLiving instanceof EntityPlayer)
        {
        	BlockPos corner1 = pos.north(this.breakRadius).east(this.breakRadius);
        	BlockPos corner2 = pos.south(this.breakRadius).west(this.breakRadius);
        	EntityPlayer player = (EntityPlayer)entityLiving;
        	
        	for (BlockPos currentPos : BlockPos.getAllInBox(corner1, corner2))
        	{
        		IBlockState currentState = worldIn.getBlockState(currentPos);
        		
        		if (currentState != null && currentState.getBlock() instanceof BlockBush)
        		{
        			boolean canHarvest = currentState.getBlock().canHarvestBlock(worldIn, currentPos, player);
        			canHarvest = currentState.getBlock().removedByPlayer(currentState, worldIn, currentPos, player, canHarvest);
        			
        			if (canHarvest)
        			{
        				worldIn.playEvent(2001, currentPos, Block.getStateId(currentState));
	        			currentState.getBlock().onBlockDestroyedByPlayer(worldIn, currentPos, currentState);
	        			currentState.getBlock().breakBlock(worldIn, currentPos, currentState);
	        			
	        			int fortune = this.toolMaterial == ToolMaterial.GOLD ? 1 : 0;
	        			
	        			currentState.getBlock().dropBlockAsItem(worldIn, currentPos, currentState, fortune);
        			}
        		}
        	}
        }

        return true;
    }
    
    /**
     * Gets a map of item attribute modifiers, used by ItemSword to increase hit damage.
     */
    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.attackDamage, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
        }

        return multimap;
    }
}

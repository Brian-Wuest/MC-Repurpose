package com.wuest.repurpose.Items;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;
import com.wuest.repurpose.ModRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * @author WuestMan
 *
 */
public class ItemSickle extends ItemTool
{
	public static Set<Block> effectiveBlocks = Sets.newHashSet(
			Blocks.LEAVES, 
			Blocks.LEAVES2, 
			Blocks.TALLGRASS, 
			Blocks.DEADBUSH, 
			Blocks.RED_FLOWER, 
			Blocks.YELLOW_FLOWER, 
			Blocks.DOUBLE_PLANT);

    protected int breakRadius = 0;

    /**
     * Initializes a new instance of the ItemSickle class.
     * @param material The type of tool material.
     * @param name The name to register.
     */
    public ItemSickle(Item.ToolMaterial material, String name)
    {
    	super(1.0f, -2.4000000953674316f, material, effectiveBlocks);
        this.breakRadius = 1 + material.getHarvestLevel();

        ModRegistry.setItemName(this, name);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
        Block block = state.getBlock();

        if (block != Blocks.WEB && state.getMaterial() != Material.LEAVES)
        {
            return super.getDestroySpeed(stack, state);
        }
        else
        {
            return 15.0F;
        }
    }

    /**
     * Called when a Block is destroyed using this Item. Return true to trigger the "Use Item" statistic.
     */
    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
    {
    	if (!worldIn.isRemote)
    	{
	    	stack.damageItem(1, entityLiving);
	    	
	        if ((double)state.getBlockHardness(worldIn, pos) != 0.0D
	        		&& !(state.getBlock() instanceof BlockLeaves))
	        {
	            stack.damageItem(1, entityLiving);
	        }
	        else if ((state.getBlock() instanceof BlockBush
	        		|| state.getBlock() instanceof BlockLeaves) 
	        		&& entityLiving instanceof EntityPlayer)
	        {
	        	BlockPos corner1 = pos.north(this.breakRadius).east(this.breakRadius).up(this.breakRadius);
	        	BlockPos corner2 = pos.south(this.breakRadius).west(this.breakRadius).down(this.breakRadius);
	        	EntityPlayer player = (EntityPlayer)entityLiving;
	        	
	        	for (BlockPos currentPos : BlockPos.getAllInBox(corner1, corner2))
	        	{
	        		IBlockState currentState = worldIn.getBlockState(currentPos);
	        		
	        		if (currentState != null 
	        				&& (currentState.getBlock() instanceof BlockBush)
	        				|| (currentState.getBlock() instanceof BlockLeaves))
	        		{
	        			boolean canHarvest = currentState.getBlock().canHarvestBlock(worldIn, currentPos, player);
	        			canHarvest = currentState.getBlock().removedByPlayer(currentState, worldIn, currentPos, player, canHarvest);
	        			
	        			if (canHarvest)
	        			{
	        				worldIn.playEvent(2001, currentPos, Block.getStateId(currentState));
		        			currentState.getBlock().onBlockDestroyedByPlayer(worldIn, currentPos, currentState);
		        			currentState.getBlock().breakBlock(worldIn, currentPos, currentState);
		        			
		        			int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack) + (this.toolMaterial == ToolMaterial.GOLD ? 1 : 0);
		        			currentState.getBlock().dropBlockAsItem(worldIn, currentPos, currentState, fortune);
	        			}
	        		}
	        	}
	        }
    	}

        return true;
    }
    
    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced)
    {
    	super.addInformation(stack, world, tooltip, advanced);
    	
    	boolean advancedKeyDown = Minecraft.getMinecraft().currentScreen.isShiftKeyDown();
    	
    	if (!advancedKeyDown)
    	{
    		tooltip.add("Hold" + TextFormatting.BLUE + " Shift " + TextFormatting.GRAY +  "for advanced information.");
    	}
    	else
    	{
    		tooltip.add("Cut grass, flowers and leaves down with ease!");
    	}
    }
}

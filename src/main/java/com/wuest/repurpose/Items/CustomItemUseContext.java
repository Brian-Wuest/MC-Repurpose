package com.wuest.repurpose.Items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Custom implementation of the item use context so protected fields and constructors can be used.
 */
public class CustomItemUseContext extends ItemUseContext {
	public CustomItemUseContext(PlayerEntity player, Hand handIn, BlockRayTraceResult rayTraceResultIn) {
		this(player.world, player, handIn, player.getHeldItem(handIn), rayTraceResultIn);
	}

	public CustomItemUseContext(World worldIn, @Nullable PlayerEntity player, Hand handIn, ItemStack heldItem, BlockRayTraceResult rayTraceResultIn) {
		super(worldIn, player, handIn, heldItem, rayTraceResultIn);
	}
}

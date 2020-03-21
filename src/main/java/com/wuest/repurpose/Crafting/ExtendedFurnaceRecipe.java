package com.wuest.repurpose.Crafting;

import com.wuest.repurpose.ModRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class ExtendedFurnaceRecipe extends FurnaceRecipe {
	public int resultCount = 1;

	public ExtendedFurnaceRecipe(ResourceLocation idIn, String groupIn, Ingredient ingredientIn, ItemStack resultIn, float experienceIn, int cookTimeIn, int resultCount) {
		super(idIn, groupIn, ingredientIn, resultIn, experienceIn, cookTimeIn);

		this.resultCount = resultCount;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRegistry.ExtendedSmelting;
	}
}

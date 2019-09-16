package com.wuest.repurpose.Crafting;

import com.wuest.repurpose.Repurpose;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;

/**
 * 
 * @author WuestMan
 *
 */
public class RecipeCondition implements ICondition {
	public static final ResourceLocation NAME = new ResourceLocation(Repurpose.MODID, "config_recipe");
	protected String recipeKeyName = "recipeKey";
	public String recipeKey;

	/**
	 * Initializes a new instance of the recipe condition class.
	 */
	public RecipeCondition(String recipeKey) {
		this.recipeKey = recipeKey;
	}

	@Override
	public ResourceLocation getID() {
		return NAME;
	}

	@Override
	public boolean test() {
		return this.determineActiveRecipe();
	}

	/**
	 * Determines if the recipe is active.
	 * 
	 * @return True if the recipe is active, otherwise false.
	 */
	public boolean determineActiveRecipe() {
		boolean result = false;

		if (this.recipeKey != null) {
			if (Repurpose.proxy.getServerConfiguration().recipeConfiguration.containsKey(this.recipeKey)) {
				result = Repurpose.proxy.getServerConfiguration().recipeConfiguration.get(this.recipeKey);
			}
		}

		return result;
	}

}

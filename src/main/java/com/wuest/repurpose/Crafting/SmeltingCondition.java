package com.wuest.repurpose.Crafting;

import com.google.gson.JsonObject;
import com.wuest.repurpose.Repurpose;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

import java.util.HashMap;

/**
 * 
 * @author WuestMan
 *
 */
public class SmeltingCondition implements ICondition {
	public static final ResourceLocation NAME = new ResourceLocation(Repurpose.MODID, "smelting_recipe");
	public String identifier;

	/**
	 * Initializes a new instance of the smelting condition class.
	 */
	public SmeltingCondition(String identifier) {
		this.identifier = identifier;
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

		if (this.identifier != null) {
			if (this.identifier.equals("flesh_to_leather")) {
				HashMap<String, Boolean> recipeConfiguration = Repurpose.proxy.getServerConfiguration().recipeConfiguration;

				if (recipeConfiguration
						.get(Repurpose.proxy.getServerConfiguration().addMiscRecipesName)) {

					result = true;
				}
			}
		}

		return result;
	}

	@SuppressWarnings("unused")
	public static class Serializer implements IConditionSerializer<SmeltingCondition> {
		public static final SmeltingCondition.Serializer INSTANCE = new SmeltingCondition.Serializer();

		@Override
		public void write(JsonObject json, SmeltingCondition value) {
			json.addProperty("recipeKey", value.identifier);
		}

		@Override
		public SmeltingCondition read(JsonObject json) {
			String recipeKeyName = "recipeKey";

			return new SmeltingCondition(json.get(recipeKeyName).getAsString());
		}

		@Override
		public ResourceLocation getID() {
			return SmeltingCondition.NAME;
		}
	}
}
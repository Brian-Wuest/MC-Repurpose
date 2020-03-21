package com.wuest.repurpose.Crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class ExtendedCookingRecipeSerializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ExtendedFurnaceRecipe> {
	private final int cookingTime;
	protected final int resultCount;

	public ExtendedCookingRecipeSerializer(int p_i50025_2_) {
		this.cookingTime = p_i50025_2_;
		this.resultCount = 1;
	}

	@Override
	public ExtendedFurnaceRecipe read(ResourceLocation recipeId, JsonObject json) {
		String groupName = JSONUtils.getString(json, "group", "");
		JsonElement jsonelement = JSONUtils.isJsonArray(json, "ingredient") ? JSONUtils.getJsonArray(json, "ingredient") : JSONUtils.getJsonObject(json, "ingredient");
		Ingredient ingredient = Ingredient.deserialize(jsonelement);

		//Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
		if (!json.has("result")) {
			throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
		}

		ItemStack itemstack;

		if (json.get("result").isJsonObject()) {
			itemstack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
		} else {
			String s1 = JSONUtils.getString(json, "result");
			ResourceLocation resourcelocation = new ResourceLocation(s1);
			itemstack = new ItemStack(Registry.ITEM.getValue(resourcelocation).orElseThrow(() -> {
				return new IllegalStateException("Item: " + s1 + " does not exist");
			}));
		}

		float experience = JSONUtils.getFloat(json, "experience", 0.0F);
		int cookingTime = JSONUtils.getInt(json, "cookingtime", this.cookingTime);
		int resultCount = JSONUtils.getInt(json, "resultcount", this.resultCount);

		if (resultCount > 1) {
			itemstack.setCount(resultCount);
		}

		return new ExtendedFurnaceRecipe(recipeId, groupName, ingredient, itemstack, experience, cookingTime, resultCount);
	}

	@Override
	public ExtendedFurnaceRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
		String groupName = buffer.readString(32767);
		Ingredient ingredient = Ingredient.read(buffer);
		ItemStack itemstack = buffer.readItemStack();
		float experience = buffer.readFloat();
		int cookingTime = buffer.readVarInt();
		int resultCount = buffer.readVarInt();

		return new ExtendedFurnaceRecipe(recipeId, groupName, ingredient, itemstack, experience, cookingTime, resultCount);
	}

	@Override
	public void write(PacketBuffer buffer, ExtendedFurnaceRecipe recipe) {
		buffer.writeString(recipe.getGroup());
		recipe.getIngredients().get(0).write(buffer);
		buffer.writeItemStack(recipe.getRecipeOutput());
		buffer.writeFloat(recipe.getExperience());
		buffer.writeVarInt(recipe.getCookTime());
		buffer.writeVarInt(recipe.resultCount);
	}
}

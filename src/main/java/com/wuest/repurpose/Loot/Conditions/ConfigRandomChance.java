package com.wuest.repurpose.Loot.Conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.wuest.repurpose.Repurpose;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

public class ConfigRandomChance implements ILootCondition {
	private final int chance;
	private final String configOptionName;

	private ConfigRandomChance(int chanceIn, String configOptionName) {
		this.chance = chanceIn;
		this.configOptionName = configOptionName;
	}

	public static ILootCondition.IBuilder builder(int chance, String configOptionName) {
		return () -> {
			return new ConfigRandomChance(chance, configOptionName);
		};
	}

	public boolean test(LootContext context) {
		CompoundNBT nbt = Repurpose.proxy.getServerConfiguration().ToNBTTagCompound();

		int chanceValue = this.chance;
		if (this.chance < 0 && nbt.contains(this.configOptionName)) {
			// The chance wasn't filled in (it overrides ALL settings) and this option exists in the configuration.
			chanceValue = nbt.getInt(this.configOptionName);
		}

		int randomResult = context.getRandom().nextInt(101);

		return randomResult < chanceValue && randomResult > 0;
	}

	public static class Serializer extends ILootCondition.AbstractSerializer<ConfigRandomChance> {
		public Serializer() {
			super(new ResourceLocation(Repurpose.MODID, "config_random_chance"), ConfigRandomChance.class);
		}

		public void serialize(JsonObject json, ConfigRandomChance value, JsonSerializationContext context) {
			json.addProperty("chance", value.chance);
			json.addProperty("option_name", value.configOptionName);
		}

		public ConfigRandomChance deserialize(JsonObject json, JsonDeserializationContext context) {
			return new ConfigRandomChance(JSONUtils.getInt(json, "chance"), JSONUtils.getString(json, "option_name"));
		}
	}
}
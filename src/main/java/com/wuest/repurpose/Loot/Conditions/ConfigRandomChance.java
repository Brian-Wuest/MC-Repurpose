package com.wuest.repurpose.Loot.Conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.wuest.repurpose.Repurpose;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class ConfigRandomChance implements ILootCondition {
	public static LootConditionType lootConditionType = ConfigRandomChance.register("config_random_chance", new ConfigRandomChance.Serializer());
	private final int chance;
	private final String configOptionName;

	private ConfigRandomChance(int chanceIn, String configOptionName) {
		this.chance = chanceIn;
		this.configOptionName = configOptionName;
	}

	public static ILootCondition.IBuilder builder(int chance, String configOptionName) {
		return () -> new ConfigRandomChance(chance, configOptionName);
	}

	private static LootConditionType register(String registryNameIn, ILootSerializer<? extends ILootCondition> serializerIn) {
		return Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(Repurpose.MODID, registryNameIn), new LootConditionType(serializerIn));
	}

	public LootConditionType func_230419_b_()
	{
		return ConfigRandomChance.lootConditionType;
	}

	public boolean test(LootContext context) {
		CompoundNBT nbt = Repurpose.proxy.getServerConfiguration().ToNBTTagCompound();

		int chanceValue = this.chance;
		if (this.chance < 0 && nbt.contains(this.configOptionName)) {
			// The chance wasn't filled in (it overrides ALL settings) and this option exists in the configuration.
			chanceValue = nbt.getInt(this.configOptionName);
		}

		int randomResult = context.getRandom().nextInt(101);

		return randomResult <= chanceValue && chanceValue > 0;
	}

	public static class Serializer implements ILootSerializer<ConfigRandomChance> {
		public Serializer() {
			//super(new ResourceLocation(Repurpose.MODID, "config_random_chance"), ConfigRandomChance.class);
		}

		@Override
		public void serialize(JsonObject json, ConfigRandomChance value, JsonSerializationContext context) {
			json.addProperty("chance", value.chance);
			json.addProperty("option_name", value.configOptionName);
		}

		@Override
		public ConfigRandomChance deserialize(JsonObject json, JsonDeserializationContext context) {
			return new ConfigRandomChance(JSONUtils.getInt(json, "chance"), JSONUtils.getString(json, "option_name"));
		}
	}
}
package com.wuest.repurpose.Crafting;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.wuest.repurpose.Repurpose;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

/**
 * 
 * @author WuestMan
 *
 */
public class GrassSeedCondition implements ILootCondition {
	public static final ResourceLocation NAME = new ResourceLocation(Repurpose.MODID, "grass_seed");

	/**
	 * Initializes a new instance of the smelting condition class.
	 */
	public GrassSeedCondition() {
	}

	@Override
	public boolean test(LootContext context) {
		return Repurpose.proxy.getServerConfiguration().enableExtraGrassDrops;
	}

	@SuppressWarnings("unused")
	public static class Serializer extends ILootCondition.AbstractSerializer<GrassSeedCondition> {
		public static final GrassSeedCondition.Serializer INSTANCE = new GrassSeedCondition.Serializer();

		public Serializer() {
			super(GrassSeedCondition.NAME, GrassSeedCondition.class);
		}

		public void serialize(JsonObject json, GrassSeedCondition value, JsonSerializationContext context) {
		}

		public GrassSeedCondition deserialize(JsonObject json, JsonDeserializationContext context) {
			return new GrassSeedCondition();
		}
	}
}
package com.wuest.repurpose.Items;

import com.wuest.repurpose.ModRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

/**
 * @author WuestMan
 */
public class ItemWoodenCrate extends Item {
	public final CrateType crateType;

	/**
	 * Creates a new instance of the ItemWoodenCrateClass.
	 */
	public ItemWoodenCrate(CrateType crateType) {
		super(new Item.Properties().group(ItemGroup.FOOD).containerItem(crateType == CrateType.Empty ? null : ModRegistry.WoodenCrate.get()));

		this.crateType = crateType;
	}

	/**
	 * ItemStack sensitive version of hasContainerItem
	 *
	 * @param stack The current item stack
	 * @return True if this item has a 'container'
	 */
	@Override
	public boolean hasContainerItem(ItemStack stack) {
		ItemWoodenCrate meta = (ItemWoodenCrate) stack.getItem();

		return meta.crateType == CrateType.Crate_Of_Beets || meta.crateType == CrateType.Crate_Of_Carrots
				|| meta.crateType == CrateType.Crate_Of_Potatoes || meta.crateType == CrateType.Carton_Of_Eggs;
	}

	/**
	 * This enum is used to identify the crate types.
	 *
	 * @author WuestMan
	 */
	public enum CrateType {
		Empty(0), Clutch_Of_Eggs(1), Carton_Of_Eggs(2), Bunch_Of_Potatoes(3), Crate_Of_Potatoes(4), Bunch_Of_Carrots(5),
		Crate_Of_Carrots(6), Bunch_Of_Beets(7), Crate_Of_Beets(8);

		public final int meta;

		CrateType(int meta) {
			this.meta = meta;
		}

		@Override
		public String toString() {
			return this.name().toLowerCase();
		}

		public static CrateType getValueFromMeta(int meta) {
			for (CrateType type : CrateType.values()) {
				if (type.meta == meta) {
					return type;
				}
			}

			return CrateType.Empty;
		}
	}
}

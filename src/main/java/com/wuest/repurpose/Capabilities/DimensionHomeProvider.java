package com.wuest.repurpose.Capabilities;

import com.wuest.repurpose.ModRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

/**
 * The capability provider for the DimensionHome capability. This class MUST
 * implement INBTSerializable in order for the capability to be saved when the
 * world is saved.
 *
 * @author WuestMan
 */
public class DimensionHomeProvider implements ICapabilitySerializable<CompoundNBT> {
	private IDimensionHome dimensionHome;

	/**
	 * Initializes a new instance of the DimensionHomeProvider class.
	 *
	 * @param dimensionHome The capability associated with the entity.
	 */
	public DimensionHomeProvider(IDimensionHome dimensionHome) {
		this.dimensionHome = dimensionHome;
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
		return ModRegistry.DimensionHomes.orEmpty(capability, LazyOptional.of(this::getDimensionHome));
	}

	@Override
	public CompoundNBT serializeNBT() {
		return (CompoundNBT) ModRegistry.DimensionHomes.getStorage().writeNBT(ModRegistry.DimensionHomes,
				this.dimensionHome, null);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		ModRegistry.DimensionHomes.getStorage().readNBT(ModRegistry.DimensionHomes, this.dimensionHome, null, nbt);
	}

	private IDimensionHome getDimensionHome() {
		return this.dimensionHome;
	}
}
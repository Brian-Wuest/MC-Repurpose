package com.wuest.repurpose.Base;

import com.wuest.repurpose.Capabilities.ITransferable;
import com.wuest.repurpose.Repurpose;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import org.apache.logging.log4j.Level;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * This is the base tile entity used by the mod.
 *
 * @param <T> The base configuration used by this tile entity.
 * @author WuestMan
 */
public abstract class TileEntityBase<T extends BaseConfig> extends TileEntity {
	protected T config;
	protected ArrayList<Capability> allowedCapabilities;

	protected TileEntityBase(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	/**
	 * @return Gets the configuration class used by this tile entity.
	 */
	public T getConfig() {
		return this.config;
	}

	/**
	 * Sets the configuration class used by this tile entity.
	 *
	 * @param value The updated tile entity.
	 */
	public void setConfig(T value) {
		this.config = value;
		this.markDirty();
	}

	/**
	 * Gets the list of allowed capabilities.
	 *
	 * @return The list of allowed capabilities if any.
	 */
	public ArrayList<Capability> getAllowedCapabilities() {
		if (this.allowedCapabilities == null) {
			this.allowedCapabilities = new ArrayList<Capability>();

			this.addAllowedCapabilities();
		}

		return this.allowedCapabilities;
	}

	/**
	 * Sets the allowed capabilities for this TileEntity.
	 *
	 * @param allowedCapabilities The list of allowed capabilities.
	 */
	public void setAllowedCapabilities(ArrayList<Capability> allowedCapabilities) {
		this.allowedCapabilities = allowedCapabilities;
		this.markDirty();
	}

	/**
	 * Transfers capabilities available for transferring to the supplied itemstack.
	 *
	 * @return
	 */
	public ItemStack transferCapabilities(ItemStack stack) {
		// Transfer each transferable capability to the itemstack.
		for (Capability allowedCapability : this.getAllowedCapabilities()) {
			// Get the interfaces for this capability.
			Object stackCapability = stack.getCapability(allowedCapability, Direction.NORTH);
			Object tileEntityCapability = this.getCapability(allowedCapability, Direction.NORTH);

			if (stackCapability != null && tileEntityCapability != null && stackCapability instanceof ITransferable
					&& tileEntityCapability instanceof ITransferable) {
				// transfer the capability data, it's up to the capability to transfer the data.
				((ITransferable) stackCapability).Transfer(tileEntityCapability);
			}
		}

		return stack;
	}

	public Class<T> getTypeParameterClass() {
		Type type = getClass().getGenericSuperclass();
		ParameterizedType paramType = (ParameterizedType) type;
		return (Class<T>) paramType.getActualTypeArguments()[0];
	}

	/**
	 * Allows for a specialized description packet to be created. This is often used
	 * to sync tile entity data from the server to the client easily. For example
	 * this is used by signs to synchronize the text to be displayed.
	 */
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		// Don't send the packet until the position has been set.
		if (this.pos.getX() == 0 && this.pos.getY() == 0 && this.pos.getZ() == 0) {
			return super.getUpdatePacket();
		}

		CompoundNBT tag = new CompoundNBT();
		this.write(tag);

		return new SUpdateTileEntityPacket(this.getPos(), 1, tag);
	}

	/**
	 * Called when you receive a TileEntityData packet for the location this
	 * TileEntity is currently in. On the client, the NetworkManager will always be
	 * the remote server. On the server, it will be whomever is responsible for
	 * sending the packet.
	 *
	 * @param net The NetworkManager the packet originated from
	 * @param pkt The data packet
	 */
	@Override
	public void onDataPacket(net.minecraft.network.NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.read(this.getBlockState(), pkt.getNbtCompound());
	}

	@Override
	public boolean receiveClientEvent(int id, int type) {
		return true;
	}

	@Override
	public void updateContainingBlockInfo() {
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.write(new CompoundNBT());
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);

		if (this.config != null) {
			this.config.WriteToNBTCompound(compound);
		}

		return compound;
	}

	@Override
	public void read(BlockState stateIn, CompoundNBT compound) {
		super.read(stateIn, compound);

		this.config = this.createConfigInstance().ReadFromCompoundNBT(compound);
	}

	public T createConfigInstance() {
		try {
			return this.getTypeParameterClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			Repurpose.LOGGER.log(Level.ERROR, e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Adds the allowed capabilities during tile entity initialization.
	 */
	protected void addAllowedCapabilities() {
	}

}

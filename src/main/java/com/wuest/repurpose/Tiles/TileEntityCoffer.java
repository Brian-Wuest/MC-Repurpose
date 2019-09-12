package com.wuest.repurpose.Tiles;

import java.util.Collections;
import java.util.Comparator;

import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Blocks.BlockCoffer;
import com.wuest.repurpose.Blocks.BlockCoffer.CofferType;
import com.wuest.repurpose.Gui.ContainerCoffer;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.Constants;

public class TileEntityCoffer extends LockableLootTileEntity implements ITickable {
	/** Chest Contents */
	public NonNullList<ItemStack> chestContents;

	/** Crystal Chest top stacks */
	private NonNullList<ItemStack> topStacks;

	/** The current angle of the lid (between 0 and 1) */
	public float lidAngle;

	/** The angle of the lid last tick */
	public float prevLidAngle;

	/** The number of players currently using this chest */
	public int numPlayersUsing;

	/** Server sync counter (once per 20 ticks) */
	private int ticksSinceSync;

	/** Direction chest is facing */
	private Direction facing;

	/** If the inventory got touched */
	private boolean inventoryTouched;

	/** If the inventory had items */
	private boolean hadStuff;

	private ITextComponent customName;

	private CofferType chestType;

	public static TileEntityType<TileEntityCoffer> CofferTileEntityType = TileEntityCoffer.register("coffer",
			TileEntityType.Builder.create(TileEntityCoffer::new, ModRegistry.Coffer()));

	public TileEntityCoffer() {
		this(CofferType.IRON);
	}

	protected TileEntityCoffer(CofferType type) {
		super(TileEntityCoffer.CofferTileEntityType);
		this.chestType = type;
		this.chestContents = NonNullList.<ItemStack>withSize(type.size, ItemStack.EMPTY);
		this.topStacks = NonNullList.<ItemStack>withSize(8, ItemStack.EMPTY);
		this.facing = Direction.NORTH;
	}

	private static <T extends TileEntity> TileEntityType<T> register(String key, TileEntityType.Builder<T> builder) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, key, null);
	}

	public void setContents(NonNullList<ItemStack> contents) {
		this.chestContents = NonNullList.<ItemStack>withSize(this.getCofferType().size, ItemStack.EMPTY);

		for (int i = 0; i < contents.size(); i++) {
			if (i < this.chestContents.size()) {
				this.getItems().set(i, contents.get(i));
			}
		}

		this.inventoryTouched = true;
	}

	@Override
	public int getSizeInventory() {
		return this.getItems().size();
	}

	public Direction getFacing() {
		return this.facing;
	}

	public CofferType getCofferType() {
		CofferType type = CofferType.IRON;

		if (this.hasWorld()) {
			BlockState state = this.world.getBlockState(this.pos);

			if (state.getBlock() == ModRegistry.Coffer()) {
				type = state.with(BlockCoffer.VARIANT_PROP);
			}
		}

		return type;
	}

	protected void setItems(NonNullList<ItemStack> itemsIn) {
		this.chestContents = itemsIn;
	}

	protected Container createMenu(int id, PlayerInventory playerInventory) {
		return new ContainerCoffer(id, playerInventory, this, this.getCofferType(), this.getCofferType().xSize, this.getCofferType().ySize);
	 }

	@Override
	public ItemStack getStackInSlot(int index) {
		this.fillWithLoot((PlayerEntity) null);

		this.inventoryTouched = true;

		return this.getItems().get(index);
	}

	@Override
	public void markDirty() {
		super.markDirty();

		this.sortTopStacks();
	}

	protected ITextComponent getDefaultName() {
		return new StringTextComponent("Coffer");
	}

	protected void sortTopStacks() {
		if (!this.getCofferType().isTransparent() || (this.world != null && this.world.isRemote)) {
			return;
		}

		NonNullList<ItemStack> tempCopy = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);

		boolean hasStuff = false;

		int compressedIdx = 0;

		mainLoop: for (int i = 0; i < this.getSizeInventory(); i++) {
			ItemStack itemStack = this.getItems().get(i);

			if (!itemStack.isEmpty()) {
				for (int j = 0; j < compressedIdx; j++) {
					ItemStack tempCopyStack = tempCopy.get(j);

					if (ItemStack.areItemsEqualIgnoreDurability(tempCopyStack, itemStack)) {
						if (itemStack.getCount() != tempCopyStack.getCount()) {
							tempCopyStack.grow(itemStack.getCount());
						}

						continue mainLoop;
					}
				}

				tempCopy.set(compressedIdx, itemStack.copy());

				compressedIdx++;

				hasStuff = true;
			}
		}

		if (!hasStuff && this.hadStuff) {
			this.hadStuff = false;

			for (int i = 0; i < this.getTopItems().size(); i++) {
				this.getTopItems().set(i, ItemStack.EMPTY);
			}

			if (this.world != null) {
				BlockState BlockState = this.world.getBlockState(this.pos);

				this.world.notifyBlockUpdate(this.pos, BlockState, BlockState, 3);
			}

			return;
		}

		this.hadStuff = true;

		Collections.sort(tempCopy, new Comparator<ItemStack>() {
			@Override
			public int compare(ItemStack stack1, ItemStack stack2) {
				if (stack1.isEmpty()) {
					return 1;
				} else if (stack2.isEmpty()) {
					return -1;
				} else {
					return stack2.getCount() - stack1.getCount();
				}
			}
		});

		int p = 0;

		for (ItemStack element : tempCopy) {
			if (!element.isEmpty() && element.getCount() > 0) {
				if (p == this.getTopItems().size()) {
					break;
				}

				this.getTopItems().set(p, element);

				p++;
			}
		}

		for (int i = p; i < this.getTopItems().size(); i++) {
			this.getTopItems().set(i, ItemStack.EMPTY);
		}

		if (this.world != null) {
			BlockState BlockState = this.world.getBlockState(this.pos);

			this.world.notifyBlockUpdate(this.pos, BlockState, BlockState, 3);
		}

		sendTopStacksPacket();
	}

	@Override
	public ITextComponent getName() {
		return this.hasCustomName() ? this.customName : new StringTextComponent(this.getCofferType().name());
	}

	@Override
	public boolean hasCustomName() {
		return this.customName != null && this.customName.getString().length() > 0;
	}

	@Override
	public void setCustomName(ITextComponent name) {
		this.customName = name;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);

		this.chestContents = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);

		if (compound.contains("CustomName", Constants.NBT.TAG_STRING)) {
			this.customName = ITextComponent.Serializer.fromJson(compound.getString("CustomName"));
		}

		if (!this.checkLootAndRead(compound)) {
			ItemStackHelper.loadAllItems(compound, this.chestContents);
		}

		this.facing = Direction.byIndex(compound.getInt("facing"));

		this.sortTopStacks();
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);

		if (!this.checkLootAndWrite(compound)) {
			ItemStackHelper.saveAllItems(compound, this.chestContents);
		}

		compound.putByte("facing", (byte) this.facing.ordinal());

		if (this.hasCustomName()) {
			compound.putString("CustomName", ITextComponent.Serializer.toJson(this.customName));
		}

		return compound;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		if (this.world == null) {
			return true;
		}

		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		}

		return player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64D;
	}

	@Override
	public void tick() {
		// Resynchronizes clients with the server state
		//@formatter:off
        if (this.world != null && !this.world.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + this.pos.getX() + this.pos.getY() + this.pos.getZ()) % 200 == 0)
        //@formatter:on
		{
			this.numPlayersUsing = 0;

			float f = 5.0F;

			//@formatter:off
            for (PlayerEntity player : this.world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(this.pos.getX() - f, this.pos.getY() - f, this.pos.getZ() - f, this.pos.getX() + 1 + f, this.pos.getY() + 1 + f, this.pos.getZ() + 1 + f)))
            //@formatter:on
			{
				if (player.openContainer instanceof ContainerCoffer) {
					++this.numPlayersUsing;
				}
			}
		}

		if (this.world != null && !this.world.isRemote && this.ticksSinceSync < 0) {
			this.world.addBlockEvent(this.pos, ModRegistry.Coffer(), 3,
					((this.numPlayersUsing << 3) & 0xF8) | (this.facing.ordinal() & 0x7));
		}

		if (!this.world.isRemote && this.inventoryTouched) {
			this.inventoryTouched = false;

			this.sortTopStacks();
		}

		this.ticksSinceSync++;

		this.prevLidAngle = this.lidAngle;

		float angle = 0.1F;

		if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
			double x = this.pos.getX() + 0.5D;
			double y = this.pos.getY() + 0.5D;
			double z = this.pos.getZ() + 0.5D;

			this.world.playSound(null, x, y, z, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F,
					this.world.rand.nextFloat() * 0.1F + 0.9F);
		}

		if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
			float currentAngle = this.lidAngle;

			if (this.numPlayersUsing > 0) {
				this.lidAngle += angle;
			} else {
				this.lidAngle -= angle;
			}

			if (this.lidAngle > 1.0F) {
				this.lidAngle = 1.0F;
			}

			float maxAngle = 0.5F;

			if (this.lidAngle < maxAngle && currentAngle >= maxAngle) {
				double x = this.pos.getX() + 0.5D;
				double y = this.pos.getY() + 0.5D;
				double z = this.pos.getZ() + 0.5D;

				this.world.playSound(null, x, y, z, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F,
						this.world.rand.nextFloat() * 0.1F + 0.9F);
			}

			if (this.lidAngle < 0.0F) {
				this.lidAngle = 0.0F;
			}
		}
	}

	@Override
	public boolean receiveClientEvent(int id, int type) {
		if (id == 1) {
			this.numPlayersUsing = type;
		} else if (id == 2) {
			this.facing = Direction.byIndex(type);
		} else if (id == 3) {
			this.facing = Direction.byIndex(type & 0x7);
			this.numPlayersUsing = (type & 0xF8) >> 3;
		}

		return true;
	}

	@Override
	public void openInventory(PlayerEntity player) {
		if (!player.isSpectator()) {
			if (this.world == null) {
				return;
			}

			if (this.numPlayersUsing < 0) {
				this.numPlayersUsing = 0;
			}

			++this.numPlayersUsing;

			this.world.addBlockEvent(this.pos, ModRegistry.Coffer(), 1, this.numPlayersUsing);
			this.world.notifyNeighborsOfStateChange(this.pos, ModRegistry.Coffer());
			this.world.notifyNeighborsOfStateChange(this.pos.down(), ModRegistry.Coffer());
		}
	}

	@Override
	public void closeInventory(PlayerEntity player) {
		if (!player.isSpectator()) {
			if (this.world == null) {
				return;
			}

			--this.numPlayersUsing;

			this.world.addBlockEvent(this.pos, ModRegistry.Coffer(), 1, this.numPlayersUsing);
			this.world.notifyNeighborsOfStateChange(this.pos, ModRegistry.Coffer());
			this.world.notifyNeighborsOfStateChange(this.pos.down(), ModRegistry.Coffer());
		}
	}

	public void setFacing(Direction facing) {
		this.facing = facing;
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT compound = new CompoundNBT();

		compound.putInt("facing", this.facing.getIndex());

		return new SUpdateTileEntityPacket(this.pos, 0, compound);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		if (pkt.getTileEntityType() == 0) {
			CompoundNBT compound = pkt.getNbtCompound();

			this.facing = Direction.byIndex(compound.getInt("facing"));
		}
	}

	public NonNullList<ItemStack> buildItemStackDataList() {
		if (this.getCofferType().isTransparent()) {
			NonNullList<ItemStack> sortList = NonNullList.<ItemStack>withSize(this.getTopItems().size(),
					ItemStack.EMPTY);

			int pos = 0;

			for (ItemStack is : this.topStacks) {
				if (!is.isEmpty()) {
					sortList.set(pos, is);
				} else {
					sortList.set(pos, ItemStack.EMPTY);
				}

				pos++;
			}

			return sortList;
		}

		return NonNullList.<ItemStack>withSize(this.getTopItems().size(), ItemStack.EMPTY);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return this.getCofferType().acceptsStack(stack);
	}

	public void rotateAround() {
		this.setFacing(this.facing.rotateY());

		this.world.addBlockEvent(this.pos, ModRegistry.Coffer(), 2, this.facing.ordinal());
	}

	public void wasPlaced(LivingEntity entityliving, ItemStack stack) {
	}

	public void removeAdornments() {
	}

	@Override
	public boolean canRenderBreaking() {
		return true;
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.write(new CompoundNBT());
	}

	@Override
	public NonNullList<ItemStack> getItems() {
		return this.chestContents;
	}

	public NonNullList<ItemStack> getTopItems() {
		return this.topStacks;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.chestContents) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	protected void sendTopStacksPacket() {
		NonNullList<ItemStack> stacks = this.buildItemStackDataList();
		// TODO: Repurpose.network.sendToAllAround(new MessageCrystalChestSync(this,
		// stacks), new TargetPoint(world.provider.getDimension(), getPos().getX(),
		// getPos().getY(), getPos().getZ(), 128));
	}

	public void receiveMessageFromServer(NonNullList<ItemStack> topStacks) {
		this.topStacks = topStacks;
	}
}
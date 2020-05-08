package com.wuest.repurpose.Items;

import com.wuest.repurpose.Capabilities.ItemBagOfHoldingProvider;
import com.wuest.repurpose.Items.Containers.BagOfHoldingContainer;
import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Repurpose;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author WuestMan
 */
public class ItemBagOfHolding extends Item {
	public static final String customValues = "bag_values";
	public static final String currentSlotName = "current_slot";
	public static final String bagOpenName = "bag_opened";

	public ItemBagOfHolding() {
		super(new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1));

		// This will determine what model is shown to the user when the bag is opened or closed.
		this.addPropertyOverride(new ResourceLocation(Repurpose.MODID, "bag_of_holding"), new IItemPropertyGetter() {

			@OnlyIn(Dist.CLIENT)
			public float call(ItemStack itemStack, @Nullable World world, @Nullable LivingEntity entity) {
				ItemBagOfHoldingProvider handler = ItemBagOfHoldingProvider.GetFromStack(itemStack);

				if (handler.opened) {
					return 1f;
				}

				return 0f;
			}
		});
	}

	public static void RefreshItemStack(PlayerEntity player, ItemStack stack) {
		if (stack.getItem() instanceof ItemBagOfHolding && !player.world.isRemote) {
			ItemBagOfHoldingProvider.UpdateRefreshValue(stack);
		}
	}

	@Nullable
	public static BlockRayTraceResult rayTrace(PlayerEntity player, double blockReachDistance, float partialTicks) {
		Vec3d vec3d = player.getEyePosition(partialTicks);
		Vec3d vec3d1 = player.getLook(partialTicks);
		Vec3d vec3d2 = vec3d.add(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance,
				vec3d1.z * blockReachDistance);

		RayTraceContext rayTraceContext = new RayTraceContext(vec3d, vec3d2, RayTraceContext.BlockMode.COLLIDER,
				RayTraceContext.FluidMode.NONE, player);
		return player.world.rayTraceBlocks(rayTraceContext);
	}

	public static int getCurrentSlotFromStack(ItemStack stack) {
		if (stack.getItem() instanceof ItemBagOfHolding) {
			ItemBagOfHoldingProvider handler = ItemBagOfHoldingProvider.GetFromStack(stack);

			return handler.slotIndex;
		}

		return 0;
	}

	public static void setCurrentSlotForStack(PlayerEntity player, ItemStack stack, int slot) {
		if (stack.getItem() instanceof ItemBagOfHolding) {
			ItemBagOfHoldingProvider handler = ItemBagOfHoldingProvider.GetFromStack(stack);
			handler.slotIndex = slot;

			handler.UpdateStack(stack);
		}
	}

	public static boolean getBagOpenedFromStack(ItemStack stack) {
		if (stack.getItem() instanceof ItemBagOfHolding) {
			ItemBagOfHoldingProvider handler = ItemBagOfHoldingProvider.GetFromStack(stack);

			return handler.opened;
		}

		return false;
	}

	public static void setBagOpenedStack(ItemStack stack, boolean open) {
		if (stack.getItem() instanceof ItemBagOfHolding) {
			ItemBagOfHoldingProvider handler = ItemBagOfHoldingProvider.GetFromStack(stack);
			handler.opened = open;

			handler.UpdateStack(stack);

			int metaValue = open ? 1 : 0;
			stack.setDamage(metaValue);
		}
	}

	public static ItemStack getItemStackFromInventory(PlayerEntity player) {
		ItemStack stack = player.getHeldItemOffhand();

		if (stack.getItem() instanceof ItemBagOfHolding) {
			int slot = ItemBagOfHolding.getCurrentSlotFromStack(stack);
			ItemBagOfHoldingProvider handler = ItemBagOfHoldingProvider.GetFromStack(stack);

			if (handler != null) {
				if (slot >= handler.getSlots()) {
					slot = 0;
				}

				return handler.getStackInSlot(slot);
			}
		}

		return ItemStack.EMPTY;
	}

	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		if (!world.isRemote && hand == Hand.OFF_HAND) {
			ItemStack stack = player.getHeldItem(hand);

			if (player.isCrouching()) {
				// Open and close the bag.
				// This is important for auto-pickup.
				ItemBagOfHolding.setBagOpenedStack(stack, !ItemBagOfHolding.getBagOpenedFromStack(stack));

				player.container.detectAndSendChanges();
				return new ActionResult<ItemStack>(ActionResultType.PASS, player.getHeldItem(hand));
			}

			BlockRayTraceResult result = ItemBagOfHolding.rayTrace(player, 5.0, 1.0f);

			if (result.getType() == Type.MISS) {

				// The consumer specified here is the "openMenu" method.
				INamedContainerProvider container = new SimpleNamedContainerProvider((windowId, playerInventory, playerEntity) ->
						new BagOfHoldingContainer(windowId, playerInventory), new StringTextComponent(ModRegistry.BagOfHolding.get().getRegistryName().toString()));

				NetworkHooks.openGui((ServerPlayerEntity) player, container, buf -> {
				});
			} else if (result.getType() == Type.BLOCK) {
				ItemBagOfHoldingProvider handler = ItemBagOfHoldingProvider.GetFromStack(stack);

				if (handler != null) {
					BlockPos newBlockPos = result.getPos().offset(result.getFace());

					if (world.isAirBlock(newBlockPos)) {
						int slot = ItemBagOfHolding.getCurrentSlotFromStack(stack);
						ItemStack stackInSlot = handler.extractItem(slot, 1, false);

						if (stackInSlot.getItem() instanceof BlockItem) {
							// This is an item block so it can be set in the
							// world.
							BlockItem itemBlock = (BlockItem) stackInSlot.getItem();
							ActionResultType placementResult = this.PlaceBlockFromPouch(player, itemBlock, result,
									hand);

							if (placementResult == ActionResultType.SUCCESS) {
								stackInSlot.shrink(1);
								handler.UpdateStack(stack);
							} else {
								handler.insertItem(slot, stackInSlot, false);
								handler.UpdateStack(stack);
							}
						}
					}
				}
			}
		}

		return new ActionResult<ItemStack>(ActionResultType.PASS, player.getHeldItem(hand));
	}

	/**
	 * Returns the unlocalized name of this item. This version accepts an ItemStack
	 * so different stacks can have different names based on their damage or NBT.
	 */
	@Override
	public String getTranslationKey(ItemStack stack) {
		String returnValue = super.getTranslationKey();

		boolean bagIsOpen = ItemBagOfHolding.getBagOpenedFromStack(stack);

		return returnValue + (bagIsOpen ? "_opened" : "_closed");
	}

	/**
	 * Override this method to change the NBT data being sent to the client. You
	 * should ONLY override this when you have no other choice, as this might change
	 * behavior client side!
	 *
	 * @param stack The stack to send the NBT tag for
	 * @return The NBT tag
	 */
	@Override
	public CompoundNBT getShareTag(ItemStack stack) {
		if (stack.getTag() == null || stack.getTag().isEmpty()) {
			// Make sure to serialize the NBT for this stack so the information is pushed to
			// the client and the
			// appropriate Icon is displayed for this stack.
			stack.setTag(stack.serializeNBT());
		}

		return stack.getTag();
	}

	public ActionResultType PlaceBlockFromPouch(PlayerEntity player, BlockItem itemBlock,
												BlockRayTraceResult rayTraceResult, Hand hand) {
		return itemBlock.tryPlace(new BlockItemUseContext(new ItemUseContext(player, hand, rayTraceResult)));
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
							   ITooltipFlag advanced) {
		super.addInformation(stack, worldIn, tooltip, advanced);

		boolean advancedKeyDown = Screen.hasShiftDown();

		if (!advancedKeyDown) {
			tooltip.add(new StringTextComponent(
					"Hold" + TextFormatting.BLUE + " Shift " + TextFormatting.GRAY + "for advanced information."));
		} else {
			tooltip.add(new StringTextComponent(
					"Place in off-hand and right-click to open inventory or place block. Sneak and right-click when in off-hand to open/close bag."));
		}
	}
}
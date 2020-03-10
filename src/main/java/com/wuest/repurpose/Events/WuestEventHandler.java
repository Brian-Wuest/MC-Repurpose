package com.wuest.repurpose.Events;

import com.wuest.repurpose.Base.TileEntityBase;
import com.wuest.repurpose.Capabilities.DimensionHome;
import com.wuest.repurpose.Capabilities.DimensionHomeProvider;
import com.wuest.repurpose.Capabilities.IDimensionHome;
import com.wuest.repurpose.Capabilities.ItemBagOfHoldingProvider;
import com.wuest.repurpose.Items.Containers.BagOfHoldingContainer;
import com.wuest.repurpose.Items.*;
import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Proxy.ClientProxy;
import com.wuest.repurpose.Proxy.Messages.BagOfHoldingUpdateMessage;
import com.wuest.repurpose.Proxy.Messages.BedLocationMessage;
import com.wuest.repurpose.Proxy.Messages.ConfigSyncMessage;
import com.wuest.repurpose.Repurpose;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.IProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.TableLootEntry;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Mod.EventBusSubscriber(modid = Repurpose.MODID)
public class WuestEventHandler {
	private static HashMap<String, BlockPos> playerBedLocation;

	private static HashMap<String, Integer> playerExistedTicks = new HashMap<String, Integer>();

	@SubscribeEvent
	public static void onPlayerLoginEvent(PlayerLoggedInEvent event) {
		if (!event.getPlayer().world.isRemote) {
			ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

			CompoundNBT tag = Repurpose.proxy.proxyConfiguration.ToNBTTagCompound();
			ConfigSyncMessage message = new ConfigSyncMessage(tag);

			Repurpose.network.sendTo(message, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
			System.out.println("Sent config to '" + player.getDisplayName().getString() + ".'");

			// get the actual inventory Slot:
			ItemStack offHandStack = player.getHeldItemOffhand();

			if (offHandStack.getItem() instanceof ItemBagOfHolding) {
				CompoundNBT compound =
						offHandStack.getItem().getShareTag(offHandStack);
				BagOfHoldingUpdateMessage bagOfHoldingUpdateMessage = new BagOfHoldingUpdateMessage(compound);
				Repurpose.network.sendTo(bagOfHoldingUpdateMessage, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
			}
		}
	}

	/**
	 * This is used to clear out the server configuration on the client side.
	 *
	 * @param event The event object.
	 */
	@SubscribeEvent
	public static void EntityJoinWorldEvent(PlayerEvent.PlayerLoggedOutEvent event) {
		if (event.getPlayer().getEntityWorld().isRemote) {
			// When the player logs out, make sure to re-set the server configuration.
			// This is so a new configuration can be successfully loaded when they switch
			// servers or worlds (on single
			// player.
			((ClientProxy) Repurpose.proxy).serverConfiguration = null;
		}
	}

	@SubscribeEvent
	public static void AttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
		// Only attach for players.
		if (event.getObject() instanceof PlayerEntity) {
			event.addCapability(new ResourceLocation(Repurpose.MODID, "dimension_home"),
					new DimensionHomeProvider(new DimensionHome()));
		}
	}

	@SubscribeEvent
	public static void AttachItemStackCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
		if (event.getObject().getItem() instanceof ItemBagOfHolding) {
			ItemBagOfHoldingProvider.AttachNewStackHandlerToStack(event.getObject());
		}
	}

	@SubscribeEvent
	public static void AttachTileEntityCapabilities(AttachCapabilitiesEvent<TileEntity> event) {
		if (event.getObject() instanceof TileEntityBase) {
			ArrayList<Capability> allowedCapabilities = ((TileEntityBase) event.getObject()).getAllowedCapabilities();
		}
	}

	@SubscribeEvent
	public static void PlayerChangedDimension(PlayerChangedDimensionEvent event) {
		IDimensionHome dimensionHome = event.getEntityPlayer().getCapability(ModRegistry.DimensionHomes, null)
				.orElse(null);

		if (dimensionHome != null) {
			dimensionHome.setHomePosition(event.getTo(), event.getEntityPlayer().getPosition());
		}
	}

	@SubscribeEvent
	public static void PlayerCloned(PlayerEvent.Clone event) {
		if (event.isWasDeath()) {
			// While not necessary for Vanilla, a mod could change where a player spawns
			// when they die, so copy over the
			// capabilities.
			PlayerEntity original = event.getOriginal();
			PlayerEntity newPlayer = event.getEntityPlayer();

			IDimensionHome originalDimensionHome = original.getCapability(ModRegistry.DimensionHomes, null)
					.orElse(null);
			IDimensionHome newDimensionHome = newPlayer.getCapability(ModRegistry.DimensionHomes, null).orElse(null);

			if (originalDimensionHome != null && newDimensionHome != null) {
				// Transfer the dimensional information to the new player.
				newDimensionHome.Transfer(originalDimensionHome);
			}
		}
	}

	@SubscribeEvent(receiveCanceled = true)
	public static void PlayerRightClicked(PlayerInteractEvent event) {
		// This only happens during the right-click event.
		// Can use the proxy's configuration.
		if (event.getHand() == Hand.OFF_HAND && Repurpose.proxy.proxyConfiguration.rightClickCropHarvest
				&& !event.getWorld().isRemote && !event.isCanceled()) {
			PlayerEntity p = event.getEntityPlayer();

			ItemStack currentStack = p.inventory.getCurrentItem();

			if (currentStack != null) {
				Item currentItem = currentStack.getItem();
				ItemStack boneMealStack = new ItemStack(Items.BONE_MEAL);
				Item boneMeal = boneMealStack.getItem();

				if (currentItem != null && currentItem == boneMeal) {
					if (Repurpose.proxy.proxyConfiguration.enableVerboseLogging) {
						System.out.println("Cannot harvest a crop when bone meal is being used.");
					}

					return;
				}
			}

			BlockState cropState = event.getWorld().getBlockState(event.getPos());
			Block crop = cropState.getBlock();
			IntegerProperty ageInteger = crop instanceof BeetrootBlock ? BeetrootBlock.BEETROOT_AGE : CropsBlock.AGE;

			// Only re-plant when this is a fully grown plant.
			if (crop instanceof CropsBlock || crop instanceof BushBlock) {
				if (Repurpose.proxy.proxyConfiguration.enableVerboseLogging) {
					System.out.println("Found a crop, check to see if it's fully grown.");
				}

				boolean cropIsMaxAge = false;

				// Look for a specific property called "age". All vanilla minecraft crops use
				// this name for their
				// property and most other mods do to.
				for (IProperty property : cropState.getProperties()) {
					if (property.getName().toLowerCase().equals("age") && property instanceof IntegerProperty) {
						// Found the age property, get the max age.
						ageInteger = (IntegerProperty) property;
						Optional<Integer> tempMax = ageInteger.getAllowedValues().stream().max(Integer::compare);

						if (tempMax.isPresent()) {
							int maxAge = tempMax.get();
							cropIsMaxAge = cropState.get(ageInteger) == maxAge;
						}

						break;
					}
				}

				if (cropIsMaxAge) {
					if (Repurpose.proxy.proxyConfiguration.enableVerboseLogging) {
						System.out.println("The crop is fully grown, get the drops, and try to re-plant.");
					}

					// Get the farmland below the crop.
					BlockPos farmlandPosition = event.getPos().down();

					// Get the drops from this crop and add it to the inventory.
					List<ItemStack> drops = Block.getDrops(cropState, (ServerWorld) event.getWorld(), event.getPos(),
							null);

					// Break the original crop block.
					event.getWorld().removeBlock(event.getPos(), false);

					ActionResultType replanted = ActionResultType.FAIL;
					BlockState tempState = cropState.with(ageInteger, 0);
					Item seed = crop.getItem(event.getWorld(), event.getPos(), cropState).getItem();

					for (ItemStack drop : drops) {
						Item dropItem = drop.getItem();

						// Make sure this is the same class as the crop's seed.
						if (dropItem.getClass() == seed.getClass() && replanted != ActionResultType.PASS) {
							Direction facing = event.getFace();
							ItemUseContext context = new ItemUseContext(p, event.getHand(),
									new BlockRayTraceResult(new Vec3d(0, 0, 0), Direction.UP, farmlandPosition, false));
							replanted = drop.onItemUse(context);

							if (replanted == ActionResultType.SUCCESS || replanted == ActionResultType.PASS) {
								replanted = ActionResultType.PASS;

								if (Repurpose.proxy.proxyConfiguration.enableVerboseLogging) {
									System.out.println("Found a 'seed' to plant for the crop ["
											+ crop.getRegistryName().toString()
											+ "] from the crop's drops. Not including it in the list of drops to be added to the player's inventory.");
								}

								continue;
							}
						}

						p.inventory.addItemStackToInventory(drop);
						p.openContainer.detectAndSendChanges();
					}

					if (replanted != ActionResultType.PASS) {
						// The only reason why we wouldn't have re-planted at this point is because the
						// wheat didn't
						// drop a seed. Check the player inventory for a seed and plant it.
						// This should work with other plants that override BlockCrops.GetItem with
						// their own seed.
						// Make sure to re-set the age to 0 to get the seed.
						// Get the seed item and check to see if the player has this in their inventory.
						// If they do we
						// can use it to re-plant.
						ItemStack seeds = new ItemStack(seed);

						if (p.inventory.hasItemStack(seeds)) {
							if (Repurpose.proxy.proxyConfiguration.enableVerboseLogging) {
								System.out.println("The player has the seed for this crop. Attempting to re-plant.");
							}

							BlockState state = event.getWorld().getBlockState(farmlandPosition);

							Direction facing = Direction.UP;

							if (p.canPlayerEdit(farmlandPosition.offset(facing), facing, seeds)
									&& state.getBlock().canSustainPlant(state, event.getWorld(), farmlandPosition,
									Direction.UP, (IPlantable) seeds.getItem())
									&& event.getWorld().isAirBlock(farmlandPosition.up())) {
								if (Repurpose.proxy.proxyConfiguration.enableVerboseLogging) {
									System.out.println(
											"The player is able to edit the top of this farmland and the farmland can sustaing this plant and the block above the farmland is air.");
								}

								event.getWorld().setBlockState(farmlandPosition.up(),
										((IPlantable) seeds.getItem()).getPlant(event.getWorld(), farmlandPosition));

								if (p instanceof ServerPlayerEntity) {
									CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) p, farmlandPosition.up(),
											seeds);

									if (seeds.getCount() == 1) {
										p.inventory.deleteStack(seeds);
									} else {
										seeds.setCount(seeds.getCount() - 1);
									}

									p.openContainer.detectAndSendChanges();

									if (Repurpose.proxy.proxyConfiguration.enableVerboseLogging) {
										System.out.println(
												"Right-click harvesting succeeded. Removing 1 'seed' item from the player's inventory since a seed was not included in the block drops.");
									}
								}
							} else {
								if (Repurpose.proxy.proxyConfiguration.enableVerboseLogging) {
									System.out.println(
											"Plant not re-planted. The player either cannot edit the block above the farmland or the farmland cannot sustaing this plant or the block above the farmland is not air.");
								}
							}
						} else {
							if (Repurpose.proxy.proxyConfiguration.enableVerboseLogging) {
								System.out.println("Plant not re-planted. The player does not have the seed: ["
										+ seed.getRegistryName().toString() + "] in their inventory.");
							}
						}
					}

					event.setCanceled(true);

					if (Repurpose.proxy.proxyConfiguration.enableVerboseLogging) {
						System.out.println(
								"Cancelling future player right-clicked events so multiple right-click harvesting mods don't duplicate drops.");
					}
				}
			} else {
				if (Repurpose.proxy.proxyConfiguration.enableVerboseLogging) {
					System.out.println(
							"The block right-clicked is not a harvestable block. Right-click harvesting did not occur.");
				}
			}
		} else if (!event.getWorld().isRemote && Repurpose.proxy.proxyConfiguration.rightClickCropHarvest
				&& event.isCanceled()) {
			if (Repurpose.proxy.proxyConfiguration.enableVerboseLogging) {
				System.out.println(
						"This event was previously canceled, right-click harvesting did not happen from this mod.");
			}
		}
	}

	@SubscribeEvent
	public static void PlayerTickEvent(TickEvent.PlayerTickEvent event) {
		if (event.side.isServer()) {
			// Send the player's actual bed location to the client for the bed compass
			// object.
			// This is needed as the client doesn't properly store the bed location.
			WuestEventHandler.sendPlayerBedLocation(event);
		}

		// this.generatePlayerParticles(event);
	}

	@SubscribeEvent
	public static void AnvilUpdate(AnvilUpdateEvent event) {
		ItemStack rightItem = event.getRight();
		ItemStack leftItem = event.getLeft();

		if (rightItem.getItem() instanceof BookItem) {
			ItemStack enchantedBook = null;

			// These items create enchanted books.
			if (leftItem.getItem() instanceof ItemFluffyFabric) {
				// Set the output to an enchanted book with the Silk Touch enchantment.
				enchantedBook = EnchantedBookItem
						.getEnchantedItemStack(new EnchantmentData(Enchantments.SILK_TOUCH, 1));
				event.setCost(3);
			} else if (leftItem.getItem() instanceof ItemWhetStone) {
				// Set the output to an enchanted book with the Sharpness 1 enchantment.
				enchantedBook = EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(Enchantments.SHARPNESS, 1));
				event.setCost(1);

			} else if (leftItem.getItem() instanceof ItemSnorkel) {
				// Set the output to an enchanted book with water breathing 1 enchantment.
				enchantedBook = EnchantedBookItem
						.getEnchantedItemStack(new EnchantmentData(Enchantments.RESPIRATION, 1));
				event.setCost(2);
			}

			if (enchantedBook != null) {
				event.setOutput(enchantedBook);
			}
		} else if ((rightItem.getItem() instanceof ArmorItem
				&& ((ArmorItem) rightItem.getItem()).getEquipmentSlot() == EquipmentSlotType.FEET
				&& leftItem.getItem() instanceof ItemFluffyFabric)
				|| (leftItem.getItem() instanceof ArmorItem
				&& ((ArmorItem) leftItem.getItem()).getEquipmentSlot() == EquipmentSlotType.FEET
				&& rightItem.getItem() instanceof ItemFluffyFabric)) {
			Item enchantingItem = rightItem.getItem() instanceof ArmorItem ? rightItem.getItem() : leftItem.getItem();

			ItemStack result = new ItemStack(enchantingItem);

			if (result != null) {
				result.addEnchantment(ModRegistry.StepAssist(), 1);
				event.setCost(2);
			}

			event.setOutput(result);
		}

		if (rightItem.getItem() instanceof ItemScroll || leftItem.getItem() instanceof ItemScroll) {
			event = WuestEventHandler.processScrollUpdate(event);
		}
	}

	@SubscribeEvent
	public static void onLootLoad(LootTableLoadEvent event) {
		ResourceLocation newTable = null;
		ResourceLocation eventName = event.getName();
		String modID = eventName.getNamespace();
		String path = eventName.getPath();

		if (path.contains("/")) {
			path = path.substring(path.lastIndexOf("/") + 1);
		}

		eventName = new ResourceLocation(modID, path);

		if (eventName.equals(Blocks.DIRT.getRegistryName()) || eventName.equals(Blocks.GRASS_BLOCK.getRegistryName())) {
			newTable = new ResourceLocation(Repurpose.MODID, "blocks/dirt");
		} else if (eventName.equals(Blocks.GRASS.getRegistryName())) {
			newTable = new ResourceLocation(Repurpose.MODID, "blocks/grass");
		} else if (eventName.equals(Blocks.STONE.getRegistryName())) {
			newTable = new ResourceLocation(Repurpose.MODID, "blocks/stone");
		} else if (eventName.equals(Blocks.COAL_ORE.getRegistryName())) {
			newTable = new ResourceLocation(Repurpose.MODID, "blocks/coal_ore");
		} else if (eventName.equals(EntityType.ZOMBIE.getRegistryName())
				|| eventName.equals(EntityType.SKELETON.getRegistryName())
				|| eventName.equals(EntityType.CREEPER.getRegistryName())) {
			newTable = new ResourceLocation(Repurpose.MODID, "entities/" + eventName.getPath());
		} else if (eventName.getPath().toLowerCase().contains("_leaves")) {
			boolean foundBlock = false;
			for (Block block : BlockTags.LEAVES.getAllElements()) {
				if (eventName.getPath().equals(block.getRegistryName().getPath())) {
					foundBlock = true;
					break;
				}
			}

			if (foundBlock) {
				newTable = new ResourceLocation(Repurpose.MODID, "blocks/leaves");
			}
		}

		if (newTable != null) {
			event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(newTable)).build());
		}
	}

	@SubscribeEvent
	public static void onPickUp(EntityItemPickupEvent event) {
		if (!event.isCanceled()) {
			boolean setSlot = false;
			PlayerEntity player = event.getEntityPlayer();
			ItemStack eventStack = event.getItem().getItem();

			for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
				ItemStack stack = player.inventory.getStackInSlot(i);

				if (stack.getItem() instanceof ItemBagOfHolding) {
					ItemBagOfHoldingProvider handler = ItemBagOfHoldingProvider.GetFromStack(stack);

					// Only auto-pickup if this bag is set to open and the stack is valid for the
					// bag of holding.
					if (handler.opened && BagOfHoldingContainer.validForContainer(eventStack)) {
						int firstEmptySlot = -1;

						for (int j = 0; j < handler.getSlots(); j++) {
							ItemStack pouchStack = handler.getStackInSlot(j);

							if (pouchStack.isEmpty() && firstEmptySlot == -1) {
								// Found an empty slot, maybe use this later.
								firstEmptySlot = j;
							}

							if (!pouchStack.isEmpty()) {
								// This has an item in it of some kind, determine if they are the same.
								if (pouchStack.areItemsEqual(pouchStack, eventStack)) {
									int updatedSize = pouchStack.getCount() + eventStack.getCount();

									// Make sure we don't go above the stack limit for this slot.
									// If there is too much, just move onto the next slot.
									if (updatedSize <= handler.getSlotLimit(j)) {
										setSlot = true;
										pouchStack.setCount(updatedSize);
										handler.setStackInSlot(j, pouchStack);
										break;
									}
								}
							}
						}

						if (!setSlot && firstEmptySlot != -1) {
							// There was not a matching stack, place it in the first empty slot.
							handler.setStackInSlot(firstEmptySlot, eventStack);
							setSlot = true;
						}

						if (setSlot) {
							// We set an inventory slot, set this event to canceled and break out of the
							// loop.
							player.onItemPickup(event.getItem(), event.getItem().getItem().getCount());

							player.addStat(Stats.ITEM_PICKED_UP.get(event.getItem().getItem().getItem()),
									event.getItem().getItem().getCount());
							event.getItem().remove();
							event.setCanceled(true);

							handler.UpdateStack(stack);
							ItemBagOfHolding.RefreshItemStack(player, stack);
							break;
						}
					}
				}
			}
		}
	}

	private static void sendPlayerBedLocation(TickEvent.PlayerTickEvent event) {
		if (WuestEventHandler.playerBedLocation == null) {
			WuestEventHandler.playerBedLocation = new HashMap<String, BlockPos>();
		}

		// Send the updated bed information to the client.
		BedLocationMessage message = new BedLocationMessage();
		CompoundNBT tag = new CompoundNBT();
		ServerPlayerEntity player = (ServerPlayerEntity) event.player;
		BlockPos bedPosition = player.getBedLocation();

		if (bedPosition != null) {
			tag.putInt("bedX", bedPosition.getX());
			tag.putInt("bedY", bedPosition.getY());
			tag.putInt("bedZ", bedPosition.getZ());
		}

		message.setMessageTag(tag);
		BlockPos existingBedPosition = null;

		if (WuestEventHandler.playerBedLocation.containsKey(player.getDisplayName().getString())) {
			existingBedPosition = WuestEventHandler.playerBedLocation.get(player.getDisplayName().getString());
		} else {
			WuestEventHandler.playerBedLocation.put(player.getDisplayName().getString(), bedPosition);
		}

		if (existingBedPosition != bedPosition) {
			// Only send the message to the client if the bed position changes.
			Repurpose.network.sendTo(message, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
		}
	}

	private static void generatePlayerParticles(TickEvent.PlayerTickEvent event) {
		PlayerEntity player = event.player;

		if (WuestEventHandler.playerExistedTicks.containsKey(player.getDisplayName().getString())) {
			int ticks = WuestEventHandler.playerExistedTicks.get(player.getDisplayName().getString());

			if (ticks % 20 == 0) {
				Repurpose.proxy.generateParticles(player);
				ticks = 0;
			}

			ticks++;
			WuestEventHandler.playerExistedTicks.put(player.getDisplayName().getString(), ticks);
		} else {
			WuestEventHandler.playerExistedTicks.put(player.getDisplayName().getString(), 0);
		}

	}

	private static void checkChanceAndAddToDrops(World world, List<ItemStack> drops, double maxPercentage,
												 Item itemToDrop, int quantity) {
		double randomChance = WuestEventHandler.getRandomChance(world);

		if (randomChance <= maxPercentage) {
			drops.add(new ItemStack(itemToDrop, quantity));
		}
	}

	private static double getRandomChance(World world) {
		double randomChance = world.rand.nextDouble();
		BigDecimal bigDecimal = new BigDecimal(Double.toString(randomChance));
		bigDecimal = bigDecimal.setScale(3, RoundingMode.HALF_UP);
		return bigDecimal.doubleValue();
	}

	private static AnvilUpdateEvent processScrollUpdate(AnvilUpdateEvent event) {
		ItemStack leftItemStack = event.getLeft();
		ItemStack rightItemStack = event.getRight();
		ItemStack copyOfLeft = leftItemStack.copy();
		Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(leftItemStack);
		int i = 0;
		int initialRepairCost = leftItemStack.getRepairCost() + rightItemStack.getRepairCost();
		int k = 0;

		boolean isEnchantedScroll = rightItemStack.getItem() instanceof ItemScroll
				&& !rightItemStack.getEnchantmentTagList().isEmpty();

		if (leftItemStack.isDamageable() && leftItemStack.getItem().getIsRepairable(copyOfLeft, rightItemStack)) {
			int l2 = Math.min(leftItemStack.getDamage(), leftItemStack.getMaxDamage() / 4);

			if (l2 <= 0) {
				event.setOutput(ItemStack.EMPTY);
				event.setCost(0);
				return event;
			}

			int i3;

			for (i3 = 0; l2 > 0 && i3 < rightItemStack.getCount(); ++i3) {
				int j3 = leftItemStack.getDamage() - l2;
				leftItemStack.setDamage(j3);
				++i;
				l2 = Math.min(leftItemStack.getDamage(), leftItemStack.getMaxDamage() / 4);
			}

			event.setCost(i3);
		} else {
			if ((!isEnchantedScroll
					&& (leftItemStack.getItem() != rightItemStack.getItem() || !leftItemStack.isDamageable()))
					|| (isEnchantedScroll
					&& !leftItemStack.getItem().isBookEnchantable(leftItemStack, rightItemStack))) {
				event.setOutput(ItemStack.EMPTY);
				event.setCost(0);
				return event;
			}

			if (leftItemStack.isDamageable() && !isEnchantedScroll) {
				int l = copyOfLeft.getMaxDamage() - copyOfLeft.getDamage();
				int i1 = rightItemStack.getMaxDamage() - rightItemStack.getDamage();
				int j1 = i1 + leftItemStack.getMaxDamage() * 12 / 100;
				int k1 = l + j1;
				int l1 = leftItemStack.getMaxDamage() - k1;

				if (l1 < 0) {
					l1 = 0;
				}

				if (l1 < leftItemStack.getDamage()) // vanilla uses metadata here instead of damage.
				{
					leftItemStack.setDamage(l1);
					i += 2;
				}
			}

			Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(rightItemStack);
			boolean validEnchantments = false;
			boolean invalidEnchantments = false;

			for (Enchantment enchantment1 : map1.keySet()) {
				if (enchantment1 != null) {
					int i2 = map.containsKey(enchantment1) ? ((Integer) map.get(enchantment1)).intValue() : 0;
					int j2 = ((Integer) map1.get(enchantment1)).intValue();
					j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
					boolean canApplyEnchantment = enchantment1.canApply(copyOfLeft);

					if (copyOfLeft.getItem() == Items.ENCHANTED_BOOK || copyOfLeft.getItem() instanceof ItemScroll) {
						canApplyEnchantment = true;
					}

					for (Enchantment enchantment : map.keySet()) {
						if (enchantment != enchantment1 && !enchantment1.isCompatibleWith(enchantment)) {
							canApplyEnchantment = false;
							++i;
						}
					}

					if (!canApplyEnchantment) {
						invalidEnchantments = true;
					} else {
						validEnchantments = true;

						if (j2 > enchantment1.getMaxLevel()) {
							j2 = enchantment1.getMaxLevel();
						}

						map.put(enchantment1, Integer.valueOf(j2));
						int k3 = 0;

						switch (enchantment1.getRarity()) {
							case COMMON: {
								k3 = 1;
								break;
							}

							case UNCOMMON: {
								k3 = 2;
								break;
							}

							case RARE: {
								k3 = 4;
								break;
							}
							case VERY_RARE: {
								k3 = 8;
							}
						}

						if (isEnchantedScroll) {
							k3 = Math.max(1, k3 / 2);
						}

						i += k3 * j2;

						if (copyOfLeft.getCount() > 1) {
							i = 40;
						}
					}
				}
			}

			if (invalidEnchantments && !validEnchantments) {
				event.setOutput(ItemStack.EMPTY);
				event.setCost(0);
				return event;
			}

			int maximumCost = initialRepairCost + i;

			if (i <= 0) {
				copyOfLeft = ItemStack.EMPTY;
			}

			if (k == i && k > 0 && maximumCost >= 40) {
				maximumCost = 39;
			}

			if (!leftItemStack.isEmpty()) {
				int totalCost = leftItemStack.getRepairCost();

				if (!rightItemStack.isEmpty() && totalCost < rightItemStack.getRepairCost()) {
					totalCost = rightItemStack.getRepairCost();
				}

				if (k != i || k == 0) {
					totalCost = totalCost * 2 + 1;
				}

				copyOfLeft.setRepairCost(totalCost);
				EnchantmentHelper.setEnchantments(map, copyOfLeft);
				event.setCost(maximumCost);
				event.setOutput(copyOfLeft);
			}
		}

		return event;
	}
}
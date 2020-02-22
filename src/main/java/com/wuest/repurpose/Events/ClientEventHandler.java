package com.wuest.repurpose.Events;

import java.awt.Color;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.mojang.blaze3d.platform.GlStateManager;
import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Enchantment.EnchantmentStepAssist;
import com.wuest.repurpose.Items.ItemBagOfHolding;
import com.wuest.repurpose.Proxy.Messages.CurrentSlotUpdateMessage;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.SectionPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * This class is used to handle client side only events.
 * 
 * @author WuestMan
 *
 */
@Mod.EventBusSubscriber(modid = Repurpose.MODID, value = { Dist.CLIENT })
public class ClientEventHandler {
	public static ArrayList<KeyBinding> keyBindings = new ArrayList<KeyBinding>();
	public static LocalDateTime bedCompassTime;
	public static BlockPos bedLocation;
	protected static final ResourceLocation WIDGETS_TEX_PATH = new ResourceLocation("textures/gui/widgets.png");
	private static final int BUFF_ICON_SIZE = 18;
	private static final int BUFF_ICON_SPACING = BUFF_ICON_SIZE + 5; // 2 pixels between buff icons
	private static final int BUFF_ICON_BASE_U_OFFSET = 0;
	private static final int BUFF_ICON_BASE_V_OFFSET = 198;
	private static final int BUFF_ICONS_OFFSET = 8;
	private static final int GREEN_TEXT = Color.GREEN.getRGB();

	private static HashMap<String, StepAssistInfo> playerStepAssists = new HashMap<String, StepAssistInfo>();

	/**
	 * This event is called by GuiIngameForge during each frame by
	 * GuiIngameForge.pre() and GuiIngameForce.post().
	 * 
	 * @param event
	 */
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onRenderExperienceBar(RenderGameOverlayEvent event) {
		// We draw after the ExperienceBar has drawn. The event raised by
		// GuiIngameForge.pre()
		// will return true from isCancelable. If you call event.setCanceled(true) in
		// that case, the portion of rendering which this event represents will be
		// canceled.
		// We want to draw *after* the experience bar is drawn, so we make sure
		// isCancelable() returns
		// false and that the eventType represents the ExperienceBar event.
		if (event.isCancelable() || event.getType() != ElementType.EXPERIENCE) {
			return;
		}

		ClientEventHandler.ShowPlayerBed(event);
	}

	/**
	 * This event is called by GuiIngameForge during each frame by
	 * GuiIngameForge.pre() and GuiIngameForce.post().
	 * 
	 * @param event
	 */
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onRenderHotbar(RenderGameOverlayEvent.Post event) {
		if (event.getType() == ElementType.HOTBAR) {
			Minecraft mc = Minecraft.getInstance();
			PlayerEntity player = mc.player;
			ItemStack itemStack = player.getHeldItemOffhand();

			if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemBagOfHolding) {
				int currentSlot = ItemBagOfHolding.getCurrentSlotFromStack(itemStack) + 1;

				int scaledWidth = event.getWindow().getScaledWidth();
				int scaledHeighht = event.getWindow().getScaledHeight();

				int offHandSlotLocationX = (scaledWidth / 2) - 91 - 29;
				int offHandSlotLocationY = scaledHeighht - 23;
				int selectedSlotLocationY = offHandSlotLocationY - 23;

				mc.fontRenderer.drawString(((Integer) currentSlot).toString(), offHandSlotLocationX - 12,
						selectedSlotLocationY + 8, ClientEventHandler.GREEN_TEXT);

				GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				mc.getTextureManager().bindTexture(WIDGETS_TEX_PATH);

				GlStateManager.enableBlend();
				Screen screen = new Screen(new StringTextComponent("some title")) {
				};

				screen.blit(offHandSlotLocationX, selectedSlotLocationY, 24, 23, 22, 23);

				GlStateManager.enableRescaleNormal();
				GlStateManager.enableBlend();
				GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
						GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
						GlStateManager.DestFactor.ZERO);
				RenderHelper.enableGUIStandardItemLighting();

				// Draw Item here:
				ItemStack itemToDraw = ItemBagOfHolding.getItemStackFromInventory(player);

				if (!itemToDraw.isEmpty()) {
					ClientEventHandler.renderHotbarItem(mc, offHandSlotLocationX + 3, selectedSlotLocationY + 3, 1,
							player, itemToDraw);
				}

				RenderHelper.disableStandardItemLighting();
				GlStateManager.disableRescaleNormal();
				GlStateManager.disableBlend();
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public void KeyInput(KeyInputEvent event) {
		for (KeyBinding binding : ClientEventHandler.keyBindings) {
			if (binding.isPressed()) {
				boolean foundModifier = false;
				int modifier = 0;
				if (binding.getKeyDescription().equals("Next Item")) {
					modifier = 1;
					foundModifier = true;
				} else if (binding.getKeyDescription().equals("Previous Item")) {
					modifier = -1;
					foundModifier = true;
				}

				if (foundModifier) {
					PlayerEntity player = Minecraft.getInstance().player;
					ItemStack stack = player.getHeldItemOffhand();

					if (stack.getItem() instanceof ItemBagOfHolding) {
						int currentSlot = ItemBagOfHolding.getCurrentSlotFromStack(stack);

						if (currentSlot == 53 && modifier > 0) {
							currentSlot = 0;
						} else if (currentSlot == 0 && modifier < 0) {
							currentSlot = 53;
						} else {
							currentSlot = currentSlot + modifier;
						}

						// Send a message to the server to update the current slot.
						CompoundNBT tag = new CompoundNBT();
						tag.putInt("slot", currentSlot);
						CurrentSlotUpdateMessage message = new CurrentSlotUpdateMessage(tag);
						Repurpose.network.sendToServer(message);
					}
				}

				break;
			}
		}
	}

	@SubscribeEvent
	public static void PlayerTickEvent(TickEvent.PlayerTickEvent event) {
		if (event.side.isClient()) {
			if (event.phase == Phase.START) {
				ClientEventHandler.setStepHeight(event);

				if (Repurpose.proxy.getServerConfiguration().enableMobileLight) {
					ClientEventHandler.setPlayerLight(event);
				}
			}
		}
	}

	private static void ShowPlayerBed(RenderGameOverlayEvent event) {
		if (ClientEventHandler.bedCompassTime != null) {
			Minecraft mc = Minecraft.getInstance();
			PlayerEntity player = mc.player;
			long timeBetween = java.time.temporal.ChronoUnit.SECONDS.between(ClientEventHandler.bedCompassTime,
					LocalDateTime.now());

			// Do drawing logic here.

			int x = event.getWindow().getScaledWidth() / 4;
			int y = event.getWindow().getScaledHeight() / 2 - 23;

			BlockPos playerPosition = player.getPosition();

			if (ClientEventHandler.bedLocation != null) {
				// If yOffset is positive, the player is higher than the bed, if it's negative
				// the player is lower than
				// the bed.
				int yOffSet = playerPosition.getY() - ClientEventHandler.bedLocation.getY();

				// X = East/West, west being less than 0 and east being greater than 0.
				// If the offset is greater than 0 the player is east of the bed, otherwise the
				// player is west of the
				// bed.
				int xOffSet = playerPosition.getX() - ClientEventHandler.bedLocation.getX();

				// Z = North/South. North being less than 0 and south being greater than 0.
				// If the offset is greater than 0 then the player is south of the bed,
				// otherwise the player is north of
				// the bed.
				int zOffSet = playerPosition.getZ() - ClientEventHandler.bedLocation.getZ();

				Minecraft.getInstance().fontRenderer.drawString("Your bed is...", x, y, Color.WHITE.getRGB());

				y = y + 10;

				if (yOffSet > 0) {
					Minecraft.getInstance().fontRenderer.drawString(((Integer) yOffSet).toString() + " Block(s) Lower",
							x, y, (new Color(200, 117, 51).getRGB()));
				} else if (yOffSet < 0) {
					Minecraft.getInstance().fontRenderer.drawString(
							((Integer) Math.abs(yOffSet)).toString() + " Block(s) Higher", x, y,
							(new Color(52, 221, 221).getRGB()));
				} else {
					y = y - 10;
				}

				y = y + 10;

				if (xOffSet > 0) {
					Minecraft.getInstance().fontRenderer.drawString(((Integer) xOffSet).toString() + " Block(s) West",
							x, y, (new Color(207, 83, 0).getRGB()));
				} else if (xOffSet < 0) {
					Minecraft.getInstance().fontRenderer.drawString(
							((Integer) Math.abs(xOffSet)).toString() + " Block(s) East", x, y,
							(new Color(255, 204, 0).getRGB()));
				} else {
					y = y - 10;
				}

				y = y + 10;

				if (zOffSet > 0) {
					Minecraft.getInstance().fontRenderer.drawString(((Integer) zOffSet).toString() + " Block(s) North",
							x, y, (new Color(204, 204, 255).getRGB()));
				} else if (zOffSet < 0) {
					Minecraft.getInstance().fontRenderer.drawString(
							((Integer) Math.abs(zOffSet)).toString() + " Block(s) South", x, y,
							(new Color(91, 194, 54).getRGB()));
				} else {
					y = y - 10;
				}

				y = y + 10;

				if (xOffSet == 0 && yOffSet == 0 && zOffSet == 0) {
					Minecraft.getInstance().fontRenderer.drawString("Right next to you", x, y, Color.WHITE.getRGB());
				} else {
					Minecraft.getInstance().fontRenderer.drawString("Of you", x, y, Color.WHITE.getRGB());
				}
			} else {
				// Send a chat to the user that their bed was not found.
				mc.player.sendChatMessage("Bed Not Found");
				timeBetween = 99999999;
			}

			if (timeBetween > 8) {
				ClientEventHandler.bedCompassTime = null;
			}
		}
	}

	/**
	 * This method is used to create light around the player when they are holding a
	 * light-source block.
	 * 
	 * @param event The Player Tick Event
	 */
	private static void setPlayerLight(TickEvent.PlayerTickEvent event) {
		World world = event.player.world;
		PlayerEntity player = event.player;
		BlockPos originalPos = new BlockPos(player.posX, player.posY + 1, player.posZ);
		BlockPos pos = new BlockPos(player.posX, player.posY + 1, player.posZ);
		BlockPos prevPos = pos.north(2).east(2).up(3);
		pos = pos.south(1).west(2).down(3);

		if (event.player.getHealth() > 0.0F) {
			ItemStack mainHandStack = player.getHeldItemMainhand();
			ItemStack offHandStack = player.getHeldItemOffhand();

			if (mainHandStack != ItemStack.EMPTY || mainHandStack != ItemStack.EMPTY) {
				Block mainBlock = null;
				Block offHandBlock = null;
				boolean foundLightBlock = false;

				if (mainHandStack != ItemStack.EMPTY) {
					mainBlock = Block.getBlockFromItem(mainHandStack.getItem());

					if (mainBlock != null && mainBlock.getLightValue(mainBlock.getDefaultState()) > 0) {
						foundLightBlock = true;
					}
				}

				if (offHandStack != ItemStack.EMPTY && !foundLightBlock) {
					offHandBlock = Block.getBlockFromItem(offHandStack.getItem());

					if (offHandBlock != null && offHandBlock.getLightValue(mainBlock.getDefaultState()) > 0) {
						foundLightBlock = true;
					}
				}

				if (foundLightBlock) {
					NibbleArray array = new NibbleArray();
					array.set(pos.getX(), pos.getY(), pos.getZ(), 12);

					world.getChunkProvider().getLightManager().setData(LightType.BLOCK, SectionPos.from(pos), array);

					// System.out.println("Light Level: " +
					// ((Integer)world.getLightFor(EnumSkyBlock.BLOCK,
					// pos)).toString());
				} else {
					NibbleArray array = new NibbleArray();
					array.set(pos.getX(), pos.getY(), pos.getZ(), 0);

					world.getChunkProvider().getLightManager().setData(LightType.BLOCK, SectionPos.from(originalPos),
							array);
				}
			}

			world.markChunkDirty(originalPos, null);
			world.notifyBlockUpdate(originalPos, world.getBlockState(originalPos), world.getBlockState(originalPos), 3);

			for (BlockPos otherPos : (BlockPos[]) BlockPos.getAllInBox(prevPos, pos).toArray()) {
				// Don't update for the current position.
				if (originalPos.getX() == otherPos.getX() && originalPos.getY() == otherPos.getY()
						&& originalPos.getZ() == otherPos.getZ()) {
					continue;
				} else {
					world.getChunkProvider().getLightManager().checkBlock(otherPos);
				}
			}
		} else {
			NibbleArray array = new NibbleArray();
			array.set(pos.getX(), pos.getY(), pos.getZ(), 0);

			world.getChunkProvider().getLightManager().setData(LightType.BLOCK, SectionPos.from(originalPos), array);

			world.getChunkProvider().getLightManager().checkBlock(originalPos);
		}
	}

	private static void setStepHeight(TickEvent.PlayerTickEvent event) {
		PlayerEntity player = event.player;
		ItemStack bootsStack = player.inventory.armorInventory.get(0);

		// Check to see if the player is wearing a pair of enchanted boots with step
		// assist.
		// Check to see if the player was added to the hashset and the game setting for
		// auto-jump was enabled.
		// If it was, re-set their step height to the original step height and remove
		// them from the hashset.
		if (ClientEventHandler.playerStepAssists.containsKey(player.getName())
				&& (bootsStack == null || !bootsStack.isEnchanted() || Minecraft.getInstance().gameSettings.autoJump)) {
			// Reset the player step height to the original step height and remove this
			// record from the hashset.
			StepAssistInfo info = ClientEventHandler.playerStepAssists.get(player.getName());
			player.stepHeight = info.oldStepHeight;
			ClientEventHandler.playerStepAssists.remove(player.getName());
			return;
		}

		// Don't bother adding them to the hashset if auto-jump is enabled.
		// On the tick after re-setting the player's step height, check to see if the
		// the enchantment is even enabled in
		// the configuration.
		if (!Minecraft.getInstance().gameSettings.autoJump
				&& Repurpose.proxy.getServerConfiguration().enableStepAssistEnchantment) {
			if (ClientEventHandler.playerStepAssists.containsKey(player.getName()) && bootsStack != null
					&& bootsStack.isEnchanted()) {
				// The player was in the list and still has boots. Make sure they have the
				// enchantment.
				// If they don't remove the player from the list and re-set the step height to
				// the difference between
				// the old step height and the new step height.
				boolean foundStepAssist = false;
				StepAssistInfo info = ClientEventHandler.playerStepAssists.get(player.getName());

				for (Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(bootsStack).entrySet()) {
					if (entry.getKey() instanceof EnchantmentStepAssist) {
						// Found the step assist, create the info and update the player's step height
						// based on level.
						if (entry.getValue() != info.enchantmentLevel) {
							// Adjust the step height because the item changed.
							float newStepHeightAdjustment = (entry.getValue() == 1 ? 1.0F
									: entry.getValue() == 2 ? 1.5F : 2.0F);
							float oldStepHeightAdjustment = (info.enchantmentLevel == 1 ? 1.0F
									: info.enchantmentLevel == 2 ? 1.5F : 2.0F);
							float stepHeightAdjustment = oldStepHeightAdjustment - newStepHeightAdjustment;
							player.stepHeight = player.stepHeight - stepHeightAdjustment;

							// If the player's step height is now greater than what the enchantment allows,
							// set it to
							// the maximum the enchantment allows
							if (player.stepHeight > (entry.getValue() == 1 ? 1.0F
									: entry.getValue() == 2 ? 1.5F : 2.0F)) {
								player.stepHeight = entry.getValue() == 1 ? 1.0F : entry.getValue() == 2 ? 1.5F : 2.0F;
							}

							info.enchantmentLevel = entry.getValue();
							info.newStepHeight = player.stepHeight;

							if (player.stepHeight < 0.0F) {
								player.stepHeight = 0.0F;
								ClientEventHandler.playerStepAssists.remove(player.getName());
							}
						}

						foundStepAssist = true;
						break;
					}
				}

				if (!foundStepAssist) {
					player.stepHeight = info.newStepHeight - info.oldStepHeight;

					// Make sure the player cannot get stuck.
					if (player.stepHeight < 0.0F) {
						player.stepHeight = 0.0F;
					}

					ClientEventHandler.playerStepAssists.remove(player.getName());
				}
			} else if (!ClientEventHandler.playerStepAssists.containsKey(player.getName()) && bootsStack != null
					&& bootsStack.isEnchanted()) {
				// The player has equipped enchanted boots.
				for (Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(bootsStack).entrySet()) {
					if (entry.getKey() instanceof EnchantmentStepAssist) {
						// Found the step assist, create the info and update the player's step height
						// based on level.
						StepAssistInfo info = new StepAssistInfo();
						info.oldStepHeight = player.stepHeight;
						info.enchantmentLevel = entry.getValue();

						// Get the adjusted step height to determine if we need to change it.
						float adjustedStepHeight = player.stepHeight
								- (entry.getValue() == 1 ? 1.0F : entry.getValue() == 2 ? 1.5F : 2.0F);
						info.newStepHeight = player.stepHeight;

						if (adjustedStepHeight < 0.0F) {
							adjustedStepHeight = adjustedStepHeight * -1;

							info.newStepHeight = player.stepHeight + adjustedStepHeight;

							// If the new step height would be greater than the maximum step height for this
							// level, set
							// it to the maximum step height.
							if (info.newStepHeight > (entry.getValue() == 1 ? 1.0F
									: entry.getValue() == 2 ? 1.5F : 2.0F)) {
								info.newStepHeight = entry.getValue() == 1 ? 1.0F : entry.getValue() == 2 ? 1.5F : 2.0F;
							}

							player.stepHeight = info.newStepHeight;
						}

						ClientEventHandler.playerStepAssists.put(player.getName().getFormattedText(), info);

						break;
					}
				}
			}
		}
	}

	private static void renderHotbarItem(Minecraft mc, int x, int y, float z, PlayerEntity player, ItemStack stack) {
		if (!stack.isEmpty()) {
			float f = (float) stack.getAnimationsToGo() - z;

			if (f > 0.0F) {
				GlStateManager.pushMatrix();
				float f1 = 1.0F + f / 5.0F;
				GlStateManager.translatef((float) (x + 8), (float) (y + 12), 0.0F);
				GlStateManager.scalef(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
				GlStateManager.translatef((float) (-(x + 8)), (float) (-(y + 12)), 0.0F);
			}

			mc.getItemRenderer().renderItemAndEffectIntoGUI(player, stack, x, y);

			if (f > 0.0F) {
				GlStateManager.popMatrix();
			}

			mc.getItemRenderer().renderItemOverlays(mc.fontRenderer, stack, x, y);
		}
	}

	/**
	 * This class is used to hold information stored about a player's step assist.
	 * 
	 * @author WuestMan
	 *
	 */
	public static class StepAssistInfo {
		public float oldStepHeight = 0.0F;
		public float newStepHeight = 0.0F;
		public int enchantmentLevel = 0;
	}
}

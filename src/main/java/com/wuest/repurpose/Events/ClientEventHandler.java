package com.wuest.repurpose.Events;

import java.awt.Color;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.Multimap;
import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Blocks.BlockCoffer;
import com.wuest.repurpose.Blocks.BlockCoffer.IronChestType;
import com.wuest.repurpose.Enchantment.EnchantmentStepAssist;
import com.wuest.repurpose.Items.ItemBlockCoffer;
import com.wuest.repurpose.Items.ItemStoneShears;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

/**
 * This class is used to handle client side only events.
 * @author WuestMan
 *
 */
@EventBusSubscriber(value = {Side.CLIENT })
public class ClientEventHandler
{
	public static LocalDateTime bedCompassTime;
	public static BlockPos bedLocation;
	private static final int BUFF_ICON_SIZE = 18;
	private static final int BUFF_ICON_SPACING = BUFF_ICON_SIZE + 5; // 2 pixels between buff icons
	private static final int BUFF_ICON_BASE_U_OFFSET = 0;
	private static final int BUFF_ICON_BASE_V_OFFSET = 198;
	private static final int BUFF_ICONS_OFFSET = 8;
	
	private static HashMap<String, StepAssistInfo> playerStepAssists = new HashMap<String, StepAssistInfo>();
	
	/**
	 * This event is called by GuiIngameForge during each frame by GuiIngameForge.pre() and GuiIngameForce.post().
	 * @param event
	 */
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onRenderExperienceBar(RenderGameOverlayEvent event)
	{
		// We draw after the ExperienceBar has drawn.  The event raised by GuiIngameForge.pre()
		// will return true from isCancelable.  If you call event.setCanceled(true) in
		// that case, the portion of rendering which this event represents will be canceled.
		// We want to draw *after* the experience bar is drawn, so we make sure isCancelable() returns
		// false and that the eventType represents the ExperienceBar event.
		if(event.isCancelable() || event.getType() != ElementType.EXPERIENCE)
		{      
			return;
		}
		
		ClientEventHandler.ShowPlayerBed(event);
	}
	
	@SubscribeEvent
	public static void PlayerTickEvent(TickEvent.PlayerTickEvent event)
	{
		if (event.side.isClient())
		{
			if (event.phase == Phase.START)
			{
				ClientEventHandler.setStepHeight(event);
				
				if (Repurpose.proxy.getServerConfiguration().enableMobileLight)
				{
					ClientEventHandler.setPlayerLight(event);
				}
			}
		}
	}
	
	private static void ShowPlayerBed(RenderGameOverlayEvent event)
	{
		if (ClientEventHandler.bedCompassTime != null)
		{
			Minecraft mc = Minecraft.getMinecraft();
			EntityPlayer player = mc.player;
			long timeBetween = java.time.temporal.ChronoUnit.SECONDS.between(ClientEventHandler.bedCompassTime, LocalDateTime.now()); 
			
			// Do drawing logic here.
			int x = event.getResolution().getScaledWidth() / 4;
			int y = event.getResolution().getScaledHeight() / 2 - 23;
			BlockPos playerPosition = player.getPosition();
			
			if (ClientEventHandler.bedLocation != null)
			{
				// If yOffset is positive, the player is higher than the bed, if it's negative the player is lower than the bed.
				int yOffSet = playerPosition.getY() - ClientEventHandler.bedLocation.getY();
				
				// X = East/West, west being less than 0 and east being greater than 0.
				// If the offset is greater than 0 the player is east of the bed, otherwise the player is west of the bed.
				int xOffSet = playerPosition.getX() - ClientEventHandler.bedLocation.getX();
				
				// Z = North/South. North being less than 0 and south being greater than 0.
				// If the offset is greater than 0 then the player is south of the bed, otherwise the player is north of the bed.
				int zOffSet = playerPosition.getZ() - ClientEventHandler.bedLocation.getZ();
				
				Minecraft.getMinecraft().fontRenderer.drawString("Your bed is...", x, y, Color.WHITE.getRGB());
				
				y = y + 10;
				
				if (yOffSet > 0)
				{
					Minecraft.getMinecraft().fontRenderer.drawString(((Integer)yOffSet).toString() + " Block(s) Lower", x, y, (new Color(200, 117, 51).getRGB()));
				}
				else if (yOffSet < 0)
				{
					Minecraft.getMinecraft().fontRenderer.drawString(((Integer)Math.abs(yOffSet)).toString() + " Block(s) Higher", x, y, (new Color(52,221,221).getRGB()));
				}
				else
				{
					y = y - 10;
				}
				
				y = y + 10;
				
				if (xOffSet > 0)
				{
					Minecraft.getMinecraft().fontRenderer.drawString(((Integer)xOffSet).toString() + " Block(s) West", x, y, (new Color(207,83,0).getRGB()));
				}
				else if (xOffSet < 0)
				{
					Minecraft.getMinecraft().fontRenderer.drawString(((Integer)Math.abs(xOffSet)).toString() + " Block(s) East", x, y, (new Color(255,204,0).getRGB()));
				}
				else
				{
					y = y - 10;
				}
				
				y = y + 10;
				
				if (zOffSet > 0)
				{
					Minecraft.getMinecraft().fontRenderer.drawString(((Integer)zOffSet).toString() + " Block(s) North", x, y, (new Color(204,204,255).getRGB()));
				}
				else if (zOffSet < 0)
				{
					Minecraft.getMinecraft().fontRenderer.drawString(((Integer)Math.abs(zOffSet)).toString() + " Block(s) South", x, y, (new Color(91,194,54).getRGB()));
				}
				else
				{
					y = y - 10;
				}
				
				y = y + 10;
				
				if (xOffSet == 0 && yOffSet == 0 && zOffSet == 0)
				{
					Minecraft.getMinecraft().fontRenderer.drawString("Right next to you", x, y, Color.WHITE.getRGB());
				}
				else
				{
					Minecraft.getMinecraft().fontRenderer.drawString("Of you", x, y, Color.WHITE.getRGB());
				}
			}
			else
			{
				// Send a chat to the user that their bed was not found.
				mc.player.sendChatMessage("Bed Not Found");
				timeBetween = 99999999;
			}
			
			if (timeBetween > 8)
			{
				ClientEventHandler.bedCompassTime = null;
			}
		}
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		for (Block block : ModRegistry.ModBlocks)
		{
			if (!(block instanceof BlockCoffer))
			{
				ClientEventHandler.regBlock(block);
			}
		}
		
		for (Item item : ModRegistry.ModItems)
		{
			if (!(item instanceof ItemBlockCoffer))
			{
				ClientEventHandler.regItem(item);
			}
			else
			{
				for (IronChestType type : IronChestType.values())
	            {
					ModelLoader.setCustomModelResourceLocation(item, type.ordinal(), new ModelResourceLocation(item.getRegistryName(), "variant=" + type.getName()));
	            }
			}
		}
	}
	
	/**
	 * Registers an item to be rendered. This is needed for textures.
	 * @param item The item to register.
	 */
	public static void regItem(Item item) 
	{
		ClientEventHandler.regItem(item, 0, item.getUnlocalizedName().substring(5));
	}
	
	/**
	 * Registers an item to be rendered. This is needed for textures.
	 * @param item The item to register.
	 * @param metaData The meta data for the item to register.
	 * @param blockName the name of the block.
	 */
	public static void regItem(Item item, int metaData, String blockName)
	{
		ModelResourceLocation location = new ModelResourceLocation(blockName, "inventory");
		//System.out.println("Registering Item: " + location.getResourceDomain() + "[" + location.getResourcePath() + "]");

		ModelLoader.setCustomModelResourceLocation(item, metaData, location);
	}

	/**
	 * Registers a block to be rendered. This is needed for textures.
	 * @param block The block to register.
	 */
	public static void regBlock(Block block)
	{
		NonNullList<ItemStack> stacks = NonNullList.create();
		
		Item itemBlock = Item.getItemFromBlock(block);
		
		// If there are sub-blocks for this block, register each of them.
		block.getSubBlocks(null, stacks);
		
		if (itemBlock != null)
		{
			if (stacks.size() > 0)
			{
				for (ItemStack stack : stacks)
				{
					Block subBlock = block.getStateFromMeta(stack.getMetadata()).getBlock();
					String name = subBlock.getRegistryName().toString();
					
					ClientEventHandler.regItem(stack.getItem(), stack.getMetadata(), name);
				}
			}
			else
			{
				ClientEventHandler.regItem(itemBlock);
			}
		}
	}
	
	/**
	 * This method is used to create light around the player when they are holding a light-source block.
	 * @param event The Player Tick Event
	 */
	private static void setPlayerLight(TickEvent.PlayerTickEvent event)
	{
		World world = event.player.world;
		EntityPlayer player = event.player;
		BlockPos originalPos = new BlockPos(player.posX, player.posY + 1, player.posZ);
		BlockPos pos = new BlockPos(player.posX, player.posY + 1, player.posZ);
		BlockPos prevPos = pos.north(2).east(2).up(3);
		pos = pos.south(1).west(2).down(3);
		
		if (!event.player.isDead)
		{
			ItemStack mainHandStack = player.getHeldItemMainhand();
			ItemStack offHandStack = player.getHeldItemOffhand();
			
			if (mainHandStack != ItemStack.EMPTY
				|| mainHandStack != ItemStack.EMPTY)
			{
				Block mainBlock = null;
				Block offHandBlock = null;
				boolean foundLightBlock = false;
				
				if (mainHandStack != ItemStack.EMPTY)
				{
					mainBlock = Block.getBlockFromItem(mainHandStack.getItem());
					
					if (mainBlock != null && mainBlock.getLightValue(mainBlock.getDefaultState()) > 0)
					{
						foundLightBlock = true;
					}
				}
				
				if (offHandStack != ItemStack.EMPTY && !foundLightBlock)
				{
					offHandBlock = Block.getBlockFromItem(offHandStack.getItem());
					
					if (offHandBlock != null && offHandBlock.getLightValue(mainBlock.getDefaultState()) > 0)
					{
						foundLightBlock = true;
					}
				}
				
				if (foundLightBlock)
				{
					world.setLightFor(EnumSkyBlock.BLOCK, originalPos, 12);
					//System.out.println("Light Level: " + ((Integer)world.getLightFor(EnumSkyBlock.BLOCK, pos)).toString());
				}
				else
				{
					world.setLightFor(EnumSkyBlock.BLOCK, originalPos, 0);
				}
			}
			
			world.markBlockRangeForRenderUpdate(originalPos.getX(), originalPos.getY(), originalPos.getZ(), 12, 12, 12);
			
			world.notifyBlockUpdate(originalPos, world.getBlockState(originalPos), world.getBlockState(originalPos), 3);

			for (BlockPos otherPos : BlockPos.getAllInBox(prevPos, pos))
			{
				// Don't update for the current position.
				if (originalPos.getX() == otherPos.getX() && originalPos.getY() == otherPos.getY() && originalPos.getZ() == otherPos.getZ())
				{
					continue;
				}
				else
				{
					world.checkLightFor(EnumSkyBlock.BLOCK, otherPos);
				}
			}
		}
		else
		{
			world.setLightFor(EnumSkyBlock.BLOCK, originalPos, 0);
			world.checkLightFor(EnumSkyBlock.BLOCK, originalPos);
		}
	}

	
	private static void setStepHeight(TickEvent.PlayerTickEvent event)
	{
		EntityPlayer player = event.player;
		ItemStack bootsStack = player.inventory.armorInventory.get(0);
		
		// Check to see if the player is wearing a pair of enchanted boots with step assist.
		// Check to see if the player was added to the hashset and the game setting for auto-jump was enabled.
		// If it was, re-set their step height to the original step height and remove them from the hashset.
		if (ClientEventHandler.playerStepAssists.containsKey(player.getName()) && (bootsStack == null || !bootsStack.isItemEnchanted() || Minecraft.getMinecraft().gameSettings.autoJump))
		{
			// Reset the player step height to the original step height and remove this record from the hashset.
			StepAssistInfo info = ClientEventHandler.playerStepAssists.get(player.getName());
			player.stepHeight = info.oldStepHeight;
			ClientEventHandler.playerStepAssists.remove(player.getName());
			return;
		}
		
		// Don't bother adding them to the hashset if auto-jump is enabled.
		// On the tick after re-setting the player's step height, check to see if the the enchantment is even enabled in the configuration.
		if (!Minecraft.getMinecraft().gameSettings.autoJump && Repurpose.proxy.getServerConfiguration().enableStepAssistEnchantment) 
		{
			if (ClientEventHandler.playerStepAssists.containsKey(player.getName()) && bootsStack != null && bootsStack.isItemEnchanted())
			{
				// The player was in the list and still has boots. Make sure they have the enchantment. 
				// If they don't remove the player from the list and re-set the step height to the difference between the old step height and the new step height.
				boolean foundStepAssist = false;
				StepAssistInfo info = ClientEventHandler.playerStepAssists.get(player.getName());
				
				for (Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(bootsStack).entrySet())
				{
					if (entry.getKey() instanceof EnchantmentStepAssist)
					{
						// Found the step assist, create the info and update the player's step height based on level.
						if (entry.getValue() != info.enchantmentLevel)
						{
							// Adjust the step height because the item changed.
							float newStepHeightAdjustment = (entry.getValue() == 1 ? 1.0F : entry.getValue() == 2 ? 1.5F : 2.0F);
							float oldStepHeightAdjustment = (info.enchantmentLevel == 1 ? 1.0F : info.enchantmentLevel == 2 ? 1.5F : 2.0F);
							float stepHeightAdjustment = oldStepHeightAdjustment - newStepHeightAdjustment;
							player.stepHeight = player.stepHeight - stepHeightAdjustment;
							
							// If the player's step height is now greater than what the enchantment allows, set it to the maximum the enchantment allows
							if (player.stepHeight > (entry.getValue() == 1 ? 1.0F : entry.getValue() == 2 ? 1.5F : 2.0F))
							{
								player.stepHeight = entry.getValue() == 1 ? 1.0F : entry.getValue() == 2 ? 1.5F : 2.0F;
							}
							
							info.enchantmentLevel = entry.getValue();
							info.newStepHeight = player.stepHeight;
							
							if (player.stepHeight < 0.0F)
							{
								player.stepHeight = 0.0F;
								ClientEventHandler.playerStepAssists.remove(player.getName());
							}
						}
						
						foundStepAssist = true;
						break;
					}
				}
				
				if (!foundStepAssist)
				{	
					player.stepHeight = info.newStepHeight - info.oldStepHeight;
					
					// Make sure the player cannot get stuck.
					if (player.stepHeight < 0.0F)
					{
						player.stepHeight = 0.0F;
					}
					
					ClientEventHandler.playerStepAssists.remove(player.getName());
				}
			}
			else if (!ClientEventHandler.playerStepAssists.containsKey(player.getName()) && bootsStack != null && bootsStack.isItemEnchanted())
			{
				// The player has equipped enchanted boots.
				for (Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(bootsStack).entrySet())
				{
					if (entry.getKey() instanceof EnchantmentStepAssist)
					{
						// Found the step assist, create the info and update the player's step height based on level.
						StepAssistInfo info = new StepAssistInfo();
						info.oldStepHeight = player.stepHeight;
						info.enchantmentLevel = entry.getValue();
						
						// Get the adjusted step height to determine if we need to change it.
						float adjustedStepHeight = player.stepHeight - (entry.getValue() == 1 ? 1.0F : entry.getValue() == 2 ? 1.5F : 2.0F);
						info.newStepHeight = player.stepHeight;
						
						if (adjustedStepHeight < 0.0F)
						{
							adjustedStepHeight = adjustedStepHeight * -1;
							
							info.newStepHeight = player.stepHeight + adjustedStepHeight;
							
							// If the new step height would be greater than the maximum step height for this level, set it to the maximum step height.
							if (info.newStepHeight > (entry.getValue() == 1 ? 1.0F : entry.getValue() == 2 ? 1.5F : 2.0F))
							{
								info.newStepHeight = entry.getValue() == 1 ? 1.0F : entry.getValue() == 2 ? 1.5F : 2.0F;
							}
							
							player.stepHeight = info.newStepHeight;
						}
						
						ClientEventHandler.playerStepAssists.put(player.getName(), info);
						
						break;
					}
				}
			}
		}
	}
	
	/**
	 * This class is used to hold information stored about a player's step assist.
	 * @author WuestMan
	 *
	 */
	public static class StepAssistInfo
	{
		public float oldStepHeight = 0.0F;
		public float newStepHeight = 0.0F;
		public int enchantmentLevel = 0;
	}
}

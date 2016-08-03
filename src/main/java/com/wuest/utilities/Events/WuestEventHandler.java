package com.wuest.utilities.Events;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.wuest.utilities.ModRegistry;
import com.wuest.utilities.UpdateChecker;
import com.wuest.utilities.WuestUtilities;
import com.wuest.utilities.Config.WuestConfiguration;
import com.wuest.utilities.Items.ItemDiamondShard;
import com.wuest.utilities.Items.ItemFluffyFabric;
import com.wuest.utilities.Items.ItemSnorkel;
import com.wuest.utilities.Items.ItemSwiftBlade;
import com.wuest.utilities.Items.ItemWhetStone;
import com.wuest.utilities.Proxy.Messages.BedLocationMessage;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBeetroot;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class WuestEventHandler
{
	private static HashMap<String, BlockPos> playerBedLocation;

	@SubscribeEvent
	public void PlayerJoinedWorld(EntityJoinWorldEvent event)
	{
		if (event.getWorld().isRemote && event.getEntity() instanceof EntityPlayer)
		{
			// Show a message to this player if their version is old.
			if (UpdateChecker.showMessage)
			{
				((EntityPlayer)event.getEntity()).addChatMessage(new TextComponentString(UpdateChecker.messageToShow));
			}
		}
	}
	
	@SubscribeEvent(receiveCanceled = true)
	public void PlayerRightClicked(PlayerInteractEvent event)
	{
		// This only happens during the right-click event.
		// Can use the proxy's configuration.
		if (event.getHand() == EnumHand.OFF_HAND && WuestUtilities.proxy.proxyConfiguration.rightClickCropHarvest
				&& !event.getWorld().isRemote
				&& !event.isCanceled())
		{
			EntityPlayer p = event.getEntityPlayer();

			ItemStack currentStack = p.inventory.getCurrentItem();

			if (currentStack != null)
			{
				Item currentItem = currentStack.getItem();
				ItemStack boneMealStack = new ItemStack(Items.DYE, 1, 15);
				Item boneMeal = boneMealStack.getItem();

				if (currentItem != null && currentItem == boneMeal)
				{
					return;
				}
			}

			IBlockState cropState = event.getWorld().getBlockState(event.getPos());
			Block crop = cropState.getBlock();
			PropertyInteger ageInteger = crop instanceof BlockBeetroot ? BlockBeetroot.BEETROOT_AGE : BlockCrops.AGE;
			int maxAge = crop instanceof BlockBeetroot ? 3 : 7;

			// Only re-plant when this is a fully grown plant.
			if (crop instanceof BlockCrops && (Integer)cropState.getValue(ageInteger) == maxAge)
			{
				// Get the farmland below the crop.
				BlockPos farmlandPosition = event.getPos().down();

				// Get the drops from this crop and add it to the inventory.
				List<ItemStack> drops = crop.getDrops(event.getWorld(), event.getPos(), cropState, 1);

				// Break the original crop block.
				event.getWorld().setBlockToAir(event.getPos());

				EnumActionResult replanted = EnumActionResult.FAIL;
				BlockCrops blockCrop = (BlockCrops)crop;
				IBlockState tempState = cropState.withProperty(ageInteger, 0);
				Item seed = blockCrop.getItemDropped(tempState, new Random(), 0);

				for (ItemStack drop : drops)
				{
					Item dropItem = drop.getItem();

					// Make sure this is the same class as the crop's seed.
					if (dropItem.getClass() == seed.getClass()
							&& replanted != EnumActionResult.PASS)
					{
						EnumFacing facing = event.getFace();
						replanted = drop.onItemUse(p, event.getWorld(), farmlandPosition, event.getHand(), EnumFacing.UP, 0, 0, 0);

						if (replanted == EnumActionResult.SUCCESS 
								|| replanted == EnumActionResult.PASS)
						{
							replanted = EnumActionResult.PASS;
							continue;
						}	
					}

					p.inventory.addItemStackToInventory(drop);
					p.inventoryContainer.detectAndSendChanges();
				}

				if (replanted != EnumActionResult.PASS)
				{
					// The only reason why we wouldn't have re-planted at this point is because the wheat didn't drop a seed. Check the player inventory for a seed and plant it.
					// This should work with other plants that override BlockCrops.GetItem with their own seed.
					// Make sure to re-set the age to 0 to get the seed.
					// Get the seed item and check to see if the player has this in their inventory. If they do we can use it to re-plant.
					ItemStack seeds = new ItemStack(seed);

					if (seeds != null && p.inventory.hasItemStack(seeds))
					{
						seeds.onItemUse(p, event.getWorld(), farmlandPosition, null, event.getFace(), 0, 0, 0);

						p.inventory.clearMatchingItems(seeds.getItem(), -1, 1, null);

						p.inventoryContainer.detectAndSendChanges();
					}
				}

				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void PlayerTickEvent(TickEvent.PlayerTickEvent event)
	{
		if (event.side.isServer())
		{
			// Send the player's actual bed location to the client for the bed compass object.
			// This is needed as the client doesn't properly store the bed location.
			this.sendPlayerBedLocation(event);
		}
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent onConfigChangedEvent)
	{
		if(onConfigChangedEvent.getModID().equals(WuestUtilities.MODID))
		{
			WuestConfiguration.syncConfig();
		}
	}

	/**
	 * This method is used to trigger the buildSword vanilla event when making swift blades.
	 * @param event The event object used for this method.
	 */
	@SubscribeEvent
	public void onCrafted(ItemCraftedEvent event)
	{
		Item craftedItem = event.crafting.getItem();
		EntityPlayer player = event.player;

		if (craftedItem == ModRegistry.SwiftBlade(ToolMaterial.WOOD) 
				|| craftedItem == ModRegistry.SwiftBlade(ToolMaterial.STONE)
				|| craftedItem == ModRegistry.SwiftBlade(ToolMaterial.IRON)
				|| craftedItem == ModRegistry.SwiftBlade(ToolMaterial.GOLD)
				|| craftedItem == ModRegistry.SwiftBlade(ToolMaterial.DIAMOND))
		{
			player.addStat(AchievementList.BUILD_SWORD);
		}
	}

	@SubscribeEvent
	public void AnvilUpdate(AnvilUpdateEvent event)
	{
		ItemStack rightItem = event.getRight();
		ItemStack leftItem = event.getLeft();
		
		if (rightItem.getItem() instanceof ItemBook)
		{
			ItemStack enchantedBook = null;
			
			// These items create enchanted books.
			if (leftItem.getItem() instanceof ItemFluffyFabric)
			{
				// Set the output to an enchanted book with the Silk Touch enchantment.
				enchantedBook = Items.ENCHANTED_BOOK.getEnchantedItemStack(new EnchantmentData(Enchantments.SILK_TOUCH, 1));
				event.setCost(3);
			}
			else if (leftItem.getItem() instanceof ItemWhetStone)
			{
				// Set the output to an enchanted book with the Sharpness 1 enchantment.
				enchantedBook = Items.ENCHANTED_BOOK.getEnchantedItemStack(new EnchantmentData(Enchantments.SHARPNESS, 1));
				event.setCost(1);
				
			}
			else if (leftItem.getItem() instanceof ItemSnorkel)
			{
				// Set the output to an enchanted book with water breathing 1 enchantment.
				enchantedBook = Items.ENCHANTED_BOOK.getEnchantedItemStack(new EnchantmentData(Enchantments.RESPIRATION, 1));
				event.setCost(2);
			}
			
			if (enchantedBook != null)
			{
				event.setOutput(enchantedBook);
			}
		}
	}
	
	@SubscribeEvent
	public void onDrops(HarvestDropsEvent event) 
	{
		Block block = event.getState().getBlock();
		
		// For coal ore, add a random chance that a diamond shard can drop.
		if (block == Blocks.COAL_ORE && !event.isCanceled() && !event.isSilkTouching())
		{ 
			double randomChance = event.getWorld().rand.nextDouble();
			BigDecimal bigDecimal = new BigDecimal(Double.toString(randomChance));
			bigDecimal = bigDecimal.setScale(3, RoundingMode.HALF_UP);
			randomChance = bigDecimal.doubleValue();
			int fortuneLevel = event.getFortuneLevel();
			double maxPercentage = 0.01;
			
			switch (fortuneLevel)
			{
				case 1:
				{
					maxPercentage = 0.015;
					break;
				}
				
				case 2:
				{
					maxPercentage = 0.02;
					break;
				}
				
				case 3:
				{
					maxPercentage = 0.025;
					break;
				}
				
				default:
				{
					maxPercentage = 0.01;
				}
			}
			
			if (randomChance <= maxPercentage)
			{
				event.getDrops().add(new ItemStack(ModRegistry.DiamondShard()));
			}
		}
	}
	
	private void sendPlayerBedLocation(TickEvent.PlayerTickEvent event)
	{
		if (WuestEventHandler.playerBedLocation == null)
		{
			WuestEventHandler.playerBedLocation = new HashMap<String, BlockPos>();
		}

		// Send the updated bed information to the client.
		BedLocationMessage message = new BedLocationMessage();
		NBTTagCompound tag = new NBTTagCompound();
		EntityPlayerMP player = (EntityPlayerMP) event.player;
		BlockPos bedPosition = player.getBedLocation();

		if (bedPosition != null)
		{
			tag.setInteger("bedX", bedPosition.getX());
			tag.setInteger("bedY", bedPosition.getY());
			tag.setInteger("bedZ", bedPosition.getZ());
		}

		message.setMessageTag(tag);
		BlockPos existingBedPosition = null;

		if (WuestEventHandler.playerBedLocation.containsKey(player.getName()))
		{
			existingBedPosition = WuestEventHandler.playerBedLocation.get(player.getName());
		}
		else
		{
			WuestEventHandler.playerBedLocation.put(player.getName(), bedPosition);
		}

		if (existingBedPosition != bedPosition)
		{
			// Only send the message to the client if the bed position changes.
			WuestUtilities.network.sendTo(message, player);
		}
	}
}
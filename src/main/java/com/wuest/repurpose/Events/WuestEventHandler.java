package com.wuest.repurpose.Events;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;

import com.google.common.collect.Multimap;
import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Base.ItemBlockCapability;
import com.wuest.repurpose.Base.TileEntityBase;
import com.wuest.repurpose.Capabilities.BlockModelCapability;
import com.wuest.repurpose.Capabilities.BlockModelProvider;
import com.wuest.repurpose.Capabilities.DimensionHome;
import com.wuest.repurpose.Capabilities.DimensionHomeProvider;
import com.wuest.repurpose.Capabilities.IDimensionHome;
import com.wuest.repurpose.Config.WuestConfiguration;
import com.wuest.repurpose.Items.ItemFluffyFabric;
import com.wuest.repurpose.Items.ItemSnorkel;
import com.wuest.repurpose.Items.ItemStoneShears;
import com.wuest.repurpose.Items.ItemWhetStone;
import com.wuest.repurpose.Proxy.ClientProxy;
import com.wuest.repurpose.Proxy.Messages.BedLocationMessage;
import com.wuest.repurpose.Proxy.Messages.ConfigSyncMessage;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBeetroot;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockStone;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(value = {Side.SERVER, Side.CLIENT })
public class WuestEventHandler
{
	private static HashMap<String, BlockPos> playerBedLocation;
	
	private static HashMap<String, Integer> playerExistedTicks = new HashMap<String, Integer>();
	
	@SubscribeEvent
	public static void equipmentChangeEvent(LivingEquipmentChangeEvent event)
	{
		if (event.getEntity() instanceof EntityPlayer && event.getSlot() == EntityEquipmentSlot.MAINHAND)
		{
			// This is still here to remove old attack modifiers which were incorrectly added.
			Multimap<String, AttributeModifier> modifiers = event.getTo()
					.getAttributeModifiers(event.getSlot());

				WuestEventHandler.removeAttackModifiers(event.getTo());
		}
	}
	
	@SubscribeEvent
	public static void onPlayerLoginEvent(PlayerLoggedInEvent event)
	{
		if(!event.player.world.isRemote)
		{
			NBTTagCompound tag = Repurpose.proxy.proxyConfiguration.ToNBTTagCompound();
			Repurpose.network.sendTo(new ConfigSyncMessage(tag), (EntityPlayerMP)event.player);
			System.out.println("Sent config to '" + event.player.getDisplayNameString() + ".'");		
			
			if (!Repurpose.proxy.proxyConfiguration.enableSwiftCombat)
			{
				// Go through the player's inventory and remove the NBTTags.
				for (ItemStack stack : event.player.inventory.mainInventory)
				{
			       WuestEventHandler.removeAttackModifiers(stack);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void OnClientDisconnectEvent(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
	{
		// When the player logs out, make sure to re-set the server configuration. 
		// This is so a new configuration can be successfully loaded when they switch servers or worlds (on single player.
		((ClientProxy)Repurpose.proxy).serverConfiguration = null;
	}
	
	@SubscribeEvent
	public static void AttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event)
	{
		// Only attach for players.
		if (event.getObject() instanceof EntityPlayer)
		{
			event.addCapability(new ResourceLocation(Repurpose.MODID, "DimensionHome"), new DimensionHomeProvider(event.getObject(), new DimensionHome()));
		}
	}
	
	@SubscribeEvent
	public static void AttachItemStackCapabilities(AttachCapabilitiesEvent<ItemStack> event)
	{
		if (event.getObject().getItem() instanceof ItemBlockCapability
				&& ((ItemBlockCapability)event.getObject().getItem()).getAllowedCapabilities().contains(ModRegistry.BlockModel))
		{
			event.addCapability(new ResourceLocation(Repurpose.MODID, "BlockModel"), new BlockModelProvider(new BlockModelCapability()));
		}
	}
	
	@SubscribeEvent
	public static void AttachTileEntityCapabilities(AttachCapabilitiesEvent<TileEntity> event)
	{
		if (event.getObject() instanceof TileEntityBase)
		{
			ArrayList<Capability> allowedCapabilities = ((TileEntityBase)event.getObject()).getAllowedCapabilities();
			
			if (allowedCapabilities.contains(ModRegistry.BlockModel))
			{
				event.addCapability(new ResourceLocation(Repurpose.MODID, "BlockModel"), new BlockModelProvider(new BlockModelCapability()));
			}
		}
	}
	
	@SubscribeEvent
	public static void PlayerChangedDimension(PlayerChangedDimensionEvent event)
	{
		IDimensionHome dimensionHome = event.player.getCapability(ModRegistry.DimensionHomes, null);
		
		if (dimensionHome != null)
		{
			dimensionHome.setHomePosition(event.toDim, event.player.getPosition());
		}
	}
	
	@SubscribeEvent
	public static void PlayerCloned(PlayerEvent.Clone event)
	{
		if (event.isWasDeath())
		{
			// While not necessary for Vanilla, a mod could change where a player spawns when they die, so copy over the capabilities.
			EntityPlayer original = event.getOriginal();
			EntityPlayer newPlayer = event.getEntityPlayer();
			
			IDimensionHome originalDimensionHome = original.getCapability(ModRegistry.DimensionHomes, null);
			IDimensionHome newDimensionHome = newPlayer.getCapability(ModRegistry.DimensionHomes, null);
			
			// Transfer the dimensional information to the new player.
			newDimensionHome.Transfer(originalDimensionHome);
		}
	}
	
	@SubscribeEvent(receiveCanceled = true)
	public static void PlayerRightClicked(PlayerInteractEvent event)
	{
		// This only happens during the right-click event.
		// Can use the proxy's configuration.
		if (event.getHand() == EnumHand.OFF_HAND && Repurpose.proxy.proxyConfiguration.rightClickCropHarvest
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
			
			// Only re-plant when this is a fully grown plant.
			if (crop instanceof BlockCrops || crop instanceof BlockBush)
			{
				boolean cropIsMaxAge = false;
				
				// Look for a specific property called "age". All vanilla minecraft crops use this name for their property and most other mods do to.
				for (IProperty property : cropState.getPropertyKeys())
				{
					if (property.getName().toLowerCase().equals("age") && property instanceof PropertyInteger)
					{
						// Found the age property, get the max age.
						ageInteger = (PropertyInteger)property;
						Optional<Integer> tempMax = ageInteger.getAllowedValues().stream().max(Integer::compare);
						
						if (tempMax.isPresent())
						{
							int maxAge = tempMax.get();
							cropIsMaxAge = cropState.getValue(ageInteger) == maxAge;
						}
						
						break;
					}
				}
				
				if (cropIsMaxAge)
				{
					// Get the farmland below the crop.
					BlockPos farmlandPosition = event.getPos().down();
	
					// Get the drops from this crop and add it to the inventory.
					List<ItemStack> drops = crop.getDrops(event.getWorld(), event.getPos(), cropState, 1);
	
					// Break the original crop block.
					event.getWorld().setBlockToAir(event.getPos());
	
					EnumActionResult replanted = EnumActionResult.FAIL;
					IBlockState tempState = cropState.withProperty(ageInteger, 0);
					Item seed = crop.getItemDropped(tempState, new Random(), 0);
	
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
	}

	@SubscribeEvent
	public static void PlayerTickEvent(TickEvent.PlayerTickEvent event)
	{
		if (event.side.isServer())
		{
			// Send the player's actual bed location to the client for the bed compass object.
			// This is needed as the client doesn't properly store the bed location.
			WuestEventHandler.sendPlayerBedLocation(event);
		}
		
		//this.generatePlayerParticles(event);
		
		//this.setPlayerLight(event);
	}

	@SubscribeEvent
	public static void BedrockGeneration(PopulateChunkEvent.Pre event)
	{
		if (!event.getWorld().isRemote && Repurpose.proxy.getServerConfiguration().enableFlatBedrockGeneration) 
		{
			int dimension = event.getWorld().provider.getDimension();
			
			// For the overworld make sure the bedrock is flat.
			if (dimension == 0)
			{
				Chunk chunk = event.getWorld().getChunkFromChunkCoords(event.getChunkX(), event.getChunkZ());
				
				if (chunk != null)
				{
					for (ExtendedBlockStorage storage : chunk
							.getBlockStorageArray())
					{
						if (storage != null)
						{
							for (int x = 0; x < 16; x++)
							{
								for (int z = 0; z < 16; z++)
								{
									for (int y = 1; y < 16; y++)
									{
										if (storage.get(x, y, z).equals(Blocks.BEDROCK.getDefaultState()))
										{
											storage.set(x, y, z, Blocks.STONE.getDefaultState());
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

/*	@SubscribeEvent
	public void TextureStitch(TextureStitchEvent.Pre preEvent)
	{
		// This is how I could register a sprite texture for use in particles and other things.
		// preEvent.getMap().registerSprite(new ResourceLocation(Repurpose.MODID, ""));
	}*/

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent onConfigChangedEvent)
	{
		if(onConfigChangedEvent.getModID().equals(Repurpose.MODID))
		{
			WuestConfiguration.syncConfig();
		}
	}

	@SubscribeEvent
	public static void AnvilUpdate(AnvilUpdateEvent event)
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
				enchantedBook = ((ItemEnchantedBook)Items.ENCHANTED_BOOK).getEnchantedItemStack(new EnchantmentData(Enchantments.SILK_TOUCH, 1));
				event.setCost(3);
			}
			else if (leftItem.getItem() instanceof ItemWhetStone)
			{
				// Set the output to an enchanted book with the Sharpness 1 enchantment.
				enchantedBook = ((ItemEnchantedBook)Items.ENCHANTED_BOOK).getEnchantedItemStack(new EnchantmentData(Enchantments.SHARPNESS, 1));
				event.setCost(1);
				
			}
			else if (leftItem.getItem() instanceof ItemSnorkel)
			{
				// Set the output to an enchanted book with water breathing 1 enchantment.
				enchantedBook = ((ItemEnchantedBook)Items.ENCHANTED_BOOK).getEnchantedItemStack(new EnchantmentData(Enchantments.RESPIRATION, 1));
				event.setCost(2);
			}
			
			if (enchantedBook != null)
			{
				event.setOutput(enchantedBook);
			}
		}
		else if ((rightItem.getItem() instanceof ItemArmor && ((ItemArmor)rightItem.getItem()).armorType == EntityEquipmentSlot.FEET && leftItem.getItem() instanceof ItemFluffyFabric)
				|| (leftItem.getItem() instanceof ItemArmor && ((ItemArmor)leftItem.getItem()).armorType == EntityEquipmentSlot.FEET && rightItem.getItem() instanceof ItemFluffyFabric))
		{
			ItemArmor itemType = rightItem.getItem() instanceof ItemArmor ? (ItemArmor)rightItem.getItem() : (ItemArmor)leftItem.getItem(); 
			ItemStack result = new ItemStack((Item)Item.REGISTRY.getObject(itemType.getRegistryName()));

			if (result != null)
			{
				result.addEnchantment(ModRegistry.StepAssist(), 1);
				event.setCost(2);
			}
			
			event.setOutput(result);
		}
	}
	
	@SubscribeEvent
	public static void onDrops(HarvestDropsEvent event) 
	{
		Block block = event.getState().getBlock();
		
		if (!event.isCanceled() && !event.isSilkTouching())
		{
			// Get the random chance.
			double maxPercentage = 0.01;
			
			// For coal ore, add a random chance that a diamond shard can drop.
			if (block == Blocks.COAL_ORE)
			{ 
				int fortuneLevel = event.getFortuneLevel();
				
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
				
				WuestEventHandler.checkChanceAndAddToDrops(event.getWorld(), event.getDrops(), maxPercentage, ModRegistry.DiamondShard(), 1);
			}
			else if (block instanceof BlockLeaves && Repurpose.proxy.proxyConfiguration.enableAppleStickExtraDrops)
			{
				// Chance to drop apples.
				maxPercentage = 0.04;
				WuestEventHandler.checkChanceAndAddToDrops(event.getWorld(), event.getDrops(), maxPercentage, Items.APPLE, 1);
				
				// Chance to drop sticks.
				maxPercentage = 0.06;
				WuestEventHandler.checkChanceAndAddToDrops(event.getWorld(), event.getDrops(), maxPercentage, Items.STICK, 1);
			}
			else if ((block instanceof BlockDirt || block instanceof BlockGrass) && Repurpose.proxy.proxyConfiguration.enableExtraDropsFromDirt)
			{
				// Check for chance of drop for carrots, potatoes, beetroots and bones.
				maxPercentage = 0.04;
				
				WuestEventHandler.checkChanceAndAddToDrops(event.getWorld(), event.getDrops(), maxPercentage, Items.CARROT, 1);
				WuestEventHandler.checkChanceAndAddToDrops(event.getWorld(), event.getDrops(), maxPercentage, Items.POTATO, 1);
				WuestEventHandler.checkChanceAndAddToDrops(event.getWorld(), event.getDrops(), maxPercentage, Items.BEETROOT, 1);
				WuestEventHandler.checkChanceAndAddToDrops(event.getWorld(), event.getDrops(), maxPercentage, Items.BONE, 1);
				WuestEventHandler.checkChanceAndAddToDrops(event.getWorld(), event.getDrops(), maxPercentage, Items.CLAY_BALL, 1);
			}
			else if (block instanceof BlockStone && Repurpose.proxy.proxyConfiguration.enableExtraDropsFromStone)
			{
				maxPercentage = 0.04;
				WuestEventHandler.checkChanceAndAddToDrops(event.getWorld(), event.getDrops(), maxPercentage, Items.COAL, 1);
				WuestEventHandler.checkChanceAndAddToDrops(event.getWorld(), event.getDrops(), maxPercentage, Items.IRON_NUGGET, 1);
				WuestEventHandler.checkChanceAndAddToDrops(event.getWorld(), event.getDrops(), maxPercentage, Items.FLINT, 1);
				
				maxPercentage = 0.02;
				WuestEventHandler.checkChanceAndAddToDrops(event.getWorld(), event.getDrops(), maxPercentage, Items.GOLD_NUGGET, 1);
			}
		}
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{	
		event.getRegistry().registerAll(ModRegistry.ModBlocks.toArray(new Block[ModRegistry.ModBlocks.size()]));
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{	
		event.getRegistry().registerAll(ModRegistry.ModItems.toArray(new Item[ModRegistry.ModItems.size()]));
	}
	
	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
	{		
		// Register the ore dictionary blocks.
		ModRegistry.RegisterRecipes();
		
		ModRegistry.RegisterOreDictionaryRecords();
	}
	
	private static void sendPlayerBedLocation(TickEvent.PlayerTickEvent event)
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
			Repurpose.network.sendTo(message, player);
		}
	}
	
	private static void setPlayerLight(TickEvent.PlayerTickEvent event)
	{
		World world = event.player.world;
		EntityPlayer player = event.player;
		BlockPos pos = new BlockPos(player.posX, player.posY + 1, player.posZ);
		BlockPos prevPos = new BlockPos(player.prevPosX, player.prevPosY + 1, player.prevPosZ);
		
		
		if (!event.player.isDead)
		{
			world.setLightFor(EnumSkyBlock.BLOCK, pos, 15);
			//System.out.println("Light Level: " + ((Integer)world.getLightFor(EnumSkyBlock.BLOCK, pos)).toString());
			
			world.markBlockRangeForRenderUpdate(pos.getX(), pos.getY(), pos.getZ(), 12, 12, 12);
			
			world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);

			if (pos.getX() != prevPos.getX() && pos.getY() != prevPos.getY() && pos.getZ() != prevPos.getZ())
			{
				for (BlockPos otherPos : BlockPos.getAllInBox(prevPos, pos))
				{
					// Don't update for the current position.
					if (pos.getX() != otherPos.getX() && pos.getY() != otherPos.getY() && pos.getZ() != otherPos.getZ())
					{
						world.checkLightFor(EnumSkyBlock.BLOCK, otherPos);
					}
				}
			}
				
			for (int i = 1; i < 2; i++)
			{
				world.checkLightFor(EnumSkyBlock.BLOCK, pos.up(i));
				//world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos.up(i)), 3);
				
				world.checkLightFor(EnumSkyBlock.BLOCK, pos.down(i));
				//world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos.down(i)), 3);
				
				world.checkLightFor(EnumSkyBlock.BLOCK, pos.east(i));
				//world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos.east(i)), 3);
				
				world.checkLightFor(EnumSkyBlock.BLOCK, pos.south(i));
				//world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos.south(i)), 3);
				
				world.checkLightFor(EnumSkyBlock.BLOCK, pos.north(i));
				//world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos.north(i)), 3);
				
				world.checkLightFor(EnumSkyBlock.BLOCK, pos.west(i));
				//world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos.south(i)), 3);
			}
			
		}
		else
		{
			world.setLightFor(EnumSkyBlock.BLOCK, pos, 0);
		}
	}

	private static void generatePlayerParticles(TickEvent.PlayerTickEvent event)
	{
		EntityPlayer player = event.player;
		
		if (WuestEventHandler.playerExistedTicks.containsKey(player.getName()))
		{
			int ticks = WuestEventHandler.playerExistedTicks.get(player.getName());
			
			if (ticks % 20 == 0)
			{
			    Repurpose.proxy.generateParticles(player);
			    ticks = 0;
			}
			
			ticks++;
			WuestEventHandler.playerExistedTicks.put(player.getName(), ticks);
		}
		else
		{
			WuestEventHandler.playerExistedTicks.put(player.getName(), 0);
		}
		
	}

	private static void checkChanceAndAddToDrops(World world, List<ItemStack> drops, double maxPercentage, Item itemToDrop, int quantity)
	{
		double randomChance = WuestEventHandler.getRandomChance(world);
		
		if (randomChance <= maxPercentage)
		{
			drops.add(new ItemStack(itemToDrop, quantity));
		}
	}
	
	private static double getRandomChance(World world)
	{
		double randomChance = world.rand.nextDouble();
		BigDecimal bigDecimal = new BigDecimal(Double.toString(randomChance));
		bigDecimal = bigDecimal.setScale(3, RoundingMode.HALF_UP);
		return bigDecimal.doubleValue();
	}

	private static void removeAttackModifiers(ItemStack stack)
	{
		if (stack.getTagCompound() != null
				&& stack.getTagCompound().hasKey("AttributeModifiers"))
		{
			NBTTagList tagList = stack.getTagCompound().getTagList("AttributeModifiers", 10);
			ArrayList<Integer> indexesToRemove = new ArrayList<Integer>();
			
			// When this value is 2 then only this mod added attribute modifiers
			if (tagList.tagCount() >= 2)
			{
				for (int i = 0; i < tagList.tagCount(); i++)
	            {
	                NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
	                AttributeModifier attributeModifier = SharedMonsterAttributes.readAttributeModifierFromNBT(nbttagcompound);
	                
	                if (attributeModifier.getID().equals(ItemStoneShears.getAttackDamageID())
	                		|| attributeModifier.getID().equals(ItemStoneShears.getAttackSpeedID()))
	                {
	                	indexesToRemove.add(i);
	                }
	                
	                if (attributeModifier.getID().equals(ItemStoneShears.getAttackSpeedID())
	                		&& attributeModifier.getAmount() != 6)
	                {
	                	// Another mod did some attribute modifiers, don't remove them so I don't break that mod.
	                	indexesToRemove.clear();
	                }
	            }
				
				for (int i = 0; i < indexesToRemove.size(); i++)
				{
					tagList.removeTag(indexesToRemove.get(i)-i);
				}
				
				if (tagList.tagCount() < 2)
				{
					stack.removeSubCompound("AttributeModifiers");
				}
				else
				{
					stack.setTagInfo("AttributeModifiers", tagList);
				}
			}
		}
	}
	
}
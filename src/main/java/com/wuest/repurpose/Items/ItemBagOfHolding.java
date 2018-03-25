package com.wuest.repurpose.Items;

import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;
import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Gui.GuiCoffer;
import com.wuest.repurpose.Gui.GuiItemGardnersPouch;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * 
 * @author WuestMan
 *
 */
public class ItemBagOfHolding extends Item
{
	public static final String customValues = "bag_values";
	public static final String currentSlotName = "current_slot";
	public static final String bagOpenName = "bag_opened";

	public ItemBagOfHolding(String name)
	{
		setCreativeTab(CreativeTabs.TOOLS);
		this.setMaxStackSize(1);
		ModRegistry.setItemName(this, name);
	}

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if (!world.isRemote && hand == EnumHand.OFF_HAND)
		{
			if (player.isSneaking())
			{
				ItemStack stack = player.getHeldItem(hand);
				
				// Open and close the bag. 
				// This is important for auto-pickup.
				ItemBagOfHolding.setBagOpenedStack(stack, !ItemBagOfHolding.getBagOpenedFromStack(stack));
				
				player.inventoryContainer.detectAndSendChanges();
				int test = stack.getMetadata();
				return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
			}
			
			RayTraceResult result = this.rayTrace(player, 5.0, 1.0f);

			if (result.typeOfHit == Type.MISS)
			{
				player.openGui(Repurpose.instance, GuiItemGardnersPouch.GUI_ID, world, player.getPosition().getX(),
					player.getPosition().getY(), player.getPosition().getZ());
			}
			else if (result.typeOfHit == Type.BLOCK)
			{
				ItemStack stack = player.getHeldItem(hand);
				ItemStackHandler handler = (ItemStackHandler)stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

				if (handler != null)
				{
					BlockPos newBlockPos = result.getBlockPos().offset(result.sideHit);

					if (world.isAirBlock(newBlockPos))
					{
						int slot = ItemBagOfHolding.getCurrentSlotFromStack(stack);
						ItemStack stackInSlot = handler.extractItem(slot, 1, false);

						if (stackInSlot.getItem() instanceof ItemBlock)
						{
							// This is an item block so it can be set in the
							// world.
							ItemBlock itemBlock = (ItemBlock) stackInSlot.getItem();
							EnumActionResult placementResult = this.PlaceBlockFromPouch(player, world, itemBlock,
								result, newBlockPos, stackInSlot);

							if (placementResult == EnumActionResult.SUCCESS)
							{
								stackInSlot.shrink(1);
								
								ItemBagOfHolding.RefreshItemStack(player, stack);
							}
							else
							{
								handler.insertItem(slot, stackInSlot, false);
							}
						}
					}
				}
			}
		}

		return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
	}

	/**
	 * This used to be 'display damage' but its really just 'aux' data in the ItemStack, usually shares the same
	 * variable as damage.
	 * 
	 * @param stack
	 * @return
	 */
	@Override
	public int getMetadata(ItemStack stack)
	{
		if (stack.getTagCompound() == null || stack.getTagCompound().hasNoTags())
		{
			// Make sure to serialize the NBT for this stack so the information is pushed to the client and the
			// appropriate Icon is displayed for this stack.
			stack.setTagCompound(stack.serializeNBT());
		}

		return stack.getItemDamage();
	}

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    @Override
	public String getUnlocalizedName(ItemStack stack)
    {
    	String returnValue = super.getUnlocalizedName();
    	
    	boolean bagIsOpen = ItemBagOfHolding.getBagOpenedFromStack(stack);
    	
        return returnValue + (bagIsOpen ? "_opened" : "_closed");
    }
	
	/**
	 * Override this method to change the NBT data being sent to the client. You should ONLY override this when you have
	 * no other choice, as this might change behavior client side!
	 *
	 * @param stack The stack to send the NBT tag for
	 * @return The NBT tag
	 */
	@Override
	public NBTTagCompound getNBTShareTag(ItemStack stack)
	{
		if (stack.getTagCompound() == null || stack.getTagCompound().hasNoTags())
		{
			// Make sure to serialize the NBT for this stack so the information is pushed to the client and the
			// appropriate Icon is displayed for this stack.
			stack.setTagCompound(stack.serializeNBT());
		}

		return stack.getTagCompound();
	}

	public EnumActionResult PlaceBlockFromPouch(EntityPlayer player, World world, ItemBlock itemBlock,
		RayTraceResult rayTraceResult, BlockPos pos, ItemStack itemStack)
	{
		IBlockState iblockstate = world.getBlockState(pos);
		Block block = iblockstate.getBlock();

		if (!block.isReplaceable(world, pos))
		{
			pos = pos.offset(rayTraceResult.sideHit);
		}

		if (player.canPlayerEdit(pos, rayTraceResult.sideHit, itemStack)
			&& world.mayPlace(itemBlock.getBlock(), pos, false, rayTraceResult.sideHit, (Entity) null))
		{
			int metaData = itemBlock.getMetadata(itemStack.getMetadata());
			IBlockState placementState = itemBlock.getBlock().getStateForPlacement(world, pos, rayTraceResult.sideHit,
				pos.getX(), pos.getY(), pos.getZ(), metaData, player, EnumHand.OFF_HAND);

			// Call the ItemBlock's PlaceBlockAt method to properly place the block.
			if (itemBlock.placeBlockAt(itemStack, player, world, pos, rayTraceResult.sideHit, pos.getX(), pos.getY(),
				pos.getZ(), placementState))
			{
				// After placing, get the block state and the associated sound and play it for the player. Return a
				// success message.
				placementState = world.getBlockState(pos);
				SoundType soundType = placementState.getBlock().getSoundType(placementState, world, pos, player);

				world.playSound(null, pos, soundType.getPlaceSound(), SoundCategory.BLOCKS,
					(soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);

				return EnumActionResult.SUCCESS;
			}
		}

		return EnumActionResult.PASS;
	}

	public static void RefreshItemStack(EntityPlayer player, ItemStack stack)
	{
		if (stack.getItem() instanceof ItemBagOfHolding)
		{
			ItemBagOfHolding item = (ItemBagOfHolding)stack.getItem();
			
			NBTTagCompound compound = item.getNBTShareTag(stack);
			boolean refreshValue = true;
			
			if (compound.hasKey("refreshValue"))
			{
				refreshValue = !compound.getBoolean("refreshValue");
			}
			
			compound.setBoolean("refreshValue", refreshValue);
			player.inventoryContainer.detectAndSendChanges();
		}
	}
	
	@Nullable
	public static RayTraceResult rayTrace(EntityPlayer player, double blockReachDistance, float partialTicks)
	{
		Vec3d vec3d = player.getPositionEyes(partialTicks);
		Vec3d vec3d1 = player.getLook(partialTicks);
		Vec3d vec3d2 = vec3d.addVector(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance,
			vec3d1.z * blockReachDistance);
		return player.world.rayTraceBlocks(vec3d, vec3d2, false, false, true);
	}

	public static int getCurrentSlotFromStack(ItemStack stack)
	{
		if (stack.getItem() instanceof ItemBagOfHolding)
		{
			NBTTagCompound sharedTag = stack.getItem().getNBTShareTag(stack);
			
			if (!sharedTag.hasKey(ItemBagOfHolding.customValues))
			{
				NBTTagCompound customValues = new NBTTagCompound();
				sharedTag.setTag(ItemBagOfHolding.customValues, customValues);
				sharedTag = customValues;
			}
			else
			{
				sharedTag = sharedTag.getCompoundTag(ItemBagOfHolding.customValues);
			}

			if (sharedTag.hasKey(ItemBagOfHolding.currentSlotName))
			{
				return sharedTag.getInteger(ItemBagOfHolding.currentSlotName);
			}
			else
			{
				sharedTag.setInteger(ItemBagOfHolding.currentSlotName, 0);
			}
		}

		return 0;
	}

	public static void setCurrentSlotForStack(ItemStack stack, int slot)
	{
		if (stack.getItem() instanceof ItemBagOfHolding)
		{
			NBTTagCompound sharedTag = stack.getOrCreateSubCompound(ItemBagOfHolding.customValues);

			sharedTag.setInteger(ItemBagOfHolding.currentSlotName, slot);
		}
	}

	public static boolean getBagOpenedFromStack(ItemStack stack)
	{
		if (stack.getItem() instanceof ItemBagOfHolding)
		{
			NBTTagCompound sharedTag = stack.getItem().getNBTShareTag(stack);
			
			if (!sharedTag.hasKey(ItemBagOfHolding.customValues))
			{
				NBTTagCompound customValues = new NBTTagCompound();
				sharedTag.setTag(ItemBagOfHolding.customValues, customValues);
				sharedTag = customValues;
			}
			else
			{
				sharedTag = sharedTag.getCompoundTag(ItemBagOfHolding.customValues);
			}

			if (sharedTag.hasKey(ItemBagOfHolding.bagOpenName))
			{
				return sharedTag.getBoolean(ItemBagOfHolding.bagOpenName);
			}
			else
			{
				sharedTag.setBoolean(ItemBagOfHolding.bagOpenName, false);
			}
		}

		return false;
	}

	public static void setBagOpenedStack(ItemStack stack, boolean open)
	{
		if (stack.getItem() instanceof ItemBagOfHolding)
		{
			NBTTagCompound sharedTag = stack.getOrCreateSubCompound(ItemBagOfHolding.customValues);

			sharedTag.setBoolean(ItemBagOfHolding.bagOpenName, open);
			
			int metaValue = open ? 1 : 0;
			stack.setItemDamage(metaValue);
		}
	}

	public static ItemStack getItemStackFromInventory(EntityPlayer player)
	{
		ItemStack stack = player.getHeldItemOffhand();

		if (stack.getItem() instanceof ItemBagOfHolding)
		{
			int slot = ItemBagOfHolding.getCurrentSlotFromStack(stack);
			IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

			if (handler != null)
			{
				if (slot >= handler.getSlots())
				{
					slot = 0;
				}

				return handler.getStackInSlot(slot);
			}
		}

		return ItemStack.EMPTY;
	}
}
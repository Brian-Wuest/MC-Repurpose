package com.wuest.repurpose.Items;

import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;
import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Capabilities.GardnersPouchProvider;
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

/**
 * 
 * @author WuestMan
 *
 */
public class ItemGardnersPouch extends Item
{
	public ItemGardnersPouch(String name)
	{
		setCreativeTab(CreativeTabs.TOOLS);
		this.setMaxStackSize(1);
		ModRegistry.setItemName(this, name);
	}

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if (!world.isRemote && hand == EnumHand.OFF_HAND)
		{
			RayTraceResult result = this.rayTrace(player, 5.0, 1.0f);

			if (result.typeOfHit == Type.MISS)
			{
				player.openGui(Repurpose.instance, GuiItemGardnersPouch.GUI_ID, world, player.getPosition().getX(),
					player.getPosition().getY(), player.getPosition().getZ());
			}
			else if (result.typeOfHit == Type.BLOCK)
			{
				ItemStack stack = player.getHeldItem(hand);
				IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

				if (handler != null)
				{
					BlockPos newBlockPos = result.getBlockPos().offset(result.sideHit);

					if (world.isAirBlock(newBlockPos))
					{
						ItemStack firstStack = handler.getStackInSlot(0);

						if (firstStack.getItem() instanceof ItemBlock)
						{
							// This is an item block so it can be set in the
							// world.
							ItemBlock itemBlock = (ItemBlock) firstStack.getItem();
							EnumActionResult placementResult = this.PlaceBlockFromPouch(player, world, itemBlock,
								result, newBlockPos, firstStack);

							if (placementResult == EnumActionResult.SUCCESS)
							{
								firstStack.shrink(1);
							}
						}
					}
				}
			}
		}

		return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
	}

	@Nullable
	public RayTraceResult rayTrace(EntityPlayer player, double blockReachDistance, float partialTicks)
	{
		Vec3d vec3d = player.getPositionEyes(partialTicks);
		Vec3d vec3d1 = player.getLook(partialTicks);
		Vec3d vec3d2 = vec3d.addVector(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance,
			vec3d1.z * blockReachDistance);
		return player.world.rayTraceBlocks(vec3d, vec3d2, false, false, true);
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
				// After placing, get the block state and the associated sound and play it for the player. Return a success message.
				placementState = world.getBlockState(pos);
				SoundType soundType = placementState.getBlock().getSoundType(placementState, world, pos, player);

				world.playSound(null, pos, soundType.getPlaceSound(), SoundCategory.BLOCKS,
					(soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);

				return EnumActionResult.SUCCESS;
			}
		}

		return EnumActionResult.PASS;
	}
}
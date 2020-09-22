package com.wuest.repurpose.Blocks;

import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Proxy.CommonProxy;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class BlockCustomWall extends WallBlock implements IModBlock {
	public BlockCustomWall.EnumType BlockVariant;

	public BlockCustomWall(Block modelBlock, BlockCustomWall.EnumType variant) {
		super(Block.Properties.create(Material.ROCK)
				.hardnessAndResistance(modelBlock.getDefaultState().getBlockHardness(null, null),
						modelBlock.getExplosionResistance() * 5.0F / 3.0F)
				.sound(modelBlock.getSoundType(null)));

		this.BlockVariant = variant;
	}

	/**
	 * Returns whether or not this block is of a type that needs random ticking.
	 * Called for ref-counting purposes by ExtendedBlockStorage in order to broadly
	 * cull a chunk from the random chunk update list for efficiency's sake.
	 */
	@Override
	public boolean ticksRandomly(BlockState state) {
		return this.BlockVariant == EnumType.DIRT;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if (!worldIn.isRemote && CommonProxy.proxyConfiguration.enableGrassSpreadToCustomDirt) {
			if (worldIn.getLight(pos.up()) >= 9) {
				for (int i = 0; i < 4; ++i) {
					BlockPos blockpos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);

					if (blockpos.getY() >= 0 && blockpos.getY() < 256 && !worldIn.isBlockLoaded(blockpos)) {
						return;
					}

					BlockState iblockstate1 = worldIn.getBlockState(blockpos);

					if ((iblockstate1.getBlock() == Blocks.GRASS_BLOCK
							|| iblockstate1.getBlock() == ModRegistry.GrassStairs.get()
							|| iblockstate1.getBlock() == ModRegistry.GrassWall.get()
							|| iblockstate1.getBlock() == ModRegistry.GrassSlab.get())
							&& worldIn.getLight(blockpos.up()) >= 4) {
						BlockState grassStairsState = ModRegistry.GrassWall.get().getDefaultState()
								.with(WallBlock.WALL_HEIGHT_EAST, state.get(WallBlock.WALL_HEIGHT_EAST))
								.with(WallBlock.WALL_HEIGHT_WEST, state.get(WallBlock.WALL_HEIGHT_WEST))
								.with(WallBlock.WALL_HEIGHT_NORTH, state.get(WallBlock.WALL_HEIGHT_NORTH))
								.with(WallBlock.WALL_HEIGHT_SOUTH, state.get(WallBlock.WALL_HEIGHT_SOUTH))
								.with(WallBlock.WATERLOGGED, state.get(WallBlock.WATERLOGGED))
								.with(WallBlock.UP, state.get(WallBlock.UP));
						worldIn.setBlockState(pos, grassStairsState, 3);
					}
				}
			}
		}
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos,
								  PlayerEntity player) {
		return new ItemStack(Item.getItemFromBlock(ModRegistry.DirtWall.get()));
	}

	public enum EnumType implements IStringSerializable {
		DIRT(0, "block_dirt_wall", "block_dirt_wall", Material.EARTH),
		GRASS(1, "block_grass_wall", "block_grass_wall", Material.EARTH);

		private static final BlockCustomWall.EnumType[] META_LOOKUP = new BlockCustomWall.EnumType[values().length];

		static {
			for (BlockCustomWall.EnumType customwall$enumtype : values()) {
				META_LOOKUP[customwall$enumtype.getMetadata()] = customwall$enumtype;
			}
		}

		private final int meta;
		private final String name;
		private String unlocalizedName;
		private Material material;

		EnumType(int meta, String name, String unlocalizedName, Material blockMaterial) {
			this.meta = meta;
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			this.material = blockMaterial;
		}

		public static BlockCustomWall.EnumType byMetadata(int meta) {
			if (meta < 0 || meta >= META_LOOKUP.length) {
				meta = 0;
			}

			return META_LOOKUP[meta];
		}

		public int getMetadata() {
			return this.meta;
		}

		public String toString() {
			return this.name;
		}

		@Override
		public String getString() {
			return this.name;
		}

		public Material getMaterial() {
			return this.material;
		}

		public String getUnlocalizedName() {
			return this.unlocalizedName;
		}
	}
}

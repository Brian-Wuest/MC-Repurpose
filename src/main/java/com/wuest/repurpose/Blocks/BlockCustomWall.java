package com.wuest.repurpose.Blocks;

import java.util.List;
import java.util.Random;

import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Repurpose;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.FourWayBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;

public class BlockCustomWall extends WallBlock implements IModBlock {
	public static final EnumProperty<BlockCustomWall.EnumType> VARIANT = EnumProperty.<BlockCustomWall.EnumType>create(
			"variant", BlockCustomWall.EnumType.class);
	public BlockCustomWall.EnumType BlockVariant;

	public BlockCustomWall(Block modelBlock, BlockCustomWall.EnumType variant) {
		super(Block.Properties.create(Material.ROCK)
				.hardnessAndResistance(modelBlock.getBlockHardness(null, null, null),
						modelBlock.getExplosionResistance() * 5.0F / 3.0F)
				.sound(modelBlock.getSoundType(null)));

		this.BlockVariant = variant;

		ModRegistry.setBlockName(this, variant.getUnlocalizedName());
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
	public void randomTick(BlockState state, World worldIn, BlockPos pos, Random random) {
		if (!worldIn.isRemote && Repurpose.proxy.proxyConfiguration.enableGrassSpreadToCustomDirt) {
			if (worldIn.getLight(pos.up()) >= 9) {
				for (int i = 0; i < 4; ++i) {
					BlockPos blockpos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);

					if (blockpos.getY() >= 0 && blockpos.getY() < 256 && !worldIn.isBlockLoaded(blockpos)) {
						return;
					}

					BlockState iblockstate1 = worldIn.getBlockState(blockpos);

					if ((iblockstate1.getBlock() == Blocks.GRASS_BLOCK
							|| iblockstate1.getBlock() == ModRegistry.GrassStairs()
							|| iblockstate1.getBlock() == ModRegistry.GrassWall()
							|| iblockstate1.getBlock() == ModRegistry.GrassSlab())
							&& worldIn.getLight(blockpos.up()) >= 4) {
						BlockState grassStairsState = ModRegistry.GrassWall().getDefaultState()
								.with(FourWayBlock.EAST, state.get(FourWayBlock.EAST))
								.with(FourWayBlock.WEST, state.get(FourWayBlock.WEST))
								.with(FourWayBlock.NORTH, state.get(FourWayBlock.NORTH))
								.with(FourWayBlock.SOUTH, state.get(FourWayBlock.SOUTH))
								.with(FourWayBlock.WATERLOGGED, state.get(FourWayBlock.WATERLOGGED))
								.with(WallBlock.UP,state.get(WallBlock.UP));
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
		return new ItemStack(Item.getItemFromBlock(ModRegistry.DirtWall()));
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> drops = super.getDrops(state, builder);

		if (drops.size() > 0) {
			ItemStack tool = builder.get(LootParameters.TOOL);
			int silklevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, tool);

			drops.clear();

			if (silklevel > 0) {
				drops.add(new ItemStack(ModRegistry.GrassWall()));
			} else {
				drops.add(new ItemStack(ModRegistry.DirtWall()));
			}
		}

		return drops;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(BlockCustomWall.VARIANT);
	}

	/**
	 * Gets the render layer this block will render on. SOLID for solid blocks, CUTOUT or CUTOUT_MIPPED for on-off
	 * transparency (glass, reeds), TRANSLUCENT for fully blended transparency (stained glass)
	 */
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	public static enum EnumType implements IStringSerializable {
		DIRT(0, "block_dirt_wall", "block_dirt_wall", Material.EARTH),
		GRASS(1, "block_grass_wall", "block_grass_wall", Material.EARTH);

		private static final BlockCustomWall.EnumType[] META_LOOKUP = new BlockCustomWall.EnumType[values().length];
		private final int meta;
		private final String name;
		private String unlocalizedName;
		private Material material;

		private EnumType(int meta, String name, String unlocalizedName, Material blockMaterial) {
			this.meta = meta;
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			this.material = blockMaterial;
		}

		public int getMetadata() {
			return this.meta;
		}

		public String toString() {
			return this.name;
		}

		public static BlockCustomWall.EnumType byMetadata(int meta) {
			if (meta < 0 || meta >= META_LOOKUP.length) {
				meta = 0;
			}

			return META_LOOKUP[meta];
		}

		public String getName() {
			return this.name;
		}

		public Material getMaterial() {
			return this.material;
		}

		public String getUnlocalizedName() {
			return this.unlocalizedName;
		}

		static {
			for (BlockCustomWall.EnumType customwall$enumtype : values()) {
				META_LOOKUP[customwall$enumtype.getMetadata()] = customwall$enumtype;
			}
		}
	}
}

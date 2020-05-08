package com.wuest.repurpose.Base;

import com.wuest.repurpose.Repurpose;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraftforge.common.ToolType;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

/**
 * The base block for any block associated with a tile entity.
 *
 * @author WuestMan
 */
public abstract class TileBlockBase<T extends TileEntityBase> extends Block {
	public final TileEntityType<?> entityType;

	/**
	 * Initializes a new instance of the TileBlockBase class.
	 */
	public TileBlockBase(Block.Properties properties, TileEntityType<?> entityType) {
		super(properties.tickRandomly());

		this.entityType = entityType;
	}

	public Class<T> getTypeParameterClass() {
		Type type = getClass().getGenericSuperclass();
		ParameterizedType paramType = (ParameterizedType) type;
		return (Class<T>) paramType.getActualTypeArguments()[0];
	}

	/**
	 * Determine if this block can make a redstone connection on the side provided,
	 * Useful to control which sides are inputs and outputs for redstone wires.
	 *
	 * @param world The current world
	 * @param pos   Block position in world
	 * @param side  The side that is trying to make the connection, CAN BE NULL
	 * @return True to make the connection
	 */
	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
		/*
			Can this block provide power. Only wire currently seems to have this change based on its state.
		*/
		return this.canProvidePower(state) && side != null;
	}

	@Override
	public boolean canProvidePower(BlockState state) {
		return true;
	}

	/**
	 * Queries the class of tool required to harvest this block, if null is returned
	 * we assume that anything can harvest this block.
	 */
	public ToolType getHarvestTool(BlockState state) {
		return null;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
		// System.out.println("Creating new tile entity.");
		return this.createNewTileEntity();
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> items = super.getDrops(state, builder);
		BlockPos pos = builder.get(LootParameters.POSITION);
		World world = builder.getWorld();

		T tileEntity = this.getLocalTileEntity(world, pos);

		for (ItemStack item : items) {
			if (tileEntity != null) {
				item = tileEntity.transferCapabilities(item);
			}
		}

		return items;
	}

	public T createNewTileEntity() {
		try {
			return this.getTypeParameterClass().getConstructor(this.entityType.getClass()).newInstance(this.entityType);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			Repurpose.LOGGER.log(Level.ERROR, e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.hasTileEntity() && state.getBlock() != newState.getBlock()) {
			worldIn.removeTileEntity(pos);
		}
	}

	@Override
	public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
		T tileEntity = this.getLocalTileEntity((World) worldIn, pos);

		this.customBreakBlock(tileEntity, (World) worldIn, pos, state);

		super.onPlayerDestroy(worldIn, pos, state);
		((World) worldIn).removeTileEntity(pos);
	}

	@Override
	public BlockState getStateForPlacement(BlockState state, Direction facing, BlockState state2, IWorld world,
										   BlockPos pos1, BlockPos pos2, Hand hand) {
		World world1 = (World) world;

		for (Direction enumfacing : Direction.values()) {
			world1.notifyNeighborsOfStateChange(pos1.offset(enumfacing), state.getBlock());
		}

		world1.getPendingBlockTicks().scheduleTick(pos1, this, this.tickRate(world));

		if (world.getTileEntity(pos1) == null) {
			T tile = this.createNewTileEntity();

			world1.setTileEntity(pos1, tile);
		}

		return super.getStateForPlacement(state, facing, state2, world, pos1, pos2, hand);
	}

	@Override
	public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int eventID, int eventParam) {
		super.eventReceived(state, worldIn, pos, eventID, eventParam);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity != null && tileentity.receiveClientEvent(eventID, eventParam);
	}

	/**
	 * Called by ItemBlocks after a block is set in the world, to allow post-place logic
	 */
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		worldIn.getPendingBlockTicks().scheduleTick(pos, this, 3);
	}

	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		this.updateState(worldIn, pos, state);
	}

	public void updateState(World worldIn, BlockPos pos, BlockState state) {
		T tileEntity = this.getLocalTileEntity(worldIn, pos);

		int tickDelay = this.customUpdateState(worldIn, pos, state, tileEntity);

		if (worldIn.isRemote) {
			ClientWorld clientWorld = (ClientWorld) worldIn;
			clientWorld.markSurroundingsForRerender(pos.getX(), pos.getY(), pos.getZ());
		}

		worldIn.getPendingBlockTicks().scheduleTick(pos, this, tickDelay);
	}

	/**
	 * Gets the tile entity at the current position.
	 *
	 * @param worldIn The world to the entity for.
	 * @param pos     The position in the world to get the entity for.
	 * @return Null if the tile was not found or if one was found and is not a
	 * proper tile entity. Otherwise the tile entity instance.
	 */
	public T getLocalTileEntity(World worldIn, BlockPos pos) {
		TileEntity entity = worldIn.getTileEntity(pos);

		if (entity != null && entity.getClass() == this.getTypeParameterClass()) {
			return (T) entity;
		} else {
			T tileEntity = this.createNewTileEntity();

			worldIn.setTileEntity(pos, tileEntity);
			tileEntity.setPos(pos);

			return tileEntity;
		}
	}

	/**
	 * Processes custom update state.
	 *
	 * @param worldIn    The world this state is being updated in.
	 * @param pos        The block position.
	 * @param state      The block state.
	 * @param tileEntity The tile entity associated with this class.
	 * @return The number of ticks to delay until the next update.
	 */
	public abstract int customUpdateState(World worldIn, BlockPos pos, BlockState state, T tileEntity);

	public abstract void customBreakBlock(T tileEntity, World worldIn, BlockPos pos, BlockState state);
}
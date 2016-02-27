package wuest.utilities.Items;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import wuest.utilities.WuestUtilities;
import wuest.utilities.Gui.GuiHouseItem;
import wuest.utilities.Gui.HouseConfiguration;

public class ItemStartHouse extends Item 
{
	public static ItemStartHouse RegisteredItem;

	private HouseConfiguration currentConfiguration = null;

	public ItemStartHouse() 
	{
		super();

		this.setCreativeTab(CreativeTabs.tabMisc);
		this.setUnlocalizedName("itemStartHouse");
	}

	/**
	 * Registers the item in the game world.
	 */
	public static void RegisterItem() 
	{
		ItemStartHouse.RegisteredItem = new ItemStartHouse();

		GameRegistry.registerItem(ItemStartHouse.RegisteredItem,
				"itemStartHouse");
		WuestUtilities.ModItems.add(ItemStartHouse.RegisteredItem);
	}

	/**
	 * Does something when the item is right-clicked.
	 */
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world,
			BlockPos hitBlockPos, EnumFacing side, float hitX, float hitY,
			float hitZ) 
	{
		if (world.isRemote)
		{
			if (side == EnumFacing.UP) 
			{
				// Open the client side gui to determine the house options.
				player.openGui(WuestUtilities.instance, GuiHouseItem.GUI_ID, player.worldObj, hitBlockPos.getX(), hitBlockPos.getY(), hitBlockPos.getZ());
				return true;
			}
		}

		return false;
	}

	public static void BuildHouse(EntityPlayer player, World world, HouseConfiguration configuration)
	{
		// This is always on the server.
		if (configuration != null)
		{
			BlockPos hitBlockPos = new BlockPos(configuration.hitX, configuration.hitY, configuration.hitZ);
			BlockPos playerPosition = player.getPosition();

			IBlockState hitBlockState = world.getBlockState(hitBlockPos);

			if (hitBlockState != null) 
			{
				Block hitBlock = hitBlockState.getBlock();

				if (hitBlock != null) 
				{
					// We hit a block, let's start building!!!!!
					BlockPos startingPosition = hitBlockPos.up();

					// Clear the space before the user is teleported. This
					// is in-case they right-click on a space that is only 1
					// block tall.
					ItemStartHouse.ClearSpace(world, startingPosition);

					// Teleport the player to the middle of the house so
					// they don't die while house is created.
					player.setPositionAndUpdate(startingPosition.up(2)
							.getX(), startingPosition.up(2).getY(),
							startingPosition.up(2).getZ());

					// Build the basic structure.
					ItemStartHouse.BuildStructure(world, startingPosition, configuration);

					// Build the interior.
					ItemStartHouse.BuildInterior(world, startingPosition, player, configuration);

					// Set up the exterior.
					ItemStartHouse.BuildExterior(world, startingPosition, player, configuration);

					if (configuration.addMineShaft
							&& startingPosition.getY() > 15) 
					{
						// Set up the mineshaft.
						ItemStartHouse.PlaceMineShaft(world, startingPosition);
					}

					player.inventory.consumeInventoryItem(ItemStartHouse.RegisteredItem);
				}
			}

			// Make sure to remove the tag so the next time the player opens the GUI we overwrite the existing tag.
			player.getEntityData().removeTag(HouseConfiguration.tagKey);
		}
	}

	public static void ReplaceBlock(World world, BlockPos pos, Block replacementBlock) {
		ItemStartHouse.ReplaceBlock(world, pos, replacementBlock.getDefaultState(), 3);
	}

	public static void ReplaceBlock(World world, BlockPos pos,
			IBlockState replacementBlockState) {
		ItemStartHouse.ReplaceBlock(world, pos, replacementBlockState, 3);
	}

	public static void ReplaceBlock(World world, BlockPos pos,
			IBlockState replacementBlockState, int flags) {
		world.setBlockToAir(pos);
		world.setBlockState(pos, replacementBlockState, flags);
	}

	private static void ClearSpace(World world, BlockPos pos) {
		// Clear a space (20X20X10) around the location the item was used on.
		// Make sure to include the original block in this.
		pos = pos.north(10).east(10);
		BlockPos originalPos = pos;

		for (int z = 0; z <= 10; z++) {
			pos = originalPos.up(z);

			for (int i = 0; i <= 20; i++) {
				// i is the east/west counter.
				for (int j = 0; j <= 20; j++) {
					// j is the north/south counter.
					world.setBlockToAir(pos);

					pos = pos.south();
				}

				pos = pos.west();
				pos = pos.north(21);
			}
		}
	}

	private static void BuildStructure(World world, BlockPos startingPosition, HouseConfiguration configuration) {
		// Make sure that the area beneath the house is all there. Don't want
		// the house to be hanging in the air.
		ItemStartHouse.SetFloor(world, startingPosition, Blocks.dirt, 4,
				new ArrayList<ItemStack>());

		Block floor = null;

		switch (configuration.floorBlock) 
		{
		case Brick: {
			floor = Blocks.brick_block;
			break;
		}

		case SandStone: {
			floor = Blocks.sandstone;
			break;
		}

		default: {
			floor = Blocks.stonebrick;
			break;
		}
		}

		// Create the floor.
		ItemStartHouse.SetFloor(world, startingPosition, floor, 4,
				new ArrayList<ItemStack>());

		// Create the walls.
		ItemStartHouse.SetWalls(world, startingPosition, ((BlockPlanks) Blocks.planks)
				.getStateFromMeta(configuration.wallWoodType.getValue()));

		Block ceiling = null;
		Block stairs = null;

		switch (configuration.ceilingBlock) {
		case Brick: {
			ceiling = Blocks.brick_block;
			stairs = Blocks.brick_stairs;
			break;
		}

		case SandStone: {
			ceiling = Blocks.sandstone;
			stairs = Blocks.sandstone_stairs;
			break;
		}

		default: {
			ceiling = Blocks.stonebrick;
			stairs = Blocks.stone_brick_stairs;
			break;
		}
		}

		// Set the ceiling.
		ItemStartHouse.SetCeiling(world, startingPosition.up(4), ceiling, stairs, configuration);
	}

	private static void BuildInterior(World world, BlockPos startingPosition,
			EntityPlayer player, HouseConfiguration configuration) 
	{
		// Keep the corner positions since they are important.
		BlockPos northEastCornerPosition = startingPosition.north(4).east(4)
				.up();
		BlockPos southEastCornerPosition = startingPosition.south(4).east(4)
				.up();
		BlockPos northWestCornerPosition = startingPosition.north(4).west(4)
				.up();
		BlockPos southWestCornerPosition = startingPosition.south(4).west(4)
				.up();

		// Use a separate position for each item.
		BlockPos itemPosition = northEastCornerPosition;

		if (configuration.addTorches) {
			// Set the torch locations so it's not dark in the house.
			ItemStartHouse.PlaceInsideTorches(world, northEastCornerPosition);
		}

		// Create an oak door in the north east corner
		ItemStartHouse.DecorateDoor(world, northEastCornerPosition, player, configuration);

		if (configuration.addBed) {
			// Place a bed in the north west corner.
			ItemStartHouse.PlaceBed(world, northWestCornerPosition);
		}

		if (configuration.addChest) {
			// Place a double chest in the south east corner.
			ItemStartHouse.PlaceAndFillChest(world, southEastCornerPosition, configuration);
		}

		if (configuration.addCraftingTable) {
			// Place a crafting table in the south west corner.
			ItemStartHouse.PlaceAndFillCraftingMachines(world, southWestCornerPosition);
		}
	}

	private static void BuildExterior(World world, BlockPos startingPosition,
			EntityPlayer player, HouseConfiguration configuration) {
		// Keep the corner positions since they are important.
		// These positions are level with 1 block above the floor.
		BlockPos northEastCornerPosition = startingPosition.north(4).east(4)
				.up();
		BlockPos southEastCornerPosition = startingPosition.south(4).east(4)
				.up();
		BlockPos northWestCornerPosition = startingPosition.north(4).west(4)
				.up();
		BlockPos southWestCornerPosition = startingPosition.south(4).west(4)
				.up();

		if (configuration.addTorches) {
			ItemStartHouse.PlaceOutsideTorches(world, northEastCornerPosition, configuration);
		}

		if (configuration.addFarm) {
			ItemStartHouse.PlaceSmallFarm(world, northEastCornerPosition.down());
		}
	}

	private static ArrayList<ItemStack> SetFloor(World world, BlockPos pos,
			Block block, int floorRadius, ArrayList<ItemStack> originalStack) {
		// Go north 4 and east 4 from block position, this will be an 8 X 8
		// house.
		pos = pos.north(floorRadius).east(floorRadius);

		for (int i = 0; i <= floorRadius * 2; i++) {
			// i is the east/west counter.
			for (int j = 0; j <= floorRadius * 2; j++) {
				// Get the drops for this block (if any) and add it to the
				// stack.
				for (ItemStack stack : world.getBlockState(pos).getBlock()
						.getDrops(world, pos, world.getBlockState(pos), 1)) {
					originalStack.add(stack);
				}

				// j is the north/south counter.
				ItemStartHouse.ReplaceBlock(world, pos, block);

				pos = pos.south();
			}

			pos = pos.west();
			pos = pos.north((floorRadius * 2) + 1);
		}

		return originalStack;
	}

	private static void SetCeiling(World world, BlockPos pos, Block block, Block stairs, HouseConfiguration configuration) {
		// If the ceiling is flat, call SetFloor since it's laid out the same.
		if (configuration.isCeilingFlat) {
			ItemStartHouse.SetFloor(world, pos, block, 4, new ArrayList<ItemStack>());
			return;
		}

		// Get to the north east corner.
		pos = pos.north(4).east(4);

		// Get the stairs state without the facing since it will change.
		IBlockState stateWithoutFacing = stairs
				.getBlockState()
				.getBaseState()
				.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM)
				.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT);

		int wallLength = 7;

		while (wallLength > 0) {
			for (int j = 0; j < 4; j++) {
				// I is the wall side starting on the east side.
				EnumFacing facing = EnumFacing.WEST;
				EnumFacing flowDirection = EnumFacing.SOUTH;

				switch (j) {
				case 1: {
					facing = EnumFacing.NORTH;
					flowDirection = EnumFacing.WEST;
					break;
				}

				case 2: {
					facing = EnumFacing.EAST;
					flowDirection = EnumFacing.NORTH;
					break;
				}

				case 3: {
					facing = EnumFacing.SOUTH;
					flowDirection = EnumFacing.EAST;
					break;
				}
				}

				for (int k = 0; k <= wallLength; k++) {
					// j is the north/south counter.
					ItemStartHouse.ReplaceBlock(world, pos, stateWithoutFacing
							.withProperty(BlockStairs.FACING, facing));

					pos = pos.offset(flowDirection);
				}
			}

			pos = pos.west().south().up();
			wallLength = wallLength - 2;
		}

		ItemStartHouse.ReplaceBlock(world, pos, block);

		IBlockState blockState = Blocks.torch.getStateFromMeta(5);
		ItemStartHouse.ReplaceBlock(world, pos.up(), blockState);
	}

	private static void SetWalls(World world, BlockPos pos, IBlockState block) {
		BlockPos initialPos = pos;

		// Get the north east corner.
		pos = initialPos.north(4).east(4);

		// East Wall.
		ItemStartHouse.CreateWall(world, 4, 8, EnumFacing.SOUTH, pos, block);

		// South Wall.
		pos = initialPos.south(4).east(4);
		ItemStartHouse.CreateWall(world, 4, 8, EnumFacing.WEST, pos, block);

		// West Wall.
		pos = initialPos.south(4).west(4);
		ItemStartHouse.CreateWall(world, 4, 8, EnumFacing.NORTH, pos, block);

		// North Wall.
		pos = initialPos.north(4).west(4);
		ItemStartHouse.CreateWall(world, 4, 8, EnumFacing.EAST, pos, block);
	}

	private static void PlaceInsideTorches(World world, BlockPos cornerPosition) {
		// Use a separate position for each item.
		BlockPos itemPosition = cornerPosition;

		/*
		 * Torch Facings 1 = East 2 = West 3 = South 4 = North 5 = Up
		 */
		// East wall torch.
		itemPosition = itemPosition.south(4).west().up(1);
		IBlockState blockState = Blocks.torch.getStateFromMeta(2);
		ItemStartHouse.ReplaceBlock(world, itemPosition, blockState);

		// North Wall torch.
		itemPosition = cornerPosition.west(4).south().up(1);
		blockState = Blocks.torch.getStateFromMeta(3);
		ItemStartHouse.ReplaceBlock(world, itemPosition, blockState);

		// West wall torch.
		itemPosition = cornerPosition.west(7).south(4).up(1);
		blockState = Blocks.torch.getStateFromMeta(1);
		ItemStartHouse.ReplaceBlock(world, itemPosition, blockState);

		// South wall torch.
		itemPosition = cornerPosition.south(7).west(4).up(1);
		blockState = Blocks.torch.getStateFromMeta(4);
		ItemStartHouse.ReplaceBlock(world, itemPosition, blockState);
	}

	private static void DecorateDoor(World world, BlockPos cornerPosition, EntityPlayer player, HouseConfiguration configuration) {
		BlockPos itemPosition = cornerPosition.west();

		world.setBlockToAir(itemPosition.up());

		Block door = null;
		Block stairs = null;

		switch (configuration.wallWoodType) 
		{
		case Spruce: {
			door = Blocks.spruce_door;
			stairs = Blocks.spruce_stairs;
			break;
		}

		case Birch: {
			door = Blocks.birch_door;
			stairs = Blocks.birch_stairs;
			break;
		}

		case Jungle: {
			door = Blocks.jungle_door;
			stairs = Blocks.jungle_stairs;
			break;
		}

		case Acacia: {
			door = Blocks.acacia_door;
			stairs = Blocks.acacia_stairs;
			break;
		}

		case DarkOak: {
			door = Blocks.dark_oak_door;
			stairs = Blocks.dark_oak_stairs;
			break;
		}

		default: {
			door = Blocks.oak_door;
			stairs = Blocks.oak_stairs;
			break;
		}
		}

		ItemDoor.placeDoor(world, itemPosition, EnumFacing.NORTH, door);

		// Put a glass pane above the door.
		ItemStartHouse.ReplaceBlock(world, itemPosition.up(2), Blocks.glass_pane);

		// Create a pressure plate for the door, no need to re-set the item
		// position here since it needs to be in relation to the door.
		itemPosition = itemPosition.south();
		ItemStartHouse.ReplaceBlock(world, itemPosition, Blocks.wooden_pressure_plate);

		// Place a stairs.
		itemPosition = itemPosition.north(2).down();
		ItemStartHouse.ReplaceBlock(
				world,
				itemPosition,
				stairs.getBlockState()
				.getBaseState()
				.withProperty(BlockStairs.FACING, EnumFacing.SOUTH)
				.withProperty(BlockStairs.HALF,
						BlockStairs.EnumHalf.BOTTOM)
				.withProperty(BlockStairs.SHAPE,
						BlockStairs.EnumShape.STRAIGHT));

		// Place a sign.
		itemPosition = itemPosition.west();
		BlockSign sign = (BlockSign)Blocks.standing_sign;

		ItemStartHouse.ReplaceBlock(world, itemPosition, sign.getStateFromMeta(8));

		TileEntity tileEntity = world.getTileEntity(itemPosition);

		if (tileEntity instanceof TileEntitySign)
		{
			TileEntitySign signTile = (TileEntitySign)tileEntity;
			signTile.signText[0] = new ChatComponentText("This is");

			if (player.getDisplayNameString().length() >= 15)
			{
				signTile.signText[1] = new ChatComponentText(player.getDisplayNameString());
			}
			else
			{
				signTile.signText[1] = new ChatComponentText(player.getDisplayNameString() + "'s");
			}

			signTile.signText[2] = new ChatComponentText("house!");
		}
	}

	private static void PlaceBed(World world, BlockPos cornerPosition) {
		BlockPos itemPosition = cornerPosition.east(1).south(2);

		IBlockState bedFootState = Blocks.bed.getDefaultState()
				.withProperty(BlockBed.OCCUPIED, Boolean.valueOf(false))
				.withProperty(BlockDirectional.FACING, EnumFacing.NORTH)
				.withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);

		if (world.setBlockState(itemPosition, bedFootState, 3)) {
			IBlockState bedHeadState = bedFootState.withProperty(BlockBed.PART,
					BlockBed.EnumPartType.HEAD);
			world.setBlockState(itemPosition.north(), bedHeadState, 3);
		}

	}

	private static void PlaceAndFillChest(World world, BlockPos cornerPosition, HouseConfiguration configuration) {
		// Create a double wide chest.
		BlockPos itemPosition = cornerPosition.north().west();
		ItemStartHouse.ReplaceBlock(world, itemPosition, Blocks.chest);

		itemPosition = itemPosition.west();
		ItemStartHouse.ReplaceBlock(world, itemPosition, Blocks.chest);

		if (configuration.addChestContents) {
			// Add each stone tool to the chest and leather armor.
			TileEntity tileEntity = world.getTileEntity(itemPosition);

			if (tileEntity instanceof TileEntityChest) {
				TileEntityChest chestTile = (TileEntityChest) tileEntity;

				int itemSlot = 0;

				// Add the tools.
				if (WuestUtilities.proxy.proxyConfiguration.addAxe)
				{
					chestTile.setInventorySlotContents(itemSlot++, new ItemStack(
							Items.stone_axe));
				}

				if (WuestUtilities.proxy.proxyConfiguration.addHoe)
				{
					chestTile.setInventorySlotContents(itemSlot++, new ItemStack(
							Items.stone_hoe));
				}

				if (WuestUtilities.proxy.proxyConfiguration.addPickAxe)
				{
					chestTile.setInventorySlotContents(itemSlot++, new ItemStack(
							Items.stone_pickaxe));
				}

				if (WuestUtilities.proxy.proxyConfiguration.addShovel)
				{
					chestTile.setInventorySlotContents(itemSlot++, new ItemStack(
							Items.stone_shovel));
				}

				if (WuestUtilities.proxy.proxyConfiguration.addSword)
				{
					chestTile.setInventorySlotContents(itemSlot++, new ItemStack(
							Items.stone_sword));
				}

				if (WuestUtilities.proxy.proxyConfiguration.addArmor)
				{
					// Add the armor.
					chestTile.setInventorySlotContents(itemSlot++, new ItemStack(
							Items.leather_boots));
					chestTile.setInventorySlotContents(itemSlot++, new ItemStack(
							Items.leather_chestplate));
					chestTile.setInventorySlotContents(itemSlot++, new ItemStack(
							Items.leather_helmet));
					chestTile.setInventorySlotContents(itemSlot++, new ItemStack(
							Items.leather_leggings));
				}

				if (WuestUtilities.proxy.proxyConfiguration.addFood)
				{
					// Add some bread.
					chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.bread, 20));
				}

				if (WuestUtilities.proxy.proxyConfiguration.addCrops)
				{
					// Add potatoes.
					chestTile.setInventorySlotContents(itemSlot++, new ItemStack(
							Items.potato, 3));

					// Add carrots.
					chestTile.setInventorySlotContents(itemSlot++, new ItemStack(
							Items.carrot, 3));

					// Add seeds.
					chestTile.setInventorySlotContents(itemSlot++, new ItemStack(
							Items.wheat_seeds, 3));
				}

				if (WuestUtilities.proxy.proxyConfiguration.addCobble)
				{
					// Add Cobblestone.
					chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Item.getItemFromBlock(Blocks.cobblestone), 64));
				}

				if (WuestUtilities.proxy.proxyConfiguration.addDirt)
				{
					// Add Dirt.
					chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Item.getItemFromBlock(Blocks.dirt), 64));
				}

				if (WuestUtilities.proxy.proxyConfiguration.addSaplings)
				{
					// Add oak sapling.
					chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Item.getItemFromBlock(Blocks.sapling), 3));
				}

				if (WuestUtilities.proxy.proxyConfiguration.addTorches)
				{
					// Add a set of 20 torches.
					chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Item.getItemFromBlock(Blocks.torch), 20));
				}
			}
		}
	}

	private static void PlaceAndFillCraftingMachines(World world,
			BlockPos cornerPosition) {
		BlockPos itemPosition = cornerPosition.east().north();
		ItemStartHouse.ReplaceBlock(world, itemPosition, Blocks.crafting_table);

		// Place a furnace next to the crafting table and fill it with 20 coal.
		itemPosition = itemPosition.east();
		ItemStartHouse.ReplaceBlock(world, itemPosition, Blocks.furnace.getDefaultState()
				.withProperty(BlockFurnace.FACING, EnumFacing.NORTH));

		TileEntity tileEntity = world.getTileEntity(itemPosition);

		if (tileEntity instanceof TileEntityFurnace) {
			TileEntityFurnace furnaceTile = (TileEntityFurnace) tileEntity;
			furnaceTile.setInventorySlotContents(1, new ItemStack(Items.coal,
					20));
		}
	}

	private static void PlaceOutsideTorches(World world, BlockPos cornerPosition, HouseConfiguration configuration) {
		cornerPosition = cornerPosition.north();
		BlockPos itemPosition = cornerPosition;

		/*
		 * Torch Facings 1 = East 2 = West 3 = South 4 = North 5 = Up
		 */
		// Set north east corner.
		IBlockState blockState = Blocks.torch.getStateFromMeta(4);
		ItemStartHouse.ReplaceBlock(world, itemPosition, blockState);

		// North middle
		itemPosition = itemPosition.west(4);
		blockState = Blocks.torch.getStateFromMeta(4);
		ItemStartHouse.ReplaceBlock(world, itemPosition, blockState);

		// North west, facing north.
		itemPosition = itemPosition.west(4);
		blockState = Blocks.torch.getStateFromMeta(4);
		ItemStartHouse.ReplaceBlock(world, itemPosition, blockState);

		// North west, facing west.
		itemPosition = itemPosition.west().south();
		blockState = Blocks.torch.getStateFromMeta(2);
		ItemStartHouse.ReplaceBlock(world, itemPosition, blockState);

		// West middle.
		itemPosition = itemPosition.south(4);
		blockState = Blocks.torch.getStateFromMeta(2);
		ItemStartHouse.ReplaceBlock(world, itemPosition, blockState);

		// South west facing west.
		itemPosition = itemPosition.south(4);
		blockState = Blocks.torch.getStateFromMeta(2);
		ItemStartHouse.ReplaceBlock(world, itemPosition, blockState);

		// South west facing south.
		itemPosition = itemPosition.south().east();
		blockState = Blocks.torch.getStateFromMeta(3);
		ItemStartHouse.ReplaceBlock(world, itemPosition, blockState);

		// South middle.
		itemPosition = itemPosition.east(4);
		blockState = Blocks.torch.getStateFromMeta(3);
		ItemStartHouse.ReplaceBlock(world, itemPosition, blockState);

		// South east facing south.
		itemPosition = itemPosition.east(4);
		blockState = Blocks.torch.getStateFromMeta(3);
		ItemStartHouse.ReplaceBlock(world, itemPosition, blockState);

		// South east facing east.
		itemPosition = itemPosition.east().north();
		blockState = Blocks.torch.getStateFromMeta(1);
		ItemStartHouse.ReplaceBlock(world, itemPosition, blockState);

		// East middle.
		itemPosition = itemPosition.north(4);
		blockState = Blocks.torch.getStateFromMeta(1);
		ItemStartHouse.ReplaceBlock(world, itemPosition, blockState);

		// North East facing east
		itemPosition = itemPosition.north(4);
		blockState = Blocks.torch.getStateFromMeta(1);
		ItemStartHouse.ReplaceBlock(world, itemPosition, blockState);

		if (configuration.isCeilingFlat) {
			// Roof Torches
			// Re-set the corner position to be on the roof.
			cornerPosition = cornerPosition.south().up(4);

			// North east corner.
			itemPosition = cornerPosition;
			blockState = Blocks.torch.getStateFromMeta(5);
			ItemStartHouse.ReplaceBlock(world, itemPosition, blockState);

			// East middle.
			itemPosition = itemPosition.south(4);
			blockState = Blocks.torch.getStateFromMeta(5);
			ItemStartHouse.ReplaceBlock(world, itemPosition, blockState);

			// South east corner
			itemPosition = itemPosition.south(4);
			blockState = Blocks.torch.getStateFromMeta(5);
			ItemStartHouse.ReplaceBlock(world, itemPosition, blockState);

			// South middle
			itemPosition = itemPosition.west(4);
			blockState = Blocks.torch.getStateFromMeta(5);
			ItemStartHouse.ReplaceBlock(world, itemPosition, blockState);

			// South west corner.
			itemPosition = itemPosition.west(4);
			blockState = Blocks.torch.getStateFromMeta(5);
			ItemStartHouse.ReplaceBlock(world, itemPosition, blockState);

			// West middle
			itemPosition = itemPosition.north(4);
			blockState = Blocks.torch.getStateFromMeta(5);
			ItemStartHouse.ReplaceBlock(world, itemPosition, blockState);

			// North West corner.
			itemPosition = itemPosition.north(4);
			blockState = Blocks.torch.getStateFromMeta(5);
			ItemStartHouse.ReplaceBlock(world, itemPosition, blockState);

			// North middle
			itemPosition = itemPosition.east(4);
			blockState = Blocks.torch.getStateFromMeta(5);
			ItemStartHouse.ReplaceBlock(world, itemPosition, blockState);
		}
	}

	private static void PlaceSmallFarm(World world, BlockPos cornerPosition) {
		BlockPos farmStart = cornerPosition.north(4).west(5);
		IBlockState state = world.getBlockState(farmStart);

		// Keep going down until we get to the surface.
		while (!state.getBlock().isAir(world, farmStart)) {
			farmStart.down();
		}

		farmStart = farmStart.down();

		// We are now at the surface and this is where the first water source
		// will be.
		ItemStartHouse.ReplaceBlock(world, farmStart, Blocks.water);
		ItemStartHouse.ReplaceBlock(world, farmStart.down(), Blocks.dirt);
		ItemStartHouse.ReplaceBlock(world, farmStart.up(), Blocks.glass);
		ItemStartHouse.ReplaceBlock(world, farmStart.up(2), Blocks.torch);
		ItemStartHouse.ReplaceBlock(world, farmStart.north(), Blocks.farmland);
		ItemStartHouse.ReplaceBlock(world, farmStart.north().west(), Blocks.farmland);
		ItemStartHouse.ReplaceBlock(world, farmStart.west(), Blocks.farmland);
		ItemStartHouse.ReplaceBlock(world, farmStart.south(), Blocks.farmland);
		ItemStartHouse.ReplaceBlock(world, farmStart.south().west(), Blocks.farmland);

		farmStart = farmStart.east();

		ItemStartHouse.ReplaceBlock(world, farmStart, Blocks.water);
		ItemStartHouse.ReplaceBlock(world, farmStart.down(), Blocks.dirt);
		ItemStartHouse.ReplaceBlock(world, farmStart.up(), Blocks.glass);
		ItemStartHouse.ReplaceBlock(world, farmStart.up(2), Blocks.torch);
		ItemStartHouse.ReplaceBlock(world, farmStart.north(), Blocks.farmland);
		ItemStartHouse.ReplaceBlock(world, farmStart.south(), Blocks.farmland);

		farmStart = farmStart.east();

		ItemStartHouse.ReplaceBlock(world, farmStart, Blocks.water);
		ItemStartHouse.ReplaceBlock(world, farmStart.down(), Blocks.dirt);
		ItemStartHouse.ReplaceBlock(world, farmStart.up(), Blocks.glass);
		ItemStartHouse.ReplaceBlock(world, farmStart.up(2), Blocks.torch);
		ItemStartHouse.ReplaceBlock(world, farmStart.north(), Blocks.farmland);
		ItemStartHouse.ReplaceBlock(world, farmStart.north().east(), Blocks.farmland);
		ItemStartHouse.ReplaceBlock(world, farmStart.east(), Blocks.farmland);
		ItemStartHouse.ReplaceBlock(world, farmStart.south(), Blocks.farmland);
		ItemStartHouse.ReplaceBlock(world, farmStart.south().east(), Blocks.farmland);
	}

	private static void PlaceMineShaft(World world, BlockPos pos) {
		// The initial position is where the character was teleported too, go
		// back 3 blocks and start building the mine shaft.
		pos = pos.south(3);

		// Keep track of all of the items to add to the chest at the end of the
		// shaft.
		ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();

		stacks = ItemStartHouse.CreateLadderShaft(world, pos, stacks);

		pos = pos.down(pos.getY() - 10);

		ArrayList<ItemStack> tempStacks = new ArrayList<ItemStack>();

		// The entire ladder has been created. Create a platform at this level
		// and place a chest next to the ladder.
		tempStacks = ItemStartHouse.SetFloor(world, pos, Blocks.stone, 3, tempStacks);

		// Now that the floor has been set, go up 1 block to star creating the
		// walls.
		pos = pos.up();

		// Clear a space around the ladder pillar and make walls. The walls are
		// necessary if there is a lot of lava down here.
		// Make a wall of air then a wall of stone.

		// South wall.
		tempStacks.addAll(ItemStartHouse.CreateWall(world, 3, 3, EnumFacing.EAST, pos
				.south(2).west(), Blocks.air));
		tempStacks.addAll(ItemStartHouse.CreateWall(world, 3, 3, EnumFacing.EAST, pos
				.south(3).west(), Blocks.stone));

		// East wall.
		tempStacks.addAll(ItemStartHouse.CreateWall(world, 3, 4, EnumFacing.NORTH, pos
				.south(2).east(), Blocks.air));
		tempStacks.addAll(ItemStartHouse.CreateWall(world, 3, 4, EnumFacing.NORTH, pos
				.south(2).east(2), Blocks.stone));

		// North wall.
		tempStacks.addAll(ItemStartHouse.CreateWall(world, 3, 3, EnumFacing.WEST, pos
				.north().east(), Blocks.air));
		tempStacks.addAll(ItemStartHouse.CreateWall(world, 3, 3, EnumFacing.WEST, pos
				.north(2).east(), Blocks.stone));

		// West wall.
		tempStacks.addAll(ItemStartHouse.CreateWall(world, 3, 4, EnumFacing.SOUTH, pos
				.north().west(), Blocks.air));
		tempStacks.addAll(ItemStartHouse.CreateWall(world, 3, 4, EnumFacing.SOUTH, pos
				.north(1).west(2), Blocks.stone));

		// Consolidate the stacks.
		for (ItemStack tempStack : tempStacks) {
			Boolean foundStack = false;

			for (ItemStack existingStack : stacks) {
				if (ItemStack.areItemsEqual(existingStack, tempStack)) {
					// Make sure that this combined stack is at or smaller than
					// the max.
					if (existingStack.stackSize + tempStack.stackSize <= tempStack
							.getMaxStackSize()) {
						existingStack.stackSize = existingStack.stackSize
								+ tempStack.stackSize;
						foundStack = true;
						break;
					}
				}
			}

			if (!foundStack) {
				stacks.add(tempStack);
			}
		}

		// Place a torch to the left of the ladder.
		IBlockState blockState = Blocks.torch.getStateFromMeta(5);
		ItemStartHouse.ReplaceBlock(world, pos.west(), blockState);

		// Place a chest to the right of the ladder.
		ItemStartHouse.ReplaceBlock(world, pos.east(), Blocks.chest);
		TileEntity tileEntity = world.getTileEntity(pos.east());

		if (tileEntity instanceof TileEntityChest) {
			TileEntityChest chestTile = (TileEntityChest) tileEntity;

			int i = 0;
			// All of the stacks should be consolidated at this point.
			for (ItemStack stack : stacks) {
				chestTile.setInventorySlotContents(i, stack);
				i++;
			}
		}
	}

	private static ArrayList<ItemStack> CreateLadderShaft(World world, BlockPos pos,
			ArrayList<ItemStack> originalStacks) {
		int torchCounter = 0;

		while (pos.getY() > 8) {
			IBlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			torchCounter++;

			// Make sure all blocks around this one are solid, if they are not
			// replace them with stone.
			for (int i = 0; i < 4; i++) {
				EnumFacing facing = EnumFacing.NORTH;

				switch (i) {
				case 1: {
					facing = EnumFacing.EAST;
					break;
				}
				case 2: {
					facing = EnumFacing.SOUTH;
					break;
				}
				case 3: {
					facing = EnumFacing.WEST;
					break;
				}
				default: {
					facing = EnumFacing.NORTH;
				}
				}

				// Every 6 blocks, place a torch on the west wall.
				// If we are close to the bottom, don't place a torch. Do the
				// normal processing.
				if (facing == EnumFacing.WEST && torchCounter == 6
						&& pos.getY() > 14) {
					// First make sure the block behind this block is stone,
					// then place the torch.
					BlockPos tempPos = pos.offset(facing, 2);
					IBlockState surroundingState = world.getBlockState(tempPos);
					Block surroundingBlock = surroundingState.getBlock();

					if (!surroundingBlock.isBlockNormalCube()) {
						// This is not a solid block. Get the drops then replace
						// it with stone.
						ItemStartHouse.ConsolidateDrops(surroundingBlock, world, tempPos,
								surroundingState, originalStacks);

						ItemStartHouse.ReplaceBlock(world, tempPos, Blocks.stone);
					}

					IBlockState torchState = Blocks.torch.getStateFromMeta(5);
					ItemStartHouse.ReplaceBlock(world, pos.west(), torchState);

					torchCounter = 0;
				} else {
					BlockPos tempPos = pos.offset(facing);
					IBlockState surroundingState = world.getBlockState(tempPos);
					Block surroundingBlock = surroundingState.getBlock();

					if (!surroundingBlock.isBlockNormalCube()) {
						// This is not a solid block. Get the drops then replace
						// it with stone.
						ItemStartHouse.ConsolidateDrops(surroundingBlock, world, tempPos,
								surroundingState, originalStacks);

						ItemStartHouse.ReplaceBlock(world, tempPos, Blocks.stone);
					}
				}
			}

			// Get the block drops then replace it with a ladder.
			ItemStartHouse.ConsolidateDrops(block, world, pos, state, originalStacks);

			// Don't place a ladder at this location since it will be destroyed.
			if (pos.getY() != 9) {
				// Ladders by default face north, this is the way it should be.
				ItemStartHouse.ReplaceBlock(world, pos, Blocks.ladder);
			}

			pos = pos.down();
		}

		return originalStacks;
	}

	private static ArrayList<ItemStack> ConsolidateDrops(Block block, World world,
			BlockPos pos, IBlockState state, ArrayList<ItemStack> originalStacks) {
		for (ItemStack stack : block.getDrops(world, pos, state, 1)) {
			// Check to see if this stack's item is equal to an existing item
			// stack. If it is just add the count.
			Boolean foundStack = false;

			for (ItemStack existingStack : originalStacks) {
				if (ItemStack.areItemsEqual(existingStack, stack)) {
					// Make sure that this combined stack is at or smaller than
					// the max.
					if (existingStack.stackSize + stack.stackSize <= stack
							.getMaxStackSize()) {
						existingStack.stackSize = existingStack.stackSize
								+ stack.stackSize;
						foundStack = true;
						break;
					}
				}
			}

			if (!foundStack) {
				originalStacks.add(stack);
			}
		}

		return originalStacks;
	}

	private static ArrayList<ItemStack> CreateWall(World world, int height,
			int length, EnumFacing direction, BlockPos startingPosition,
			Block replacementBlock) {
		ArrayList<ItemStack> itemsDropped = new ArrayList<ItemStack>();

		BlockPos wallPos = startingPosition;

		// i height, j is the actual wall counter.
		for (int i = 0; i < height; i++) {
			// Reset wall building position to the starting position up by the
			// height counter.
			wallPos = startingPosition.up(i);

			for (int j = 0; j < length; j++) {
				for (ItemStack stack : world
						.getBlockState(wallPos)
						.getBlock()
						.getDrops(world, wallPos, world.getBlockState(wallPos),
								1)) {
					itemsDropped.add(stack);
				}

				// j is the north/south counter.
				ItemStartHouse.ReplaceBlock(world, wallPos, replacementBlock);

				wallPos = wallPos.offset(direction);
			}
		}

		return itemsDropped;
	}

	private static ArrayList<ItemStack> CreateWall(World world, int height,
			int length, EnumFacing direction, BlockPos startingPosition,
			IBlockState replacementBlock) {
		ArrayList<ItemStack> itemsDropped = new ArrayList<ItemStack>();

		BlockPos wallPos = startingPosition;

		// i height, j is the actual wall counter.
		for (int i = 0; i < height; i++) {
			// Reset wall building position to the starting position up by the
			// height counter.
			wallPos = startingPosition.up(i);

			for (int j = 0; j < length; j++) {
				for (ItemStack stack : world
						.getBlockState(wallPos)
						.getBlock()
						.getDrops(world, wallPos, world.getBlockState(wallPos),
								1)) {
					itemsDropped.add(stack);
				}

				// j is the north/south counter.
				ItemStartHouse.ReplaceBlock(world, wallPos, replacementBlock);

				wallPos = wallPos.offset(direction);
			}
		}

		return itemsDropped;
	}
}
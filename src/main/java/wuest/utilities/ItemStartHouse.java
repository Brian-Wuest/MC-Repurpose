package wuest.utilities;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemStartHouse extends Item
{
	public static ItemStartHouse RegisteredItem;
	
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
		
		GameRegistry.registerItem(ItemStartHouse.RegisteredItem, "itemStartHouse");
		WuestUtilities.ModItems.add(ItemStartHouse.RegisteredItem);
	}
	
	/**
	 * Does something when the item is right-clicked.
	 */
    @Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos hitBlockPos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
    	if (!world.isRemote)
    	{
	    	BlockPos playerPosition = player.getPosition();
	    	
	    	if (side == EnumFacing.UP)
	    	{
	    		IBlockState hitBlockState = world.getBlockState(hitBlockPos);
	    		
	    		if (hitBlockState != null)
	    		{
	    			Block hitBlock = hitBlockState.getBlock();
	    			
	    			if (hitBlock != null)
	    			{
	    				// We hit a block, let's start building!!!!!
	    				BlockPos startingPosition = hitBlockPos.up();
	    				
	    				// Clear the space before the user is teleported. This is in-case they right-click on a space that is only 1 block tall.
	    				this.ClearSpace(world, startingPosition);
	    				
	    				// Teleport the player to the middle of the house so they don't die while house is created.
	    				player.setPositionAndUpdate(startingPosition.up(2).getX(), startingPosition.up(2).getY(), startingPosition.up(2).getZ());
	    				
	    				// Build the basic structure.
	    				this.BuildStructure(world, startingPosition);
	    				
	    				// Build the interior.
	    				this.BuildInterior(world, startingPosition, player);
	    				
	    				// Set up the exterior.
	    				this.BuildExterior(world, startingPosition, player);
	    				
	    				if (WuestConfiguration.addMineShaft)
	    				{
		    				// Set up the mineshaft.
		    				this.PlaceMineShaft(world, startingPosition);
	    				}
	    				
	    				player.inventory.consumeInventoryItem(this);
	    			}
	    		}
	    		
	    		return true;
	    	}
    	}
    	
        return false;
    }
    
    public void ReplaceBlock(World world, BlockPos pos, Block replacementBlock)
    {
    	this.ReplaceBlock(world, pos, replacementBlock.getDefaultState(), 3);
    }
    
    public void ReplaceBlock(World world, BlockPos pos, IBlockState replacementBlockState)
    {
    	this.ReplaceBlock(world, pos, replacementBlockState, 3);
    }
    
    public void ReplaceBlock(World world, BlockPos pos, IBlockState replacementBlockState, int flags)
    {
    	world.setBlockToAir(pos);
		world.setBlockState(pos, replacementBlockState, flags);
    }

    private void ClearSpace(World world, BlockPos pos)
    {
    	// Clear a space (20X20X10) around the location the item was used on.
    	// Make sure to include the original block in this.
    	pos = pos.north(10).east(10);
    	BlockPos originalPos = pos;
    	
    	for (int z = 0; z <= 10; z++)
    	{
    		pos = originalPos.up(z);
    		
        	for (int i = 0; i <= 20; i++)
        	{
        		// i is the east/west counter.	
        		for (int j = 0; j <= 20; j++)
        		{
        			// j is the north/south counter.
        			world.setBlockToAir(pos);
        			
        			pos = pos.south();
        		}
        		
        		pos = pos.west();
        		pos = pos.north(21);
        	}
    	}
    }
    
    private void BuildStructure(World world, BlockPos startingPosition)
    {
    	// Make sure that the area beneath the house is all there. Don't want the house to be hanging in the air.
    	this.SetFloor(world, startingPosition, Blocks.dirt, 4, new ArrayList<ItemStack>());
    	
    	Block floor = null;
    	
    	switch (WuestConfiguration.floorBlock)
    	{
	    	case 1:
	    	{
	    		floor = Blocks.brick_block;
	    		break;
	    	}
	    	
	    	case 2:
	    	{
	    		floor = Blocks.sandstone;
	    		break;
	    	}
	    	
	    	default:
	    	{
	    		floor = Blocks.stonebrick;
	    		break;
	    	}
    	}
    	
		// Create the floor.
		this.SetFloor(world, startingPosition, floor, 4, new ArrayList<ItemStack>());
		
		// Create the walls.
		this.SetWalls(world, startingPosition, ((BlockPlanks) Blocks.planks).getStateFromMeta(WuestConfiguration.wallWoodType));
		
    	Block ceiling = null;
    	Block stairs = null;
    	
    	switch (WuestConfiguration.ceilingBlock )
    	{
	    	case 1:
	    	{
	    		ceiling = Blocks.brick_block;
	    		stairs = Blocks.brick_stairs;
	    		break;
	    	}
	    	
	    	case 2:
	    	{
	    		ceiling = Blocks.sandstone;
	    		stairs = Blocks.sandstone_stairs;
	    		break;
	    	}
	    	
	    	default:
	    	{
	    		ceiling = Blocks.stonebrick;
	    		stairs = Blocks.stone_brick_stairs;
	    		break;
	    	}
    	}
		
		// Set the ceiling.
		this.SetCeiling(world, startingPosition.up(4), ceiling, stairs);
    }
    
    private void BuildInterior(World world, BlockPos startingPosition, EntityPlayer player)
    {
    	// Keep the corner positions since they are important.
    	BlockPos northEastCornerPosition = startingPosition.north(4).east(4).up();
    	BlockPos southEastCornerPosition = startingPosition.south(4).east(4).up();
    	BlockPos northWestCornerPosition = startingPosition.north(4).west(4).up();
    	BlockPos southWestCornerPosition = startingPosition.south(4).west(4).up();
    	
    	// Use a separate position for each item.
    	BlockPos itemPosition = northEastCornerPosition;
    	
    	if (WuestConfiguration.addTorches)
    	{
	    	// Set the torch locations so it's not dark in the house.
	    	this.PlaceInsideTorches(world, northEastCornerPosition);
    	}
    	
    	// Create an oak door in the north east corner
    	this.DecorateDoor(world, northEastCornerPosition);
    	
    	if (WuestConfiguration.addBed)
    	{
    		// Place a bed in the north west corner.
    		this.PlaceBed(world, northWestCornerPosition);
    	}
    	
    	if (WuestConfiguration.addChest)
    	{
    		// Place a double chest in the south east corner.
    		this.PlaceAndFillChest(world, southEastCornerPosition);
    	}
    	
    	if (WuestConfiguration.addCrafingtable)
    	{
	    	// Place a crafting table in the south west corner.
	        this.PlaceAndFillCraftingMachines(world, southWestCornerPosition);
    	}
    }

    private void BuildExterior(World world, BlockPos startingPosition, EntityPlayer player)
    {
    	// Keep the corner positions since they are important.
    	// These positions are level with 1 block above the floor.
    	BlockPos northEastCornerPosition = startingPosition.north(4).east(4).up();
    	BlockPos southEastCornerPosition = startingPosition.south(4).east(4).up();
    	BlockPos northWestCornerPosition = startingPosition.north(4).west(4).up();
    	BlockPos southWestCornerPosition = startingPosition.south(4).west(4).up();
    	
    	if (WuestConfiguration.addTorches)
    	{
    		this.PlaceOutsideTorches(world, northEastCornerPosition);
    	}
    	
    	if (WuestConfiguration.addFarm)
    	{
    		this.PlaceSmallFarm(world, northEastCornerPosition.down());
    	}
    }
    
    private ArrayList<ItemStack> SetFloor(World world, BlockPos pos, Block block, int floorRadius, ArrayList<ItemStack> originalStack)
    {
    	// Go north 4 and east 4 from block position, this will be an 8 X 8 house.
    	pos = pos.north(floorRadius).east(floorRadius);
    	
    	for (int i = 0; i <= floorRadius*2; i++)
    	{
    		// i is the east/west counter.
    		for (int j = 0; j <= floorRadius*2; j++)
    		{
    			// Get the drops for this block (if any) and add it to the stack.
    			for (ItemStack stack : world.getBlockState(pos).getBlock().getDrops(world, pos, world.getBlockState(pos), 1))
				{
    				originalStack.add(stack);
				}
    			
    			// j is the north/south counter.
    			this.ReplaceBlock(world, pos, block);
    			
    			pos = pos.south();
    		}
    		
    		pos = pos.west();
    		pos = pos.north((floorRadius*2) + 1);
    	}
    	
    	return originalStack;
    }
    
    private void SetCeiling(World world, BlockPos pos, Block block, Block stairs)
    {
    	// If the ceiling is flat, call SetFloor since it's laid out the same.
    	if (WuestConfiguration.isCeilingFlat)
    	{
    		this.SetFloor(world, pos, block, 8, new ArrayList<ItemStack>());
    		return;
    	}
    	
    	// Get to the north east corner.
    	pos = pos.north(4).east(4);
    	
    	// Get the stairs state without the facing since it will change.
    	IBlockState stateWithoutFacing = stairs.getBlockState().getBaseState().withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT); 
    	
    	int wallLength = 7;
    	
    	while (wallLength > 0)
    	{
	    	for (int j = 0; j < 4; j++)
	    	{
	    		// I is the wall side starting on the east side.
				EnumFacing facing = EnumFacing.WEST;
				EnumFacing flowDirection = EnumFacing.SOUTH;
				
				switch (j)
				{
	    			case 1:
	    			{
	    				facing = EnumFacing.NORTH;
	    				flowDirection = EnumFacing.WEST;
	    				break;
	    			}
	    			
	    			case 2:
	    			{
	    				facing = EnumFacing.EAST;
	    				flowDirection = EnumFacing.NORTH;
	    				break;
	    			}
	    			
	    			case 3:
	    			{
	    				facing = EnumFacing.SOUTH;
	    				flowDirection = EnumFacing.EAST;
	    				break;
	    			}
				}
	    			
				for (int k = 0; k <= wallLength; k++)
				{
	    			// j is the north/south counter.
	    			this.ReplaceBlock(world, pos, stateWithoutFacing.withProperty(BlockStairs.FACING, facing));
	    			
	    			pos = pos.offset(flowDirection);
				}
	    	}
	    	
	    	pos = pos.west().south().up();
	    	wallLength = wallLength - 2;
    	}
    	
    	this.ReplaceBlock(world, pos, block);
    	
    	IBlockState blockState = Blocks.torch.getStateFromMeta(5);
    	this.ReplaceBlock(world, pos.up(), blockState);
    }
    
    private void SetWalls(World world, BlockPos pos, IBlockState block)
    {
    	// Get the north east corner.
    	pos = pos.north(4).east(4);
    	
    	for (int i = 0; i <= 4; i++)
    	{
    		// i height, j is a facing, k is the actual wall counter.
    		for (int j = 0; j <= 3; j++)
    		{
    			EnumFacing facing = EnumFacing.SOUTH;
    			int offsetCount = 7;
    			
    			switch (j)
    			{
	    			case 1:
	    			{
	    				facing = EnumFacing.WEST;
	    				offsetCount = 7;
	    				break;
	    			}
	    			
	    			case 2:
	    			{
	    				facing = EnumFacing.NORTH;
	    				offsetCount = 7;
	    				break;
	    			}
	    			
	    			case 3:
	    			{
	    				facing = EnumFacing.EAST;
	    				offsetCount = 7;
	    				break;
	    			}
    			}
    			
    			for (int k = 0; k <= offsetCount; k++)
    			{
	    			// j is the north/south counter.
	    			this.ReplaceBlock(world, pos, block);
	    			
	    			pos = pos.offset(facing);
    			}
    		}

    		pos = pos.up();
    	}
    }

    private void PlaceInsideTorches(World world, BlockPos cornerPosition)
    {
    	// Use a separate position for each item.
    	BlockPos itemPosition = cornerPosition;
    	
    	/*
    	 * Torch Facings
    	 * 1 = East
    	 * 2 = West
    	 * 3 = South
    	 * 4 = North
    	 * 5 = Up
    	 */
    	// East wall torch.
    	itemPosition = itemPosition.south(4).west().up(1);
    	IBlockState blockState = Blocks.torch.getStateFromMeta(2);
    	this.ReplaceBlock(world, itemPosition, blockState);
    	
    	// North Wall torch.
    	itemPosition = cornerPosition.west(4).south().up(1);
    	blockState = Blocks.torch.getStateFromMeta(3);
    	this.ReplaceBlock(world, itemPosition, blockState);
    	
    	// West wall torch.
    	itemPosition = cornerPosition.west(7).south(4).up(1);
    	blockState = Blocks.torch.getStateFromMeta(1);
    	this.ReplaceBlock(world, itemPosition, blockState);
    	
    	// South wall torch.
    	itemPosition = cornerPosition.south(7).west(4).up(1);
    	blockState = Blocks.torch.getStateFromMeta(4);
    	this.ReplaceBlock(world, itemPosition, blockState);
    }

    private void DecorateDoor(World world, BlockPos cornerPosition)
    {
    	BlockPos itemPosition = cornerPosition.west();;
    	world.setBlockToAir(itemPosition.up());
    	
    	Block door = null;
    	Block stairs = null;
    	
    	switch (WuestConfiguration.wallWoodType)
    	{
	    	case 1:
	    	{
	    		door = Blocks.spruce_door;
	    		stairs = Blocks.spruce_stairs;
	    		break;
	    	}
	    	
	    	case 2:
	    	{	
	    		door = Blocks.birch_door;
	    		stairs = Blocks.birch_stairs;
	    		break;
	    	}
	    	
	    	case 3:
	    	{	
	    		door = Blocks.jungle_door;
	    		stairs = Blocks.jungle_stairs;
	    		break;
	    	}
	    	
	    	case 4:
	    	{
	    		door = Blocks.acacia_door;
	    		stairs = Blocks.acacia_stairs;
	    		break;
	    	}
	    	
	    	case 5:
	    	{	
	    		door = Blocks.dark_oak_door;
	    		stairs = Blocks.dark_oak_stairs;
	    		break;
	    	}
	    	
	    	default:
	    	{	
	    		door = Blocks.oak_door;
	    		stairs = Blocks.oak_stairs;
	    		break;
	    	}
    	}
    	
    	ItemDoor.placeDoor(world, itemPosition, EnumFacing.NORTH, door);
    	
    	// Put a glass pane above the door.
    	this.ReplaceBlock(world, itemPosition.up(2), Blocks.glass_pane);
    	
    	// Create a pressure plate for the door, no need to re-set the item position here since it needs to be in relation to the door.
    	itemPosition = itemPosition.south();
    	this.ReplaceBlock(world, itemPosition, Blocks.wooden_pressure_plate);
    	
    	// Place a stairs.
    	itemPosition = itemPosition.north(2).down();
    	this.ReplaceBlock(world, itemPosition, stairs.getBlockState().getBaseState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT));
    }

    private void PlaceBed(World world, BlockPos cornerPosition)
    {
    	BlockPos itemPosition = cornerPosition.east(1).south(2);
    	
        IBlockState bedFootState = Blocks.bed.getDefaultState().withProperty(BlockBed.OCCUPIED, Boolean.valueOf(false)).withProperty(BlockBed.FACING, EnumFacing.NORTH).withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);

        if (world.setBlockState(itemPosition, bedFootState, 3))
        {
            IBlockState bedHeadState = bedFootState.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD);
            world.setBlockState(itemPosition.north(), bedHeadState, 3);
        }
    	
    }
    
    private void PlaceAndFillChest(World world, BlockPos cornerPosition)
    {
    	// Create a double wide chest.
    	BlockPos itemPosition = cornerPosition.north().west();
    	this.ReplaceBlock(world, itemPosition, Blocks.chest);
    	
    	itemPosition = itemPosition.west();
    	this.ReplaceBlock(world, itemPosition, Blocks.chest);
    	
    	if (WuestConfiguration.addChestContents)
    	{
	    	// Add each stone tool to the chest and leather armor.
	    	TileEntity tileEntity = world.getTileEntity(itemPosition);
	    	
	    	if (tileEntity instanceof TileEntityChest)
	    	{
	    		TileEntityChest chestTile = (TileEntityChest)tileEntity;
	    		
	    		// Add the weapons.
	    		chestTile.setInventorySlotContents(0, new ItemStack(Items.stone_axe));
	    		chestTile.setInventorySlotContents(1, new ItemStack(Items.stone_hoe));
	    		chestTile.setInventorySlotContents(2, new ItemStack(Items.stone_pickaxe));
	    		chestTile.setInventorySlotContents(3, new ItemStack(Items.stone_shovel));
	    		chestTile.setInventorySlotContents(4, new ItemStack(Items.stone_sword));
	    		
	    		// Add the armor.
	    		chestTile.setInventorySlotContents(5, new ItemStack(Items.leather_boots));
	    		chestTile.setInventorySlotContents(6, new ItemStack(Items.leather_chestplate));
	    		chestTile.setInventorySlotContents(7, new ItemStack(Items.leather_helmet));
	    		chestTile.setInventorySlotContents(8, new ItemStack(Items.leather_leggings));
	    		
	    		// Add some bread.
	    		chestTile.setInventorySlotContents(9, new ItemStack(Items.bread, 20));
	    		
	    		// Add potatoes.
	    		chestTile.setInventorySlotContents(10, new ItemStack(Items.potato, 3));
	    		
	    		// Add carrots.
	    		chestTile.setInventorySlotContents(11, new ItemStack(Items.carrot, 3));
	    		
	    		// Add seeds.
	    		chestTile.setInventorySlotContents(12, new ItemStack(Items.wheat_seeds, 3));
	    	}
    	}
    }

    private void PlaceAndFillCraftingMachines(World world, BlockPos cornerPosition)
    {
    	BlockPos itemPosition = cornerPosition.east().north();
    	this.ReplaceBlock(world, itemPosition, Blocks.crafting_table);
    	
    	// Place a furnace next to the crafting table and fill it with 20 coal.
    	itemPosition =itemPosition.east();
    	this.ReplaceBlock(world, itemPosition, Blocks.furnace.getDefaultState().withProperty(BlockFurnace.FACING, EnumFacing.NORTH));

    	TileEntity tileEntity = world.getTileEntity(itemPosition);

    	if (tileEntity instanceof TileEntityFurnace)
		{
    		TileEntityFurnace furnaceTile = (TileEntityFurnace)tileEntity;
		    furnaceTile.setInventorySlotContents(1, new ItemStack(Items.coal, 20));
		}
    }

    private void PlaceOutsideTorches(World world, BlockPos cornerPosition)
    {
    	cornerPosition = cornerPosition.north();
    	BlockPos itemPosition = cornerPosition;
    	
    	/*
    	 * Torch Facings
    	 * 1 = East
    	 * 2 = West
    	 * 3 = South
    	 * 4 = North
    	 * 5 = Up
    	 */
    	// Set north east corner.
    	IBlockState blockState = Blocks.torch.getStateFromMeta(4);
    	this.ReplaceBlock(world, itemPosition, blockState);
    	
    	// North middle
    	itemPosition = itemPosition.west(4);
    	blockState = Blocks.torch.getStateFromMeta(4);
    	this.ReplaceBlock(world, itemPosition, blockState);
    	
    	// North west, facing north.
    	itemPosition = itemPosition.west(4);
    	blockState = Blocks.torch.getStateFromMeta(4);
    	this.ReplaceBlock(world, itemPosition, blockState);
    	
    	// North west, facing west.
    	itemPosition = itemPosition.west().south();
    	blockState = Blocks.torch.getStateFromMeta(2);
    	this.ReplaceBlock(world, itemPosition, blockState);
    	
    	// West middle.
    	itemPosition = itemPosition.south(4);
    	blockState = Blocks.torch.getStateFromMeta(2);
    	this.ReplaceBlock(world, itemPosition, blockState);
    	
    	// South west facing west.
    	itemPosition = itemPosition.south(4);
    	blockState = Blocks.torch.getStateFromMeta(2);
    	this.ReplaceBlock(world, itemPosition, blockState);
    	
    	// South west facing south.
    	itemPosition = itemPosition.south().east();
    	blockState = Blocks.torch.getStateFromMeta(3);
    	this.ReplaceBlock(world, itemPosition, blockState);
    	
    	// South middle.
    	itemPosition = itemPosition.east(4);
    	blockState = Blocks.torch.getStateFromMeta(3);
    	this.ReplaceBlock(world, itemPosition, blockState);
    	
    	// South east facing south.
    	itemPosition = itemPosition.east(4);
    	blockState = Blocks.torch.getStateFromMeta(3);
    	this.ReplaceBlock(world, itemPosition, blockState);
    	
    	// South east facing east.
    	itemPosition = itemPosition.east().north();
    	blockState = Blocks.torch.getStateFromMeta(1);
    	this.ReplaceBlock(world, itemPosition, blockState);
    	
    	// East middle.
    	itemPosition = itemPosition.north(4);
    	blockState = Blocks.torch.getStateFromMeta(1);
    	this.ReplaceBlock(world, itemPosition, blockState);
    	
    	// North East facing east
    	itemPosition = itemPosition.north(4);
    	blockState = Blocks.torch.getStateFromMeta(1);
    	this.ReplaceBlock(world, itemPosition, blockState);
    	
    	// Roof Torches
    	// Re-set the corner position to be on the roof.
    	cornerPosition = cornerPosition.south().up(4);
    	
    	// North east corner.
    	itemPosition = cornerPosition;
    	blockState = Blocks.torch.getStateFromMeta(5);
    	this.ReplaceBlock(world, itemPosition, blockState);
    	
    	// East middle.
    	itemPosition = itemPosition.south(4);
    	blockState = Blocks.torch.getStateFromMeta(5);
    	this.ReplaceBlock(world, itemPosition, blockState);
    	
    	// South east corner
    	itemPosition = itemPosition.south(4);
    	blockState = Blocks.torch.getStateFromMeta(5);
    	this.ReplaceBlock(world, itemPosition, blockState);
    	
    	// South middle
    	itemPosition = itemPosition.west(4);
    	blockState = Blocks.torch.getStateFromMeta(5);
    	this.ReplaceBlock(world, itemPosition, blockState);
    	
    	// South west corner.
    	itemPosition = itemPosition.west(4);
    	blockState = Blocks.torch.getStateFromMeta(5);
    	this.ReplaceBlock(world, itemPosition, blockState);
    	
    	// West middle
    	itemPosition = itemPosition.north(4);
    	blockState = Blocks.torch.getStateFromMeta(5);
    	this.ReplaceBlock(world, itemPosition, blockState);
    	
    	// North West corner.
    	itemPosition = itemPosition.north(4);
    	blockState = Blocks.torch.getStateFromMeta(5);
    	this.ReplaceBlock(world, itemPosition, blockState);
    	
    	// North middle
    	itemPosition = itemPosition.east(4);
    	blockState = Blocks.torch.getStateFromMeta(5);
    	this.ReplaceBlock(world, itemPosition, blockState);
    }

    private void PlaceSmallFarm(World world, BlockPos cornerPosition)
    {
    	BlockPos farmStart = cornerPosition.north(4).west(5);
    	IBlockState state = world.getBlockState(farmStart);
    	
    	// Keep going down until we get to the surface.
    	while (!state.getBlock().isAir(world, farmStart))
    	{
    		farmStart.down();
    	}
    	
    	farmStart = farmStart.down();
    	
    	// We are now at the surface and this is where the first water source will be.    	
    	this.ReplaceBlock(world, farmStart, Blocks.water);
    	this.ReplaceBlock(world, farmStart.down(), Blocks.dirt);
    	this.ReplaceBlock(world, farmStart.up(), Blocks.glass);
    	this.ReplaceBlock(world, farmStart.up(2), Blocks.torch);
    	this.ReplaceBlock(world, farmStart.north(), Blocks.farmland);
    	this.ReplaceBlock(world, farmStart.north().west(), Blocks.farmland);
    	this.ReplaceBlock(world, farmStart.west(), Blocks.farmland);
    	this.ReplaceBlock(world, farmStart.south(), Blocks.farmland);
    	this.ReplaceBlock(world, farmStart.south().west(), Blocks.farmland);
    	
    	farmStart = farmStart.east();
    	
    	this.ReplaceBlock(world, farmStart, Blocks.water);
    	this.ReplaceBlock(world, farmStart.down(), Blocks.dirt);
    	this.ReplaceBlock(world, farmStart.up(), Blocks.glass);
    	this.ReplaceBlock(world, farmStart.up(2), Blocks.torch);
    	this.ReplaceBlock(world, farmStart.north(), Blocks.farmland);
    	this.ReplaceBlock(world, farmStart.south(), Blocks.farmland);
    	
    	farmStart = farmStart.east();
    	
    	this.ReplaceBlock(world, farmStart, Blocks.water);
    	this.ReplaceBlock(world, farmStart.down(), Blocks.dirt);
    	this.ReplaceBlock(world, farmStart.up(), Blocks.glass);
    	this.ReplaceBlock(world, farmStart.up(2), Blocks.torch);
    	this.ReplaceBlock(world, farmStart.north(), Blocks.farmland);
    	this.ReplaceBlock(world, farmStart.north().east(), Blocks.farmland);
    	this.ReplaceBlock(world, farmStart.east(), Blocks.farmland);
    	this.ReplaceBlock(world, farmStart.south(), Blocks.farmland);
    	this.ReplaceBlock(world, farmStart.south().east(), Blocks.farmland);
    }

    private void PlaceMineShaft(World world, BlockPos pos)
    {
    	// The initial position is where the character was teleported too, go back 3 blocks and start building the mine shaft.
    	pos = pos.south(3);
    	
    	// Keep track of all of the items to add to the chest at the end of the shaft.
    	ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
    	
    	stacks = this.CreateLadderShaft(world, pos, stacks);
    	
    	pos = pos.down(pos.getY() - 10);
    	
    	ArrayList<ItemStack> tempStacks = new ArrayList<ItemStack>();
    	
    	// The entire ladder has been created. Create a platform at this level and place a chest next to the ladder.
    	tempStacks = this.SetFloor(world, pos, Blocks.stone, 3, tempStacks);
    	
    	// Now that the floor has been set, go up 1 block to star creating the walls.
    	pos = pos.up();
    	
    	// Clear a space around the ladder pillar and make walls. The walls are necessary if there is a lot of lava down here.
    	// Make a wall of air then a wall of stone.
    	
    	// South wall.
    	tempStacks.addAll(this.CreateWall(world, 3, 3, EnumFacing.EAST, pos.south(2).west(), Blocks.air));
    	tempStacks.addAll(this.CreateWall(world, 3, 3, EnumFacing.EAST, pos.south(3).west(), Blocks.stone));
    	
    	// East wall.
    	tempStacks.addAll(this.CreateWall(world, 3, 4, EnumFacing.NORTH, pos.south(2).east(), Blocks.air));
    	tempStacks.addAll(this.CreateWall(world, 3, 4, EnumFacing.NORTH, pos.south(2).east(2), Blocks.stone));
    	
    	// North wall.
    	tempStacks.addAll(this.CreateWall(world, 3, 3, EnumFacing.WEST, pos.north().east(), Blocks.air));
    	tempStacks.addAll(this.CreateWall(world, 3, 3, EnumFacing.WEST, pos.north(2).east(), Blocks.stone));
    	
    	// West wall.
    	tempStacks.addAll(this.CreateWall(world, 3, 4, EnumFacing.SOUTH, pos.north().west(), Blocks.air));
    	tempStacks.addAll(this.CreateWall(world, 3, 4, EnumFacing.SOUTH, pos.north(1).west(2), Blocks.stone));
    	
    	// Consolidate the stacks.
    	for (ItemStack tempStack : tempStacks)
    	{
    		Boolean foundStack = false;
			
			for (ItemStack existingStack : stacks)
			{
				if (ItemStack.areItemsEqual(existingStack, tempStack))
				{
					// Make sure that this combined stack is at or smaller than the max.
					if (existingStack.stackSize + tempStack.stackSize <= tempStack.getMaxStackSize())
					{
						existingStack.stackSize = existingStack.stackSize + tempStack.stackSize;
						foundStack = true;
						break;
					}
				}
			}
			
			if (!foundStack)
			{
				stacks.add(tempStack);
			}
    	}
    	
    	// Place a torch to the left of the  ladder.
    	IBlockState blockState = Blocks.torch.getStateFromMeta(5);
    	this.ReplaceBlock(world, pos.west(), blockState);
    	
    	// Place a chest to the right of the ladder.
    	this.ReplaceBlock(world, pos.east(), Blocks.chest);
    	TileEntity tileEntity = world.getTileEntity(pos.east());
    	
    	if (tileEntity instanceof TileEntityChest)
    	{
    		TileEntityChest chestTile = (TileEntityChest)tileEntity;
    		
    		int i = 0;
    		// All of the stacks should be consolidated at this point.
    		for(ItemStack stack : stacks)
    		{
    			chestTile.setInventorySlotContents(i, stack);
    			i++;
    		}
    	}
    }
    
    private ArrayList<ItemStack> CreateLadderShaft(World world, BlockPos pos, ArrayList<ItemStack> originalStacks)
    {
    	int torchCounter = 0;
    	
    	while (pos.getY() > 8)
    	{
    		IBlockState state = world.getBlockState(pos);    		
    		Block block = state.getBlock();
    		torchCounter++;
    		
    		// Make sure all blocks around this one are solid, if they are not replace them with stone.
    		for (int i = 0; i < 4; i++)
    		{
    			EnumFacing facing = EnumFacing.NORTH;
    			
    			switch (i)
    			{
	    			case 1:
	    			{
	    				facing = EnumFacing.EAST;
	    				break;
	    			}
	    			case 2:
	    			{
	    				facing = EnumFacing.SOUTH;
	    				break;
	    			}
	    			case 3:
	    			{
	    				facing = EnumFacing.WEST;
	    				break;
	    			}
    				default:
    				{
    					facing = EnumFacing.NORTH;
    				}
    			}
    			
    			// Every 6 blocks, place a torch on the west wall.
    			// If we are close to the bottom, don't place a torch. Do the normal processing.
    			if (facing == EnumFacing.WEST && torchCounter == 6 && pos.getY() > 14)
    			{
    				// First make sure the block behind this block is stone, then place the torch.
    				BlockPos tempPos = pos.offset(facing, 2);
	    			IBlockState surroundingState = world.getBlockState(tempPos);
	    			Block surroundingBlock = surroundingState.getBlock();
	    			
	    			if (!surroundingBlock.isBlockNormalCube())
	    			{
	    				// This is not a solid block. Get the drops then replace it with stone.
	    				this.ConsolidateDrops(surroundingBlock, world, tempPos, surroundingState, originalStacks);
	    				
	    				this.ReplaceBlock(world, tempPos, Blocks.stone);
	    			}
	    			
	    			IBlockState torchState = Blocks.torch.getStateFromMeta(5);
	    	    	this.ReplaceBlock(world, pos.west(), torchState);
	    	    	
	    	    	torchCounter = 0;
    			}
    			else
    			{
	    			BlockPos tempPos = pos.offset(facing);
	    			IBlockState surroundingState = world.getBlockState(tempPos);
	    			Block surroundingBlock = surroundingState.getBlock();
	    			
	    			if (!surroundingBlock.isBlockNormalCube())
	    			{
	    				// This is not a solid block. Get the drops then replace it with stone.
	    				this.ConsolidateDrops(surroundingBlock, world, tempPos, surroundingState, originalStacks);
	    				
	    				this.ReplaceBlock(world, tempPos, Blocks.stone);
	    			}
    			}
    		}
    		
    		// Get the block drops then replace it with a ladder.
    		this.ConsolidateDrops(block, world, pos, state, originalStacks);
    		
    		// Don't place a ladder at this location since it will be destroyed.
    		if (pos.getY() != 9)
    		{
	    		// Ladders by default face north, this is the way it should be.
	    		this.ReplaceBlock(world, pos, Blocks.ladder);
    		}
    		
    		pos = pos.down();
    	}
    	
    	return originalStacks;
    }
    
    private ArrayList<ItemStack> ConsolidateDrops(Block block, World world, BlockPos pos, IBlockState state, ArrayList<ItemStack> originalStacks)
    {
		for(ItemStack stack : block.getDrops(world, pos, state, 1))
		{
			// Check to see if this stack's item is equal to an existing item stack. If it is just add the count.
			Boolean foundStack = false;
			
			for (ItemStack existingStack : originalStacks)
			{
				if (ItemStack.areItemsEqual(existingStack, stack))
				{
					// Make sure that this combined stack is at or smaller than the max.
					if (existingStack.stackSize + stack.stackSize <= stack.getMaxStackSize())
					{
						existingStack.stackSize = existingStack.stackSize + stack.stackSize;
						foundStack = true;
						break;
					}
				}
			}
			
			if (!foundStack)
			{
				originalStacks.add(stack);
			}
		}
		
		return originalStacks;
    }
    
    private ArrayList<ItemStack> CreateWall(World world, int height, int length, EnumFacing direction, BlockPos startingPosition, Block replacementBlock)
    {
    	ArrayList<ItemStack> itemsDropped = new ArrayList<ItemStack>();
    	
    	BlockPos wallPos = startingPosition;
    	
    	// i height, j is the actual wall counter.
    	for (int i = 0; i < height; i++)
    	{
    		// Reset wall building position to the starting position up by the height counter.
    		wallPos = startingPosition.up(i);
    		
			for (int j = 0; j < length; j++)
			{
				for (ItemStack stack : world.getBlockState(wallPos).getBlock().getDrops(world, wallPos, world.getBlockState(wallPos), 1))
				{
					itemsDropped.add(stack);
				}
				
    			// j is the north/south counter.
    			this.ReplaceBlock(world, wallPos, replacementBlock);
    			
    			wallPos = wallPos.offset(direction);
			}
    	}
    	
    	return itemsDropped;
    }
}
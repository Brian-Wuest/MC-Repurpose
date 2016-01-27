package wuest.utilities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockStairs;
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
import net.minecraft.world.World;
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
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
    	BlockPos playerPosition = player.getPosition();
    	MovingObjectPosition varX = player.rayTrace(100.0D, 1.0F);
    	
    	if (varX.typeOfHit == MovingObjectType.BLOCK && varX.sideHit == EnumFacing.UP)
    	{
    		BlockPos hitBlockPos = varX.getBlockPos();
    		IBlockState hitBlockState = world.getBlockState(hitBlockPos);
    		
    		if (hitBlockState != null)
    		{
    			Block hitBlock = hitBlockState.getBlock();
    			
    			if (hitBlock != null)
    			{
    				// We hit a block, let's start building!!!!!
    				BlockPos startingPosition = hitBlockPos.up();
    				
    				// Teleport the player to the middle of the house so they don't die while house is created.
    				player.setPositionAndUpdate(startingPosition.up(2).getX(), startingPosition.up().getY(), startingPosition.up().getZ());
    				
    				this.ClearSpace(world, startingPosition);
    				
    				// Build the basic structure.
    				this.BuildStructure(world, startingPosition);
    				
    				// Build the interior.
    				this.BuildInterior(world, startingPosition, player);
    				
    				// Set up the exterior.
    				this.BuildExterior(world, startingPosition, player);
    				
    				player.inventory.consumeInventoryItem(this);
    			}
    		}
    	}
    	
        return stack;
    }
    
    public void ReplaceBlock(World world, BlockPos pos, Block replacementBlock)
    {
    	this.ReplaceBlock(world, pos, replacementBlock.getDefaultState(), 0);
    }
    
    public void ReplaceBlock(World world, BlockPos pos, IBlockState replacementBlockState)
    {
    	this.ReplaceBlock(world, pos, replacementBlockState, 0);
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
		// Create the floor.
		this.SetFloor(world, startingPosition);
		
		// Create the walls.
		this.SetWalls(world, startingPosition);
		
		// Set the ceiling.
		this.SetFloor(world, startingPosition.up(4));
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
    
    private void SetFloor(World world, BlockPos pos)
    {
    	// Go north 4 and east 4 from block position, this will be an 8 X 8 house.
    	pos = pos.north(4).east(4);
    	
    	for (int i = 0; i <= 8; i++)
    	{
    		// i is the east/west counter.
    		for (int j = 0; j <= 8; j++)
    		{
    			// j is the north/south counter.
    			this.ReplaceBlock(world, pos, Blocks.stonebrick);
    			
    			pos = pos.south();
    		}
    		
    		pos = pos.west();
    		pos = pos.north(9);
    	}
    }
    
    private void SetWalls(World world, BlockPos pos)
    {
    	// Get the north east corner.
    	pos = pos.north(4).east(4);
    	
    	for (int i = 0; i <= 4; i++)
    	{
    		// i height, j is a facing, k is the acual wall counter.
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
	    			this.ReplaceBlock(world, pos, Blocks.planks);
	    			
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
    	ItemDoor.placeDoor(world, itemPosition, EnumFacing.NORTH, Blocks.oak_door);
    	
    	// Put a glass pane above the door.
    	this.ReplaceBlock(world, itemPosition.up(2), Blocks.glass_pane);
    	
    	// Create a pressure plate for the door, no need to re-set the item position here since it needs to be in relation to the door.
    	itemPosition = itemPosition.south();
    	this.ReplaceBlock(world, itemPosition, Blocks.wooden_pressure_plate);
    	
    	// Place a stairs.
    	itemPosition = itemPosition.north(2).down();
    	this.ReplaceBlock(world, itemPosition, Blocks.oak_stairs.getBlockState().getBaseState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT));
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
    	this.ReplaceBlock(world, itemPosition, Blocks.furnace.getDefaultState().withProperty(BlockFurnace.FACING, EnumFacing.NORTH), 3);

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
}
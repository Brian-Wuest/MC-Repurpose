package wuest.utilities.Blocks;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wuest.utilities.Proxy.CommonProxy;

/**
 * This class defines a set of grass stairs.
 * @author WuestMan
 *
 */
public class BlockGrassStairs extends BlockStairs
{
	public static BlockGrassStairs RegisteredBlock;
	
	public BlockGrassStairs()
	{
		super(Blocks.grass.getDefaultState());
		
		Color test = new Color(this.blockMapColor.colorValue);
		
		this.useNeighborBrightness = true;
		CommonProxy.setBlockName(this, "blockGrassStairs");
	}
    
	/**
	 * Registers this block in the game registry and sets the static field.
	 */
	public static void RegisterBlock()
	{
		BlockGrassStairs.RegisteredBlock = new BlockGrassStairs();
		//ItemGrassStairs.RegisterItem(); 
		//CommonProxy.registerBlock(BlockGrassStairs.RegisteredBlock, ItemGrassStairs.RegisteredItem);
		CommonProxy.registerBlock(BlockGrassStairs.RegisteredBlock);
		
		// Register the block and add the recipe for it.
		GameRegistry.addRecipe(
				new ItemStack(BlockGrassStairs.RegisteredBlock, 4),
				"  x",
				" xx",
				"xxx",
				'x', Blocks.dirt);
		
		GameRegistry.addRecipe(
				new ItemStack(Blocks.grass, 3),
				"x",
				"x",
				'x', BlockGrassStairs.RegisteredBlock);
	}

	@SideOnly(Side.CLIENT)
	public static void RegisterBlockRenderer()
	{
		// Register the block renderer.
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor()
		{
			public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex)
			{
				return worldIn != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(worldIn, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D);
			}
		}, new Block[] {BlockGrassStairs.RegisteredBlock});

		// Register the item renderer.
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor()
		{
			public int getColorFromItemstack(ItemStack stack, int tintIndex)
			{
				// Get the item for this stack.
				Item item = stack.getItem();
				
				if (item instanceof ItemBlock)
				{
					// Get the block for this item and determine if it's a grass stairs.
					ItemBlock itemBlock = (ItemBlock)item;
					
					if (itemBlock.getBlock() instanceof BlockGrassStairs)
					{
						BlockPos pos = Minecraft.getMinecraft().thePlayer.getPosition();
						WorldClient world = Minecraft.getMinecraft().theWorld;
						return pos != null ? BiomeColorHelper.getGrassColorAtPos(world, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D);
					}
				}
				
				return -1;
			}
		}, new Block[] { BlockGrassStairs.RegisteredBlock });
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.SOLID;
    }
	
    /**
     * Get the MapColor for this Block and the given BlockState
     */
	@Override
	@SideOnly(Side.CLIENT)
	public MapColor getMapColor(IBlockState state)
    {
        return this.blockMapColor;
    }
}

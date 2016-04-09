package wuest.utilities.Blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDoubleDirtSlab extends BlockDirtSlab
{
	@Override
	public boolean isDouble()
	{
		return true;
	}
}

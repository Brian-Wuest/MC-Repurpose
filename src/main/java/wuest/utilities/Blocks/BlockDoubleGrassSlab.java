package wuest.utilities.Blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockDoubleGrassSlab extends BlockGrassSlab
{
	@Override
	public boolean isDouble()
	{
		return true;
	}
	
	@Override
    protected ItemStack createStackedBlock(IBlockState state)
    {
        Item item = Item.getItemFromBlock(BlockGrassSlab.RegisteredHalfBlock);

        if (item == null)
        {
            return null;
        }
        else
        {
            int i = 0;

            if (item.getHasSubtypes())
            {
                i = this.getMetaFromState(state);
            }

            return new ItemStack(item, 2, i);
        }
    }
}

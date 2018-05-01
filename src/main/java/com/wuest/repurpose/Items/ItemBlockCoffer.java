package com.wuest.repurpose.Items;

import java.util.Locale;

import com.wuest.repurpose.Blocks.BlockCoffer.IronChestType;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockCoffer extends ItemBlock
{
    public ItemBlockCoffer(Block block)
    {
        super(block);

        this.setRegistryName(block.getRegistryName());
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        return "tile.repurpose.chest." + IronChestType.VALUES[itemstack.getMetadata()].name().toLowerCase(Locale.US);
    }
}
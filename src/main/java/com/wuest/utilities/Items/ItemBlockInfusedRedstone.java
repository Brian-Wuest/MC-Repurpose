package com.wuest.utilities.Items;

import java.util.ArrayList;

import com.wuest.utilities.ModRegistry;
import com.wuest.utilities.Base.ItemBlockCapability;

import net.minecraft.block.Block;
import net.minecraftforge.common.capabilities.Capability;

/**
 * This is the item block used in the infused redstone feature.
 * @author WuestMan
 */
public class ItemBlockInfusedRedstone extends ItemBlockCapability
{

	/**
	 * Initializes a new instance of the ItemBlockInfusedRedstone class.
	 * @param block The parent block for this item.
	 */
	public ItemBlockInfusedRedstone(Block block, String name)
	{
		super(block, new ArrayList<Capability>());
		
		this.allowedCapabilities.add(ModRegistry.BlockModel);
		
		ModRegistry.setItemName(this, name);
	}
	
	/**
	 * Adds the allowed capabilities during item initialization.
	 */
    @Override
	protected void addAllowedCapabilities()
    {
    	this.getAllowedCapabilities().add(ModRegistry.BlockModel);
    }

}
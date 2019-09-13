package com.wuest.repurpose.Blocks;

import net.minecraft.item.ItemGroup;

/*!
Filename: c:\Users\Brian\Documents\GitHub\MC-Repurpose\src\main\java\com\wuest\repurpose\Blocks\ModBlock.java
Path: c:\Users\Brian\Documents\GitHub\MC-Repurpose\src\main\java\com\wuest\repurpose\Blocks
Created Date: Wednesday, September 11th 2019, 8:29:40 pm
Author: Brian

Copyright (c) 2019 Your Company
 */
public interface IModBlock {
	default ItemGroup getItemGroup() {
		return ItemGroup.BUILDING_BLOCKS;
	}
}
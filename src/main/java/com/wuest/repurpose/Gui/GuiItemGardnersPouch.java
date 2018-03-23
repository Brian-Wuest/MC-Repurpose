package com.wuest.repurpose.Gui;

import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Items.Containers.ItemGardnersPouchContainer;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

public class GuiItemGardnersPouch extends GuiContainer
{
	public static final int GUI_ID = 8;
	
	@SuppressWarnings("unused")
	private IItemHandler i;

	public GuiItemGardnersPouch(IItemHandler i, EntityPlayer p)
	{
		super(new ItemGardnersPouchContainer(i, p));

		this.xSize = 175;
		this.ySize = 221;
		this.i = i;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}
}
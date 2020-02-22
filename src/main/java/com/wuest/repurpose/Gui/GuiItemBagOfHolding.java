package com.wuest.repurpose.Gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.wuest.repurpose.Capabilities.ItemBagOfHoldingProvider;
import com.wuest.repurpose.Items.Containers.BagOfHoldingContainer;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.items.IItemHandler;

public class GuiItemBagOfHolding extends ContainerScreen<BagOfHoldingContainer> {
	public static final int GUI_ID = 8;

	private IItemHandler itemHandler;

	public GuiItemBagOfHolding(IItemHandler itemHandler, PlayerEntity p) {
		super(new BagOfHoldingContainer(p.inventory), p.inventory, new StringTextComponent("Bag of Holding"));

		this.xSize = 175;
		this.ySize = 221;
		this.itemHandler = itemHandler;
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	@Override
	public void removed() {
		super.removed();

		if (this.minecraft.player != null && this.itemHandler instanceof ItemBagOfHoldingProvider) {
			((ItemBagOfHoldingProvider) this.itemHandler).UpdateStack(this.minecraft.player.getHeldItemOffhand());
		}
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the
	 * items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.font.drawString("Pouch", 8, 6, 4210752);
		this.font.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/generic_54.png"));
		this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}
}
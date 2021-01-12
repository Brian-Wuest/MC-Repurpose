package com.wuest.repurpose.Gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.wuest.repurpose.Capabilities.ItemBagOfHoldingProvider;
import com.wuest.repurpose.Items.Containers.BagOfHoldingContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.items.IItemHandler;

public class GuiItemBagOfHolding extends ContainerScreen<BagOfHoldingContainer> {

	private IItemHandler itemHandler;

	public GuiItemBagOfHolding(BagOfHoldingContainer container, PlayerInventory itemHandler, ITextComponent textComponent) {
		super(container, itemHandler, new StringTextComponent("Bag of Holding"));

		this.xSize = 175;
		this.ySize = 221;
		ItemStack offHandStack = itemHandler.offHandInventory.get(0);

		this.itemHandler = ItemBagOfHoldingProvider.GetFromStack(offHandStack);
	}

	@Override
	protected void init() {
		super.init();
		assert this.minecraft != null;
		this.minecraft.player.openContainer = this.getContainer();
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		if (this.minecraft != null) {
			this.renderBackground(matrixStack);
			
			super.render(matrixStack, mouseX, mouseY, partialTicks);

			this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
		}
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	@Override
	public void onClose() {
		super.onClose();

		if (this.minecraft != null && this.minecraft.player != null && this.itemHandler instanceof ItemBagOfHoldingProvider) {
			((ItemBagOfHoldingProvider) this.itemHandler).UpdateStack(this.minecraft.player.getHeldItemOffhand());
		}
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the
	 * items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
		this.font.drawString(matrixStack,"Pouch", 8, 6, 4210752);
		this.font.drawString(matrixStack,"Inventory", 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		assert this.minecraft != null;
		this.minecraft.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/generic_54.png"));
		this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}
}
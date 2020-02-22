package com.wuest.repurpose.Gui;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;
import com.wuest.repurpose.Tuple;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public abstract class BasicGui extends Screen {

	public BlockPos pos;
	protected PlayerEntity player;
	protected int textColor = Color.DARK_GRAY.getRGB();

	private final ResourceLocation backgroundTextures = new ResourceLocation("repurpose",
			"textures/gui/default_background.png");
	private boolean pauseGame;

	public BasicGui() {
		super(new StringTextComponent(""));
		this.pauseGame = true;
	}

	protected int getCenteredXAxis() {
		return this.width / 2;
	}

	protected int getCenteredYAxis() {
		return this.height / 2;
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in
	 * single-player
	 */
	@Override
	public boolean isPauseScreen() {
		return this.pauseGame;
	}

	@Override
	public void init() {
		this.player = this.minecraft.player;
		this.Initialize();
	}

	/**
	 * This method is used to initialize GUI specific items.
	 */
	protected void Initialize() {
	}

	@Override
	public void render(int x, int y, float f) {
		Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();

		this.preButtonRender(adjustedXYValue.getFirst(), adjustedXYValue.getSecond());

		this.renderButtons(x, y);

		this.postButtonRender(adjustedXYValue.getFirst(), adjustedXYValue.getSecond());
	}

	/**
	 * Creates a {@link GuiButtonExt} using the button clicked event as the handler.
	 * Then adds it to the buttons list and returns the created object.
	 *
	 * @param x      The x-axis position.
	 * @param y      The y-axis position.
	 * @param width  The width of the button.
	 * @param height The height of the button.
	 * @param text   The text of the button.
	 * @return A new button.
	 */
	public GuiButtonExt createAndAddButton(int x, int y, int width, int height, String text) {
		GuiButtonExt returnValue = new GuiButtonExt(x, y, width, height, text, this::buttonClicked);

		this.addButton(returnValue);

		return returnValue;
	}

	protected void drawControlBackground(int grayBoxX, int grayBoxY) {
		this.getMinecraft().getTextureManager().bindTexture(this.backgroundTextures);
		this.blit(grayBoxX, grayBoxY, 0, 0, 256, 256);
	}

	protected void renderButtons(int mouseX, int mouseY) {
		for (net.minecraft.client.gui.widget.Widget button : this.buttons) {
			Button currentButton = (Button) button;

			if (currentButton != null && currentButton.visible) {
				currentButton.renderButton(mouseX, mouseY, this.getMinecraft().getRenderPartialTicks());
			}
		}
	}

	public abstract void buttonClicked(Button button);

	protected abstract void preButtonRender(int x, int y);

	protected abstract void postButtonRender(int x, int y);

	protected abstract Tuple<Integer, Integer> getAdjustedXYValue();

	/**
	 * Draws a textured rectangle Args: x, y, z, width, height, textureWidth,
	 * textureHeight
	 *
	 * @param x             The X-Axis screen coordinate.
	 * @param y             The Y-Axis screen coordinate.
	 * @param z             The Z-Axis screen coordinate.
	 * @param width         The width of the rectangle.
	 * @param height        The height of the rectangle.
	 * @param textureWidth  The width of the texture.
	 * @param textureHeight The height of the texture.
	 */
	public static void drawModalRectWithCustomSizedTexture(int x, int y, int z, int width, int height,
			float textureWidth, float textureHeight) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableBlend();
		GlStateManager.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

		float u = 0;
		float v = 0;
		float f = 1.0F / textureWidth;
		float f1 = 1.0F / textureHeight;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexBuffer = tessellator.getBuffer();

		vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX);

		vertexBuffer.pos(x, y + height, z).tex(u * f, (v + height) * f1).endVertex();

		vertexBuffer.pos(x + width, y + height, z).tex((u + width) * f, (v + height) * f1).endVertex();

		vertexBuffer.pos(x + width, y, z).tex((u + width) * f, v * f1).endVertex();

		vertexBuffer.pos(x, y, z).tex(u * f, v * f1).endVertex();

		tessellator.draw();
	}
}
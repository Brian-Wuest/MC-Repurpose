package com.wuest.repurpose.Gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.wuest.repurpose.Triple;
import com.wuest.repurpose.Tuple;
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
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.config.HoverChecker;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BasicGui extends Screen {

	private final ResourceLocation backgroundTextures = new ResourceLocation("repurpose",
			"textures/gui/default_background.png");
	public BlockPos pos;
	protected PlayerEntity player;
	protected int textColor = Color.DARK_GRAY.getRGB();
	protected int modifiedInitialXAxis = 128;
	protected int modifiedInitialYAxis = 83;
	protected ArrayList<Triple<HoverChecker, String, Integer>> hoverCheckers;
	private boolean pauseGame;

	public BasicGui() {
		super(new StringTextComponent(""));
		this.pauseGame = true;
		this.hoverCheckers = new ArrayList<>();
	}

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

		this.renderBackground();

		this.preButtonRender(adjustedXYValue.getFirst(), adjustedXYValue.getSecond());

		this.renderButtons(x, y);

		this.postButtonRender(adjustedXYValue.getFirst(), adjustedXYValue.getSecond(), x, y);
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

	public GuiCheckBox createAndAddCheckBox(int xPos, int yPos, String displayString, boolean isChecked, Button.IPressable handler) {
		GuiCheckBox checkBox = new GuiCheckBox(xPos, yPos, displayString, isChecked, handler);

		this.addButton(checkBox);
		return checkBox;
	}

	public GuiSlider createAndAddSlider(int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, Button.IPressable handler) {
		GuiSlider slider = new GuiSlider(xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, handler);

		this.addButton(slider);
		return slider;
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

	protected abstract void postButtonRender(int x, int y, int mouseX, int mouseY);

	/**
	 * Gets the adjusted x/y coordinates for the top-left most part of the screen.
	 *
	 * @return A new tuple containing the x/y coordinates.
	 */
	protected Tuple<Integer, Integer> getAdjustedXYValue() {
		return new Tuple<>(this.getCenteredXAxis() - this.modifiedInitialXAxis, this.getCenteredYAxis() - this.modifiedInitialYAxis);
	}

	/**
	 * Draws a string on the screen.
	 *
	 * @param text  The text to draw.
	 * @param x     The X-Coordinates of the string to start.
	 * @param y     The Y-Coordinates of the string to start.
	 * @param color The color of the text.
	 * @return Some integer value.
	 */
	public int drawString(String text, float x, float y, int color) {
		return this.getMinecraft().fontRenderer.drawString(text, x, y, color);
	}

	public void addHoverChecker(Button parentButton, String displayString, int threshold, int wordWrapWidth) {
		HoverChecker hoverChecker = new HoverChecker(parentButton, threshold);
		this.hoverCheckers.add(new Triple<>(hoverChecker, displayString, wordWrapWidth));
	}

	/**
	 * Draws a string on the screen with word wrapping.
	 *
	 * @param str       The text to draw.
	 * @param x         The X-Coordinates of the string to start.
	 * @param y         The Y-Coordinates of the string to start.
	 * @param wrapWidth The maximum width before wrapping begins.
	 * @param textColor The color of the text.
	 */
	public void drawSplitString(String str, int x, int y, int wrapWidth, int textColor) {
		this.getMinecraft().fontRenderer.drawSplitString(str, x, y, wrapWidth, textColor);
	}

	/**
	 * Breaks a string into a list of pieces where the width of each line is always less than or equal to the provided
	 * width. Formatting codes will be preserved between lines.
	 */
	public List<String> listFormattedStringToWidth(String str, int wrapWidth) {
		return this.getMinecraft().fontRenderer.listFormattedStringToWidth(str, wrapWidth);
	}

	/**
	 * Closes the current screen.
	 */
	public void closeScreen() {
		this.getMinecraft().displayGuiScreen(null);
	}

	/**
	 * Binds a texture to the texture manager for rendering.
	 *
	 * @param resourceLocation The resource location to bind.
	 */
	public void bindTexture(ResourceLocation resourceLocation) {
		this.getMinecraft().getTextureManager().bindTexture(resourceLocation);
	}
}
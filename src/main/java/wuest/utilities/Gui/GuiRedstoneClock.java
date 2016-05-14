package wuest.utilities.Gui;

import java.awt.Color;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.config.HoverChecker;
import wuest.utilities.WuestUtilities;
import wuest.utilities.Config.*;
import wuest.utilities.Proxy.Messages.RedstoneClockMessage;
import wuest.utilities.Tiles.TileEntityRedstoneClock;

public class GuiRedstoneClock extends GuiScreen
{
	public static final int GUI_ID = 5;
	public BlockPos pos;
	private static final ResourceLocation backgroundTextures = new ResourceLocation("wuestutilities", "textures/gui/defaultBackground.png");
	public RedstoneClockPowerConfiguration powerConfiguration;
	protected GuiButtonExt btnCancel;
	protected GuiButtonExt btnDone;
	protected GuiCheckBox btnNorth;
	protected HoverChecker northChecker;
	protected GuiCheckBox btnEast;
	protected HoverChecker eastChecker;
	protected GuiCheckBox btnSouth;
	protected HoverChecker southChecker;
	protected GuiCheckBox btnWest;
	protected HoverChecker westChecker;
	protected GuiCheckBox btnDown;
	protected HoverChecker downChecker;
	protected GuiCheckBox btnUp;
	protected HoverChecker upChecker;

	protected GuiSlider btnPowered;
	protected GuiSlider btnUnPowered;

	protected TileEntityRedstoneClock tileEntity;

	public GuiRedstoneClock(int x, int y, int z)
	{
		this.pos = new BlockPos(x, y, z);
	}

	public void Initialize()
	{
		// Get the power configuration settings.
		TileEntity entity = this.mc.theWorld.getTileEntity(this.pos);

		if (entity != null && entity.getClass() == TileEntityRedstoneClock.class)
		{
			this.powerConfiguration = ((TileEntityRedstoneClock)entity).getPowerConfiguration();
			this.tileEntity = (TileEntityRedstoneClock)entity;
		}
		else
		{
			this.tileEntity = new TileEntityRedstoneClock();
			this.mc.theWorld.setTileEntity(pos, this.tileEntity);

			this.powerConfiguration = this.tileEntity.getPowerConfiguration();
		}

		this.powerConfiguration.setPos(this.pos);

		// Get the upper left hand corner of the GUI box.
		int grayBoxX = (this.width / 2) - 128;
		int grayBoxY = (this.height / 2) - 83;

		// Create the buttons.
		this.btnUp = new GuiCheckBox(3, grayBoxX + 190, grayBoxY + 25, "", this.powerConfiguration.getSidePower(EnumFacing.UP));
		this.buttonList.add(this.btnUp);
		this.upChecker = new HoverChecker(this.btnUp, 800);

		this.btnNorth = new GuiCheckBox(4, grayBoxX + 190, grayBoxY + 37, "", this.powerConfiguration.getSidePower(EnumFacing.NORTH));
		this.buttonList.add(this.btnNorth);
		this.northChecker = new HoverChecker(this.btnNorth, 800);

		this.btnDown = new GuiCheckBox(5, grayBoxX + 190, grayBoxY + 49, "", this.powerConfiguration.getSidePower(EnumFacing.DOWN));
		this.buttonList.add(this.btnDown);
		this.downChecker = new HoverChecker(this.btnDown, 800);

		this.btnEast = new GuiCheckBox(6, grayBoxX + 202, grayBoxY + 49, "", this.powerConfiguration.getSidePower(EnumFacing.EAST));
		this.buttonList.add(this.btnEast);
		this.eastChecker = new HoverChecker(this.btnEast, 800);

		this.btnWest = new GuiCheckBox(7, grayBoxX + 178, grayBoxY + 49, "", this.powerConfiguration.getSidePower(EnumFacing.WEST));
		this.buttonList.add(this.btnWest);
		this.westChecker = new HoverChecker(this.btnWest, 800);

		this.btnSouth = new GuiCheckBox(8, grayBoxX + 190, grayBoxY + 61, "", this.powerConfiguration.getSidePower(EnumFacing.SOUTH));
		this.buttonList.add(this.btnSouth);
		this.southChecker = new HoverChecker(this.btnSouth, 800);

		this.btnPowered = new GuiSlider(9, grayBoxX + 10, grayBoxY + 30, 100, 20, "", "", 1, 30, this.powerConfiguration.getPoweredTick() / 20, false, true);
		this.buttonList.add(this.btnPowered);

		this.btnUnPowered = new GuiSlider(10, grayBoxX + 10, grayBoxY + 80, 100, 20, "", "", 1, 30, this.powerConfiguration.getUnPoweredTick() / 20, false, true);
		this.buttonList.add(this.btnUnPowered);

		// Create the done and cancel buttons.
		this.btnDone = new GuiButtonExt(1, grayBoxX + 10, grayBoxY + 136, 90, 20, "Done");
		this.buttonList.add(this.btnDone);

		this.btnCancel = new GuiButtonExt(2, grayBoxX + 147, grayBoxY + 136, 90, 20, "Cancel");
		this.buttonList.add(this.btnCancel);
	}

	@Override
	public void initGui()
	{
		this.Initialize();
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in single-player
	 */
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
	 */
	@Override
	public void drawScreen(int x, int y, float f) 
	{
		this.drawDefaultBackground();

		// Draw the control background.
		this.mc.getTextureManager().bindTexture(backgroundTextures);

		int grayBoxX = (this.width / 2) - 128;
		int grayBoxY = (this.height / 2) - 83;
		this.drawTexturedModalRect(grayBoxX, grayBoxY, 0, 0, 256,256);

		for (int i = 0; i < this.buttonList.size(); ++i)
		{
			((GuiButton)this.buttonList.get(i)).drawButton(this.mc, x, y);
		}

		for (int j = 0; j < this.labelList.size(); ++j)
		{
			((GuiLabel)this.labelList.get(j)).drawLabel(this.mc, x, y);
		}

		// Draw the text here.
		int color = Color.DARK_GRAY.getRGB();

		this.mc.fontRendererObj.drawString("Powered Sides", grayBoxX + 150, grayBoxY + 10, color);

		this.mc.fontRendererObj.drawString("Powered Duration", grayBoxX + 10, grayBoxY + 10, color);
		this.mc.fontRendererObj.drawString("(In Seconds)", grayBoxX + 10, grayBoxY + 20, color);

		this.mc.fontRendererObj.drawString("Un-Powered Duration", grayBoxX + 10, grayBoxY + 60, color);
		this.mc.fontRendererObj.drawString("(In Seconds)", grayBoxX + 10, grayBoxY + 70, color);

		this.mc.fontRendererObj.drawString("Changes reflected after current state", grayBoxX + 10, grayBoxY + 116, color);
		this.mc.fontRendererObj.drawString("is complete.", grayBoxX + 10, grayBoxY + 126, color);

		if (this.upChecker.checkHover(x, y))
		{
			this.drawHoveringText(this.mc.fontRendererObj.listFormattedStringToWidth("The 'Up' side.", 300), x, y);
		}
		else if (this.northChecker.checkHover(x, y))
		{
			this.drawHoveringText(this.mc.fontRendererObj.listFormattedStringToWidth("The 'North' side.", 300), x, y);
		}
		else if (this.downChecker.checkHover(x, y))
		{
			this.drawHoveringText(this.mc.fontRendererObj.listFormattedStringToWidth("The 'Down' side.", 300), x, y);
		}
		else if (this.eastChecker.checkHover(x, y))
		{
			this.drawHoveringText(this.mc.fontRendererObj.listFormattedStringToWidth("The 'East' side.", 300), x, y);
		}
		else if (this.westChecker.checkHover(x, y))
		{
			this.drawHoveringText(this.mc.fontRendererObj.listFormattedStringToWidth("The 'West' side.", 300), x, y);
		}
		else if (this.southChecker.checkHover(x, y))
		{
			this.drawHoveringText(this.mc.fontRendererObj.listFormattedStringToWidth("The 'South' side.", 300), x, y);
		}
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button == this.btnCancel)
		{
			this.mc.displayGuiScreen(null);
		}
		else if (button == this.btnDone)
		{
			// Close this screen when this is done.
			this.powerConfiguration.setSidePower(EnumFacing.UP, this.btnUp.isChecked());
			this.powerConfiguration.setSidePower(EnumFacing.NORTH, this.btnNorth.isChecked());
			this.powerConfiguration.setSidePower(EnumFacing.DOWN, this.btnDown.isChecked());
			this.powerConfiguration.setSidePower(EnumFacing.EAST, this.btnEast.isChecked());
			this.powerConfiguration.setSidePower(EnumFacing.WEST, this.btnWest.isChecked());
			this.powerConfiguration.setSidePower(EnumFacing.SOUTH, this.btnSouth.isChecked());
			this.powerConfiguration.setPoweredTick(this.btnPowered.getValueInt() * 20);
			this.powerConfiguration.setUnPoweredTick(this.btnUnPowered.getValueInt() * 20);
			this.powerConfiguration.setPos(this.pos);

			WuestUtilities.network.sendToServer(new RedstoneClockMessage(this.powerConfiguration.GetNBTTagCompound()));

			// After sending the info to the server, make sure the client is updated.
			this.tileEntity.setPowerConfiguration(this.powerConfiguration);
			Block block = this.mc.theWorld.getBlockState(this.tileEntity.getPos()).getBlock();
			this.mc.theWorld.scheduleUpdate(this.tileEntity.getPos(), block, 2);

			this.mc.displayGuiScreen(null);
		}
	}
}

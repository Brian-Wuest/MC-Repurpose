package com.wuest.repurpose.Gui;

import java.awt.Color;
import java.io.IOException;

import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Config.RedstoneScannerConfig;
import com.wuest.repurpose.Proxy.Messages.RedstoneScannerMessage;
import com.wuest.repurpose.Tiles.TileEntityRedstoneScanner;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.config.HoverChecker;

/**
 * This class is used to provide a GUI for the redstone scanner.
 * @author WuestMan
 *
 */
public class GuiRedstoneScanner extends GuiScreen
{
	public static final int GUI_ID = 6;
	private static final ResourceLocation backgroundTextures = new ResourceLocation("wuestutilities", "textures/gui/default_background.png");
	public BlockPos pos;
	public RedstoneScannerConfig Config;
	protected TileEntityRedstoneScanner scannerTile;
	protected GuiButtonExt btnCancel;
	protected GuiButtonExt btnDone;
	
	protected GuiSlider btnHorizontalRange;
	protected GuiSlider btnUpRange;
	protected GuiSlider btnDownRange;
	
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
	
	protected HoverChecker animalChecker;
	protected GuiCheckBox btnAnimals;
	protected GuiCheckBox btnMonsters;
	protected GuiCheckBox btnPlayers;
	protected GuiCheckBox btnNonPlayers;
	
	/**
	 * Initializes a new instance of the GuiRedstoneScanner class.
	 * @param x - The X axis of the current block position.
	 * @param y - The Y axis of the current block position.
	 * @param z - The Z axis of the current block position.
	 */
	public GuiRedstoneScanner(int x, int y, int z)
	{
		this.pos = new BlockPos(x, y, z);
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

		// Start drawing the text.
		int white = Color.WHITE.getRGB();
		int darkGray = Color.DARK_GRAY.getRGB();
		
		// Draw the control background.
		this.mc.getTextureManager().bindTexture(backgroundTextures);

		int grayBoxX = (this.width / 2) - 128;
		int grayBoxY = (this.height / 2) - 83;
		this.drawTexturedModalRect(grayBoxX, grayBoxY, 0, 0, 256, 256);

		this.mc.fontRenderer.drawString("Active Sides", grayBoxX + 170, grayBoxY + 10, darkGray);
		this.mc.fontRenderer.drawString("Horizontal Scan Radius", grayBoxX + 5, grayBoxY + 5, darkGray);		
		this.mc.fontRenderer.drawString("Up Scan Range", grayBoxX + 5, grayBoxY + 40, darkGray);
		this.mc.fontRenderer.drawString("Down Scan Range", grayBoxX + 90, grayBoxY + 40, darkGray);
		this.mc.fontRenderer.drawString("Types of entities to scan for:", grayBoxX + 5, grayBoxY + 85, darkGray);
		
		for (int i = 0; i < this.buttonList.size(); ++i)
		{
			((GuiButton)this.buttonList.get(i)).drawButton(this.mc, x, y, f);
		}

		for (int j = 0; j < this.labelList.size(); ++j)
		{
			((GuiLabel)this.labelList.get(j)).drawLabel(this.mc, x, y);
		}
		
		if (this.upChecker.checkHover(x, y))
		{
			this.drawHoveringText(this.mc.fontRenderer.listFormattedStringToWidth("The 'Up' side.", 300), x, y);
		}
		else if (this.northChecker.checkHover(x, y))
		{
			this.drawHoveringText(this.mc.fontRenderer.listFormattedStringToWidth("The 'North' side.", 300), x, y);
		}
		else if (this.downChecker.checkHover(x, y))
		{
			this.drawHoveringText(this.mc.fontRenderer.listFormattedStringToWidth("The 'Down' side.", 300), x, y);
		}
		else if (this.eastChecker.checkHover(x, y))
		{
			this.drawHoveringText(this.mc.fontRenderer.listFormattedStringToWidth("The 'East' side.", 300), x, y);
		}
		else if (this.westChecker.checkHover(x, y))
		{
			this.drawHoveringText(this.mc.fontRenderer.listFormattedStringToWidth("The 'West' side.", 300), x, y);
		}
		else if (this.southChecker.checkHover(x, y))
		{
			this.drawHoveringText(this.mc.fontRenderer.listFormattedStringToWidth("The 'South' side.", 300), x, y);
		}
		else if (this.animalChecker.checkHover(x, y))
		{
			this.drawHoveringText(this.mc.fontRenderer.listFormattedStringToWidth("This includes things such as: pigs, cows, iron golems, etc...", 300), x, y);
		}
		
		// This is for the middle.
		grayBoxX += 109;
		grayBoxY = (this.height / 2) + 4;
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
			// Update the configuration.
			this.Config.SetFacingConfig(EnumFacing.NORTH, this.btnNorth.isChecked(), this.btnHorizontalRange.getValueInt());
			this.Config.SetFacingConfig(EnumFacing.SOUTH, this.btnSouth.isChecked(), this.btnHorizontalRange.getValueInt());
			this.Config.SetFacingConfig(EnumFacing.EAST, this.btnEast.isChecked(), this.btnHorizontalRange.getValueInt());
			this.Config.SetFacingConfig(EnumFacing.WEST, this.btnWest.isChecked(), this.btnHorizontalRange.getValueInt());
			this.Config.SetFacingConfig(EnumFacing.UP, this.btnUp.isChecked(), this.btnUpRange.getValueInt());
			this.Config.SetFacingConfig(EnumFacing.DOWN, this.btnDown.isChecked(), this.btnDownRange.getValueInt());
			this.Config.setAnimalsDetected(this.btnAnimals.isChecked());
			this.Config.setNonPlayersDetected(this.btnNonPlayers.isChecked());
			this.Config.setMonstersDetected(this.btnMonsters.isChecked());
			this.Config.setPlayersDetected(this.btnPlayers.isChecked());
			
			Repurpose.network.sendToServer(new RedstoneScannerMessage(this.Config.GetNBTTagCompound()));
			
			// After sending the info to the server, make sure the client is updated.
			this.scannerTile.setConfig(this.Config);
			Block block = this.mc.world.getBlockState(this.scannerTile.getPos()).getBlock();
			this.mc.world.scheduleUpdate(this.scannerTile.getPos(), block, 2);
			
			this.mc.displayGuiScreen(null);
		}
	}
	
	/**
	 * Initializes the GUI when opening the GUI.
	 */
	protected void Initialize()
	{
		// Get the power configuration settings.
		TileEntity entity = this.mc.world.getTileEntity(this.pos);

		if (entity != null && entity.getClass() == TileEntityRedstoneScanner.class)
		{
			this.Config = ((TileEntityRedstoneScanner)entity).getConfig();
			this.scannerTile = (TileEntityRedstoneScanner)entity;
		}
		else
		{
			this.scannerTile = new TileEntityRedstoneScanner();
			this.mc.world.setTileEntity(pos, this.scannerTile);

			this.Config = this.scannerTile.getConfig();
		}

		this.Config.setBlockPos(this.pos);
		int color = Color.DARK_GRAY.getRGB();
		
		// Get the upper left hand corner of the GUI box.
		int grayBoxX = (this.width / 2) - 123;
		int grayBoxY = (this.height / 2) - 83;
		
		this.btnHorizontalRange = new GuiSlider(9, grayBoxX + 5, grayBoxY + 15, 50, 20, "", "", 0, 5, 
				this.Config.getFacingConfig(EnumFacing.NORTH).getScanLength(), false, true);
		this.buttonList.add(this.btnHorizontalRange);
		
		this.btnUpRange = new GuiSlider(10, grayBoxX + 5, grayBoxY + 50, 50, 20, "", "", 0, 5,
				this.Config.getFacingConfig(EnumFacing.UP).getScanLength(), false, true);
		this.buttonList.add(this.btnUpRange);
		
		this.btnDownRange = new GuiSlider(11, grayBoxX + 90, grayBoxY + 50, 50, 20, "", "", 0, 5,
				this.Config.getFacingConfig(EnumFacing.DOWN).getScanLength(), false, true);
		this.buttonList.add(this.btnDownRange);

		this.btnNorth = new GuiCheckBox(4, grayBoxX + 200, grayBoxY + 25, "", this.Config.getFacingConfig(EnumFacing.NORTH).getActive());
		this.buttonList.add(this.btnNorth);
		this.northChecker = new HoverChecker(this.btnNorth, 800);

		this.btnDown = new GuiCheckBox(5, grayBoxX + 200, grayBoxY + 37, "", this.Config.getFacingConfig(EnumFacing.DOWN).getActive());
		this.buttonList.add(this.btnDown);
		this.downChecker = new HoverChecker(this.btnDown, 800);

		this.btnEast = new GuiCheckBox(6, grayBoxX + 212, grayBoxY + 37, "", this.Config.getFacingConfig(EnumFacing.EAST).getActive());
		this.buttonList.add(this.btnEast);
		this.eastChecker = new HoverChecker(this.btnEast, 800);

		this.btnWest = new GuiCheckBox(7, grayBoxX + 188, grayBoxY + 37, "", this.Config.getFacingConfig(EnumFacing.WEST).getActive());
		this.buttonList.add(this.btnWest);
		this.westChecker = new HoverChecker(this.btnWest, 800);

		this.btnSouth = new GuiCheckBox(8, grayBoxX + 200, grayBoxY + 49, "", this.Config.getFacingConfig(EnumFacing.SOUTH).getActive());
		this.buttonList.add(this.btnSouth);
		this.southChecker = new HoverChecker(this.btnSouth, 800);
		
		this.btnUp = new GuiCheckBox(3, grayBoxX + 212, grayBoxY + 49, "", this.Config.getFacingConfig(EnumFacing.UP).getActive());
		this.buttonList.add(this.btnUp);
		this.upChecker = new HoverChecker(this.btnUp, 800);

		this.btnAnimals = new GuiCheckBox(15, grayBoxX + 5, grayBoxY + 100, "Animals", this.Config.getAnimalsDetected()).setStringColor(color).setWithShadow(false);
		this.buttonList.add(this.btnAnimals);
		this.animalChecker = new HoverChecker(this.btnAnimals, 800);
		
		this.btnNonPlayers = new GuiCheckBox(16, grayBoxX + 5, grayBoxY + 115, "Non-Players", this.Config.getNonPlayersDetected()).setStringColor(color).setWithShadow(false);
		this.buttonList.add(this.btnNonPlayers);

		// Middle Column:
		grayBoxX += 95;
		grayBoxY = (this.height / 2) - 83;
		
		this.btnMonsters = new GuiCheckBox(17, grayBoxX, grayBoxY + 100, "Monsters", this.Config.getMonstersDetected()).setStringColor(color).setWithShadow(false);
		this.buttonList.add(this.btnMonsters);
		
		this.btnPlayers = new GuiCheckBox(18, grayBoxX, grayBoxY + 115, "Players", this.Config.getPlayersDetected()).setStringColor(color).setWithShadow(false);
		this.buttonList.add(this.btnPlayers);
		
		grayBoxX = (this.width / 2) - 128;
		grayBoxY = (this.height / 2) - 83;
		
		this.btnDone = new GuiButtonExt(1, grayBoxX + 10, grayBoxY + 136, 90, 20, "Done");
		this.buttonList.add(this.btnDone);

		this.btnCancel = new GuiButtonExt(2, grayBoxX + 147, grayBoxY + 136, 90, 20, "Cancel");
		this.buttonList.add(this.btnCancel);
	}
	
}
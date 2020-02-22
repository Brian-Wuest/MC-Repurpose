package com.wuest.repurpose.Gui;

import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Tuple;
import com.wuest.repurpose.Config.RedstoneScannerConfig;
import com.wuest.repurpose.Proxy.Messages.RedstoneScannerMessage;
import com.wuest.repurpose.Tiles.TileEntityRedstoneScanner;

import net.minecraft.block.Block;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.config.HoverChecker;

/**
 * This class is used to provide a GUI for the redstone scanner.
 * 
 * @author WuestMan
 *
 */
public class GuiRedstoneScanner extends BasicGui {
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

	public GuiRedstoneScanner() {
	}

	/**
	 * Initializes the GUI when opening the GUI.
	 */
	protected void Initialize() {
		// Get the power configuration settings.
		TileEntity entity = this.minecraft.world.getTileEntity(this.pos);

		if (entity != null && entity.getClass() == TileEntityRedstoneScanner.class) {
			this.Config = ((TileEntityRedstoneScanner) entity).getConfig();
			this.scannerTile = (TileEntityRedstoneScanner) entity;
		} else {
			this.scannerTile = new TileEntityRedstoneScanner();
			this.minecraft.world.setTileEntity(pos, this.scannerTile);

			this.Config = this.scannerTile.getConfig();
		}

		this.Config.setBlockPos(this.pos);

		// Get the upper left hand corner of the GUI box.
		Tuple<Integer, Integer> upperLeft = this.getAdjustedXYValue();
		int grayBoxX = upperLeft.getFirst();
		int grayBoxY = upperLeft.getSecond();

		this.btnHorizontalRange = new GuiSlider(grayBoxX + 5, grayBoxY + 15, 50, 20, "", "", 0, 5,
				this.Config.getFacingConfig(Direction.NORTH).getScanLength(), false, true, this::buttonClicked);
		this.addButton(this.btnHorizontalRange);

		this.btnUpRange = new GuiSlider(grayBoxX + 5, grayBoxY + 50, 50, 20, "", "", 0, 5,
				this.Config.getFacingConfig(Direction.UP).getScanLength(), false, true, this::buttonClicked);
		this.addButton(this.btnUpRange);

		this.btnDownRange = new GuiSlider(grayBoxX + 90, grayBoxY + 50, 50, 20, "", "", 0, 5,
				this.Config.getFacingConfig(Direction.DOWN).getScanLength(), false, true, this::buttonClicked);
		this.addButton(this.btnDownRange);

		this.btnNorth = new GuiCheckBox(grayBoxX + 200, grayBoxY + 25, "",
				this.Config.getFacingConfig(Direction.NORTH).getActive(), this::buttonClicked);
		this.addButton(this.btnNorth);
		this.northChecker = new HoverChecker(this.btnNorth, 800);

		this.btnDown = new GuiCheckBox(grayBoxX + 200, grayBoxY + 37, "",
				this.Config.getFacingConfig(Direction.DOWN).getActive(), this::buttonClicked);
		this.addButton(this.btnDown);
		this.downChecker = new HoverChecker(this.btnDown, 800);

		this.btnEast = new GuiCheckBox(grayBoxX + 212, grayBoxY + 37, "",
				this.Config.getFacingConfig(Direction.EAST).getActive(), this::buttonClicked);
		this.addButton(this.btnEast);
		this.eastChecker = new HoverChecker(this.btnEast, 800);

		this.btnWest = new GuiCheckBox(grayBoxX + 188, grayBoxY + 37, "",
				this.Config.getFacingConfig(Direction.WEST).getActive(), this::buttonClicked);
		this.addButton(this.btnWest);
		this.westChecker = new HoverChecker(this.btnWest, 800);

		this.btnSouth = new GuiCheckBox(grayBoxX + 200, grayBoxY + 49, "",
				this.Config.getFacingConfig(Direction.SOUTH).getActive(), this::buttonClicked);
		this.addButton(this.btnSouth);
		this.southChecker = new HoverChecker(this.btnSouth, 800);

		this.btnUp = new GuiCheckBox(grayBoxX + 212, grayBoxY + 49, "",
				this.Config.getFacingConfig(Direction.UP).getActive(), this::buttonClicked);
		this.addButton(this.btnUp);
		this.upChecker = new HoverChecker(this.btnUp, 800);

		this.btnAnimals = new GuiCheckBox(grayBoxX + 5, grayBoxY + 100, "Animals", this.Config.getAnimalsDetected(),
				this::buttonClicked).setStringColor(this.textColor).setWithShadow(false);
		this.addButton(this.btnAnimals);
		this.animalChecker = new HoverChecker(this.btnAnimals, 800);

		this.btnNonPlayers = new GuiCheckBox(grayBoxX + 5, grayBoxY + 115, "Non-Players",
				this.Config.getNonPlayersDetected(), this::buttonClicked).setStringColor(this.textColor)
						.setWithShadow(false);
		this.addButton(this.btnNonPlayers);

		// Middle Column:
		grayBoxX += 95;
		grayBoxY = (this.height / 2) - 83;

		this.btnMonsters = new GuiCheckBox(grayBoxX, grayBoxY + 100, "Monsters", this.Config.getMonstersDetected(),
				this::buttonClicked).setStringColor(this.textColor).setWithShadow(false);
		this.addButton(this.btnMonsters);

		this.btnPlayers = new GuiCheckBox(grayBoxX, grayBoxY + 115, "Players", this.Config.getPlayersDetected(),
				this::buttonClicked).setStringColor(this.textColor).setWithShadow(false);
		this.addButton(this.btnPlayers);

		grayBoxX = (this.width / 2) - 128;
		grayBoxY = (this.height / 2) - 83;

		this.btnDone = this.createAndAddButton(grayBoxX + 10, grayBoxY + 136, 90, 20, "Done");
		this.btnCancel = this.createAndAddButton(grayBoxX + 147, grayBoxY + 136, 90, 20, "Cancel");
	}

	@Override
	protected void preButtonRender(int x, int y) {
		this.drawControlBackground(x, y);

		this.font.drawString("Active Sides", x + 170, y + 10, this.textColor);
		this.font.drawString("Horizontal Scan Radius", x + 5, y + 5, this.textColor);
		this.font.drawString("Up Scan Range", x + 5, y + 40, this.textColor);
		this.font.drawString("Down Scan Range", x + 90, y + 40, this.textColor);
		this.font.drawString("Types of entities to scan for:", x + 5, y + 85, this.textColor);
	}

	@Override
	protected void postButtonRender(int x, int y) {
		if (this.upChecker.checkHover(x, y)) {
			this.renderTooltip(this.minecraft.fontRenderer.listFormattedStringToWidth("The 'Up' side.", 300), x, y);
		} else if (this.northChecker.checkHover(x, y)) {
			this.renderTooltip(this.minecraft.fontRenderer.listFormattedStringToWidth("The 'North' side.", 300), x, y);
		} else if (this.downChecker.checkHover(x, y)) {
			this.renderTooltip(this.minecraft.fontRenderer.listFormattedStringToWidth("The 'Down' side.", 300), x, y);
		} else if (this.eastChecker.checkHover(x, y)) {
			this.renderTooltip(this.minecraft.fontRenderer.listFormattedStringToWidth("The 'East' side.", 300), x, y);
		} else if (this.westChecker.checkHover(x, y)) {
			this.renderTooltip(this.minecraft.fontRenderer.listFormattedStringToWidth("The 'West' side.", 300), x, y);
		} else if (this.southChecker.checkHover(x, y)) {
			this.renderTooltip(this.minecraft.fontRenderer.listFormattedStringToWidth("The 'South' side.", 300), x, y);
		} else if (this.animalChecker.checkHover(x, y)) {
			this.renderTooltip(this.minecraft.fontRenderer.listFormattedStringToWidth(
					"This includes things such as: pigs, cows, iron golems, etc...", 300), x, y);
		}
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for
	 * buttons)
	 */
	@Override
	public void buttonClicked(Button button) {
		if (button == this.btnCancel) {
			this.minecraft.displayGuiScreen(null);
		} else if (button == this.btnDone) {
			// Update the configuration.
			this.Config.SetFacingConfig(Direction.NORTH, this.btnNorth.isChecked(),
					this.btnHorizontalRange.getValueInt());

			this.Config.SetFacingConfig(Direction.SOUTH, this.btnSouth.isChecked(),
					this.btnHorizontalRange.getValueInt());

			this.Config.SetFacingConfig(Direction.EAST, this.btnEast.isChecked(),
					this.btnHorizontalRange.getValueInt());

			this.Config.SetFacingConfig(Direction.WEST, this.btnWest.isChecked(),
					this.btnHorizontalRange.getValueInt());

			this.Config.SetFacingConfig(Direction.UP, this.btnUp.isChecked(), this.btnUpRange.getValueInt());
			this.Config.SetFacingConfig(Direction.DOWN, this.btnDown.isChecked(), this.btnDownRange.getValueInt());
			this.Config.setAnimalsDetected(this.btnAnimals.isChecked());
			this.Config.setNonPlayersDetected(this.btnNonPlayers.isChecked());
			this.Config.setMonstersDetected(this.btnMonsters.isChecked());
			this.Config.setPlayersDetected(this.btnPlayers.isChecked());

			Repurpose.network.sendToServer(new RedstoneScannerMessage(this.Config.GetCompoundNBT()));

			// After sending the info to the server, make sure the client is updated.
			this.scannerTile.setConfig(this.Config);
			Block block = this.minecraft.world.getBlockState(this.scannerTile.getPos()).getBlock();
			this.minecraft.world.getPendingBlockTicks().scheduleTick(this.scannerTile.getPos(), block, 2);

			this.minecraft.displayGuiScreen(null);
		}
	}

	@Override
	protected Tuple<Integer, Integer> getAdjustedXYValue() {
		return new Tuple<>(this.getCenteredXAxis() - 128, this.getCenteredYAxis() - 83);
	}

}
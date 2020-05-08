package com.wuest.repurpose.Gui;

import com.wuest.repurpose.Config.RedstoneScannerConfig;
import com.wuest.repurpose.Proxy.Messages.RedstoneScannerMessage;
import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Tiles.TileEntityRedstoneScanner;
import com.wuest.repurpose.Triple;
import com.wuest.repurpose.Tuple;
import net.minecraft.block.Block;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.client.gui.HoverChecker;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import net.minecraftforge.fml.client.gui.widget.Slider;

/**
 * This class is used to provide a GUI for the redstone scanner.
 *
 * @author WuestMan
 */
public class GuiRedstoneScanner extends BasicGui {
	public RedstoneScannerConfig Config;
	protected TileEntityRedstoneScanner scannerTile;
	protected ExtendedButton btnCancel;
	protected ExtendedButton btnDone;

	protected Slider btnHorizontalRange;
	protected Slider btnUpRange;
	protected Slider btnDownRange;

	protected GuiCheckBox btnNorth;
	protected GuiCheckBox btnEast;
	protected GuiCheckBox btnSouth;
	protected GuiCheckBox btnWest;
	protected GuiCheckBox btnDown;
	protected GuiCheckBox btnUp;

	protected GuiCheckBox btnAnimals;
	protected GuiCheckBox btnMonsters;
	protected GuiCheckBox btnPlayers;
	protected GuiCheckBox btnNonPlayers;

	public GuiRedstoneScanner() {
		super();
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

		this.btnHorizontalRange = this.createAndAddSlider(grayBoxX + 5, grayBoxY + 15, 50, 20, "", "", 0, 5,
				this.Config.getFacingConfig(Direction.NORTH).getScanLength(), false, true, this::buttonClicked);

		this.btnUpRange = this.createAndAddSlider(grayBoxX + 5, grayBoxY + 50, 50, 20, "", "", 0, 5,
				this.Config.getFacingConfig(Direction.UP).getScanLength(), false, true, this::buttonClicked);

		this.btnDownRange = this.createAndAddSlider(grayBoxX + 90, grayBoxY + 50, 50, 20, "", "", 0, 5,
				this.Config.getFacingConfig(Direction.DOWN).getScanLength(), false, true, this::buttonClicked);

		this.btnNorth = this.createAndAddCheckBox(grayBoxX + 200, grayBoxY + 23, "",
				this.Config.getFacingConfig(Direction.NORTH).getActive(), this::buttonClicked);
		this.addHoverChecker(this.btnNorth, "The 'North' side.", 800, 300);

		this.btnDown = this.createAndAddCheckBox(grayBoxX + 200, grayBoxY + 36, "",
				this.Config.getFacingConfig(Direction.DOWN).getActive(), this::buttonClicked);
		this.addHoverChecker(this.btnDown, "The 'Bottom' side.", 800, 300);

		this.btnEast = this.createAndAddCheckBox(grayBoxX + 212, grayBoxY + 36, "",
				this.Config.getFacingConfig(Direction.EAST).getActive(), this::buttonClicked);
		this.addHoverChecker(this.btnEast, "The 'East' side.", 800, 300);

		this.btnWest = this.createAndAddCheckBox(grayBoxX + 188, grayBoxY + 36, "",
				this.Config.getFacingConfig(Direction.WEST).getActive(), this::buttonClicked);
		this.addHoverChecker(this.btnWest, "The 'West' side.", 800, 300);

		this.btnSouth = this.createAndAddCheckBox(grayBoxX + 200, grayBoxY + 49, "",
				this.Config.getFacingConfig(Direction.SOUTH).getActive(), this::buttonClicked);
		this.addHoverChecker(this.btnSouth, "The 'South' side.", 800, 300);

		this.btnUp = this.createAndAddCheckBox(grayBoxX + 212, grayBoxY + 49, "",
				this.Config.getFacingConfig(Direction.UP).getActive(), this::buttonClicked);
		this.addHoverChecker(this.btnUp, "The 'Top' side.", 800, 300);

		this.btnAnimals = this.createAndAddCheckBox(grayBoxX + 5, grayBoxY + 100, "Animals", this.Config.getAnimalsDetected(),
				this::buttonClicked).setStringColor(this.textColor).setWithShadow(false);
		this.addHoverChecker(this.btnAnimals, "This includes things such as: pigs, cows, iron golems, etc...", 800, 300);

		this.btnNonPlayers = this.createAndAddCheckBox(grayBoxX + 5, grayBoxY + 115, "Non-Players",
				this.Config.getNonPlayersDetected(), this::buttonClicked).setStringColor(this.textColor)
				.setWithShadow(false);

		// Middle Column:
		grayBoxX += 95;
		grayBoxY = (this.height / 2) - 83;

		this.btnMonsters = this.createAndAddCheckBox(grayBoxX, grayBoxY + 100, "Monsters", this.Config.getMonstersDetected(),
				this::buttonClicked).setStringColor(this.textColor).setWithShadow(false);

		this.btnPlayers = this.createAndAddCheckBox(grayBoxX, grayBoxY + 115, "Players", this.Config.getPlayersDetected(),
				this::buttonClicked).setStringColor(this.textColor).setWithShadow(false);

		grayBoxX = upperLeft.getFirst();
		grayBoxY = upperLeft.getSecond();

		this.btnDone = this.createAndAddButton(grayBoxX + 10, grayBoxY + 136, 90, 20, "Done");
		this.btnCancel = this.createAndAddButton(grayBoxX + 147, grayBoxY + 136, 90, 20, "Cancel");
	}

	@Override
	protected void preButtonRender(int x, int y) {
		this.drawControlBackground(x, y);

		this.drawString("Active Sides", x + 170, y + 10, this.textColor);
		this.drawString("Horizontal Scan Radius", x + 5, y + 5, this.textColor);
		this.drawString("Up Scan Range", x + 5, y + 40, this.textColor);
		this.drawString("Down Scan Range", x + 90, y + 40, this.textColor);
		this.drawString("Types of entities to scan for:", x + 5, y + 85, this.textColor);
	}

	@Override
	protected void postButtonRender(int x, int y, int mouseX, int mouseY) {
		for (Triple<HoverChecker, String, Integer> triple : this.hoverCheckers) {
			if (triple.getFirst().checkHover(mouseX, mouseY)) {
				this.renderTooltip(this.listFormattedStringToWidth(triple.getSecond(), triple.getThird()), mouseX, mouseY);
				break;
			}
		}
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for
	 * buttons)
	 */
	@Override
	public void buttonClicked(AbstractButton button) {
		if (button == this.btnCancel) {
			this.closeScreen();
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

			this.closeScreen();
		}
	}
}
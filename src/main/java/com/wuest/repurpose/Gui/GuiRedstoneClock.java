package com.wuest.repurpose.Gui;

import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Tuple;
import com.wuest.repurpose.Config.RedstoneClockPowerConfiguration;
import com.wuest.repurpose.Proxy.Messages.RedstoneClockMessage;
import com.wuest.repurpose.Tiles.TileEntityRedstoneClock;

import net.minecraft.block.Block;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.config.HoverChecker;

public class GuiRedstoneClock extends BasicGui {
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

	public GuiRedstoneClock() {
	}

	@Override
	public void Initialize() {
		// Get the power configuration settings.
		TileEntity entity = this.minecraft.world.getTileEntity(this.pos);

		if (entity != null && entity.getClass() == TileEntityRedstoneClock.class) {
			this.powerConfiguration = ((TileEntityRedstoneClock) entity).getConfig();
			this.tileEntity = (TileEntityRedstoneClock) entity;
		} else {
			this.tileEntity = new TileEntityRedstoneClock();
			this.minecraft.world.setTileEntity(pos, this.tileEntity);

			this.powerConfiguration = this.tileEntity.getConfig();
		}

		this.powerConfiguration.setPos(this.pos);

		// Get the upper left hand corner of the GUI box.
		int grayBoxX = (this.width / 2) - 128;
		int grayBoxY = (this.height / 2) - 83;

		// Create the buttons.
		this.btnUp = new GuiCheckBox(grayBoxX + 190, grayBoxY + 25, "",
				this.powerConfiguration.getSidePower(Direction.UP), this::buttonClicked);
		this.addButton(this.btnUp);
		this.upChecker = new HoverChecker(this.btnUp, 800);

		this.btnNorth = new GuiCheckBox(grayBoxX + 190, grayBoxY + 37, "",
				this.powerConfiguration.getSidePower(Direction.NORTH), this::buttonClicked);
		this.addButton(this.btnNorth);
		this.northChecker = new HoverChecker(this.btnNorth, 800);

		this.btnDown = new GuiCheckBox(grayBoxX + 190, grayBoxY + 49, "",
				this.powerConfiguration.getSidePower(Direction.DOWN), this::buttonClicked);
		this.addButton(this.btnDown);
		this.downChecker = new HoverChecker(this.btnDown, 800);

		this.btnEast = new GuiCheckBox(grayBoxX + 202, grayBoxY + 49, "",
				this.powerConfiguration.getSidePower(Direction.EAST), this::buttonClicked);
		this.addButton(this.btnEast);
		this.eastChecker = new HoverChecker(this.btnEast, 800);

		this.btnWest = new GuiCheckBox(grayBoxX + 178, grayBoxY + 49, "",
				this.powerConfiguration.getSidePower(Direction.WEST), this::buttonClicked);
		this.addButton(this.btnWest);
		this.westChecker = new HoverChecker(this.btnWest, 800);

		this.btnSouth = new GuiCheckBox(grayBoxX + 190, grayBoxY + 61, "",
				this.powerConfiguration.getSidePower(Direction.SOUTH), this::buttonClicked);
		this.addButton(this.btnSouth);
		this.southChecker = new HoverChecker(this.btnSouth, 800);

		this.btnPowered = new GuiSlider(grayBoxX + 10, grayBoxY + 30, 100, 20, "", "", 1, 30,
				this.powerConfiguration.getPoweredTick() / 20, false, true, this::buttonClicked);
		this.addButton(this.btnPowered);

		this.btnUnPowered = new GuiSlider(grayBoxX + 10, grayBoxY + 80, 100, 20, "", "", 1, 30,
				this.powerConfiguration.getUnPoweredTick() / 20, false, true, this::buttonClicked);
		this.addButton(this.btnUnPowered);

		// Create the done and cancel buttons.
		this.btnDone = this.createAndAddButton(grayBoxX + 10, grayBoxY + 136, 90, 20, "Done");

		this.btnCancel = this.createAndAddButton(grayBoxX + 147, grayBoxY + 136, 90, 20, "Cancel");
	}

	@Override
	protected void preButtonRender(int x, int y) {
		this.drawControlBackground(x, y);
	}

	@Override
	protected void postButtonRender(int x, int y) {
		this.font.drawString("Powered Sides", x + 150, y + 10, this.textColor);
		this.font.drawString("Powered Duration", x + 10, y + 10, this.textColor);
		this.font.drawString("(In Seconds)", x + 10, y + 20, this.textColor);

		this.font.drawString("Un-Powered Duration", x + 10, y + 60, this.textColor);
		this.font.drawString("(In Seconds)", x + 10, y + 70, this.textColor);

		this.font.drawString("Changes reflected after current state", x + 10, y + 116, this.textColor);
		this.font.drawString("is complete.", x + 10, y + 126, this.textColor);

		if (this.upChecker.checkHover(x, y)) {
			this.renderTooltip(this.font.listFormattedStringToWidth("The 'Up' side.", 300), x, y);
		} else if (this.northChecker.checkHover(x, y)) {
			this.renderTooltip(this.font.listFormattedStringToWidth("The 'North' side.", 300), x, y);
		} else if (this.downChecker.checkHover(x, y)) {
			this.renderTooltip(this.font.listFormattedStringToWidth("The 'Down' side.", 300), x, y);
		} else if (this.eastChecker.checkHover(x, y)) {
			this.renderTooltip(this.font.listFormattedStringToWidth("The 'East' side.", 300), x, y);
		} else if (this.westChecker.checkHover(x, y)) {
			this.renderTooltip(this.font.listFormattedStringToWidth("The 'West' side.", 300), x, y);
		} else if (this.southChecker.checkHover(x, y)) {
			this.renderTooltip(this.font.listFormattedStringToWidth("The 'South' side.", 300), x, y);
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
			// Close this screen when this is done.
			this.powerConfiguration.setSidePower(Direction.UP, this.btnUp.isChecked());
			this.powerConfiguration.setSidePower(Direction.NORTH, this.btnNorth.isChecked());
			this.powerConfiguration.setSidePower(Direction.DOWN, this.btnDown.isChecked());
			this.powerConfiguration.setSidePower(Direction.EAST, this.btnEast.isChecked());
			this.powerConfiguration.setSidePower(Direction.WEST, this.btnWest.isChecked());
			this.powerConfiguration.setSidePower(Direction.SOUTH, this.btnSouth.isChecked());
			this.powerConfiguration.setPoweredTick(this.btnPowered.getValueInt() * 20);
			this.powerConfiguration.setUnPoweredTick(this.btnUnPowered.getValueInt() * 20);
			this.powerConfiguration.setPos(this.pos);

			Repurpose.network.sendToServer(new RedstoneClockMessage(this.powerConfiguration.GetCompoundNBT()));

			// After sending the info to the server, make sure the client is updated.
			this.tileEntity.setConfig(this.powerConfiguration);
			Block block = this.minecraft.world.getBlockState(this.tileEntity.getPos()).getBlock();
			this.minecraft.world.getPendingBlockTicks().scheduleTick(this.tileEntity.getPos(), block, 2);

			this.minecraft.displayGuiScreen(null);
		}
	}

	@Override
	protected Tuple<Integer, Integer> getAdjustedXYValue() {
		return new Tuple<>(this.getCenteredXAxis() - 128, this.getCenteredYAxis() - 83);
	}
}

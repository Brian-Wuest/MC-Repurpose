package com.wuest.repurpose.Gui;

import com.wuest.repurpose.Config.RedstoneClockPowerConfiguration;
import com.wuest.repurpose.Proxy.Messages.RedstoneClockMessage;
import com.wuest.repurpose.Repurpose;
import com.wuest.repurpose.Tiles.TileEntityRedstoneClock;
import com.wuest.repurpose.Triple;
import com.wuest.repurpose.Tuple;
import net.minecraft.block.Block;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.client.gui.HoverChecker;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import net.minecraftforge.fml.client.gui.widget.Slider;

public class GuiRedstoneClock extends BasicGui {
	public RedstoneClockPowerConfiguration powerConfiguration;
	protected ExtendedButton btnCancel;
	protected ExtendedButton btnDone;
	protected GuiCheckBox btnNorth;
	protected GuiCheckBox btnEast;
	protected GuiCheckBox btnSouth;
	protected GuiCheckBox btnWest;
	protected GuiCheckBox btnDown;
	protected GuiCheckBox btnUp;

	protected Slider btnPowered;
	protected Slider btnUnPowered;

	protected TileEntityRedstoneClock tileEntity;

	public GuiRedstoneClock() {
		super();
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
		Tuple<Integer, Integer> adjustedPosition = this.getAdjustedXYValue();
		int grayBoxX = adjustedPosition.getFirst();
		int grayBoxY = adjustedPosition.getSecond();

		// Create the buttons.
		this.btnNorth = this.createAndAddCheckBox(grayBoxX + 185, grayBoxY + 27, "",
				this.powerConfiguration.getSidePower(Direction.NORTH), this::buttonClicked);
		this.addHoverChecker(this.btnNorth, "The 'North' side.", 800, 300);

		this.btnDown = this.createAndAddCheckBox(grayBoxX + 185, grayBoxY + 39, "",
				this.powerConfiguration.getSidePower(Direction.DOWN), this::buttonClicked);
		this.addHoverChecker(this.btnDown, "The 'Bottom' side.", 800, 300);

		this.btnEast = this.createAndAddCheckBox(grayBoxX + 197, grayBoxY + 39, "",
				this.powerConfiguration.getSidePower(Direction.EAST), this::buttonClicked);
		this.addHoverChecker(this.btnEast, "The 'East' side.", 800, 300);

		this.btnWest = this.createAndAddCheckBox(grayBoxX + 173, grayBoxY + 39, "",
				this.powerConfiguration.getSidePower(Direction.WEST), this::buttonClicked);
		this.addHoverChecker(this.btnWest, "The 'West' side.", 800, 300);

		this.btnSouth = this.createAndAddCheckBox(grayBoxX + 185, grayBoxY + 51, "",
				this.powerConfiguration.getSidePower(Direction.SOUTH), this::buttonClicked);
		this.addHoverChecker(this.btnSouth, "The 'South' side.", 800, 300);

		this.btnUp = this.createAndAddCheckBox(grayBoxX + 197, grayBoxY + 51, "",
				this.powerConfiguration.getSidePower(Direction.UP), this::buttonClicked);
		this.addHoverChecker(this.btnUp, "The 'Top' side.", 800, 300);

		this.btnPowered = this.createAndAddSlider(grayBoxX + 10, grayBoxY + 30, 100, 20, "", "", 1, 30,
				this.powerConfiguration.getPoweredTick() / 20, false, true, this::buttonClicked);

		this.btnUnPowered = this.createAndAddSlider(grayBoxX + 10, grayBoxY + 80, 100, 20, "", "", 1, 30,
				this.powerConfiguration.getUnPoweredTick() / 20, false, true, this::buttonClicked);

		// Create the done and cancel buttons.
		this.btnDone = this.createAndAddButton(grayBoxX + 10, grayBoxY + 136, 90, 20, "Done");

		this.btnCancel = this.createAndAddButton(grayBoxX + 147, grayBoxY + 136, 90, 20, "Cancel");
	}

	@Override
	protected void preButtonRender(int x, int y) {
		this.drawControlBackground(x, y);
	}

	@Override
	protected void postButtonRender(int x, int y, int mouseX, int mouseY) {
		this.drawString("Powered Sides", x + 150, y + 10, this.textColor);
		this.drawString("Powered Duration", x + 10, y + 10, this.textColor);
		this.drawString("(In Seconds)", x + 10, y + 20, this.textColor);

		this.drawString("Un-Powered Duration", x + 10, y + 60, this.textColor);
		this.drawString("(In Seconds)", x + 10, y + 70, this.textColor);

		this.drawString("Changes reflected after current state", x + 10, y + 116, this.textColor);
		this.drawString("is complete.", x + 10, y + 126, this.textColor);

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

			this.closeScreen();
		}
	}
}

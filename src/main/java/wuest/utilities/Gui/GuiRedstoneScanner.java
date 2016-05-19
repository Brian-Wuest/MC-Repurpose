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
import wuest.utilities.WuestUtilities;
import wuest.utilities.Config.RedstoneScannerConfig;
import wuest.utilities.Proxy.Messages.RedstoneScannerMessage;
import wuest.utilities.Tiles.TileEntityRedstoneScanner;

/**
 * This class is used to provide a GUI for the redstone scanner.
 * @author WuestMan
 *
 */
public class GuiRedstoneScanner extends GuiScreen
{
	public static final int GUI_ID = 6;
	private static final ResourceLocation backgroundTextures = new ResourceLocation("wuestutilities", "textures/gui/defaultBackground.png");
	public BlockPos pos;
	public RedstoneScannerConfig Config;
	protected TileEntityRedstoneScanner scannerTile;
	protected GuiButtonExt btnCancel;
	protected GuiButtonExt btnDone;
	protected GuiSlider btnNorthSlider;
	protected GuiSlider btnSouthSlider;
	protected GuiSlider btnEastSlider;
	protected GuiSlider btnWestSlider;
	protected GuiSlider btnUpSlider;
	protected GuiSlider btnDownSlider;
	
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
		
		// Draw the control background.
		this.mc.getTextureManager().bindTexture(backgroundTextures);

		int grayBoxX = (this.width / 2) - 128;
		int grayBoxY = (this.height / 2) - 83;
		this.drawTexturedModalRect(grayBoxX, grayBoxY, 0, 0, 256, 256);

		grayBoxX += 5;
		grayBoxY += 5;
		this.drawString(this.mc.fontRendererObj, "The number of blocks each side should scan", grayBoxX, grayBoxY, white);
		
		grayBoxY += 70;
		this.drawString(this.mc.fontRendererObj, "Types of entities to scan for:", grayBoxX, grayBoxY, white);
		
		for (int i = 0; i < this.buttonList.size(); ++i)
		{
			((GuiButton)this.buttonList.get(i)).drawButton(this.mc, x, y);
		}

		for (int j = 0; j < this.labelList.size(); ++j)
		{
			((GuiLabel)this.labelList.get(j)).drawLabel(this.mc, x, y);
		}
		
		grayBoxX += 14;
		grayBoxY += 12;
		this.drawString(this.mc.fontRendererObj, "Animals", grayBoxX, grayBoxY, white);
		
		grayBoxY += 15;
		this.drawString(this.mc.fontRendererObj, "Non-Players", grayBoxX, grayBoxY, white);
		
		grayBoxX += 90;
		grayBoxY = (this.height / 2) + 4;
		
		this.drawString(this.mc.fontRendererObj, "Monsters", grayBoxX, grayBoxY, white);
		
		grayBoxY += 15;
		this.drawString(this.mc.fontRendererObj, "Players", grayBoxX, grayBoxY, white);
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
			this.Config.SetFacingConfig(EnumFacing.NORTH, this.btnNorthSlider.getValueInt());
			this.Config.SetFacingConfig(EnumFacing.SOUTH, this.btnSouthSlider.getValueInt());
			this.Config.SetFacingConfig(EnumFacing.EAST, this.btnEastSlider.getValueInt());
			this.Config.SetFacingConfig(EnumFacing.WEST, this.btnWestSlider.getValueInt());
			this.Config.SetFacingConfig(EnumFacing.UP, this.btnUpSlider.getValueInt());
			this.Config.SetFacingConfig(EnumFacing.DOWN, this.btnDownSlider.getValueInt());
			this.Config.setAnimalsDetected(this.btnAnimals.isChecked());
			this.Config.setNonPlayersDetected(this.btnNonPlayers.isChecked());
			this.Config.setMonstersDetected(this.btnMonsters.isChecked());
			this.Config.setPlayersDetected(this.btnPlayers.isChecked());
			
			WuestUtilities.network.sendToServer(new RedstoneScannerMessage(this.Config.GetNBTTagCompound()));
			
			// After sending the info to the server, make sure the client is updated.
			this.scannerTile.setConfig(this.Config);
			Block block = this.mc.theWorld.getBlockState(this.scannerTile.getPos()).getBlock();
			this.mc.theWorld.scheduleUpdate(this.scannerTile.getPos(), block, 2);
			
			this.mc.displayGuiScreen(null);
		}
	}
	
	/**
	 * Initializes the GUI when opening the GUI.
	 */
	protected void Initialize()
	{
		// Get the power configuration settings.
		TileEntity entity = this.mc.theWorld.getTileEntity(this.pos);

		if (entity != null && entity.getClass() == TileEntityRedstoneScanner.class)
		{
			this.Config = ((TileEntityRedstoneScanner)entity).getConfig();
			this.scannerTile = (TileEntityRedstoneScanner)entity;
		}
		else
		{
			this.scannerTile = new TileEntityRedstoneScanner();
			this.mc.theWorld.setTileEntity(pos, this.scannerTile);

			this.Config = this.scannerTile.getConfig();
		}

		this.Config.setBlockPos(this.pos);
		
		// Get the upper left hand corner of the GUI box.
		int grayBoxX = (this.width / 2) - 123;
		int grayBoxY = (this.height / 2) - 68;
		int x = 20;
		int y = 30;
		
		// These buttons are in the middle of the screen.
		x = this.width / 2 - 60;
		
		this.btnNorthSlider = new GuiSlider(4, grayBoxX, grayBoxY, 50, 20, "North: ", "", 0, 5, 
				this.Config.getFacingConfig(EnumFacing.NORTH.getName2()).getScanLength(), false, true);
		
		this.buttonList.add(this.btnNorthSlider);
		
		y += 25;
		grayBoxY +=25;
		
		this.btnSouthSlider = new GuiSlider(12, grayBoxX, grayBoxY, 50, 20, "South: ", "", 0, 5, 
				this.Config.getFacingConfig(EnumFacing.SOUTH.getName2()).getScanLength(), false, true);
		
		this.buttonList.add(this.btnSouthSlider);
		
		grayBoxY += 45;
		this.btnAnimals = new GuiCheckBox(15, grayBoxX, grayBoxY, "", this.Config.getAnimalsDetected());
		this.buttonList.add(this.btnAnimals);
		
		grayBoxY += 15;
		this.btnNonPlayers = new GuiCheckBox(16, grayBoxX, grayBoxY, "", this.Config.getNonPlayersDetected());
		this.buttonList.add(this.btnNonPlayers);

		// Middle Column:
		grayBoxX += 90;
		grayBoxY = (this.height / 2) - 68;
		
		this.btnEastSlider = new GuiSlider(10, grayBoxX, grayBoxY, 50, 20, "East: ", "", 0, 5, 
				this.Config.getFacingConfig(EnumFacing.EAST.getName2()).getScanLength(), false, true);
		
		this.buttonList.add(this.btnEastSlider);
		
		y += 25;
		grayBoxY += 25;
		
		this.btnWestSlider = new GuiSlider(11, grayBoxX, grayBoxY, 50, 20, "West: ", "", 0, 5, 
				this.Config.getFacingConfig(EnumFacing.WEST.getName2()).getScanLength(), false, true);
		
		this.buttonList.add(this.btnWestSlider);
		
		grayBoxY += 45;
		this.btnMonsters = new GuiCheckBox(17, grayBoxX, grayBoxY, "", this.Config.getMonstersDetected());
		this.buttonList.add(this.btnMonsters);
		
		grayBoxY += 15;
		this.btnPlayers = new GuiCheckBox(18, grayBoxX, grayBoxY, "", this.Config.getPlayersDetected());
		this.buttonList.add(this.btnPlayers);
		
		// Right Column: 
		grayBoxX += 90;
		grayBoxY = (this.height / 2) - 68;
		
		this.btnUpSlider = new GuiSlider(13, grayBoxX, grayBoxY, 50, 20, "Up: ", "", 0, 5, 
				this.Config.getFacingConfig(EnumFacing.UP.getName2()).getScanLength(), false, true);
		
		this.buttonList.add(this.btnUpSlider);
		
		grayBoxY += 25;
		this.btnDownSlider = new GuiSlider(14, grayBoxX, grayBoxY, 50, 20, "Down: ", "", 0, 5, 
				this.Config.getFacingConfig(EnumFacing.DOWN.getName2()).getScanLength(), false, true);
		
		this.buttonList.add(this.btnDownSlider);
		
		grayBoxX = (this.width / 2) - 128;
		grayBoxY = (this.height / 2) - 83;
		
		this.btnDone = new GuiButtonExt(1, grayBoxX + 10, grayBoxY + 136, 90, 20, "Done");
		this.buttonList.add(this.btnDone);

		this.btnCancel = new GuiButtonExt(2, grayBoxX + 147, grayBoxY + 136, 90, 20, "Cancel");
		this.buttonList.add(this.btnCancel);
	}
	
}
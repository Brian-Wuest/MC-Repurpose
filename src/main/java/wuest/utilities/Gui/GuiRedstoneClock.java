package wuest.utilities.Gui;

import java.io.IOException;

import wuest.utilities.WuestUtilities;
import wuest.utilities.Blocks.RedstoneClock.PowerConfiguration;
import wuest.utilities.Proxy.HouseMessage;
import wuest.utilities.Tiles.TileEntityRedstoneClock;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiRedstoneClock extends GuiScreen
{
	public static final int GUI_ID = 5;
	public BlockPos pos;
	private static final ResourceLocation backgroundTextures = new ResourceLocation("wuestutilities", "textures/gui/defaultBackground.png");
	public PowerConfiguration powerConfiguration;
	protected GuiButtonExt btnCancel;
	protected GuiButtonExt btnDone;
	
	public GuiRedstoneClock(int x, int y, int z)
	{
		this.pos = new BlockPos(x, y, z);
	}
	
	public void Initialize()
	{
		// Get the power configuration settings.
		TileEntity entity = this.mc.theWorld.getTileEntity(this.pos);
		this.powerConfiguration = new PowerConfiguration();
		
		if (entity != null && entity.getClass() == TileEntityRedstoneClock.class)
		{
			this.powerConfiguration = ((TileEntityRedstoneClock)entity).getPowerConfiguration();
		}
		
		// Get the upper left hand corner of the GUI box.
		int grayBoxX = (this.width / 2) - 128;
    	int grayBoxY = (this.height / 2) - 83;
		
		// Create the buttons.
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
    		this.mc.displayGuiScreen(null);
    	}
    }
}

package com.wuest.repurpose.Gui;

import java.io.IOException;

import org.lwjgl.input.Mouse;

import com.wuest.repurpose.Blocks.BlockCoffer.IronChestType;
import com.wuest.repurpose.Tiles.TileEntityCoffer;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

/**
 * 
 * @author WuestMan
 *
 */
public class GuiCoffer extends InventoryEffectRenderer
{
    public static final int GUI_ID = 7;
    private GUI type;
    
    /** Amount scrolled in Creative mode inventory (0 = top, 1 = bottom) */
    private float currentScroll;
    
    /** True if the left mouse button was held down last time drawScreen was called. */
    private boolean wasClicking;

    private GuiCoffer(GUI type, IInventory player, IInventory chest)
    {
        super(type.makeContainer(player, chest));
        this.type = type;
        this.xSize = type.xSize;
        this.ySize = type.ySize;
        this.allowUserInput = false;
    }
    
    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        super.initGui();
        
        // Make sure to scroll to the top on opening.
        ((ContainerCoffer)this.inventorySlots).scrollTo(0f);
    }
    
    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
    	boolean flag = Mouse.hasWheel() && Mouse.getDWheel() != 0;
    	
        this.drawDefaultBackground();
        int i = this.guiLeft;
        int j = this.guiTop;
        int k = i + 175;
        int l = j + 18;
        int i1 = k + 14;
        int j1 = l + 112;
        
        this.wasClicking = Mouse.isButtonDown(0);
        
        super.drawScreen(mouseX, mouseY, partialTicks);
        
        this.renderHoveredToolTip(mouseX, mouseY);
    }
    
    /**
     * Handles mouse input.
     */
    @Override
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0)
        {
            int j = (this.type.mainType.size + 9 - 1) / 9 - 5;

            if (i > 0)
            {
                i = 1;
            }

            if (i < 0)
            {
                i = -1;
            }

            this.currentScroll = (float)((double)this.currentScroll - (double)i / (double)j);
            this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
            ((ContainerCoffer)this.inventorySlots).scrollTo(this.currentScroll);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(this.type.guiResourceList.location);

        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }
    
	/**
	 * 
	 * @author WuestMan
	 * This enum is used to contain the various variants of chests and what their gui looks like.
	 *
	 */
    public enum ResourceList
    {
        IRON(new ResourceLocation("repurpose", "textures/gui/coffer_container.png"));
    	
        public final ResourceLocation location;

        ResourceList(ResourceLocation loc)
        {
            this.location = loc;
        }
    }

    /**
     * 
     * @author WuestMan
     * This enum is used to contain various configuration options for each type of chest size.
     *
     */
    public enum GUI
    {
        IRON(184, 202, ResourceList.IRON, IronChestType.IRON);

        private int xSize;
        private int ySize;
        private ResourceList guiResourceList;
        private IronChestType mainType;

        GUI(int xSize, int ySize, ResourceList guiResourceList, IronChestType mainType)
        {
            this.xSize = xSize;
            this.ySize = ySize;
            this.guiResourceList = guiResourceList;
            this.mainType = mainType;
        }

        public Container makeContainer(IInventory player, IInventory chest)
        {
            return new ContainerCoffer(player, chest, this.mainType, this.xSize, this.ySize);
        }

        public static GuiCoffer buildGUI(IronChestType type, IInventory playerInventory, TileEntityCoffer chestInventory)
        {
            return new GuiCoffer(values()[chestInventory.getType().ordinal()], playerInventory, chestInventory);
        }
    }

}

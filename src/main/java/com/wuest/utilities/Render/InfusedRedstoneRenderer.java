package com.wuest.utilities.Render;

import com.wuest.utilities.ModRegistry;
import com.wuest.utilities.Capabilities.IBlockModelCapability;
import com.wuest.utilities.Tiles.TileEntityInfusedRedstone;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;

public class InfusedRedstoneRenderer extends TileEntitySpecialRenderer<TileEntityInfusedRedstone>
{
	@Override
    public void renderTileEntityAt(TileEntityInfusedRedstone te, double x, double y, double z, float partialTicks, int destroyStage)
    {
		// Get the appropriate resource location to render the vanilla block.
		// Then overlay my "infused" texture on top of that.
	    GlStateManager.pushMatrix();
	    GlStateManager.translate(x, y, z);

        if (destroyStage >= 0)
        {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 2.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }
        else
        {
        	// Get the parent block capability for this tile entity.
        	IBlockModelCapability capability = te.getCapability(ModRegistry.BlockModel, EnumFacing.NORTH);
            this.bindTexture(capability.getBlockResourceLocation());
        }
	    
	    //Your rendering code goes here

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();

        if (destroyStage >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
}

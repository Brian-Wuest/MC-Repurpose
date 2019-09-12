package com.wuest.repurpose.Renderer;

import java.util.Random;

import com.google.common.primitives.SignedBytes;
import com.mojang.blaze3d.platform.GlStateManager;
import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Blocks.BlockCoffer;
import com.wuest.repurpose.Blocks.BlockCoffer.CofferType;
import com.wuest.repurpose.Tiles.TileEntityCoffer;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.model.ChestModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

public class TileEntityCofferRenderer extends TileEntityRenderer<TileEntityCoffer>
{
    private ChestModel model;

    private static float[][] shifts = { { 0.3F, 0.45F, 0.3F }, { 0.7F, 0.45F, 0.3F }, { 0.3F, 0.45F, 0.7F }, { 0.7F, 0.45F, 0.7F }, { 0.3F, 0.1F, 0.3F }, { 0.7F, 0.1F, 0.3F }, { 0.3F, 0.1F, 0.7F }, { 0.7F, 0.1F, 0.7F }, { 0.5F, 0.32F, 0.5F } };

    private static float halfPI = (float) (Math.PI / 2D);

    public TileEntityCofferRenderer()
    {
        this.model = new ChestModel();
    }

    @Override
    public void render(TileEntityCoffer te, double x, double y, double z, float partialTicks, int destroyStage)
    {
    	CofferType type = CofferType.IRON;
    	
        if (te == null || te.isRemoved())
        {
        	return;
        }

        Direction facing = Direction.SOUTH;

        if (te.hasWorld() && te.getWorld().getBlockState(te.getPos()).getBlock() == ModRegistry.Coffer())
        {
        	type = te.getCofferType();
            facing = te.getFacing();
            BlockState state = te.getWorld().getBlockState(te.getPos());
            type = state.with(BlockCoffer.VARIANT_PROP);
        }

        if (destroyStage >= 0)
        {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scalef(4F, 4F, 1F);
            GlStateManager.translatef(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }
        else
        {
            this.bindTexture(type.modelTexture);
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();

        GlStateManager.color4f(1F, 1F, 1F, 1F);
        GlStateManager.translatef((float) x, (float) y + 1F, (float) z + 1F);
        GlStateManager.scalef(1F, -1F, -1F);
        GlStateManager.translatef(0.5F, 0.5F, 0.5F);

        switch (facing)
        {
	        case NORTH:
	        {
	            GlStateManager.rotatef(180F, 0F, 1F, 0F);
	            break;
	        }
	        
	        case SOUTH:
	        {
	            GlStateManager.rotatef(0F, 0F, 1F, 0F);
	            break;
	        }
	        
	        case WEST:
	        {
	            GlStateManager.rotatef(90F, 0F, 1F, 0F);
	            break;
	        }
	        
	        case EAST:
	        {
	            GlStateManager.rotatef(270F, 0F, 1F, 0F);
	            break;
	        }
	        
	        default:
	        {
	            GlStateManager.rotatef(0F, 0F, 1F, 0F);
	            break;
	        }
        }

        GlStateManager.translatef(-0.5F, -0.5F, -0.5F);

        float lidangle = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;

        lidangle = 1F - lidangle;
        lidangle = 1F - lidangle * lidangle * lidangle;

        this.model.getLid().rotateAngleX = -(lidangle * halfPI);
        
        // Render the chest itself
        this.model.renderAll();
        
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (destroyStage >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
}
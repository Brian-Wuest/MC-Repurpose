package com.wuest.repurpose.Renderer;

import java.util.Random;

import com.google.common.primitives.SignedBytes;
import com.wuest.repurpose.ModRegistry;
import com.wuest.repurpose.Blocks.BlockCoffer;
import com.wuest.repurpose.Blocks.BlockCoffer.IronChestType;
import com.wuest.repurpose.Tiles.TileEntityCoffer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TileEntityCofferRenderer extends TileEntitySpecialRenderer<TileEntityCoffer>
{
    private ModelChest model;

    private static float[][] shifts = { { 0.3F, 0.45F, 0.3F }, { 0.7F, 0.45F, 0.3F }, { 0.3F, 0.45F, 0.7F }, { 0.7F, 0.45F, 0.7F }, { 0.3F, 0.1F, 0.3F }, { 0.7F, 0.1F, 0.3F }, { 0.3F, 0.1F, 0.7F }, { 0.7F, 0.1F, 0.7F }, { 0.5F, 0.32F, 0.5F } };

    private static float halfPI = (float) (Math.PI / 2D);

    public TileEntityCofferRenderer()
    {
        this.model = new ModelChest();
    }

    @Override
    public void render(TileEntityCoffer te, double x, double y, double z, float partialTicks, int destroyStage, float partial)
    {
    	IronChestType type = IronChestType.IRON;
    	
        if (te == null || te.isInvalid())
        {
        	return;
        }

        EnumFacing facing = EnumFacing.SOUTH;

        if (te.hasWorld() && te.getWorld().getBlockState(te.getPos()).getBlock() == ModRegistry.Coffer())
        {
        	type = te.getType();
            facing = te.getFacing();
            IBlockState state = te.getWorld().getBlockState(te.getPos());
            type = state.getValue(BlockCoffer.VARIANT_PROP);
        }

        if (destroyStage >= 0)
        {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4F, 4F, 1F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }
        else
        {
            this.bindTexture(type.modelTexture);
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();

        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.translate((float) x, (float) y + 1F, (float) z + 1F);
        GlStateManager.scale(1F, -1F, -1F);
        GlStateManager.translate(0.5F, 0.5F, 0.5F);

        switch (facing)
        {
	        case NORTH:
	        {
	            GlStateManager.rotate(180F, 0F, 1F, 0F);
	            break;
	        }
	        
	        case SOUTH:
	        {
	            GlStateManager.rotate(0F, 0F, 1F, 0F);
	            break;
	        }
	        
	        case WEST:
	        {
	            GlStateManager.rotate(90F, 0F, 1F, 0F);
	            break;
	        }
	        
	        case EAST:
	        {
	            GlStateManager.rotate(270F, 0F, 1F, 0F);
	            break;
	        }
	        
	        default:
	        {
	            GlStateManager.rotate(0F, 0F, 1F, 0F);
	            break;
	        }
        }

        GlStateManager.translate(-0.5F, -0.5F, -0.5F);

        float lidangle = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;

        lidangle = 1F - lidangle;
        lidangle = 1F - lidangle * lidangle * lidangle;

        this.model.chestLid.rotateAngleX = -(lidangle * halfPI);
        
        // Render the chest itself
        this.model.renderAll();
        
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (destroyStage >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
}
package com.wuest.repurpose.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;

public class MysteriousParticle extends ParticleRedstone
{

	public MysteriousParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, (float)xSpeedIn, (float)ySpeedIn, (float)zSpeedIn);
		
		// same as happy villager
        //this.setParticleTextureIndex(82); 
        //this.particleScale = 2.0F;
        //this.setRBGColorF(0x88, 0x00, 0x88);
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		this.setParticleTextureIndex(176 + (7 - this.particleAge * 8 / this.particleMaxAge));
	}
	
}

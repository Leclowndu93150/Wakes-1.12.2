package com.leclowndu93150.wakes.particle.custom;

import com.leclowndu93150.wakes.particle.SplashCloudSprites;
import com.leclowndu93150.wakes.simulation.WakeNode;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SplashCloudParticle extends Particle {
    private final boolean isFromPaddles;
    private final float oSize;

    public SplashCloudParticle(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y, z);
        this.motionX = velocityX;
        this.motionY = velocityY;
        this.motionZ = velocityZ;

        this.posY = y + 0.1;
        this.prevPosX = x;
        this.prevPosY = this.posY;
        this.prevPosZ = z;

        this.particleMaxAge = (int) (WakeNode.maxAge * 1.5);
        this.isFromPaddles = velocityX == 0 && velocityZ == 0;
        this.particleScale = isFromPaddles ? 3.0f : 1.5f;
        this.oSize = this.particleScale;

        float grey = 1.0F - (float) (Math.random() * 0.15);
        this.particleRed = grey;
        this.particleGreen = grey;
        this.particleBlue = grey;
        this.particleAlpha = 0.8f;

        this.particleTexture = SplashCloudSprites.getRandomSprite();
        this.canCollide = false;
    }

    @Override
    public void onUpdate() {
        this.particleAge++;

        if (this.isFromPaddles) {
            if (this.particleAge > particleMaxAge) {
                this.setExpired();
                return;
            }
            this.particleAlpha = 0.8f * (1f - (float) this.particleAge / this.particleMaxAge);
            return;
        } else {
            if (this.particleAge > particleMaxAge / 3) {
                this.setExpired();
                return;
            }
            this.particleAlpha = 0.8f * (1f - (float) this.particleAge / (this.particleMaxAge / 3f));
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        IBlockState state = world.getBlockState(new BlockPos(this.posX, this.posY, this.posZ));
        if (state.getMaterial() == Material.WATER) {
            this.motionY = 0.1;
            this.motionX *= 0.92;
            this.motionY *= 0.92;
            this.motionZ *= 0.92;
        } else {
            this.motionY -= 0.05;
            this.motionX *= 0.95;
            this.motionY *= 0.95;
            this.motionZ *= 0.95;
        }

        this.posX += motionX;
        this.posY += motionY;
        this.posZ += motionZ;
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotX, float rotZ, float rotYZ, float rotXY, float rotXZ) {
        float age = ((float) this.particleAge + partialTicks) / (float) this.particleMaxAge;
        this.particleScale = this.oSize * (1.0F - age * age * 0.5F);
        super.renderParticle(buffer, entityIn, partialTicks, rotX, rotZ, rotYZ, rotXY, rotXZ);
    }

    @Override
    public int getFXLayer() {
        return 1;
    }
}

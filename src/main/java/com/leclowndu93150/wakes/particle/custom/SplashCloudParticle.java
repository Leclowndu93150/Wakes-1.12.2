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

    public SplashCloudParticle(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y, z);
        this.motionX = velocityX;
        this.motionY = velocityY;
        this.motionZ = velocityZ;

        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;

        this.particleMaxAge = (int) (WakeNode.maxAge * 1.5);
        this.isFromPaddles = velocityX == 0 && velocityZ == 0;
        this.particleScale = isFromPaddles ? this.particleScale * 2 : 3.0f;

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
            if (this.particleAge <= 20) {
                this.particleAlpha = 1f - (float) this.particleAge / 20f;
            }
            return;
        } else {
            if (this.particleAge > particleMaxAge / 3) {
                this.setExpired();
                return;
            }
            if (this.particleAge <= 20) {
                this.particleAlpha = 1f - (float) this.particleAge / 20f;
            }
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
    public int getFXLayer() {
        return 1;
    }
}

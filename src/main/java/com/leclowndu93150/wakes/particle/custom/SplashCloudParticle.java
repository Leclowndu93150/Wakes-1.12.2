package com.leclowndu93150.wakes.particle.custom;

import com.leclowndu93150.wakes.simulation.WakeNode;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SplashCloudParticle extends Particle {
    Entity owner;
    final double offset;
    final boolean isFromPaddles;

    public SplashCloudParticle(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.motionX = velocityX;
        this.motionY = velocityY;
        this.motionZ = velocityZ;

        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;

        this.particleMaxAge = (int) (WakeNode.maxAge * 1.5);
        this.offset = velocityX;
        this.isFromPaddles = velocityX == 0;
        this.particleScale = isFromPaddles ? particleScale * 2 : 0.3f;
    }

    @Override
    public void onUpdate() {
        this.particleAge++;
        if (this.isFromPaddles) {
            if (this.particleAge > particleMaxAge) {
                this.setExpired();
                return;
            }
            this.particleAlpha = 1f - (float) this.particleAge / this.particleMaxAge;
            return;
        } else {
            if (this.particleAge > particleMaxAge / 3) {
                this.setExpired();
                return;
            }
            this.particleAlpha = 1f - (float) this.particleAge / (this.particleMaxAge / 3f);
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
        float f = ((float) this.particleAge + partialTicks) / (float) this.particleMaxAge;
        float scale = this.particleScale * (1.0F - f * f * 0.5F);

        float pX = (float) (this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
        float pY = (float) (this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
        float pZ = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);

        int light = this.getBrightnessForRender(partialTicks);
        int j = light >> 16 & 65535;
        int k = light & 65535;

        buffer.pos(pX - rotX * scale - rotXY * scale, pY - rotZ * scale, pZ - rotYZ * scale - rotXZ * scale)
                .tex(0, 1).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
                .lightmap(j, k).endVertex();
        buffer.pos(pX - rotX * scale + rotXY * scale, pY + rotZ * scale, pZ - rotYZ * scale + rotXZ * scale)
                .tex(1, 1).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
                .lightmap(j, k).endVertex();
        buffer.pos(pX + rotX * scale + rotXY * scale, pY + rotZ * scale, pZ + rotYZ * scale + rotXZ * scale)
                .tex(1, 0).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
                .lightmap(j, k).endVertex();
        buffer.pos(pX + rotX * scale - rotXY * scale, pY - rotZ * scale, pZ + rotYZ * scale - rotXZ * scale)
                .tex(0, 0).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
                .lightmap(j, k).endVertex();
    }

    @Override
    public int getFXLayer() {
        return 1;
    }
}

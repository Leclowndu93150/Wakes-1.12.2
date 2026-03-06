package com.leclowndu93150.wakes.render;

import com.leclowndu93150.wakes.simulation.QuadTree;
import com.leclowndu93150.wakes.simulation.WakeHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;

import java.nio.ByteBuffer;

public class WakeTexture {
    public int res;
    public int glTexId;
    public final boolean isUsingBricks;
    private final int resolutionScaling;

    public WakeTexture(int res, boolean useBricks) {
        this.res = res;
        this.glTexId = TextureUtil.glGenTextures();
        this.isUsingBricks = useBricks;
        this.resolutionScaling = useBricks ? QuadTree.BRICK_WIDTH : 1;

        GlStateManager.bindTexture(glTexId);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, 0);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MIN_LOD, 0);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LOD, 0);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0f);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        int dim = resolutionScaling * res;
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, dim, dim, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
    }

    public void loadTexture(ByteBuffer imgBuffer) {
        GlStateManager.bindTexture(glTexId);
        GL11.glPixelStorei(GL11.GL_UNPACK_ROW_LENGTH, 0);
        GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_PIXELS, 0);
        GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_ROWS, 0);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);

        int dim = resolutionScaling * WakeHandler.resolution.res;
        imgBuffer.position(0);
        imgBuffer.limit(dim * dim * 4);
        GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, dim, dim, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imgBuffer);
        imgBuffer.clear();
    }
}

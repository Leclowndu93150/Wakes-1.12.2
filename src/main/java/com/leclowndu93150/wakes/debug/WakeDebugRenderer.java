package com.leclowndu93150.wakes.debug;

import com.leclowndu93150.wakes.config.WakesConfig;
import com.leclowndu93150.wakes.simulation.Brick;
import com.leclowndu93150.wakes.simulation.WakeHandler;
import com.leclowndu93150.wakes.simulation.WakeNode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.Random;

public class WakeDebugRenderer {

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        WakeHandler wakeHandler = WakeHandler.getInstance().orElse(null);
        if (wakeHandler == null) return;

        if (!WakesConfig.drawDebugBoxes) return;

        Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
        if (viewer == null) return;

        double cx = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * event.getPartialTicks();
        double cy = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * event.getPartialTicks();
        double cz = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * event.getPartialTicks();

        Frustum frustum = new Frustum();
        frustum.setPosition(cx, cy, cz);

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();

        for (WakeNode node : wakeHandler.getVisible(frustum, WakeNode.class)) {
            AxisAlignedBB box = node.toBox().offset(-cx, -cy, -cz);
            drawFilledBox(box, 1f, 0f, 1f, 0.5f);
        }

        for (Brick brick : wakeHandler.getVisible(frustum, Brick.class)) {
            AxisAlignedBB box = new AxisAlignedBB(
                    brick.pos.x, brick.pos.y - (1 - WakeNode.WATER_OFFSET), brick.pos.z,
                    brick.pos.x + brick.dim, brick.pos.y, brick.pos.z + brick.dim
            ).offset(-cx, -cy, -cz);
            float[] col = Color.getHSBColor(new Random(brick.pos.hashCode()).nextFloat(), 1f, 1f).getRGBColorComponents(null);
            drawFilledBox(box, col[0], col[1], col[2], 0.5f);
        }

        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    private static void drawFilledBox(AxisAlignedBB box, float r, float g, float b, float a) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        buffer.pos(box.minX, box.minY, box.minZ).color(r, g, b, a).endVertex();
        buffer.pos(box.maxX, box.minY, box.minZ).color(r, g, b, a).endVertex();
        buffer.pos(box.maxX, box.minY, box.maxZ).color(r, g, b, a).endVertex();
        buffer.pos(box.minX, box.minY, box.maxZ).color(r, g, b, a).endVertex();

        buffer.pos(box.minX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
        buffer.pos(box.minX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();
        buffer.pos(box.maxX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();
        buffer.pos(box.maxX, box.maxY, box.minZ).color(r, g, b, a).endVertex();

        buffer.pos(box.minX, box.minY, box.minZ).color(r, g, b, a).endVertex();
        buffer.pos(box.minX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
        buffer.pos(box.maxX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
        buffer.pos(box.maxX, box.minY, box.minZ).color(r, g, b, a).endVertex();

        buffer.pos(box.maxX, box.minY, box.minZ).color(r, g, b, a).endVertex();
        buffer.pos(box.maxX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
        buffer.pos(box.maxX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();
        buffer.pos(box.maxX, box.minY, box.maxZ).color(r, g, b, a).endVertex();

        buffer.pos(box.minX, box.minY, box.maxZ).color(r, g, b, a).endVertex();
        buffer.pos(box.maxX, box.minY, box.maxZ).color(r, g, b, a).endVertex();
        buffer.pos(box.maxX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();
        buffer.pos(box.minX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();

        buffer.pos(box.minX, box.minY, box.minZ).color(r, g, b, a).endVertex();
        buffer.pos(box.minX, box.minY, box.maxZ).color(r, g, b, a).endVertex();
        buffer.pos(box.minX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();
        buffer.pos(box.minX, box.maxY, box.minZ).color(r, g, b, a).endVertex();

        tessellator.draw();
    }
}

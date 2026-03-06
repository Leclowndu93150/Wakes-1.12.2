package com.leclowndu93150.wakes.particle;

import com.leclowndu93150.wakes.WakesMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class SplashCloudSprites {

    private static final ResourceLocation[] SPRITE_LOCATIONS = new ResourceLocation[]{
            new ResourceLocation(WakesMod.MOD_ID, "particle/splash_cloud_1"),
            new ResourceLocation(WakesMod.MOD_ID, "particle/splash_cloud_2"),
            new ResourceLocation(WakesMod.MOD_ID, "particle/splash_cloud_3"),
            new ResourceLocation(WakesMod.MOD_ID, "particle/splash_cloud_4")
    };

    private static TextureAtlasSprite[] sprites = new TextureAtlasSprite[4];
    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {
        TextureMap map = event.getMap();
        for (int i = 0; i < SPRITE_LOCATIONS.length; i++) {
            map.registerSprite(SPRITE_LOCATIONS[i]);
        }
    }

    @SubscribeEvent
    public void onTextureStitchPost(TextureStitchEvent.Post event) {
        TextureMap map = event.getMap();
        for (int i = 0; i < SPRITE_LOCATIONS.length; i++) {
            sprites[i] = map.getAtlasSprite(SPRITE_LOCATIONS[i].toString());
        }
    }

    public static TextureAtlasSprite getRandomSprite() {
        return sprites[RANDOM.nextInt(sprites.length)];
    }
}

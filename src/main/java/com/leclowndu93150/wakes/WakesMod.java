package com.leclowndu93150.wakes;

import com.leclowndu93150.wakes.config.WakesConfig;
import com.leclowndu93150.wakes.debug.WakeDebugRenderer;
import com.leclowndu93150.wakes.event.ChunkWakeCleanup;
import com.leclowndu93150.wakes.event.WakeClientTicker;
import com.leclowndu93150.wakes.event.WakeWorldTicker;
import com.leclowndu93150.wakes.particle.SplashCloudSprites;
import com.leclowndu93150.wakes.render.SplashPlaneRenderer;
import com.leclowndu93150.wakes.render.WakeRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = WakesMod.MOD_ID, name = WakesMod.MOD_NAME, version = WakesMod.VERSION)
public class WakesMod {

    public static final String MOD_ID = "wakes";
    public static final String MOD_NAME = "Wakes";
    public static final String VERSION = "1.0.0";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static boolean areShadersEnabled = false;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        WakesConfig.init(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(new SplashCloudSprites());
        LOGGER.info("Wakes mod loaded for 1.12.2");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        SplashPlaneRenderer.init();
        MinecraftForge.EVENT_BUS.register(new WakeRenderer());
        MinecraftForge.EVENT_BUS.register(new SplashPlaneRenderer());
        MinecraftForge.EVENT_BUS.register(new WakeDebugRenderer());
        MinecraftForge.EVENT_BUS.register(new WakeWorldTicker());
        MinecraftForge.EVENT_BUS.register(new WakeClientTicker());
        MinecraftForge.EVENT_BUS.register(new ChunkWakeCleanup());
    }
}

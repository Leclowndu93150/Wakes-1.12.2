package com.leclowndu93150.wakes.event;

import com.leclowndu93150.wakes.debug.WakesDebugInfo;
import com.leclowndu93150.wakes.simulation.WakeHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;

public class WakeWorldTicker {

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (Minecraft.getMinecraft().world == null) return;

        WakesDebugInfo.reset();
        WakeHandler.getInstance(Minecraft.getMinecraft().world).ifPresent(WakeHandler::tick);
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.player != null && event.player.world.isRemote) {
            WakeHandler.init(event.player.world);
            if (Minecraft.getMinecraft().world == null ||
                    Minecraft.getMinecraft().world.provider.getDimension() != event.fromDim) {
                WakeHandler.killDimension(event.fromDim);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player != null && event.player.world.isRemote) {
            WakeHandler.init(event.player.world);
        }
    }
}

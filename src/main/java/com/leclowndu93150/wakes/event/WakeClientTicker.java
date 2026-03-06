package com.leclowndu93150.wakes.event;

import com.leclowndu93150.wakes.simulation.WakeHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.event.world.WorldEvent;

public class WakeClientTicker {

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft client = Minecraft.getMinecraft();
        if (event.phase != TickEvent.Phase.START) return;

        if (client.world == null) {
            WakeHandler.kill();
        } else if (!WakeHandler.getInstance(client.world).isPresent()) {
            WakeHandler.init(client.world);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (event.getWorld().isRemote) {
            WakeHandler.killDimension(event.getWorld().provider.getDimension());
        }
    }
}

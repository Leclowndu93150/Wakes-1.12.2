package com.leclowndu93150.wakes.event;

import com.leclowndu93150.wakes.simulation.WakeHandler;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChunkWakeCleanup {

    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) {
        if (event.getWorld().isRemote) {
            Chunk chunk = event.getChunk();
            int minX = chunk.x << 4;
            int minZ = chunk.z << 4;
            int maxX = minX + 16;
            int maxZ = minZ + 16;
            WakeHandler.getInstance().ifPresent(handler ->
                    handler.cleanupChunk(minX, minZ, maxX, maxZ));
        }
    }
}

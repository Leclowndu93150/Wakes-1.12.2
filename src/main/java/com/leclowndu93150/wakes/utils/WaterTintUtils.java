package com.leclowndu93150.wakes.utils;

public final class WaterTintUtils {
    // 1.13+ vanilla default water tint for biomes that do not override water color.
    private static final int DEFAULT_WATER_TINT = 0x3F76E4;

    private WaterTintUtils() {
    }

    public static int normalizeBiomeWaterColor(int biomeWaterColor) {
        return biomeWaterColor == 0xFFFFFF ? DEFAULT_WATER_TINT : biomeWaterColor;
    }
}

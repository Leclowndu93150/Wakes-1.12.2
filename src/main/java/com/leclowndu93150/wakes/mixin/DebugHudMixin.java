package com.leclowndu93150.wakes.mixin;

import com.leclowndu93150.wakes.config.WakesConfig;
import com.leclowndu93150.wakes.debug.WakesDebugInfo;
import net.minecraft.client.gui.GuiOverlayDebug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(GuiOverlayDebug.class)
public abstract class DebugHudMixin {

    @Inject(at = @At("RETURN"), method = "call")
    protected void wakes$getLeftText(CallbackInfoReturnable<List<String>> info) {
        if (WakesConfig.showDebugInfo) {
            if (WakesConfig.disableMod) {
                info.getReturnValue().add("[Wakes] Mod disabled!");
            } else {
                WakesDebugInfo.show(info);
            }
        }
    }
}

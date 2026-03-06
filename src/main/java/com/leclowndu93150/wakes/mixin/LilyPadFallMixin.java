package com.leclowndu93150.wakes.mixin;

import com.leclowndu93150.wakes.config.WakesConfig;
import com.leclowndu93150.wakes.config.enums.EffectSpawningRule;
import com.leclowndu93150.wakes.duck.ProducesWake;
import com.leclowndu93150.wakes.simulation.WakeNode;
import com.leclowndu93150.wakes.utils.WakesUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class LilyPadFallMixin {

    @Inject(at = @At("TAIL"), method = "onFallenUpon")
    public void wakes$onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance, CallbackInfo ci) {
        IBlockState above = world.getBlockState(pos.up());
        if (above.getBlock() != Blocks.WATERLILY) return;
        if (WakesConfig.disableMod) return;
        EffectSpawningRule rule = WakesUtils.getEffectRuleFromSource(entity);
        ProducesWake wakeProducer = (ProducesWake) entity;
        if (rule.simulateWakes) {
            wakeProducer.wakes$setWakeHeight(pos.getY() + WakeNode.WATER_OFFSET);
            WakesUtils.placeFallSplash(entity);
        }
    }
}

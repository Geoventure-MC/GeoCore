package fr.geoventure.geocore.harness.mixin;

import fr.geoventure.geocore.harness.Harness;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fabric has no callback after both world and screen drawing; HUD callbacks run too early.
 * Access widening cannot provide a render notification. Forge and NeoForge use their render-end events.
 */
@Mixin(GameRenderer.class)
public abstract class MixinHarnessFrames {
    //? if fabric {
    /*@Inject(method = "render", at = @At("RETURN"))
    private void geocore$completedFrame(CallbackInfo ci) {
        Harness.completedFrame();
    }
    *///?}
}

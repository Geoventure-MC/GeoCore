package fr.geoventure.geocore.mixin;

// Before 26.1 Fabric has only a post-HUD callback, which cannot suppress the vanilla crosshair. The
// smallest viable hook is the crosshair method itself; Forge-like loaders have cancellable overlay events.
//? if fabric && <26.1 {

/*import fr.geoventure.geocore.client.Crosshairs;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class MixinLegacyCrosshair {

    //? if >=1.21 {
    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void geocore$replaceCrosshair(GuiGraphics graphics, net.minecraft.client.DeltaTracker deltaTracker,
            CallbackInfo callback) {
    //?} else {
    /^@Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void geocore$replaceCrosshair(GuiGraphics graphics, CallbackInfo callback) {
    ^///?}
        if (Crosshairs.render(graphics)) callback.cancel();
    }
}
*///?}

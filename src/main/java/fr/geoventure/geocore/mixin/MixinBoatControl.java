package fr.geoventure.geocore.mixin;

import fr.geoventure.geocore.world.BoatControl;
import fr.geoventure.geocore.world.BoatInputs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hands vanilla's private {@code controlBoat} to a boat that implements {@link BoatControl}. The
 * steering class moved from {@code Boat} to {@code AbstractBoat} at 1.21.2; the flags and the method
 * are the same on both.
 */
//? if >=26.1 {
@Mixin(net.minecraft.world.entity.vehicle.boat.AbstractBoat.class)
//?} else {
/*@Mixin(net.minecraft.world.entity.vehicle.boat.Boat.class)
*///?}
public abstract class MixinBoatControl implements BoatInputs {

    @Shadow
    private boolean inputLeft;

    @Shadow
    private boolean inputRight;

    @Shadow
    private boolean inputUp;

    @Shadow
    private boolean inputDown;

    @Inject(method = "controlBoat", at = @At("HEAD"), cancellable = true)
    private void geocore$controlBoat(CallbackInfo ci) {
        if (this instanceof BoatControl control) {
            control.geocore$controlBoat();
            ci.cancel();
        }
    }

    @Override
    public boolean geocore$inputLeft() {
        return inputLeft;
    }

    @Override
    public boolean geocore$inputRight() {
        return inputRight;
    }

    @Override
    public boolean geocore$inputUp() {
        return inputUp;
    }

    @Override
    public boolean geocore$inputDown() {
        return inputDown;
    }
}

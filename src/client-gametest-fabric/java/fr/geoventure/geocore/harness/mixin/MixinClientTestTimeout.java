package fr.geoventure.geocore.harness.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** Native defaults and Fabric expose no timeout policy; widening cannot intercept framework-internal waits. */
@Pseudo
@Mixin(targets = "net.fabricmc.fabric.impl.client.gametest.context.ClientGameTestContextImpl", remap = false)
public abstract class MixinClientTestTimeout {
    @Unique
    private static final int geocore$timeoutScale = geocore$configuredScale();

    @Unique
    private static int geocore$configuredScale() {
        var key = "geocore.client.gametest.serverTickRate";
        var requested = System.getProperty(key);
        final int rate;
        try {
            rate = requested == null ? 20 : Integer.parseInt(requested);
        } catch (NumberFormatException invalid) {
            throw new IllegalArgumentException(key + " must be a positive integer: " + requested, invalid);
        }
        if (rate <= 0) throw new IllegalArgumentException(key + " must be positive: " + rate);
        return Math.max(1, rate / 20);
    }

    @ModifyVariable(method = "waitFor(Ljava/util/function/Predicate;I)I", at = @At("HEAD"),
            argsOnly = true, ordinal = 0, require = 1)
    private int geocore$waitTimeout(int timeout) {
        return timeout <= 0 ? timeout : Math.multiplyExact(timeout, geocore$timeoutScale);
    }
}

package fr.geoventure.geocore.mixin;

import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;

/**
 * {@link fr.geoventure.geocore.world.Gliders}, Forge 1.20.1 and NeoForge 1.21.1: the client only sends
 * {@code START_FALL_FLYING} after asking the chest item's {@code canElytraFly} in {@code LocalPlayer.aiStep}. Same
 * justification as {@link MixinGliderFlight}. Empty (and unlisted) on 26.1 and on Fabric.
 */
@Mixin(LocalPlayer.class)
public abstract class MixinGliderClientStart {

    //? if <26.1 && !fabric {
    /*@com.llamalad7.mixinextras.injector.ModifyExpressionValue(method = "aiStep",
            at = @org.spongepowered.asm.mixin.injection.At(value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;canElytraFly(Lnet/minecraft/world/entity/LivingEntity;)Z"))
    private boolean geocore$canStart(boolean vanilla) {
        return vanilla || fr.geoventure.geocore.world.Gliders.canGlide((LocalPlayer) (Object) this);
    }
    *///?}
}

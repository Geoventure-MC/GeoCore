package fr.geoventure.geocore.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;

/**
 * {@link fr.geoventure.geocore.world.Gliders}, Forge 1.20.1 and NeoForge 1.21.1: the server's start check
 * ({@code Player.tryToStartFallFlying}) asks the chest item's {@code canElytraFly}. Same justification as
 * {@link MixinGliderFlight}. Empty (and unlisted) on 26.1, where {@code canGlide} covers the start, and on Fabric.
 */
@Mixin(Player.class)
public abstract class MixinGliderStart {

    //? if <26.1 && !fabric {
    /*@com.llamalad7.mixinextras.injector.ModifyExpressionValue(method = "tryToStartFallFlying",
            at = @org.spongepowered.asm.mixin.injection.At(value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;canElytraFly(Lnet/minecraft/world/entity/LivingEntity;)Z"))
    private boolean geocore$canStart(boolean vanilla) {
        return vanilla || fr.geoventure.geocore.world.Gliders.canGlide((Player) (Object) this);
    }
    *///?}
}

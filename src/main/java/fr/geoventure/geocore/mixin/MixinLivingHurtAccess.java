package fr.geoventure.geocore.mixin;

import fr.geoventure.geocore.world.LivingHurtAccess;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Exposes {@link LivingEntity#lastHurt} and {@code getHurtSound}; see {@link LivingHurtAccess}.
 *
 * <p>{@code getHurtSound} is a shadow, not an access widener: widening a protected method makes it public, and every
 * subclass that overrides it as protected (vanilla mobs, HBM's own) would then no longer compile.
 */
@Mixin(LivingEntity.class)
public abstract class MixinLivingHurtAccess implements LivingHurtAccess {

    @Shadow
    protected float lastHurt;

    @Override
    public float geocore$lastHurt() {
        return lastHurt;
    }

    @Shadow
    protected abstract @Nullable SoundEvent getHurtSound(
            DamageSource source);

    @Override
    public @Nullable SoundEvent geocore$hurtSound(
            DamageSource source) {
        return getHurtSound(source);
    }

    //? if >=26.1 {
    @Shadow
    public abstract @Nullable Player getLastHurtByPlayer();

    @Override
    public @Nullable Player geocore$lastHurtByPlayer() {
        return getLastHurtByPlayer();
    }
    //?} else {
    /*@Shadow
    protected @Nullable Player lastHurtByPlayer;

    @Override
    public @Nullable Player geocore$lastHurtByPlayer() {
        return lastHurtByPlayer;
    }
    *///?}
}

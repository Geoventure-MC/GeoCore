package fr.geoventure.geocore.world;

import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.Nullable;

/**
 * {@code PrimedTnt}'s private igniter and private detonation, exposed by {@code MixinTntOwner}: the owner
 * field is private on every version, and {@code explode()} is private on Fabric (the Forge-like loaders
 * widen it), so a custom TNT replaces the detonation through {@link #geocore$explode()} instead of an override.
 */
public interface TntOwner {

    void geocore$setOwner(@Nullable LivingEntity owner);

    /** Asked at the head of vanilla's {@code explode()}; true skips vanilla's explosion. */
    default boolean geocore$explode() {
        return false;
    }
}

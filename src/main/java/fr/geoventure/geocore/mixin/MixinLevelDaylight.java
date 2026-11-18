package fr.geoventure.geocore.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import fr.geoventure.geocore.world.Skies;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Vanilla and loaders expose no runtime daytime-query provider.
 * Widening cannot alter dispatch, so compose providers with the native query result.
 */
@Mixin(Level.class)
public abstract class MixinLevelDaylight {

    //? if >=26.1 {
    @ModifyReturnValue(method = "isBrightOutside", at = @At("RETURN"))
    //?} else {
    /*@ModifyReturnValue(method = "isDay", at = @At("RETURN"))
    *///?}
    private boolean geocore$daylightQuery(boolean bright) {
        return Skies.brightOutside((Level) (Object) this, bright);
    }
}

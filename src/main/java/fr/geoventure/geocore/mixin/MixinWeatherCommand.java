package fr.geoventure.geocore.mixin;

import org.spongepowered.asm.mixin.Unique;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.geoventure.geocore.world.Weathers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.WeatherCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Vanilla exposes no level-owned weather-command provider. Loader registration replaces command execution;
 * this hook preserves native argument/default-duration handling. Widening alone cannot change that dispatch.
 */
@Mixin(WeatherCommand.class)
public abstract class MixinWeatherCommand {
    @Inject(method = "setClear", at = @At("HEAD"), cancellable = true)
    private static void geocore$clear(CommandSourceStack source, int duration, CallbackInfoReturnable<Integer> ci)
            throws CommandSyntaxException {
        geocore$apply(source, Weathers.Mode.CLEAR, duration, ci);
    }

    @Inject(method = "setRain", at = @At("HEAD"), cancellable = true)
    private static void geocore$rain(CommandSourceStack source, int duration, CallbackInfoReturnable<Integer> ci)
            throws CommandSyntaxException {
        geocore$apply(source, Weathers.Mode.RAIN, duration, ci);
    }

    @Inject(method = "setThunder", at = @At("HEAD"), cancellable = true)
    private static void geocore$thunder(CommandSourceStack source, int duration, CallbackInfoReturnable<Integer> ci)
            throws CommandSyntaxException {
        geocore$apply(source, Weathers.Mode.THUNDER, duration, ci);
    }
    @Unique
    private static void geocore$apply(CommandSourceStack source, Weathers.Mode mode, int duration,
                              CallbackInfoReturnable<Integer> ci) throws CommandSyntaxException {
        var result = Weathers.command(source, mode, duration);
        if (result != null) ci.setReturnValue(result);
    }
}

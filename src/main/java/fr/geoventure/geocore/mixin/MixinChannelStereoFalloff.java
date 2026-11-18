package fr.geoventure.geocore.mixin;

import com.mojang.blaze3d.audio.Channel;
import com.mojang.blaze3d.audio.SoundBuffer;
import fr.geoventure.geocore.client.Sounds;
import fr.geoventure.geocore.client.StereoFalloff;
import net.minecraft.client.sounds.AudioStream;
import org.lwjgl.openal.AL10;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Gives stereo sources the distance gain OpenAL skips ({@link StereoFalloff}), refreshed each tick by {@code updateStream}.
 * Rungs 1-3: vanilla sets a one-shot's gain once, NeoForge's {@code PlaySoundSourceEvent} fires only at start and
 * fabric-api has no sound event, and visibility is not the obstacle. TAIL injections that only re-set the gain.
 */
@Mixin(Channel.class)
public abstract class MixinChannelStereoFalloff {

    @Unique
    private boolean geocore$stereo;
    @Unique
    private float geocore$base = 1F;
    @Unique
    private float geocore$maxDistance;

    @Inject(method = "setVolume", at = @At("TAIL"))
    private void geocore$volume(float volume, CallbackInfo ci) {
        geocore$base = volume;
        geocore$apply();
    }

    @Inject(method = "linearAttenuation", at = @At("TAIL"))
    private void geocore$linear(float maxDistance, CallbackInfo ci) {
        geocore$maxDistance = maxDistance;
        geocore$apply();
    }

    @Inject(method = "disableAttenuation", at = @At("TAIL"))
    private void geocore$unattenuated(CallbackInfo ci) {
        geocore$maxDistance = 0F;
        geocore$apply();
    }

    @Inject(method = "attachStaticBuffer", at = @At("TAIL"))
    private void geocore$staticBuffer(SoundBuffer buffer, CallbackInfo ci) {
        geocore$stereo = StereoFalloff.stereo(Sounds.source((Channel) (Object) this));
        geocore$apply();
    }

    @Inject(method = "attachBufferStream", at = @At("TAIL"))
    private void geocore$stream(AudioStream stream, CallbackInfo ci) {
        geocore$stereo = stream.getFormat().getChannels() > 1;
        geocore$apply();
    }

    @Inject(method = {"setSelfPosition", "updateStream"}, at = @At("TAIL"))
    private void geocore$refresh(CallbackInfo ci) {
        geocore$apply();
    }

    @Unique
    private void geocore$apply() {
        if (!geocore$stereo) return;
        int source = Sounds.source((Channel) (Object) this);
        AL10.alSourcef(source, AL10.AL_GAIN, StereoFalloff.gain(source, geocore$base, geocore$maxDistance));
    }
}

package fr.geoventure.geocore.mixin;

//? if >=1.20.2 {
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl;
//?} else {
/*import net.minecraft.client.multiplayer.ClientPacketListener;
*///?}

import fr.geoventure.geocore.net.ChannelAccess;
import fr.geoventure.geocore.net.ClientSessions;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.20.2 {
/**
 * Vanilla has no metadata-session callback; loader play callbacks follow configuration.
 * Widening cannot add constructor notification or a common listener contract.
 */
@Mixin(ClientCommonPacketListenerImpl.class)
public abstract class MixinClientConnectionSession implements ChannelAccess.HasConnection {
    @Shadow protected Connection connection;
//?} else {
/*@Mixin(ClientPacketListener.class)
public abstract class MixinClientConnectionSession implements ChannelAccess.HasConnection {
    @Shadow private Connection connection;
*///?}

    @Override
    public Connection geocore$connection() { return connection; }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void geocore$metadataSession(CallbackInfo ci) {
        //? if >=1.20.2 {
        if ((Object) this instanceof ClientConfigurationPacketListenerImpl) {
            ClientSessions.configure(connection);
            return;
        }
        //?}
        ClientSessions.play(connection);
    }
}

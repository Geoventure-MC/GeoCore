package fr.geoventure.geocore.platform;

//? if fabric {
/*import net.fabricmc.api.ClientModInitializer;

/^*
 * GeoCore's Fabric client entrypoint. Fabric runs client entrypoints in no particular order, so a mod's clientbound
 * payload handlers may be registered after another mod already installed its own; GeoCore installs whatever is
 * still missing once the client has started, before any connection exists.
 ^/
public final class FabricClientEntrypoint implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        fr.geoventure.geocore.world.RecipeTables.initClient();
        fr.geoventure.geocore.data.DataFiles.initClient();
        fr.geoventure.geocore.profiler.ProfilerCommands.initClient();
        net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.CLIENT_STARTED.register(
                client -> fr.geoventure.geocore.net.GeoCoreNetwork.flushClientRegistrations());
    }
}
*///?}

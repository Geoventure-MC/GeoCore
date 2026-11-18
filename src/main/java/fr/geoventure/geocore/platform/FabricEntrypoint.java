package fr.geoventure.geocore.platform;

import fr.geoventure.geocore.client.UnbakedModels;
import fr.geoventure.geocore.profiler.ProfilerCommands;

//? if fabric {
/*import fr.geoventure.geocore.GeoCore;
import net.fabricmc.api.ModInitializer;

public class FabricEntrypoint implements ModInitializer {

    private static final java.util.concurrent.atomic.AtomicBoolean INITIALIZED = new java.util.concurrent.atomic.AtomicBoolean();

    @Override
    public void onInitialize() {
        initialize();
    }

    /^*
     * GeoCore's main initialisation, once: from this entrypoint, or earlier from a mod that needs every GeoCore-based
     * mod declared before its own main entrypoint finishes (GeoCore's KubeJS plugin, through
     * {@link ModEntries#ensureDeclared}); Fabric runs main entrypoints in no set order.
     ^/
    static void initialize() {
        if (!INITIALIZED.compareAndSet(false, true)) return;
        if (Platform.isClient()) UnbakedModels.initialize();
        GeoCore.LOG.info("GeoCore loaded on {} for MC {}", GeoCore.loader(), GeoCore.MC_VERSION);
        fr.geoventure.geocore.registry.RegistryDump.install(); // dev only: GEOCORE_REGISTRY_DUMP=<file>
        fr.geoventure.geocore.world.Physics.init();
        fr.geoventure.geocore.world.RecipeTables.init();
        fr.geoventure.geocore.world.LootCounts.init();
        fr.geoventure.geocore.data.DataFiles.init();
        ProfilerCommands.init();
        // The GUI scene renderer must be registered before the game renderer builds its PiP list.
        if (Platform.isClient()) fr.geoventure.geocore.gui.GuiScenes.bootstrap();
        // Every GeoCore-based mod's geocore:entry, in dependency order, and the registries filled once.
        ModEntries.runFabric();
    }
}
*///?}

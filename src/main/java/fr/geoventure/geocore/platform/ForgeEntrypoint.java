package fr.geoventure.geocore.platform;

import fr.geoventure.geocore.client.UnbakedModels;
import fr.geoventure.geocore.profiler.ProfilerCommands;

//? if forge {
/*import fr.geoventure.geocore.GeoCore;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(GeoCore.MOD_ID)
public final class ForgeEntrypoint {

    private static IEventBus modBus;

    public ForgeEntrypoint() {
        modBus = FMLJavaModLoadingContext.get().getModEventBus();
        GeoCore.LOG.info("GeoCore loaded on {} for MC {}", GeoCore.loader(), GeoCore.MC_VERSION);
        fr.geoventure.geocore.registry.RegistryDump.install(); // dev only: GEOCORE_REGISTRY_DUMP=<file>
        fr.geoventure.geocore.world.Physics.init();
        fr.geoventure.geocore.world.RecipeTables.init();
        fr.geoventure.geocore.world.LootCounts.init();
        fr.geoventure.geocore.data.DataFiles.init();
        ProfilerCommands.init();
        if (Platform.isClient()) ProfilerCommands.initClient();
        if (Platform.isClient()) fr.geoventure.geocore.data.DataFiles.initClient();
        if (Platform.isClient()) UnbakedModels.initialize();
        if (Platform.isClient()) fr.geoventure.geocore.world.RecipeTables.initClient();
        // Every GeoCore-based mod's registers and payloads, filled once by GeoCore (see ModEntries). Payloads wait
        // for common setup, after every mod is constructed: Forge numbers them as they are registered.
        modBus.addListener(ForgeEntrypoint::fillRegistry);
        modBus.addListener((net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) ->
                event.enqueueWork(() -> {
                    fr.geoventure.geocore.net.GeoCoreNetwork.flushCommonRegistrations();
                    ModEntries.runRegistered();
                }));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void fillRegistry(net.minecraftforge.registries.RegisterEvent event) {
        fr.geoventure.geocore.registry.DeferredRegister.flushRegistry(event.getRegistryKey(), (registryKey, entryKey, value) ->
                event.register((net.minecraft.resources.ResourceKey) registryKey, fr.geoventure.geocore.Ids.keyId(entryKey),
                        (java.util.function.Supplier) value));
    }

    public static IEventBus modBus() {
        if (modBus == null) throw new IllegalStateException("GeoCore's mod bus is not available yet");
        return modBus;
    }
}
*///?}

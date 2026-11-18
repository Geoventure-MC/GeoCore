package fr.geoventure.geocore.mobends;

import fr.geoventure.geocore.GeoCore;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

/** NeoForge entry of {@link MoBendsIntegration}. */
@EventBusSubscriber(modid = GeoCore.MOD_ID, value = Dist.CLIENT)
public final class MoBendsClientSetup {

    private MoBendsClientSetup() {}

    @SubscribeEvent
    public static void setup(FMLClientSetupEvent event) {
        if (ModList.get().isLoaded("mobends")) MoBendsIntegration.setup();
    }
}

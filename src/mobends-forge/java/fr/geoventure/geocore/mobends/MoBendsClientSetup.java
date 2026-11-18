package fr.geoventure.geocore.mobends;

import fr.geoventure.geocore.GeoCore;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/** Forge entry of {@link MoBendsIntegration}. */
@Mod.EventBusSubscriber(modid = GeoCore.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class MoBendsClientSetup {

    private MoBendsClientSetup() {}

    @SubscribeEvent
    public static void setup(FMLClientSetupEvent event) {
        if (ModList.get().isLoaded("mobends")) MoBendsIntegration.setup();
    }
}

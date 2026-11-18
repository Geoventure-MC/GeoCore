package fr.geoventure.geocore.harness;

//? if neoforge {
import fr.geoventure.geocore.GeoCore;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;

/** Starts the harness right after GeoCore is constructed. Found by annotation, so release jars hold no call to it. */
@EventBusSubscriber(modid = GeoCore.MOD_ID, value = Dist.CLIENT)
public final class HarnessSetup {

    private HarnessSetup() {}

    @SubscribeEvent
    public static void construct(FMLConstructModEvent event) {
        Harness.init();
    }
}
//?} elif forge {
/*import fr.geoventure.geocore.GeoCore;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

/^* Starts the harness right after GeoCore is constructed. Found by annotation, so release jars hold no call to it. ^/
@Mod.EventBusSubscriber(modid = GeoCore.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class HarnessSetup {

    private HarnessSetup() {}

    @SubscribeEvent
    public static void construct(FMLConstructModEvent event) {
        Harness.init();
    }
}
*///?}

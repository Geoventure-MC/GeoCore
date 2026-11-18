package fr.geoventure.geocore.mobends;

import fr.geoventure.geocore.GeoCore;
import fr.geoventure.geocore.render.BodyAnimations;
import goblinbob.mobends.api.skeleton.MoBendsAPI;

/** Mo' Bends answers {@link BodyAnimations}; the loader roots call {@link #setup} when it is loaded. */
public final class MoBendsIntegration {

    private MoBendsIntegration() {}

    public static void setup() {
        BodyAnimations.install(MoBendsAPI::isAnimated);
        GeoCore.LOG.info("Mo' Bends present: humanoid layers follow its skeleton");
    }
}

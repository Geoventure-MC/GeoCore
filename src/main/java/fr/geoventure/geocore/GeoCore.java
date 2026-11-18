package fr.geoventure.geocore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entry point and identity constants for GeoCore.
 *
 * <p>GeoCore exists to make one source tree compile and run across several Minecraft versions and
 * mod loaders. Its design rule is <b>backport the newest API downward</b>: every GeoCore type mirrors
 * the shape of the newest supported Minecraft version's API, and older versions get an implementation
 * that translates that shape into whatever the old API offered. Consumers therefore write code once,
 * against the newest shape, and never branch on version themselves.
 *
 * <p>The corollary rule: <b>no bytecode manipulation</b>. Version gaps are closed with ordinary Java
 * facades selected by Stonecutter comments. Mixins are a last resort, used only where no hook exists
 * on any supported target.
 */
public final class GeoCore {

    public static final String MOD_ID = "geocore";

    public static final Logger LOG = LoggerFactory.getLogger("GeoCore");

    /** The Minecraft version this build targets, injected by Stonecutter from the active node. */
    public static final String MC_VERSION = /*$ mc_version*/ "1.20.1";

    private GeoCore() {}

    /** The mod loader this build targets. Resolved at compile time, not runtime. */
    public static Loader loader() {
        //? if fabric {
        /*return Loader.FABRIC;
        *///?} elif neoforge {
        /*return Loader.NEOFORGE;
        *///?} elif forge {
        return Loader.FORGE;
        //?}
    }

    public enum Loader {
        FABRIC,
        NEOFORGE,
        FORGE;

        /** True for the two Forge-descended loaders, which share most of their API surface. */
        public boolean isForgeLike() {
            return this == NEOFORGE || this == FORGE;
        }
    }
}

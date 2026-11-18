package fr.geoventure.geocore.harness;

import fr.geoventure.geocore.GeoCore;
import fr.geoventure.geocore.client.ClientTicks;
import fr.geoventure.geocore.platform.Lifecycle;
import fr.geoventure.geocore.platform.Platform;
import java.util.Locale;
import net.minecraft.client.Minecraft;
//? if neoforge {
/*import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.neoforged.neoforge.common.NeoForge;
*///?} else if forge {
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
//?}

/**
 * Development-only in-game listener for the visual test harness (CLI: {@code GeoCore/harness}, see its README).
 *
 * <p>Active only when {@code -Dgeocore.harness.port=<n>} (or {@code GEOCORE_HARNESS_PORT}) is set on a
 * physical client in a development environment. It then accepts JSON-lines commands on a loopback
 * socket and executes them on the client thread: synthetic keyboard and mouse input through the vanilla
 * handlers, structured state queries, screenshots, world open/close, and window policy (never focused,
 * never grabbing the mouse, minimized by default, never pausing on lost focus).
 *
 * <p>Everything here is a facade over per-version vanilla API; scenarios live outside the mod.
 */
public final class Harness {

    private static boolean initialised;
    private static long tick;

    private Harness() {}

    /** The listener port, or 0 when the harness is not requested. */
    public static int port() {
        String value = System.getProperty("geocore.harness.port");
        if (value == null || value.isBlank()) value = System.getenv("GEOCORE_HARNESS_PORT");
        if (value == null || value.isBlank()) return 0;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            GeoCore.LOG.warn("[harness] ignoring malformed port {}", value);
            return 0;
        }
    }

    /** True when {@code -Dgeocore.harness.<key>} or {@code GEOCORE_HARNESS_<KEY>} is unset or not "false". */
    public static boolean flag(String key, boolean fallback) {
        String value = System.getProperty("geocore.harness." + key);
        if (value == null || value.isBlank()) {
            value = System.getenv("GEOCORE_HARNESS_" + key.toUpperCase(Locale.ROOT));
        }
        if (value == null || value.isBlank()) return fallback;
        return !value.equalsIgnoreCase("false") && !value.equals("0");
    }

    public static boolean isActive() {
        return initialised;
    }

    /** Current client tick counter, incremented at the end of every client tick. */
    public static long tick() {
        return tick;
    }

    /** Called once from each loader's client initialisation. Safe to call twice. */
    public static void init() {
        if (initialised) return;
        int port = port();
        if (port <= 0) return;
        if (!Platform.isClient()) return;
        if (!Platform.isDevelopment()) {
            GeoCore.LOG.warn("[harness] refusing to start outside a development environment");
            return;
        }
        initialised = true;
        GeoCore.LOG.info("[harness] starting listener on 127.0.0.1:{}", port);
        HarnessLog.install();
        HarnessCommands.registerDefaults();
        HarnessHooks.install(HarnessInput::keyHeld, HarnessInput::mouseHeld);
        Lifecycle.onServerStarted(HarnessPacing::serverStarted);
        ClientTicks.onEndTick(Harness::onTick);
        //? if neoforge {
        /*NeoForge.EVENT_BUS.addListener((RenderFrameEvent.Post event) -> completedFrame());
        *///?} else if forge {
        MinecraftForge.EVENT_BUS.addListener((TickEvent.RenderTickEvent event) -> {
            if (event.phase == TickEvent.Phase.END) completedFrame();
        });
        //?}
        HarnessServer.start(port);
    }

    /** Development render hook after world, HUD and screen drawing. */
    public static void completedFrame() {
        HarnessScreenshot.completedFrame();
    }

    private static void onTick() {
        tick++;
        Minecraft minecraft = Minecraft.getInstance();
        try {
            HarnessWindow.tick(minecraft);
            HarnessInput.tick(minecraft);
            HarnessCamera.tick(minecraft);
            HarnessSounds.tick(minecraft);
            HarnessServer.drain(minecraft);
        } catch (Throwable t) {
            GeoCore.LOG.error("[harness] tick failed", t);
        }
    }
}

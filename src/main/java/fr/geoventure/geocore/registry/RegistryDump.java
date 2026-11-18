package fr.geoventure.geocore.registry;

import fr.geoventure.geocore.GeoCore;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.platform.Lifecycle;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;

/**
 * Dev tool: once the server has started, writes every non-vanilla entry of every built-in registry, one per line
 * ({@code <registry> <id> <class>}, sorted), to the file named by {@code -Dgeocore.registryDump} or the environment
 * variable {@code GEOCORE_REGISTRY_DUMP}. Off unless one is set.
 *
 * <p>The proof that a change to how mods register (who flushes, substitution, a new registration chain) left the
 * registries alone is the diff of two dumps, not the absence of errors; the class column shows a substitution.
 */
public final class RegistryDump {

    private RegistryDump() {}

    /** Called once by GeoCore's loader entrypoint. */
    public static void install() {
        String target = System.getProperty("geocore.registryDump", System.getenv("GEOCORE_REGISTRY_DUMP"));
        if (target == null || target.isBlank()) return;
        Lifecycle.onServerStarted(server -> write(Path.of(target)));
    }

    private static void write(Path file) {
        List<String> lines = new ArrayList<>();
        for (Registry<?> registry : BuiltInRegistries.REGISTRY) add(lines, registry);
        Collections.sort(lines);
        try {
            Path parent = file.toAbsolutePath().getParent();
            if (parent != null) Files.createDirectories(parent);
            Files.write(file, lines);
            GeoCore.LOG.info("Registry dump: {} non-vanilla entries written to {}", lines.size(), file.toAbsolutePath());
        } catch (IOException e) {
            GeoCore.LOG.error("Registry dump to {} failed", file, e);
        }
    }

    private static <T> void add(List<String> lines, Registry<T> registry) {
        String registryId = Ids.keyId(registry.key()).toString();
        for (Map.Entry<ResourceKey<T>, T> entry : registry.entrySet()) {
            var id = Ids.keyId(entry.getKey());
            if ("minecraft".equals(id.getNamespace())) continue;
            lines.add(registryId + " " + id + " " + className(entry.getValue()));
        }
    }

    /** A lambda's generated class name carries a per-run hash; keep only the stable part. */
    private static String className(Object value) {
        String name = value == null ? "null" : value.getClass().getName();
        int lambda = name.indexOf("$$Lambda");
        return lambda < 0 ? name : name.substring(0, lambda) + "$$Lambda";
    }
}

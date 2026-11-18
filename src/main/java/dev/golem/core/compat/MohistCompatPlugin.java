package dev.golem.core.compat;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

/**
 * Pont de compatibilite Mohist 1.20.1 pour Golem Core :
 * sous serveur hybride Mohist, {@code PlayerList.respawn(ServerPlayer, boolean)}
 * remplace l'appel vanilla {@code MinecraftServer.overworld()} par
 * {@code MinecraftServer.getLevel(Level.OVERWORLD)}. Ce plugin bascule alors de
 * {@code MixinPlayerListRespawns} vers {@code MixinPlayerListRespawnsMohist} afin
 * de conserver les hooks {@code Respawns.RULES}, {@code Respawns.FALLBACKS} et
 * {@code WorldAttributes.BED_SPAWN} au lieu de les desactiver.
 */
public final class MohistCompatPlugin implements IMixinConfigPlugin {

    static final String STANDARD_RESPAWN_MIXIN = "dev.golem.core.mixin.MixinPlayerListRespawns";
    static final String MOHIST_RESPAWN_MIXIN = "dev.golem.core.mixin.MixinPlayerListRespawnsMohist";

    private static final boolean MOHIST = detectMohist();

    private static boolean detectMohist() {
        ClassLoader[] loaders = new ClassLoader[] {
                Thread.currentThread().getContextClassLoader(),
                MohistCompatPlugin.class.getClassLoader(),
                ClassLoader.getSystemClassLoader()
        };
        for (ClassLoader cl : loaders) {
            if (cl == null) {
                continue;
            }
            if (cl.getResource("com/mohistmc/MohistMCStart.class") != null
                    || cl.getResource("com/mohistmc/MohistMC.class") != null) {
                return true;
            }
        }
        return false;
    }

    static boolean selectMixin(boolean isMohist, String mixinClassName) {
        if (MOHIST_RESPAWN_MIXIN.equals(mixinClassName)) {
            return isMohist;
        }
        if (STANDARD_RESPAWN_MIXIN.equals(mixinClassName)) {
            return !isMohist;
        }
        return true;
    }

    @Override
    public void onLoad(String mixinPackage) {
        if (MOHIST) {
            System.out.println("[GolemCore-MohistCompat] Mohist detecte : MixinPlayerListRespawnsMohist active a la place de MixinPlayerListRespawns.");
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return selectMixin(MOHIST, mixinClassName);
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}

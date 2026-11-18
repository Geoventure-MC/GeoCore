package fr.geoventure.geocore.world;

import net.minecraft.server.level.ServerPlayer;

/** Cross-version advancement lookup and criterion award. */
public final class Advancements {

    private Advancements() {}

    /** Whether the player has completed advancement {@code id}; {@code null} when no such advancement is loaded. */
    public static Boolean isDone(ServerPlayer player, String id) {
        //? if >=1.21 {
        /*var advancement = player.level().getServer().getAdvancements().get(fr.geoventure.geocore.Ids.parse(id));
        *///?} else {
        var advancement = player.level().getServer().getAdvancements().getAdvancement(fr.geoventure.geocore.Ids.parse(id));
        //?}
        return advancement == null ? null : player.getAdvancements().getOrStartProgress(advancement).isDone();
    }

    public static void award(ServerPlayer player, String id, String criterion) {
        //? if >=1.21 {
        /*var advancement = player.level().getServer().getAdvancements().get(fr.geoventure.geocore.Ids.parse(id));
        if (advancement != null) player.getAdvancements().award(advancement, criterion);
        *///?} else {
        var advancement = player.level().getServer().getAdvancements().getAdvancement(fr.geoventure.geocore.Ids.parse(id));
        if (advancement != null) player.getAdvancements().award(advancement, criterion);
        //?}
    }
}

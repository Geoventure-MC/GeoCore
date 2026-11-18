package fr.geoventure.geocore.compat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MohistCompatPluginTest {

    @Test
    void selectsMohistRespawnMixinOnlyWhenMohistPresent() {
        assertFalse(MohistCompatPlugin.selectMixin(true, MohistCompatPlugin.STANDARD_RESPAWN_MIXIN));
        assertTrue(MohistCompatPlugin.selectMixin(true, MohistCompatPlugin.MOHIST_RESPAWN_MIXIN));

        assertTrue(MohistCompatPlugin.selectMixin(false, MohistCompatPlugin.STANDARD_RESPAWN_MIXIN));
        assertFalse(MohistCompatPlugin.selectMixin(false, MohistCompatPlugin.MOHIST_RESPAWN_MIXIN));
    }

    @Test
    void leavesAllOtherMixinsEnabled() {
        assertTrue(MohistCompatPlugin.selectMixin(true, "fr.geoventure.geocore.mixin.MixinBlockUse"));
        assertTrue(MohistCompatPlugin.selectMixin(false, "fr.geoventure.geocore.mixin.MixinBlockUse"));
        assertTrue(MohistCompatPlugin.selectMixin(true, "fr.geoventure.geocore.mixin.MixinAcceptedPlayerDeath"));
    }
}

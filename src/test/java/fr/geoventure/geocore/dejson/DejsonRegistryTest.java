package fr.geoventure.geocore.dejson;

import com.google.gson.JsonObject;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DejsonRegistryTest {

    @BeforeEach
    void clearRegistry() {
        Dejson.reset();
    }

    @Test
    void aDeclarationPublishesItsModelsBlockstateAndItem() {
        Dejson.block("geontm", "crate_steel")
                .models(Models.cubeAll("geontm:blocks/crate_steel"))
                .states(Variants.facing())
                .blockItem()
                .register();

        assertNotNull(Dejson.model("geontm:block/crate_steel"));
        assertEquals(Fixtures.json("{'parent':'geontm:block/crate_steel'}"),
                Dejson.model("geontm:item/crate_steel"));
        assertEquals(Fixtures.blockState("crate_steel"), Dejson.blockState("geontm:crate_steel"));
        assertEquals(Set.of("geontm:blocks/crate_steel"), Dejson.sprites());
    }

    @Test
    void aStairDeclarationPublishesAllThreeModels() {
        Dejson.block("geontm", "brick_compound_stairs")
                .models(Models.stairsAll("geontm:blocks/brick_compound"))
                .states(Variants.stairs())
                .blockItem()
                .register();

        assertEquals(Set.of("geontm:block/brick_compound_stairs",
                        "geontm:block/brick_compound_stairs_inner",
                        "geontm:block/brick_compound_stairs_outer",
                        "geontm:item/brick_compound_stairs"),
                Dejson.modelIds());
        assertEquals(Fixtures.blockState("brick_compound_stairs"),
                Dejson.blockState("geontm:brick_compound_stairs"));
    }

    @Test
    void aBlockstateNamingAModelTheShapeNeverEmitsIsRefused() {
        DejsonException failure = assertThrows(DejsonException.class, () ->
                Dejson.block("geontm", "half_slab")
                        .models(Models.cubeAll("geontm:blocks/x"))
                        .states(Variants.slab())
                        .register());

        assertTrue(failure.getMessage().contains("geontm:block/half_slab_top"), failure.getMessage());
        assertNull(Dejson.blockState("geontm:half_slab"), "a refused declaration must not publish");
    }

    @Test
    void anIncompleteDeclarationIsRefused() {
        assertThrows(DejsonException.class, () ->
                Dejson.block("geontm", "a").states(Variants.simple()).register());
        assertThrows(DejsonException.class, () ->
                Dejson.block("geontm", "b").models(Models.cubeAll("geontm:blocks/b")).register());
    }

    @Test
    void declaringTheSameBlockOrModelTwiceIsRefused() {
        Dejson.block("geontm", "a").models(Models.cubeAll("geontm:blocks/a"))
                .states(Variants.simple()).register();

        assertThrows(DejsonException.class, () -> Dejson.block("geontm", "a")
                .models(Models.cubeAll("geontm:blocks/a")).states(Variants.simple()).register());
        DejsonException collision = assertThrows(DejsonException.class, () -> Dejson.block("geontm", "b")
                .models(base -> {
                    Map<String, JsonObject> models = new LinkedHashMap<>();
                    models.put(base, Models.json(Models.CUBE_ALL, Slots.of("all", "geontm:blocks/b")));
                    models.put("geontm:block/a", Models.json(Models.CUBE_ALL, Slots.of("all", "geontm:blocks/a")));
                    return models;
                })
                .states(Variants.simple())
                .register());

        assertTrue(collision.getMessage().contains("geontm:block/a"), collision.getMessage());
        assertNull(Dejson.blockState("geontm:b"), "a refused declaration must not half-register");
        assertNull(Dejson.model("geontm:block/b"), "a refused declaration must not publish its models");
    }

    @Test
    void aBlockItemAlsoPublishesTheModernItemDefinition() {
        Dejson.block("geontm", "waste_log")
                .models(Models.column("geontm:blocks/waste_log_top", "geontm:blocks/waste_log_side"))
                .states(Variants.axis())
                .blockItem()
                .register();

        assertEquals(Fixtures.json("{'parent':'geontm:block/waste_log'}"),
                Dejson.model("geontm:item/waste_log"));
        assertEquals(Fixtures.json("{'model':{'type':'minecraft:model','model':'geontm:item/waste_log'}}"),
                Dejson.itemDefinition("geontm:item/waste_log"));
    }

    @Test
    void aBlockWithNoItemPublishesNoItemDefinition() {
        Dejson.block("geontm", "fire_digamma")
                .models(Models.cubeAll("geontm:blocks/fire_digamma"))
                .states(Variants.simple())
                .register();

        assertNull(Dejson.itemDefinition("geontm:item/fire_digamma"));
    }

    @Test
    void slotReferencesAreNotReportedAsSprites() {
        Dejson.block("geontm", "ref")
                .models(Models.parented(Models.CUBE_BOTTOM_TOP,
                        Slots.of("side", "geontm:blocks/ref").and("top", "#side").and("bottom", "#side")))
                .states(Variants.simple())
                .register();

        assertEquals(Set.of("geontm:blocks/ref"), Dejson.sprites());
    }

    @Test
    void declaredPropertiesAndKeysAreVisibleForTheCoverageCheck() {
        Dejson.Declaration declaration = Dejson.block("geontm", "s")
                .models(Models.stairsAll("geontm:blocks/s"))
                .states(Variants.stairs())
                .register();

        assertEquals(Set.of("facing", "half", "shape"), declaration.properties());
        assertEquals(40, declaration.variantKeys().size());
        assertTrue(declaration.dispatch().covers(Map.of(
                "facing", "north", "half", "top", "shape", "outer_left", "waterlogged", "false")));
    }
}

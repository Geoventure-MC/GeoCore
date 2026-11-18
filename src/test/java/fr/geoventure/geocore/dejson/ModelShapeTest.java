package fr.geoventure.geocore.dejson;

import com.google.gson.JsonObject;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModelShapeTest {

    private static final String BASE = "geontm:block/example";

    @Test
    void cubeAllEmitsOneParentedModel() {
        Map<String, JsonObject> models = Models.cubeAll("geontm:blocks/example").models(BASE);

        assertEquals(Set.of(BASE), models.keySet());
        assertEquals(Fixtures.json("{'parent':'minecraft:block/cube_all',"
                + "'textures':{'all':'geontm:blocks/example'}}"), models.get(BASE));
    }

    @Test
    void slabEmitsBottomTopAndDouble() {
        Map<String, JsonObject> models = Models.slabAll("geontm:blocks/brick_compound")
                .models("geontm:block/brick_compound_slab");

        assertEquals(List.of("geontm:block/brick_compound_slab",
                        "geontm:block/brick_compound_slab_top",
                        "geontm:block/brick_compound_slab_double"),
                List.copyOf(models.keySet()));
        Fixtures.assertSameModel(Fixtures.model("brick_compound_slab"),
                models.get("geontm:block/brick_compound_slab"));
        Fixtures.assertSameModel(Fixtures.model("brick_compound_slab_top"),
                models.get("geontm:block/brick_compound_slab_top"));
        Fixtures.assertSameModel(Fixtures.model("brick_compound_slab_double"),
                models.get("geontm:block/brick_compound_slab_double"));
    }

    @Test
    void stairsEmitStraightInnerAndOuter() {
        Map<String, JsonObject> models = Models.stairsAll("geontm:blocks/brick_concrete_broken")
                .models("geontm:block/brick_concrete_broken_stairs");

        assertEquals(3, models.size());
        Fixtures.assertSameModel(Fixtures.model("brick_concrete_broken_stairs"),
                models.get("geontm:block/brick_concrete_broken_stairs"));
        assertEquals(Models.INNER_STAIRS, models.get("geontm:block/brick_concrete_broken_stairs_inner")
                .get("parent").getAsString());
        assertEquals(Models.OUTER_STAIRS, models.get("geontm:block/brick_concrete_broken_stairs_outer")
                .get("parent").getAsString());
    }

    @Test
    void columnAndCubeBottomTopMatchTheirSlotNames() {
        Fixtures.assertSameModel(Fixtures.model("basalt"),
                Models.column("geontm:blocks/basalt_top", "geontm:blocks/basalt")
                        .models("geontm:block/basalt").get("geontm:block/basalt"));
        Fixtures.assertSameModel(Fixtures.model("c4"),
                Models.cubeBottomTop("geontm:blocks/c4_bottom", "geontm:blocks/c4_side", "geontm:blocks/c4_top")
                        .models("geontm:block/c4").get("geontm:block/c4"));
    }

    @Test
    void itemShapesCoverGeneratedHandheldAndBlockItem() {
        assertEquals(Fixtures.json("{'parent':'minecraft:item/generated',"
                        + "'textures':{'layer0':'geontm:items/ammo_debug'}}"),
                Models.generated("geontm:items/ammo_debug").models("geontm:item/ammo_debug")
                        .get("geontm:item/ammo_debug"));
        assertEquals(Fixtures.json("{'parent':'minecraft:item/handheld',"
                        + "'textures':{'layer0':'geontm:items/blowtorch'}}"),
                Models.handheld("geontm:items/blowtorch").models("geontm:item/blowtorch")
                        .get("geontm:item/blowtorch"));
        assertEquals(Fixtures.json("{'parent':'geontm:block/ash_digamma'}"),
                Models.blockItem("geontm:block/ash_digamma").models("geontm:item/ash_digamma")
                        .get("geontm:item/ash_digamma"));
    }

    @Test
    void generatedNumbersItsLayersFromZero() {
        JsonObject model = Models.generated("geontm:items/a", "geontm:items/b")
                .models("geontm:item/two").get("geontm:item/two");

        assertEquals("geontm:items/a", model.getAsJsonObject("textures").get("layer0").getAsString());
        assertEquals("geontm:items/b", model.getAsJsonObject("textures").get("layer1").getAsString());
    }

    @Test
    void slotsAreEmittedEvenWhenNothingReferencesThem() {
        JsonObject model = Models.parented("geontm:block/ctm_base",
                Slots.of("all", "geontm:blocks/deco_steel").and("ct", "geontm:blocks/deco_steel_ctm"))
                .models(BASE).get(BASE);

        assertTrue(model.getAsJsonObject("textures").has("ct"));
        assertEquals("geontm:blocks/deco_steel_ctm",
                model.getAsJsonObject("textures").get("ct").getAsString());
    }

    @Test
    void slotReferencesAreAccepted() {
        Slots slots = Slots.of("side", "geontm:blocks/a").and("particle", "#side");

        assertEquals(Set.of("geontm:blocks/a"), slots.sprites());
    }

    @Test
    void malformedDeclarationsThrowAtTheDeclarationSite() {
        assertThrows(DejsonException.class, () -> Slots.of("All", "geontm:blocks/a"));
        assertThrows(DejsonException.class, () -> Slots.of("all", "geontm:Blocks/A"));
        assertThrows(DejsonException.class, () -> Slots.of("all", "#Side"));
        assertThrows(DejsonException.class, () -> Slots.of("all", "geontm:blocks/a").and("all", "x:y"));
        assertThrows(DejsonException.class, () -> Models.parented("geontm:a:b", Slots.EMPTY));
        assertThrows(DejsonException.class, Models::generated);
    }
}

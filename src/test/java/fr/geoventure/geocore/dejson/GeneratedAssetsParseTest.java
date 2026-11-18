package fr.geoventure.geocore.dejson;

//? if >=26.1 {
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.SharedConstants;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelDispatcher;
import net.minecraft.client.resources.model.cuboid.CuboidModel;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The generated JSON through the game's own parsers, on the era whose model system was rewritten.
 *
 * <p>Nothing here asserts a string. A generated model is only correct if
 * {@link CuboidModel#fromStream} accepts it and reports back the parent and slots that were asked
 * for, and a generated blockstate is only correct if
 * {@link BlockStateModelDispatcher#instantiate} maps every state of a real block. Both are the
 * same code paths the client takes at reload, minus the bake.
 */
class GeneratedAssetsParseTest {

    @BeforeAll
    static void bootstrapRegistries() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    @Test
    void parentedModelsKeepTheirParentAndSlots() {
        CuboidModel model = parse(Models.cubeBottomTop("geontm:blocks/c4_bottom",
                "geontm:blocks/c4_side", "geontm:blocks/c4_top").models("geontm:block/c4").get("geontm:block/c4"));

        assertEquals("minecraft:block/cube_bottom_top", model.parent().toString());
        assertEquals(Set.of("bottom", "side", "top"), model.textureSlots().values().keySet());
    }

    @Test
    void aSlotNothingReferencesSurvivesTheParse() {
        CuboidModel model = parse(Models.parented(Models.CUBE_ALL,
                Slots.of("all", "geontm:blocks/deco_steel").and("ct", "geontm:blocks/deco_steel_ctm"))
                .models("geontm:block/deco_steel").get("geontm:block/deco_steel"));

        assertTrue(model.textureSlots().values().containsKey("ct"),
                "a connected-texture slot carrier must survive generation and parsing");
    }

    @Test
    void everyShapeInTheVocabularyParses() {
        for (Shape shape : new Shape[]{
                Models.cubeAll("geontm:blocks/a"),
                Models.cubeBottomTop("geontm:blocks/a", "geontm:blocks/b", "geontm:blocks/c"),
                Models.column("geontm:blocks/a", "geontm:blocks/b"),
                Models.orientable("geontm:blocks/a", "geontm:blocks/b", "geontm:blocks/c"),
                Models.cross("geontm:blocks/a"),
                Models.slabAll("geontm:blocks/a"),
                Models.slab("geontm:blocks/a", "geontm:blocks/b", "geontm:blocks/c"),
                Models.stairsAll("geontm:blocks/a"),
                Models.generated("geontm:items/a", "geontm:items/b"),
                Models.handheld("geontm:items/a"),
                Models.blockItem("geontm:block/a")}) {
            shape.models("geontm:block/example").forEach((id, json) -> assertEquals(
                    json.get("parent").getAsString(), parse(json).parent().toString(),
                    id + " lost its parent"));
        }
    }

    @Test
    void stairsCoverEveryStateOfARealStairBlock() {
        assertCoversEveryState(Blocks.OAK_STAIRS, Variants.stairs());
    }

    @Test
    void slabsCoverEveryStateOfARealSlabBlock() {
        assertCoversEveryState(Blocks.OAK_SLAB, Variants.slab());
    }

    @Test
    void axisCoversEveryStateOfARealLog() {
        assertCoversEveryState(Blocks.OAK_LOG, Variants.axis());
    }

    @Test
    void facingCrossedWithLitCoversEveryStateOfAFurnace() {
        assertCoversEveryState(Blocks.FURNACE,
                Variants.product(Variants.facing(), Variants.select("lit", "false", "_off", "true", "_on")));
    }

    @Test
    void aKeyThatOmitsAPropertyWildcardsIt() {
        Block chest = Blocks.CHEST;
        assertEquals(24, chest.getStateDefinition().getPossibleStates().size());
        assertEquals(4, Variants.facing().placements().size());

        assertCoversEveryState(chest, Variants.facing());
    }

    @Test
    void aBlockstateMissingAValueIsCaughtBeforeItShips() {
        Dispatch incomplete = Variants.select("axis", "x", "_x", "y", "_y");

        assertEquals(java.util.List.of("axis=z"), BlockStates.uncovered(Blocks.OAK_LOG, incomplete));
    }

    @Test
    void aDispatchNamingAPropertyTheBlockLacksIsCaught() {
        assertEquals(java.util.List.of("lid"),
                BlockStates.unknownProperties(Blocks.OAK_LOG, Variants.uniform("lid", "none")));
    }

    /**
     * The declaration-level matcher and the game's own instantiation must agree state for state.
     * If they ever disagree, every check built on {@link Dispatch#covers} is measuring nothing.
     */
    private static void assertCoversEveryState(Block block, Dispatch dispatch) {
        JsonObject json = dispatch.toJson("geontm:block/example");
        BlockStateModelDispatcher parsed = BlockStateModelDispatcher.CODEC
                .parse(JsonOps.INSTANCE, json).getOrThrow();
        Map<BlockState, BlockStateModel.UnbakedRoot> roots =
                parsed.instantiate(block.getStateDefinition(), () -> "test");

        assertEquals(java.util.List.of(), BlockStates.uncovered(block, dispatch));
        for (BlockState state : block.getStateDefinition().getPossibleStates()) {
            assertTrue(roots.containsKey(state), state + " got no model from the generated blockstate");
            assertTrue(dispatch.covers(BlockStates.properties(state)),
                    state + " is covered by the game but not by Dispatch.covers");
        }
        assertEquals(block.getStateDefinition().getPossibleStates().size(), roots.size());
    }

    private static CuboidModel parse(JsonObject json) {
        CuboidModel model = CuboidModel.fromStream(new StringReader(json.toString()));
        assertSame(CuboidModel.class, model.getClass());
        return model;
    }
}
//?}

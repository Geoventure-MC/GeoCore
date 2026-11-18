package fr.geoventure.geocore.dejson;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Every shape in the vocabulary must write exactly the file it replaces. */
class DejsonVocabularyTest {

    @BeforeEach
    void clearRegistry() {
        Dejson.reset();
    }

    private static JsonObject json(String text) {
        return JsonParser.parseString(text.replace('\'', '"')).getAsJsonObject();
    }

    @Test
    void resultComponentsMayBeStrings() {
        Crafting.componentShaped("geontm", "cart_test", Crafting.stack("geontm:cart_empty").with("geontm:cart_base", "steel")
                        .with("geontm:item_variant", 2),
                Crafting.rows("P"), Crafting.key("P", Crafting.item("geontm:plate_steel")));
        JsonObject recipe = DejsonWriter.files().get("data/geontm/recipe/cart_test.json");
        JsonObject components = recipe.getAsJsonObject("result").getAsJsonObject("components");
        assertEquals("steel", components.get("geontm:cart_base").getAsString());
        assertEquals(2, components.get("geontm:item_variant").getAsInt());
        assertThrows(DejsonException.class, () -> Crafting.stack("geontm:x").with("geontm:cart_base", ""));
    }

    @Test
    void elementGeometryKeepsIntegersAndFaceDetail() {
        Dejson.model("geontm:block/pipe_anchor_down", Model.parent("minecraft:block/block")
                .ambientOcclusion(false)
                .textures(Slots.of("all", "geontm:blocks/pipe_anchor", "particle", "geontm:blocks/pipe_anchor"))
                .elements(Model.Element.box(6, 0, 6, 10, 16.5, 10).rotation(8, 8, 8, "y", 22.5, true)
                        .face(Model.Face.of("north", "#all").uv(6, 0, 10, 16).cullface("north").rotation(90))));
        JsonObject model = DejsonWriter.files().get("assets/geontm/models/block/pipe_anchor_down.json");
        assertEquals(json("{'parent':'minecraft:block/block','ambientocclusion':false,"
                + "'textures':{'all':'geontm:blocks/pipe_anchor','particle':'geontm:blocks/pipe_anchor'},"
                + "'elements':[{'from':[6,0,6],'to':[10,16.5,10],"
                + "'rotation':{'origin':[8,8,8],'axis':'y','angle':22.5,'rescale':true},"
                + "'faces':{'north':{'uv':[6,0,10,16],'texture':'#all','cullface':'north','rotation':90}}}]}"), model);
        assertTrue(model.getAsJsonArray("elements").get(0).getAsJsonObject().getAsJsonArray("from").get(0)
                .getAsJsonPrimitive().getAsNumber() instanceof Integer);
        assertTrue(Dejson.sprites().contains("geontm:blocks/pipe_anchor"));
    }

    @Test
    void multipartAndVariantFilesAreWrittenUnderBlockstates() {
        States.multipart("geontm", "fence_metal")
                .part(new Variant("geontm:block/fence_metal_post"))
                .part(When.or(When.of("north", "true"), When.of("south", "true")),
                        new Variant("geontm:block/fence_metal_side", 0, 90, true))
                .register();
        assertEquals(json("{'multipart':[{'apply':{'model':'geontm:block/fence_metal_post'}},"
                        + "{'when':{'OR':[{'north':'true'},{'south':'true'}]},"
                        + "'apply':{'model':'geontm:block/fence_metal_side','y':90,'uvlock':true}}]}"),
                DejsonWriter.files().get("assets/geontm/blockstates/fence_metal.json"));
        assertThrows(DejsonException.class, () -> States.variants("geontm", "fence_metal")
                .variant("", new Variant("geontm:block/x")).register());
    }

    @Test
    void anItemDefinitionTreeIsEmittedWithTheOversizedFlag() {
        Dejson.item("geontm", "machine_press")
                .definition(ItemModels.special("geontm:item/machine_obj_base",
                        ItemModels.custom("geontm:machine_obj").with("obj", "geontm:models/press.obj").with("yaw", 90)))
                .oversizedInGui()
                .register();
        assertEquals(json("{'model':{'type':'minecraft:special','base':'geontm:item/machine_obj_base',"
                        + "'model':{'type':'geontm:machine_obj','obj':'geontm:models/press.obj','yaw':90}},"
                        + "'oversized_in_gui':true}"),
                DejsonWriter.files().get("assets/geontm/items/machine_press.json"));
    }

    @Test
    void dataShapesWriteTheirFiles() {
        Tags.of("item", "geontm", "repairs/cobalt").register();
        Tags.of("block", "geontm", "fbi_breakable").values("minecraft:chest").optional("geontm:crate_iron").register();
        Equipment.humanoid("geontm", "steel", "geontm:steel");
        Atlases.directories("minecraft", "blocks", "blocks", "blocks/");
        DataMaps.furnaceFuels().burnTime("geontm:lignite", 1200).register();
        Biomes.biome("geontm", "crater").downfall(0.0).precipitation(false).sky(0x525A52)
                .spawn(Biomes.MONSTER, "minecraft:witch", 5, 1, 1).register();

        assertEquals(json("{'replace':false,'values':[]}"),
                DejsonWriter.files().get("data/geontm/tags/item/repairs/cobalt.json"));
        assertEquals(json("{'replace':false,'values':['minecraft:chest',{'id':'geontm:crate_iron','required':false}]}"),
                DejsonWriter.files().get("data/geontm/tags/block/fbi_breakable.json"));
        assertEquals(json("{'layers':{'humanoid':[{'texture':'geontm:steel'}],'humanoid_leggings':[{'texture':'geontm:steel'}]}}"),
                DejsonWriter.files().get("assets/geontm/equipment/steel.json"));
        assertEquals(json("{'sources':[{'type':'minecraft:directory','source':'blocks','prefix':'blocks/'}]}"),
                DejsonWriter.files().get("assets/minecraft/atlases/blocks.json"));
        assertEquals(json("{'values':{'geontm:lignite':{'burn_time':1200}}}"),
                DejsonWriter.files().get("data/neoforge/data_maps/item/furnace_fuels.json"));
        JsonObject biome = DejsonWriter.files().get("data/geontm/worldgen/biome/crater.json");
        assertEquals("#525a52", biome.getAsJsonObject("attributes").get("minecraft:visual/sky_color").getAsString());
        assertEquals(11, biome.getAsJsonArray("features").size());
        assertEquals(1, biome.getAsJsonObject("spawners").getAsJsonArray("monster").size());
    }

    @Test
    void lootShapesAddedForTheMigration() {
        Drops.selfWhen("geontm", "door_metal", "half", "lower");
        Drops.stacks("geontm", "zirnox_destroyed", "geontm:concrete_smooth", 6);
        JsonObject door = DejsonWriter.files().get("data/geontm/loot_table/blocks/door_metal.json");
        JsonObject entry = door.getAsJsonArray("pools").get(0).getAsJsonObject().getAsJsonArray("entries").get(0)
                .getAsJsonObject();
        assertEquals("lower", entry.getAsJsonArray("conditions").get(0).getAsJsonObject()
                .getAsJsonObject("properties").get("half").getAsString());
        JsonObject wreck = DejsonWriter.files().get("data/geontm/loot_table/blocks/zirnox_destroyed.json");
        assertEquals(6, wreck.getAsJsonArray("pools").get(0).getAsJsonObject().getAsJsonArray("entries").get(0)
                .getAsJsonObject().getAsJsonArray("functions").get(0).getAsJsonObject().get("count").getAsInt());
    }

    @Test
    void featureModifierExcludesATag() {
        BiomeModifiers.addFeatures("geontm", "tfc_test", "#geontm:has_feature/tfc_test", "#minecraft:is_overworld",
                List.of("geontm:bedrock_ore"), BiomeModifiers.UNDERGROUND_ORES);
        assertEquals(json("{'type':'neoforge:add_features','biomes':{'type':'neoforge:and','values':["
                + "'#geontm:biome_modifier/tfc_test/biomes',{'type':'neoforge:not','value':'#geontm:biome_modifier/tfc_test/excluded'}]},"
                + "'features':'geontm:bedrock_ore','step':'underground_ores'}"),
                DejsonWriter.files().get("data/geontm/neoforge/biome_modifier/tfc_test.json"));
        assertEquals("forge:and", DejsonWriter.files().get("data/geontm/forge/biome_modifier/tfc_test.json")
                .getAsJsonObject("biomes").get("type").getAsString());
        assertEquals(json("{'replace':false,'values':[{'id':'#minecraft:is_overworld','required':false}]}"),
                DejsonWriter.files().get("data/geontm/tags/worldgen/biome/biome_modifier/tfc_test/excluded.json"));
        assertEquals(json("{'replace':false,'values':[{'id':'#geontm:has_feature/tfc_test','required':false}]}"),
                DejsonWriter.files().get("data/geontm/tags/worldgen/biome/biome_modifier/tfc_test/biomes.json"));
    }

    @Test
    void featureModifierExcludingABiomeNeedsNoTagOfItsOwn() {
        BiomeModifiers.addFeatures("geontm", "direct_test", "minecraft:plains", "minecraft:desert",
                List.of("geontm:bedrock_ore"), BiomeModifiers.UNDERGROUND_ORES);
        assertEquals(json("{'type':'neoforge:add_features','biomes':{'type':'neoforge:and','values':["
                + "'minecraft:plains',{'type':'neoforge:not','value':'minecraft:desert'}]},"
                + "'features':'geontm:bedrock_ore','step':'underground_ores'}"),
                DejsonWriter.files().get("data/geontm/neoforge/biome_modifier/direct_test.json"));
        assertEquals(null, DejsonWriter.files().get("data/geontm/tags/worldgen/biome/biome_modifier/direct_test/excluded.json"));
    }
}

package io.github.irishgreencitrus.occultengineering.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.data.MultiblockDataManager;
import com.klikli_dev.occultism.registry.OccultismBlocks;
import com.klikli_dev.occultism.registry.OccultismTags;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PentacleProvider implements DataProvider {
    private final Map<String, JsonElement> toSerialize = new HashMap<>();
    private final DataGenerator generator;

    public PentacleProvider(DataGenerator generator) {
        this.generator = generator;
    }

    private void start() {
        this.addPentacle("craft_puca",
                createPattern(
                        "CW   WC",
                        "W c c W",
                        " c W c ",
                        "  W0W  ",
                        " c W c ",
                        "W c c W",
                        "CW   WC"
                ),
                new MappingBuilder()
                        .candle('C')
                        .whiteChalk('W')
                        .copperChalk('c')
                        .chamber('0')
                        .ground().build());

        this.addPentacle("craft_puca_tier2",
                createPattern(
                        "  wwg gww  ",
                        " zC  g  CG ",
                        "cCzC S CGCc",
                        "c C g z C c",
                        "G  g c z  G",
                        " GS c0c SG ",
                        "G  z c g  G",
                        "c C z g C c",
                        "cCGC S CzCc",
                        " GC  g  Cz ",
                        "  wwg gww  "
                ),
                new MappingBuilder()
                        .chamber('0')
                        .candle('C')
                        .lightGrayChalk('G')
                        .skeleton('S')
                        .copperChalk('c')
                        .limeChalk('g')
                        .whiteChalk('w')
                        .zincChalk('z')
                        .ground()
                        .build()
        );
    }

    private List<String> createPattern(String... rows) {
        List<String> pattern = new ArrayList<>();
        for (String row : rows) {
            pattern.add(row.replace(" ", "_"));
        }
        return pattern;
    }

    private void addPentacle(String name, List<String> pattern, Map<Character, JsonElement> mappings) {
        this.addPentacle(OccultEngineering.asResource(name), pattern, mappings);
    }

    private void addPentacle(ResourceLocation rl, List<String> pattern, Map<Character, JsonElement> mappings) {
        JsonObject json = new JsonObject();

        json.addProperty("type", "modonomicon:dense");

        JsonArray outerPattern = createOuterPattern(pattern);

        json.add("pattern", outerPattern);

        // sanity check
        var allChars = String.join("", pattern).chars().mapToObj(i -> (char) i).collect(Collectors.toSet());
        allChars.removeAll(mappings.keySet());
        allChars.remove('_');

        if (!allChars.isEmpty()) {
            var dif = allChars.stream().map(String::valueOf).collect(Collectors.joining());
            throw new RuntimeException("Pentacle is missing mappings for following items: '" + dif + "'");
        }

        JsonObject jsonMapping = new JsonObject();
        for (Entry<Character, JsonElement> entry : mappings.entrySet())
            jsonMapping.add(String.valueOf(entry.getKey()), entry.getValue());
        json.add("mapping", jsonMapping);

        this.toSerialize.put(rl.getPath(), json);
    }

    private static JsonArray createOuterPattern(List<String> pattern) {
        JsonArray outerPattern = new JsonArray();
        JsonArray innerPattern = new JsonArray();

        for (String row : pattern)
            innerPattern.add(row);

        outerPattern.add(innerPattern);

        JsonArray ground = new JsonArray();
        for (int i = 0; i < pattern.size(); i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < pattern.get(i).length(); j++) {
                row.append((i + j) % 2 == 0 ? "*" : "+");
            }
            ground.add(row.toString());
        }

        outerPattern.add(ground);
        return outerPattern;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        Path folder = this.generator.getPackOutput().getOutputFolder();

        this.start();

        this.toSerialize.forEach((name, json) -> {
            Path path = folder.resolve("data/" + OccultEngineering.MODID + "/" + MultiblockDataManager.FOLDER + "/" + name + ".json");
            futures.add(DataProvider.saveStable(cache, json, path));
        });

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Pentacles: " + OccultEngineering.MODID;
    }

    private static class MappingBuilder {
        private final Map<Character, JsonElement> mappings = new HashMap<>();

        public MappingBuilder() {
            this.ground(); //always map ground
        }

        private MappingBuilder element(char c, JsonElement e) {
            this.mappings.put(c, e);
            return this;
        }

        private Map<Character, JsonElement> build() {
            return this.mappings;
        }

        private MappingBuilder block(char c, Supplier<? extends Block> b) {
            JsonObject json = new JsonObject();
            json.addProperty("type", "modonomicon:block");
            json.addProperty("block", BuiltInRegistries.BLOCK.getKey(b.get()).toString());
            return this.element(c, json);
        }

        private MappingBuilder blockDisplay(char c, Supplier<? extends Block> b, Supplier<? extends Block> display) {
            JsonObject json = new JsonObject();
            json.addProperty("type", "modonomicon:block");
            json.addProperty("block", BuiltInRegistries.BLOCK.getKey(b.get()).toString());
            json.addProperty("display", BuiltInRegistries.BLOCK.getKey(display.get()).toString());
            return this.element(c, json);
        }

        private MappingBuilder display(char c, Supplier<? extends Block> display) {
            JsonObject json = new JsonObject();
            json.addProperty("type", "modonomicon:display");
            json.addProperty("display", BuiltInRegistries.BLOCK.getKey(display.get()).toString());
            return this.element(c, json);
        }

        private MappingBuilder tag(char c, TagKey<Block> tag) {
            JsonObject json = new JsonObject();
            json.addProperty("type", "modonomicon:tag");
            json.addProperty("tag", "#" + tag.location());
            return this.element(c, json);
        }

        private MappingBuilder bowl() {
            return this.block('0', OccultismBlocks.GOLDEN_SACRIFICIAL_BOWL);
        }

        private MappingBuilder chamber(char c) {
            return this.block(c, OccultEngineeringBlocks.MECHANICAL_CHAMBER);
        }

        private MappingBuilder copperChalk(char c) {
            return this.block(c, OccultEngineeringBlocks.COPPER_CHALK);
        }

        private MappingBuilder zincChalk(char c) {
            return this.block(c, OccultEngineeringBlocks.ZINC_CHALK);
        }

        private MappingBuilder brassChalk(char c) {
            return this.block(c, OccultEngineeringBlocks.BRASS_CHALK);
        }

        private MappingBuilder candle(char c) {
            return this.tag(c, OccultismTags.Blocks.CANDLES);
        }

        private MappingBuilder whiteChalk(char c) {
            return this.block(c, OccultismBlocks.CHALK_GLYPH_WHITE);
        }

        private MappingBuilder greenChalk(char c) {
            return this.block(c, OccultismBlocks.CHALK_GLYPH_GREEN);
        }

        private MappingBuilder limeChalk(char c) {
            return this.block(c, OccultismBlocks.CHALK_GLYPH_LIME);
        }

        private MappingBuilder lightGrayChalk(char c) {
            return this.block(c, OccultismBlocks.CHALK_GLYPH_LIGHT_GRAY);
        }

        private MappingBuilder yellowChalk(char c) {
            return this.block(c, OccultismBlocks.CHALK_GLYPH_YELLOW);
        }

        private MappingBuilder purpleChalk(char c) {
            return this.block(c, OccultismBlocks.CHALK_GLYPH_PURPLE);
        }

        private MappingBuilder redChalk(char c) {
            return this.block(c, OccultismBlocks.CHALK_GLYPH_RED);
        }

        private MappingBuilder crystal(char c) {
            return this.block(c, OccultismBlocks.SPIRIT_ATTUNED_CRYSTAL);
        }

        private MappingBuilder skeleton(char c) {
            return this.block(c, () -> Blocks.SKELETON_SKULL);
        }


        private MappingBuilder ground() {
            return this.display('*', OccultismBlocks.OTHERSTONE).display('+', () -> Blocks.STONE);
        }

        private MappingBuilder wither() {
            return this.block('N', () -> Blocks.WITHER_SKELETON_SKULL);
        }
    }

}
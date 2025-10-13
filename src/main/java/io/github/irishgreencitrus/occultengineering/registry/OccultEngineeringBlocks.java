package io.github.irishgreencitrus.occultengineering.registry;

import com.klikli_dev.occultism.common.block.ChalkGlyphBlock;
import com.klikli_dev.occultism.registry.OccultismBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.config.OcEngStress;
import io.github.irishgreencitrus.occultengineering.content.block.WrenchableBlock;
import io.github.irishgreencitrus.occultengineering.content.block.mechanical_chamber.MechanicalChamberBlock;
import io.github.irishgreencitrus.occultengineering.content.block.mechanical_pulverizer.PulverizerBlock;
import io.github.irishgreencitrus.occultengineering.content.block.otherworld_detector.OtherworldDetectorBlock;
import io.github.irishgreencitrus.occultengineering.content.block.pentacle_altar.PentacleAltarBlock;
import io.github.irishgreencitrus.occultengineering.content.block.pucalith.PucalithBlock;
import io.github.irishgreencitrus.occultengineering.content.phlogiport.PhlogiportBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOnly;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import static io.github.irishgreencitrus.occultengineering.OccultEngineering.REGISTRATE;

@SuppressWarnings("unused")
public class OccultEngineeringBlocks {
    static {
        REGISTRATE.setCreativeTab(OccultEngineeringCreativeModeTab.CREATIVE_TAB);
    }

    // TODO: use Occultism's tag.
    // FUTURE: When Create gets updated to 1.21, use Occultism's tag, to prevent the pentacle jank.

    public static final BlockEntry<MechanicalChamberBlock> MECHANICAL_CHAMBER = REGISTRATE
            .block("mechanical_chamber", MechanicalChamberBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .transform(b -> b.tag(BlockTags.MINEABLE_WITH_PICKAXE))
            .properties(p -> p.mapColor(MapColor.COLOR_LIGHT_BLUE).noOcclusion())
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .transform(OcEngStress.setImpact(4.0))
            .simpleItem()
            .register();

    private static final Block.Properties GLYPH_PROPERTIES = Block.Properties.of()
            .sound(SoundType.WOOL)
            .pushReaction(PushReaction.DESTROY)
            .replaceable()
            .noCollission()
            .strength(5f, 30);


    public static void genChalkGlyph(DataGenContext<Block, ChalkGlyphBlock> context, RegistrateBlockstateProvider blockstateProvider) {
        var parentModel = blockstateProvider.models().getExistingFile(OccultEngineering.asResource("block/chalk_glyph"));
        blockstateProvider.getVariantBuilder(context.get())
                .forAllStates(state -> {
                    int sign = state.getValue(ChalkGlyphBlock.SIGN);
                    var glyphLocation = "block/chalk_glyph/" + sign;
                    var model = blockstateProvider.models().getBuilder(glyphLocation).parent(parentModel)
                            .texture("texture", glyphLocation);

                    return ConfiguredModel.builder()
                            .modelFile(model)
                            .rotationY((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot())
                            .build();
                });

    }

    public static final BlockEntry<ChalkGlyphBlock> COPPER_CHALK = REGISTRATE
            .block("copper_chalk", p -> new ChalkGlyphBlock(GLYPH_PROPERTIES, () -> 0xEB8E38, OccultEngineeringItems.COPPER_CHALK::get))
            .blockstate(OccultEngineeringBlocks::genChalkGlyph)
            .color(() -> () -> (state, world, pos, layer) -> 0xEB8E38)
            .register();

    public static final BlockEntry<ChalkGlyphBlock> ZINC_CHALK = REGISTRATE
            .block("zinc_chalk", p -> new ChalkGlyphBlock(GLYPH_PROPERTIES, () -> 0x92D6A4, OccultEngineeringItems.ZINC_CHALK::get))
            .blockstate(OccultEngineeringBlocks::genChalkGlyph)
            .color(() -> () -> (state, world, pos, layer) -> 0x92D6A4)
            .register();

    public static final BlockEntry<ChalkGlyphBlock> BRASS_CHALK = REGISTRATE
            .block("brass_chalk", p -> new ChalkGlyphBlock(GLYPH_PROPERTIES, () -> 0xFBC655, OccultEngineeringItems.BRASS_CHALK::get))
            .blockstate(OccultEngineeringBlocks::genChalkGlyph)
            .color(() -> () -> (state, world, pos, layer) -> 0xFBC655)
            .register();

    public static final BlockEntry<OtherworldDetectorBlock> OTHERWORLD_DETECTOR = REGISTRATE
            .block("otherworld_detector", OtherworldDetectorBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.COLOR_CYAN))
            .properties(p -> p.isRedstoneConductor(($1, $2, $3) -> false))
            .blockstate((c, p) -> BlockStateGen.simpleBlock(c, p, s -> {
                boolean powered = s.getValue(OtherworldDetectorBlock.POWERED);
                String name = c.getName() + (powered ? "_powered" : "");
                return p.models().cubeAll(name, p.modLoc("block/" + name));
            }))
            .transform(pickaxeOnly())
            .lang("Otherworld Detector")
            .simpleItem()
            .register();

    public static final BlockEntry<PulverizerBlock> MECHANICAL_PULVERIZER = REGISTRATE
            .block("mechanical_pulverizer", PulverizerBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY).noOcclusion())
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .transform(OcEngStress.setImpact(4.0))
            .simpleItem()
            .register();

    public static final BlockEntry<Block> STERLING_SILVER_BLOCK = REGISTRATE.block("sterling_silver_block", Block::new)
            .initialProperties(SharedProperties::softMetal)
            .transform(pickaxeOnly())
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY))
            .lang("Block of Sterling Silver")
            .simpleItem()
            .register();

    public static final BlockEntry<WrenchableBlock> FAN_ENSPIRIT_CATALYST = REGISTRATE.block("fan_enspirit_catalyst", WrenchableBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p
                    .mapColor(MapColor.TERRACOTTA_YELLOW)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
                    .isRedstoneConductor((state, level, pos) -> false)
            )
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
            .tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
            .lang("Fan Enspirit Catalyst")
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<PentacleAltarBlock> PENTACLE_ALTAR = REGISTRATE.block("pentacle_altar", PentacleAltarBlock::new)
            .initialProperties(SharedProperties::wooden)
            .properties(p -> p
                    .mapColor(MapColor.COLOR_RED)
                    .requiresCorrectToolForDrops()
            )
            .transform(axeOnly())
            .blockstate((c, p) -> p.simpleBlock(
                    c.getEntry(),
                    p.models().cubeBottomTop("pentacle_altar",
                            p.modLoc("block/pentacle_altar_side"),
                            p.modLoc("block/pentacle_altar_bottom"),
                            p.modLoc("block/pentacle_altar_top"))))
            .lang("Pentacle Altar")
            .simpleItem()
            .register();

    public static final BlockEntry<PucalithBlock> PUCALITH = REGISTRATE.block("pucalith", PucalithBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p
                    .mapColor(MapColor.COLOR_YELLOW)
                    .requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.simpleBlock(
                    c.getEntry(),
                    p.models().cubeBottomTop("pucalith",
                            p.modLoc("block/pucalith_side"),
                            p.modLoc("block/pucalith_bottom"),
                            p.modLoc("block/pucalith_top"))
            ))
            .lang("Púcalith")
            .simpleItem()
            .register();

    public static final BlockEntry<PhlogiportBlock> PHLOGIPORT = REGISTRATE.block("phlogiport", PhlogiportBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .transform(pickaxeOnly())
            .properties(p ->
                    p.noOcclusion()
                            .mapColor(MapColor.COLOR_GRAY)
                            .requiresCorrectToolForDrops())
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
            .lang("Phlogiport")
            .simpleItem()
            .register();


    public static void register() {
    }
}

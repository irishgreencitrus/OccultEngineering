package io.github.irishgreencitrus.occultengineering.registry;

import com.klikli_dev.occultism.common.block.ChalkGlyphBlock;
import com.klikli_dev.occultism.registry.OccultismBlocks;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.block.MechanicalChamberBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

import static io.github.irishgreencitrus.occultengineering.OccultEngineering.REGISTRATE;

public class OccultEngineeringBlocks {
    static {
        REGISTRATE.setCreativeTab(OccultEngineeringCreativeModeTab.CREATIVE_TAB);
    }
    // FUTURE: When Create gets updated to 1.21, use Occultism's tag, to prevent the pentacle jank.

    public static final BlockEntry<MechanicalChamberBlock> MECHANICAL_CHAMBER = REGISTRATE
            .block("mechanical_chamber", MechanicalChamberBlock::new)
            .initialProperties(OccultismBlocks.IESNIUM_BLOCK::get)
            .transform(b -> b.tag(BlockTags.MINEABLE_WITH_PICKAXE))
            .properties(p -> p.mapColor(MapColor.COLOR_LIGHT_BLUE).noOcclusion())
            .blockstate((ctx, pov) -> pov.simpleBlock(ctx.get(), AssetLookup.standardModel(ctx, pov)))
            .transform(BlockStressDefaults.setImpact(4.0))
            .simpleItem()
            .register();

    private static final Block.Properties GLYPH_PROPERTIES = Block.Properties.of()
            .sound(SoundType.WOOL)
            .pushReaction(PushReaction.DESTROY)
            .replaceable()
            .noCollission()
            .strength(5f, 30);


    public static void genChalkGlyph(DataGenContext<Block, ChalkGlyphBlock> context, RegistrateBlockstateProvider blockstateProvider) {
        ModelFile.ExistingModelFile parentModel = blockstateProvider.models().getExistingFile(ResourceLocation.fromNamespaceAndPath(OccultEngineering.MODID, "block/chalk_glyph"));
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
            .block("copper_chalk", p -> new ChalkGlyphBlock(GLYPH_PROPERTIES, () -> 0xf96a1e, OccultEngineeringItems.COPPER_CHALK::get))
            .blockstate(OccultEngineeringBlocks::genChalkGlyph)
            .color(() -> () -> (state, world, pos, layer) -> 0xf96a1e)
            .register();

    public static void register() {
    }
}

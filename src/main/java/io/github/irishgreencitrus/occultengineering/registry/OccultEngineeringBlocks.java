package io.github.irishgreencitrus.occultengineering.registry;

import com.klikli_dev.occultism.registry.OccultismBlocks;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.irishgreencitrus.occultengineering.block.MechanicalChamberBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.material.MapColor;

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
            .transform(BlockStressDefaults.setImpact(1.0))
            .simpleItem()
            .register();

    public static void register() {
    }
}

package io.github.irishgreencitrus.occultengineering.registry;

import com.klikli_dev.occultism.common.block.ChalkGlyphBlock;
import com.klikli_dev.occultism.registry.OccultismBlocks;
import com.simibubi.create.api.schematic.requirement.SchematicRequirementRegistries;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.List;

public class OccultEngineeringSchematic {
    public static void register() {
        registerDamageChalk(OccultEngineeringBlocks.COPPER_CHALK);
        registerDamageChalk(OccultEngineeringBlocks.ZINC_CHALK);
        registerDamageChalk(OccultEngineeringBlocks.BRASS_CHALK);
        var occultismChalks = List.of(
                OccultismBlocks.CHALK_GLYPH_WHITE,
                OccultismBlocks.CHALK_GLYPH_YELLOW,
                OccultismBlocks.CHALK_GLYPH_PURPLE,
                OccultismBlocks.CHALK_GLYPH_RED,
                OccultismBlocks.CHALK_GLYPH_LIGHT_GRAY,
                OccultismBlocks.CHALK_GLYPH_GRAY,
                OccultismBlocks.CHALK_GLYPH_BLACK,
                OccultismBlocks.CHALK_GLYPH_BROWN,
                OccultismBlocks.CHALK_GLYPH_ORANGE,
                OccultismBlocks.CHALK_GLYPH_LIME,
                OccultismBlocks.CHALK_GLYPH_GREEN,
                OccultismBlocks.CHALK_GLYPH_CYAN,
                OccultismBlocks.CHALK_GLYPH_LIGHT_BLUE,
                OccultismBlocks.CHALK_GLYPH_BLUE,
                OccultismBlocks.CHALK_GLYPH_MAGENTA,
                OccultismBlocks.CHALK_GLYPH_PINK,
                OccultismBlocks.CHALK_GLYPH_RAINBOW,
                OccultismBlocks.CHALK_GLYPH_VOID
        );

        for (var c : occultismChalks) registerDamageChalk(c);
    }

    private static void registerDamageChalk(BlockEntry<ChalkGlyphBlock> glyph) {
        registerDamageItem(glyph.get(), glyph.get().getChalk());
    }

    private static void registerDamageChalk(DeferredBlock<? extends ChalkGlyphBlock> glyph) {
        registerDamageItem(glyph.get(), glyph.get().getChalk());
    }

    private static void registerDamageItem(Block block, Item item) {
        SchematicRequirementRegistries.BLOCKS.register(block, (state, blockEntity) -> new ItemRequirement(ItemRequirement.ItemUseType.DAMAGE, item));
    }
}

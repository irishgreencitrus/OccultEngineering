package io.github.irishgreencitrus.occultengineering.registry;

import com.klikli_dev.occultism.registry.OccultismBlocks;
import com.klikli_dev.occultism.registry.OccultismItems;
import com.simibubi.create.api.schematic.requirement.SchematicRequirementRegistries;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

public class OccultEngineeringSchematic {
    public static void register() {
        registerDamageItem(OccultEngineeringBlocks.COPPER_CHALK, OccultEngineeringItems.COPPER_CHALK);
        registerDamageItem(OccultEngineeringBlocks.ZINC_CHALK, OccultEngineeringItems.ZINC_CHALK);
        registerDamageItem(OccultEngineeringBlocks.BRASS_CHALK, OccultEngineeringItems.BRASS_CHALK);
        registerDamageItem(OccultismBlocks.CHALK_GLYPH_WHITE, OccultismItems.CHALK_WHITE);
        registerDamageItem(OccultismBlocks.CHALK_GLYPH_YELLOW, OccultismItems.CHALK_YELLOW);
        registerDamageItem(OccultismBlocks.CHALK_GLYPH_RED, OccultismItems.CHALK_RED);
        registerDamageItem(OccultismBlocks.CHALK_GLYPH_PURPLE, OccultismItems.CHALK_PURPLE);
    }

    private static void registerDamageItem(DeferredBlock<?> block, DeferredItem<?> item) {
        registerDamageItem(block.get(), item.get());
    }

    private static void registerDamageItem(BlockEntry<?> block, ItemEntry<?> item) {
        registerDamageItem(block.get(), item.get());
    }


    private static void registerDamageItem(Block block, Item item) {
        SchematicRequirementRegistries.BLOCKS.register(block, (state, blockEntity) -> new ItemRequirement(ItemRequirement.ItemUseType.DAMAGE, item));
    }
}

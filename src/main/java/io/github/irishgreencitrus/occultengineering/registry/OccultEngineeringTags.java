package io.github.irishgreencitrus.occultengineering.registry;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class OccultEngineeringTags {
    public static final TagKey<Block> ENSPIRIT_CATALYST = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(OccultEngineering.MODID, "enspirit_catalyst")
    );

    public static final TagKey<Fluid> SPIRIT_SOLUTION_FLUID = TagKey.create(
            Registries.FLUID,
            ResourceLocation.fromNamespaceAndPath(OccultEngineering.MODID, "spirit_solution_fluid")
    );

    public static final TagKey<Item> MECHANICAL_CHAMBER_INSERTABLE = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(OccultEngineering.MODID, "mechanical_chamber_insertable")
    );
}

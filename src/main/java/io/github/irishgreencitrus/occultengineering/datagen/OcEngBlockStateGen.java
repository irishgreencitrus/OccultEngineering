package io.github.irishgreencitrus.occultengineering.datagen;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import io.github.irishgreencitrus.occultengineering.content.block.OcEngBlockStates;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class OcEngBlockStateGen {
    public static ModelFile tieredModel(DataGenContext<?, ?> ctx, RegistrateBlockstateProvider prov, int tier) {
        return prov.models()
                .getExistingFile(prov.modLoc("block/" + ctx.getName() + (tier <= 1 ? "" : "_tier" + tier)));
    }

    public static <T extends Block> void horizontalBlockWithTier(DataGenContext<Block, T> ctx,
                                                             RegistrateBlockstateProvider prov) {
        prov.getVariantBuilder(ctx.getEntry())
                .forAllStates(state -> {
                    var axis = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                    var tier = state.getValue(OcEngBlockStates.TIER);

                    return ConfiguredModel.builder()
                            .modelFile(tieredModel(ctx, prov, tier))
                            .rotationY((int) ((axis.toYRot() + 180) % 360))
                            .build();
                });
    }
}

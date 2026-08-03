package io.github.irishgreencitrus.occultengineering.datagen;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import io.github.irishgreencitrus.occultengineering.content.block.OcEngBlockStates;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

public final class OcEngBlockStateGen {
    private OcEngBlockStateGen() {
    }

    public static ModelFile tieredModel(DataGenContext<?, ?> context, RegistrateBlockstateProvider provider, int tier) {
        return provider.models().getExistingFile(provider.modLoc(
                "block/" + context.getName() + (tier <= 1 ? "" : "_tier" + tier)));
    }

    public static <T extends Block> void horizontalBlockWithTier(DataGenContext<Block, T> context,
                                                                 RegistrateBlockstateProvider provider) {
        provider.getVariantBuilder(context.getEntry()).forAllStates(state -> {
            var facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            var tier = state.getValue(OcEngBlockStates.TIER);
            return ConfiguredModel.builder()
                    .modelFile(tieredModel(context, provider, tier))
                    .rotationY((int) ((facing.toYRot() + 180) % 360))
                    .build();
        });
    }
}

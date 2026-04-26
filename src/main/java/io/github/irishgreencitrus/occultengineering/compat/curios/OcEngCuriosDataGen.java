package io.github.irishgreencitrus.occultengineering.compat.curios;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;

public class OcEngCuriosDataGen extends CuriosDataProvider {
    public OcEngCuriosDataGen(PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
        super(OccultEngineering.MODID, output, fileHelper, registries);
    }

    @Override
    public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper) {
        createEntities("players").addPlayer().addSlots("head");
    }
}

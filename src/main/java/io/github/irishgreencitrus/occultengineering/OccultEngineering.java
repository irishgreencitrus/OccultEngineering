package io.github.irishgreencitrus.occultengineering;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import io.github.irishgreencitrus.occultengineering.datagen.DataProviders;
import io.github.irishgreencitrus.occultengineering.registry.*;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;

@Mod(OccultEngineering.MODID)
public class OccultEngineering {
    public static final String NAME = "Create: Occult Engineering";
    public static final String MODID = "occultengineering";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID).defaultCreativeTab((ResourceKey<CreativeModeTab>) null);


    public OccultEngineering(IEventBus modEventBus, ModContainer container) {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modEventBus.addListener(OccultEngineering::onRegister);
        modEventBus.addListener(DataProviders::gatherData);
        //modEventBus.addListener(REGISTRATE::genData);
        OccultEngineeringCreativeModeTab.register(modEventBus);

        OccultEngineeringItems.init();
        OccultEngineeringFluids.init();
        OccultEngineeringBlocks.init();
        OccultEngineeringBlockEntities.init();
        REGISTRATE.registerEventListeners(modEventBus);

        LOGGER.info("Setup is complete.");
    }

    public static void onRegister(final RegisterEvent event) {
        OccultEngineeringFanProcessingTypes.init();
        OccultEngineeringArmInteractionPoints.init();
    }

    public static LangBuilder lang() {
        return new LangBuilder(MODID);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}

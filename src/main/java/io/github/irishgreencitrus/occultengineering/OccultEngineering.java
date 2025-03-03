package io.github.irishgreencitrus.occultengineering;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import io.github.irishgreencitrus.occultengineering.registry.*;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;

@Mod(OccultEngineering.MODID)
public class OccultEngineering {
    public static final String NAME = "Create: Occult Engineering";
    public static final String MODID = "occultengineering";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    //public static final Lang LANG = Lang;

    public OccultEngineering() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get()
                .getModEventBus();
        modEventBus.addListener(OccultEngineering::onRegister);


        REGISTRATE.registerEventListeners(modEventBus);

        OccultEngineeringCreativeModeTab.register(modEventBus);

        OccultEngineeringItems.register();
        OccultEngineeringFluids.register();
        OccultEngineeringBlocks.register();
        OccultEngineeringBlockEntities.register();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> OccultEngineeringClient.onCtorClient(modEventBus));
        LOGGER.info("Setup is complete.");
    }

    public static void onRegister(final RegisterEvent event) {
        OccultEngineeringFanProcessingTypes.init();
        OccultEngineeringArmInteractionPoints.init();
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }

    public static LangBuilder lang() {
        return new LangBuilder(MODID);
    }
}

package io.github.irishgreencitrus.occultengineering;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import io.github.irishgreencitrus.occultengineering.compat.curios.OcEngCurios;
import io.github.irishgreencitrus.occultengineering.config.OccultEngineeringConfig;
import io.github.irishgreencitrus.occultengineering.registry.*;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;

@Mod(OccultEngineering.MODID)
public class OccultEngineering {
    public static final String NAME = "Create: Occult Engineering";
    public static final String MODID = "occultengineering";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final CreateRegistrate REGISTRATE;

    static {
        assert OccultEngineeringCreativeModeTab.CREATIVE_TAB.getKey() != null;
        REGISTRATE = CreateRegistrate.create(MODID)
                .defaultCreativeTab(OccultEngineeringCreativeModeTab.CREATIVE_TAB.getKey())
                .setTooltipModifierFactory(item ->
                        new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                                .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
                );
    }

    public OccultEngineering() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get()
                .getModEventBus();
        var modLoadingContext = ModLoadingContext.get();
        modEventBus.addListener(OccultEngineering::init);
        modEventBus.addListener(OccultEngineering::onRegister);


        REGISTRATE.registerEventListeners(modEventBus);

        OccultEngineeringCreativeModeTab.register(modEventBus);

        OccultEngineeringItems.register();
        OccultEngineeringFluids.register();
        OccultEngineeringBlocks.register();
        OccultEngineeringBlockEntities.register();
        OccultEngineeringMenuTypes.register();
        OccultEngineeringPackets.registerPackets();

        OccultEngineeringConfig.register(modLoadingContext);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> OccultEngineeringClient.onCtorClient(modEventBus));
        OcEngCurios.init(modEventBus);
        LOGGER.info("Setup is complete.");
    }

    public static void init(final FMLCommonSetupEvent event) {
        OccultEngineeringFluids.registerFluidInteractions();
        OccultEngineeringSchematic.register();
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

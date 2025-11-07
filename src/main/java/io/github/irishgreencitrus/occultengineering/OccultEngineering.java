package io.github.irishgreencitrus.occultengineering;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import io.github.irishgreencitrus.occultengineering.compat.ModIntegration;
import io.github.irishgreencitrus.occultengineering.compat.Mods;
import io.github.irishgreencitrus.occultengineering.compat.curios.OcEngCurios;
import io.github.irishgreencitrus.occultengineering.compat.enchant_industry.OcEngEnchantIndustry;
import io.github.irishgreencitrus.occultengineering.config.OccultEngineeringConfig;
import io.github.irishgreencitrus.occultengineering.content.block.phlogiport.PhlogiportNetworkHandler;
import io.github.irishgreencitrus.occultengineering.datagen.DataProviders;
import io.github.irishgreencitrus.occultengineering.registry.*;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;

@Mod(OccultEngineering.MODID)
public class OccultEngineering {
    public static final String NAME = "Create: Occult Engineering";
    public static final String MODID = "occultengineering";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID)
            .defaultCreativeTab((ResourceKey<CreativeModeTab>) null)
            .setTooltipModifierFactory(item ->
                    new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                            .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
            );

    public static final PhlogiportNetworkHandler PHLOGIPORT_NETWORK = new PhlogiportNetworkHandler();

    public OccultEngineering(IEventBus modEventBus, ModContainer modContainer) {
        var modLoadingContext = ModLoadingContext.get();

        REGISTRATE.registerEventListeners(modEventBus);

        DataProviders.registerRegistrateProviders();

        OccultEngineeringCreativeModeTab.register(modEventBus);

        OccultEngineeringItems.register();
        OccultEngineeringFluids.register();
        OccultEngineeringBlocks.register();
        OccultEngineeringDisplaySources.register();
        OccultEngineeringBlockEntities.register();
        OccultEngineeringMenuTypes.register();
        OccultEngineeringEntities.register(modEventBus);
        OccultEngineeringBrains.register(modEventBus);
        OccultEngineeringParticleTypes.register(modEventBus);
        OccultEngineeringPackets.register();
        OccultEngineeringRecipeTypes.register(modEventBus);

        OccultEngineeringConfig.register(modLoadingContext, modContainer);

        modEventBus.addListener(OccultEngineering::init);
        modEventBus.addListener(OccultEngineering::onRegister);
        modEventBus.addListener(OccultEngineeringEntities::registerEntityAttributes);
        OcEngCurios.init(modEventBus);

        loadModIntegrations();

        Mods.LOADED_INTEGRATIONS.forEach(it -> it.onCommonSetup(modEventBus));
        LOGGER.info("Setup is complete.");
    }

    public static void loadModIntegrations() {
        Mods.loadIntegration(Mods.ENCHANTMENT_INDUSTRY, OcEngEnchantIndustry::new);
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
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static LangBuilder lang() {
        return new LangBuilder(MODID);
    }
}

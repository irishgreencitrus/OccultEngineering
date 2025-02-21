package io.github.irishgreencitrus.occultengineering;

import com.mojang.logging.LogUtils;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingTypeRegistry;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import com.simibubi.create.foundation.data.CreateRegistrate;
import io.github.irishgreencitrus.occultengineering.kinetics.fan.processing.FanEnspiritType;
import io.github.irishgreencitrus.occultengineering.kinetics.mechanicalArm.DimensionalStorageActuatorInteractionPoint;
import io.github.irishgreencitrus.occultengineering.kinetics.mechanicalArm.SacrificialBowlInteractionPoint;
import io.github.irishgreencitrus.occultengineering.kinetics.mechanicalArm.StableWormholeInteractionPoint;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringFluids;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(OccultEngineering.MODID)
public class OccultEngineering {
    public static final String MODID = "occultengineering";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);
    public OccultEngineering(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        REGISTRATE.registerEventListeners(modEventBus);
        FanProcessingTypeRegistry.register(ResourceLocation.fromNamespaceAndPath(MODID,"enspirit"), new FanEnspiritType());
        ArmInteractionPointType.register(new SacrificialBowlInteractionPoint(ResourceLocation.fromNamespaceAndPath(MODID, "sacrificial_bowl_interaction_point")));
        ArmInteractionPointType.register(new StableWormholeInteractionPoint(ResourceLocation.fromNamespaceAndPath(MODID, "stable_wormhole_interaction_point")));
        ArmInteractionPointType.register(new DimensionalStorageActuatorInteractionPoint(ResourceLocation.fromNamespaceAndPath(MODID, "dimensional_storage_actuator_interaction_point")));

        OccultEngineeringFluids.register();
    }
}

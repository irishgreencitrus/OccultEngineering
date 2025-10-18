package io.github.irishgreencitrus.occultengineering.registry;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecs;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class OccultEngineeringDataComponents {
    private static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, OccultEngineering.MODID);

    public static final DataComponentType<ResourceLocation> PENTSCHEM_RESOURCE_LOCATION = register(
            "pentacle_schematic_resource_location",
            b -> b.persistent(ResourceLocation.CODEC).networkSynchronized(ResourceLocation.STREAM_CODEC)
    );

    public static final DataComponentType<Vec3i> PENTSCHEM_BOUNDS = register(
            "pentacle_schematic_bounds",
            b -> b.persistent(Vec3i.CODEC).networkSynchronized(CatnipStreamCodecs.VEC3I)
    );

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        var type = builder.apply(DataComponentType.builder()).build();
        DATA_COMPONENTS.register(name, () -> type);
        return type;
    }

    public static void register(IEventBus modEventBus) {
        DATA_COMPONENTS.register(modEventBus);
    }
}

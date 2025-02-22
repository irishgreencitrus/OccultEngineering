package io.github.irishgreencitrus.occultengineering.registry;

import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.utility.Components;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class OccultEngineeringCreativeModeTab {
    private static final DeferredRegister<CreativeModeTab> REGISTER;
    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB;

    public OccultEngineeringCreativeModeTab() {
    }

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }


    static {
        REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, OccultEngineering.MODID);
        CREATIVE_TAB = REGISTER.register("base",
                () -> CreativeModeTab
                        .builder()
                        .title(Components.literal("Create: Occult Engineering"))
                        .withTabsBefore(
                                AllCreativeModeTabs.BASE_CREATIVE_TAB.getId(),
                                AllCreativeModeTabs.PALETTES_CREATIVE_TAB.getId())
                        .icon(
                                () -> new ItemStack(OccultEngineeringFluids.SPIRIT_SOLUTION.getBucket().get())
                        )
                        .displayItems((itemDisplayParameters, output) -> {
                            output.accept(OccultEngineeringFluids.SPIRIT_SOLUTION.get().getBucket());
                        })
                        .build());
    }
}

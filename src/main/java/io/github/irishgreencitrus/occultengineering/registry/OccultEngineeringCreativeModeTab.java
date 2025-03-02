package io.github.irishgreencitrus.occultengineering.registry;

import com.simibubi.create.AllCreativeModeTabs;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

public class OccultEngineeringCreativeModeTab {
    private static final DeferredRegister<CreativeModeTab> REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, OccultEngineering.MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = REGISTER.register("main",
            () -> CreativeModeTab
                    .builder()
                    .title(Component.literal("Create: Occult Engineering"))
                    .withTabsBefore(
                            AllCreativeModeTabs.BASE_CREATIVE_TAB.getId(),
                            AllCreativeModeTabs.PALETTES_CREATIVE_TAB.getId())
                    .icon(
                            () -> new ItemStack(OccultEngineeringFluids.SPIRIT_SOLUTION.getBucket().get())
                    )
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(OccultEngineeringFluids.SPIRIT_SOLUTION.getBucket().get());
                        output.accept(OccultEngineeringBlocks.MECHANICAL_CHAMBER.get());
                        output.accept(OccultEngineeringItems.COPPER_CHALK_IMPURE.get());
                        output.accept(OccultEngineeringItems.ZINC_CHALK_IMPURE.get());
                        output.accept(OccultEngineeringItems.BRASS_CHALK_IMPURE.get());
                        output.accept(OccultEngineeringItems.COPPER_CHALK.get());
                        output.accept(OccultEngineeringItems.ZINC_CHALK.get());
                        output.accept(OccultEngineeringItems.BRASS_CHALK.get());
                    })
                    .build());

    @ApiStatus.Internal
    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}

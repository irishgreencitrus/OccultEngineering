package io.github.irishgreencitrus.occultengineering.compat.curios;

import com.simibubi.create.content.equipment.goggles.GogglesItem;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Map;
import java.util.Optional;

public class OcEngCurios {
    private static Optional<Map<String, ICurioStacksHandler>> resolveCuriosMap(LivingEntity entity) {
        return entity.getCapability(CuriosCapability.INVENTORY).map(ICuriosItemHandler::getCurios);
    }

    public static void init(IEventBus modEventBus) {
        modEventBus.addListener(OcEngCurios::onClientSetup);
        GogglesItem.addIsWearingPredicate(player -> resolveCuriosMap(player)
                .map(curiosMap -> {
                    for (ICurioStacksHandler stacksHandler : curiosMap.values()) {
                        // Search all the curio slots for Goggles existing
                        int slots = stacksHandler.getSlots();
                        for (int slot = 0; slot < slots; slot++) {
                            if (OccultEngineeringItems.COMBINED_GOGGLES.isIn(stacksHandler.getStacks().getStackInSlot(slot))) {
                                return true;
                            }
                        }
                    }
                    return false;
                })
                .orElse(false));

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> modEventBus.addListener(OcEngCuriosRenderers::onLayerRegister));
    }

    private static void onClientSetup(final FMLClientSetupEvent event) {
        OcEngCuriosRenderers.register();
    }
}

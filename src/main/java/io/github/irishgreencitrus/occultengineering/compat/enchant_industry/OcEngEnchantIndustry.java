package io.github.irishgreencitrus.occultengineering.compat.enchant_industry;

import io.github.irishgreencitrus.occultengineering.compat.ModIntegration;
import io.github.irishgreencitrus.occultengineering.compat.Mods;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.level.LevelEvent;
import plus.dragons.createenchantmentindustry.common.fluids.printer.behaviour.PrintingBehaviour;

public class OcEngEnchantIndustry extends ModIntegration {
    public OcEngEnchantIndustry() {
        super(Mods.ENCHANTMENT_INDUSTRY);
    }

    @Override
    public void onCommonSetup(IEventBus modEventBus) {
    }

    @Override
    public void onClientSetup() {

    }

    @Override
    public void onWorldLoad(LevelEvent.Load event) {
        PrintingBehaviour.register(BindingBookPrintingBehaviour::create);
    }

    @Override
    public void onWorldUnload(LevelEvent.Unload event) {

    }
}

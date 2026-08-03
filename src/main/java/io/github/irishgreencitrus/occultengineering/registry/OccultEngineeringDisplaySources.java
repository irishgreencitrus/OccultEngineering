package io.github.irishgreencitrus.occultengineering.registry;

import com.simibubi.create.api.behaviour.display.DisplaySource;
import com.tterrag.registrate.util.entry.RegistryEntry;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.content.block.mechanical_chamber.display.CurrentPentacleDisplaySource;
import io.github.irishgreencitrus.occultengineering.content.block.mechanical_chamber.display.RitualCompletionDisplaySource;
import io.github.irishgreencitrus.occultengineering.content.block.mechanical_chamber.display.RitualResultItemDisplaySource;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class OccultEngineeringDisplaySources {
    public static final RegistryEntry<CurrentPentacleDisplaySource> CURRENT_PENTACLE =
            simple("current_pentacle", CurrentPentacleDisplaySource::new);
    public static final RegistryEntry<RitualResultItemDisplaySource> CURRENT_RITUAL_RESULT =
            simple("current_ritual_result", RitualResultItemDisplaySource::new);
    public static final RegistryEntry<RitualCompletionDisplaySource> RITUAL_COMPLETION =
            simple("ritual_completion", RitualCompletionDisplaySource::new);

    private static <T extends DisplaySource> RegistryEntry<T> simple(String name, Supplier<T> supplier) {
        return OccultEngineering.REGISTRATE.displaySource(name, supplier).register();
    }

    public static void register() {
    }
}

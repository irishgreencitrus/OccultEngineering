package io.github.irishgreencitrus.occultengineering.registry;

import com.tterrag.registrate.util.entry.MenuEntry;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.content.block.pentacle_altar.PentacleAltarMenu;
import io.github.irishgreencitrus.occultengineering.content.block.pentacle_altar.PentacleAltarScreen;

public class OccultEngineeringMenuTypes {
    public static final MenuEntry<PentacleAltarMenu> PENTACLE_ALTAR = OccultEngineering.REGISTRATE.menu("pentacle_altar", PentacleAltarMenu::new, () -> PentacleAltarScreen::new).register();

    public static void register() {
    }
}

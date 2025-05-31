package io.github.irishgreencitrus.occultengineering.registry;

import com.tterrag.registrate.util.entry.MenuEntry;
import io.github.irishgreencitrus.occultengineering.content.block.pentacle_altar.PentacleAltarMenu;
import io.github.irishgreencitrus.occultengineering.content.block.pentacle_altar.PentacleAltarScreen;
import io.github.irishgreencitrus.occultengineering.content.block.pucalith.PucalithMenu;
import io.github.irishgreencitrus.occultengineering.content.block.pucalith.PucalithScreen;

import static io.github.irishgreencitrus.occultengineering.OccultEngineering.REGISTRATE;

public class OccultEngineeringMenuTypes {
    public static final MenuEntry<PentacleAltarMenu> PENTACLE_ALTAR = REGISTRATE.menu("pentacle_altar", PentacleAltarMenu::new, () -> PentacleAltarScreen::new).register();
    public static final MenuEntry<PucalithMenu> PUCALITH = REGISTRATE.menu("pucalith", PucalithMenu::new, () -> PucalithScreen::new).register();

    public static void register() {
    }
}

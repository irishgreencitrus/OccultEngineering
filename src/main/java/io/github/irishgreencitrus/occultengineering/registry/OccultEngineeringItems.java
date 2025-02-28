package io.github.irishgreencitrus.occultengineering.registry;

import com.klikli_dev.occultism.common.item.tool.ChalkItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

import static io.github.irishgreencitrus.occultengineering.OccultEngineering.REGISTRATE;

public class OccultEngineeringItems {
    public static final ItemEntry<Item> COPPER_CHALK_IMPURE = REGISTRATE.item("chalk_copper_impure", Item::new)
            .lang("Impure Copper Chalk")
            .register();
    public static final ItemEntry<ChalkItem> COPPER_CHALK = REGISTRATE.item("chalk_copper",
                    p -> new ChalkItem(new Item.Properties().setNoRepair().durability(128), OccultEngineeringBlocks.COPPER_CHALK))
            .lang("Copper Chalk")
            .register();

    // TODO: add zinc and brass chalk
}

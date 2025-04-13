package io.github.irishgreencitrus.occultengineering.registry;

import com.klikli_dev.occultism.common.item.DummyTooltipItem;
import com.klikli_dev.occultism.common.item.spirit.BookOfBindingItem;
import com.klikli_dev.occultism.common.item.tool.ChalkItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import io.github.irishgreencitrus.occultengineering.item.BookOfBindingBoundGlintItem;
import io.github.irishgreencitrus.occultengineering.item.MechanicalGuideItem;
import net.minecraft.world.item.Item;

import static io.github.irishgreencitrus.occultengineering.OccultEngineering.REGISTRATE;

@SuppressWarnings("unused")
public class OccultEngineeringItems {
    public static final ItemEntry<MechanicalGuideItem> ENCYCLOPEDIA_OF_SOULS = REGISTRATE.item("encyclopedia_of_souls", MechanicalGuideItem::new)
            .lang("Encyclopedia of Souls")
            .register();

    public static final ItemEntry<Item> COPPER_CHALK_IMPURE = REGISTRATE.item("chalk_copper_impure", Item::new)
            .lang("Impure Copper Chalk")
            .register();
    public static final ItemEntry<Item> ZINC_CHALK_IMPURE = REGISTRATE.item("chalk_zinc_impure", Item::new)
            .lang("Impure Zinc Chalk")
            .register();

    public static final ItemEntry<Item> BRASS_CHALK_IMPURE = REGISTRATE.item("chalk_brass_impure", Item::new)
            .lang("Impure Brass Chalk")
            .register();

    public static final ItemEntry<ChalkItem> COPPER_CHALK = REGISTRATE.item("chalk_copper",
                    p -> new ChalkItem(new Item.Properties().setNoRepair().durability(128), OccultEngineeringBlocks.COPPER_CHALK))
            .lang("Copper Chalk")
            .register();

    public static final ItemEntry<ChalkItem> ZINC_CHALK = REGISTRATE.item("chalk_zinc",
                    p -> new ChalkItem(new Item.Properties().setNoRepair().durability(128), OccultEngineeringBlocks.ZINC_CHALK))
            .lang("Zinc Chalk")
            .register();

    public static final ItemEntry<ChalkItem> BRASS_CHALK = REGISTRATE.item("chalk_brass",
                    p -> new ChalkItem(new Item.Properties().setNoRepair().durability(128), OccultEngineeringBlocks.BRASS_CHALK))
            .lang("Brass Chalk")
            .register();

    public static final ItemEntry<BookOfBindingItem> BOOK_OF_BINDING_PUCA = REGISTRATE.item("book_of_binding_puca", BookOfBindingItem::new)
            .lang("Book of Binding: Púca")
            .register();

    public static final ItemEntry<BookOfBindingBoundGlintItem> BOOK_OF_BINDING_BOUND_PUCA = REGISTRATE.item("book_of_binding_bound_puca", BookOfBindingBoundGlintItem::new)
            .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("item/book_of_binding_puca")))
            .lang("Book of Binding: Púca (Bound)")
            .register();

    public static final ItemEntry<Item> ZINC_DUST = REGISTRATE
            .item("zinc_dust", Item::new)
            .lang("Zinc Dust")
            .register();

    public static final ItemEntry<Item> BRASS_DUST = REGISTRATE
            .item("brass_dust", Item::new)
            .lang("Brass Dust")
            .register();

    public static final ItemEntry<Item> STERLING_SILVER_INGOT = REGISTRATE
            .item("sterling_silver_ingot", Item::new)
            .lang("Sterling Silver Ingot")
            .register();

    public static final ItemEntry<Item> STERLING_SILVER_NUGGET = REGISTRATE
            .item("sterling_silver_nugget", Item::new)
            .lang("Sterling Silver Nugget")
            .register();


    // TODO: change texture of impure chalks

    // TODO: move crafting recipes to datagen

    static {
        REGISTRATE.item("ritual_dummy_craft_otherworld_detector", DummyTooltipItem::new)
                .model((c, p) -> p.singleTexture(c.getName(), p.mcLoc("item/generated"), "layer0", p.modLoc("item/ritual_dummy")))
                .lang("Ritual: Craft Otherworld Detector")
                .register();
    }

    public static void register() {
    }
}

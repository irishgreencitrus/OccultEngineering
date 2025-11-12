package io.github.irishgreencitrus.occultengineering.registry;

import com.klikli_dev.occultism.common.item.DummyTooltipItem;
import com.klikli_dev.occultism.common.item.spirit.BookOfBindingItem;
import com.klikli_dev.occultism.common.item.tool.ChalkItem;
import com.simibubi.create.content.equipment.goggles.GogglesModel;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import io.github.irishgreencitrus.occultengineering.content.item.BookOfBindingBoundGlintItem;
import io.github.irishgreencitrus.occultengineering.content.item.CombinedGogglesItem;
import io.github.irishgreencitrus.occultengineering.content.item.MechanicalGuideItem;
import io.github.irishgreencitrus.occultengineering.content.item.PentacleSchematicItem;
import io.github.irishgreencitrus.occultengineering.content.item.model.CombinedGogglesModel;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;

import static io.github.irishgreencitrus.occultengineering.OccultEngineering.REGISTRATE;

@SuppressWarnings("unused")
public class OccultEngineeringItems {
    static {
        REGISTRATE.setCreativeTab(OccultEngineeringCreativeModeTab.CREATIVE_TAB);
    }

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
                    p -> new ChalkItem(new Item.Properties().setNoRepair().durability(128), OccultEngineeringBlocks.COPPER_CHALK.get()))
            .lang("Copper Chalk")
            .register();

    public static final ItemEntry<ChalkItem> ZINC_CHALK = REGISTRATE.item("chalk_zinc",
                    p -> new ChalkItem(new Item.Properties().setNoRepair().durability(128), OccultEngineeringBlocks.ZINC_CHALK.get()))
            .lang("Zinc Chalk")
            .register();

    public static final ItemEntry<ChalkItem> BRASS_CHALK = REGISTRATE.item("chalk_brass",
                    p -> new ChalkItem(new Item.Properties().setNoRepair().durability(128), OccultEngineeringBlocks.BRASS_CHALK.get()))
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

    public static final ItemEntry<CombinedGogglesItem> COMBINED_GOGGLES = REGISTRATE
            .item("combined_goggles", CombinedGogglesItem::new)
            .properties(p -> p.stacksTo(1))
            .onRegister(CreateRegistrate.itemModel(() -> CombinedGogglesModel::new))
            .lang("Otherworldly Engineer's Goggles")
            .register();

    public static final ItemEntry<Item> EMPTY_PENTACLE_SCHEMATIC = REGISTRATE
            .item("empty_pentacle_schematic", Item::new)
            .properties(p -> p.stacksTo(1))
            .lang("Empty Pentacle Schematic")
            .register();

    public static final ItemEntry<PentacleSchematicItem> PENTACLE_SCHEMATIC = REGISTRATE
            .item("pentacle_schematic", PentacleSchematicItem::new)
            .properties(p -> p.stacksTo(1))
            .lang("Pentacle Schematic")
            .register();

    public static final ItemEntry<DeferredSpawnEggItem> SPAWN_EGG_PUCA = REGISTRATE
            .item("puca_spawn_egg", p -> new DeferredSpawnEggItem(OccultEngineeringEntities.PUCA_TYPE, 0x242424, 0xd69c13, p))
            .model((c, p) -> p.withExistingParent(c.getName(), p.mcLoc("item/template_spawn_egg")))
            .lang("Púca Spawn Egg")
            .register();

    public static final ItemEntry<Item> PHLOGISTON = REGISTRATE
            .item("phlogiston", Item::new)
            .lang("Phlogiston")
            .register();

    public static final ItemEntry<Item> SILVER_PHLOGISTATE = REGISTRATE
            .item("silver_phlogistate", Item::new)
            .lang("Silver Phlogistate")
            .register();

    // TODO: change texture of impure chalks

    static {
        REGISTRATE.item("ritual_dummy_craft_otherworld_detector", DummyTooltipItem::new)
                .model(OccultEngineeringItems::ritualDummyModel)
                .lang("Ritual: Craft Otherworld Detector")
                .register();

        REGISTRATE.item("ritual_dummy_craft_mechanical_chamber", DummyTooltipItem::new)
                .model(OccultEngineeringItems::ritualDummyModel)
                .lang("Ritual: Craft Mechanical Chamber")
                .register();

        REGISTRATE.item("ritual_dummy_craft_phlogiport", DummyTooltipItem::new)
                .model(OccultEngineeringItems::ritualDummyModel)
                .lang("Ritual: Craft Phlogiport")
                .register();
    }

    public static void register() {
    }

    private static void ritualDummyModel(DataGenContext<Item, DummyTooltipItem> c, RegistrateItemModelProvider p) {
        p.singleTexture(c.getName(), p.mcLoc("item/generated"), "layer0", p.modLoc("item/ritual_dummy"));
    }
}

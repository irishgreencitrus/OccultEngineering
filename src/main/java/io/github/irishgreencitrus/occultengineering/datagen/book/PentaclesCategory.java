package io.github.irishgreencitrus.occultengineering.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.klikli_dev.modonomicon.api.datagen.CategoryEntryMap;
import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryParentModel;
import com.klikli_dev.modonomicon.api.datagen.book.condition.BookTrueConditionModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookMultiblockPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.occultism.registry.OccultismItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class PentaclesCategory extends CategoryProvider {
    public static final String CATEGORY_ID = "pentacles";

    public PentaclesCategory(BookProvider parent) {
        super(parent, CATEGORY_ID);
    }

    @Override
    protected String[] generateEntryMap() {
        return new String[]{
                "____O_p__"
        };
    }

    @Override
    protected void generateEntries() {
        var overview = add(makeOverviewEntry(entryMap, 'O'));
        var craftPuca = add(makeCraftPucaEntry(entryMap, 'p'));
        craftPuca.withParent(BookEntryParentModel.create(overview.getId()));
        craftPuca.withCondition(BookTrueConditionModel.builder().build());

    }

    @Override
    protected BookCategoryModel generateCategory() {
        add(context().categoryName(), "Pentacles");
        return BookCategoryModel.create(modLoc(context().categoryId()), context().categoryName())
                .withIcon(OccultismItems.PENTACLE.get())
                .withShowCategoryButton(true);
    }

    private BookEntryModel makeOverviewEntry(CategoryEntryMap entryMap, char icon) {
        var entryId = "overview";
        context().entry(entryId);
        lang().add(context().entryName(), "Overview");

        context().page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle("Pentacles")
                .withText("""
                        This section contains the list of Pentacles added by Occult Engineering.
                        \\
                        Each Pentacle in this section requires the use of the Mechanical Chamber, and will not work
                        with the Golden Sacrificial Bowl.
                        \\
                        Other than that, the Pentacles work the same way as in Occultism.
                        """)
                .build();

        return BookEntryModel.create(fullyQualifiedEntryId(), context().entryName())
                .withIcon(Items.BOOK)
                .withLocation(entryMap.get(icon))
                .withEntryBackground(0, 1)
                .withPages(intro);
    }

    private BookEntryModel makeCraftPucaEntry(CategoryEntryMap entryMap, char icon) {
        var entryId = "craft_puca";
        context().entry(entryId);
        context().page("intro");
        lang().add(context().entryName(), "Fionntán's Uncompromising Captivation");
        lang().add(context().entryDescription(), "The first of the mechanical rituals.");
        var intro = BookTextPageModel.builder()
                .withTitle(context().pageTitle())
                .withText(context().pageText())
                .build();
        lang().add(context().pageTitle(), "Fionntán's Uncompromising Captivation");
        lang().add(context().pageText(), "**Purpose:** Bind Púca\n\\\n\\\nFionntán's Uncompromising Captivation is a pentacle for binding Púca into blocks and items which can be used for simple automation. It is suitable to permanently infuse machinery.");

        context().page("multiblock");
        var multiblock = BookMultiblockPageModel.builder()
                .withMultiblockId(modLoc(entryId))
                .build();

        context().page("uses");
        var uses = BookTextPageModel.builder()
                .withTitle(context().pageTitle())
                .withText(context().pageText())
                .build();
        lang().add(context().pageTitle(), "Uses");
        lang().add(context().pageText(), "Craft Otherworld Detector");
        return BookEntryModel.create(fullyQualifiedEntryId(), context().entryName())
                .withDescription(context().entryDescription())
                .withIcon(OccultismItems.PENTACLE.get())
                .withLocation(entryMap.get(icon))
                .withPages(
                        intro,
                        multiblock,
                        uses
                );
    }

    private ResourceLocation fullyQualifiedEntryId() {
        return modLoc(context().categoryId() + "/" + context().entryId());
    }
}

package io.github.irishgreencitrus.occultengineering.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.klikli_dev.modonomicon.api.datagen.CategoryEntryMap;
import com.klikli_dev.modonomicon.api.datagen.ModonomiconLanguageProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookMultiblockPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.occultism.registry.OccultismItems;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

import static io.github.irishgreencitrus.occultengineering.item.MechanicalGuideItem.ENCYCLOPEDIA_OF_SOULS;

public class OcEngBookProvider extends BookProvider {

    public OcEngBookProvider(PackOutput packOutput, ModonomiconLanguageProvider defaultLang) {
        super(ENCYCLOPEDIA_OF_SOULS.getPath(), packOutput, OccultEngineering.MODID, defaultLang);
    }

    @Override
    protected void registerDefaultMacros() {

    }

    @Override
    protected BookModel generateBook() {
        context().book(bookId);
        lang().add(context().bookName(), "Encyclopedia of Souls");
        lang().add(context().bookTooltip(), "A guide to all Occult Engineering");
        int sortNum = 1;

        var pentaclesCategory = makePentaclesCategory().withSortNumber(sortNum++);


        return BookModel.create(ENCYCLOPEDIA_OF_SOULS, context().bookName())
                .withModel(modLoc("encyclopedia_of_souls_item"))
                .withTooltip(context().bookTooltip())
                .withCategories(
                        pentaclesCategory
                )
                .withGenerateBookItem(false)
                .withCustomBookItem(ENCYCLOPEDIA_OF_SOULS)
                .withAutoAddReadConditions(true)
                .withAllowOpenBooksWithInvalidLinks(true);
    }

    private @NotNull BookCategoryModel makePentaclesCategory() {
        context().category("pentacles");
        lang().add(context().categoryName(), "Pentacles");

        var entryMap = new CategoryEntryMap();
        entryMap.setMap(
                "__p_________________"
        );

        var craftPuca = makeCraftPucaEntry(entryMap, 'p');
        return BookCategoryModel.create(modLoc(context().categoryId()), context().categoryName())
                .withIcon(OccultismItems.PENTACLE.get())
                .withEntries(craftPuca);
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
        return BookEntryModel.create(modLoc(context().categoryId() + "/" + context().entryId()), context().entryName())
                .withIcon(OccultismItems.PENTACLE.get())
                .withLocation(entryMap.get(icon))
                .withPages(
                        intro,
                        multiblock,
                        uses
                );

    }
}

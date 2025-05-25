package io.github.irishgreencitrus.occultengineering.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.klikli_dev.modonomicon.api.datagen.ModonomiconLanguageProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookModel;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.minecraft.data.PackOutput;

import static io.github.irishgreencitrus.occultengineering.content.item.MechanicalGuideItem.ENCYCLOPEDIA_OF_SOULS;

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

        var gettingStartedCategory = new GettingStartedCategory(this).generate().withSortNumber(sortNum++);
        var pentaclesCategory = new PentaclesCategory(this).generate().withSortNumber(sortNum);

        return BookModel.create(ENCYCLOPEDIA_OF_SOULS, context().bookName())
                .withModel(modLoc("encyclopedia_of_souls_item"))
                .withTooltip(context().bookTooltip())
                .withCategories(
                        gettingStartedCategory,
                        pentaclesCategory
                )
                .withGenerateBookItem(false)
                .withCustomBookItem(ENCYCLOPEDIA_OF_SOULS)
                .withAutoAddReadConditions(true)
                .withAllowOpenBooksWithInvalidLinks(true);
    }
}

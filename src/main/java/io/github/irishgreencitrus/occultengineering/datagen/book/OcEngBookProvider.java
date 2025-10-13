package io.github.irishgreencitrus.occultengineering.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.klikli_dev.modonomicon.api.datagen.ModonomiconLanguageProvider;
import com.klikli_dev.modonomicon.api.datagen.SingleBookSubProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookModel;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.minecraft.data.PackOutput;

import java.util.List;

import static io.github.irishgreencitrus.occultengineering.content.item.MechanicalGuideItem.ENCYCLOPEDIA_OF_SOULS;

public class OcEngBookProvider extends SingleBookSubProvider {

    public OcEngBookProvider(ModonomiconLanguageProvider defaultLang) {
        super(ENCYCLOPEDIA_OF_SOULS.getPath(), OccultEngineering.MODID, defaultLang);
    }

    @Override
    protected void registerDefaultMacros() {

    }

    @Override
    protected void generateCategories() {
        var sortNum = 0;
        var categories = List.of(
                new GettingStartedCategory(this).generate().withSortNumber(sortNum++),
                new PentaclesCategory(this).generate().withSortNumber(sortNum)
        );
        for (var category : categories) {
            this.add(category);
        }
    }

    @Override
    protected String bookName() {
        return "Encyclopedia of Souls";
    }

    @Override
    protected String bookTooltip() {
        return "A guide to all Occult Engineering";
    }
}

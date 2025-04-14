package io.github.irishgreencitrus.occultengineering.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.klikli_dev.modonomicon.api.datagen.CategoryEntryMap;
import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryParentModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.occultism.registry.OccultismItems;
import com.simibubi.create.AllBlocks;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringBlocks;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringFluids;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

public class GettingStartedCategory extends CategoryProvider {
    public static final String CATEGORY_ID = "getting_started";

    public GettingStartedCategory(BookProvider parent) {
        super(parent, CATEGORY_ID);
    }

    @Override
    protected String[] generateEntryMap() {
        /*
        i -> intro to mod
        b -> mod basics
        S -> Spirit Solution
        ć -> new chalks
        g -> Sterling silver
        P -> pulverizer
        M -> mechanical chamber
        D -> otherworld detector
        ú -> púca book
        e -> pentacles
        */
        return new String[]{
                "__________________________________",
                "__________________________________",
                "_____i__b_________________________",
                "__________________________________",
                "_____S_ć_____e____________________",
                "________M____ú____________________",
                "_____g__P_________________________",
                "________D_________________________",
                "__________________________________",

        };
    }

    @Override
    protected void generateEntries() {
        var intro = add(makeIntroEntry(entryMap, 'i'));
        var basics = add(makeBasicsEntry(entryMap, 'b'));
        basics.withParent(BookEntryParentModel.create(intro.getId()));

        var spiritSolution = add(makeSpiritSolutionEntry(entryMap, 'S'));
        spiritSolution.withParent(BookEntryParentModel.create(intro.getId()));

        var chalks = add(makeChalksEntry(entryMap, 'ć'));
        chalks.withParent(BookEntryParentModel.create(spiritSolution.getId()));

        var sterlingSilver = add(makeSterlingSilverEntry(entryMap, 'g'));
        sterlingSilver.withParent(BookEntryParentModel.create(spiritSolution.getId()));

        var chamber = add(makeChamberEntry(entryMap, 'M'));
        chamber.withParent(BookEntryParentModel.create(sterlingSilver.getId()));

        var pulverizer = add(makePulverizerEntry(entryMap, 'P'));
        pulverizer.withParent(BookEntryParentModel.create(sterlingSilver.getId()));

        var detector = add(makeDetectorEntry(entryMap, 'D'));
        detector.withParent(BookEntryParentModel.create(sterlingSilver.getId()));

        var pucaBook = add(makePucaBookEntry(entryMap, 'ú'));
        pucaBook.withParent(BookEntryParentModel.create(chamber.getId()));

        var pentacles = add(makePentaclesLinkEntry(entryMap, 'e'));
        pentacles.withParent(BookEntryParentModel.create(chamber.getId()));
    }

    @Override
    protected BookCategoryModel generateCategory() {
        add(context().categoryName(), "Getting Started");
        return BookCategoryModel.create(modLoc(context().categoryId()), context().categoryName())
                .withIcon(OccultEngineeringItems.ENCYCLOPEDIA_OF_SOULS)
                .withShowCategoryButton(true);
    }


    private BookEntryModel makeIntroEntry(CategoryEntryMap entryMap, char icon) {
        var entryId = "intro";
        context().entry(entryId);
        lang().add(context().entryName(), "About");
        lang().add(context().entryDescription(), "About using the Encyclopedia");

        context().page("intro");
        var intro = BookTextPageModel.builder()
                .withTitle(context().pageTitle())
                .withText(context().pageText())
                .build();

        lang().add(context().pageTitle(), "About");
        lang().add(context().pageText(), """
                This book aims to introduce the new functionality of Occult Engineering,
                and aims to complement the Ponder system of the Kinetic Blocks, adding info about the other features.
                """);

        context().page("help");
        var help = BookTextPageModel.builder()
                .withTitle(context().pageTitle())
                .withText(context().pageText())
                .build();

        lang().add(context().pageTitle(), "Getting Help");
        lang().add(context().pageText(), """
                If you run into any trouble with Occult Engineering, feel free to join the Discord to get help.
                \\
                \\
                [Join at https://discord.gg/B7Sd3eaTrs](https://discord.gg/B7Sd3eaTrs)
                """);

        return BookEntryModel.create(fullyQualifiedEntryId(), context().entryName())
                .withDescription(context().entryDescription())
                .withIcon(OccultEngineeringItems.ENCYCLOPEDIA_OF_SOULS)
                .withLocation(entryMap.get(icon))
                .withEntryBackground(0, 1)
                .withPages(intro, help);
    }

    private BookEntryModel makeBasicsEntry(CategoryEntryMap entryMap, char icon) {
        var entryId = "basics";
        context().entry(entryId);

        lang().add(context().entryName(), "Basics");
        lang().add(context().entryDescription(), "Simple interactions between Create & Occultism");

        context().page("new_recipes");
        var newRecipes = BookTextPageModel.builder()
                .withTitle(context().pageTitle())
                .withText(context().pageText())
                .build();
        lang().add(context().pageTitle(), "New Recipes");
        lang().add(context().pageText(), """
                Occult Engineering contains a bunch of new recipes to assist the creation of occult items.
                \\
                \\
                For one, the mixer can now be used to mix up all the books of binding from Occultism.
                """);

        context().page("new_recipes2");
        var newRecipes2 = BookTextPageModel.builder()
                .withText(context().pageText())
                .build();
        lang().add(context().pageText(), """
                Don't have enough silver? Raw Gold can be haunted into Raw Silver.
                \\
                \\
                Check JEI for the specific recipes.
                """);

        context().page("new_fan_catalyst");
        var newFanCatalyst = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(AllBlocks.ENCASED_FAN))
                .withTitle(context().pageTitle())
                .withText(context().pageText())
                .build();

        lang().add(context().pageTitle(), "Encased Fan & Spiritfire");
        lang().add(context().pageText(), """
                Spiritfire and the Spirit Campfire can now be used in front of an encased fan to 'Bulk Enspirit'
                items. This can be used to automate all of the Spiritfire's recipes.
                """);

        context().page("new_arm_interaction");
        var newArmInteraction = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(AllBlocks.MECHANICAL_ARM))
                .withTitle(context().pageTitle())
                .withText(context().pageText())
                .build();

        lang().add(context().pageTitle(), "The Mechanical Arm");
        lang().add(context().pageText(), """
                The Mechanical Arm has new targets!
                \\
                \\
                It can directly interact with the Sacrificial Bowl, the Stable Wormhole and the Dimensional Storage
                Actuator.
                """);

        return BookEntryModel.create(fullyQualifiedEntryId(), context().entryName())
                .withIcon(AllBlocks.COGWHEEL)
                .withLocation(entryMap.get(icon))
                .withEntryBackground(0, 2)
                .withPages(
                        newRecipes,
                        newRecipes2,
                        newFanCatalyst,
                        newArmInteraction
                );
    }

    private BookEntryModel makeSpiritSolutionEntry(CategoryEntryMap entryMap, char icon) {
        var entryId = "spirit_solution";
        context().entry(entryId);

        lang().add(context().entryName(), "Spirit Solution");
        lang().add(context().entryDescription(), "The essential elixir");

        return BookEntryModel.create(fullyQualifiedEntryId(), context().entryName())
                .withIcon(OccultEngineeringFluids.SPIRIT_SOLUTION.getBucket().get())
                .withLocation(entryMap.get(icon))
                .withPages();
    }

    private BookEntryModel makeChalksEntry(CategoryEntryMap entryMap, char icon) {
        var entryId = "chalks";
        context().entry(entryId);

        lang().add(context().entryName(), "New Chalks");
        lang().add(context().entryDescription(), "Fancier symbols to decorate the ground");

        return BookEntryModel.create(fullyQualifiedEntryId(), context().entryName())
                .withIcon(OccultEngineeringItems.COPPER_CHALK)
                .withLocation(entryMap.get(icon))
                .withPages();
    }

    private BookEntryModel makeSterlingSilverEntry(CategoryEntryMap entryMap, char icon) {
        var entryId = "sterling_silver";
        context().entry(entryId);

        lang().add(context().entryName(), "Sterling Silver");
        lang().add(context().entryDescription(), "A new versatile material");

        return BookEntryModel.create(fullyQualifiedEntryId(), context().entryName())
                .withIcon(OccultEngineeringItems.STERLING_SILVER_INGOT)
                .withLocation(entryMap.get(icon))
                .withPages();
    }

    private BookEntryModel makeChamberEntry(CategoryEntryMap entryMap, char icon) {
        var entryId = "mechanical_chamber";
        context().entry(entryId);

        lang().add(context().entryName(), "Mechanical Chamber");
        lang().add(context().entryDescription(), "The height of ritual automation");

        return BookEntryModel.create(fullyQualifiedEntryId(), context().entryName())
                .withIcon(OccultEngineeringBlocks.MECHANICAL_CHAMBER)
                .withLocation(entryMap.get(icon))
                .withPages();
    }

    private BookEntryModel makePulverizerEntry(CategoryEntryMap entryMap, char icon) {
        var entryId = "mechanical_pulverizer";
        context().entry(entryId);

        lang().add(context().entryName(), "Mechanical Pulverizer");
        lang().add(context().entryDescription(), "Dust galore!");

        return BookEntryModel.create(fullyQualifiedEntryId(), context().entryName())
                .withIcon(OccultEngineeringBlocks.MECHANICAL_PULVERIZER)
                .withLocation(entryMap.get(icon))
                .withPages();
    }

    private BookEntryModel makeDetectorEntry(CategoryEntryMap entryMap, char icon) {
        var entryId = "otherworld_detector";
        context().entry(entryId);

        lang().add(context().entryName(), "Otherworld Detector");
        lang().add(context().entryDescription(), "It knows when you're awake");

        return BookEntryModel.create(fullyQualifiedEntryId(), context().entryName())
                .withIcon(OccultEngineeringBlocks.OTHERWORLD_DETECTOR)
                .withLocation(entryMap.get(icon))
                .withPages();
    }

    private BookEntryModel makePucaBookEntry(CategoryEntryMap entryMap, char icon) {
        var entryId = "puca_book";
        context().entry(entryId);

        lang().add(context().entryName(), "The Púca");
        lang().add(context().entryDescription(), "A new spirit fascinated with machinery");

        return BookEntryModel.create(fullyQualifiedEntryId(), context().entryName())
                .withIcon(OccultEngineeringItems.BOOK_OF_BINDING_PUCA)
                .withLocation(entryMap.get(icon))
                .withPages();
    }

    private BookEntryModel makePentaclesLinkEntry(CategoryEntryMap entryMap, char icon) {
        this.context().entry("pentacles_link");

        lang().add(context().entryName(), "Go to Pentacles");

        return BookEntryModel.create(fullyQualifiedEntryId(), context().entryName())
                .withIcon(OccultismItems.PENTACLE.get())
                .withCategoryToOpen(modLoc("pentacles"))
                .withLocation(entryMap.get(icon))
                .withEntryBackground(1, 2);
    }


    private ResourceLocation fullyQualifiedEntryId() {
        return modLoc(context().categoryId() + "/" + context().entryId());
    }
}

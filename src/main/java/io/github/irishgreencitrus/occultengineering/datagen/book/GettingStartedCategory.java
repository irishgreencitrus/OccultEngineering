package io.github.irishgreencitrus.occultengineering.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.klikli_dev.modonomicon.api.datagen.CategoryEntryMap;
import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryParentModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookSpotlightPageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.occultism.integration.modonomicon.pages.BookRitualRecipePageModel;
import com.klikli_dev.occultism.integration.modonomicon.pages.BookSpiritFireRecipePageModel;
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
                .withDescription(context().entryDescription())
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

        context().page("intro");
        var intro = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(OccultEngineeringFluids.SPIRIT_SOLUTION.getBucket().get()))
                .withTitle(context().pageTitle())
                .withText(context().pageText())
                .build();

        lang().add(context().pageTitle(), "Spirit Solution");
        lang().add(context().pageText(), """
                Spirit Solution is used for a number of things in the mod. Most notably, it can be poured onto books
                of binding to bind them ready for a ritual which can be used in place of  of crafting with a Dictionary of Spirits.
                \\
                It is also used as the only way to craft the new chalks.
                """);

        context().page("intro2");
        var intro2 = BookTextPageModel.builder()
                .withText(context().pageText())
                .build();

        lang().add(context().pageText(), """
                To get started with a small bit of Spirit Solution, crush some Demon's Dream Seeds.
                \\
                Use standard Create machinery to pump it around, or collect it in a bucket when you have enough.
                \\
                To get the solution more efficiently, Water can either be mixed with Demon's Dream Fruit or
                Otherworld Essence with heat applied.
                """);

        return BookEntryModel.create(fullyQualifiedEntryId(), context().entryName())
                .withDescription(context().entryDescription())
                .withIcon(OccultEngineeringFluids.SPIRIT_SOLUTION.getBucket().get())
                .withLocation(entryMap.get(icon))
                .withPages(intro, intro2);
    }

    private BookEntryModel makeChalksEntry(CategoryEntryMap entryMap, char icon) {
        var entryId = "chalks";
        context().entry(entryId);

        lang().add(context().entryName(), "New Chalks");
        lang().add(context().entryDescription(), "Fancier symbols to decorate the ground");

        context().page("chalks");
        var chalks = BookTextPageModel.builder()
                .withTitle(context().pageTitle())
                .withText(context().pageText())
                .build();

        lang().add(context().pageTitle(), "More Chalks");
        lang().add(context().pageText(), """
                Along with Occultism's standard chalk, you may need to end up crafting 3 new types of chalk.
                \\
                \\
                The cost of the chalk scales with the cost of the ritual, with Copper being the cheapest, then Zinc and
                finally Brass.
                """);

        context().page("chalks2");
        var chalks2 = BookTextPageModel.builder()
                .withText(context().pageText())
                .build();

        lang().add(context().pageText(), """
                The new chalks can only be crafted by pressing them with [Spirit Solution](entry://occultengineering:encyclopedia_of_souls/getting_started/spirit_solution).
                \\
                \\
                The more expensive the chalk, the more spirit solution that they require.
                """);

        context().page("purify_copper");
        var purify_copper = BookSpiritFireRecipePageModel.builder()
                .withRecipeId1(modLoc("spirit_fire/chalk_copper"))
                .withTitle1(context().pageTitle())
                .build();
        lang().add(context().pageTitle(), "Copper Chalk");

        context().page("purify_zinc");
        var purify_zinc = BookSpiritFireRecipePageModel.builder()
                .withRecipeId1(modLoc("spirit_fire/chalk_zinc"))
                .withTitle1(context().pageTitle())
                .build();
        lang().add(context().pageTitle(), "Zinc Chalk");

        context().page("purify_brass");
        var purify_brass = BookSpiritFireRecipePageModel.builder()
                .withRecipeId1(modLoc("spirit_fire/chalk_brass"))
                .withTitle1(context().pageTitle())
                .build();
        lang().add(context().pageTitle(), "Brass Chalk");

        return BookEntryModel.create(fullyQualifiedEntryId(), context().entryName())
                .withDescription(context().entryDescription())
                .withIcon(OccultEngineeringItems.COPPER_CHALK)
                .withLocation(entryMap.get(icon))
                .withPages(chalks, chalks2, purify_copper, purify_zinc, purify_brass);
    }

    private BookEntryModel makeSterlingSilverEntry(CategoryEntryMap entryMap, char icon) {
        var entryId = "sterling_silver";
        context().entry(entryId);

        lang().add(context().entryName(), "Sterling Silver");
        lang().add(context().entryDescription(), "A new versatile material");

        context().page("sterling_silver");
        var sterlingSilver = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(OccultEngineeringItems.STERLING_SILVER_INGOT))
                .withTitle(context().pageTitle())
                .withText(context().pageText())
                .build();

        lang().add(context().pageTitle(), "Sterling Silver");
        lang().add(context().pageText(), """
                Sterling Silver is a new crafting material that can be used to craft Occult Machinery.
                \\
                \\
                Craft it by mixing Copper Dust and Silver Dust in a heated basin.
                """);

        context().page("uses");
        var uses = BookTextPageModel.builder()
                .withTitle(context().pageTitle())
                .withText(context().pageText())
                .build();
        lang().add(context().pageTitle(), "Usage");
        lang().add(context().pageText(), """
                - [*Mechanical Chamber*](entry://occultengineering:encyclopedia_of_souls/getting_started/mechanical_chamber)
                - [*Mechanical Pulverizer*](entry://occultengineering:encyclopedia_of_souls/getting_started/mechanical_pulverizer)
                """);

        return BookEntryModel.create(fullyQualifiedEntryId(), context().entryName())
                .withDescription(context().entryDescription())
                .withIcon(OccultEngineeringItems.STERLING_SILVER_INGOT)
                .withLocation(entryMap.get(icon))
                .withPages(sterlingSilver, uses);
    }

    private BookEntryModel makeChamberEntry(CategoryEntryMap entryMap, char icon) {
        var entryId = "mechanical_chamber";
        context().entry(entryId);

        lang().add(context().entryName(), "Mechanical Chamber");
        lang().add(context().entryDescription(), "The height of ritual automation");

        context().page("mechanical_chamber");
        var mechanicalChamber = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(OccultEngineeringBlocks.MECHANICAL_CHAMBER))
                .withTitle(context().pageTitle())
                .withText(context().pageText())
                .build();
        lang().add(context().pageTitle(), "Mechanical Chamber");
        lang().add(context().pageText(), """
                The Mechanical Chamber can be used in place of the Golden Sacrificial Bowl for any ritual that does not require item use
                or sacrifice.
                \\
                \\
                It works best with the rituals that produce items.
                \\
                \\
                For more info, ponder the block.
                """);

        context().page("ritual");

        var ritual = BookRitualRecipePageModel.builder()
                .withRecipeId1(modLoc("ritual/craft_mechanical_chamber"))
                .build();

        return BookEntryModel.create(fullyQualifiedEntryId(), context().entryName())
                .withDescription(context().entryDescription())
                .withIcon(OccultEngineeringBlocks.MECHANICAL_CHAMBER)
                .withLocation(entryMap.get(icon))
                .withPages(mechanicalChamber, ritual);
    }

    private BookEntryModel makePulverizerEntry(CategoryEntryMap entryMap, char icon) {
        var entryId = "mechanical_pulverizer";
        context().entry(entryId);

        lang().add(context().entryName(), "Mechanical Pulverizer");
        lang().add(context().entryDescription(), "Dust galore!");

        context().page("mechanical_pulverizer");
        var mechanicalPulverizer = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(OccultEngineeringBlocks.MECHANICAL_PULVERIZER))
                .withTitle(context().pageTitle())
                .withText(context().pageText())
                .build();
        lang().add(context().pageTitle(), "Mechanical Pulverizer");
        lang().add(context().pageText(), """
                The Mechanical Chamber can be used instead of Occultism's crushing for any item a spirit can crush.
                \\
                \\
                For more info ponder the block.
                """);

        return BookEntryModel.create(fullyQualifiedEntryId(), context().entryName())
                .withIcon(OccultEngineeringBlocks.MECHANICAL_PULVERIZER)
                .withLocation(entryMap.get(icon))
                .withPages(mechanicalPulverizer);
    }

    private BookEntryModel makeDetectorEntry(CategoryEntryMap entryMap, char icon) {
        var entryId = "otherworld_detector";
        context().entry(entryId);

        lang().add(context().entryName(), "Otherworld Detector");
        lang().add(context().entryDescription(), "It knows when you're awake");

        context().page("otherworld_detector");
        var otherworldDetector = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(OccultEngineeringBlocks.OTHERWORLD_DETECTOR))
                .withTitle(context().pageTitle())
                .withText(context().pageText())
                .build();
        lang().add(context().pageTitle(), "Otherworld Detector");
        lang().add(context().pageText(), """
                An Otherworld Detector can be used to detect whether the nearest player can see into the otherworld.
                \\
                \\
                It outputs a redstone signal if they can.
                \\
                \\
                For more info ponder the block.
                """);

        /*
        context().page("ritual");

        var ritual = BookRitualRecipePageModel.builder()
                .withRecipeId1(modLoc("ritual/craft_otherworld_detector"))
                .build();
         */

        return BookEntryModel.create(fullyQualifiedEntryId(), context().entryName())
                .withDescription(context().entryDescription())
                .withIcon(OccultEngineeringBlocks.OTHERWORLD_DETECTOR)
                .withLocation(entryMap.get(icon))
                .withPages(otherworldDetector);
    }

    private BookEntryModel makePucaBookEntry(CategoryEntryMap entryMap, char icon) {
        var entryId = "puca_book";
        context().entry(entryId);

        lang().add(context().entryName(), "The Púca");
        lang().add(context().entryDescription(), "A new spirit fascinated with machinery");

        context().page("puca");
        var puca = BookSpotlightPageModel.builder()
                .withItem(Ingredient.of(OccultEngineeringItems.BOOK_OF_BINDING_PUCA))
                .withTitle(context().pageTitle())
                .withText(context().pageText())
                .build();

        lang().add(context().pageTitle(), "The Púca");
        lang().add(context().pageText(), """
                The Púca is a new spirit that can be used to perform new rituals.
                \\
                \\
                Its not very harmful, so it doesn't require large pentacles to contain it.
                \\
                \\
                They are primarily used for crafting machines.
                """);

        context().page("uses");
        var uses = BookTextPageModel.builder()
                .withTitle(context().pageTitle())
                .withText(context().pageText())
                .build();
        lang().add(context().pageTitle(), "Used to Craft");
        lang().add(context().pageText(), """
                - [*Otherworld Detector*](entry://occultengineering:encyclopedia_of_souls/getting_started/otherworld_detector)
                """);

        return BookEntryModel.create(fullyQualifiedEntryId(), context().entryName())
                .withDescription(context().entryDescription())
                .withIcon(OccultEngineeringItems.BOOK_OF_BINDING_PUCA)
                .withLocation(entryMap.get(icon))
                .withPages(puca, uses);
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

package io.github.irishgreencitrus.occultengineering.content.ponder;

import com.klikli_dev.occultism.common.blockentity.SacrificialBowlBlockEntity;
import com.klikli_dev.occultism.registry.OccultismBlocks;
import com.klikli_dev.occultism.registry.OccultismItems;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmBlockEntity;
import com.simibubi.create.content.logistics.box.PackageItem;
import com.simibubi.create.content.logistics.box.PackageStyles;
import com.simibubi.create.content.redstone.nixieTube.NixieTubeBlockEntity;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import io.github.irishgreencitrus.occultengineering.content.block.mechanical_chamber.MechanicalChamberBlockEntity;
import io.github.irishgreencitrus.occultengineering.content.block.mechanical_pulverizer.PulverizerBlockEntity;
import io.github.irishgreencitrus.occultengineering.content.phlogiport.PhlogiportSignalParticleData;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringBlocks;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringItems;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class OcEngPonders {
    public static void pulverizer(SceneBuilder builder, SceneBuildingUtil util) {
        var scene = new CreateSceneBuilder(builder);

        scene.title("mechanical_pulverizer", "Processing Items in the Mechanical Pulverizer");
        scene.configureBasePlate(0, 0, 5);
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(5);

        Selection beltAndCog = util.select().fromTo(0, 1, 3, 1, 2, 2);
        Selection chute = util.select().position(2, 3, 2);
        Selection cogs = util.select().fromTo(2, 1, 3, 3, 2, 5);

        BlockPos pulverizer = util.grid().at(2, 2, 2);
        Selection pulverizerSelect = util.select().position(pulverizer);

        scene.idle(5);
        scene.world().showSection(util.select().position(2, 1, 2), Direction.DOWN);
        scene.idle(10);
        scene.world().showSection(pulverizerSelect, Direction.DOWN);
        scene.world().setKineticSpeed(pulverizerSelect, 32);

        scene.idle(10);
        var pulverizerTop = util.vector().topOf(pulverizer);
        scene.overlay().showText(60)
                .attachKeyFrame()
                .text("Pulverizers can be used to crush items into dust")
                .pointAt(pulverizerTop)
                .placeNearTarget();
        scene.idle(70);

        scene.world().showSection(cogs, Direction.DOWN);
        scene.idle(10);
        scene.effects().indicateSuccess(pulverizer);
        scene.idle(10);

        scene.overlay().showText(60)
                .attachKeyFrame()
                .colored(PonderPalette.GREEN)
                .text("Power them from the back with a Shaft")
                .pointAt(util.vector().blockSurface(pulverizer, Direction.SOUTH))
                .placeNearTarget();
        scene.idle(70);

        scene.overlay().showText(40)
                .attachKeyFrame()
                .text("Right-click the Pulverizer with a valid item")
                .pointAt(pulverizerTop)
                .placeNearTarget();
        scene.idle(50);

        ItemStack rawIron = new ItemStack(Items.RAW_IRON);
        scene.overlay()
                .showControls(util.vector().blockSurface(pulverizer, Direction.NORTH), Pointing.RIGHT, 30)
                .rightClick()
                .withItem(rawIron);
        scene.idle(40);
        scene.world().modifyBlockEntity(pulverizer, PulverizerBlockEntity.class,
                ms -> ms.inputInv.setStackInSlot(0, rawIron));
        scene.idle(20);
        scene.overlay().showText(50)
                .text("After some time, the result can be retrieved by Right-clicking again")
                .pointAt(util.vector().blockSurface(pulverizer, Direction.NORTH))
                .placeNearTarget();
        scene.idle(60);
        scene.world().modifyBlockEntity(pulverizer, PulverizerBlockEntity.class,
                pv -> pv.inputInv.setStackInSlot(0, ItemStack.EMPTY));

        ItemStack ironDust = new ItemStack(OccultismItems.IRON_DUST.get(), 2);
        scene.overlay()
                .showControls(util.vector().blockSurface(pulverizer, Direction.NORTH), Pointing.RIGHT, 40)
                .rightClick()
                .withItem(ironDust);
        scene.idle(50);

        scene.world().showSection(chute, Direction.DOWN);
        scene.idle(10);
        scene.world().showSection(beltAndCog, Direction.EAST);
        scene.idle(15);

        scene.overlay().showText(40)
                .attachKeyFrame()
                .text("Inputs and Outputs can also be automated")
                .pointAt(util.vector().blockSurface(pulverizer, Direction.EAST).add(-0.5, 0.4, 0.0))
                .placeNearTarget();
        scene.idle(50);

        ItemStack copperIngot = new ItemStack(Items.COPPER_INGOT);
        Vec3 entitySpawn = util.vector().topOf(pulverizer.above(2));
        ElementLink<EntityElement> ingotEntity = scene.world().createItemEntity(entitySpawn, new Vec3(0, 0.2, 0), copperIngot);
        scene.idle(12);
        scene.world().modifyEntity(ingotEntity, Entity::discard);
        scene.world().modifyBlockEntity(pulverizer, PulverizerBlockEntity.class,
                pv -> pv.inputInv.setStackInSlot(0, copperIngot));
        scene.idle(40);

        var beltPos = util.grid().at(1, 1, 2);
        scene.world().modifyBlockEntity(pulverizer, PulverizerBlockEntity.class,
                pv -> pv.inputInv.setStackInSlot(0, ItemStack.EMPTY));

        scene.world().createItemOnBelt(beltPos, Direction.EAST, new ItemStack(OccultismItems.COPPER_DUST.get()));
        scene.idle(60);
    }

    public static void chamber(SceneBuilder builder, SceneBuildingUtil util) {
        var scene = new CreateSceneBuilder(builder);
        scene.title("mechanical_chamber", "Using the Mechanical Chamber to Automate Rituals");

        scene.configureBasePlate(0, 0, 7);
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(5);

        BlockPos mechChamber = util.grid().at(3, 1, 3);
        Selection mechChamberSelect = util.select().position(mechChamber);

        scene.idle(5);
        scene.world().showSection(mechChamberSelect, Direction.DOWN);
        scene.world().setKineticSpeed(mechChamberSelect, 0);
        scene.idle(10);

        var chamberTop = util.vector().topOf(mechChamber);
        scene.overlay().showText(80)
                .text("The Mechanical Chamber can be used in place of a Golden Sacrificial Bowl for Rituals which craft items")
                .pointAt(chamberTop)
                .placeNearTarget();
        scene.idle(90);

        var cogs = util.select().fromTo(3, 2, 3, 3, 3, 3);
        scene.world().showSection(cogs, Direction.DOWN);
        scene.world().setKineticSpeed(mechChamberSelect, -32);
        scene.idle(10);
        scene.effects().indicateSuccess(mechChamber);
        scene.idle(10);
        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("It must be powered from the top or bottom with Rotational Power")
                .pointAt(chamberTop)
                .placeNearTarget();
        scene.idle(60);


        scene.overlay().showText(50)
                .colored(PonderPalette.GREEN)
                .text("The faster the Rotation, the faster the Ritual will complete")
                .pointAt(chamberTop.add(0, 1, 0))
                .placeNearTarget();
        scene.idle(60);

        var chalks = util.select().fromTo(1, 1, 1, 6, 1, 6)
                .substract(mechChamberSelect)
                .add(util.select().fromTo(0, 1, 0, 1, 1, 1))
                .add(util.select().fromTo(0, 1, 5, 1, 1, 6))
                .add(util.select().fromTo(5, 1, 0, 6, 1, 1));

        scene.world().showSection(chalks, Direction.DOWN);
        scene.idle(10);
        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Surround the chamber with chalks and other ritual blocks")
                .pointAt(chamberTop)
                .placeNearTarget();
        scene.idle(60);

        var bowls = util.select().position(2, 1, 0)
                .add(util.select().position(0, 1, 2))
                .add(util.select().position(4, 1, 0))
                .add(util.select().position(0, 1, 4));

        scene.world().showSection(bowls, Direction.DOWN);
        scene.idle(10);
        scene.overlay().showOutline(PonderPalette.GREEN, new Object(), bowls, 50);

        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("The chamber will pull automatically from Sacrificial Bowls within 8 blocks")
                .pointAt(util.vector().blockSurface(util.grid().at(2, 1, 0), Direction.NORTH))
                .placeNearTarget();
        scene.idle(60);

        var bowl1 = util.grid().at(2, 1, 0);
        var bowl2 = util.grid().at(0, 1, 2);
        var bowl3 = util.grid().at(4, 1, 0);

        var observer = new ItemStack(Blocks.OBSERVER);
        var otherStone = new ItemStack(OccultismBlocks.OTHERSTONE.get());
        var otherEssence = new ItemStack(OccultismItems.OTHERWORLD_ESSENCE.get());

        scene.world().modifyBlockEntity(bowl1, SacrificialBowlBlockEntity.class,
                bwl -> bwl.itemStackHandler.setStackInSlot(0, observer));
        scene.idle(5);
        scene.world().modifyBlockEntity(bowl2, SacrificialBowlBlockEntity.class,
                bwl -> bwl.itemStackHandler.setStackInSlot(0, otherStone));
        scene.idle(5);
        scene.world().modifyBlockEntity(bowl3, SacrificialBowlBlockEntity.class,
                bwl -> bwl.itemStackHandler.setStackInSlot(0, otherEssence));
        scene.idle(10);


        scene.overlay().showText(40)
                .attachKeyFrame()
                .text("The bowls can either be filled by hand or through automation")
                .pointAt(util.vector().topOf(bowl2))
                .placeNearTarget();
        scene.idle(50);

        var inputDepot = util.grid().at(3, 1, 0);
        var outputDepot = util.grid().at(0, 1, 3);
        var depots = util.select().position(inputDepot).add(util.select().position(outputDepot));

        var inputArm = util.grid().at(4, 3, 2);
        var outputArm = util.grid().at(2, 3, 4);

        var arms = util.select().position(inputArm).add(util.select().position(outputArm));

        scene.world().showSection(depots, Direction.DOWN);
        scene.idle(10);
        scene.world().showSection(arms, Direction.DOWN);
        scene.idle(10);

        scene.overlay().showOutline(PonderPalette.BLUE, new Object(), arms, 40);
        scene.overlay().showText(40)
                .attachKeyFrame()
                .text("Mechanical Arms are the easiest way to use the chamber")
                .pointAt(util.vector().blockSurface(util.grid().at(4, 3, 2), Direction.NORTH))
                .placeNearTarget();
        scene.idle(50);


        var inputBook = OccultEngineeringItems.BOOK_OF_BINDING_BOUND_PUCA.asStack();
        scene.world().createItemOnBeltLike(inputDepot, Direction.NORTH, inputBook);
        scene.idle(20);
        scene.overlay().showText(60)
                .text("They only insert bound books once the rest of the ritual is complete")
                .pointAt(util.vector().blockSurface(inputDepot, Direction.NORTH))
                .placeNearTarget();
        scene.idle(70);

        scene.world().instructArm(inputArm, ArmBlockEntity.Phase.MOVE_TO_INPUT, ItemStack.EMPTY, 0);
        scene.idle(28);
        scene.world().removeItemsFromBelt(inputDepot);
        scene.world().instructArm(inputArm, ArmBlockEntity.Phase.SEARCH_OUTPUTS, inputBook, -1);
        scene.idle(20);
        scene.world().instructArm(inputArm, ArmBlockEntity.Phase.MOVE_TO_OUTPUT, inputBook, 0);
        scene.idle(28);
        scene.world().modifyBlockEntity(mechChamber, MechanicalChamberBlockEntity.class,
                mech -> mech.itemStackHandler.setStackInSlot(0, inputBook));
        scene.world().instructArm(inputArm, ArmBlockEntity.Phase.SEARCH_INPUTS, ItemStack.EMPTY, 0);
        scene.idle(20);

        scene.overlay().showText(50)
                .text("The Mechanical Chamber will then initiate the Ritual")
                .pointAt(util.vector().topOf(mechChamber))
                .placeNearTarget();
        scene.idle(60);

        scene.world().modifyBlockEntity(bowl1, SacrificialBowlBlockEntity.class,
                bwl -> bwl.itemStackHandler.setStackInSlot(0, ItemStack.EMPTY));
        scene.effects().emitParticles(bowl1.getCenter(),
                scene.effects().simpleParticleEmitter(ParticleTypes.SMOKE, new Vec3(0, 0.1, 0)),
                1,
                10);
        scene.idle(20);

        scene.world().modifyBlockEntity(bowl2, SacrificialBowlBlockEntity.class,
                bwl -> bwl.itemStackHandler.setStackInSlot(0, ItemStack.EMPTY));
        scene.effects().emitParticles(bowl2.getCenter(),
                scene.effects().simpleParticleEmitter(ParticleTypes.SMOKE, new Vec3(0, 0.1, 0)),
                1,
                10);
        scene.idle(20);

        scene.world().modifyBlockEntity(bowl3, SacrificialBowlBlockEntity.class,
                bwl -> bwl.itemStackHandler.setStackInSlot(0, ItemStack.EMPTY));
        scene.effects().emitParticles(bowl3.getCenter(),
                scene.effects().simpleParticleEmitter(ParticleTypes.SMOKE, new Vec3(0, 0.1, 0)),
                1,
                10);
        scene.idle(20);

        var outputBlock = OccultEngineeringBlocks.OTHERWORLD_DETECTOR.asStack();

        scene.world().modifyBlockEntity(mechChamber, MechanicalChamberBlockEntity.class,
                mech -> mech.itemStackHandler.setStackInSlot(0, outputBlock));

        scene.idle(20);

        scene.world().instructArm(outputArm, ArmBlockEntity.Phase.MOVE_TO_INPUT, ItemStack.EMPTY, 0);
        scene.idle(28);
        scene.world().modifyBlockEntity(mechChamber, MechanicalChamberBlockEntity.class,
                mech -> mech.itemStackHandler.setStackInSlot(0, ItemStack.EMPTY));
        scene.world().instructArm(outputArm, ArmBlockEntity.Phase.SEARCH_OUTPUTS, outputBlock, -1);
        scene.idle(20);
        scene.world().instructArm(outputArm, ArmBlockEntity.Phase.MOVE_TO_OUTPUT, outputBlock, 0);
        scene.idle(28);
        scene.world().createItemOnBeltLike(outputDepot, Direction.EAST, outputBlock);
        scene.world().instructArm(outputArm, ArmBlockEntity.Phase.SEARCH_INPUTS, ItemStack.EMPTY, 0);
        scene.overlay().showText(60)
                .text("They also only extract the resulting items")
                .pointAt(util.vector().blockSurface(outputDepot, Direction.NORTH))
                .placeNearTarget();
        scene.idle(120);
    }

    public static void detector(SceneBuilder builder, SceneBuildingUtil util) {
        var scene = new CreateSceneBuilder(builder);
        scene.title("otherworld_detector", "Detecting with the Otherworld Detector");
        scene.configureBasePlate(0, 0, 5);
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(10);
        BlockPos detector = util.grid().at(2, 1, 2);
        Selection detectorSelect = util.select().position(detector);

        Selection redstoneWiring = util.select().position(1, 1, 2);
        Selection comparatorAndTubes = util.select().fromTo(2, 1, 0, 2, 1, 1);
        scene.world().showSection(detectorSelect, Direction.DOWN);
        scene.idle(10);
        scene.overlay().showText(60)
                .attachKeyFrame()
                .text("Otherworld Detectors detect whether the nearest player can see into the Otherworld")
                .pointAt(util.vector().topOf(detector))
                .placeNearTarget();
        scene.idle(70);

        scene.world().showSection(redstoneWiring, Direction.DOWN);
        scene.idle(10);

        var zombieLoc = util.vector().topOf(2, 0, 4);

        scene.idle(4);
        scene.effects().indicateRedstone(detector);
        scene.world().toggleRedstonePower(detectorSelect);
        scene.world().toggleRedstonePower(redstoneWiring);
        scene.idle(10);
        scene.overlay().showText(60)
                .attachKeyFrame()
                .text("This can be accomplished by either consuming Demon's Dream Fruit to get the Third Eye, or by wearing Otherworld Goggles")
                .pointAt(zombieLoc)
                .placeNearTarget();
        scene.idle(70);

        var nixieLoc = util.grid().at(2, 1, 0);
        scene.world().toggleRedstonePower(util.select().position(2, 1, 1));
        scene.world().modifyBlockEntityNBT(util.select().position(nixieLoc), NixieTubeBlockEntity.class, nbt -> nbt.putInt("RedstoneStrength", 15));
        scene.world().showSection(comparatorAndTubes, Direction.DOWN);
        scene.idle(10);


        scene.overlay().showText(90)
                .attachKeyFrame()
                .text("A comparator can also be used to get the distance to the player")
                .pointAt(util.vector().topOf(nixieLoc))
                .placeNearTarget();

        for (int i = 14; i >= 1; i--) {
            int finalI = i;
            scene.world().modifyBlockEntityNBT(util.select().position(nixieLoc), NixieTubeBlockEntity.class, nbt -> nbt.putInt("RedstoneStrength", finalI));
            scene.idle(10);
        }

        scene.idle(60);
    }

    public static void phlogiport(SceneBuilder builder, SceneBuildingUtil util) {
        var scene = new CreateSceneBuilder(builder);
        scene.title("phlogiport", "Transporting Items with the Phlogiport");
        scene.configureBasePlate(0, 0, 9);
        scene.scaleSceneView(0.75F);
        scene.setSceneOffsetY(-1.0F);
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(10);

        var mainPhlogiport = util.grid().at(4, 2, 7);
        var mainPhlogiportSelect = util.select().fromTo(mainPhlogiport.below(), mainPhlogiport);
        scene.world().showSection(mainPhlogiportSelect, Direction.DOWN);
        scene.idle(10);

        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("Phlogiports can be used to wirelessly transfer items short distances")
                .pointAt(util.vector().topOf(mainPhlogiport))
                .placeNearTarget();

        scene.idle(80);
        scene.overlay().showControls(util.vector().topOf(mainPhlogiport), Pointing.DOWN, 40).rightClick();
        scene.idle(7);

        scene.overlay().chaseBoundingBoxOutline(PonderPalette.BLUE, mainPhlogiport, new AABB(mainPhlogiport), 70);
        scene.overlay().showText(70).attachKeyFrame().colored(PonderPalette.BLUE).text("Assign it an address in the inventory UI for it to function").pointAt(util.vector().topOf(mainPhlogiport)).placeNearTarget();
        scene.idle(80);


        scene.overlay().showText(90)
                .attachKeyFrame()
                .text("Items can be inserted via the Inventory UI")
                .pointAt(util.vector().topOf(mainPhlogiport))
                .placeNearTarget();

        scene.idle(20);
        scene.overlay().showText(70)
                .text("...or via automation")
                .pointAt(util.vector().centerOf(mainPhlogiport.below()))
                .placeNearTarget();
        scene.idle(70);

        var funnel = util.grid().at(3, 2, 7);
        var automationSelect = util.select().fromTo(0, 1, 7, 3, 2, 9);
        scene.world().showSection(automationSelect, Direction.DOWN);


        var secondPhlogiport = util.grid().at(7, 2, 1);
        var secondPhlogiportSelect = util.select().fromTo(secondPhlogiport.west().below(), secondPhlogiport);
        var secondFunnel = secondPhlogiport.west();
        scene.world().showSection(secondPhlogiportSelect, Direction.DOWN);
        scene.idle(10);

        var boxItem = PackageStyles.getDefaultBox().copy();
        PackageItem.addAddress(boxItem, "Warehouse");
        scene.world().createItemOnBelt(util.grid().at(0, 1, 7), Direction.NORTH, boxItem);

        scene.idle(5);
        scene.world().multiplyKineticSpeed(util.select().everywhere(), 0.03125F);

        scene.overlay().showText(60).attachKeyFrame().text("If the address of an inserted package does not match it..").pointAt(util.vector().centerOf(0, 1, 8)).placeNearTarget();
        scene.idle(70);

        scene.overlay().showText(40).colored(PonderPalette.BLUE).text("Home").pointAt(util.vector().blockSurface(mainPhlogiport, Direction.NORTH).add(-0.5F, 0.0F, 0.0F)).placeNearTarget();
        scene.idle(5);

        scene.overlay().showText(40).colored(PonderPalette.OUTPUT).text("→ Warehouse").pointAt(util.vector().centerOf(0, 1, 8)).placeNearTarget();
        scene.idle(50);

        scene.world().multiplyKineticSpeed(util.select().everywhere(), 32.0F);
        scene.idle(36);

        scene.world().removeItemsFromBelt(mainPhlogiport.west().below());
        scene.world().flapFunnel(funnel, false);
        scene.idle(15);

        scene.overlay().showText(60)
                .text("..and there is another Phlogiport within range with that name")
                .pointAt(util.vector().centerOf(secondPhlogiport))
                .placeNearTarget();
        scene.idle(70);

        scene.overlay().chaseBoundingBoxOutline(PonderPalette.BLUE, mainPhlogiport, new AABB(mainPhlogiport), 60);

        scene.overlay().showText(60)
                .text("Home")
                .colored(PonderPalette.BLUE)
                .pointAt(util.vector().blockSurface(mainPhlogiport, Direction.SOUTH))
                .placeNearTarget();

        scene.overlay().chaseBoundingBoxOutline(PonderPalette.GREEN, secondPhlogiport, new AABB(secondPhlogiport), 60);

        scene.overlay().showText(60)
                .text("Warehouse")
                .colored(PonderPalette.GREEN)
                .pointAt(util.vector().blockSurface(secondPhlogiport, Direction.EAST))
                .placeNearTarget();

        scene.idle(70);

        scene.overlay().showText(60)
                .text("The package will be wirelessly sent to its inventory")
                .pointAt(util.vector().centerOf(secondPhlogiport))
                .placeNearTarget();

        scene.addInstruction(s -> {
            var vec3 = Vec3.atCenterOf(mainPhlogiport);
            s.getWorld().addParticle(new PhlogiportSignalParticleData(new BlockPositionSource(secondPhlogiport), 40), vec3.x, vec3.y, vec3.z, 1, 1, 1);
        });
        scene.idle(45);

        scene.world().createItemOnBeltLike(secondFunnel.below(), Direction.EAST, boxItem);
        scene.world().flapFunnel(secondFunnel, true);
        scene.idle(30);

        var thirdPhlogiport = util.grid().at(1, 2, 1);
        var thirdPhlogiportSelect = util.select().fromTo(thirdPhlogiport.below().east(), thirdPhlogiport);
        scene.world().showSection(thirdPhlogiportSelect, Direction.DOWN);
        scene.idle(10);

        scene.overlay().showText(60)
                .attachKeyFrame()
                .text("If there are multiple ports with the same name...")
                .pointAt(util.vector().topOf(thirdPhlogiport))
                .placeNearTarget();

        scene.idle(70);

        scene.overlay().chaseBoundingBoxOutline(PonderPalette.GREEN, secondPhlogiport, new AABB(secondPhlogiport), 130);
        scene.overlay().chaseBoundingBoxOutline(PonderPalette.GREEN, thirdPhlogiport, new AABB(thirdPhlogiport), 130);

        scene.overlay().showText(60)
                .text("Warehouse")
                .colored(PonderPalette.GREEN)
                .pointAt(util.vector().blockSurface(secondPhlogiport, Direction.EAST))
                .placeNearTarget();
        scene.overlay().showText(60)
                .text("Warehouse")
                .colored(PonderPalette.GREEN)
                .pointAt(util.vector().blockSurface(thirdPhlogiport, Direction.WEST))
                .placeNearTarget();
        scene.idle(70);

        scene.overlay().showText(60)
                .text("They will receive matching packages randomly")
                .pointAt(util.vector().topOf(thirdPhlogiport))
                .placeNearTarget();
    }
}

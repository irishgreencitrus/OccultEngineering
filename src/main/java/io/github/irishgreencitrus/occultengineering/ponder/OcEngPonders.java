package io.github.irishgreencitrus.occultengineering.ponder;

import com.klikli_dev.occultism.registry.OccultismItems;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import io.github.irishgreencitrus.occultengineering.block.mechanical_pulverizer.PulverizerBlockEntity;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
}

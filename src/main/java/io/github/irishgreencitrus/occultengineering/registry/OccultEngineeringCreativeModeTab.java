package io.github.irishgreencitrus.occultengineering.registry;

import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@EventBusSubscriber
public class OccultEngineeringCreativeModeTab {
    private static final DeferredRegister<CreativeModeTab>
            REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, OccultEngineering.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab>
            CREATIVE_TAB = REGISTER.register("base", () -> CreativeModeTab
            .builder()
            .title(Component.literal("Create: Occult Engineering"))
            .withTabsBefore(
                    AllCreativeModeTabs.BASE_CREATIVE_TAB.getId(),
                    AllCreativeModeTabs.PALETTES_CREATIVE_TAB.getId())
            .icon(
                    OccultEngineeringBlocks.MECHANICAL_CHAMBER::asStack
            )
            .displayItems(new OcEngDisplayItems())
            .build());

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }

    private static class OcEngDisplayItems implements CreativeModeTab.DisplayItemsGenerator {
        private static Predicate<Item> makeExclusionPredicate() {
            Set<Item> exclusions = new ReferenceOpenHashSet<>();

            var simpleExclusions = Set.of(
                    OccultEngineeringItems.EMPTY_PENTACLE_SCHEMATIC,
                    OccultEngineeringItems.PENTACLE_SCHEMATIC,
                    OccultEngineeringItems.SPAWN_EGG_PUCA,
                    OccultEngineeringBlocks.PENTACLE_ALTAR,
                    OccultEngineeringBlocks.PUCALITH
            );

            for (var i : simpleExclusions) {
                exclusions.add(i.asItem());
            }

            return exclusions::contains;
        }

        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
            Predicate<Item> exclusionPredicate = makeExclusionPredicate();
            List<Item> items = new ArrayList<>();
            items.addAll(collectItems(exclusionPredicate));
            items.addAll(collectBlocks(exclusionPredicate));

            for (Item item : items) {
                output.accept(item, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }

        private List<Item> collectBlocks(Predicate<Item> exclusionPredicate) {
            List<Item> items = new ReferenceArrayList<>();
            for (RegistryEntry<Block, Block> entry : OccultEngineering.REGISTRATE.getAll(Registries.BLOCK)) {
                if (!CreateRegistrate.isInCreativeTab(entry, OccultEngineeringCreativeModeTab.CREATIVE_TAB))
                    continue;
                Item item = entry.get()
                        .asItem();
                if (item == Items.AIR)
                    continue;
                if (!exclusionPredicate.test(item))
                    items.add(item);
            }
            items = new ReferenceArrayList<>(new ReferenceLinkedOpenHashSet<>(items));
            return items;
        }

        private List<Item> collectItems(Predicate<Item> exclusionPredicate) {
            List<Item> items = new ReferenceArrayList<>();
            for (RegistryEntry<Item, Item> entry : OccultEngineering.REGISTRATE.getAll(Registries.ITEM)) {
                if (!CreateRegistrate.isInCreativeTab(entry, OccultEngineeringCreativeModeTab.CREATIVE_TAB))
                    continue;
                Item item = entry.get();
                if (item instanceof BlockItem)
                    continue;
                if (!exclusionPredicate.test(item))
                    items.add(item);
            }
            return items;
        }
    }
}

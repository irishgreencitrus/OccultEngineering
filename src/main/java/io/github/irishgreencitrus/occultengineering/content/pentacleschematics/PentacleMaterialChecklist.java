package io.github.irishgreencitrus.occultengineering.content.pentacleschematics;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllDataComponents;
import com.simibubi.create.content.equipment.clipboard.ClipboardEntry;
import com.simibubi.create.content.equipment.clipboard.ClipboardOverrides;
import com.simibubi.create.content.equipment.clipboard.ClipboardOverrides.ClipboardType;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.utility.CreateLang;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.network.Filterable;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PentacleMaterialChecklist {
    public static final int MAX_ENTRIES_PER_PAGE = 5;
    public static final int MAX_ENTRIES_PER_CLIPBOARD_PAGE = 7;

    public Object2IntMap<Item> gathered = new Object2IntArrayMap<>();
    public Object2IntMap<Item> required = new Object2IntArrayMap<>();
    public Object2IntMap<Item> damageRequired = new Object2IntArrayMap<>();

    // We can also have blocks in pentacles that map to a tag, i.e. the candles in every Occultism pentacle
    //  have the tag '#minecraft:candles'.
    // So we need a way to properly display whether you have collected a tag item.
    // TODO(maybe): we may need to have a least common tag system
    //  If we have 2 tags in a pentacle, say '#occultism:candles' and '#minecraft:candles',
    //  Occultism's White Candle (renamed Large Candle in 1.21 I believe), should first fill the requirement for '#occultism:candles'
    //  if there are already enough '#minecraft:candles'. or something of the sort.
    public Object2IntMap<GatheredTagItem> gatheredTag = new Object2IntArrayMap<>();
    public Object2IntMap<TagKey<Block>> requiredTag = new Object2IntArrayMap<>();
    public boolean blocksNotLoaded;

    public void warnBlockNotLoaded() {
        blocksNotLoaded = true;
    }

    public void require(ItemRequirement requirement) {
        if (requirement.isEmpty())
            return;
        if (requirement.isInvalid())
            return;

        for (ItemRequirement.StackRequirement stack : requirement.getRequiredItems()) {
            if (stack instanceof BlockTagRequirement btr) {
                putOrIncrement(requiredTag, btr.tagKey);
                continue;
            }

            switch (stack.usage) {
                case DAMAGE -> putOrIncrement(damageRequired, stack.stack);
                case CONSUME -> putOrIncrement(required, stack.stack);
            }
        }
    }

    public void clear() {
        damageRequired.clear();
        required.clear();

        gathered.clear();

        gatheredTag.clear();
        requiredTag.clear();

        blocksNotLoaded = false;
    }

    private void putOrIncrement(Object2IntMap<Item> map, ItemStack stack) {
        Item item = stack.getItem();
        if (item == Items.AIR)
            return;
        if (map.containsKey(item))
            map.put(item, map.getInt(item) + stack.getCount());
        else
            map.put(item, stack.getCount());
    }

    private <T> void putOrIncrement(Object2IntMap<T> map, @Nullable T item) {
        if (item == null) return;
        map.put(item, map.getOrDefault(item, 0) + 1);
    }

    private ObjectSet<Object2IntMap.Entry<GatheredTagItem>> getAllGatheredTag(TagKey<Block> tag) {
        return gatheredTag
                .object2IntEntrySet()
                .stream()
                .filter(i -> i.getKey().tag == tag)
                .collect(Collectors.toCollection(ObjectOpenHashSet::new));
    }

    private ObjectSet<Object2IntMap.Entry<GatheredTagItem>> getAllGatheredTag(Item item) {
        return gatheredTag
                .object2IntEntrySet()
                .stream()
                .filter(i -> i.getKey().item == item)
                .collect(Collectors.toCollection(ObjectOpenHashSet::new));
    }

    public void collect(ItemStack stack) {
        Item item = stack.getItem();
        if (required.containsKey(item) || damageRequired.containsKey(item))
            gathered.put(item, gathered.getOrDefault(item, 0) + stack.getCount());
        else for (var tag : requiredTag.keySet())
            if (itemHasBlockTag(stack, tag)) {
                var gatheredItem = new GatheredTagItem(tag, stack.getItem());
                // NOTE: may run into an issue if an item can match multiple tags in a pentacle.
                var value = gatheredTag.getOrDefault(gatheredItem, 0);

                var matchingTagCount = gatheredTag
                        .keySet()
                        .stream()
                        .filter(t -> t.item == stack.getItem())
                        .map(i -> gatheredTag.getOrDefault(i, 0))
                        .reduce(Integer::sum)
                        .orElse(0);

                // this could solve the issue however?
                // if we have more than we need we try match the next tag
                // also may create more problems than it solves
                if (matchingTagCount >= requiredTag.getInt(tag)) continue;

                gatheredTag.put(gatheredItem, value + stack.getCount());
                return;
            }
    }

    private boolean itemHasBlockTag(ItemStack stack, TagKey<Block> tag) {
        if (!(stack.getItem() instanceof BlockItem blockItem)) return false;
        Block block = blockItem.getBlock();
        return block.defaultBlockState().is(tag);
    }

    public ItemStack createWrittenBook() {
        ItemStack book = new ItemStack(Items.WRITTEN_BOOK);

        List<Filterable<Component>> pages = new ArrayList<>();

        MutableComponent textComponent = Component.empty();

        if (blocksNotLoaded) {
            textComponent = Component.literal("\n" + ChatFormatting.RED);
            textComponent = textComponent.append(CreateLang.translateDirect("materialChecklist.blocksNotLoaded"));
            pages.add(Filterable.passThrough(textComponent));
        }

        var checklistEntries = getChecklistEntries();

        int itemsWritten = 0;

        for (var entry : checklistEntries) {
            if (itemsWritten == MAX_ENTRIES_PER_PAGE) {
                itemsWritten = 0;
                textComponent.append(Component.literal("\n >>>")
                        .withStyle(entry.unfinished ? ChatFormatting.DARK_GRAY : ChatFormatting.DARK_GREEN));
                pages.add(Filterable.passThrough(textComponent));
                textComponent = Component.empty();
            }

            itemsWritten++;
            textComponent.append(entry.format(true));
        }

        pages.add(Filterable.passThrough(textComponent));

        WrittenBookContent contents = new WrittenBookContent(
                Filterable.passThrough(ChatFormatting.BLUE + "Material Checklist"),
                "Púcalith",
                0,
                pages,
                true
        );
        book.set(DataComponents.WRITTEN_BOOK_CONTENT, contents);

        textComponent = CreateLang.translateDirect("materialChecklist")
                .setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE)
                        .withItalic(Boolean.FALSE));

        book.set(DataComponents.CUSTOM_NAME, textComponent);

        return book;
    }

    public ItemStack createWrittenClipboard() {
        ItemStack clipboard = AllBlocks.CLIPBOARD.asStack();


        List<List<ClipboardEntry>> pages = new ArrayList<>();
        List<ClipboardEntry> currentPage = new ArrayList<>();

        if (blocksNotLoaded) {
            currentPage.add(new ClipboardEntry(false, CreateLang.translateDirect("materialChecklist.blocksNotLoaded")
                    .withStyle(ChatFormatting.RED)));
        }


        var checklistEntries = getChecklistEntries();

        int itemsWritten = 0;

        for (var entry : checklistEntries) {
            if (itemsWritten == MAX_ENTRIES_PER_CLIPBOARD_PAGE) {
                itemsWritten = 0;
                currentPage.add(new ClipboardEntry(!entry.unfinished, Component.literal(">>>")
                        .withStyle(entry.unfinished ? ChatFormatting.DARK_GRAY : ChatFormatting.DARK_GREEN)));
                pages.add(currentPage);
                currentPage = new ArrayList<>();
            }

            itemsWritten++;
            currentPage.add(new ClipboardEntry(!entry.unfinished, entry.format(false))
                    .displayItem(entry.item, 0));
        }

        pages.add(currentPage);
        /*
        TODO: rework and reuse
        ClipboardEntry.saveAll(pages, clipboard);
        ClipboardOverrides.switchTo(ClipboardType.WRITTEN, clipboard);

        clipboard.set(DataComponents.CUSTOM_NAME, CreateLang.translateDirect("materialChecklist")
                        .setStyle(Style.EMPTY.withItalic(Boolean.FALSE)));
        clipboard.set(AllDataComponents.CLIPBOARD_READ_ONLY, Unit.INSTANCE);
         */
        return clipboard;
    }

    public List<ChecklistEntry> getChecklistEntries() {
        List<Item> allRequired = new ArrayList<>(Sets.union(required.keySet(), damageRequired.keySet()));

        allRequired.sort((item1, item2) -> {
            Locale locale = Locale.ENGLISH;
            String name1 = item1.getDescription()
                    .getString()
                    .toLowerCase(locale);
            String name2 = item2.getDescription()
                    .getString()
                    .toLowerCase(locale);
            return name1.compareTo(name2);
        });

        List<ChecklistEntry> checklistEntries = new ArrayList<>();

        List<Item> completedItem = new ArrayList<>();
        for (var item : allRequired) {
            int amount = getRequiredAmount(item);
            if (gathered.containsKey(item))
                amount -= gathered.getInt(item);

            if (amount <= 0) {
                completedItem.add(item);
                continue;
            }

            checklistEntries.add(new ChecklistItemEntry(item, amount, true));
        }

        List<TagKey<Block>> completedTag = new ArrayList<>();
        for (var blockTag : requiredTag.keySet()) {
            int amount = requiredTag.getInt(blockTag);
            var gatheredOf = getAllGatheredTag(blockTag);
            if (!gatheredOf.isEmpty()) {
                for (var entry : gatheredOf)
                    amount -= entry.getIntValue();
            }

            if (amount <= 0) {
                completedTag.add(blockTag);
                continue;
            }

            checklistEntries.add(new ChecklistTagEntry(blockTag, getRepresentativeItem(blockTag), amount, true));
        }

        for (var item : completedItem) {
            checklistEntries.add(new ChecklistItemEntry(item, getRequiredAmount(item), false));
        }

        for (var blockTag : completedTag) {
            checklistEntries.add(new ChecklistTagEntry(blockTag, getRepresentativeItem(blockTag), getRequiredAmount(blockTag), false));
        }

        return checklistEntries;
    }

    public int getRequiredAmount(Item item) {
        int amount = required.getOrDefault(item, 0);
        if (damageRequired.containsKey(item))
            amount += (int) Math.ceil(damageRequired.getInt(item) / (float) new ItemStack(item).getMaxDamage());
        return amount;
    }

    public int getRequiredAmount(TagKey<Block> blockTag) {
        return requiredTag.getOrDefault(blockTag, 0);
    }

    private ItemStack getRepresentativeItem(TagKey<Block> tag) {
        ImmutableList<Holder<Block>> all = ImmutableList.copyOf(BuiltInRegistries.BLOCK.getTagOrEmpty(tag));
        if (all.isEmpty()) return ItemStack.EMPTY;
        return new ItemStack(all.getFirst().value());
    }

    public record GatheredTagItem(TagKey<Block> tag, Item item) {
    }

    public abstract static class ChecklistEntry {
        public final ItemStack item;
        public final int amount;
        public boolean unfinished;


        private ChecklistEntry(ItemStack item, int amount, boolean unfinished) {
            this.unfinished = unfinished;
            this.item = item;
            this.amount = amount;
        }

        private int stackCount() {
            return amount / 64;
        }

        private int stackRemainder() {
            return amount % 64;
        }

        public MutableComponent format(boolean forBook) {
            MutableComponent tc = Component.empty();

            tc.append(getEntryName()
                    .setStyle(Style.EMPTY
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(item)))));

            if (!unfinished && forBook)
                tc.append(" ✔");

            if (!unfinished || forBook)
                tc.withStyle(unfinished ? ChatFormatting.BLUE : ChatFormatting.DARK_GREEN);

            return tc.append(Component.literal("\n" + " x" + amount)
                            .withStyle(ChatFormatting.BLACK))
                    .append(Component.literal(" | " + stackCount() + "▤ +" + stackRemainder() + (forBook ? "\n" : ""))
                            .withStyle(ChatFormatting.GRAY));
        }

        public abstract @NotNull MutableComponent getEntryName();
    }

    private class ChecklistItemEntry extends ChecklistEntry {
        private ChecklistItemEntry(Item item, int amount, boolean unfinished) {
            this(new ItemStack(item), amount, unfinished);
        }

        private ChecklistItemEntry(ItemStack item, int amount, boolean unfinished) {
            super(item, amount, unfinished);
        }

        @Override
        public @NotNull MutableComponent getEntryName() {
            return Component.translatable(item.getDescriptionId());
        }
    }

    private class ChecklistTagEntry extends ChecklistEntry {
        TagKey<Block> tag;

        private ChecklistTagEntry(TagKey<Block> tag, Item item, int amount, boolean unfinished) {
            this(tag, new ItemStack(item), amount, unfinished);
        }

        private ChecklistTagEntry(TagKey<Block> tag, ItemStack item, int amount, boolean unfinished) {
            super(item, amount, unfinished);
            this.tag = tag;
        }

        @Override
        public @NotNull MutableComponent getEntryName() {
            var tl = tag.location();
            return Component.literal("Any #" + tl.getNamespace() + ":" + tl.getPath());
        }
    }
}
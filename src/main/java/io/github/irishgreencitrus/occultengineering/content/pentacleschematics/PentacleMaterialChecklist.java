package io.github.irishgreencitrus.occultengineering.content.pentacleschematics;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.simibubi.create.AllBlocks;
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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
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

    public class GatheredTagItem {
        public final TagKey<Block> tag;
        public final Item item;

        public GatheredTagItem(TagKey<Block> tag, Item stack) {
            this.tag = tag;
            this.item = stack;
        }
    }

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

        CompoundTag tag = book.getOrCreateTag();
        ListTag pages = new ListTag();

        int itemsWritten = 0;
        MutableComponent textComponent;

        if (blocksNotLoaded) {
            textComponent = Component.literal("\n" + ChatFormatting.RED);
            textComponent = textComponent.append(CreateLang.translateDirect("materialChecklist.blocksNotLoaded"));
            pages.add(StringTag.valueOf(Component.Serializer.toJson(textComponent)));
        }

        List<Item> keys = new ArrayList<>(Sets.union(required.keySet(), damageRequired.keySet()));
        keys.sort((item1, item2) -> {
            Locale locale = Locale.ENGLISH;
            String name1 = item1.getDescription()
                    .getString()
                    .toLowerCase(locale);
            String name2 = item2.getDescription()
                    .getString()
                    .toLowerCase(locale);
            return name1.compareTo(name2);
        });

        textComponent = Component.empty();
        List<Item> completed = new ArrayList<>();
        for (Item item : keys) {
            int amount = getRequiredAmount(item);
            if (gathered.containsKey(item))
                amount -= gathered.getInt(item);

            if (amount <= 0) {
                completed.add(item);
                continue;
            }

            if (itemsWritten == MAX_ENTRIES_PER_PAGE) {
                itemsWritten = 0;
                textComponent.append(Component.literal("\n >>>")
                        .withStyle(ChatFormatting.BLUE));
                pages.add(StringTag.valueOf(Component.Serializer.toJson(textComponent)));
                textComponent = Component.empty();
            }

            itemsWritten++;
            textComponent.append(itemEntry(new ItemStack(item), amount, true, true));
        }

        for (Item item : completed) {
            if (itemsWritten == MAX_ENTRIES_PER_PAGE) {
                itemsWritten = 0;
                textComponent.append(Component.literal("\n >>>")
                        .withStyle(ChatFormatting.DARK_GREEN));
                pages.add(StringTag.valueOf(Component.Serializer.toJson(textComponent)));
                textComponent = Component.empty();
            }

            itemsWritten++;
            textComponent.append(itemEntry(new ItemStack(item), getRequiredAmount(item), false, true));
        }

        pages.add(StringTag.valueOf(Component.Serializer.toJson(textComponent)));

        tag.put("pages", pages);
        tag.putBoolean("readonly", true);
        tag.putString("author", "Schematicannon");
        tag.putString("title", ChatFormatting.BLUE + "Material Checklist");
        textComponent = CreateLang.translateDirect("materialChecklist")
                .setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE)
                        .withItalic(Boolean.FALSE));
        book.getOrCreateTagElement("display")
                .putString("Name", Component.Serializer.toJson(textComponent));
        book.setTag(tag);

        return book;
    }

    public ItemStack createWrittenClipboard() {
        ItemStack clipboard = AllBlocks.CLIPBOARD.asStack();
        CompoundTag tag = clipboard.getOrCreateTag();
        int itemsWritten = 0;

        List<List<ClipboardEntry>> pages = new ArrayList<>();
        List<ClipboardEntry> currentPage = new ArrayList<>();

        if (blocksNotLoaded) {
            currentPage.add(new ClipboardEntry(false, CreateLang.translateDirect("materialChecklist.blocksNotLoaded")
                    .withStyle(ChatFormatting.RED)));
        }

        List<Item> keys = new ArrayList<>(Sets.union(required.keySet(), damageRequired.keySet()));
        Collections.sort(keys, (item1, item2) -> {
            Locale locale = Locale.ENGLISH;
            String name1 = item1.getDescription()
                    .getString()
                    .toLowerCase(locale);
            String name2 = item2.getDescription()
                    .getString()
                    .toLowerCase(locale);
            return name1.compareTo(name2);
        });

        List<Item> completed = new ArrayList<>();
        for (Item item : keys) {
            int amount = getRequiredAmount(item);
            if (gathered.containsKey(item))
                amount -= gathered.getInt(item);

            if (amount <= 0) {
                completed.add(item);
                continue;
            }

            if (itemsWritten == MAX_ENTRIES_PER_CLIPBOARD_PAGE) {
                itemsWritten = 0;
                currentPage.add(new ClipboardEntry(false, Component.literal(">>>")
                        .withStyle(ChatFormatting.DARK_GRAY)));
                pages.add(currentPage);
                currentPage = new ArrayList<>();
            }

            itemsWritten++;
            currentPage.add(new ClipboardEntry(false, itemEntry(new ItemStack(item), amount, true, false))
                    .displayItem(new ItemStack(item), amount));
        }

        List<TagKey<Block>> completedTag = new ArrayList<>();
        for (TagKey<Block> blockTag : requiredTag.keySet()) {
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

            if (itemsWritten == MAX_ENTRIES_PER_CLIPBOARD_PAGE) {
                itemsWritten = 0;
                currentPage.add(new ClipboardEntry(false, Component.literal(">>>")
                        .withStyle(ChatFormatting.DARK_GRAY)));
                pages.add(currentPage);
                currentPage = new ArrayList<>();
            }

            itemsWritten++;
            currentPage.add(new ClipboardEntry(false, tagEntry(getRepresentativeItem(blockTag), blockTag, amount, true, false))
                    .displayItem(getRepresentativeItem(blockTag), amount));

        }

        for (Item item : completed) {
            if (itemsWritten == MAX_ENTRIES_PER_CLIPBOARD_PAGE) {
                itemsWritten = 0;
                currentPage.add(new ClipboardEntry(true, Component.literal(">>>")
                        .withStyle(ChatFormatting.DARK_GREEN)));
                pages.add(currentPage);
                currentPage = new ArrayList<>();
            }

            itemsWritten++;
            currentPage.add(new ClipboardEntry(true, itemEntry(new ItemStack(item), getRequiredAmount(item), false, false))
                    .displayItem(new ItemStack(item), 0));
        }

        for (TagKey<Block> blockTag : completedTag) {
            if (itemsWritten == MAX_ENTRIES_PER_CLIPBOARD_PAGE) {
                itemsWritten = 0;
                currentPage.add(new ClipboardEntry(true, Component.literal(">>>")
                        .withStyle(ChatFormatting.DARK_GREEN)));
                pages.add(currentPage);
                currentPage = new ArrayList<>();
            }

            itemsWritten++;
            currentPage.add(new ClipboardEntry(true, tagEntry(getRepresentativeItem(blockTag), blockTag, getRequiredAmount(blockTag), false, false))
                    .displayItem(getRepresentativeItem(blockTag), 0));
        }

        pages.add(currentPage);
        ClipboardEntry.saveAll(pages, clipboard);
        ClipboardOverrides.switchTo(ClipboardType.WRITTEN, clipboard);
        clipboard.getOrCreateTagElement("display")
                .putString("Name", Component.Serializer.toJson(CreateLang.translateDirect("materialChecklist")
                        .setStyle(Style.EMPTY.withItalic(Boolean.FALSE))));
        tag.putBoolean("Readonly", true);
        clipboard.setTag(tag);
        return clipboard;
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
        return new ItemStack(all.get(0).get());
    }

    private MutableComponent itemEntry(ItemStack item, int amount, boolean unfinished, boolean forBook) {
        int stacks = amount / 64;
        int remainder = amount % 64;
        MutableComponent tc = Component.empty();
        tc.append(Component.translatable(item.getDescriptionId())
                .setStyle(Style.EMPTY
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(item)))));

        if (!unfinished && forBook)
            tc.append(" ✔");
        if (!unfinished || forBook)
            tc.withStyle(unfinished ? ChatFormatting.BLUE : ChatFormatting.DARK_GREEN);
        return tc.append(Component.literal("\n" + " x" + amount)
                        .withStyle(ChatFormatting.BLACK))
                .append(Component.literal(" | " + stacks + "▤ +" + remainder + (forBook ? "\n" : ""))
                        .withStyle(ChatFormatting.GRAY));
    }

    private MutableComponent tagEntry(ItemStack representativeItem, TagKey<Block> tag, int amount, boolean unfinished, boolean forBook) {
        int stacks = amount / 64;
        int remainder = amount % 64;
        MutableComponent tc = Component.empty();
        var tagLocation = tag.location();
        tc.append(Component.literal("Any #" + tagLocation.getNamespace() + ":" + tagLocation.getPath())
                .setStyle(Style.EMPTY
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(representativeItem)))));

        if (!unfinished && forBook)
            tc.append(" ✔");
        if (!unfinished || forBook)
            tc.withStyle(unfinished ? ChatFormatting.GOLD : ChatFormatting.DARK_GREEN);
        return tc.append(Component.literal("\n" + " x" + amount)
                        .withStyle(ChatFormatting.BLACK))
                .append(Component.literal(" | " + stacks + "▤ +" + remainder + (forBook ? "\n" : ""))
                        .withStyle(ChatFormatting.GRAY));
    }

}
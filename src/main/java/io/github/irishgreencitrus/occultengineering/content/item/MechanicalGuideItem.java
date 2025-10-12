package io.github.irishgreencitrus.occultengineering.content.item;

import com.klikli_dev.modonomicon.api.ModonomiconConstants;
import com.klikli_dev.modonomicon.book.Book;
import com.klikli_dev.modonomicon.client.gui.BookGuiManager;
import com.klikli_dev.modonomicon.data.BookDataManager;
import com.klikli_dev.modonomicon.item.ModonomiconItem;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MechanicalGuideItem extends ModonomiconItem {
    public static ResourceLocation ENCYCLOPEDIA_OF_SOULS = OccultEngineering.asResource("encyclopedia_of_souls");

    public MechanicalGuideItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        var itemInHand = pPlayer.getItemInHand(pUsedHand);

        if (pLevel.isClientSide) {
            var book = BookDataManager.get().getBook(ENCYCLOPEDIA_OF_SOULS);
            BookGuiManager.get().openBook(book.getLeafletAddress());
        }

        return InteractionResultHolder.sidedSuccess(itemInHand, pLevel.isClientSide);
    }

    @Override
    public Component getName(ItemStack pStack) {
        Book book = BookDataManager.get().getBook(ENCYCLOPEDIA_OF_SOULS);
        if (book != null) {
            return Component.translatable(book.getName());
        }

        return super.getName(pStack);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag tooltipFlag) {
        Book book = BookDataManager.get().getBook(ENCYCLOPEDIA_OF_SOULS);
        if (book != null) {
            if (tooltipFlag.isAdvanced()) {
                tooltip.add(Component.literal("Book ID: ").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.literal(book.getId().toString()).withStyle(ChatFormatting.RED)));
            }

            if (!book.getTooltip().isBlank()) {
                tooltip.add(Component.translatable(book.getTooltip()).withStyle(ChatFormatting.GRAY));
            }

        } else {
            var compoundTag = new CompoundTag();
            for (var entry : itemStack.getComponents()) {
                var tag = entry.encodeValue(Objects.requireNonNull(tooltipContext.registries()).createSerializationContext(NbtOps.INSTANCE)).getOrThrow();
                var key = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(entry.type());
                assert key != null;
                compoundTag.put(key.toString(), tag);
            }
            tooltip.add(Component.translatable(ModonomiconConstants.I18n.Tooltips.ITEM_NO_BOOK_FOUND_FOR_STACK, NbtUtils.toPrettyComponent(compoundTag)).withStyle(ChatFormatting.DARK_GRAY));
        }
    }


}

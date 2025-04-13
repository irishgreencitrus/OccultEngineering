package io.github.irishgreencitrus.occultengineering.item;

import com.klikli_dev.modonomicon.api.ModonomiconConstants;
import com.klikli_dev.modonomicon.book.Book;
import com.klikli_dev.modonomicon.client.gui.BookGuiManager;
import com.klikli_dev.modonomicon.data.BookDataManager;
import com.klikli_dev.modonomicon.item.ModonomiconItem;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
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
            if (itemInHand.hasTag()) {
                var book = BookDataManager.get().getBook(ENCYCLOPEDIA_OF_SOULS);
                BookGuiManager.get().openBook(book.getId());
            } else {
                OccultEngineering.LOGGER.error("Encyclopedia of Souls: ItemStack has no tag!");
            }
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
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        Book book = BookDataManager.get().getBook(ENCYCLOPEDIA_OF_SOULS);
        if (book != null) {
            if (flagIn.isAdvanced()) {
                tooltip.add(Component.literal("Book ID: ").withStyle(ChatFormatting.DARK_GRAY)
                        .append(Component.literal(ENCYCLOPEDIA_OF_SOULS.toString()).withStyle(ChatFormatting.RED)));
            }

            if (!book.getTooltip().isBlank()) {
                tooltip.add(Component.translatable(book.getTooltip()).withStyle(ChatFormatting.GRAY));
            }

        } else {
            tooltip.add(Component.translatable(ModonomiconConstants.I18n.Tooltips.ITEM_NO_BOOK_FOUND_FOR_STACK,
                            stack.hasTag() ? NbtUtils.toPrettyComponent(Objects.requireNonNull(stack.getTag())) : Component.literal("{}"))
                    .withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        stack.getOrCreateTag().putString(ModonomiconConstants.Nbt.ITEM_BOOK_ID_TAG, ENCYCLOPEDIA_OF_SOULS.toString());
        return super.initCapabilities(stack, nbt);
    }
}

package io.github.irishgreencitrus.occultengineering.item;

import com.klikli_dev.occultism.common.item.spirit.BookOfBindingBoundItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BookOfBindingBoundGlintItem extends BookOfBindingBoundItem {
    public BookOfBindingBoundGlintItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }
}

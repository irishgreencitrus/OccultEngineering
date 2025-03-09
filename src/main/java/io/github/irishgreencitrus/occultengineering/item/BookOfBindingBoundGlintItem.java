package io.github.irishgreencitrus.occultengineering.item;

import com.klikli_dev.occultism.common.item.spirit.BookOfBindingBoundItem;
import net.minecraft.world.item.ItemStack;

public class BookOfBindingBoundGlintItem extends BookOfBindingBoundItem {
    public BookOfBindingBoundGlintItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}

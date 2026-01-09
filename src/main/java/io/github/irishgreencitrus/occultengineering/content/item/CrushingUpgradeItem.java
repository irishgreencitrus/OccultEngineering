package io.github.irishgreencitrus.occultengineering.content.item;

import net.minecraft.world.item.Item;

public class CrushingUpgradeItem extends Item {
    private final int tier;
    public CrushingUpgradeItem(int tier, Properties properties) {
        super(properties);

        this.tier = tier;
    }

    public int getTier() {
        return tier;
    }
}

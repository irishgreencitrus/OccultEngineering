package io.github.irishgreencitrus.occultengineering.content.item;

import net.minecraft.world.item.Item;

public class MechanicalUpgradeItem extends Item {
    private final int tier;
    public MechanicalUpgradeItem(int tier, Properties properties) {
        super(properties);

        this.tier = tier;
    }

    public int getTier() {
        return tier;
    }
}

package io.github.irishgreencitrus.occultengineering.mixin;

import com.simibubi.create.content.schematics.cannon.SchematicannonInventory;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringItems;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SchematicannonInventory.class, remap = false)
public class SchematicannonInventoryMixin {
    @Inject(method = "isItemValid", at = @At(value = "RETURN"), cancellable = true)
    public void isItemValid(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        OccultEngineering.LOGGER.debug("IsItemValid called!");
        if (slot == 0 && OccultEngineeringItems.PENTACLE_SCHEMATIC.isIn(stack)) {
            cir.setReturnValue(true);
        }
    }
}

package io.github.irishgreencitrus.occultengineering.compat.enchant_industry;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class DyeingItemRecipeSerializer implements RecipeSerializer<DyeingItemRecipe> {
    public static final MapCodec<DyeingItemRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance ->
                instance.group(
                        Ingredient.CODEC.fieldOf("input").forGetter(DyeingItemRecipe::getInput),
                        DyeColor.CODEC.fieldOf("dye_color").forGetter(DyeingItemRecipe::getRequiredColor),
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("dye_amount").forGetter(DyeingItemRecipe::getRequiredAmount),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter(DyeingItemRecipe::getOutput)
                ).apply(instance, DyeingItemRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, DyeingItemRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC,
                    DyeingItemRecipe::getInput,
                    DyeColor.STREAM_CODEC,
                    DyeingItemRecipe::getRequiredColor,
                    ByteBufCodecs.INT,
                    DyeingItemRecipe::getRequiredAmount,
                    ItemStack.STREAM_CODEC,
                    DyeingItemRecipe::getOutput,
                    DyeingItemRecipe::new
            );

    @Override
    public MapCodec<DyeingItemRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, DyeingItemRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}

package io.github.irishgreencitrus.occultengineering.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.irishgreencitrus.occultengineering.compat.Mods;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.stream.Stream;

public class JsonDatagenIngredient extends Ingredient {
    private ResourceLocation resourceLocation;

    public JsonDatagenIngredient(ResourceLocation location) {
        super(Stream.empty());
        this.resourceLocation = location;
    }

    public JsonDatagenIngredient(Mods mod, String location) {
        super(Stream.empty());
        this.resourceLocation = mod.rl(location);
    }

    public JsonElement toJson() {
        JsonObject jsonobject = new JsonObject();
        jsonobject.addProperty("item", resourceLocation.toString());
        return jsonobject;
    }

}

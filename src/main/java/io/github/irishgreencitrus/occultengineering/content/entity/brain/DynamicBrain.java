package io.github.irishgreencitrus.occultengineering.content.entity.brain;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import org.jetbrains.annotations.UnknownNullability;

import java.util.List;

public abstract class DynamicBrain<T extends LivingEntity & SmartBrainOwner<T>> implements INBTSerializable<CompoundTag> {
    public T entity;
    public boolean initialised;
    public ResourceLocation brainID;

    public abstract void tick();

    protected abstract void onInit();

    public abstract void onCleanup();

    public DynamicBrain(T entity) {
        this.entity = entity;
    }

    public List<ExtendedSensor<T>> getSensors() {
        return ImmutableList.of();
    }

    public BrainActivityGroup<T> getCoreTasks() {
        return BrainActivityGroup.empty();
    }

    public BrainActivityGroup<T> getIdleTasks() {
        return BrainActivityGroup.empty();
    }


    public void onBrainSetup(Brain<? extends T> brain) {

    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        /*
        var tag = new CompoundTag();
        tag.putString("DynamicBrainID", brainID.toString());
        return tag;
         */
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag) {

    }

}

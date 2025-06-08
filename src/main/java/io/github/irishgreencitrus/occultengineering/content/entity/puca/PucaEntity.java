package io.github.irishgreencitrus.occultengineering.content.entity.puca;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Dynamic;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.content.entity.brain.DynamicBrain;
import io.github.irishgreencitrus.occultengineering.content.entity.brain.DynamicBrainSupplantable;
import io.github.irishgreencitrus.occultengineering.content.entity.puca.brain.PucaBrain;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringBrains;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringEntities;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrain;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PucaEntity extends PathfinderMob implements GeoEntity, SmartBrainOwner<PucaEntity>, DynamicBrainSupplantable {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);


    // This needs to be nullable, as the super() constructor calls some of these
    // methods before we have a chance to set the brain up.
    protected @Nullable PucaBrain dynamicBrain;
    protected ItemStack heldItem;
    protected boolean hasJumped = false;

    protected static final EntityDataAccessor<String> DYNAMIC_BRAIN_ID = SynchedEntityData.defineId(PucaEntity.class, EntityDataSerializers.STRING);

    @SuppressWarnings("unchecked")
    public PucaEntity(EntityType<?> entityType, Level level) {
        super((EntityType<? extends PathfinderMob>) entityType, level);
        heldItem = ItemStack.EMPTY;
        supplantBrain(OccultEngineeringBrains.PUCA_WANDER.get().create(this));
    }

    public PucaEntity(Level level, ItemStack heldItem, BlockState stateToPlace, BlockPos pos) {
        this(OccultEngineeringEntities.PUCA.get(), level);
        this.heldItem = heldItem;
    }

    // Required to make the entity builder happy
    public static PucaEntity genericPuca(EntityType<?> entityType, Level level) {
        return new PucaEntity(entityType, level);
    }

    public void jumpNow() {
        this.jumpControl.jump();
        hasJumped = true;

        triggerAnim("main", "jump");
    }

    public boolean getHasJumped() {
        return hasJumped;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "main", 1, this::animPredicate)
                .triggerableAnim("jump", RawAnimation.begin().thenPlayAndHold("jump")));

    }

    public PlayState animPredicate(AnimationState<PucaEntity> animState) {
        if (!animState.getAnimatable().onGround())
            return PlayState.CONTINUE;
        return animState.setAndContinue(RawAnimation.begin().thenPlay("idle"));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ARMOR, 2.0);
    }


    @Override
    protected Brain.Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this, true);
    }

    @Override
    public void handleAdditionalBrainSetup(SmartBrain<? extends PucaEntity> brain) {
        if (dynamicBrain != null) {
            dynamicBrain.onBrainSetup(brain);
        }
    }

    @Override
    public List<? extends ExtendedSensor<? extends PucaEntity>> getSensors() {
        return dynamicBrain != null ? dynamicBrain.getSensors() : ImmutableList.of();
    }

    @Override
    public BrainActivityGroup<PucaEntity> getCoreTasks() {
        return dynamicBrain != null ? dynamicBrain.getCoreTasks() : BrainActivityGroup.empty();
    }

    @Override
    public BrainActivityGroup<PucaEntity> getIdleTasks() {
        return dynamicBrain != null ? dynamicBrain.getIdleTasks() : BrainActivityGroup.empty();
    }

    public void supplantBrain(DynamicBrain<? extends LivingEntity> newBrain) {
        OccultEngineering.LOGGER.info("Supplanting {}", newBrain.brainID.toString());
        if (dynamicBrain != null) {
            dynamicBrain.onCleanup();
        }
        dynamicBrain = (PucaBrain) newBrain;
        remakeBrain();
    }

    public void remakeBrain() {
        NbtOps nbtops = NbtOps.INSTANCE;
        this.brain = this.makeBrain(new Dynamic<>(nbtops, nbtops.createMap(ImmutableMap.of(nbtops.createString("memories"), nbtops.emptyMap()))));
    }

    @Override
    protected void customServerAiStep() {
        if (dynamicBrain != null) {
            dynamicBrain.tick();
        }
        tickBrain(this);
    }

    @Override
    public void tick() {
        super.tick();
        if (onGround()) {
            hasJumped = false;
        }
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        if (equipmentSlot == EquipmentSlot.MAINHAND)
            return heldItem;
        else return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {
        if (equipmentSlot == EquipmentSlot.MAINHAND) {
            heldItem = itemStack;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DYNAMIC_BRAIN_ID, "");
    }

    @Override
    public CompoundTag serializeNBT() {
        return super.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
    }
}

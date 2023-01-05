package com.littleezra.augment.entity.custom;

import com.littleezra.augment.Augment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Collection;
import java.util.Optional;

public class Scutl extends Monster implements IAnimatable {
    private static final EntityDataAccessor<Optional<BlockState>> DATA_FLOWER_STATE = SynchedEntityData.defineId(Scutl.class, EntityDataSerializers.BLOCK_STATE);
    private int explosionRadius = 1;

    private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public Scutl(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData data, @Nullable CompoundTag nbt) {
        return super.finalizeSpawn(serverLevelAccessor, difficulty, spawnType, data, nbt);
    }

    public static AttributeSupplier setAttributes(){
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 15.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.ATTACK_DAMAGE, 0D)
                .build();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        this.spawnLingeringCloud();
    }

    @Nullable
    public FlowerBlock getFlower(){
        BlockState state = getFlowerState();
        if (state != null && state.getBlock() instanceof FlowerBlock flower){
            return flower;
        }
        return null;
    }
    @Nullable
    public BlockState getFlowerState(){
        return this.entityData.get(DATA_FLOWER_STATE).orElse((BlockState)null);
    }
    public void setFlower(@Nullable BlockState state){
        if (state == null) return;

        if (state.getBlock() instanceof FlowerBlock){
            this.entityData.set(DATA_FLOWER_STATE, Optional.ofNullable(state));
        } else{
            Augment.printDebug("Trying to set flower of " + this.getName().getString() + " to invalid block " + state.getBlock().getName().getString());
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLOWER_STATE, Optional.of(Blocks.LILY_OF_THE_VALLEY.defaultBlockState()));
    }

    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);

        BlockState blockstate = this.getFlowerState();
        if (blockstate != null) {
            nbt.put("flowerBlockState", NbtUtils.writeBlockState(blockstate));
        }

        nbt.putByte("ExplosionRadius", (byte)this.explosionRadius);
    }

    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);

        BlockState blockstate = null;
        if (nbt.contains("flowerBlockState", 10)) {
            blockstate = NbtUtils.readBlockState(nbt.getCompound("flowerBlockState"));
            if (blockstate.isAir()) {
                blockstate = null;
            }
        }

        this.setFlower(blockstate);

        if (nbt.contains("ExplosionRadius", 99)) {
            this.explosionRadius = nbt.getByte("ExplosionRadius");
        }

    }

    @Override
    public boolean doHurtTarget(Entity target) {
        explodeScutl();
        return true;
    }

    private void explodeScutl() {
        if (!this.level.isClientSide) {
            Explosion.BlockInteraction explosion$blockinteraction = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
            float f = 1.0f;
            this.dead = true;
            this.level.explode(this, this.getX(), this.getY(), this.getZ(), (float)this.explosionRadius * f, explosion$blockinteraction);
            this.discard();
            this.spawnLingeringCloud();
        }

    }

    private void spawnLingeringCloud() {
        Collection<MobEffectInstance> collection = this.getActiveEffects();
        if (getFlower() != null) {
            AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
            areaeffectcloud.setRadius(2.5f);
            areaeffectcloud.setRadiusOnUse(-0.5F);
            areaeffectcloud.setWaitTime(10);
            areaeffectcloud.setDuration(areaeffectcloud.getDuration() / 2);
            areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float)areaeffectcloud.getDuration());

            areaeffectcloud.addEffect(new MobEffectInstance(getFlower().getSuspiciousStewEffect(), 100, 0));

            for(MobEffectInstance mobeffectinstance : collection) {
                areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
            }

            this.level.addFreshEntity(areaeffectcloud);
        }

    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event){
        if (event.isMoving()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("scutl.animation.walk", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("scutl.animation.idle", ILoopType.EDefaultLoopTypes.LOOP));

        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}

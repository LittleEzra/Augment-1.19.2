package com.littleezra.augment.entity.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.EnumSet;

public class Sentinel extends Monster implements IAnimatable {

    private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private LivingEntity currentTarget;
    private int attackDelay;
    private int animationTime;
    private boolean attacking;
    private boolean playingAttack;

    private final int ATTACK_DELAY_TIME = 15;
    private final int ANIMATION_TIME = 30;

    public Sentinel(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier setAttributes(){
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 10D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 32D)
                .build();
    }

    @Override
    public void tick() {
        super.tick();

        if (this.playingAttack) animationTime++;

        if (animationTime >= ANIMATION_TIME){
            this.animationTime = 0;
            this.playingAttack = false;
        }

        if (currentTarget == null) return;

        if (currentTarget.isDeadOrDying()){
            currentTarget = null;
            this.attacking = false;
            this.attackDelay = 0;
        }

        if (this.attacking) attackDelay++;

        if (attackDelay >= ATTACK_DELAY_TIME){
            this.attacking = false;
            this.attackDelay = 0;
            if (this.distanceToSqr(currentTarget) < this.getMeleeHurtRangeSqr(currentTarget)) this.doHurtTarget(currentTarget);
            currentTarget = null;
        }
    }

    public double getMeleeHurtRangeSqr(LivingEntity entity){
        return this.getMeleeAttackRangeSqr(entity) * 2;
    }

    public void stopAttack(){
        currentTarget = null;
        this.attacking = false;
        this.playingAttack = false;
    }

    @Override
    public float getSpeed() {
        if (this.playingAttack) return 0f;

        return super.getSpeed();
    }

    public void startAttack(LivingEntity target){
        this.currentTarget = target;
        this.attacking = true;
        this.playingAttack = true;
        target.sendSystemMessage(Component.literal("Starting Sentinel Attack"));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SentinelAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.7D));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
        data.addAnimationController(new AnimationController(this, "attackController",
                0, this::attackPredicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event){
        if (event.isMoving()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("sentinel.animation.walk", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("sentinel.animation.idle", ILoopType.EDefaultLoopTypes.LOOP));

        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable>PlayState attackPredicate(AnimationEvent<E> event){
        if (event.getController().getAnimationState().equals(AnimationState.Stopped)){
            if (this.swinging){
                event.getController().markNeedsReload();
                event.getController().setAnimation(new AnimationBuilder().addAnimation("sentinel.animation.attack", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
                this.swinging = false;
            }
        }

        return PlayState.CONTINUE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.NETHERITE_BLOCK_BREAK;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    public class SentinelAttackGoal extends Goal{
        protected final Sentinel mob;
        private final double speedModifier;
        private final boolean followingTargetEvenIfNotSeen;
        private Path path;
        private double pathedTargetX;
        private double pathedTargetY;
        private double pathedTargetZ;
        private int ticksUntilNextPathRecalculation;
        private int ticksUntilNextAttack;
        private final int attackInterval = 20;
        private long lastCanUseCheck;
        private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L;
        private int failedPathFindingPenalty = 0;
        private boolean canPenalize = false;

        public SentinelAttackGoal(Sentinel mob, double speedModifier, boolean followIfNotSeen) {
            this.mob = mob;
            this.speedModifier = speedModifier;
            this.followingTargetEvenIfNotSeen = followIfNotSeen;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            long i = this.mob.level.getGameTime();
            if (i - this.lastCanUseCheck < 20L) {
                return false;
            } else {
                this.lastCanUseCheck = i;
                LivingEntity livingentity = this.mob.getTarget();
                if (livingentity == null) {
                    return false;
                } else if (!livingentity.isAlive()) {
                    return false;
                } else {
                    if (canPenalize) {
                        if (--this.ticksUntilNextPathRecalculation <= 0) {
                            this.path = this.mob.getNavigation().createPath(livingentity, 0);
                            this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                            return this.path != null;
                        } else {
                            return true;
                        }
                    }
                    this.path = this.mob.getNavigation().createPath(livingentity, 0);
                    if (this.path != null) {
                        return true;
                    } else {
                        return this.getAttackReachSqr(livingentity) >= this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                    }
                }
            }
        }

        public boolean canContinueToUse() {
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else if (!this.followingTargetEvenIfNotSeen) {
                return !this.mob.getNavigation().isDone();
            } else if (!this.mob.isWithinRestriction(livingentity.blockPosition())) {
                return false;
            } else {
                return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player)livingentity).isCreative();
            }
        }

        public void start() {
            this.mob.getNavigation().moveTo(this.path, this.speedModifier);
            this.mob.setAggressive(true);
            this.ticksUntilNextPathRecalculation = 0;
            this.ticksUntilNextAttack = 0;
        }

        public void stop() {
            LivingEntity livingentity = this.mob.getTarget();
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
                this.mob.setTarget((LivingEntity)null);
            }

            this.mob.setAggressive(false);
            this.mob.getNavigation().stop();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity != null) {
                this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                double d0 = this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
                if ((this.followingTargetEvenIfNotSeen || this.mob.getSensing().hasLineOfSight(livingentity)) && this.ticksUntilNextPathRecalculation <= 0 && (this.pathedTargetX == 0.0D && this.pathedTargetY == 0.0D && this.pathedTargetZ == 0.0D || livingentity.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0D || this.mob.getRandom().nextFloat() < 0.05F)) {
                    this.pathedTargetX = livingentity.getX();
                    this.pathedTargetY = livingentity.getY();
                    this.pathedTargetZ = livingentity.getZ();
                    this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                    if (this.canPenalize) {
                        this.ticksUntilNextPathRecalculation += failedPathFindingPenalty;
                        if (this.mob.getNavigation().getPath() != null) {
                            net.minecraft.world.level.pathfinder.Node finalPathPoint = this.mob.getNavigation().getPath().getEndNode();
                            if (finalPathPoint != null && livingentity.distanceToSqr(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
                                failedPathFindingPenalty = 0;
                            else
                                failedPathFindingPenalty += 10;
                        } else {
                            failedPathFindingPenalty += 10;
                        }
                    }
                    if (d0 > 1024.0D) {
                        this.ticksUntilNextPathRecalculation += 10;
                    } else if (d0 > 256.0D) {
                        this.ticksUntilNextPathRecalculation += 5;
                    }

                    if (!this.mob.getNavigation().moveTo(livingentity, this.speedModifier)) {
                        this.ticksUntilNextPathRecalculation += 15;
                    }

                    this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
                }

                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
                this.checkAndPerformAttack(livingentity, d0);
            }
        }

        protected void checkAndPerformAttack(LivingEntity target, double distance) {
            double d0 = this.getAttackReachSqr(target);
            if (!this.mob.attacking && !this.mob.playingAttack && distance <= d0 && this.ticksUntilNextAttack <= 0) {
                this.resetAttackCooldown();
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.startAttack(target);
            }

        }

        protected void resetAttackCooldown() {
            this.ticksUntilNextAttack = this.adjustedTickDelay(20);
        }

        protected boolean isTimeToAttack() {
            return this.ticksUntilNextAttack <= 0;
        }

        protected int getTicksUntilNextAttack() {
            return this.ticksUntilNextAttack;
        }

        protected int getAttackInterval() {
            return this.adjustedTickDelay(20);
        }

        protected double getAttackReachSqr(LivingEntity p_25556_) {
            return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + p_25556_.getBbWidth());
        }
    }
}

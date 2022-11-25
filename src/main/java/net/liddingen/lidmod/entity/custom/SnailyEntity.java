package net.liddingen.lidmod.entity.custom;

import net.liddingen.lidmod.entity.ModEntityTypes;
import net.liddingen.lidmod.item.ModItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.FrogAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.UUID;

public class SnailyEntity extends Animal implements IAnimatable, IAnimationTickable, Bucketable{
    //Breeding
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(Turtle.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LAYING_EGG = SynchedEntityData.defineId(Turtle.class, EntityDataSerializers.BOOLEAN);
    int layEggCounter;
    //Breeding

    //Converting
    @Nullable
    private UUID conversionStarter;
    private int snailEntityConversionTime;
    private static final int SNAIL_ENTITY_CONVERSION_WAIT_MIN = 1000;
    private static final int SNAIL_ENTITY_CONVERSION_WAIT_MAX = 2000;
    private static final EntityDataAccessor<Boolean> DATA_CONVERTING_ID = SynchedEntityData.defineId(SnailyEntity.class, EntityDataSerializers.BOOLEAN);
    private int snailXp;
    //Converting
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Axolotl.class, EntityDataSerializers.BOOLEAN);

    private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    //Climbing
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(SnailEntity.class, EntityDataSerializers.BYTE);
    //Climbing

    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.OAK_LEAVES, Items.BIRCH_LEAVES, Items.SPRUCE_LEAVES, Items.DARK_OAK_LEAVES, Items.OAK_LEAVES,
            Items.ACACIA_LEAVES, Items.JUNGLE_LEAVES, Items.MANGROVE_LEAVES);

    public SnailyEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier setAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5.0D)
                .add(Attributes.ATTACK_DAMAGE, 1f)
                .add(Attributes.ATTACK_SPEED, 1f)
                .add(Attributes.MOVEMENT_SPEED, 0.1f).build();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new TemptGoal(this, 1.25D, Ingredient.of(Items.ENCHANTED_GOLDEN_APPLE), false));
        this.goalSelector.addGoal(2, new PanicGoal(this, 2.0D));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(1, new SnailyEntity.SnailLayEggGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Frog.class, true));
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this)).setAlertOthers());
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.snaily.walk", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        } else {

            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.snaily.idle", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
    }

    private PlayState attackPredicate(AnimationEvent event) {
        if(this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.snaily.attack", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            this.swinging = false;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
        data.addAnimationController(new AnimationController(this, "attackController", 0, this::attackPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public int tickTimer() {
        return tickCount;
    }

    public boolean isFood(ItemStack p_29508_) {
        return FOOD_ITEMS.test(p_29508_);
    }

    /*
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return null;
    }

    public boolean isFood(ItemStack itemStack) {
        return Ingredient.of(Items.ENCHANTED_GOLDEN_APPLE).test(itemStack);
    }*/


    public void setTarget(@Nullable LivingEntity livingEntity) {

        if (livingEntity instanceof Player) {
            this.setLastHurtByPlayer((Player)livingEntity);
        }

        super.setTarget(livingEntity);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SLIME_BLOCK_STEP;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SLIME_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SLIME_DEATH;
    }

    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
        this.playSound(SoundEvents.SLIME_BLOCK_STEP, 0.15F, 1.0F);
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HAS_EGG, false);
        this.entityData.define(LAYING_EGG, false);
        this.entityData.define(DATA_CONVERTING_ID, false);
        this.entityData.define(FROM_BUCKET, false);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("FromBucket", this.fromBucket());
        tag.putInt("ConversionTime", this.isConverting() ? this.snailEntityConversionTime : -1);
        if (this.conversionStarter != null) {
            tag.putUUID("ConversionPlayer", this.conversionStarter);
        }
        tag.putInt("Xp", this.snailXp);
        tag.putBoolean("HasEgg", this.hasEgg());
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setFromBucket(tag.getBoolean("FromBucket"));

        if (tag.contains("ConversionTime", 99) && tag.getInt("ConversionTime") > -1) {
            this.startConverting(tag.hasUUID("ConversionPlayer") ? tag.getUUID("ConversionPlayer") : null, tag.getInt("ConversionTime"));
        }

        if (tag.contains("Xp", 3)) {
            this.snailXp = tag.getInt("Xp");
        }
        this.setHasEgg(tag.getBoolean("HasEgg"));
    }

    public void tick() {
        if (!this.level.isClientSide && this.isAlive() && this.isConverting()) {
            int i = this.getConversionProgress();
            this.snailEntityConversionTime -= i;
            if (this.snailEntityConversionTime <= 0 && net.minecraftforge.event.ForgeEventFactory.canLivingConvert(this, ModEntityTypes.SNAIL.get(), (timer) -> this.snailEntityConversionTime = timer)) {
                this.finishConversion((ServerLevel)this.level);
            }
        }

        super.tick();
    }


    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        if(isFood(itemstack)) { //
            return super.mobInteract(player, interactionHand); //
        }
         if (itemstack.is(Items.ENCHANTED_GOLDEN_APPLE) && !isBaby()) {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (!this.level.isClientSide) {
                    this.startConverting(player.getUUID(), this.random.nextInt(2401) + 3600);
                }
                return InteractionResult.SUCCESS;

        } else {
            //return super.mobInteract(player, interactionHand);
            return Bucketable.bucketMobPickup(player, interactionHand, this).orElse(super.mobInteract(player, interactionHand));
        }
    }

    private void startConverting(@Nullable UUID uuid, int i) {
        this.conversionStarter = uuid;
        this.snailEntityConversionTime = i;
        this.getEntityData().set(DATA_CONVERTING_ID, true);
        this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, i, Math.min(this.level.getDifficulty().getId() - 1, 0)));
        this.level.broadcastEntityEvent(this, (byte)16);
    }
    public boolean removeWhenFarAway(double v) {
        return !this.isConverting() && this.snailXp == 0;
    }

    public boolean isConverting() {
        return this.getEntityData().get(DATA_CONVERTING_ID);
    }

    public void handleEntityEvent(byte b) {
        if (b == 16) {
            if (!this.isSilent()) {
                this.level.playLocalSound(this.getX(), this.getEyeY(), this.getZ(), SoundEvents.SLIME_JUMP, this.getSoundSource(), 1.0F + this.random.nextFloat(), this.random.nextFloat() * 0.7F + 0.3F, false);
            }

        } else {
            super.handleEntityEvent(b);
        }
    }

    private void finishConversion(ServerLevel serverLevel) {
        SnailEntity snailEntity = this.convertTo(ModEntityTypes.SNAIL.get(), false);

        snailEntity.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(snailEntity.blockPosition()), MobSpawnType.CONVERSION, (SpawnGroupData)null, (CompoundTag)null);
        if (this.conversionStarter != null) {
            Player player = serverLevel.getPlayerByUUID(this.conversionStarter);
        }
        if (!this.isSilent()) {
            serverLevel.levelEvent((Player)null, 1027, this.blockPosition(), 0);
        }
        net.minecraftforge.event.ForgeEventFactory.onLivingConvert(this, snailEntity);
    }

    private int getConversionProgress() {
        int i = 1;
        if (this.random.nextFloat() < 0.01F) {
            int j = 0;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for(int k = (int)this.getX() - 4; k < (int)this.getX() + 4 && j < 14; ++k) {
                for(int l = (int)this.getY() - 4; l < (int)this.getY() + 4 && j < 14; ++l) {
                    for(int i1 = (int)this.getZ() - 4; i1 < (int)this.getZ() + 4 && j < 14; ++i1) {
                        BlockState blockstate = this.level.getBlockState(blockpos$mutableblockpos.set(k, l, i1));
                        if (blockstate.is(Blocks.IRON_BARS) || blockstate.getBlock() instanceof BedBlock) {
                            if (this.random.nextFloat() < 0.3F) {
                                ++i;
                            }

                            ++j;
                        }
                    }
                }
            }
        }

        return i;
    }

    //ende

    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return this.isBaby() ? dimensions.height * 0.95F : 0.25F;
    }

        //Snail Bowl
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        if (mobSpawnType == MobSpawnType.BUCKET) {
            return spawnGroupData;
        }
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
    }

    protected void usePlayerItem(Player player, InteractionHand interactionHand, ItemStack itemStack) {
        if (itemStack.is(Items.TROPICAL_FISH_BUCKET)) {
            player.setItemInHand(interactionHand, new ItemStack(Items.WATER_BUCKET));
        } else {
            super.usePlayerItem(player, interactionHand, itemStack);
        }

    }

    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }


    public void setFromBucket(boolean p_148834_) {
        this.entityData.set(FROM_BUCKET, p_148834_);
    }
    @Deprecated
    public void saveToBucketTag(ItemStack p_149187_) {
        Bucketable.saveDefaultDataToBucketTag(this, p_149187_);
    }
    @Deprecated
    public void loadFromBucketTag(CompoundTag p_149163_) {
        Bucketable.loadDefaultDataFromBucketTag(this, p_149163_);
    }
    @Deprecated
    public ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.SNAIL_BUCKET.get());
    }

    public SoundEvent getPickupSound() {
        return SoundEvents.SLIME_SQUISH_SMALL;
    }

    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromBucket();
    }

    //breeding
   /* @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob mob) {
        return ModEntityTypes.SNAILY.get().create(serverLevel);
    }*/

    public boolean hasEgg() {
        return this.entityData.get(HAS_EGG);
    }

    void setHasEgg(boolean pHasEgg) {
        this.entityData.set(HAS_EGG, pHasEgg);
    }

    public boolean isLayingEgg() {
        return this.entityData.get(LAYING_EGG);
    }

    void setLayingEgg(boolean pIsLayingEgg) {
        this.layEggCounter = pIsLayingEgg ? 1 : 0;
        this.entityData.set(LAYING_EGG, pIsLayingEgg);
    }
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        SnailyEntity snaily = ModEntityTypes.SNAILY.get().create(pLevel);
        /*if (snaily != null) {
            FrogAi.initMemories(snaily, pLevel.getRandom());
        }*/

        return snaily;
    }

    public boolean isBaby() {
        return false;
    }

    public void setBaby(boolean pBaby) {
    }

    public void spawnChildFromBreeding(ServerLevel pLevel, Animal pMate) {
        ServerPlayer serverplayer = this.getLoveCause();
        if (serverplayer == null) {
            serverplayer = pMate.getLoveCause();
        }

        if (serverplayer != null) {
            serverplayer.awardStat(Stats.ANIMALS_BRED);
            CriteriaTriggers.BRED_ANIMALS.trigger(serverplayer, this, pMate, (AgeableMob)null);
        }

        this.setAge(6000);
        pMate.setAge(6000);
        this.resetLove();
        pMate.resetLove();
        this.getBrain().setMemory(MemoryModuleType.IS_PREGNANT, Unit.INSTANCE);
        pLevel.broadcastEntityEvent(this, (byte)18);
        if (pLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            pLevel.addFreshEntity(new ExperienceOrb(pLevel, this.getX(), this.getY(), this.getZ(), this.getRandom().nextInt(7) + 1));
        }

    }
    static class SnailLayEggGoal extends MoveToBlockGoal {
        private final SnailyEntity snaily;

        SnailLayEggGoal(SnailyEntity pSnaily, double pSpeedModifier) {
            super(pSnaily, pSpeedModifier, 16);
            this.snaily = pSnaily;
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return this.snaily.hasEgg() ? super.canUse() : false;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return super.canContinueToUse() && this.snaily.hasEgg();
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            super.tick();
            BlockPos blockpos = this.snaily.blockPosition();
            if (!this.snaily.isInWater() && this.isReachedTarget()) {
                if (this.snaily.layEggCounter < 1) {
                    this.snaily.setLayingEgg(true);
                } else if (this.snaily.layEggCounter > this.adjustedTickDelay(200)) {
                    Level level = this.snaily.level;
                    level.playSound((Player)null, blockpos, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3F, 0.9F + level.random.nextFloat() * 0.2F);
                    level.setBlock(this.blockPos.above(), Blocks.TURTLE_EGG.defaultBlockState().setValue(TurtleEggBlock.EGGS, Integer.valueOf(this.snaily.random.nextInt(4) + 1)), 3);
                    this.snaily.setHasEgg(false);
                    this.snaily.setLayingEgg(false);
                    this.snaily.setInLoveTime(600);
                }

                if (this.snaily.isLayingEgg()) {
                    ++this.snaily.layEggCounter;
                }
            }

        }

        /**
         * Return true to set given position as destination
         */
        protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
            return !pLevel.isEmptyBlock(pPos.above()) ? false : TurtleEggBlock.isSand(pLevel, pPos);
        }
    }


    /*//Climbing
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }
    }

    public boolean onClimbable() {
        return this.isClimbing();
    }

    protected PathNavigation createNavigation(Level level) {
        return new WallClimberNavigation(this, level);
    }

    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setClimbing(boolean p_33820_) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (p_33820_) {
            b0 = (byte)(b0 | 1);
        } else {
            b0 = (byte)(b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }
    //End */

}

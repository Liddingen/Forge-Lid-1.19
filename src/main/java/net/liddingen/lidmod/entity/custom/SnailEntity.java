package net.liddingen.lidmod.entity.custom;

import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import net.liddingen.lidmod.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.features.EndFeatures;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
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
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
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

import java.util.Set;
import java.util.UUID;

public class SnailEntity extends Animal implements ItemSteerable, Saddleable, IAnimatable, IAnimationTickable, NeutralMob {
    /*Container
    private static final EntityDataAccessor<Boolean> DATA_ID_CHEST = SynchedEntityData.defineId(AbstractChestedHorse.class, EntityDataSerializers.BOOLEAN);
    public static final int INV_CHEST_COUNT = 15;
    Container*/

    //Thunder
    private static final Logger LOGGER = LogUtils.getLogger();
    //Thunder

    //Climbing
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(SnailEntity.class, EntityDataSerializers.BYTE);
    //Climbing

    //Saddle

    private boolean inputLeft;
    private boolean inputRight;
    private boolean inputUp;
    private boolean inputDown;
    private float deltaRotation;

    private static final EntityDataAccessor<Boolean> DATA_SADDLE_ID = SynchedEntityData.defineId(SnailEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_BOOST_TIME = SynchedEntityData.defineId(SnailEntity.class, EntityDataSerializers.INT);
    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.OAK_LEAVES, Items.BIRCH_LEAVES, Items.SPRUCE_LEAVES, Items.DARK_OAK_LEAVES, Items.OAK_LEAVES,
            Items.ACACIA_LEAVES, Items.JUNGLE_LEAVES, Items.MANGROVE_LEAVES);
    private final ItemBasedSteering steering = new ItemBasedSteering(this.entityData, DATA_BOOST_TIME, DATA_SADDLE_ID);
    //Saddle
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    //Anger
    private int remainingPersistentAngerTime;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    @Nullable
    private UUID persistentAngerTarget;
    private int playFirstAngerSoundIn;
    private static final UniformInt FIRST_ANGER_SOUND_DELAY = TimeUtil.rangeOfSeconds(0, 1);


    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.snail.walk", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        } else {

            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.snail.idle", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
    }

    private PlayState attackPredicate(AnimationEvent event) {
        if (this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.snail.attack", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            this.swinging = false;
        }
        return PlayState.CONTINUE;
    }


    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
        data.addAnimationController(new AnimationController(this, "attackController", 0, this::attackPredicate));
    }

    public SnailEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier setAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.ATTACK_DAMAGE, 10f)
                .add(Attributes.ATTACK_SPEED, 0.5f)
                .add(Attributes.MOVEMENT_SPEED, 0.1f).build();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        //this.goalSelector.addGoal(3, new TemptGoal(this, 1.2D, Ingredient.of(Items.CARROT_ON_A_STICK), false)); //Noch ändern
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.2D, FOOD_ITEMS, false));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Frog.class, true));
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    public double getMeleeAttackRangeSqr(LivingEntity livingEntity) {
        return 0.01D + (double) livingEntity.getBbWidth() * 0.01D;
    }

    //Anger
    private void playAngerSound() {
        this.playSound(SoundEvents.SLIME_HURT, this.getSoundVolume() * 2.0F, this.getVoicePitch() * 1.8F);
    } //not Working

    public void setTarget(@Nullable LivingEntity p_34478_) {

        if (p_34478_ instanceof Player) {
            this.setLastHurtByPlayer((Player) p_34478_);
        }

        super.setTarget(p_34478_);
    }

    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    public void setRemainingPersistentAngerTime(int p_34448_) {
        this.remainingPersistentAngerTime = p_34448_;
    }


    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID p_34444_) {
        this.persistentAngerTarget = p_34444_;
    }

    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }


    //saddle
   /* public LivingEntity getControllingPassenger() {
        LivingEntity LivingEntity = (net.minecraft.world.entity.LivingEntity) this.getFirstPassenger();
        return LivingEntity != null && this.canBeControlledBy(LivingEntity) ? LivingEntity : null;
    }*/

    @Nullable
    public LivingEntity getControllingPassenger() {
        if (this.isSaddled()) {
            Entity entity = this.getFirstPassenger();
            if (entity instanceof LivingEntity) {
                return (LivingEntity)entity;
            }
        }
        return null;
    }
    protected boolean canAddPassenger(Entity pPassenger) {
        return this.getPassengers().size() < this.getMaxPassengers();
    }

    protected int getMaxPassengers() {
        return 4;
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        }
    /*private boolean canBeControlledBy(LivingEntity p_218248_) {
        if (this.isSaddled() && p_218248_ instanceof Player player) {
            return player.getMainHandItem().is(Items.CARROT_ON_A_STICK) || player.getOffhandItem().is(Items.CARROT_ON_A_STICK);
        } else {
            return false;
        }
    }*/


    public void onSyncedDataUpdated(EntityDataAccessor<?> p_29480_) {
        if (DATA_BOOST_TIME.equals(p_29480_) && this.level.isClientSide) {
            this.steering.onSynced();
        }

        super.onSyncedDataUpdated(p_29480_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SADDLE_ID, false);
        this.entityData.define(DATA_BOOST_TIME, 0);
        this.entityData.define(DATA_FLAGS_ID, (byte) 0); //Climbing
        // this.entityData.define(DATA_ID_CHEST, false);//Container
    }

    public void tick() { //Climbing
        super.tick();
        if (!this.level.isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }

    }

    public boolean onClimbable() {
        return this.isClimbing();
    } //Climbing


    public void addAdditionalSaveData(CompoundTag p_29495_) {
        super.addAdditionalSaveData(p_29495_);
        this.steering.addAdditionalSaveData(p_29495_);
        this.addPersistentAngerSaveData(p_29495_); //Anger
        /*p_29495_.putBoolean("ChestedHorse", this.hasChest()); //Container
        if (this.hasChest()) { //Container
            ListTag listtag = new ListTag();

            for(int i = 2; i < this.inventory.getContainerSize(); ++i) {
                ItemStack itemstack = this.inventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    CompoundTag compoundtag = new CompoundTag();
                    compoundtag.putByte("Slot", (byte)i);
                    itemstack.save(compoundtag);
                    listtag.add(compoundtag);
                }
            }

            p_29495_.put("Items", listtag);
        }*/
    }

    public void readAdditionalSaveData(CompoundTag p_29478_) {
        super.readAdditionalSaveData(p_29478_);
        this.steering.readAdditionalSaveData(p_29478_);
        this.readPersistentAngerSaveData(this.level, p_29478_); //Anger
       /* this.setChest(p_29478_.getBoolean("ChestedHorse")); //Container
        this.createInventory();
        if (this.hasChest()) {
            ListTag listtag = p_29478_.getList("Items", 10);

            for (int i = 0; i < listtag.size(); ++i) {
                CompoundTag compoundtag = listtag.getCompound(i);
                int j = compoundtag.getByte("Slot") & 255;
                if (j >= 2 && j < this.inventory.getContainerSize()) {
                    this.inventory.setItem(j, ItemStack.of(compoundtag));
                }
            }
        }
        this.updateContainerEquipment();*/
    }

    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {

        boolean flag = this.isFood(player.getItemInHand(interactionHand));
        if (!flag && this.isSaddled() && !this.isVehicle() && !player.isSecondaryUseActive()) {
            if (!this.level.isClientSide) {
                player.startRiding(this);
            }

            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else {
            InteractionResult interactionresult = super.mobInteract(player, interactionHand);
            if (!interactionresult.consumesAction()) {
                ItemStack itemstack = player.getItemInHand(interactionHand);
                return itemstack.is(Items.SADDLE) ? itemstack.interactLivingEntity(player, this, interactionHand) : InteractionResult.PASS;
            } else {
                return interactionresult;
            }
        }
    }

    public boolean isSaddleable() {
        return this.isAlive() && !this.isBaby();
    }

    protected void dropEquipment() {
        super.dropEquipment();
        if (this.isSaddled()) {
            this.spawnAtLocation(Items.SADDLE);
        }
        /*if (this.hasChest()) { //Container
            if (!this.level.isClientSide) {
                this.spawnAtLocation(Blocks.CHEST);
            }

            this.setChest(false);
        } */

    }

    public boolean isSaddled() {
        return this.steering.hasSaddle();
    }

    public void equipSaddle(@Nullable SoundSource p_29476_) {
        this.steering.setSaddle(true);
        if (p_29476_ != null) {
            this.level.playSound((Player) null, this, SoundEvents.HORSE_SADDLE, p_29476_, 0.5F, 1.0F);
        }

    }

    //
    public void positionRider(Entity p_30830_) {
        if (this.hasPassenger(p_30830_)) {
            float f = Mth.cos(this.yBodyRot * ((float) Math.PI / 180F));
            float f1 = Mth.sin(this.yBodyRot * ((float) Math.PI / 180F));
            p_30830_.setPos(this.getX() + (double) (-2.5F * f1),
                    this.getY() + this.getPassengersRidingOffset() + p_30830_.getMyRidingOffset(), this.getZ() - (double) (-2.5F * f));
        }
    }

    public double getPassengersRidingOffset() {
        return (double) this.getBbHeight() * 1F;
    }


    public boolean causeFallDamage(float p_149538_, float p_149539_, DamageSource p_149540_) {
        int i = this.calculateFallDamage(p_149538_, p_149539_);
        if (i <= 0) {
            return false;
        } else {
            if (p_149538_ >= 10.0F) {
                this.hurt(p_149540_, (float) i);
                if (this.isVehicle()) {
                    for (Entity entity : this.getIndirectPassengers()) {
                        entity.hurt(p_149540_, (float) i);
                    }
                }
            }

            this.playBlockFallSound();
            return true;
        }
    }


    public Vec3 getDismountLocationForPassenger(LivingEntity p_33908_) {
        Vec3[] avec3 = new Vec3[]{getCollisionHorizontalEscapeVector((double) this.getBbWidth(), (double) p_33908_.getBbWidth(), p_33908_.getYRot()), getCollisionHorizontalEscapeVector((double) this.getBbWidth(), (double) p_33908_.getBbWidth(), p_33908_.getYRot() - 22.5F), getCollisionHorizontalEscapeVector((double) this.getBbWidth(), (double) p_33908_.getBbWidth(), p_33908_.getYRot() + 22.5F), getCollisionHorizontalEscapeVector((double) this.getBbWidth(), (double) p_33908_.getBbWidth(), p_33908_.getYRot() - 45.0F), getCollisionHorizontalEscapeVector((double) this.getBbWidth(), (double) p_33908_.getBbWidth(), p_33908_.getYRot() + 45.0F)};
        Set<BlockPos> set = Sets.newLinkedHashSet();
        double d0 = this.getBoundingBox().maxY;
        double d1 = this.getBoundingBox().minY - 0.5D;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (Vec3 vec3 : avec3) {
            blockpos$mutableblockpos.set(this.getX() + vec3.x, d0, this.getZ() + vec3.z);

            for (double d2 = d0; d2 > d1; --d2) {
                set.add(blockpos$mutableblockpos.immutable());
                blockpos$mutableblockpos.move(Direction.DOWN);
            }
        }

        for (BlockPos blockpos : set) {
            if (!this.level.getFluidState(blockpos).is(FluidTags.LAVA)) {
                double d3 = this.level.getBlockFloorHeight(blockpos);
                if (DismountHelper.isBlockFloorValid(d3)) {
                    Vec3 vec31 = Vec3.upFromBottomCenterOf(blockpos, d3);

                    for (Pose pose : p_33908_.getDismountPoses()) {
                        AABB aabb = p_33908_.getLocalBoundsForPose(pose);
                        if (DismountHelper.canDismountTo(this.level, p_33908_, aabb.move(vec31))) {
                            p_33908_.setPose(pose);
                            return vec31;
                        }
                    }
                }
            }
        }


        return new Vec3(this.getX(), this.getBoundingBox().maxY, this.getZ());
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 3.4F;
    }

    public Vec3 getLeashOffset() {
        return new Vec3(0.1D, (double) (0.3F * this.getEyeHeight()), (double) (this.getBbWidth() * 0.3F));
    }

    public int getExperienceReward() {
        return 1 + this.level.random.nextInt(20);
    }


    public void travel(Vec3 p_29506_) {
        this.travel(this, this.steering, p_29506_);
    }

    public boolean isFood(ItemStack p_29508_) {
        return FOOD_ITEMS.test(p_29508_);
    }

    public float getSteeringSpeed() {
        return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.225F;
    }

    public void travelWithInput(Vec3 p_29482_) {
        super.travel(p_29482_);
    }

    public boolean boost() {
        return this.steering.boost(this.getRandom());
    }
//Saddle zuende

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
        return null;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public int tickTimer() {
        return tickCount;
    }


    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.SLIME_BLOCK_STEP, 0.15F, 1.0F);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SLIME_BLOCK_FALL;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.SLIME_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SLIME_DEATH;
    }

    protected float getSoundVolume() {
        return 0.2F;
    }

//Climbing

    protected PathNavigation createNavigation(Level p_33802_) {
        return new WallClimberNavigation(this, p_33802_);
    }

    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setClimbing(boolean p_33820_) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (p_33820_) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    //Thunder Hit

    public void thunderHit(ServerLevel p_35409_, LightningBolt p_35410_) {
        addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1440, 60));
        super.thunderHit(p_35409_, p_35410_);
    }
    //Thunder HIt

    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide) {
            int i = Mth.floor(this.getX());
            int j = Mth.floor(this.getY());
            int k = Mth.floor(this.getZ());
            BlockPos blockpos = new BlockPos(i, j, k);
            Biome biome = this.level.getBiome(blockpos).value();
            if (biome.shouldSnowGolemBurn(blockpos)) {
                this.hurt(DamageSource.ON_FIRE, 1.0F);
            }

            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
                return;
            }

            BlockState blockstate = ModBlocks.SLIME_TRAIL.get().defaultBlockState();

            for (int l = 0; l < 4; ++l) {
                i = Mth.floor(this.getX() + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
                j = Mth.floor(this.getY());
                k = Mth.floor(this.getZ() + (double) ((float) (l / 2 % 2 * 2 - 1) * 0.25F));
                BlockPos blockpos1 = new BlockPos(i, j, k);
                if (this.level.isEmptyBlock(blockpos1) && blockstate.canSurvive(this.level, blockpos1)) {
                    this.level.setBlockAndUpdate(blockpos1, blockstate);
                    this.level.gameEvent(GameEvent.BLOCK_PLACE, blockpos1, GameEvent.Context.of(this, blockstate));
                }
            }
        }
    }
    //Ende

    //SchneckenHaus
/*
    public void kill() {
        this.remove(Entity.RemovalReason.KILLED);
        this.gameEvent(GameEvent.ENTITY_DIE);
        if (this.dragonFight != null) {
            this.dragonFight.updateDragon(this);
            this.dragonFight.setDragonKilled(this);
        }

    }



    public void setDragonKilled(EnderDragon p_64086_) {
        if (p_64086_.getUUID().equals(this.dragonUUID)) {
            this.dragonEvent.setProgress(0.0F);
            this.dragonEvent.setVisible(false);
            this.spawnExitPortal(true);
            this.spawnNewGateway();
            if (!this.previouslyKilled) {
                this.level.setBlockAndUpdate(this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, EndPodiumFeature.END_PODIUM_LOCATION), Blocks.DRAGON_EGG.defaultBlockState());
            }

            this.previouslyKilled = true;
            this.dragonKilled = true;
        }

    }

    private void spawnNewGateway() {
        if (!this.gateways.isEmpty()) {
            int i = this.gateways.remove(this.gateways.size() - 1);
            int j = Mth.floor(96.0D * Math.cos(2.0D * (-Math.PI + 0.15707963267948966D * (double)i)));
            int k = Mth.floor(96.0D * Math.sin(2.0D * (-Math.PI + 0.15707963267948966D * (double)i)));
            this.spawnNewGateway(new BlockPos(j, 75, k));
        }
    }

    private void spawnNewGateway(BlockPos p_64090_) {
        this.level.levelEvent(3000, p_64090_, 0);
        EndFeatures.END_GATEWAY_DELAYED.value().place(this.level, this.level.getChunkSource().getGenerator(), RandomSource.create(), p_64090_);
    }

*/
}







    /*container
    public boolean hasChest() {
        return this.entityData.get(DATA_ID_CHEST);
    }

    public void setChest(boolean p_30505_) {
        this.entityData.set(DATA_ID_CHEST, p_30505_);
    }

    protected int getInventorySize() {
        return this.hasChest() ? 17 : super.getInventorySize();
    }

    public SlotAccess getSlot(int p_149479_) {
        return p_149479_ == 499 ? new SlotAccess() {
            public ItemStack get() {
                return SnailEntity.this.hasChest() ? new ItemStack(Items.CHEST) : ItemStack.EMPTY;
            }

            public boolean set(ItemStack p_149485_) {
                if (p_149485_.isEmpty()) {
                    if (SnailEntity.this.hasChest()) {
                        SnailEntity.this.setChest(false);
                        SnailEntity.this.createInventory();
                    }

                    return true;
                } else if (p_149485_.is(Items.CHEST)) {
                    if (!SnailEntity.this.hasChest()) {
                        SnailEntity.this.setChest(true);
                        SnailEntity.this.createInventory();
                    }

                    return true;
                } else {
                    return false;
                }
            }
        } : super.getSlot(p_149479_);
    }

    protected void playChestEquipsSound() {
        this.playSound(SoundEvents.DONKEY_CHEST, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
    }

    public int getInventoryColumns() {
        return 5;
    }  */
    //Taming
/*
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();

        Item itemForTaming = Items.OAK_LEAVES;

        if (item == itemForTaming && !isTame()) {
            if (this.level.isClientSide) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (!ForgeEventFactory.onAnimalTame(this, player)) {
                    if (!this.level.isClientSide) {
                        super.tame(player);
                        this.navigation.recomputePath();
                        this.setTarget(null);
                        this.level.broadcastEntityEvent(this, (byte)7);
                    }
                }

                return InteractionResult.SUCCESS;
            }
        }

        if(isTame() && !this.level.isClientSide && hand == InteractionHand.MAIN_HAND) {
            return InteractionResult.SUCCESS;
        }

        if (itemstack.getItem() == itemForTaming) {
            return InteractionResult.PASS;
        }

        return super.mobInteract(player, hand);
    }


    @Override
    public Team getTeam() {
        return super.getTeam();
    }

    public boolean canBeLeashed(Player player) {
        return true;
    }

    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(200.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)0.1f);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(100.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(15D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)0.2f);
        }
    }
}
*/
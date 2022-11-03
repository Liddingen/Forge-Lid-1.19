package net.liddingen.lidmod.entity.custom;

/*
import javax.annotation.Nullable;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RunAroundLikeCrazyGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Set;

public class SnailEntity1 extends AbstractChestedHorse implements ItemSteerable, Saddleable, IAnimatable {
    private static final int MAX_STRENGTH = 5;
    private static final EntityDataAccessor<Integer> DATA_STRENGTH_ID = SynchedEntityData.defineId(net.minecraft.world.entity.animal.horse.Llama.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_SWAG_ID = SynchedEntityData.defineId(net.minecraft.world.entity.animal.horse.Llama.class, EntityDataSerializers.INT);
//Saddle
    private static final EntityDataAccessor<Boolean> DATA_SADDLE_ID = SynchedEntityData.defineId(SnailEntity1.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_BOOST_TIME = SynchedEntityData.defineId(SnailEntity1.class, EntityDataSerializers.INT);
    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.OAK_LEAVES, Items.BIRCH_LEAVES, Items.SPRUCE_LEAVES, Items.DARK_OAK_LEAVES, Items.OAK_LEAVES,
            Items.ACACIA_LEAVES, Items.JUNGLE_LEAVES, Items.MANGROVE_LEAVES);
    private final ItemBasedSteering steering = new ItemBasedSteering(this.entityData, DATA_BOOST_TIME, DATA_SADDLE_ID);

    private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public SnailEntity1(EntityType<? extends AbstractChestedHorse> entityType, Level level) {
        super(entityType, level);
    }

   // public SsnailEentity(EntityType<? extends net.minecraft.world.entity.animal.horse.Llama> p_30750_, Level p_30751_) {
     //   super(p_30750_, p_30751_);
   // }

    private void setStrength(int p_30841_) {
        this.entityData.set(DATA_STRENGTH_ID, Math.max(1, Math.min(5, p_30841_)));
    }

    private void setRandomStrength(RandomSource p_218818_) {
        int i = p_218818_.nextFloat() < 0.04F ? 5 : 3;
        this.setStrength(1 + p_218818_.nextInt(i));
    }

    public int getStrength() {
        return this.entityData.get(DATA_STRENGTH_ID);
    }

    public void addAdditionalSaveData(CompoundTag p_30793_) {
        super.addAdditionalSaveData(p_30793_);
        this.steering.addAdditionalSaveData(p_30793_);
        p_30793_.putInt("Strength", this.getStrength());
        if (!this.inventory.getItem(1).isEmpty()) {
            p_30793_.put("DecorItem", this.inventory.getItem(1).save(new CompoundTag()));
        }

    }

    public void readAdditionalSaveData(CompoundTag p_30780_) {
        this.steering.readAdditionalSaveData(p_30780_);
        this.setStrength(p_30780_.getInt("Strength"));
        super.readAdditionalSaveData(p_30780_);
        if (p_30780_.contains("DecorItem", 10)) {
            this.inventory.setItem(1, ItemStack.of(p_30780_.getCompound("DecorItem")));
        }

        this.updateContainerEquipment();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RunAroundLikeCrazyGoal(this, 1.2D));
        this.goalSelector.addGoal(3, new PanicGoal(this, 1.2D));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.25D, Ingredient.of(Items.HAY_BLOCK), false));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.7D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier setAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.ATTACK_DAMAGE, 10f)
                .add(Attributes.ATTACK_SPEED, 0.5f)
                .add(Attributes.MOVEMENT_SPEED, 0.1f).build();
    }

    protected int getInventorySize() {
        return this.hasChest() ? 2 + 3 * this.getInventoryColumns() : super.getInventorySize();
    }



    protected boolean handleEating(Player p_30796_, ItemStack p_30797_) {
        int i = 0;
        int j = 0;
        float f = 0.0F;
        boolean flag = false;
        if (p_30797_.is(Items.WHEAT)) {
            i = 10;
            j = 3;
            f = 2.0F;
        } else if (p_30797_.is(Blocks.HAY_BLOCK.asItem())) {
            i = 90;
            j = 6;
            f = 10.0F;
            if (this.isTamed() && this.getAge() == 0 && this.canFallInLove()) {
                flag = true;
                this.setInLove(p_30796_);
            }
        }

        if (this.getHealth() < this.getMaxHealth() && f > 0.0F) {
            this.heal(f);
            flag = true;
        }

        if (this.isBaby() && i > 0) {
            this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
            if (!this.level.isClientSide) {
                this.ageUp(i);
            }

            flag = true;
        }

        if (j > 0 && (flag || !this.isTamed()) && this.getTemper() < this.getMaxTemper()) {
            flag = true;
            if (!this.level.isClientSide) {
                this.modifyTemper(j);
            }
        }

        if (flag && !this.isSilent()) {
            SoundEvent soundevent = this.getEatingSound();
            if (soundevent != null) {
                this.level.playSound((Player)null, this.getX(), this.getY(), this.getZ(), this.getEatingSound(), this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            }
        }

        return flag;
    }

    protected boolean isImmobile() {
        return this.isDeadOrDying() || this.isEating();
    }

    protected SoundEvent getAngrySound() {
        return SoundEvents.LLAMA_ANGRY;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.LLAMA_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_30803_) {
        return SoundEvents.LLAMA_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.LLAMA_DEATH;
    }

    @Nullable
    protected SoundEvent getEatingSound() {
        return SoundEvents.LLAMA_EAT;
    }

    protected void playStepSound(BlockPos p_30790_, BlockState p_30791_) {
        this.playSound(SoundEvents.LLAMA_STEP, 0.15F, 1.0F);
    }

    protected void playChestEquipsSound() {
        this.playSound(SoundEvents.LLAMA_CHEST, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
    }

    public void makeMad() {
        SoundEvent soundevent = this.getAngrySound();
        if (soundevent != null) {
            this.playSound(soundevent, this.getSoundVolume(), this.getVoicePitch());
        }

    }

    public int getInventoryColumns() {
        return this.getStrength();
    }

    public boolean canWearArmor() {
        return true;
    }

    public boolean isWearingArmor() {
        return !this.inventory.getItem(1).isEmpty();
    }

    public boolean isArmor(ItemStack p_30834_) {
        return p_30834_.is(ItemTags.WOOL_CARPETS);
    }

    public void containerChanged(Container p_30760_) {
        DyeColor dyecolor = this.getSwag();
        super.containerChanged(p_30760_);
        DyeColor dyecolor1 = this.getSwag();
        if (this.tickCount > 20 && dyecolor1 != null && dyecolor1 != dyecolor) {
            this.playSound(SoundEvents.LLAMA_SWAG, 0.5F, 1.0F);
        }

    }

    protected void updateContainerEquipment() {
        if (!this.level.isClientSide) {
            super.updateContainerEquipment();
            this.setSwag(getDyeColor(this.inventory.getItem(1)));
        }
    }

    private void setSwag(@Nullable DyeColor p_30772_) {
        this.entityData.set(DATA_SWAG_ID, p_30772_ == null ? -1 : p_30772_.getId());
    }

    @Nullable
    private static DyeColor getDyeColor(ItemStack p_30836_) {
        Block block = Block.byItem(p_30836_.getItem());
        return block instanceof WoolCarpetBlock ? ((WoolCarpetBlock)block).getColor() : null;
    }

    @Nullable
    public DyeColor getSwag() {
        int i = this.entityData.get(DATA_SWAG_ID);
        return i == -1 ? null : DyeColor.byId(i);
    }

    public int getMaxTemper() {
        return 30;
    }

    protected double followLeashSpeed() {
        return 2.0D;
    }

    public boolean canEatGrass() {
        return false;
    }

    //saddle
    public LivingEntity getControllingPassenger() {
        LivingEntity LivingEntity = (net.minecraft.world.entity.LivingEntity) this.getFirstPassenger();
        return LivingEntity != null && this.canBeControlledBy(LivingEntity) ? LivingEntity : null;
    }

    private boolean canBeControlledBy(LivingEntity p_218248_) {
        if (this.isSaddled() && p_218248_ instanceof Player player) {
            return player.getMainHandItem().is(Items.CARROT_ON_A_STICK) || player.getOffhandItem().is(Items.CARROT_ON_A_STICK);
        } else {
            return false;
        }
    }


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
        this.entityData.define(DATA_STRENGTH_ID, 0);
        this.entityData.define(DATA_SWAG_ID, -1);
    }
          public InteractionResult mobInteract(Player p_29489_, InteractionHand p_29490_) {
        boolean flag = this.isFood(p_29489_.getItemInHand(p_29490_));
        if (!flag && this.isSaddled() && !this.isVehicle() && !p_29489_.isSecondaryUseActive()) {
            if (!this.level.isClientSide) {
                p_29489_.startRiding(this);
            }

            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else {
            InteractionResult interactionresult = super.mobInteract(p_29489_, p_29490_);
            if (!interactionresult.consumesAction()) {
                ItemStack itemstack = p_29489_.getItemInHand(p_29490_);
                return itemstack.is(Items.SADDLE) ? itemstack.interactLivingEntity(p_29489_, this, p_29490_) : InteractionResult.PASS;
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

    }

    public boolean isSaddled() {
        return this.steering.hasSaddle();
    }

    public void equipSaddle(@org.jetbrains.annotations.Nullable SoundSource p_29476_) {
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
            float f2 = 0.3F;
            p_30830_.setPos(this.getX() + (double) (0.3F * f1),
                    this.getY() + this.getPassengersRidingOffset() + p_30830_.getMyRidingOffset(), this.getZ() - (double) (0.01F * f));
        }
    }

    public double getPassengersRidingOffset() {
        return (double) this.getBbHeight() * 0.965F;
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

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.snail.walk", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.snail.idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }
    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<SnailEntity1> controller = new AnimationController<>(this, "controller", 0,
                this::predicate);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
} */
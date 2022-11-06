package net.liddingen.lidmod.item.custom;
/*
import net.liddingen.lidmod.entity.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nullable;

public class SnailBowl extends BucketItem {
    private final java.util.function.Supplier<? extends EntityType<?>> entityTypeSupplier;
    private final java.util.function.Supplier<? extends SoundEvent> emptySoundSupplier;

    @Deprecated
    public void SnailBowl(EntityType<?> entityType, Fluid fluid, SoundEvent soundEvent, Properties properties) {
        this(() -> entityType, () -> fluid, () -> soundEvent, properties);
    }
    public SnailBowl(java.util.function.Supplier<? extends EntityType<?>> entitySupplier, java.util.function.Supplier<? extends Fluid> fluidSupplier, java.util.function.Supplier<? extends SoundEvent> soundSupplier, Item.Properties properties) {
        super(fluidSupplier, properties);
        this.emptySoundSupplier = soundSupplier;
        this.entityTypeSupplier = entitySupplier;
    }

    public void checkExtraContent(@Nullable Player player, Level level, ItemStack itemStack, BlockPos blockPos) {
        if (level instanceof ServerLevel) {
            this.spawn((ServerLevel)level, itemStack, blockPos);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, blockPos);
        }

    }

    protected void playEmptySound(@Nullable Player player, LevelAccessor levelAccessor, BlockPos blockPos) {
        levelAccessor.playSound(player, blockPos, getEmptySound(), SoundSource.NEUTRAL, 1.0F, 1.0F);
    }

    private void spawn(ServerLevel serverLevel, ItemStack itemStack, BlockPos blockPos) {
        Entity entity = ModEntityTypes.SNAILY.get().spawn(serverLevel, itemStack, (Player)null, blockPos, MobSpawnType.BUCKET, true, false);
        if (entity instanceof Bucketable bucketable) {
            bucketable.loadFromBucketTag(itemStack.getOrCreateTag());
            bucketable.setFromBucket(true);
        }

    }*/
/*
    public void appendHoverText(ItemStack p_151155_, @Nullable Level p_151156_, List<Component> p_151157_, TooltipFlag p_151158_) {
        if (getFishType() == EntityType.TROPICAL_FISH) {
            CompoundTag compoundtag = p_151155_.getTag();
            if (compoundtag != null && compoundtag.contains("BucketVariantTag", 3)) {
                int i = compoundtag.getInt("BucketVariantTag");
                ChatFormatting[] achatformatting = new ChatFormatting[]{ChatFormatting.ITALIC, ChatFormatting.GRAY};
                String s = "color.minecraft." + TropicalFish.getBaseColor(i);
                String s1 = "color.minecraft." + TropicalFish.getPatternColor(i);

                for(int j = 0; j < TropicalFish.COMMON_VARIANTS.length; ++j) {
                    if (i == TropicalFish.COMMON_VARIANTS[j]) {
                        p_151157_.add(Component.translatable(TropicalFish.getPredefinedName(j)).withStyle(achatformatting));
                        return;
                    }
                }

                p_151157_.add(Component.translatable(TropicalFish.getFishTypeName(i)).withStyle(achatformatting));
                MutableComponent mutablecomponent = Component.translatable(s);
                if (!s.equals(s1)) {
                    mutablecomponent.append(", ").append(Component.translatable(s1));
                }

                mutablecomponent.withStyle(achatformatting);
                p_151157_.add(mutablecomponent);
            }
        }

    }*/

    // Forge Start
/*
    protected SoundEvent getEmptySound() {
        return emptySoundSupplier.get();
    }
}
*/

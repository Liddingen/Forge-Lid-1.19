/*package net.liddingen.lidmod.item.custom;


import net.liddingen.lidmod.entity.ModEntityTypes;
import net.liddingen.lidmod.entity.custom.SnailyEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class SnailBowl extends MobBucketItem {

    private final Supplier<? extends SoundEvent> emptySoundSupplier;
    private final Supplier<? extends EntityType<?>> entityTypeSupplier;

    @Deprecated
    public SnailBowl(EntityType<?> type, SoundEvent event, Item.Properties properties) {
        this(() -> type, () -> event, properties);
    }

    public SnailBowl(Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends SoundEvent> soundSupplier, Item.Properties properties) {
        super(properties);
        this.emptySoundSupplier = soundSupplier;
        this.entityTypeSupplier = entitySupplier;
    }

    public void checkExtraContent(@Nullable Player player, Level level, ItemStack stack, BlockPos pos) {
        if (level instanceof ServerLevel) {
            this.spawn((ServerLevel)level, stack, pos);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
        }

    }

    private void spawn(ServerLevel level, ItemStack stack, BlockPos pos) {
        Entity modEntityTypes = ModEntityTypes.SNAILY.get().spawn(level, stack, (Player) null, pos, MobSpawnType.BUCKET, true, false);
        if (modEntityTypes instanceof Bucketable bucketable) {
            bucketable.loadFromBucketTag(stack.getOrCreateTag());
            bucketable.setFromBucket(true);
        }
    }

        protected void playEmptySound(@Nullable Player player, LevelAccessor accessor, BlockPos pos) {
            accessor.playSound(player, pos, getEmptySound(), SoundSource.NEUTRAL, 1.0F, 1.0F);
        }

    protected EntityType<?> getFishType() {
        return entityTypeSupplier.get();
    }

    protected SoundEvent getEmptySound() {
        return emptySoundSupplier.get();
    }
}
*/

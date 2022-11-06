package net.liddingen.lidmod.item.custom;

import net.liddingen.lidmod.entity.custom.ThrownThunderJug;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.item.EggItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;


public class ThunderJug extends Item {
    public ThunderJug(Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        player.getCooldowns().addCooldown(this, 20);
        level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!level.isClientSide) {
            ThrownThunderJug thrownThunderJug = new ThrownThunderJug(level, player);
            thrownThunderJug.setItem(itemstack);
            thrownThunderJug.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(thrownThunderJug);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}

/*  ITEM
    public ThunderJug(Properties properties) {
        super(properties);
    }


    @Override
    public InteractionResultHolder<ItemStack> use( Level level, Player player, InteractionHand usedHand) {

        Vec3 look = player.getLookAngle(); //Throw

        ItemStack itemStack = player.getItemInHand(usedHand);
        player.getCooldowns().addCooldown(this, 20);
        if (!level.isClientSide()) {
            ServerLevel world = ((ServerLevel) player.level);
            if (!player.isCreative()) {
               itemStack.setDamageValue(itemStack.getDamageValue() + 1);
               itemStack.shrink(1);
            }
            if (!level.isClientSide) {
                world.playSound(null, player,SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 1.0F,1.0F);
                EntityType.LIGHTNING_BOLT.spawn(world, null, player, player.blockPosition(), MobSpawnType.TRIGGERED, true, true);
                itemStack.shrink(1);
            }
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
        }
        return new InteractionResultHolder<>(InteractionResult.FAIL, itemStack);
    }
} */
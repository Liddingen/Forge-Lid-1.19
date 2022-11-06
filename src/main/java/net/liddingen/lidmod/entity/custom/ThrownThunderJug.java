package net.liddingen.lidmod.entity.custom;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ThrownThunderJug extends ThrowableItemProjectile {

    public ThrownThunderJug(Level level, LivingEntity entity) {
        super(EntityType.EGG, entity, level);
    }

    public void handleEntityEvent(byte p_37484_) {
        if (p_37484_ == 3) {
            double d0 = 0.08D;

            for(int i = 0; i < 8; ++i) {
                this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItem()), this.getX(), this.getY(), this.getZ(), ((double)this.random.nextFloat() - 0.5D) * 0.08D, ((double)this.random.nextFloat() - 0.5D) * 0.08D, ((double)this.random.nextFloat() - 0.5D) * 0.08D);
            }
        }

    }

    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        hitResult.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), 0.0F);
    }

    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level.isClientSide) {
            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(this.level);
            lightningBolt.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
            this.level.addFreshEntity(lightningBolt);

            this.level.broadcastEntityEvent(this, (byte)3);
            this.discard();
        }

    }

    protected Item getDefaultItem() {
        return Items.EGG;
    }
}

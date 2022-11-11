package net.liddingen.lidmod.networking.packet;

import net.liddingen.lidmod.effect.ModEffects;
import net.liddingen.lidmod.entity.ModEntityTypes;
import net.liddingen.lidmod.item.ModItems;
import net.minecraft.client.particle.MobAppearanceParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ShellHidingC2SPacket {

    public ShellHidingC2SPacket() {
    }

    public ShellHidingC2SPacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    protected boolean spawnCustomParticles() {
    return true;
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();
            // Shell Hiding Key Event
            if (player.getItemBySlot(EquipmentSlot.CHEST).is(ModItems.SNAIL_HOUSE.get())){
                if (player.hasEffect(ModEffects.HIDING.get())) {
                    player.removeAllEffects();
                    level.playSound(null, player.getOnPos(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS,
                            0.5F, level.random.nextFloat() * 0.1F + 0.9F);
                    player.getLevel().addParticle(ParticleTypes.ANGRY_VILLAGER, 0D, 0D, 0D, 0D, 0D,0D);

                } else {
                    level.playSound(null, player.getOnPos(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS,
                            0.5F, level.random.nextFloat() * 0.1F + 0.9F);
                   player.getLevel().addParticle(ParticleTypes.LARGE_SMOKE, 20D, 20D, 20D, 20D, 20D,20D);
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1440, 60));
                    player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 1440, 0));
                    player.addEffect(new MobEffectInstance(ModEffects.HIDING.get(), 1440, 0));
                    ModEntityTypes.SHELL.get().spawn(level, null, null, player.blockPosition(),
                            MobSpawnType.COMMAND, true, false);
                }
            }
        });
        return true;
    }

}

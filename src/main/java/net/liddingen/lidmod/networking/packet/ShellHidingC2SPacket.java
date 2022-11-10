package net.liddingen.lidmod.networking.packet;

import net.liddingen.lidmod.entity.ModEntityTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ShellHidingC2SPacket {
    public ShellHidingC2SPacket() {

    }

    public ShellHidingC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // Shell Hiding Key Event
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();
            ModEntityTypes.SHELL.get().spawn(level, null, null, player.blockPosition(),
                    MobSpawnType.COMMAND, true, false);
        });
        return true;
    }

}

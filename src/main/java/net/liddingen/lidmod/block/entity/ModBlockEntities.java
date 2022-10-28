package net.liddingen.lidmod.block.entity;

import net.liddingen.lidmod.LidMod;
import net.liddingen.lidmod.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, LidMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<NetheriteFrameEntity>> NETHERITE_FRAME
            = BLOCK_ENTITIES.register("netherite_frame", () ->
            BlockEntityType.Builder.of(NetheriteFrameEntity::new, ModBlocks.NETHERITE_FRAME.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
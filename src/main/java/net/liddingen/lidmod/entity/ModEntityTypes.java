package net.liddingen.lidmod.entity;


import net.liddingen.lidmod.LidMod;
import net.liddingen.lidmod.entity.custom.SnailEntity;
import net.liddingen.lidmod.entity.custom.SnailyEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, LidMod.MOD_ID);


    public static final RegistryObject<EntityType<SnailEntity>> SNAIL =
            ENTITY_TYPES.register("snail",
                    () -> EntityType.Builder.of(SnailEntity::new, MobCategory.CREATURE)
                            .sized(6.5f, 7.5f)  //HIT-box
                            .build(new ResourceLocation(LidMod.MOD_ID, "snail").toString()));

    public static final RegistryObject<EntityType<SnailyEntity>> SNAILY =
            ENTITY_TYPES.register("snaily",
                    () -> EntityType.Builder.of(SnailyEntity::new, MobCategory.CREATURE)
                            .sized(0.5f, 0.5f)  //HIT-box
                            .build(new ResourceLocation(LidMod.MOD_ID, "snaily").toString()));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
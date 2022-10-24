package net.liddingen.lidmod.villager;

import com.google.common.collect.ImmutableSet;
import net.liddingen.lidmod.LidMod;
import net.liddingen.lidmod.block.ModBlocks;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.InvocationTargetException;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(ForgeRegistries.POI_TYPES, LidMod.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, LidMod.MOD_ID);

public static final RegistryObject<PoiType> CHASE_BLOCK_POI = POI_TYPES.register("chase_block_poi",
        () -> new PoiType(ImmutableSet.copyOf(ModBlocks.CHASE_BLOCK.get().getStateDefinition().getPossibleStates()),
    1,1));

public static final RegistryObject<VillagerProfession> RAGEFUL_REVENGER = VILLAGER_PROFESSIONS.register("rageful_revenger",
        () -> new VillagerProfession("rageful_revenger", x -> x.get() == CHASE_BLOCK_POI.get(),
                x -> x.get() == CHASE_BLOCK_POI.get(), ImmutableSet.of(), ImmutableSet.of(),
                SoundEvents.VILLAGER_WORK_CARTOGRAPHER));



    public static void registerPOIs() {
        try {
            ObfuscationReflectionHelper.findMethod(PoiType.class,
                    "registerBlockStates", PoiType.class).invoke(null, CHASE_BLOCK_POI.get());
        } catch (InvocationTargetException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }


    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}

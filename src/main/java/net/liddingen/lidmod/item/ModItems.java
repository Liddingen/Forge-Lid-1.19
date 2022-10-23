package net.liddingen.lidmod.item;

import net.liddingen.lidmod.LidMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, LidMod.MOD_ID);


    public static final RegistryObject<Item> ELEKTRUM = ITEMS.register("elektrum",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.LID_TAB)));
    public static final RegistryObject<Item> RAW_ELEKTRUM = ITEMS.register("raw_elektrum",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.LID_TAB)));
    public static final RegistryObject<Item> ELEKTRUM_FIBER = ITEMS.register("elektrum_fiber",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.LID_TAB)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}

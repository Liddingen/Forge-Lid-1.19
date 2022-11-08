package net.liddingen.lidmod.item;

import net.liddingen.lidmod.LidMod;
import net.liddingen.lidmod.entity.ModEntityTypes;
import net.liddingen.lidmod.item.custom.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
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
    public static final RegistryObject<Item> JUG = ITEMS.register("jug",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.LID_TAB).stacksTo(16)));
    public static final RegistryObject<Item> THUNDER_JUG = ITEMS.register("thunder_jug",
            () -> new ThunderJug(new Item.Properties().tab(ModCreativeModeTab.LID_TAB).stacksTo(16)));
    public static final RegistryObject<Item> ELEKTRUM_CELL = ITEMS.register("elektrum_cell",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.LID_TAB)));

    public static final RegistryObject<Item> SNAIL_SPAWN_EGG = ITEMS.register("snail_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.SNAIL, 0xb0b58a, 0x83663c,
                    new Item.Properties().tab(ModCreativeModeTab.LID_TAB)));
    public static final RegistryObject<Item> SNAILY_SPAWN_EGG = ITEMS.register("snaily_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.SNAILY, 0xA06F40, 0xD3D699,
                    new Item.Properties().tab(ModCreativeModeTab.LID_TAB)));
    @Deprecated
    public static final RegistryObject<MobBucketItem> SNAIL_BUCKET = ITEMS.register("snail_bucket",
            () -> new MobBucketItem(ModEntityTypes.SNAILY.get(), Fluids.WATER, SoundEvents.SLIME_SQUISH_SMALL,
                    new Item.Properties().tab(ModCreativeModeTab.LID_TAB).craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final RegistryObject<Item> INSTRUCTION_PAPER = ITEMS.register("instruction_paper",
            () -> new InstructionPaper(new Item.Properties().tab(ModCreativeModeTab.LID_TAB).stacksTo(1)));
    public static final RegistryObject<Item> INSTRUCTION_PAPER_TWO = ITEMS.register("instruction_paper_two",
            () -> new InstructionPaperTwo(new Item.Properties().tab(ModCreativeModeTab.LID_TAB).stacksTo(1)));
    public static final RegistryObject<Item> INSTRUCTION_PAPER_THREE = ITEMS.register("instruction_paper_three",
            () -> new InstructionPaperThree(new Item.Properties().tab(ModCreativeModeTab.LID_TAB).stacksTo(1)));
    public static final RegistryObject<Item> INSTRUCTION_PAPER_FOUR = ITEMS.register("instruction_paper_four",
            () -> new InstructionPaperFour(new Item.Properties().tab(ModCreativeModeTab.LID_TAB).stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}

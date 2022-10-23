package net.liddingen.lidmod.block;

import net.liddingen.lidmod.LidMod; //
import net.liddingen.lidmod.block.custom.Charger;
import net.liddingen.lidmod.block.custom.ToggleableRedstoneLampBlock;
import net.liddingen.lidmod.item.ModCreativeModeTab;
import net.liddingen.lidmod.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab; //
import net.minecraft.world.level.block.Block; //
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus; //
import net.minecraftforge.registries.DeferredRegister; //
import net.minecraftforge.registries.ForgeRegistries; //
import net.minecraftforge.registries.RegistryObject; //
import java.util.function.Supplier; //



public class ModBlocks {
public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, LidMod.MOD_ID);

public static final RegistryObject<Block> ELEKTRUM_BLOCK = registerBlock("elektrum_block",
        () -> new Block(BlockBehaviour.Properties.of(Material.METAL)
                .strength(5f).requiresCorrectToolForDrops()), ModCreativeModeTab.LID_TAB);


    public static final RegistryObject<Block> ELEKTRUM_ORE = registerBlock("elektrum_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(6f).requiresCorrectToolForDrops(),
                    UniformInt.of(3, 7)), ModCreativeModeTab.LID_TAB);

    public static final RegistryObject<Block> DEEPSLATE_ELEKTRUM_ORE = registerBlock("deepslate_elektrum_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(7f).requiresCorrectToolForDrops(),
                    UniformInt.of(3, 7)), ModCreativeModeTab.LID_TAB);

    public static final RegistryObject<Block> CHARGER = registerBlock("charger",
            () -> new Charger(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(5f).requiresCorrectToolForDrops()), ModCreativeModeTab.LID_TAB);

    public static final RegistryObject<Block> TOGGLEABLE_REDSTONE_LAMP = registerBlock("toggleable_redstone_lamp",
            () -> new ToggleableRedstoneLampBlock(BlockBehaviour.Properties.of(Material.GLASS)
                    .strength(5f).requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(ToggleableRedstoneLampBlock.LIT) ? 15 : 0)), ModCreativeModeTab.LID_TAB);




private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
RegistryObject<T> toReturn = BLOCKS.register(name, block);
registerBlockItem(name, toReturn, tab);
return toReturn;
}

private static <T extends Block>  RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block,
                                                                         CreativeModeTab tab) {
    return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));

}

public static void register(IEventBus eventBus) {
    BLOCKS.register(eventBus);

    }
}

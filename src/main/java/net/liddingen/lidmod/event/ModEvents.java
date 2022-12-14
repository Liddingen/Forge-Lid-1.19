package net.liddingen.lidmod.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.liddingen.lidmod.LidMod;
import net.liddingen.lidmod.block.ModBlocks;
import net.liddingen.lidmod.entity.ModEntityTypes;
import net.liddingen.lidmod.entity.custom.ShellEntity;
import net.liddingen.lidmod.entity.custom.SnailEntity;
import net.liddingen.lidmod.entity.custom.SnailyEntity;
import net.liddingen.lidmod.item.ModItems;
import net.liddingen.lidmod.villager.ModVillagers;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;


public class ModEvents {

    @Mod.EventBusSubscriber(modid = LidMod.MOD_ID)
    public static class  ForgeEvents {
        @SubscribeEvent
        public static void addCustomTrades(VillagerTradesEvent event) {

            if(event.getType() == ModVillagers.RAGEFUL_REVENGER.get()) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(ModItems.INSTRUCTION_PAPER.get(), 1);
                int villagerLevel = 1;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.MAP,1 ),
                        new ItemStack(Items.WRITTEN_BOOK,1),
                        stack,1,5,0.00F));
            }

            if(event.getType() == ModVillagers.RAGEFUL_REVENGER.get()) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(ModItems.JUG.get(), 1);
                int villagerLevel = 1;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(ModItems.INSTRUCTION_PAPER.get(),1 ),
                        new ItemStack(Items.CREEPER_HEAD,1),
                        stack,1,5,0.00F));
            }

            //Instruction Two


            if(event.getType() == ModVillagers.RAGEFUL_REVENGER.get()) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(ModItems.INSTRUCTION_PAPER_TWO.get(), 1);
                int villagerLevel = 2;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.MAP,1 ),
                        new ItemStack(Items.WRITTEN_BOOK,1),
                        stack,1,30,0.00F));
            }

            if(event.getType() == ModVillagers.RAGEFUL_REVENGER.get()) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(ModItems.ELEKTRUM_CELL.get(), 4);
                int villagerLevel = 2;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(ModItems.INSTRUCTION_PAPER_TWO.get(),1 ),
                        new ItemStack(Items.MUSIC_DISC_5,1),
                        stack,1,30,0.00F));
            }

            //Instruction Three


            if(event.getType() == ModVillagers.RAGEFUL_REVENGER.get()) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(ModItems.INSTRUCTION_PAPER_THREE.get(), 1);
                int villagerLevel = 3;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.MAP,1 ),
                        new ItemStack(Items.WRITTEN_BOOK,1),
                        stack,1,40,0.00F));
            }

            if(event.getType() == ModVillagers.RAGEFUL_REVENGER.get()) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(ModBlocks.NETHERITE_FRAME.get(), 1);
                int villagerLevel = 3;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(ModItems.INSTRUCTION_PAPER_THREE.get(),1 ),
                        new ItemStack(Items.AXOLOTL_BUCKET,1),
                        stack,1,40,0.00F));
            }

            //Instruction Four


            if(event.getType() == ModVillagers.RAGEFUL_REVENGER.get()) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(ModItems.INSTRUCTION_PAPER_FOUR.get(), 1);
                int villagerLevel = 4;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.MAP,1 ),
                        new ItemStack(Items.WRITTEN_BOOK,1),
                        stack,1,50,0.00F));
            }

            if(event.getType() == ModVillagers.RAGEFUL_REVENGER.get()) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(ModBlocks.TRANSFORMER.get(), 1);
                int villagerLevel = 4;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(ModItems.INSTRUCTION_PAPER_FOUR.get(),1 ),
                        new ItemStack(Items.TRIDENT,1),
                        stack,1,50,0.00F));
            }

            if(event.getType() == ModVillagers.RAGEFUL_REVENGER.get()) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(Items.PAINTING, 1);
                int villagerLevel = 5;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD,3 ),
                        stack,16,0,0.03F));
            }


        }
        public boolean canRestock() {
            return false;
        }
    }

    @Mod.EventBusSubscriber(modid = LidMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventsBusEvents {
        @SubscribeEvent
        public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
            event.put(ModEntityTypes.SNAIL.get(), SnailEntity.setAttributes());
            event.put(ModEntityTypes.SNAILY.get(), SnailyEntity.setAttributes());
            event.put(ModEntityTypes.SHELL.get(), ShellEntity.setAttributes());
        }
    }
}


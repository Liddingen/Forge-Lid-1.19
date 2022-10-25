package net.liddingen.lidmod.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.liddingen.lidmod.LidMod;
import net.liddingen.lidmod.item.ModItems;
import net.liddingen.lidmod.villager.ModVillagers;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = LidMod.MOD_ID)
public class ModEvents {
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
            ItemStack stack = new ItemStack(ModItems.ELEKTRUM.get(), 64);
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
            ItemStack stack = new ItemStack(ModItems.ELEKTRUM_FIBER.get(), 64);
            int villagerLevel = 2;

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(ModItems.INSTRUCTION_PAPER_TWO.get(),1 ),
                    new ItemStack(Items.MUSIC_DISC_5,1),
                    stack,1,30,0.00F));
        }

    }
}


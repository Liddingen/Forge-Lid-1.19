package net.liddingen.lidmod.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InstructionPaper extends Item {
    public InstructionPaper(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            outputRandomInstructionOne(player);
            player.getCooldowns().addCooldown(this, 100);
            level.playSound(null, player.getOnPos(), SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundSource.PLAYERS,
                    0.5F, level.random.nextFloat() * 0.1F + 0.9F);

        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_41422_, List<Component> components, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            components.add(Component.literal("Your next objective is to kill one creeper with" +
                    " a charged creeper explosion and bring with you the creepers head!").withStyle(ChatFormatting.AQUA));
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.YELLOW));
        }


        super.appendHoverText(stack, p_41422_, components, flag);
    }

    private void outputRandomInstructionOne(Player player) {
        player.sendSystemMessage(Component.literal("Your next objective is to kill one creeper with" +
                " a charged creeper explosion and bring with you the creepers head!").withStyle(ChatFormatting.GOLD));
    }
}

    /*private void getRandomInstruction(String[] args) {
        String[] instructions = new String[3];
        instructions[0] = "kill one creeper with a charged creeper explosion and bring with you the creepers head!";
        instructions[1] = "go deep in the caves and find nine musik disc fragments to craft the 5th musik disc. Then bring the disk to me!";
        instructions[2] = "kill a drowns to earn a trident and then give it to me!";
} */

package net.liddingen.lidmod.item.custom;

import net.liddingen.lidmod.entity.custom.ThrownThunderJug;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ThunderJug extends Item {
    public ThunderJug(Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        player.getCooldowns().addCooldown(this, 20);
        level.playSound((Player)null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!level.isClientSide) {
            ThrownThunderJug thrownThunderJug = new ThrownThunderJug(level, player);
            thrownThunderJug.setItem(itemstack);
            thrownThunderJug.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.5F, 1.0F);
            level.addFreshEntity(thrownThunderJug);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_41422_, List<Component> components, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            components.add(Component.literal("Summons a lightning bolt on impact." +
                    " Hitting a snail will increase the snail's speed").withStyle(ChatFormatting.AQUA));
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.YELLOW));
        }

        super.appendHoverText(stack, p_41422_, components, flag);
    }
}

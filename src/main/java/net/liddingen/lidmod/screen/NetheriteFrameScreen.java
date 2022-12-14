package net.liddingen.lidmod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.liddingen.lidmod.LidMod;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class NetheriteFrameScreen extends AbstractContainerScreen<NetheriteFrameMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(LidMod.MOD_ID,"textures/gui/netherite_frame_gui.png");

    public NetheriteFrameScreen(NetheriteFrameMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.titleLabelX = 51;
        this.titleLabelY = 6;
        this.inventoryLabelY = 73;
        this.inventoryLabelX = 7;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }
}
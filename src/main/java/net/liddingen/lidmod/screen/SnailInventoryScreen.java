package net.liddingen.lidmod.screen;
/*
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.liddingen.lidmod.LidMod;
import net.liddingen.lidmod.entity.custom.SnailEntity;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SnailInventoryScreen extends AbstractContainerScreen<SnailInventoryMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(LidMod.MOD_ID,"textures/gui/snail_gui.png");

    private final SnailEntity snail;

    private float xMouse;

    private float yMouse;

    public SnailInventoryScreen(SnailInventoryMenu pMenu, Inventory pPlayerInventory, SnailEntity pSnail) {
        super(pMenu, pPlayerInventory, pSnail.getDisplayName());
        this.snail = pSnail;
        this.passEvents = false;
    }
    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pX, int pY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(pPoseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        if (this.snail instanceof SnailEntity) {
            SnailEntity snailEntity = (SnailEntity)this.snail;
            if (snailEntity.hasChest()) {
                this.blit(pPoseStack, i + 79, j + 17, 0, this.imageHeight, snailEntity.getInventoryColumns() * 18, 54);
            }
        }

        if (this.snail.isSaddleable()) {
            this.blit(pPoseStack, i + 7, j + 35 - 18, 18, this.imageHeight + 54, 18, 18);
        }
        InventoryScreen.renderEntityInInventory(i + 51, j + 60, 17, (float)(i + 51) - this.xMouse, (float)(j + 75 - 50) - this.yMouse, this.snail);
    }
    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        this.xMouse = (float)pMouseX;
        this.yMouse = (float)pMouseY;
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }
}*/

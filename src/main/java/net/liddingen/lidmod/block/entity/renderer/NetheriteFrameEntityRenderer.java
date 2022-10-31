package net.liddingen.lidmod.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.liddingen.lidmod.block.custom.NetheriteFrame;
import net.liddingen.lidmod.block.entity.NetheriteFrameEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class NetheriteFrameEntityRenderer implements BlockEntityRenderer<NetheriteFrameEntity> {
    public NetheriteFrameEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(NetheriteFrameEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
                       MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStack = pBlockEntity.getRenderStack();
        pPoseStack.pushPose();
        pPoseStack.translate(0.5f, 0.3f, 0.5f);
        pPoseStack.scale(0.35f, 0.35f, 0.35f);

        switch (pBlockEntity.getBlockState().getValue(NetheriteFrame.FACING)) {
            case NORTH -> pPoseStack.mulPose(Vector3f.YP.rotationDegrees(0));
            case EAST -> pPoseStack.mulPose(Vector3f.YP.rotationDegrees(90));
            case SOUTH -> pPoseStack.mulPose(Vector3f.YP.rotationDegrees(180));
            case WEST -> pPoseStack.mulPose(Vector3f.YP.rotationDegrees(270));
        }

        itemRenderer.renderStatic(itemStack, ItemTransforms.TransformType.GUI, getLightLevel(pBlockEntity.getLevel(),
                        pBlockEntity.getBlockPos()),
                OverlayTexture.NO_OVERLAY, pPoseStack, pBufferSource, 1);
        pPoseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
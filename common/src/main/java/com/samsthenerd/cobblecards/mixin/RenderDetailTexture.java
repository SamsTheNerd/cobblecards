package com.samsthenerd.cobblecards.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import com.samsthenerd.cobblecards.items.IDetailTexture;
import com.samsthenerd.cobblecards.items.IDetailTexture.DetailTexture;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Mixin(ItemRenderer.class)
public class RenderDetailTexture {
    @WrapOperation(
        method="renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
        at=@At(value="INVOKE", target="net/minecraft/client/render/item/ItemRenderer.renderBakedItemModel (Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;)V")
    )
    public void renderDetailTexture(ItemRenderer renderer, BakedModel model, ItemStack stack, int light, int overlay, MatrixStack matrices, 
        VertexConsumer vertices, Operation<Void> original, ItemStack stackEnclosing, ModelTransformationMode renderMode, 
        boolean leftHanded, MatrixStack matricesEnclosing, VertexConsumerProvider vertexConsumers){
        original.call(renderer, model, stack, light, overlay, matrices, vertices);
        if(renderMode == ModelTransformationMode.GUI && stack.getItem() instanceof IDetailTexture detailTextureItem){
        // if(renderMode == ModelTransformationMode.GUI && stack.getItem() instanceof IDetailTexture detailTextureItem){
            // render the detail texture
            DetailTexture detail = detailTextureItem.getDetailTexture(stack);
            if(detail == null) return;
            Identifier textureId = detail.sprite.getTextureId();
            if(textureId == null || textureId.equals(new Identifier(""))){
                return;
            }

            if(detail.isOverItem() && vertexConsumers instanceof VertexConsumerProvider.Immediate immediateVCs){
                RenderSystem.disableDepthTest();
                immediateVCs.draw();
                RenderSystem.enableDepthTest();
            }

            matrices.push();
            matrices.translate(detail.getLeft(), 1-detail.getTop(), 500);
            // matrices.translate(0, 0, 500);
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            
            // float scale = (float) detail.width() / detail.texture().getWidth();
            matrices.scale((float)detail.getWidth(), (float)-detail.getHeight(), 1f);

            Tessellator tess = Tessellator.getInstance();

            BufferBuilder buffer = tess.getBuffer();
            buffer.begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            float atX = 0;
            float atY = 0;
            float width = 1;
            float height = 1;
            buffer.vertex(matrices.peek().getPositionMatrix(), atX, atY + height, 0).texture(0, 1).next();
            buffer.vertex(matrices.peek().getPositionMatrix(), atX + width, atY + height, 0).texture(1, 1).next();
            buffer.vertex(matrices.peek().getPositionMatrix(), atX + width, atY, 0).texture(1, 0).next();
            buffer.vertex(matrices.peek().getPositionMatrix(), atX, atY, 0).texture(0, 0).next();

            RenderSystem.setShaderTexture(0, textureId);
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.disableCull();
            RenderSystem.disableDepthTest();

            tess.draw();

            matrices.pop();
        }
    }
}

package com.samsthenerd.cobblecards.mixin.inline;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.samsthenerd.cobblecards.inline.Inline;
import com.samsthenerd.cobblecards.inline.InlineData;
import com.samsthenerd.cobblecards.inline.InlineRenderer;
import com.samsthenerd.cobblecards.inline.InlineRenderer.TextRenderingContext;
import com.samsthenerd.cobblecards.inline.InlineStyle;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer.TextLayerType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;

@Mixin( targets = "net.minecraft.client.font.TextRenderer$Drawer")
public class MixinInlineRendering {
    @Shadow
	float x;
	@Shadow
	float y;
    @Shadow
    private Matrix4f matrix;

    @Shadow
    @Final
    private int light;

    @Shadow
    @Final
    private boolean shadow;
    @Shadow
    @Final
    private float brightnessMultiplier;
    @Shadow
    @Final
    private float red;
    @Shadow
    @Final
    private float green;
    @Shadow
    @Final
    private float blue;
    @Shadow
    @Final
    private float alpha;

    @Shadow
    @Final
    private TextLayerType layerType;

    @Shadow
    @Final
    VertexConsumerProvider vertexConsumers;

    private static final Tessellator secondaryTess = new Tessellator();


    @Inject(method = "accept(ILnet/minecraft/text/Style;I)Z", at = @At("HEAD"), cancellable = true)
	private void PatStyDrawerAccept(int index, Style style, int codepoint, CallbackInfoReturnable<Boolean> cir) {
        InlineStyle inlStyle = (InlineStyle) style;
        InlineData inlData = inlStyle.getInlineData();
        if(inlData == null){
            return;
        }
        InlineRenderer renderer = Inline.getRenderer(inlData.getRendererId());
        if(renderer == null){
            return;
        }
        // for now just try to get an item renderer set up here

        Tessellator heldTess = Tessellator.getInstance();

        MixinSetTessBuffer.setInstance(secondaryTess);

        DrawContext drawContext = new DrawContext(MinecraftClient.getInstance(), VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer()));
        
        MatrixStack matrices = drawContext.getMatrices();

        matrices.push();

        matrices.multiplyPositionMatrix(matrix);
        matrices.translate(x, y, 0);

        TextRenderingContext trContext = new InlineRenderer.TextRenderingContext(light, shadow, brightnessMultiplier, red, green, blue, alpha, layerType, vertexConsumers);
        x += renderer.render(inlData, drawContext, index, style, codepoint, trContext);

        matrices.pop();

        MixinSetTessBuffer.setInstance(heldTess);

        cir.setReturnValue(true);
    }
}


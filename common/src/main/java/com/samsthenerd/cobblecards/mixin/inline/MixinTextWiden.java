package com.samsthenerd.cobblecards.mixin.inline;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.samsthenerd.cobblecards.inline.Inline;
import com.samsthenerd.cobblecards.inline.InlineData;
import com.samsthenerd.cobblecards.inline.InlineStyle;

import net.minecraft.client.font.Glyph;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Style;

@Mixin(TextRenderer.class)
public class MixinTextWiden {
    @Inject(method = "method_27516(ILnet/minecraft/text/Style;)F", at = @At("HEAD"), cancellable = true)
    private void TextHandlerOverrideForPattern(int codepoint, Style style, CallbackInfoReturnable<Float> cir){
        InlineStyle inlStyle = (InlineStyle)style;
        // if(inlStyle.isHidden()){
        //     cir.setReturnValue(0f);
        //     return;
        // }
        InlineData inlData = inlStyle.getInlineData();
        if(inlData == null){
            return;
        }
        int cWidth = Inline.getRenderer(inlData.getRendererId()).charWidth(inlData, style, codepoint);
        cir.setReturnValue((float)cWidth);
    }


    @WrapOperation(method="method_37297(Lnet/minecraft/client/font/TextRenderer$Drawer;[FIFIIILnet/minecraft/text/Style;I)Z",
    at=@At(value="INVOKE", target="net/minecraft/client/font/Glyph.getAdvance (Z)F"))
    private float MakeTextGlowWider(Glyph glyph, boolean bold, Operation<Float> original, @Coerce Object drawer, float[] fs, int i, float f, int j, int k, int index, Style style, int codepoint){
        InlineStyle inlStyle = (InlineStyle)style;
        InlineData inlData = inlStyle.getInlineData();
        if(inlData == null){
            return original.call(glyph, bold);
        }
        int cWidth = Inline.getRenderer(inlData.getRendererId()).charWidth(inlData, style, codepoint);
        return cWidth;
    }

    // TODO: mixin to the drawer accept call right after this ^ and stick a marker on the style 
}

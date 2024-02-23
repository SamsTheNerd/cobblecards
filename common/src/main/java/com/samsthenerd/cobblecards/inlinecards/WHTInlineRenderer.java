package com.samsthenerd.cobblecards.inlinecards;

import com.mojang.blaze3d.systems.RenderSystem;
import com.samsthenerd.cobblecards.CobbleCards;
import com.samsthenerd.cobblecards.inline.InlineRenderer;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;

public class WHTInlineRenderer implements InlineRenderer<WHTInlineData>{

    public static final WHTInlineRenderer INSTANCE = new WHTInlineRenderer();

    public Identifier getId(){
        return new Identifier(CobbleCards.MOD_ID, "whtexture");
    
    }

    public int render(WHTInlineData data, DrawContext context, int index, Style style, int codepoint, TextRenderingContext trContext){
        int height = data.texture.getHeight();
        if(height == 0){
            return 0;
        }
        int width = data.texture.getWidth();
        float whRatio = (width / (float)height);
        float scale = (float)(8.0 / height);
        MatrixStack matrices = context.getMatrices();
        matrices.scale(scale, scale, 1);
        matrices.translate(0, 0, 1);
        RenderSystem.enableDepthTest();
        context.drawTexture(data.texture.getTextureId(), 0, 0 , 0, 0, 0, width, height, width, height);
        return (int)Math.ceil(whRatio * 8);
    }

    public int charWidth(WHTInlineData data, Style style, int codepoint){
        int height = data.texture.getHeight();
        if(height == 0){
            return 0;
        }
        return (int)Math.ceil(8.0 * data.texture.getWidth() / (float)height);
    }
}

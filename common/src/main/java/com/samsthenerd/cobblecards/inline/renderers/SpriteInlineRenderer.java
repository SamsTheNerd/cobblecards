package com.samsthenerd.cobblecards.inline.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.samsthenerd.cobblecards.CobbleCards;
import com.samsthenerd.cobblecards.inline.InlineRenderer;
import com.samsthenerd.cobblecards.inline.data.SpriteInlineData;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;

public class SpriteInlineRenderer implements InlineRenderer<SpriteInlineData>{

    public static final SpriteInlineRenderer INSTANCE = new SpriteInlineRenderer();

    public Identifier getId(){
        return new Identifier(CobbleCards.MOD_ID, "whtexture");
    
    }

    public int render(SpriteInlineData data, DrawContext context, int index, Style style, int codepoint, TextRenderingContext trContext){
        int height = data.sprite.getSpriteHeight();
        if(height == 0){
            return 0;
        }
        int width = data.sprite.getSpriteWidth();
        float whRatio = (width / (float)height);
        float scale = (float)(8.0 / height);
        MatrixStack matrices = context.getMatrices();
        matrices.scale(scale, scale, 1);
        matrices.translate(0, 0, 1);
        RenderSystem.enableDepthTest();
        // context.drawTexture(data.sprite.getTextureId(), 0, 0 , 0, 0, 0, width, height, width, height);
        data.sprite.drawSprite(context, 0, 0, 0, 1, 1);
        return (int)Math.ceil(whRatio * 8);
    }

    public int charWidth(SpriteInlineData data, Style style, int codepoint){
        int height = data.sprite.getSpriteHeight();
        if(height == 0){
            return 0;
        }
        return (int)Math.ceil(8.0 * data.sprite.getSpriteWidth() / (float)height);
    }
}

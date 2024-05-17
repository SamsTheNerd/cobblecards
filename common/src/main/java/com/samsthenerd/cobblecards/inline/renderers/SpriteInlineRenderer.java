package com.samsthenerd.cobblecards.inline.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.samsthenerd.cobblecards.inline.Inline;
import com.samsthenerd.cobblecards.inline.InlineRenderer;
import com.samsthenerd.cobblecards.inline.data.SpriteInlineData;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper.Argb;

// this is generic-d weirdly so that other renderers can extend and use its render method while keeping their own data types
// ultimately the generics don't matter all that much since it's all called based on trust that the renderer a data says it should use can actually use it
public class SpriteInlineRenderer<T extends SpriteInlineData> implements InlineRenderer<T>{

    public static final SpriteInlineRenderer<SpriteInlineData> INSTANCE = new SpriteInlineRenderer<SpriteInlineData>();

    public Identifier getId(){
        return new Identifier(Inline.MOD_ID, "spritelike");
    
    }

    public int render(SpriteInlineData data, DrawContext context, int index, Style style, int codepoint, TextRenderingContext trContext){
        float height = data.sprite.getSpriteHeight();
        if(height == 0){
            return 0;
        }
        float width = data.sprite.getSpriteWidth();
        float whRatio = (width / (float)height);
        MatrixStack matrices = context.getMatrices();
        // matrices.scale(scale, scale, 1);
        matrices.translate(0, 0, 1);
        RenderSystem.enableDepthTest();
        // context.drawTexture(data.sprite.getTextureId(), 0, 0 , 0, 0, 0, width, height, width, height);
        int argb = Argb.getArgb((int)(trContext.alpha * 255), (int)(trContext.red * 255), (int)(trContext.green * 255), (int)(trContext.blue * 255));
        data.sprite.drawSpriteWithLight(context, 0, 0, 0, 8 * width / height,8, trContext.light, 0xFFFFFFFF);
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

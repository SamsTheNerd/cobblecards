package com.samsthenerd.cobblecards.tooltips.components;

import java.util.function.BiFunction;

import com.mojang.blaze3d.systems.RenderSystem;
import com.samsthenerd.cobblecards.tooltips.data.WHTextureTooltipData;
import com.samsthenerd.cobblecards.utils.WHTexture;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class WHTextureTooltipComponent implements TooltipComponent {
    public static final float DEFAULT_RENDER_SIZE = 96f;

    private WHTexture texture;
    private BiFunction<Integer, Integer, Integer> widthProvider;

    public WHTextureTooltipComponent(WHTextureTooltipData tt) {
        this.texture = tt.texture;
        this.widthProvider = tt.widthProvider;
    }

    @Override
    public void drawItems(TextRenderer font, int mouseX, int mouseY, DrawContext context) {
        // reload it just incase it failed the first time or whatever ?
        Identifier textureId = texture.getTextureId();
        if(textureId == null || textureId.equals(new Identifier(""))){
            return;
        }
        MatrixStack ps = context.getMatrices();
        ps.push();
        ps.translate(mouseX, mouseY, 500);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        
        float scale = ((float)getWidth(font)) / texture.getWidth();
        ps.scale(scale, scale, 1f);
        context.drawTexture(textureId, 0, 0, 0, 0, 0, texture.getWidth(), texture.getHeight(), texture.getWidth(), texture.getHeight());
        ps.pop();
    }

    @Override
    public int getWidth(TextRenderer pFont) {
        return widthProvider.apply(texture.getWidth(), texture.getHeight());
    }

    @Override
    public int getHeight() {
        int realWidth = widthProvider.apply(texture.getWidth(), texture.getHeight());
        return (int)(realWidth * ((double)texture.getHeight())/texture.getWidth());
    }
}

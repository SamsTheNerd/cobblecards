package com.samsthenerd.cobblecards.tooltips.components;

import java.util.function.BiFunction;

import com.mojang.blaze3d.systems.RenderSystem;
import com.samsthenerd.cobblecards.tooltips.data.URLImageTooltipData;
import com.samsthenerd.cobblecards.utils.URLTextureUtils;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class URLImageTooltipComponent implements TooltipComponent{
    public static final float DEFAULT_RENDER_SIZE = 96f;

    private final Identifier textureId;
    private final String url;
    private Identifier realTextureId;
    private final BiFunction<Integer, Integer, Integer> widthProvider;

    public URLImageTooltipComponent(URLImageTooltipData tt) {
        this.textureId = tt.id();
        this.widthProvider = tt.widthProvider();
        this.url = tt.url();
        realTextureId = URLTextureUtils.loadTextureFromURL(url, textureId);
    }

    @Override
    public void drawItems(TextRenderer font, int mouseX, int mouseY, DrawContext context) {
        // reload it just incase it failed the first time or whatever ?
        realTextureId = URLTextureUtils.loadTextureFromURL(url, textureId);
        if(realTextureId == null || realTextureId.equals(new Identifier(""))){
            return;
        }
        MatrixStack ps = context.getMatrices();
        ps.push();
        ps.translate(mouseX, mouseY, 500);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        
        Pair<Integer, Integer> dims =  URLTextureUtils.getTextureDimensions(textureId);
        float scale = ((float)getWidth(font)) / dims.getLeft();
        ps.scale(scale, scale, 1f);
        context.drawTexture(realTextureId, 0, 0, 0, 0, 0, dims.getLeft(), dims.getRight(), dims.getLeft(), dims.getRight());
        ps.pop();
    }

    @Override
    public int getWidth(TextRenderer pFont) {
        Pair<Integer, Integer> dims =  URLTextureUtils.getTextureDimensions(textureId);
        return widthProvider.apply(dims.getLeft(), dims.getRight());
    }

    @Override
    public int getHeight() {
        Pair<Integer, Integer> dims =  URLTextureUtils.getTextureDimensions(textureId);
        int realWidth = widthProvider.apply(dims.getLeft(), dims.getRight());
        return (int)(realWidth * ((double)dims.getRight())/dims.getLeft());
    }
}

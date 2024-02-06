package com.samsthenerd.cobblecards.tooltips.data;

import java.util.function.BiFunction;

import com.samsthenerd.cobblecards.utils.WHTexture;

import net.minecraft.client.item.TooltipData;

// maxWidth/maxHeight are for how big it should render
public class WHTextureTooltipData implements TooltipData{
    public final WHTexture texture;
    // takes in the native width and height of the given texture and returns the width to render it at
    public BiFunction<Integer, Integer, Integer> widthProvider = (w, h) -> 128;

    public WHTextureTooltipData(WHTexture texture){
        this.texture = texture;
    }

    public WHTextureTooltipData(WHTexture texture, BiFunction<Integer, Integer, Integer> widthProvider){
        this.texture = texture;
        this.widthProvider = widthProvider;
    }
}

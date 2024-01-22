package com.samsthenerd.cobblecards.tooltips.data;

import java.util.function.BiFunction;

import net.minecraft.client.item.TooltipData;
import net.minecraft.util.Identifier;

// width provider is image native width, native height -> render width
public record URLImageTooltipData(Identifier id, String url, BiFunction<Integer, Integer, Integer> widthProvider) implements TooltipData{
    
}

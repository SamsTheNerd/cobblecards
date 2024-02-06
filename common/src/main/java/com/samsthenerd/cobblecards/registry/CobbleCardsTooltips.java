package com.samsthenerd.cobblecards.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.samsthenerd.cobblecards.tooltips.components.WHTextureTooltipComponent;
import com.samsthenerd.cobblecards.tooltips.data.WHTextureTooltipData;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;

public class CobbleCardsTooltips {

    public static final Map<Class<? extends TooltipData>, Function<TooltipData, TooltipComponent>> tooltipDataToComponent = new HashMap<>();

    public static TooltipComponent getTooltipComponent(TooltipData data){
        Function<TooltipData, TooltipComponent> ttFunc = tooltipDataToComponent.get(data.getClass());
        return ttFunc == null ? null : ttFunc.apply(data);
    }

    public static <C extends TooltipComponent, D extends TooltipData> Function<TooltipData, C> convertTooltip(Class<D> dataClass, 
        Function<D, C> componentFactory){
        return (data) -> {
            if(dataClass.isInstance(data)){
                return componentFactory.apply(dataClass.cast(data));
            }
            return null;
        };
    }

    // should be called sided in tooltip registration stuff
    public static void init(){
        tooltipDataToComponent.put(WHTextureTooltipData.class, convertTooltip(WHTextureTooltipData.class, WHTextureTooltipComponent::new));
    }
}

package com.samsthenerd.cobblecards.forge;

import java.util.Map.Entry;
import java.util.function.Function;

import com.samsthenerd.cobblecards.CobbleCards;
import com.samsthenerd.cobblecards.registry.CobbleCardsTooltips;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CobbleCardsForgeClient {
    @SubscribeEvent
    public static void registerTooltipComponents(RegisterClientTooltipComponentFactoriesEvent evt) {
        CobbleCards.logPrint("registering tooltip components");
        // evt.register(MirrorTooltipData.class, MirrorTooltipComponent::new);
        CobbleCardsTooltips.init();
        for(Entry<Class<? extends TooltipData>, Function<TooltipData, TooltipComponent>> entry : CobbleCardsTooltips.tooltipDataToComponent.entrySet()){
            evt.register(entry.getKey(), entry.getValue());
        }
    }
}


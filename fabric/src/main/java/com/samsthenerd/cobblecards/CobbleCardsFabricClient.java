package com.samsthenerd.cobblecards;

import com.samsthenerd.cobblecards.registry.CobbleCardsTooltips;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;

public class CobbleCardsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CobbleCardsClientInit.initClient();

        CobbleCardsTooltips.init();
        TooltipComponentCallback.EVENT.register(CobbleCardsTooltips::getTooltipComponent);
    }
}

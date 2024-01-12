package com.samsthenerd.cobblecards;

import net.fabricmc.api.ClientModInitializer;

public class CobbleCardsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CobbleCardsClientInit.initClient();
    }
}

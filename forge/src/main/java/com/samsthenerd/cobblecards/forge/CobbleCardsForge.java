package com.samsthenerd.cobblecards.forge;

import com.samsthenerd.cobblecards.CobbleCards;
import com.samsthenerd.cobblecards.CobbleCardsClientInit;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("cobblecards")
public class CobbleCardsForge {
    public CobbleCardsForge(){
        // so that we can register properly with architectury
        EventBuses.registerModEventBus(CobbleCards.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);

        CobbleCards.onInitialize();
    }

    private void onClientSetup(FMLClientSetupEvent event) { 
        event.enqueueWork(() -> {
            CobbleCardsClientInit.initClient();
        });
    }
}

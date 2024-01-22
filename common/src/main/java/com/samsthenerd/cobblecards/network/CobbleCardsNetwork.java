package com.samsthenerd.cobblecards.network;

import com.samsthenerd.cobblecards.CobbleCards;
import com.samsthenerd.cobblecards.utils.CardNewnessTracker;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.NetworkManager.Side;
import net.minecraft.util.Identifier;

public class CobbleCardsNetwork {
    public static final Identifier VIEW_CARD_C2S = new Identifier(CobbleCards.MOD_ID, "view_card_c2s");

    public static void init(){
        NetworkManager.registerReceiver(Side.C2S, VIEW_CARD_C2S, CardNewnessTracker::receiveCardView);
    }
}

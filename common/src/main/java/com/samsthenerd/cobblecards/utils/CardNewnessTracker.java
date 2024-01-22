package com.samsthenerd.cobblecards.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.samsthenerd.cobblecards.network.CobbleCardsNetwork;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.NetworkManager.PacketContext;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;

/**
 * This is a very silly solution to a very silly problem.
 * We need to track what cards haven't been viewed yet by a player.
 * 
 * Player opens a pack, cards are added to the inventory with some random UUID.
 * 
 * A player can then view the card, we detect this with the `append tooltip` on the client, which adds the UUID to VIEWED_CARDS clientside.
 * It also sends a packet to the server saying "this uuid was viewed".
 * 
 * When the server receives this it adds the uuid to VIEWED_CARDS serverside to indicate that it's been viewed by a client
 * 
 * The next time this stack gets to some 'gatepoint', where we can check/modify its nbt data serverside, we check the NEW_CARDS for the uuid, if it's marked as viewed then we remove the UUID tag from the nbt. This should maybe also send a packet back to the clients to acknowledge it's been viewed so it can remove it from the set so it doesn't get too big, but that's a lower priority
 * Gatepoints would be inventory ticks and clicks
 */
public class CardNewnessTracker {
    private static final Set<UUID> VIEWED_CARDS = new HashSet<>();

    // @Environment(EnvType.CLIENT)
    public static void viewCard(UUID uuid){
        if(VIEWED_CARDS.add(uuid)){
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeUuid(uuid);
            NetworkManager.sendToServer(CobbleCardsNetwork.VIEW_CARD_C2S, buf);
        }
    }

    public static boolean isCardViewed(UUID uuid){
        return VIEWED_CARDS.contains(uuid);
    }

    public static void receiveCardView(PacketByteBuf buf, PacketContext context){
        UUID uuid = buf.readUuid();
        VIEWED_CARDS.add(uuid);
    }
}

package com.samsthenerd.cobblecards.pokedata.packs;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.samsthenerd.cobblecards.CobbleCards;
import com.samsthenerd.cobblecards.clientmisc.DynamicModelOverride;
import com.samsthenerd.cobblecards.pokedata.Card;
import com.samsthenerd.cobblecards.pokedata.CardPack;
import com.samsthenerd.cobblecards.pokedata.CardSet;
import com.samsthenerd.cobblecards.pokedata.PullSlot;
import com.samsthenerd.cobblecards.utils.WHTexture;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SetPack extends CardPack {
    private CardSet set;

    public SetPack(CardSet set){
        this.set = set;
    }

    public CardSet getSet(){
        return set;
    }

    @Override
    public Text getPackName(){
        return Text.literal(set.name);
    }

    @Override
    public Text getSeries(){
        return Text.literal(set.seriesName);
    }

    @Override
    public String getType(){
        return "set";
    }

    private static List<PullSlot> FULL_SLOTS = List.of(
        PullSlot.COMMON,
        PullSlot.COMMON,
        PullSlot.COMMON,
        PullSlot.COMMON,
        PullSlot.UNCOMMON,
        PullSlot.UNCOMMON,
        PullSlot.UNCOMMON,
        PullSlot.REV_HOLO,
        PullSlot.REV_HOLO,
        PullSlot.RARE,
        PullSlot.BASIC_ENERGY
    );

    private static List<PullSlot> FULL_SLOTS_GOD_PACK = List.of(
        PullSlot.REV_HOLO,
        PullSlot.REV_HOLO,
        PullSlot.REV_HOLO,
        PullSlot.REV_HOLO,
        PullSlot.REV_HOLO,
        PullSlot.RARE,
        PullSlot.RARE,
        PullSlot.RARE,
        PullSlot.RARE,
        PullSlot.RARE,
        PullSlot.BASIC_ENERGY
    );

    private static List<PullSlot> SMALL_PACK = List.of(
        PullSlot.ANY,
        PullSlot.ANY,
        PullSlot.ANY
    );

    private static List<PullSlot> TINY_PACK = List.of(
        PullSlot.ANY
    );


    public List<PullSlot> getPullSlots(){
        int cardCount = getPossibleCards().size();
        if(cardCount < 25){
            return SMALL_PACK;
        }
        if(cardCount < 10){
            return TINY_PACK;
        }
        return FULL_SLOTS; //TODO: add chance of god packs maybe
    }

    // TODO: make this grab energies from wherever it needs them
    public Set<Card> getPossibleCards(){
        Set<Card> cards = set.getCards();
        for(CardSet connectedSet : set.findConnectedSets()){
            cards.addAll(connectedSet.getCards());
        }
        return cards;
    }

    @Nullable
    public WHTexture getLogo(){
        return set.getLogo();
    }

    @Nullable
    public WHTexture getSymbol(){
        return set.getSymbol();
    }

    @Override
    public DynamicModelOverride getModelOverride(ItemStack stack){
        return DynamicModelOverride.fromTextures(List.of(new Identifier(CobbleCards.MOD_ID, "item/packs/" + set.id)), new Identifier("cobblecards", "packs/" + set.id));
    }

    public static SetPack fromNbt(NbtCompound nbt){
        return new SetPack(CardSet.get(nbt.getString("set")));
    }

    public static SetPack fromJson(JsonObject json){
        return new SetPack(CardSet.get(json.get("set").getAsString()));
    }

    @Override
    public NbtCompound toNbt(){
        NbtCompound nbt = super.toNbt();
        nbt.putString("set", set.id);
        return nbt;
    }
}

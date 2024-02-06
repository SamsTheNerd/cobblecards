package com.samsthenerd.cobblecards.pokedata.packs;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.samsthenerd.cobblecards.pokedata.Card;
import com.samsthenerd.cobblecards.pokedata.CardPack;
import com.samsthenerd.cobblecards.pokedata.CardQuery.NumberIndexerQuery;
import com.samsthenerd.cobblecards.pokedata.PullSlot;
import com.samsthenerd.cobblecards.utils.WHTexture;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

public class PokedexPack extends CardPack {

    public final int dexNum;

    public PokedexPack(int dexNum){
        this.dexNum = dexNum;
    }

    // TODO: figure out how to get the name of the pokemon
    @Override
    public Text getPackName(){
        return Text.literal("Pokedex");
    }

    @Override
    public Text getSeries(){
        return Text.literal("Pokedex");
    }

    @Override
    public String getType(){
        return "pokedex";
    }

    // TODO: figure out how we want to do the slots for this
    public List<PullSlot> getPullSlots(){
        return List.of(PullSlot.COMMON);
    }

    public Set<Card> getPossibleCards(){
        return new NumberIndexerQuery("dexNum", dexNum, dexNum).getCards();
    }

    @Nullable
    public WHTexture getLogo(){
        return null;
    }

    @Nullable
    public WHTexture getSymbol(){
        return null;
    }

    public static PokedexPack fromNbt(NbtCompound nbt){
        return new PokedexPack(nbt.getInt("dexNum"));
    }

    public static PokedexPack fromJson(JsonObject json){
        return new PokedexPack(json.get("dexNum").getAsInt());
    }

    @Override
    public NbtCompound toNbt(){
        NbtCompound nbt = super.toNbt();
        nbt.putInt("dexNum", dexNum);
        return nbt;
    }
}

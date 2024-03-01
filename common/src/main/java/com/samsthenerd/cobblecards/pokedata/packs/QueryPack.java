package com.samsthenerd.cobblecards.pokedata.packs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.samsthenerd.cobblecards.pokedata.Card;
import com.samsthenerd.cobblecards.pokedata.CardPack;
import com.samsthenerd.cobblecards.pokedata.CardQuery;
import com.samsthenerd.cobblecards.pokedata.PullSlot;
import com.samsthenerd.cobblecards.utils.Spritelike;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

public class QueryPack extends CardPack {
    private CardQuery query;
    private String name = "Query";
    private String series = "Query";
    private List<PullSlot> pullSlots = new ArrayList<>();
    private Spritelike logo = null;
    private Spritelike symbol = null;

    public QueryPack(CardQuery query){
        this.query = query;
    }

    public CardQuery getQuery(){
        return query;
    }

    @Override
    public Text getPackName(){
        return Text.literal(name);
    }

    @Override
    public Text getSeries(){
        return Text.literal(series);
    }

    @Override
    public String getType(){
        return "query";
    }

    public List<PullSlot> getPullSlots(){
        return new ArrayList<>();
    }

    public Set<Card> getPossibleCards(){
        return query.getCards();
    }

    @Nullable
    public Spritelike getLogo(){
        return logo;
    }

    @Nullable
    public Spritelike getSymbol(){
        return symbol;
    }

    // TODO: store most of this data elsewhere (perhaps closer to datapack stuff) and let the nbt just be a reference to that - that way it's easier to update the image or the description or name or whatever, maybe ?
    public static QueryPack fromNbt(NbtCompound nbt){
        return null;
    }

    public static QueryPack fromJson(JsonObject json){
        CardQuery query = CardQuery.fromJson(json.get("query").getAsJsonObject());
        QueryPack pack = new QueryPack(query);
        if(json.has("name")){
            pack.name = json.get("name").getAsString();
        }
        if(json.has("series")){
            pack.series = json.get("series").getAsString();
        }
        if(json.has("logo")){
            pack.logo = Spritelike.fromJson(json.get("logo"));
        }
        if(json.has("symbol")){
            pack.symbol = Spritelike.fromJson(json.get("symbol"));
        }
        if(json.has("slots")){
            for(JsonElement slotElem : json.getAsJsonArray("slots")){
                pack.pullSlots.add(PullSlot.get(slotElem.getAsString()));
            }
        }
        return pack;
    }

    @Override
    public NbtCompound toNbt(){
        NbtCompound nbt = super.toNbt();
        return nbt;
    }
}

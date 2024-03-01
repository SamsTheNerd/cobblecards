package com.samsthenerd.cobblecards.pokedata;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonObject;
import com.samsthenerd.cobblecards.pokedata.datagenish.DataFetcher;
import com.samsthenerd.cobblecards.utils.Spritelike;
import com.samsthenerd.cobblecards.utils.URLSprite;

import net.minecraft.util.Identifier;

// read in from a json file usually
public class CardSet {
    public final String id;
    public final String name;
    public final String seriesName;
    public final int cardCount;
    public final int printedCardCount;
    public final Date releaseDate;
    private Set<CardSet> connectedSets = null;

    private CardSet(String id, String name, String seriesName, int cardCount, int printedCardCount, Date releaseDate){
        this.id = id;
        this.name = name;
        this.seriesName = seriesName;
        this.cardCount = cardCount;
        this.printedCardCount = printedCardCount;
        this.releaseDate = releaseDate;
    }

    protected static CardSet fromJson(JsonObject json){
        String id = json.get("id").getAsString();
        String name = json.get("name").getAsString();
        String seriesName = json.get("series").getAsString();
        int cardCount = json.get("total").getAsInt();
        int printedCardCount = json.get("printedTotal").getAsInt();
        Date releaseDate;
        try{
            releaseDate = DataFetcher.RELEASE_DATE_FORMAT.parse(json.get("releaseDate").getAsString());
        } catch (Exception e){
            throw new IllegalArgumentException("Invalid release date for set " + id + ": " + json.get("releaseDate").getAsString());
        }
        CardSet set = new CardSet(id, name, seriesName, cardCount, printedCardCount, releaseDate);
        return set;
    }

    private final static Map<String, CardSet> CARD_SET_HOLDER = Collections.synchronizedMap(new HashMap<>());

    // yay interning it
    protected static void add(CardSet set){
        if(!CARD_SET_HOLDER.containsKey(set.id)){
            CARD_SET_HOLDER.put(set.id, set);
        }
    }
    
    public static CardSet get(String id){
        if(CARD_SET_HOLDER.containsKey(id)){
            return CARD_SET_HOLDER.get(id);
        }
        return null;
    }

    public Set<Card> getCards(){
        return new HashSet<>(CardHolder.PRIMARY.getCards(CardHolder.SET_INDEXER, id));
    }

    public Spritelike getLogo(){
        return new URLSprite(String.format("https://images.pokemontcg.io/%s/logo.png", id), new Identifier("cobblecards", "setlogo/" + id.toLowerCase()));
    }

    public Spritelike getSymbol(){
        return new URLSprite(String.format("https://images.pokemontcg.io/%s/symbol.png", id), new Identifier("cobblecards", "setsymbol/" + id.toLowerCase()));
    }

    // sets like trainer galleries or shiny vaults or whatever where they're clearly the same set but separated for whatever reason
    public Set<CardSet> findConnectedSets(){
        if(connectedSets != null){
            return new HashSet<>(connectedSets);
        }
        connectedSets = new HashSet<>();
        String nameMatcher = "^" + name + ".*";
        for(CardSet otherSet : CARD_SET_HOLDER.values()){
            if(otherSet.id.equals(id)){
                continue;
            }
            if(otherSet.releaseDate.equals(releaseDate) && otherSet.name.matches(nameMatcher)){
                connectedSets.add(otherSet);
            }
        }
        return connectedSets;
    }

    public JsonObject toJson(){
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("name", name);
        json.addProperty("series", seriesName);
        json.addProperty("total", cardCount);
        json.addProperty("printedTotal", printedCardCount);
        return json;
    }
}

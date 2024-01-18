package com.samsthenerd.cobblecards.pokedata;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.samsthenerd.cobblecards.pokedata.datagenish.DataFetcher;

// read in from a json file usually
public class CardSet {
    public final String id;
    public final String name;
    public final String seriesName;
    public final int cardCount;
    public final int printedCardCount;
    public final Date releaseDate;
    private String idFormatter = "%d";

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
        if(json.has("idFormatter")){
            set.idFormatter = json.get("idFormatter").getAsString();
        }
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

    // some card sets have different IDs, like instead of sv1-1,sv1-2,..,sv1-3, you have swsh45sv-SV001, swsh45sv-SV002,...
    // only returns the card id part, not the set id with it
    // should get the card and ask it for its id though
    protected String getCardId(int num){
        return String.format(idFormatter, num);
    }

    public JsonObject toJson(){
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("name", name);
        json.addProperty("series", seriesName);
        json.addProperty("total", cardCount);
        json.addProperty("printedTotal", printedCardCount);
        json.addProperty("idFormatter", idFormatter);
        return json;
    }
}

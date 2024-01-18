package com.samsthenerd.cobblecards.pokedata;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.samsthenerd.cobblecards.CobbleCards;

public class Card {
    public final String setId;
    public final Optional<String> cardId; // cardId is included if it's different from what you'd expect it to be based on the cardNum - doesn't include the set id
    public final int cardNum; // not necessarily unique due to alt arts

    public final String name;
    public final String artist;
    private final Set<Integer> pokedexNumbers;
    private final Set<String> types; // could probably be made into an enum or something
    public final String superType; // pokemon, trainer, energy
    private final Set<String> subTypes;
    public final String flavorText; // I think it's neat to have

    // you should only be using this to read in from data json files
    protected static Card fromJson(JsonObject json){
        String setId = json.get("set").getAsString();
        int cardNum = json.get("number").getAsInt();
        Optional<String> cardId = Optional.empty();
        if(json.has("cardId")){
            cardId = Optional.of(json.get("cardId").getAsString());
        }
        String name = json.get("name").getAsString();
        String artist = "";
        if(json.has("artist")){
            artist = json.get("artist").getAsString();
        } else {
            CobbleCards.logPrint("Card " + setId + "-" + cardNum + " has no artist");
        }
        String superType = json.get("supertype").getAsString();
        String flavorText = "";
        if(json.has("flavorText")){
            flavorText = json.get("flavorText").getAsString();
        }
        Set<String> types = new HashSet<>();
        if(json.has("types")){
            for(JsonElement typeElem : json.getAsJsonArray("types")){
                types.add(typeElem.getAsString());
            }
        }
        Set<String> subTypes = new HashSet<>();
        if(json.has("subtypes")){
            for(JsonElement subTypeElem : json.getAsJsonArray("subtypes")){
                subTypes.add(subTypeElem.getAsString());
            }
        } else {
            CobbleCards.logPrint("Card " + setId + "-" + cardNum + " has no subtypes");
        }
        Set<Integer> dexNums = new HashSet<>();
        if(json.has("pokedexNumbers")){
            for(JsonElement dexNumElem : json.getAsJsonArray("pokedexNumbers")){
                dexNums.add(dexNumElem.getAsInt());
            }
        }
        return new Card(setId, cardId, cardNum, name, artist, dexNums, types, superType, subTypes, flavorText);
    }

    private Card(String setId, Optional<String> cardId, int cardNum, String name, String artist, Set<Integer> pokedexNumbers, Set<String> types, 
        String superType, Set<String> subTypes, String flavorText) {
        this.setId = setId;
        this.cardId = cardId;
        this.cardNum = cardNum;
        this.name = name;
        this.artist = artist;
        this.pokedexNumbers = Set.copyOf(pokedexNumbers);
        this.types = Set.copyOf(types);
        this.superType = superType;
        this.subTypes = Set.copyOf(subTypes);
        this.flavorText = flavorText;
    }

    public String fullId(){
        return setId + "-" + cardId();
    }

    public String cardId(){
        if(cardId.isPresent()){
            return cardId.get();
        }
        return getSet().getCardId(cardNum);
    }

    public CardSet getSet(){
        return CardSet.get(setId);
    }

    public String getImageUrl(boolean hires){
        return "https://images.pokemontcg.io/" + setId + "/" + cardNum + (hires ? "_hires" : "") + ".png";
    }

    public String getImageUrl(){
        return getImageUrl(true);
    }

    // presumably because of the multi pokemon cards
    public Set<Integer> getPokedexNumbers(){
        return pokedexNumbers;
    }

    public Set<String> getTypes(){
        return types;
    }

    public Set<String> getSubTypes(){
        return subTypes;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Card){
            Card c = (Card) o;
            return c.setId.equals(setId) && c.cardNum == cardNum;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return fullId().hashCode();
    }
}

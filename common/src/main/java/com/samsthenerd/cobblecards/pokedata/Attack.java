package com.samsthenerd.cobblecards.pokedata;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Attack {
    public final String name;
    public final String description;
    public final String damage;
    private final List<CardType> cost;
    
    public Attack(String name, String description, String damage, List<CardType> cost){
        this.name = name;
        this.description = description;
        this.damage = damage;
        this.cost = cost;
    }

    public List<CardType> getCost(){
        return cost;
    }

    public static Attack fromJson(JsonObject json){
        String name = json.get("name").getAsString();
        String description = "";
        if(json.has("description")){
            description = json.get("text").getAsString();
        }
        String damage = "0";
        if(json.has("damage")){
            damage = json.get("damage").getAsString();
        }
        List<CardType> cost = new ArrayList<>();
       if(json.has("cost")){
            for(JsonElement costElem : json.getAsJsonArray("cost")){
                cost.add(CardType.of(costElem.getAsString()));
            }
       }
        return new Attack(name, description, damage, cost);
    }
}

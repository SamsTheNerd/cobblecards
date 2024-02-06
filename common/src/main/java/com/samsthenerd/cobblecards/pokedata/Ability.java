package com.samsthenerd.cobblecards.pokedata;

import com.google.gson.JsonObject;

public class Ability {
    public final String name;
    public final String text;
    public final String type;

    public Ability(String name, String text, String type){
        this.name = name;
        this.text = text;
        this.type = type;
    }

    public static Ability fromJson(JsonObject json, String type){
        String name = json.get("name").getAsString();
        String text = json.get("text").getAsString();
        if(json.has("type")){
            type = json.get("type").getAsString();
        }
        return new Ability(name, text, type);
    }

    public static Ability fromJson(JsonObject json){
        return fromJson(json, "");
    }
}

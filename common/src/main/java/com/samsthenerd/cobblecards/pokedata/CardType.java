package com.samsthenerd.cobblecards.pokedata;

public class CardType {
    public final String name;

    // TODO: add a type enum for the different types - maybe - just this for now because bleh
    private CardType(String name){
        this.name = name;
    }

    public static CardType of(String name){
        return new CardType(name);
    }
}

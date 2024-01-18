package com.samsthenerd.cobblecards.pokedata;

import java.util.Comparator;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nullable;

/**
 * A class for indexing cards.
 * We want to be able to quickly search for cards with a variety of different fields, some examples:
 * - Find all cards with Charizard in them
 * - Find all mega cards
 * - Find all cards in a set
 * - Find all cards by an artist
 * Instead of manually having maps for each field, we can make an indexer and let the CardHolder handle the implementation.
 */
public interface CardIndexer<T> {
    // gives a key to use for storing this index, for example "artist", these should be unique
    public String getFieldKey();

    // some feilds might make sense to keep sorted for easier key iteration, like card numbers or names.
    // if this is null the keys won't be sorted or returned in any particular order. 
    // Unsorted may be ever so slightly faster, using a hashmap instead of a treemap.
    @Nullable
    public Comparator<T> getSortFunction();

    // this extract the value to be indexed from the card. For example, if the field is "artist", this should return the artist name.
    // returns a set since some fields may have multiple values, like types.
    public Set<T> getValueFromCard(Card card);

    public static class SimpleCardIndexer<T> implements CardIndexer<T>{
        private final String fieldKey;
        private final Comparator<T> sortFunction;
        private final Function<Card, Set<T>> valueExtractor;

        public SimpleCardIndexer(String fieldKey, Comparator<T> sortFunction, Function<Card, Set<T>> valueExtractor){
            this.fieldKey = fieldKey;
            this.sortFunction = sortFunction;
            this.valueExtractor = valueExtractor;
        }

        public SimpleCardIndexer(String fieldKey, Function<Card, Set<T>> valueExtractor){
            this(fieldKey, null, valueExtractor);
        }


        public String getFieldKey(){
            return fieldKey;
        }

        public Comparator<T> getSortFunction(){
            return sortFunction;
        }

        public Set<T> getValueFromCard(Card card){
            return valueExtractor.apply(card);
        }
    }
}

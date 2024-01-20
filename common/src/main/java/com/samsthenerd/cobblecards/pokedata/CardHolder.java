package com.samsthenerd.cobblecards.pokedata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.samsthenerd.cobblecards.pokedata.CardIndexer.SimpleCardIndexer;

// a class for holding, retrieving, and searching for cards
public class CardHolder {
    protected static Map<String, CardIndexer<?>> INDEXERS = new HashMap<>();

    // important one, used for actually being able to get an arbitrary card
    public static final CardIndexer<String> FULL_ID_INDEXER = addIndexer(new SimpleCardIndexer<String>("id", card -> Set.of(card.fullId())));
    public static final CardIndexer<String> SET_INDEXER = addIndexer(new SimpleCardIndexer<String>("set", card -> Set.of(card.setId)));

    public static final CardIndexer<String> NAME_INDEXER = addIndexer(new SimpleCardIndexer<String>("name", card -> Set.of(card.name)));
    public static final CardIndexer<String> ARTIST_INDEXER = addIndexer(new SimpleCardIndexer<String>("artist", card -> Set.of(card.artist)));
    public static final CardIndexer<String> SUPER_TYPE_INDEXER = addIndexer(new SimpleCardIndexer<String>("supertype", card -> Set.of(card.superType)));
    public static final CardIndexer<Integer> DEX_NUM_INDEXER = addIndexer(new SimpleCardIndexer<Integer>("dexNum", Card::getPokedexNumbers));
    public static final CardIndexer<String> SUB_TYPES_INDEXER = addIndexer(new SimpleCardIndexer<String>("subtypes", Card::getSubTypes));
    public static final CardIndexer<String> TYPES_INDEXER = addIndexer(new SimpleCardIndexer<String>("types", Card::getTypes));

    // used to hold all our cards. arguably this class should just be a singleton only but whatever, if you want to use it for something else that's fine i guess
    public static CardHolder PRIMARY = new CardHolder();

    public CardHolder(){
        for(CardIndexer<?> indexer : INDEXERS.values()){
            if(indexer.getSortFunction() != null){
                INDICES.put(indexer.getFieldKey(), new HashMap<>());
            } else {
                INDICES.put(indexer.getFieldKey(), new TreeMap<>(indexer.getSortFunction()));
            }
        }
    }

    public Card getCard(String fullId){
        Set<Card> cards = getCards(FULL_ID_INDEXER, fullId);
        if(cards.size() == 0) return null;
        return cards.iterator().next();
    }

    // gets auto indexed by the indexers
    @SuppressWarnings("unchecked")
    protected void addCard(Card card){
        for(CardIndexer<?> indexer : INDEXERS.values()){
            // shouldn't but just in case
            if(!INDICES.containsKey(indexer.getFieldKey())){
                continue;
            }
            for(Object val : indexer.getValueFromCard(card)){
                Map index = INDICES.get(indexer.getFieldKey());
                if(!index.containsKey(val)){
                    index.put(val, new HashSet<>());
                }
                ((Set<Card>)index.get(val)).add(card);
            }
        }
    }

    public <T> Set<Card> getCards(CardIndexer<T> indexer, T searchVal){
        return getCards(indexer.getFieldKey(), searchVal);
    }

    public <T> Set<Card> getCards(String fieldKey, T searchVal){
        if(!INDICES.containsKey(fieldKey)){
            return new HashSet<>();
        }
        Set<Card> foundCards = INDICES.get(fieldKey).get(searchVal);
        if(foundCards == null) return new HashSet<>(); // empty set
        return foundCards;
    }
    
    // ( not supporting String input since return generic becomes unknown )
    // returns all keys for a given indexer. for example, all artist names for the artist indexer
    @SuppressWarnings("unchecked") // it's fine,,,,
    public <T> Set<T> getKeys(CardIndexer<T> indexer){
        if(!INDICES.containsKey(indexer.getFieldKey())){
            return new HashSet<>();
        }
        return (Set<T>)(INDICES.get(indexer.getFieldKey()).keySet());
    }



    public static <T> CardIndexer<T> addIndexer(CardIndexer<T> indexer){
        INDEXERS.put(indexer.getFieldKey(), indexer);
        return indexer;
    }

    private final Map<String, Map<?, Set<Card>>> INDICES = new HashMap<>();

}

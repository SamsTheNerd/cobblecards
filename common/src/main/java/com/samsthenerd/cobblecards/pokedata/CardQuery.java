package com.samsthenerd.cobblecards.pokedata;

import static com.samsthenerd.cobblecards.pokedata.CardQuery.registerDeserializer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.google.gson.JsonObject;

// provides an easier (and serializable method for interacting with the CardIndexers)
// follows a stream-ish design
public interface CardQuery {
    public Set<Card> getCards(CardHolder holder);

    public default Set<Card> getCards(){
        return getCards(CardHolder.PRIMARY);
    }

    public JsonObject toJson();

    public static void registerDeserializer(String key, Function<JsonObject, CardQuery> deserializer){
        DESERIALIZERS.put(key, deserializer);
    }

    public static CardQuery fromJson(JsonObject json){
        String key = json.get("key").getAsString();
        return DESERIALIZERS.get(key).apply(json);
    }

    // intersection of the two queries
    public default CardQuery and(CardQuery other){
        return new CombinationQuery("and", this, other);
    }

    public default CardQuery or(CardQuery other){
        return new CombinationQuery("or", this, other);
    }

    public static final Map<String, Function<JsonObject, CardQuery>> DESERIALIZERS = new HashMap<>();

    public static class CombinationQuery implements CardQuery{
        /**
         * EXAMPLE JSON: {
         *  "type": "combinator-and",
         *  "left": {...},
         *  "right": {...}
         * }
         */
        private final BiFunction<Set<Card>, Set<Card>, Set<Card>> combinator;
        private String combinatorKey; // for serialization - 
        private final CardQuery left;
        private final CardQuery right;

        static {
            registerCombinator("and", (left, right) -> {
                Set<Card> result = new HashSet<>(left);
                result.retainAll(right);
                return result;
            });
            registerCombinator("or", (left, right) -> {
                Set<Card> result = new HashSet<>(left);
                result.addAll(right);
                return result;
            });
        }

        private static final Map<String, BiFunction<Set<Card>, Set<Card>, Set<Card>>> COMBINATORS = new HashMap<>();

        // need to limit them for ser/deser
        public static void registerCombinator(String key, BiFunction<Set<Card>, Set<Card>, Set<Card>> combinator){
            COMBINATORS.put(key, combinator);
            registerDeserializer("combinator-" + key, (json) -> {
                CardQuery left = CardQuery.fromJson(json.get("left").getAsJsonObject());
                CardQuery right = CardQuery.fromJson(json.get("right").getAsJsonObject());
                return new CombinationQuery(key, left, right);
            });
        }

        public CombinationQuery(String combinatorKey, CardQuery left, CardQuery right){
            this.combinatorKey = combinatorKey;
            this.combinator = COMBINATORS.get(combinatorKey);
            this.left = left;
            this.right = right;
        }

        public Set<Card> getCards(CardHolder holder){
            return combinator.apply(left.getCards(holder), right.getCards(holder));
        }

        public JsonObject toJson(){
            JsonObject json = new JsonObject();
            json.addProperty("type", "combinator-" + combinatorKey);
            json.add("left", left.toJson());
            json.add("right", right.toJson());
            return json;

        }
    }

    public static class StringIndexerQuery implements CardQuery{
        private final String indexerKey;
        private final String searchVal;
        private final boolean exact;

        static{
            registerDeserializer("string-indexer", (json) -> {
                String indexerKey = json.get("field").getAsString();
                if(json.has("value")){
                    String searchVal = json.get("value").getAsString();
                    return new StringIndexerQuery(indexerKey, searchVal, true);
                }
                String searchVal = json.get("search").getAsString();
                return new StringIndexerQuery(indexerKey, searchVal, false);
            });
        }

        public StringIndexerQuery(String indexerKey, String searchVal, boolean exact){
            this.indexerKey = indexerKey;
            this.searchVal = searchVal;
            this.exact = exact;
        }

        @SuppressWarnings("unchecked")
        public Set<Card> getCards(CardHolder holder){
            if(exact){
                return holder.getCards(indexerKey, searchVal);
            } else {
                // treat it as regex and TODO: cache the results
                Set<Card> foundCards = new HashSet<>();
                try{
                    Set<String> values = (Set<String>)(holder.getKeys(indexerKey));
                    for(String val : values){
                        if(val.matches(searchVal)){
                            foundCards.addAll(holder.getCards(indexerKey, val));
                        }
                    }
                } catch (Exception e){}
                return foundCards;
            }
        }

        public JsonObject toJson(){
            JsonObject json = new JsonObject();
            json.addProperty("type", "string-indexer");
            json.addProperty("field", indexerKey);
            if(exact){
                json.addProperty("value", searchVal);
            } else {
                json.addProperty("search", searchVal);
            }
            return json;
        }
    }

    public static class NumberIndexerQuery implements CardQuery{
        private final String indexerKey;
        private final Number minVal;
        private final Number maxVal;

        static{
            registerDeserializer("number-indexer", (json) -> {
                String indexerKey = json.get("field").getAsString();
                if(json.has("value")){
                    Number num = json.get("value").getAsNumber();
                    return new NumberIndexerQuery(indexerKey, num, num);   
                }
                Number minVal = json.get("min").getAsNumber();
                Number maxVal = json.get("max").getAsNumber();
                return new NumberIndexerQuery(indexerKey, minVal, maxVal);
            });
        }

        public NumberIndexerQuery(String indexerKey, Number minVal, Number maxVal){
            this.indexerKey = indexerKey;
            this.minVal = minVal;
            this.maxVal = maxVal;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Set<Card> getCards(CardHolder holder){
            if(minVal.equals(maxVal)){
                return holder.getCards(indexerKey, minVal);
            } else {
                Set<Card> foundCards = new HashSet<>();
                try{
                    Set<Number> values = (Set<Number>)(holder.getKeys(indexerKey));
                    for(Number val : values){
                        if(val.doubleValue() >= minVal.doubleValue() && val.doubleValue() <= maxVal.doubleValue()){
                            foundCards.addAll(holder.getCards(indexerKey, val));
                        }
                    }
                } catch (Exception e){}
                return foundCards;
            }
        }

        public JsonObject toJson(){
            JsonObject json = new JsonObject();
            json.addProperty("type", "number-indexer");
            json.addProperty("field", indexerKey);
            if(minVal.equals(maxVal)){
                json.addProperty("value", minVal);
            } else {
                json.addProperty("min", minVal);
                json.addProperty("max", maxVal);
            }
            return json;
        }
    }
}

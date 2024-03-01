package com.samsthenerd.cobblecards.pokedata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.samsthenerd.cobblecards.CobbleCards;
import com.samsthenerd.cobblecards.utils.Spritelike;
import com.samsthenerd.cobblecards.utils.URLSprite;

import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class Card {
    public final String setId;
    public final String cardNum; // this is a string because the pokemon company doesn't understand what a number is. 

    public final String name;
    public final String artist;
    private final Set<Integer> pokedexNumbers;
    private final Set<CardType> types; // could probably be made into an enum or something
    public final String superType; // pokemon, trainer, energy
    private final Set<String> subTypes;
    public final String flavorText; // I think it's neat to have
    private final CardRarity rarity; 
    public final String hp;
    public final String level;
    public final String evolvesFrom;
    private final List<String> evolvesTo;
    private final List<Ability> abilities; // and ancient traits
    private final List<String> rules;
    private final List<Pair<CardType, String>> weaknesses;
    private final List<Pair<CardType, String>> resistances;
    private final List<CardType> retreatCost;
    private final List<Attack> attacks;

    // you should only be using this to read in from data json files
    // TODO: could probably neaten this all up a bit
    protected static Card fromJson(JsonObject json){
        String setId = json.get("set").getAsString();
        String cardNum = json.get("cardNum").getAsString();
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
        Set<CardType> types = new HashSet<>();
        if(json.has("types")){
            for(JsonElement typeElem : json.getAsJsonArray("types")){
                types.add(CardType.of(typeElem.getAsString()));
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
        CardRarity rarity = CardRarity.RARE;
        if(json.has("rarity")){
            rarity = CardRarity.get(json.get("rarity").getAsString());
        }

        String hp = "";
        if(json.has("hp")){
            hp = json.get("hp").getAsString();
        }

        String level = "";
        if(json.has("level")){
            level = json.get("level").getAsString();
        }

        String evolvesFrom = "";
        if(json.has("evolvesFrom")){
            evolvesFrom = json.get("evolvesFrom").getAsString();
        }

        List<String> evolvesTo = new ArrayList<>();
        if(json.has("evolvesTo")){
            for(JsonElement evolvesToElem : json.getAsJsonArray("evolvesTo")){
                evolvesTo.add(evolvesToElem.getAsString());
            }
        }

        List<Ability> abilities = new ArrayList<>();
        if(json.has("abilities")){
            for(JsonElement abilityElem : json.getAsJsonArray("abilities")){
                abilities.add(Ability.fromJson(abilityElem.getAsJsonObject()));
            }
        }
        if(json.has("ancientTraits")){
            abilities.add(Ability.fromJson(json.get("ancientTraits").getAsJsonObject(), "ancientTraits"));
        }

        List<String> rules = new ArrayList<>();
        if(json.has("rules")){
            for(JsonElement ruleElem : json.getAsJsonArray("rules")){
                rules.add(ruleElem.getAsString());
            }
        }

        List<Pair<CardType, String>> weaknesses = new ArrayList<>();
        if(json.has("weaknesses")){
            for(JsonElement weaknessElem : json.getAsJsonArray("weaknesses")){
                weaknesses.add(new Pair<>(CardType.of(weaknessElem.getAsJsonObject().get("type").getAsString()), weaknessElem.getAsJsonObject().get("value").getAsString()));
            }
        }
        List<Pair<CardType, String>> resistances = new ArrayList<>();
        if(json.has("resistances")){
            for(JsonElement resistanceElem : json.getAsJsonArray("resistances")){
                resistances.add(new Pair<>(CardType.of(resistanceElem.getAsJsonObject().get("type").getAsString()), resistanceElem.getAsJsonObject().get("value").getAsString()));
            }
        }

        List<CardType> retreatCost = new ArrayList<>();
        if(json.has("retreatCost")){
            for(JsonElement retreatCostElem : json.getAsJsonArray("retreatCost")){
                retreatCost.add(CardType.of(retreatCostElem.getAsString()));
            }
        }

        List<Attack> attacks = new ArrayList<>();
        if(json.has("attacks")){
            for(JsonElement attackElem : json.getAsJsonArray("attacks")){
                attacks.add(Attack.fromJson(attackElem.getAsJsonObject()));
            }
        }

        return new Card(setId, cardNum, name, artist, dexNums, types, superType, subTypes, flavorText, rarity, 
            hp, level, evolvesFrom, evolvesTo, abilities, rules, weaknesses, resistances, retreatCost, attacks);
    }

    // Horrible constructor !! death !!
    private Card(String setId, String cardNum, String name, String artist, Set<Integer> pokedexNumbers, Set<CardType> types, 
        String superType, Set<String> subTypes, String flavorText, CardRarity rarity, String hp, String level, String evolvesFrom, 
        List<String> evolvesTo, List<Ability> abilities, List<String> rules, List<Pair<CardType, String>> weaknesses, 
        List<Pair<CardType, String>> resistances, List<CardType> retreatCost, List<Attack> attacks) {
        this.setId = setId;
        this.cardNum = cardNum;
        this.name = name;
        this.artist = artist;
        this.pokedexNumbers = Set.copyOf(pokedexNumbers);
        this.types = Set.copyOf(types);
        this.superType = superType;
        this.subTypes = Set.copyOf(subTypes);
        this.flavorText = flavorText;
        this.rarity = rarity;
        this.hp = hp;
        this.level = level;
        this.evolvesFrom = evolvesFrom;
        this.evolvesTo = List.copyOf(evolvesTo);
        this.abilities = List.copyOf(abilities);
        this.rules = List.copyOf(rules);
        this.weaknesses = List.copyOf(weaknesses);
        this.resistances = List.copyOf(resistances);
        this.retreatCost = List.copyOf(retreatCost);
        this.attacks = List.copyOf(attacks);
    }

    public static Card get(String fullId){
        return CardHolder.PRIMARY.getCard(fullId);
    }

    public String fullId(){
        return setId + "-" + cardId();
    }

    public String cardId(){
        return cardNum;
    }

    public CardSet getSet(){
        return CardSet.get(setId);
    }

    private String getImageUrl(boolean hires){
        return "https://images.pokemontcg.io/" + setId + "/" + cardNum + (hires ? "_hires" : "") + ".png";
    }

    public Spritelike getTexture(boolean hires){
        return new URLSprite(getImageUrl(hires), new Identifier("cobblecards", "pokecard/" + fullId().toLowerCase() + (hires ? "_hires" : "")));
    }

    public Spritelike getTexture(){
        return getTexture(true);
    }

    // presumably because of the multi pokemon cards
    public Set<Integer> getPokedexNumbers(){
        return pokedexNumbers;
    }

    public Set<CardType> getTypes(){
        return types;
    }

    public Set<String> getStringyTypes(){
        Set<String> stringyTypes = new HashSet<>();
        for(CardType type : types){
            stringyTypes.add(type.toString());
        }
        return stringyTypes;
    }

    public Set<String> getSubTypes(){
        return subTypes;
    }

    public CardRarity getRarity(){
        if(rarity == null) return CardRarity.RARE;
        return rarity;
    }

    public List<String> getEvolvesTo(){
        return evolvesTo;
    }

    public List<Ability> getAbilities(){
        return abilities;
    }

    public List<String> getRules(){
        return rules;
    }

    public List<Pair<CardType, String>> getWeaknesses(){
        return weaknesses;
    }

    public List<Pair<CardType, String>> getResistances(){
        return resistances;
    }

    public List<CardType> getRetreatCost(){
        return retreatCost;
    }

    public List<Attack> getAttacks(){
        return attacks;
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

    /**
     * current heuristics for if it's a shiny:
     * name contains "shining", "★", or "radiant"
     * rarity indicates shiny
     * comes from a shiny vault set
     * has two types ? not sure if every card with multiple types is shiny or not though.
     * description mentions it being a different color - seems common for bw-era shinies "this extremely rare" seems to match only the expected shinies
     * 
     * currently don't have a good way to detect hgss and xy era shinies - probably just want to hardcode those ?
     */
}

package com.samsthenerd.cobblecards.pokedata;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.samsthenerd.cobblecards.pokedata.CardRarity.BaseRarity;

// a way to standardize different types of cards in a pack
public interface PullSlot {

    public String getName();

    // return 0-1, 0 is none, 1 is normal likelyhood, 0.5 is ~half as likely
    public double getWeight(CardRarity rarity);

    // so that it can modify individual weights, returning 2 makes it twice as likely
    public default double modifyWeight(Card card){
        return 1.0;
    }

    public static Map<String, PullSlot> EXISTING_SLOTS = new HashMap<>();

    public static PullSlot get(String name){
        if(EXISTING_SLOTS.containsKey(name)){
            return EXISTING_SLOTS.get(name);
        }
        return COMMON;
    }

    // yes it's silly,, i recognize and acknowledge that it's silly. go away
    public static PullSlot create(String name, Function<CardRarity, Double> weightRarityFunction, Function<Card, Double> weightCardFunction){
        PullSlot slot = new PullSlot(){
            @Override
            public double getWeight(CardRarity rarity) {
                return weightRarityFunction.apply(rarity);
            }

            @Override
            public double modifyWeight(Card card) {
                return weightCardFunction.apply(card);
            }

            @Override
            public String getName() {
                return name;
            }
        };
        EXISTING_SLOTS.put(name, slot);
        return slot;
    }

    public static PullSlot create(String name, Function<CardRarity, Double> weightRarityFunction){
        return create(name, weightRarityFunction, card -> 1.0);
    }


    public static final PullSlot ANY = create("Any", rarity -> {
        if(rarity.baseRarity == BaseRarity.COMMON) return 1.0;
        if(rarity.baseRarity == BaseRarity.UNCOMMON) return 0.75;
        return 0.25 / rarity.weight;
    });
    public static final PullSlot COMMON = create("Common", rarity -> rarity.baseRarity == BaseRarity.COMMON ? 1.0 : 0.0);
    public static final PullSlot UNCOMMON = create("Uncommon", rarity -> rarity.baseRarity == BaseRarity.UNCOMMON ? 1.0 : 0.0);
    public static final PullSlot BASIC_ENERGY = create("Basic Energy",
        rarity -> 1.0 / rarity.weight, 
        card -> card.superType.equals("Energy") && card.getSubTypes().contains("Basic") ? 1.0 : 0.0
    );
    // guarantees a rare but doesn't allow for anything better than base rare
    public static final PullSlot LIMITED_RARE = create("Limited Rare", rarity -> rarity.baseRarity == BaseRarity.RARE ? 1.0 : 0.0);
    public static final PullSlot RARE = create("Rare", rarity -> rarity.isRareLike() && !rarity.goesInHoloSlot ? 1.0/rarity.weight : 0.0);
    public static final PullSlot REV_HOLO = create("Reverse Holo", rarity -> {
        if(rarity.baseRarity == BaseRarity.COMMON || rarity.baseRarity == BaseRarity.UNCOMMON) return 1.0;
        if(rarity.baseRarity == BaseRarity.RARE) return 0.5;
        if(rarity.goesInHoloSlot) return 0.5 / rarity.weight;
        return 0.0;
    });
}

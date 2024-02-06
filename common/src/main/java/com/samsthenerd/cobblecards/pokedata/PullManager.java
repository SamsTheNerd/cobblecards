package com.samsthenerd.cobblecards.pokedata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.samsthenerd.cobblecards.CobbleCards;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;

// manages pack opening
public class PullManager {

    public static final Random RANDOM = new Random();

    // someone is opening the pack, nullable since idk could be automated or something ?
    public final @Nullable ServerPlayerEntity opener;

    private static final Set<Function<Card, Double>> WEIGHT_MODIFIERS = new HashSet<>();

    public PullManager(@Nullable ServerPlayerEntity opener){
        this.opener = opener;
    }

    // stack multiplicatively
    public static void addWeightModifier(Function<Card, Double> modifier){
        WEIGHT_MODIFIERS.add(modifier);
    }

    public static double getExternalWeightModifiers(Card card){
        double weight = 1;
        for(Function<Card, Double> modifier : WEIGHT_MODIFIERS){
            weight *= modifier.apply(card);
        }
        return weight;
    }

    // want to also pass slots here of some sort, still need to figure out exactly how that should be set up though, but probably just something to filter what rarities we want
    public List<Card> pullCards(Set<Card> possibleCards, List<PullSlot> slots){
        List<Card> pulledCards = new ArrayList<>();
        for(PullSlot slot : slots){
            // possible cards and their multiplicative weight modifiers
            Map<Card, Double> possibleCardsForSlot = new HashMap<>();
            Map<CardRarity, Double> allRarities = new HashMap<>();
            // one pass to get valid cards and their rarity multipliers
            for(Card card : possibleCards){
                if(pulledCards.contains(card)) continue; // don't add cards already pulled
                double rarityWeight = 0;
                if(allRarities.containsKey(card.getRarity())){
                    rarityWeight = allRarities.get(card.getRarity());
                } else {
                    rarityWeight = slot.getWeight(card.getRarity());
                    allRarities.put(card.getRarity(), rarityWeight);
                }
                if(rarityWeight > 0){
                    double weightModifier = slot.modifyWeight(card) * getExternalWeightModifiers(card);
                    if(weightModifier > 0){
                        possibleCardsForSlot.put(card, weightModifier);
                    }
                }
            }
            // so we can get an actual count of how many cards are in each rarity
            Map<CardRarity, Set<Card>> cardsByRarity = new HashMap<>();
            for(Entry<Card, Double> cardWeightPair : possibleCardsForSlot.entrySet()){
                Card card = cardWeightPair.getKey();
                CardRarity rarity = card.getRarity();
                if(cardWeightPair.getValue() <= 0 || allRarities.get(rarity) <= 0) continue;
                if(!cardsByRarity.containsKey(rarity)){
                    cardsByRarity.put(rarity, new HashSet<>());
                }
                cardsByRarity.get(rarity).add(card);
            }
            // now make a proper list of them
            List<Pair<Card, Double>> weightedCardList = new ArrayList<>();
            double totalPullWeight = 0;
            for(CardRarity rarity : cardsByRarity.keySet()){
                double totalRarityWeight = allRarities.get(rarity);
                double perCardWeight = totalRarityWeight / cardsByRarity.get(rarity).size();
                for(Card card : cardsByRarity.get(rarity)){
                    double finalCardWeight = perCardWeight * possibleCardsForSlot.get(card);
                    weightedCardList.add(new Pair<>(card, finalCardWeight));
                    totalPullWeight += finalCardWeight;
                }
            }
            CobbleCards.logPrint("Pulling " + slot.getName() + " slot:");
            for(CardRarity rarity : cardsByRarity.keySet()){
                double totalRarityWeight = allRarities.get(rarity);
                CobbleCards.logPrint("\t-" + rarity.name + ": " + totalRarityWeight + "/" + totalPullWeight + "(" + (int)(100*totalRarityWeight / totalPullWeight) + "%)");
            }
            double weightVal = RANDOM.nextDouble() * totalPullWeight;
            double accumWeight = 0;
            int i = 0;
            if(weightedCardList.size() == 0) continue;
            while(accumWeight < weightVal && i < weightedCardList.size()){
                accumWeight += weightedCardList.get(i).getRight();
                i++;
            }
            pulledCards.add(weightedCardList.get(i - 1).getLeft());
        }
        return pulledCards;
    }
}
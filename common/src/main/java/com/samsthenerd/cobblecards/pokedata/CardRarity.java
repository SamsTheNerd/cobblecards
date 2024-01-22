package com.samsthenerd.cobblecards.pokedata;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class CardRarity {
    
    private static final Map<String, CardRarity> EXISTING_RARITIES = new HashMap<>();

    public final String name; 
    public final boolean shiny; // idk for fun i guess
    public final BaseRarity baseRarity; // for easier categorization
    public final int weight; // for randomization of rare pulls.

    private CardRarity(String name, boolean shiny, BaseRarity baseRarity, int weight){
        this.name = name;
        this.shiny = shiny;
        this.baseRarity = baseRarity;
        this.weight = weight;
    }

    @Nullable
    public static CardRarity get(String name){
        return EXISTING_RARITIES.get(name);
    }

    protected static CardRarityBuilder builder(String name){
        return new CardRarityBuilder(name);
    }

    public boolean isRareLike(){
        return baseRarity.rareLike;
    }

    private static class CardRarityBuilder{
        private String name;
        private boolean shiny;
        private BaseRarity baseRarity = BaseRarity.COMMON;
        private int additionalWeight = 0;

        public CardRarityBuilder(String name){
            this.name = name;
        }

        public CardRarityBuilder fromBase(BaseRarity base){
            this.baseRarity = base;
            return this;
        }

        public CardRarityBuilder shiny(){
            this.shiny = true;
            return this;
        }

        public CardRarityBuilder additionalWeight(int weight){
            this.additionalWeight = weight;
            return this;
        }

        public CardRarity build(){
            CardRarity rarity = new CardRarity(name, shiny, baseRarity, baseRarity.weight + additionalWeight);
            EXISTING_RARITIES.put(name, rarity);
            return rarity;
        }
    }

    // basic rarities to build off of - maybe redundant but oh well
    // weights don't really matter for non rare ones i guess
    public static enum BaseRarity{
        COMMON("Common"),
        UNCOMMON("Uncommon"),
        RARE("Rare", 1),
        GIMMICK_RARE("Gimmick Rare", 4),
        EXTRA_RARE("Extra Rare", 12),
        PROMO("Promo");

        BaseRarity(String name){
            this(name, 1, false);
        }

        BaseRarity(String name, int weight){
            this(name, weight, true);
        }

        public final String name;
        public final int weight;
        public final boolean rareLike;

        BaseRarity(String name, int weight, boolean rareLike){
            this.name = name;
            this.weight = weight;
            this.rareLike = rareLike;
        }
    }

    /*
     * Documenting what rarities mean and with examples
     * explanation on sv changes: https://infinite.tcgplayer.com/article/How-Pok%C3%A9mon-Card-Rarities-Are-Changing-in-Scarlet-Violet/d3d9fbf9-5501-4c34-bdfd-4e47c9900312/
     * ------ 
     * Amazing Rare -- swsh era, rainbow paint splatter effect behind them
     * Classic Collection -- reprints of older cards for 25th anniversary - seems limited to that set
     * Common - common cards
     * Double Rare - new in sv, Regular ex cards
     * Hyper Rare - new in sv, gold cards
     * Illustration Rare - new in sv, alt art for regular pokemon cards
     * LEGEND - sideways half cards from hgss-ish
     * Promo - promo cards
     * Radiant Rare - Shiny pokemon
     * Rare - standard rare
     * Rare ACE - BW good trainer cards
     * Rare BREAK - Gold break cards
     * Rare Holo - maybe more rare than regular rare ? not sure it matters much since printing doesn't show up on these
     * Rare Holo EX - most EXs 
     * Rare Holo GX - GXs 
     * Rare Holo LV.X - lv.x cards
     * Rare Holo Star - older shiny cards
     * Rare Holo V - v cards 
     * Rare Holo VMAX - vmax
     * Rare Holo VSTAR - vstar
     * Rare Prime - spiky ones
     * Rare Prism Star - sm rhombus cards
     * Rare Rainbow - gay little rainbow cards ! - seems like mostly sm and swsh eras?
     * Rare Secret - usually gold but i guess more generally just secret cards that don't fall into other categories
     * Rare Shining - says "Shining" and is a shiny pokemon
     * Rare Shiny - shiny pokemon
     * Rare Shiny GX - shiny GX cards
     * Rare Ultra - seem like full art gx/v/EX/trainer cards?
     * Special Illustration Rare - new in sv, alt art for ex and trainers
     * Trainer Gallery Rare Holo - usually full art of pokemon with a popular trainer character, or sometimes just a trainer
     * Ultra Rare - new in sv, full arts 
     * Uncommon - just uncommon
     */
}

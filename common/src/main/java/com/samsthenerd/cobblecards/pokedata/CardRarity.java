package com.samsthenerd.cobblecards.pokedata;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.samsthenerd.cobblecards.CobbleCards;

public class CardRarity {
    
    private static final Map<String, CardRarity> EXISTING_RARITIES = new HashMap<>();

    public final String name; 
    public final boolean shiny; // idk for fun i guess
    public final BaseRarity baseRarity; // for easier categorization
    public final int weight; // for randomization of rare pulls.
    public final boolean goesInHoloSlot; // for cards that go in the holo slot

    private CardRarity(String name, boolean shiny, BaseRarity baseRarity, int weight, boolean goesInHoloSlot){
        this.name = name;
        this.shiny = shiny;
        this.baseRarity = baseRarity;
        this.weight = weight;
        this.goesInHoloSlot = goesInHoloSlot;
    }

    @Nullable
    public static CardRarity get(String name){
        CardRarity rarity = EXISTING_RARITIES.get(name);
        if(rarity == null){
            CobbleCards.LOGGER.error("Tried to get nonexistant rarity: " + name);
        }
        return rarity;
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
        private boolean goesInHoloSlot = false;
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

        public CardRarityBuilder goesInHoloSlot(){
            this.goesInHoloSlot = true;
            return this;
        }

        public CardRarity build(){
            CardRarity rarity = new CardRarity(name, shiny, baseRarity, baseRarity.weight + additionalWeight, goesInHoloSlot);
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
        MORE_RARE("More Rare", 3), // mostly EXs and whatnot, rare but not extremely rare
        EXTRA_RARE("Extra Rare", 8),
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

    // these are *VERY* approximate numbers, but hopefully enough to get close to useable - might end up adjusting some

    public static final CardRarity PROMO = builder("Promo").fromBase(BaseRarity.PROMO).build();
    public static final CardRarity COMMON = builder("Common").build();
    public static final CardRarity UNCOMMON = builder("Uncommon").fromBase(BaseRarity.UNCOMMON).build();
    public static final CardRarity RARE = builder("Rare").fromBase(BaseRarity.RARE).build();
    public static final CardRarity RARE_HOLO = builder("Rare Holo").fromBase(BaseRarity.RARE).additionalWeight(1).build();
    public static final CardRarity AMAZING_RARE = builder("Amazing Rare").fromBase(BaseRarity.RARE).additionalWeight(2).goesInHoloSlot().build();
    public static final CardRarity CLASSIC_COLLECTION = builder("Classic Collection").fromBase(BaseRarity.RARE).build();
    public static final CardRarity DOUBLE_RARE = builder("Double Rare").fromBase(BaseRarity.MORE_RARE).build();
    public static final CardRarity ULTRA_RARE = builder("Ultra Rare").fromBase(BaseRarity.MORE_RARE).additionalWeight(4).build();
    public static final CardRarity HYPER_RARE = builder("Hyper Rare").fromBase(BaseRarity.EXTRA_RARE).build();
    public static final CardRarity ILLUSTRATION_RARE = builder("Illustration Rare").fromBase(BaseRarity.RARE).additionalWeight(2).goesInHoloSlot().build();
    public static final CardRarity SPECIAL_ILLUSTRATION_RARE = builder("Special Illustration Rare").fromBase(BaseRarity.MORE_RARE).additionalWeight(3).goesInHoloSlot().build();
    public static final CardRarity LEGEND = builder("LEGEND").fromBase(BaseRarity.MORE_RARE).additionalWeight(2).build();
    public static final CardRarity RADIANT_RARE = builder("Radiant Rare").fromBase(BaseRarity.MORE_RARE).shiny().additionalWeight(2).goesInHoloSlot().build();
    public static final CardRarity RARE_ACE = builder("Rare ACE").fromBase(BaseRarity.MORE_RARE).additionalWeight(3).goesInHoloSlot().build();
    public static final CardRarity RARE_BREAK = builder("Rare BREAK").fromBase(BaseRarity.MORE_RARE).goesInHoloSlot().build();
    public static final CardRarity RARE_HOLO_EX = builder("Rare Holo EX").fromBase(BaseRarity.MORE_RARE).build();
    public static final CardRarity RARE_HOLO_GX = builder("Rare Holo GX").fromBase(BaseRarity.MORE_RARE).build();
    public static final CardRarity RARE_HOLO_LVX = builder("Rare Holo LV.X").fromBase(BaseRarity.MORE_RARE).build();
    public static final CardRarity RARE_HOLO_STAR = builder("Rare Holo Star").fromBase(BaseRarity.MORE_RARE).build();
    public static final CardRarity RARE_HOLO_V = builder("Rare Holo V").fromBase(BaseRarity.MORE_RARE).build();
    public static final CardRarity RARE_HOLO_VMAX = builder("Rare Holo VMAX").fromBase(BaseRarity.EXTRA_RARE).build();
    public static final CardRarity RARE_HOLO_VSTAR = builder("Rare Holo VSTAR").fromBase(BaseRarity.MORE_RARE).additionalWeight(4).build();
    public static final CardRarity RARE_PRIME = builder("Rare Prime").fromBase(BaseRarity.MORE_RARE).goesInHoloSlot().build();
    public static final CardRarity RARE_PRISM_STAR = builder("Rare Prism Star").fromBase(BaseRarity.MORE_RARE).goesInHoloSlot().build();
    public static final CardRarity RARE_RAINBOW = builder("Rare Rainbow").fromBase(BaseRarity.EXTRA_RARE).additionalWeight(6).build();
    public static final CardRarity RARE_SECRET = builder("Rare Secret").fromBase(BaseRarity.EXTRA_RARE).additionalWeight(8).build();
    public static final CardRarity RARE_SHINING = builder("Rare Shining").fromBase(BaseRarity.MORE_RARE).shiny().build();
    public static final CardRarity RARE_SHINY = builder("Rare Shiny").fromBase(BaseRarity.MORE_RARE).shiny().goesInHoloSlot().build();
    public static final CardRarity RARE_SHINY_GX = builder("Rare Shiny GX").fromBase(BaseRarity.MORE_RARE).additionalWeight(3).shiny().goesInHoloSlot().build();
    public static final CardRarity RARE_ULTRA = builder("Rare Ultra").fromBase(BaseRarity.MORE_RARE).additionalWeight(3).build();
    public static final CardRarity TRAINER_GALLERY = builder("Trainer Gallery Rare Holo").fromBase(BaseRarity.MORE_RARE).additionalWeight(3).build();
    public static final CardRarity SHINY_RARE = builder("Shiny Rare").fromBase(BaseRarity.RARE).shiny().goesInHoloSlot().build();
    public static final CardRarity SHINY_ULTRA_RARE = builder("Shiny Ultra Rare").fromBase(BaseRarity.EXTRA_RARE).shiny().build();


    /**
     * https://discord.com/channels/222559292497068043/222559292497068043/1061249148415905862 <- someone made a tier system for rarities, could be a good base
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

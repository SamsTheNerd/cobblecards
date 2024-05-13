package com.samsthenerd.cobblecards.inlinecards;

import com.samsthenerd.cobblecards.CobbleCards;
import com.samsthenerd.cobblecards.inline.Inline;
import com.samsthenerd.cobblecards.inline.InlineMatchResult.DataMatch;
import com.samsthenerd.cobblecards.inline.data.SpriteInlineData;
import com.samsthenerd.cobblecards.inline.matchers.RegexMatcher;
import com.samsthenerd.cobblecards.pokedata.Card;
import com.samsthenerd.cobblecards.pokedata.CardSet;
import com.samsthenerd.cobblecards.pokedata.packs.SetPack;
import com.samsthenerd.cobblecards.registry.CobbleCardsItems;

import net.minecraft.item.ItemStack;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.HoverEvent.ItemStackContent;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;

public class InlineCards {
    public static void init(){
        Inline.addMatcher(new Identifier(CobbleCards.MOD_ID, "card"), new RegexMatcher.Simple("<card:([a-zA-Z0-9]+)-([a-zA-Z0-9]+)>",
            mr -> {
                String fullId = mr.group(1) + "-" + mr.group(2);
                Card card = Card.get(fullId);
                if(card == null){
                    return null;
                }
                HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ItemStackContent(CobbleCardsItems.POKEMON_CARD_ITEM.get().fromCard(card, false)));
                return new DataMatch(new SpriteInlineData(card.getTexture(false)), Style.EMPTY.withHoverEvent(he));
            }));

        Inline.addMatcher(new Identifier(CobbleCards.MOD_ID, "set"), new RegexMatcher.Simple("<set:([a-zA-Z0-9]+)>",
            mr -> {
                String setId = mr.group(1);
                CardSet set = CardSet.get(setId);
                ItemStack packStack = CobbleCardsItems.CARD_PACK_ITEM.get().fromPack(new SetPack(set));
                HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ItemStackContent(packStack));
                return new DataMatch(new SpriteInlineData(set.getSymbol()), Style.EMPTY.withHoverEvent(he));
            }));
    }
}

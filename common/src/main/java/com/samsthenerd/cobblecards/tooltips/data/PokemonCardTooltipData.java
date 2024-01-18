package com.samsthenerd.cobblecards.tooltips.data;

import com.samsthenerd.cobblecards.pokedata.Card;

import net.minecraft.client.item.TooltipData;

public record PokemonCardTooltipData(Card card) implements TooltipData {
    
}


package com.samsthenerd.cobblecards;
import com.samsthenerd.cobblecards.items.ItemPokemonCard;
import com.samsthenerd.cobblecards.registry.CobbleCardsItems;

import dev.architectury.registry.item.ItemPropertiesRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment (EnvType.CLIENT)
public class CobbleCardsClientInit{
    public static void initClient() {
        addModelPredicates();
    }

    public static void addModelPredicates(){
        ItemPropertiesRegistry.register(CobbleCardsItems.POKEMON_CARD_ITEM.get(), ItemPokemonCard.NEW_CARD_PREDICATE,
        (stack, world, entity, seed) -> CobbleCardsItems.POKEMON_CARD_ITEM.get().isNewCard(stack) ? 1.0F : 0.0F);
    }
}
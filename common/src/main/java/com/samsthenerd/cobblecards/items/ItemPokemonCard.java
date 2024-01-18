package com.samsthenerd.cobblecards.items;

import java.util.Optional;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.samsthenerd.cobblecards.pokedata.Card;
import com.samsthenerd.cobblecards.pokedata.CardHolder;
import com.samsthenerd.cobblecards.tooltips.data.PokemonCardTooltipData;

import net.minecraft.client.item.TooltipData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;

public class ItemPokemonCard extends Item {
    public ItemPokemonCard(Settings settings) {
        super(settings);
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        Card card = getCard(stack);
        if(card != null){
            return Optional.of(new PokemonCardTooltipData(card));
        }
        return Optional.empty();
    }

    public ItemStack fromCard(@NotNull Card card){
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateNbt().putString("card", card.fullId());
        return stack;
    }

    @Nullable
    public Card getCard(ItemStack stack){
        if(stack.hasNbt() && stack.getNbt().contains("card", NbtElement.STRING_TYPE)){
            return CardHolder.PRIMARY.getCard(stack.getNbt().getString("card"));
        }
        return null;
    }
}

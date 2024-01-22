package com.samsthenerd.cobblecards.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.samsthenerd.cobblecards.pokedata.Card;
import com.samsthenerd.cobblecards.pokedata.CardSet;
import com.samsthenerd.cobblecards.tooltips.data.URLImageTooltipData;

import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ItemCardSetPack extends ItemCardPack{

    public ItemCardSetPack(Settings settings) {
        super(settings);
    }

    public ItemStack fromSet(CardSet set){
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateNbt().putString("set", set.id);
        return stack;
    }

    public CardSet getSet(ItemStack stack){
        if(stack.hasCustomName()){
            return CardSet.get(stack.getName().getString());
        }
        if(stack.hasNbt() && stack.getNbt().contains("set")){
            CardSet.get(stack.getNbt().getString("set"));
        }
        return null;
    }

    public Set<Card> possibleCards(ItemStack stack){
        CardSet set = getSet(stack);
        if(set != null){
            return set.getCards();
        }
        return null;
    }

    // TODO: make this actually random
    public List<Card> getRandomizedCards(ItemStack stack, ServerWorld world){
        List<Card> cards = new ArrayList<>();
        CardSet set = getSet(stack);
        if(set != null){
            for(Card card : set.getCards()){
                if(cards.size() >= getPackSize(stack)) break;
                cards.add(card);
            }
        }
        return cards;
    }

    public int getPackSize(ItemStack stack){
        return 10;
    }

    // allows it to wait for other stuff before it opens
    public boolean readyToOpen(ItemStack stack){
        return getSet(stack) != null;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        CardSet set = getSet(stack);
        return super.use(world, user, hand);
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        CardSet set = getSet(stack);
        if(set != null){
            return Optional.of(new URLImageTooltipData(
                new Identifier("cobblecards", "pokeset/" + set.id),
                set.getLogoUrl(),
                (w, h) -> { return 96; }
            ));
        }
        return Optional.empty();
    }
}

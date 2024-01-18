package com.samsthenerd.cobblecards.items;

import java.util.List;
import java.util.Set;

import com.samsthenerd.cobblecards.pokedata.Card;
import com.samsthenerd.cobblecards.pokedata.CardSet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
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
        return null;
    }

    public List<Card> getRandomizedCards(ItemStack stack, ServerWorld world){
        return null;
    }

    public int getPackSize(ItemStack stack){
        return 10;
    }

    // allows it to wait for other stuff before it opens
    public boolean readyToOpen(ItemStack stack){
        return true;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        CardSet set = getSet(stack);
        return super.use(world, user, hand);
    }
}

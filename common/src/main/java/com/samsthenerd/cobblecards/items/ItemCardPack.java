package com.samsthenerd.cobblecards.items;

import java.util.List;
import java.util.Set;

import com.samsthenerd.cobblecards.pokedata.Card;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public abstract class ItemCardPack extends Item{
    public ItemCardPack(Settings settings) {
        super(settings);
    }

    public abstract Set<Card> possibleCards(ItemStack stack);

    public abstract List<Card> getRandomizedCards(ItemStack stack, ServerWorld world);

    public abstract int getPackSize(ItemStack stack);

    // allows it to wait for other stuff before it opens
    public abstract boolean readyToOpen(ItemStack stack);

    public void openPack(ItemStack stack, ServerWorld world){

    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if(world.isClient){
            return TypedActionResult.success(stack, true);
        }
        if(readyToOpen(stack)){
            openPack(stack, (ServerWorld)world);
            stack.decrement(1);
            return TypedActionResult.success(stack, true);
        }
        return TypedActionResult.pass(stack);
    }
}

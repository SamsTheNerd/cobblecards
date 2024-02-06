package com.samsthenerd.cobblecards.items;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.samsthenerd.cobblecards.clientmisc.DynamicModelOverride;
import com.samsthenerd.cobblecards.pokedata.Card;
import com.samsthenerd.cobblecards.pokedata.CardPack;
import com.samsthenerd.cobblecards.pokedata.CardSet;
import com.samsthenerd.cobblecards.pokedata.PullManager;
import com.samsthenerd.cobblecards.pokedata.packs.SetPack;
import com.samsthenerd.cobblecards.registry.CobbleCardsItems;
import com.samsthenerd.cobblecards.utils.WHTexture;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ItemCardPack extends Item implements IDynamicModelItem, IDetailTexture{
    public ItemCardPack(Settings settings) {
        super(settings);
    }

    @Nullable
    public CardPack getPack(ItemStack stack){
        if(stack.hasCustomName()) {
            CardSet nameSet = CardSet.get(stack.getName().getString());
            if(nameSet != null){
                CardPack pack = new SetPack(nameSet);
                stack.getOrCreateNbt().put("pack", pack.toNbt());
                stack.removeCustomName();
                return pack;
            }
        }
        if(stack.hasNbt() && stack.getNbt().contains("pack")){
            return CardPack.fromNbt(stack.getNbt().getCompound("pack"));
        }
        return null;
    }

    public ItemStack fromPack(CardPack pack){
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateNbt().put("pack", pack.toNbt());
        return stack;
    }

    // TODO: make player nullable
    public void openPack(ItemStack stack, ServerPlayerEntity player, ServerWorld world){
        List<Card> cards = getPack(stack).getRandomizedCards(new PullManager(player));
        for(Card card : cards){
            player.getInventory().offerOrDrop(CobbleCardsItems.POKEMON_CARD_ITEM.get().fromCard(card, true));
        }
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if(!(user instanceof ServerPlayerEntity sPlayer)){
            return TypedActionResult.success(stack, true);
        }
        if(getPack(stack) != null){
            openPack(stack, sPlayer, (ServerWorld)world);
            stack.decrement(1);
            return TypedActionResult.success(stack, true);
        }
        return TypedActionResult.pass(stack);
    }

    @Override
    public DynamicModelOverride getModelIdentifier(ItemStack stack) {
        CardPack pack = getPack(stack);
        if(pack == null) return null;
        return pack.getModelOverride(stack);
    }

    @Override 
    public DetailTexture getDetailTexture(ItemStack stack){
        CardPack pack = getPack(stack);
        if(pack != null && pack.getSymbol() != null && Screen.hasShiftDown()){
            WHTexture symbolTexture = pack.getSymbol();
            return new DetailTexture(symbolTexture).withWidth(0.5f).fromLeft(0f).fromBottom(0f);
        }
        return null;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        CardPack pack = getPack(stack);
        if(pack != null){
            pack.appendTooltip(stack, world, tooltip, context);
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        CardPack pack = getPack(stack);
        if(pack != null){
            return pack.getTooltipData(stack);
        }
        return Optional.empty();
    }

    @Override
    public Text getName(ItemStack stack) {
        CardPack pack = getPack(stack);
        if(pack != null){
            return pack.getPackName().copy().append(" Booster Pack");
        }
        return super.getName(stack);
    }
}

package com.samsthenerd.cobblecards.items;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.samsthenerd.cobblecards.CobbleCards;
import com.samsthenerd.cobblecards.pokedata.Card;
import com.samsthenerd.cobblecards.pokedata.CardHolder;
import com.samsthenerd.cobblecards.tooltips.data.WHTextureTooltipData;
import com.samsthenerd.cobblecards.utils.CardNewnessTracker;
import com.samsthenerd.cobblecards.utils.WHTexture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ItemPokemonCard extends Item implements IDetailTexture{
    public static Identifier NEW_CARD_PREDICATE = new Identifier(CobbleCards.MOD_ID, "new_card");

    public ItemPokemonCard(Settings settings) {
        super(settings);
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        Card card = getCard(stack);
        if(card != null){
            return Optional.of(
                new WHTextureTooltipData(card.getTexture(), (w, h) -> 96)
            );
        }
        return Optional.empty();
    }

    public ItemStack fromCard(@NotNull Card card, boolean newCard){
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateNbt().putString("card", card.fullId());
        if(newCard){
            UUID newCardUUID = UUID.randomUUID();
            stack.getOrCreateNbt().putUuid("newCard", newCardUUID);
        }
        return stack;
    }

    @Environment(EnvType.CLIENT)
    public DetailTexture getDetailTexture(ItemStack stack){
        Card card = getCard(stack);
        if(card != null && Screen.hasShiftDown() && !isNewCard(stack)){
            WHTexture texture = card.getTexture(false);
            return new DetailTexture(texture).withWidth(0.5f).fromLeft(0f).fromBottom(0f);
        }
        return null; 
    }

    @Nullable
    public UUID getCardUUID(ItemStack stack){
        if(stack.isOf(this) && stack.hasNbt() && stack.getNbt().containsUuid("newCard")){
            return stack.getNbt().getUuid("newCard");
        }
        return null;
    }

    @Nullable
    public Card getCard(ItemStack stack){
        if(stack.hasNbt() && stack.getNbt().contains("card", NbtElement.STRING_TYPE)){
            return CardHolder.PRIMARY.getCard(stack.getNbt().getString("card"));
        }
        return null;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        Card card = getCard(stack);
        UUID cardUuid = getCardUUID(stack);
        if(cardUuid != null){
            CardNewnessTracker.viewCard(cardUuid);
        }
        if(card != null){
            // idk whatever we might want here    
        }
    }

    // for the client
    public boolean isNewCard(ItemStack stack){
        UUID cardUuid = getCardUUID(stack);
        if(cardUuid != null){
            return !CardNewnessTracker.isCardViewed(cardUuid);
        }
        return false;
    }

    // for the server -- *mostly*. probably also called validly clientside for some creative mode gate points.
    public ItemStack gatekeepCard(ItemStack stack){
        UUID cardUuid = getCardUUID(stack);
        if(cardUuid != null && CardNewnessTracker.isCardViewed(cardUuid)){
            Card card = getCard(stack);
            String cardName = card != null ? card.name : "unknown";
            CobbleCards.logPrint("Gatekeeping card(" + cardName + ") with uuid " + cardUuid );
            stack.getOrCreateNbt().remove("newCard");
        }
        return stack;
    }

    // in cursor ?
    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        gatekeepCard(stack);
        gatekeepCard(slot.getStack()); // sure why not
        return super.onStackClicked(stack, slot, clickType, player);
    }

    // card is in slot
    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        gatekeepCard(stack);
        gatekeepCard(otherStack);
        cursorStackReference.set(gatekeepCard(cursorStackReference.get()));
        return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        gatekeepCard(stack);
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public Text getName(ItemStack stack) {
        Card card = getCard(stack);
        if(card != null){
            return Text.of(card.name);
        }
        return super.getName(stack);
    }
}


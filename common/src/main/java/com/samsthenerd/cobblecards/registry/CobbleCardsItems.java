package com.samsthenerd.cobblecards.registry;

import java.util.function.Supplier;

import com.samsthenerd.cobblecards.CobbleCards;
import com.samsthenerd.cobblecards.items.ItemCardPack;
import com.samsthenerd.cobblecards.items.ItemPokemonCard;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CobbleCardsItems {
    public static DeferredRegister<Item> items = DeferredRegister.create(CobbleCards.MOD_ID, RegistryKeys.ITEM);
    public static final DeferredRegister<ItemGroup> TABS = DeferredRegister.create(CobbleCards.MOD_ID, RegistryKeys.ITEM_GROUP);

    public static final RegistrySupplier<ItemPokemonCard> POKEMON_CARD_ITEM = item("pokemon_card", () -> new ItemPokemonCard(defItemSettings()));

    public static final RegistrySupplier<ItemCardPack> CARD_PACK_ITEM = item("card_pack", () -> new ItemCardPack(defItemSettings()));

    public static final RegistrySupplier<ItemGroup> COBBLE_CARDS_GROUP = TABS.register("cobblecards_tab", () ->
            CreativeTabRegistry.create(Text.translatable("itemgroup.cobblecards.general"),
                    () -> new ItemStack(POKEMON_CARD_ITEM.get())));

    public static <T extends Item> RegistrySupplier<T> item(String name, Supplier<T> item) {
		return items.register(new Identifier(CobbleCards.MOD_ID, name), item);
	}

    public static Item.Settings defItemSettings(){
		return new Item.Settings().arch$tab(COBBLE_CARDS_GROUP);
	}

    public static void register(){
        items.register();
        TABS.register();
    }
}

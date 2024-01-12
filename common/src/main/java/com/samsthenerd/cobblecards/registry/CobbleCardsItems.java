package com.samsthenerd.cobblecards.registry;

import java.util.function.Supplier;

import com.samsthenerd.cobblecards.CobbleCards;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CobbleCardsItems {
    public static DeferredRegister<Item> items = DeferredRegister.create(CobbleCards.MOD_ID, RegistryKeys.ITEM);
    public static final DeferredRegister<ItemGroup> TABS = DeferredRegister.create(CobbleCards.MOD_ID, RegistryKeys.ITEM_GROUP);

    public static final RegistrySupplier<ItemGroup> COBBLE_CARDS_GROUP = TABS.register("cobblecards_tab", () ->
            CreativeTabRegistry.create(Text.translatable("itemgroup.cobblecards.general"),
                    () -> new ItemStack(Items.STICK)));

    
    public static <T extends Item> RegistrySupplier<T> item(String name, Supplier<T> item) {
		return items.register(new Identifier(CobbleCards.MOD_ID, name), item);
	}

    public static Item.Settings defItemSettings(){
		return new Item.Settings().arch$tab(COBBLE_CARDS_GROUP.get());
	}

    public static void register(){
        items.register();
        TABS.register();
    }
}

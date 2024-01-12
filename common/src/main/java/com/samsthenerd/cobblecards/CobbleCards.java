package com.samsthenerd.cobblecards;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Suppliers;
import com.samsthenerd.cobblecards.registry.CobbleCardsItems;

import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.event.GameEvent;

public class CobbleCards{
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("cobblecards");

	public static final void logPrint(String message){
		if(Platform.isDevelopmentEnvironment())
			LOGGER.info(message);
	}

	public static final String MOD_ID = "cobblecards";
	public static final Supplier<RegistrarManager> REGISTRIES = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

	// various architectury registry wrappers - could maybe move each into their own files, we'll see.
	
	public static DeferredRegister<Block> blocks = DeferredRegister.create(MOD_ID, RegistryKeys.BLOCK);
	public static Map<RegistrySupplier<? extends Block>, Item.Settings> blockItems = new HashMap<>();
	public static DeferredRegister<BlockEntityType<?>> blockEntities = DeferredRegister.create(MOD_ID, RegistryKeys.BLOCK_ENTITY_TYPE);
	public static DeferredRegister<EntityType<?>> entities = DeferredRegister.create(MOD_ID, RegistryKeys.ENTITY_TYPE);
	public static final DeferredRegister<SoundEvent> sounds = DeferredRegister.create(MOD_ID, RegistryKeys.SOUND_EVENT);
	public static final DeferredRegister<GameEvent> gameEvents = DeferredRegister.create(MOD_ID, RegistryKeys.GAME_EVENT);
	public static final DeferredRegister<ScreenHandlerType<?> > screenHandlers = DeferredRegister.create(MOD_ID, RegistryKeys.SCREEN_HANDLER);
	

	

	public static void onInitialize() {
		blocks.register();
		blockItems.forEach((block, itemprops) -> {
			CobbleCardsItems.items.register(block.getId(), () -> new BlockItem(block.get(), itemprops));
		});
		CobbleCardsItems.register();

		blockEntities.register();
		entities.register();
		sounds.register();
		gameEvents.register();
		screenHandlers.register();
	}

	// stealing from hex casting :D
	public static <T extends Block> RegistrySupplier<T> blockNoItem(String name, Supplier<T> block) {
        return blocks.register(new Identifier(MOD_ID, name), block);
    }

    public static <T extends Block> RegistrySupplier<T> blockItem(String name, Supplier<T> block) {
        return blockItem(name, block, CobbleCardsItems.defItemSettings());
    }
		
    public static <T extends Block> RegistrySupplier<T> blockItem(String name, Supplier<T> block, Item.Settings props) {
		RegistrySupplier<T> blockRegistered = blockNoItem(name, block);
		blockItems.put(blockRegistered, props);
		// items.register(new Identifier(MOD_ID, name), () -> new BlockItem(block.get(), props));
        return blockRegistered;
    }

	public static RegistrySupplier<SoundEvent> soundEvent(String id){
		return sounds.register(new Identifier(MOD_ID, id), () -> SoundEvent.of(new Identifier(MOD_ID, id)));
	}

	public static RegistrySupplier<GameEvent> gameEvent(String id, int range){
		return gameEvents.register(new Identifier(MOD_ID, id), 
				() -> new GameEvent(new Identifier(MOD_ID, id).toString(), range));
	}
}

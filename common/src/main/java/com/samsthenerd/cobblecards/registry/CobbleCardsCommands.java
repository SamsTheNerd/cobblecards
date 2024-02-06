package com.samsthenerd.cobblecards.registry;

import java.io.File;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.samsthenerd.cobblecards.pokedata.Card;
import com.samsthenerd.cobblecards.pokedata.CardPack;
import com.samsthenerd.cobblecards.pokedata.datagenish.DataFetcher;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class CobbleCardsCommands {
    public static void init(){
        CommandRegistrationEvent.EVENT.register(
            (dispatcher, registry, selection) -> {
                dispatcher.register(
                    CommandManager.literal("fetchcarddata")
                    .executes(context -> {
                        return fetchCardDataCommand(context, null);
                    }).then(CommandManager.argument("key", StringArgumentType.string())
                    .executes(context -> {
                        return fetchCardDataCommand(context, StringArgumentType.getString(context, "key"));
                    })
                ));
            }
        );

        CommandRegistrationEvent.EVENT.register(
            (dispatcher, registry, selection) -> {
                dispatcher.register(
                    CommandManager.literal("givecard")
                    .requires(source -> source.hasPermissionLevel(2))
                    .then(CommandManager.argument("players", EntityArgumentType.player())
                    .then(CommandManager.argument("card", StringArgumentType.word())
                    .executes(context -> {
                        String cardId = StringArgumentType.getString(context, "card");
                        Card card = Card.get(cardId);
                        if(card == null){
                            context.getSource().sendError(Text.literal("Invalid card: ").append(cardId));
                            return 0;
                        }
                        EntityArgumentType.getPlayers(context, "players").forEach(player -> {
                            giveCard(player, card);
                        });
                        return 1;
                    })
                    )
                ));
            }
        );

        CommandRegistrationEvent.EVENT.register(
            (dispatcher, registry, selection) -> {
                dispatcher.register(
                    CommandManager.literal("givepack")
                    .requires(source -> source.hasPermissionLevel(2))
                    .then(CommandManager.argument("players", EntityArgumentType.player())
                    .then(CommandManager.argument("pack", StringArgumentType.word())
                    .executes(context -> {
                        String packId = StringArgumentType.getString(context, "pack");
                        CardPack pack = CardPack.get(packId);
                        if(pack == null){
                            context.getSource().sendError(Text.literal("Invalid pack: ").append(packId));
                            return 0;
                        }
                        EntityArgumentType.getPlayers(context, "players").forEach(player -> {
                            givePack(player, pack);
                        });
                        return 1;
                    })
                    )
                ));
            }
        );
        
    }

    private static int fetchCardDataCommand(CommandContext<ServerCommandSource> context, String ApiKey){
        File outputDir = DataFetcher.fetchCardData(ApiKey, context.getSource().getServer());
        Text pathText = Text.literal(outputDir.getName()).formatted(Formatting.UNDERLINE).styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, outputDir.getAbsolutePath())));
        context.getSource().sendFeedback(() -> Text.literal("Output to: ").append(pathText), false);
        return 1;
    }

    private static void giveCard(ServerPlayerEntity player, Card card){
        player.getInventory().offerOrDrop(CobbleCardsItems.POKEMON_CARD_ITEM.get().fromCard(card, true));
    }

    private static void givePack(ServerPlayerEntity player, CardPack pack){
        player.getInventory().offerOrDrop(CobbleCardsItems.CARD_PACK_ITEM.get().fromPack(pack));
    }
}

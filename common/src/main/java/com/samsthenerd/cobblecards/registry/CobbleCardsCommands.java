package com.samsthenerd.cobblecards.registry;

import java.io.File;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.samsthenerd.cobblecards.pokedata.datagenish.DataFetcher;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
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
    }

    private static int fetchCardDataCommand(CommandContext<ServerCommandSource> context, String ApiKey){
        File outputDir = DataFetcher.fetchCardData(ApiKey, context.getSource().getServer());
        Text pathText = Text.literal(outputDir.getName()).formatted(Formatting.UNDERLINE).styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, outputDir.getAbsolutePath())));
        context.getSource().sendFeedback(() -> Text.literal("Output to: ").append(pathText), false);
        return 1;
    }
}

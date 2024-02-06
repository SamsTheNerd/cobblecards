package com.samsthenerd.cobblecards.commands;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.samsthenerd.cobblecards.pokedata.Card;

// i would like to be able to give it suggestions for cards or something, we'll see i guess
public class CardArgumentType implements ArgumentType<Card>{

    public static CardArgumentType INSTANCE = new CardArgumentType();

    @Override
    public Card parse(StringReader reader) throws CommandSyntaxException {
        return Card.get(reader.readString());
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        return Suggestions.empty();
    }

    @Override
    public Collection<String> getExamples() {
        return Collections.emptyList();
    }
}

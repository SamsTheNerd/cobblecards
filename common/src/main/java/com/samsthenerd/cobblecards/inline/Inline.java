package com.samsthenerd.cobblecards.inline;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.MatchResult;

import com.samsthenerd.cobblecards.CobbleCards;
import com.samsthenerd.cobblecards.inline.InlineMatchResult.DataMatch;
import com.samsthenerd.cobblecards.inline.InlineMatchResult.TextMatch;
import com.samsthenerd.cobblecards.inline.data.EntityInlineData;
import com.samsthenerd.cobblecards.inline.data.ItemInlineData;
import com.samsthenerd.cobblecards.inline.matchers.RegexMatcher;
import com.samsthenerd.cobblecards.inline.renderers.InlineEntityRenderer;
import com.samsthenerd.cobblecards.inline.renderers.InlineItemRenderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.HoverEvent.ItemStackContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

// this will probably be bumped out into its own mod Soon, but i want to get it working in this test environment first
public class Inline {
    private static final Map<Identifier, InlineRenderer<?>> RENDERERS = new HashMap<>();
    private static final Map<Identifier, InlineMatcher> MATCHERS = new HashMap<>();

    public static final String MOD_ID = "inline";

    static {
        addMatcher(new Identifier(MOD_ID, "item"), new RegexMatcher.Simple("<item:([a-z:\\/_]+)>", (MatchResult mr) ->{
            Item item = Registries.ITEM.get(new Identifier(mr.group(1)));
            if(item == null) return null;
            ItemStack stack = new ItemStack(item);
            HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ItemStackContent(stack));
            return new DataMatch(new ItemInlineData(stack), Style.EMPTY.withHoverEvent(he));
        }));

        addMatcher(new Identifier(MOD_ID, "entity"), new RegexMatcher.Simple("<entity:([a-z:\\/_]+)>", (MatchResult mr) ->{
            EntityType entType = Registries.ENTITY_TYPE.get(new Identifier(mr.group(1)));
            if(entType == null) return null;
            Entity ent = entType.create(MinecraftClient.getInstance().world); // TODO: don't ship this
            return new DataMatch(new EntityInlineData(ent));
        }));

        addMatcher(new Identifier(MOD_ID, "link"), new RegexMatcher.Simple("\\[(.*)\\]\\((.*)\\)", (MatchResult mr) ->{
            String text = mr.group(1);
            String link = mr.group(2);
            ClickEvent ce = new ClickEvent(ClickEvent.Action.OPEN_URL, link);
            HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.of(link));
            MutableText linkText = Text.literal(text + " 🔗");
            linkText.setStyle(Style.EMPTY.withClickEvent(ce).withHoverEvent(he).withUnderline(true).withColor(Formatting.BLUE));
            return new TextMatch(linkText);
        }));

        addRenderer(InlineItemRenderer.INSTANCE);
        addRenderer(InlineEntityRenderer.INSTANCE);
    }

    public static void addMatcher(Identifier id, InlineMatcher matcher){
        MATCHERS.put(id, matcher);
    }

    public static void addRenderer(InlineRenderer<?> renderer){
        if(RENDERERS.containsKey(renderer.getId())){
            CobbleCards.LOGGER.error("renderer with id " + renderer.getId().toString() + " already exists");
            return;
        }
        RENDERERS.put(renderer.getId(), renderer);
    }

    public static Set<InlineMatcher> getMatchers(){
        return new HashSet<>(MATCHERS.values());
    }

    public static InlineMatcher getMatcher(Identifier id){
        return MATCHERS.get(id);
    }

    public static InlineRenderer getRenderer(Identifier id){
        if(RENDERERS.get(id) == null) {
            CobbleCards.logPrint("couldn't find renderer: " + id.toString());
            CobbleCards.logPrint("available renderers: ");
            for(Identifier i : RENDERERS.keySet()){
                CobbleCards.logPrint("\t-" + i.toString());
            }
        }
        return RENDERERS.get(id);
    }
}

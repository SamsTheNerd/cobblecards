package com.samsthenerd.cobblecards.pokedata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.samsthenerd.cobblecards.CobbleCards;

import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

public class PokeDataLoader extends JsonDataLoader{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public PokeDataLoader() {
        super(GSON, "pokedata");
    }

    protected void apply(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler) {
        CobbleCards.logPrint("in pokedata apply, with " + map.size() + " elements");
        int cardCount = 0;
        int setCount = 0;
        // do sets first and then cards
        List<JsonObject> cardJsons = new ArrayList<>();
        for(Entry<Identifier, JsonElement> entry : map.entrySet()){
            JsonObject jsonObj = entry.getValue().getAsJsonObject();
            JsonElement dataElem = jsonObj.get("data");
            String dataType = jsonObj.get("datatype").getAsString();
            if(dataType.equals("sets")){
                List<JsonElement> setJsons = new ArrayList<>();
                if(dataElem.isJsonArray()){
                    setJsons = dataElem.getAsJsonArray().asList();
                } else {
                    setJsons.add(dataElem);
                }
                for(JsonElement setJsonElem : setJsons){
                    JsonObject setJson = setJsonElem.getAsJsonObject();
                    CardSet set = CardSet.fromJson(setJson);
                    CardSet.add(set);
                    setCount++;
                }
            } else if(dataType.equals("cards")){
                List<JsonElement> cardJsonsList = new ArrayList<>();
                if(dataElem.isJsonArray()){
                    cardJsonsList = dataElem.getAsJsonArray().asList();
                } else {
                    cardJsonsList.add(dataElem);
                }
                for(JsonElement cardJsonElem : cardJsonsList){
                    cardJsons.add(cardJsonElem.getAsJsonObject());
                }
            }
        }
        for(JsonObject cardJson : cardJsons){
            Card card = Card.fromJson(cardJson);
            CardHolder.PRIMARY.addCard(card);
            cardCount++;
        }
        CobbleCards.logPrint("Loaded " + cardCount + " cards and " + setCount + " sets");
    }

    // register the event(s)
    public static void init(){
        ReloadListenerRegistry.register(ResourceType.SERVER_DATA, new PokeDataLoader());
    }
}

package com.samsthenerd.cobblecards.pokedata.datagenish;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.samsthenerd.cobblecards.CobbleCards;
import com.samsthenerd.cobblecards.pokedata.CardSet;

import net.minecraft.server.MinecraftServer;

public class DataFetcher {

    public static final DateFormat EXPORT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    // for parsing
    public static final DateFormat RELEASE_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

    public static final Gson GSON_WRITER = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create(); 

    static final List<String> fieldsToCopy = List.of("name", "artist", "supertype", "flavorText", "types", "subtypes",
                    "hp", "rules", "level", "evolvesFrom", "evolvesTo", "rules", "ancientTrait", "rarity", "abilities", "attacks", 
                    "weaknesses", "resistances", "retreatCost");

    // give it your api key if you want it to go at a reasonable speed
    public static File fetchCardData(String key, MinecraftServer server){
        try{
        File exportDir = new File(server.getRunDirectory(), "cobblecard_exported_data/data_" + EXPORT_DATE_FORMAT.format(new Date()));
        File cardsExportDir = new File(exportDir, "cards");
        File setsExportDir = new File(exportDir, "sets");
        cardsExportDir.mkdirs();
        setsExportDir.mkdirs();
        
        // first get all the sets

        // TODO: check for and reuse existing data

        JsonArray sets = getAllSets(key);

        // need to fetch cards first so that we can find if they need an idFormatter, then print them all to files
        Map<String, JsonObject> setJsons = new HashMap<>();
        // new sets that we want to find cards for
        Set<String> newSetIDs = new HashSet<>();

        for(JsonElement rawSet : sets){
            JsonObject set = rawSet.getAsJsonObject();
            String setId = set.get("id").getAsString();
            CobbleCards.logPrint("Found set: " + setId);
            CardSet existingSet = CardSet.get(setId);
            JsonObject setJsonOutput = new JsonObject();
            if(existingSet == null){
                // doesn't already exist
                setJsonOutput.addProperty("id", setId);
                setJsonOutput.addProperty("name", set.get("name").getAsString());
                setJsonOutput.addProperty("series", set.get("series").getAsString());
                setJsonOutput.addProperty("total", set.get("total").getAsInt());
                setJsonOutput.addProperty("printedTotal", set.get("printedTotal").getAsInt());
                setJsonOutput.addProperty("releaseDate", set.get("releaseDate").getAsString());
                newSetIDs.add(setId);
            } else {
                setJsonOutput = existingSet.toJson();
            }
            setJsons.put(setId, setJsonOutput);
        }

        // need to fetch cards for new sets, and also re-save existing cards
        // TODO: still need to get and re-save existing cards

        int setCount = 0;
        for(String setId : newSetIDs){
            JsonArray cards = getCardsInSet(setId, key);
            List<JsonElement> rawCardJsons = cards.asList();
            JsonArray cardOutputJsons = new JsonArray();
            for(int i = 0; i < rawCardJsons.size(); i++){
                JsonObject rawCard = rawCardJsons.get(i).getAsJsonObject();
                JsonObject cardOutput = new JsonObject();

                String cardNum = rawCard.get("number").getAsString();

                cardOutput.addProperty("set", setId);
                cardOutput.addProperty("cardNum", cardNum);

                // very silly, just picking specific fields to copy over
                for(String field : fieldsToCopy){
                    if(rawCard.has(field)){
                        cardOutput.add(field, rawCard.get(field));
                    }
                }

                cardOutput.add("pokedexNumbers", rawCard.get("nationalPokedexNumbers"));

                cardOutputJsons.add(cardOutput);
            }
            File cardOutputFile = new File(cardsExportDir, setId + ".json");
            try { 
                cardOutputFile.createNewFile();
                JsonObject wrappedCardJson = new JsonObject();
                wrappedCardJson.add("data", cardOutputJsons);
                wrappedCardJson.addProperty("datatype", "cards");
                FileWriter writer = new FileWriter(cardOutputFile);
                writer.write(GSON_WRITER.toJson(wrappedCardJson));
                writer.close();
                CobbleCards.logPrint("Wrote " + setId + " (" + setCount + "/" + newSetIDs.size() + ")");
            } catch (Exception e){
                CobbleCards.LOGGER.error("Failed to create file " + cardOutputFile.getAbsolutePath() + "\n" + e);
            }
            setCount++;
        }

        for(String setId : setJsons.keySet()){
            File setOutputFile = new File(setsExportDir, setId + ".json");
            try { 
                setOutputFile.createNewFile();
                JsonObject wrappedSetJson = new JsonObject();
                wrappedSetJson.add("data", setJsons.get(setId));
                wrappedSetJson.addProperty("datatype", "sets");
                FileWriter writer = new FileWriter(setOutputFile);
                writer.write(GSON_WRITER.toJson(wrappedSetJson));
                writer.close();
            } catch (Exception e){
                CobbleCards.LOGGER.error("Failed to create file " + setOutputFile.getAbsolutePath() + "\n" + e);
            }
        }
        
        return exportDir;
        } catch (Exception e){
            CobbleCards.LOGGER.error("Failed to fetch data: \n" + e);
            throw e;
        }
    }

    // null page if you want all pages
    private static JsonArray doQuery(String baseURL, String query, String selects, String ApiKey, @Nullable Integer page){
        try{
            String params = "";
            if(query != null && !query.isEmpty()){
                params += (params.isEmpty() ? "?" : "&") + "q=" + query;
            }
            if(selects != null && !selects.isEmpty()){
                params += (params.isEmpty() ? "?" : "&") + "select=" + query;
            }
            if(page != null){
                params += (params.isEmpty() ? "?" : "&") + "page=" + page;
            }
            CobbleCards.logPrint("Fetching data from " + baseURL + params);
            URL url = new URL(baseURL + params);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(0);
            connection.setReadTimeout(0);
            if(ApiKey != null && !ApiKey.isEmpty()){
                connection.setRequestProperty("X-Api-Key", ApiKey);
            }
            BufferedReader in = new BufferedReader(
            new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();
            JsonObject result = GSON_WRITER.fromJson(content.toString(), JsonObject.class);
            JsonArray dataObject = result.getAsJsonArray("data");
            int totalCount = result.getAsJsonPrimitive("totalCount").getAsNumber().intValue();
            int pagesLeftToGet = (totalCount-1) / 250;
            if(page == null){
                for(int p = 0; p < pagesLeftToGet; p++){
                    CobbleCards.logPrint("Would get extra page " + (p+2));
                    dataObject.addAll(doQuery(baseURL, query, selects, ApiKey,  p + 2));
                }
            }
            return dataObject;
        } catch (Exception e){
            CobbleCards.LOGGER.error("Failed to fetch data for " + baseURL + "?q=" + query + "\n" + e);
        }

        return new JsonArray();
    }

    private static JsonArray getAllSets(String ApiKey){
        return doQuery("https://api.pokemontcg.io/v2/sets/", null, null, ApiKey, null);
    }

    private static JsonArray getCardsInSet(String setId, String ApiKey){
        return doQuery("https://api.pokemontcg.io/v2/cards/", "set.id:" + setId, null, ApiKey, null);
    }

    public static int extractCardNum(String cardNum){
        String rawNumString = "";
        // strip off any trailing non-digits
        while(cardNum.length() > 0 && !Character.isDigit(cardNum.charAt(cardNum.length()-1))){
            cardNum = cardNum.substring(0, cardNum.length()-1);
        }
        while(cardNum.length() > 0 && Character.isDigit(cardNum.charAt(cardNum.length()-1))){
            rawNumString = cardNum.charAt(cardNum.length()-1) + rawNumString;
            cardNum = cardNum.substring(0, cardNum.length()-1);
        }
        int num = 100000; // i guess this will encode for awful garbage !
        try {
            num = Integer.parseInt(rawNumString);
        } catch (Exception e){
            CobbleCards.LOGGER.error("Failed to parse card number " + rawNumString + " from " + cardNum);
        }
        return num;
    }
}

package com.samsthenerd.cobblecards.pokedata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.samsthenerd.cobblecards.clientmisc.DynamicModelOverride;
import com.samsthenerd.cobblecards.pokedata.packs.PokedexPack;
import com.samsthenerd.cobblecards.pokedata.packs.SetPack;
import com.samsthenerd.cobblecards.tooltips.data.WHTextureTooltipData;
import com.samsthenerd.cobblecards.utils.WHTexture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.World;

// a class representing a pack of pokemon cards - can be read in from json and rw with nbt
public abstract class CardPack {

    private static Map<String, Function<NbtCompound, CardPack>> NBT_DESERIALIZERS = new HashMap<>();
    private static Map<String, Function<JsonObject, CardPack>> JSON_DESERIALIZERS = new HashMap<>();
    static {
        registerType("set", SetPack::fromNbt, SetPack::fromJson);
        registerType("pokedex", PokedexPack::fromNbt, PokedexPack::fromJson);
    }

    // used as a part of the item name and maybe in store area ?
    public abstract Text getPackName();

    public abstract Text getSeries();

    public abstract String getType();

    public List<Card> getRandomizedCards(PullManager pullManager){
        List<Card> cards = pullManager.pullCards(getPossibleCards(), getPullSlots());
        return cards;
    }

    public abstract List<PullSlot> getPullSlots();

    public abstract Set<Card> getPossibleCards();

    @Nullable
    public abstract WHTexture getLogo();

    @Nullable
    public abstract WHTexture getSymbol();

    // expose tooltip stuff here since it's nice to have
    public Optional<TooltipData> getTooltipData(ItemStack stack){
        WHTexture logo = getLogo();
        if(logo != null){
            return Optional.of(new WHTextureTooltipData(logo, (w,h) -> 96));
        }
        return Optional.empty();
    }

    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {}

    @Environment(EnvType.CLIENT)
    public DynamicModelOverride getModelOverride(ItemStack stack){
        return null;
    }

    public NbtCompound toNbt(){
        NbtCompound nbt = new NbtCompound();
        nbt.putString("type", getType());
        return nbt;
    }

    public static void registerType(String type, Function<NbtCompound, CardPack> nbtDeserializer, Function<JsonObject, CardPack> jsonDeserializer){
        NBT_DESERIALIZERS.put(type, nbtDeserializer);
        JSON_DESERIALIZERS.put(type, jsonDeserializer);
    }

    @Nullable
    public static CardPack fromNbt(NbtCompound nbt){
        String type = nbt.getString("type");
        if(!NBT_DESERIALIZERS.containsKey(type)){
            return null;
        }
        return NBT_DESERIALIZERS.get(type).apply(nbt);
    }

    @Nullable
    public static CardPack fromJson(JsonObject json){
        String type = json.get("type").getAsString();
        if(!JSON_DESERIALIZERS.containsKey(type)){
            return null;
        }
        return JSON_DESERIALIZERS.get(type).apply(json);
    }
}

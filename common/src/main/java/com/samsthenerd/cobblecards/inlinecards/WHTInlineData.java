package com.samsthenerd.cobblecards.inlinecards;

import com.google.gson.JsonObject;
import com.samsthenerd.cobblecards.CobbleCards;
import com.samsthenerd.cobblecards.inline.InlineData;
import com.samsthenerd.cobblecards.utils.WHTexture;

import net.minecraft.util.Identifier;

public class WHTInlineData implements InlineData{
    public Identifier getDataType(){
        return new Identifier(CobbleCards.MOD_ID, "whtexture");
    }

    public Identifier getRendererId(){
        return new Identifier(CobbleCards.MOD_ID, "whtexture");
    }

    public WHTexture texture;

    public WHTInlineData(WHTexture texture){
        this.texture = texture;
    }

    public IDSerializer<WHTInlineData> getSerializer(){
        return Serializer.INSTANCE;
    }

    public static class Serializer implements InlineData.IDSerializer<WHTInlineData> {
        public static Serializer INSTANCE = new Serializer();

        public WHTInlineData deserialize(JsonObject json){
            return new WHTInlineData(WHTexture.deserialize(json));
        }

        public JsonObject serializeData(WHTInlineData data){
            return data.texture.serialize();
        }
    }
}

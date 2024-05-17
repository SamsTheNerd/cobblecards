package com.samsthenerd.cobblecards.inline.data;

import java.util.Optional;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.samsthenerd.cobblecards.inline.Inline;
import com.samsthenerd.cobblecards.inline.InlineData;
import com.samsthenerd.cobblecards.utils.Spritelike;

import net.minecraft.util.Identifier;

public class SpriteInlineData implements InlineData{
    public Identifier getDataType(){
        return new Identifier(Inline.MOD_ID, "spritelike");
    }

    public Identifier getRendererId(){
        return new Identifier(Inline.MOD_ID, "spritelike");
    }

    public Spritelike sprite;

    public SpriteInlineData(Spritelike sprite){
        this.sprite = sprite;
    }

    @Override
    public IDSerializer<? extends SpriteInlineData> getSerializer(){
        return Serializer.INSTANCE;
    }

    public static class Serializer implements InlineData.IDSerializer<SpriteInlineData> {
        public static Serializer INSTANCE = new Serializer();

        public SpriteInlineData deserialize(JsonObject json){
            return new SpriteInlineData(Spritelike.fromJson(json));
        }

        public JsonElement serializeData(SpriteInlineData data){
            Optional<JsonElement> res = Spritelike.CODEC.encodeStart(JsonOps.INSTANCE, data.sprite).result();
            if(res.isPresent()){
                return res.get();
            }
            return new JsonObject();
        }
    }
}

package com.samsthenerd.cobblecards.utils;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

// specifies some texture and a width/height. can be either from a URL or a local texture. Mostly used for in tooltips
public class WHTexture {
    private Identifier id;
    private Identifier textureId;
    private boolean isLocal;
    // for url:
    private String url;
    // for local:
    private int width;
    private int height;

    public static WHTexture fromUrl(String url, Identifier id){
        WHTexture texture = new WHTexture();
        texture.url = url;
        texture.id = id;
        texture.isLocal = false;
        return texture;
    }

    public static WHTexture fromLocal(Identifier textureId, int width, int height){
        WHTexture texture = new WHTexture();
        texture.textureId = textureId;
        texture.id = textureId;
        texture.isLocal = true;
        texture.width = width;
        texture.height = height;
        return texture;
    }

    @Nullable
    public static WHTexture fromJson(JsonElement json){
        if(!json.isJsonObject()){
            return null;
        }
        JsonObject obj = json.getAsJsonObject();
        if(obj.has("texture")){
            Identifier textureId = new Identifier(obj.get("texture").getAsString());
            int width = obj.get("width").getAsInt();
            int height = obj.get("height").getAsInt();
            return fromLocal(textureId, width, height);
        } else if(obj.has("url")){
            Identifier id = new Identifier(obj.get("id").getAsString());
            return fromUrl(obj.get("url").getAsString(), id);
        }
        return null;
    }

    public Identifier getTextureId(){
        if(isLocal){
            return textureId;
        } else {
            return URLTextureUtils.loadTextureFromURL(url, id);
        }
    }

    public int getWidth(){
        if(isLocal){
            return width;
        } else {
            getTextureId(); // force it to load real quick
            Pair<Integer, Integer> dims = URLTextureUtils.getTextureDimensions(id);
            if(dims == null){
                return 0;
            }
            return dims.getLeft();
        }
    }

    public int getHeight(){
        if(isLocal){
            return height;
        } else {
            getTextureId(); // force it to load real quick
            Pair<Integer, Integer> dims = URLTextureUtils.getTextureDimensions(id);
            if(dims == null){
                return 0;
            }
            return dims.getRight();
        }
    }
}
package com.samsthenerd.cobblecards.inline.data;

import com.google.gson.JsonObject;
import com.samsthenerd.cobblecards.inline.Inline;
import com.samsthenerd.cobblecards.inline.InlineData;
import com.samsthenerd.cobblecards.inline.InlineStyle;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.registry.Registries;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.HoverEvent.ItemStackContent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemInlineData implements InlineData{

    private ItemStack stack;

    public Identifier getDataType(){
        return new Identifier(Inline.MOD_ID, "item");
    }

    public Identifier getRendererId(){
        return new Identifier(Inline.MOD_ID, "item");
    }

    public IDSerializer<ItemInlineData> getSerializer(){
        return Serializer.INSTANCE;
    }

    public ItemStack getStack(){
        return stack;
    }

    public ItemInlineData(ItemStack stack){
        this.stack = stack;
    }

    // gives a character that's styled to appear as the item, with the same hover event
    public static Text make(ItemStack stack){
        HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ItemStackContent(stack));
        Style style = ((InlineStyle)Style.EMPTY.withHoverEvent(hover)).withInlineData(new ItemInlineData(stack));
        return Text.literal("#").setStyle(style);
    }

    public static class Serializer implements InlineData.IDSerializer<ItemInlineData> {

        public static Serializer INSTANCE = new Serializer();

        public ItemInlineData deserialize(JsonObject json){
            ItemStack stack = new ItemStack(Items.AIR);
            if(json.has("item")){
                Item item = Registries.ITEM.get(new Identifier(json.get("item").getAsString()));
                stack = new ItemStack(item);
                if(json.has("nbt")){
                    String stringyNbt = json.get("nbt").getAsString();
                    NbtCompound tag = new NbtCompound();
                    try{
                        tag = StringNbtReader.parse(stringyNbt);
                    } catch(Exception e){}
                    stack.setNbt(tag);
                }
            }
            return new ItemInlineData(stack);
        }

        public JsonObject serializeData(ItemInlineData data){
            JsonObject json = new JsonObject();
            json.addProperty("item", Registries.ITEM.getId(data.stack.getItem()).toTranslationKey());
            json.addProperty("count", data.stack.getCount());
            NbtCompound tag = data.stack.getNbt();
            if(tag != null){
                json.addProperty("nbt", tag.asString());
            }
            return json;
        }
    }
}

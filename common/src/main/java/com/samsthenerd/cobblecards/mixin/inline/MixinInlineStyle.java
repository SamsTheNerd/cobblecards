package com.samsthenerd.cobblecards.mixin.inline;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

@Mixin(Style.class)
public class MixinInlineStyle {// implements InlineStyle{

    // @Override
    // public Style setPattern(HexPattern pattern) {
    //     // yoinked from PatternTooltipComponent
    //     this.pattern = pattern;
    //     Pair<Float, List<Vec2f> > pair = RenderLib.getCenteredPattern(pattern, RENDER_SIZE, RENDER_SIZE, 16f);
    //     this.patScale = pair.getFirst();
    //     List<Vec2f> dots = pair.getSecond();
    //     this.zappyPoints = RenderLib.makeZappy(
    //         dots, RenderLib.findDupIndices(pattern.positions()),
    //         10, 0.8f, 0f, 0f, RenderLib.DEFAULT_READABILITY_OFFSET, RenderLib.DEFAULT_LAST_SEGMENT_LEN_PROP,
    //         0.0);
    //     this.pathfinderDots = dots;
    //     return (Style)(Object)this;
    // }

    // @Override
    // public Style withPattern(HexPattern pattern, boolean withPatternHoverEvent, boolean withPatternClickEvent) {
    //     Style style = (Style)(Object)this;

    //     if (withPatternHoverEvent) {
    //         StringBuilder bob = new StringBuilder();
    //         bob.append(pattern.getStartDir());
    //         var sig = pattern.anglesSignature();
    //         if (!sig.isEmpty()) {
    //             bob.append(" ");
    //             bob.append(sig);
    //         }
    //         Text hoverText = Text.translatable("hexcasting.tooltip.pattern_iota",
    //             Text.literal(bob.toString())).setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.GOLD));
    //         ItemStack scrollStack = new ItemStack(HexItems.SCROLL_LARGE);
    //         scrollStack.setCustomName(hoverText);
    //         HexItems.SCROLL_LARGE.writeDatum(scrollStack, new PatternIota(pattern));
    //         style = style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackContent(scrollStack)));
    //     }
    //     if(withPatternClickEvent){
    //         style = style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "<" + 
    //             pattern.getStartDir().toString().replace("_", "").toLowerCase() + "," + pattern.anglesSignature() + ">"));
    //     }
    //     return style.withParent(PatternStyle.fromPattern(pattern));
    // }

    
    // @Override
    // public Style setHidden(boolean hidden){
    //     this._isHidden = hidden;
    //     return (Style)(Object)this;
    // }

    // @Override
    // public Style withHidden(boolean hidden){
    //     return ((Style)(Object)this).withParent(((PatternStyle)Style.EMPTY.withBold(null)).setHidden(hidden));
    // }

    // @Inject(at=@At("TAIL"), method="<init>(Lnet/minecraft/text/TextColor;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Boolean;Lnet/minecraft/text/ClickEvent;Lnet/minecraft/text/HoverEvent;Ljava/lang/String;Lnet/minecraft/util/Identifier;)V")
    // private void HexPatDefaultStyleConstructor(@Nullable TextColor color, @Nullable Boolean bold, @Nullable Boolean italic, @Nullable Boolean underlined, @Nullable Boolean strikethrough, @Nullable Boolean obfuscated, @Nullable ClickEvent clickEvent, @Nullable HoverEvent hoverEvent, @Nullable String insertion, @Nullable Identifier font, CallbackInfo cinfo){
    //     this.pattern = null;
    //     this.zappyPoints = null;
    //     this.pathfinderDots = null;
    //     this._isHidden = false;
    //     this.patScale = 1f;
    // }

    // @Inject(method = "withParent", at = @At("RETURN"), cancellable = true)
	// private void HexPatStyWithParent(Style parent, CallbackInfoReturnable<Style> cir) {
    //     Style rstyle = cir.getReturnValue();
    //     if(this.getPattern() != null){
    //         ((PatternStyle) rstyle).setPattern(this.getPattern());
    //     } else { // no pattern on this style, try falling back to inherit parent
    //         HexPattern parentPattern = ((PatternStyle) parent).getPattern();
    //         if(parentPattern != null){
    //             ((PatternStyle) rstyle).setPattern(parentPattern);
    //         }
    //     }
    //     // i guess?
    //     if(this.isHidden() || ((PatternStyle) parent).isHidden()){
    //         ((PatternStyle) rstyle).setHidden(true);
    //     }
	// 	cir.setReturnValue(rstyle);
	// }

	// @Inject(method = "equals", at = @At("HEAD"), cancellable = true)
	// private void HexPatStyEquals(Object obj, CallbackInfoReturnable<Boolean> cir) {
	// 	if (this != obj && (obj instanceof PatternStyle style)) {
	// 		if (!Objects.equals(this.getPattern(), style.getPattern())) {
	// 			cir.setReturnValue(false);
	// 		}
    //         if(this.isHidden() != style.isHidden()){
    //             cir.setReturnValue(false);
    //         }
	// 	}
	// }

	// @Mixin(Style.Serializer.class)
	// public static class MixinPatternStyleSerializer {
	// 	@ModifyReturnValue(method = "deserialize", at = @At("RETURN"))
	// 	private Style HexPatStyDeserialize(Style initialStyle, JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
	// 		if (!jsonElement.isJsonObject() || initialStyle == null) {
	// 			return initialStyle;
	// 		}
	// 		JsonObject json = jsonElement.getAsJsonObject();
	// 		if (!json.has("hexPatternStyle")) {
	// 			return initialStyle;
	// 		}
    //         Boolean hiddenFromJson = JsonHelper.hasBoolean(json, PATTERN_HIDDEN_KEY) ? JsonHelper.getBoolean(json, PATTERN_HIDDEN_KEY) : false;
            
    //         JsonObject patternObj = JsonHelper.getObject(json, PATTERN_KEY);
            
    //         String startDirString = JsonHelper.hasString(patternObj, PATTERN_START_DIR_KEY) ? JsonHelper.getString(patternObj, PATTERN_START_DIR_KEY) : null;
    //         String angleSigString = JsonHelper.hasString(patternObj, PATTERN_ANGLE_SIG_KEY) ? JsonHelper.getString(patternObj, PATTERN_ANGLE_SIG_KEY) : null;

    //         if(startDirString == null || angleSigString == null) return initialStyle;

    //         HexDir startDir = HexDir.fromString(startDirString);
    //         HexPattern pattern = HexPattern.fromAngles(angleSigString, startDir);
    //         return initialStyle.withPattern(pattern).setHidden(hiddenFromJson);
	// 	}

	// 	@ModifyReturnValue(method = "serialize", at = @At("RETURN"))
	// 	private JsonElement HexPatStySerialize(JsonElement jsonElement, Style style, Type type, JsonSerializationContext jsonSerializationContext) {
	// 		PatternStyle pStyle = (PatternStyle) style;
	// 		if (jsonElement == null || !jsonElement.isJsonObject() || pStyle.getPattern() == null) {
	// 			return jsonElement;
	// 		}
	// 		JsonObject json = jsonElement.getAsJsonObject();
    //         json.add(PATTERN_HIDDEN_KEY, new JsonPrimitive(pStyle.isHidden()));
    //         JsonObject patternObj = new JsonObject();
    //         patternObj.addProperty(PATTERN_START_DIR_KEY, pStyle.getPattern().getStartDir().toString());
    //         patternObj.addProperty(PATTERN_ANGLE_SIG_KEY, pStyle.getPattern().anglesSignature());
	// 		json.add(PATTERN_KEY, patternObj);
    //         return json;
	// 	}
	// }

    // // meant to be called at the 
    // private Style keepPattern(Style returnedStyle){
    //     PatternStyle pStyle = (PatternStyle)(Object)this;
    //     if(pStyle.getPattern() != null){
    //         ((PatternStyle) returnedStyle).setPattern(pStyle.getPattern());
    //     }
    //     if(pStyle.isHidden()){
    //         ((PatternStyle) returnedStyle).setHidden(true);
    //     }
    //     return returnedStyle;
    // }

    // TOOD: make this actually work
    private Style keepData(Style newStyle){
        return newStyle; // for now
    }

    @ModifyReturnValue(method = "withColor(Lnet/minecraft/text/TextColor;)Lnet/minecraft/text/Style;",
    at=@At("RETURN"))
    private Style fixWithColor(Style newStyle, TextColor color){
        return keepData(newStyle);
    }

    @ModifyReturnValue(method = "withBold(Ljava/lang/Boolean;)Lnet/minecraft/text/Style;",
    at=@At("RETURN"))
    private Style fixWithBold(Style newStyle, Boolean boldBool){
        return keepData(newStyle);
    }
    
    @ModifyReturnValue(method = "withItalic(Ljava/lang/Boolean;)Lnet/minecraft/text/Style;",
    at=@At("RETURN"))
	private Style fixWithItalic(Style newStyle, Boolean boldBool){
		return keepData(newStyle);
	}

    @ModifyReturnValue(method = "withUnderline(Ljava/lang/Boolean;)Lnet/minecraft/text/Style;",
    at=@At("RETURN"))
	private Style fixWithUnderline(Style newStyle, Boolean boldBool){
		return keepData(newStyle);
	}

    @ModifyReturnValue(method = "withStrikethrough(Ljava/lang/Boolean;)Lnet/minecraft/text/Style;",
    at=@At("RETURN"))
	private Style fixWithStrikethrough(Style newStyle, Boolean boldBool){
		return keepData(newStyle);
	}

    @ModifyReturnValue(method = "withObfuscated(Ljava/lang/Boolean;)Lnet/minecraft/text/Style;",
    at=@At("RETURN"))
	private Style fixWithObfuscated(Style newStyle, Boolean boldBool){
		return keepData(newStyle);
	}

    @ModifyReturnValue(method = "withClickEvent(Lnet/minecraft/text/ClickEvent;)Lnet/minecraft/text/Style;",
    at=@At("RETURN"))
	private Style fixWithClickEvent(Style newStyle, ClickEvent clickEvent){
		return keepData(newStyle);
	}

    @ModifyReturnValue(method = "withHoverEvent(Lnet/minecraft/text/HoverEvent;)Lnet/minecraft/text/Style;",
    at=@At("RETURN"))
	private Style fixWithHoverEvent(Style newStyle, HoverEvent hoverEvent){
		return keepData(newStyle);
	}

    @ModifyReturnValue(method = "withInsertion(Ljava/lang/String;)Lnet/minecraft/text/Style;",
    at=@At("RETURN"))
	private Style fixWithInsertion(Style newStyle, String insertionString){
		return keepData(newStyle);
	}

    @ModifyReturnValue(method = "withFont(Lnet/minecraft/util/Identifier;)Lnet/minecraft/text/Style;",
    at=@At("RETURN"))
	private Style fixWithFont(Style newStyle, Identifier fontID){
		return keepData(newStyle);
	}

    @ModifyReturnValue(method = "withFormatting(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/Style;",
    at=@At("RETURN"))
	private Style fixWithFormatting(Style newStyle, Formatting formatting){
		return keepData(newStyle);
	}

    @ModifyReturnValue(method = "withExclusiveFormatting(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/Style;",
    at=@At("RETURN"))
	private Style fixWithExclusiveFormatting(Style newStyle, Formatting formatting){
		return keepData(newStyle);
	}

}


package com.samsthenerd.cobblecards.inline.renderers;

import com.samsthenerd.cobblecards.inline.Inline;

import net.minecraft.util.Identifier;

public class InlineErrorRenderer extends InlineItemRenderer{
    @Override
    public Identifier getId(){
        return new Identifier(Inline.MOD_ID, "error");
    }
}

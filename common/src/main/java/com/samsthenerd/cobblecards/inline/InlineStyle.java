package com.samsthenerd.cobblecards.inline;

import java.util.Map;

import net.minecraft.util.Identifier;

// duck interface to carry added style data
public interface InlineStyle {
    // hm - actually not sure that there's a case where we get multiple data in a single style ? 
    public Map<Identifier, InlineData> getInlineData();

    public void putInlineData(Identifier id, InlineData data);
}

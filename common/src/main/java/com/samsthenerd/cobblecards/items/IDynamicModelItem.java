package com.samsthenerd.cobblecards.items;

import com.samsthenerd.cobblecards.clientmisc.DynamicModelOverride;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;

public interface IDynamicModelItem {

    @Environment(EnvType.CLIENT)
    public DynamicModelOverride getModelIdentifier(ItemStack stack);
}

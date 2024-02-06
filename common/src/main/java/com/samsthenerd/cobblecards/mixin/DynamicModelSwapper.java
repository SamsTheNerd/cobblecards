package com.samsthenerd.cobblecards.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.samsthenerd.cobblecards.clientmisc.DynamicModelOverride;
import com.samsthenerd.cobblecards.items.IDynamicModelItem;

import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.item.ItemStack;

@Mixin(ItemModels.class)
public class DynamicModelSwapper {

    @Shadow
    @Final
    private BakedModelManager modelManager;

    @Inject(method="getModel(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/client/render/model/BakedModel;",
        at=@At("HEAD"), cancellable=true)
    private void getDynamicModel(ItemStack stack, CallbackInfoReturnable<BakedModel> info){
        if(stack.getItem() instanceof IDynamicModelItem dynamicModelItem){
            DynamicModelOverride override = dynamicModelItem.getModelIdentifier(stack);
            if(override == null) return;
            BakedModel model = override.getBakedModel(modelManager);
            if(model != null){
                info.setReturnValue(model);
            }
        }
    }
}

package com.samsthenerd.cobblecards.inline.renderers;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.samsthenerd.cobblecards.inline.Inline;
import com.samsthenerd.cobblecards.inline.InlineRenderer;
import com.samsthenerd.cobblecards.inline.data.ItemInlineData;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.world.World;

public class InlineItemRenderer implements InlineRenderer<ItemInlineData>{

    public static final InlineItemRenderer INSTANCE = new InlineItemRenderer();

    public Identifier getId(){
        return new Identifier(Inline.MOD_ID, "item");
    }

    public int render(ItemInlineData data, DrawContext context, int index, Style style, int codepoint, TextRenderingContext trContext){
        // only draw it once
        if(trContext.shadow){
            return 8;
        }
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.scale(0.5f, 0.5f, 0);
        // context.drawItem(data.getStack(), 0, 0);
        // matrices.pop();
        ItemStack stack = data.getStack();
        MinecraftClient client = MinecraftClient.getInstance();
        World world = client.world;

        if (stack.isEmpty()) {
            return 8;
        }
        BakedModel bakedModel = client.getItemRenderer().getModel(stack, world, null, 0);
        matrices.push();
        matrices.translate(8, 8, 150 + (bakedModel.hasDepth() ? 0 : 0));
        RenderSystem.enableDepthTest();
        try {
            boolean bl;
            matrices.multiplyPositionMatrix(new Matrix4f().scaling(1.0f, -1.0f, 1.0f));
            matrices.scale(16.0f, 16.0f, 16.0f);
            boolean bl2 = bl = !bakedModel.isSideLit();
            if (bl) {
                DiffuseLighting.disableGuiDepthLighting();
            }
            RenderSystem.enableDepthTest();
            RenderSystem.enableCull();
            client.getItemRenderer().renderItem(stack, ModelTransformationMode.GUI, false, matrices, context.getVertexConsumers(), trContext.light, OverlayTexture.DEFAULT_UV, bakedModel);
            context.draw();
            if (bl) {
                DiffuseLighting.enableGuiDepthLighting();
            }
        } catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.create(throwable, "Rendering item");
            CrashReportSection crashReportSection = crashReport.addElement("Item being rendered");
            crashReportSection.add("Item Type", () -> String.valueOf(stack.getItem()));
            crashReportSection.add("Item Damage", () -> String.valueOf(stack.getDamage()));
            crashReportSection.add("Item NBT", () -> String.valueOf(stack.getNbt()));
            crashReportSection.add("Item Foil", () -> String.valueOf(stack.hasGlint()));
            throw new CrashException(crashReport);
        }
        matrices.pop();
        matrices.pop();
        return 8;
    }

    public int charWidth(ItemInlineData data, Style style, int codepoint){
        return 8;
    }
}

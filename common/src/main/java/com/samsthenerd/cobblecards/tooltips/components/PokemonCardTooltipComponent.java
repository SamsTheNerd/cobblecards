package com.samsthenerd.cobblecards.tooltips.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.samsthenerd.cobblecards.CobbleCards;
import com.samsthenerd.cobblecards.pokedata.Card;
import com.samsthenerd.cobblecards.tooltips.data.PokemonCardTooltipData;
import com.samsthenerd.cobblecards.utils.URLTextureUtils;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class PokemonCardTooltipComponent implements TooltipComponent {

    private static final float RENDER_SIZE = 96f;
    public static final Identifier CARD_BACK = new Identifier(CobbleCards.MOD_ID, "textures/gui/card_back.png");

    private static final int lowWidth = 245;
    private static final int lowHeight = 342;

    private static final int highWidth = 734;
    private static final int highHeight = 1021;

    private Card card;

    public PokemonCardTooltipComponent(PokemonCardTooltipData tt) {
        this.card = tt.card();
    }

    @Override
    public void drawItems(TextRenderer font, int mouseX, int mouseY, DrawContext context) {
        MatrixStack ps = context.getMatrices();
        ps.push();
        ps.translate(mouseX, mouseY, 500);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        Identifier cardID = new Identifier("cobblecards", "pokecard/" + card.fullId());
        Identifier cardTextureId = URLTextureUtils.loadTextureFromURL(card.getImageUrl(true), cardID);
        if(cardTextureId == null || cardTextureId.equals(new Identifier(""))){
            cardTextureId = CARD_BACK;
        }
        ps.scale(RENDER_SIZE / lowWidth, RENDER_SIZE / lowWidth, 1f);
        context.drawTexture(cardTextureId, 0, 0, 0, 0, 0, lowWidth, lowHeight, lowWidth, lowHeight);
        ps.pop();
    }

    @Override
    public int getWidth(TextRenderer pFont) {
        return (int) RENDER_SIZE;
    }

    @Override
    public int getHeight() {
        return (int)(RENDER_SIZE * ((double)lowHeight)/lowWidth);
    }
}
